/**
 * 
 */
package com.gclouddemo.ecommerce.catalog.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.Base64;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.cloudkms.v1.CloudKMS;
import com.google.api.services.cloudkms.v1.CloudKMSScopes;
import com.google.api.services.cloudkms.v1.model.DecryptRequest;
import com.google.api.services.cloudkms.v1.model.DecryptResponse;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

/**
 * Helper class to do the underlying KMS-based encryption and decryption.
 * <p>
 * Largely copied from the original GCP KMS secrets document, but with some features
 * changed as discussed below.
 */
public class KmsHelper {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(KmsHelper.class.getName());
	
	private static final String LOCATION = "global";
	private static final String KEY_RES_NAME_FMT_STR = "projects/%s/locations/%s/keyRings/%s/cryptoKeys/%s";
	
    public static final String CLIENT_NAME = "GCloud Catalog App Engine Example";
    public static final String KEY_PROJECT_NAME = "keyring-project-name";
    
    public static final String KEYRING_NAME_PROP = "keyring-name";
    public static final String KEY_NAME_PROP = "key-name";
    
    public static final String BUCKET_NAME_PROP_NAME = "mysql-pwd-bucketname";
    public static final String OBJECT_NAME_PROP_NAME = "mysql-pwd-objectname";
	
	// BUFFER_SIZE doesn't actually need to be big here -- we're only getting the password (HR)
	private static final int BUFFER_SIZE = 1024; 
	
	public static CloudKMS createAuthorizedClient(String clientName) throws IOException {
	    
		// Create the credential
	    HttpTransport transport = new NetHttpTransport();
	    JsonFactory jsonFactory = new JacksonFactory();
	    
	    // Authorize the client using Application Default Credentials
	    // @see https://g.co/dv/identity/protocols/application-default-credentials
	    GoogleCredential credential = GoogleCredential.getApplicationDefault(transport, jsonFactory);

	    // Depending on the environment that provides the default credentials (e.g. Compute Engine, App
	    // Engine), the credentials may require us to specify the scopes we need explicitly.
	    // Check for this case, and inject the scope if required.
	    
	    if (credential.createScopedRequired()) {
	    	credential = credential.createScoped(CloudKMSScopes.all());
	    }

	    return new CloudKMS.Builder(transport, jsonFactory, credential)
	        .setApplicationName(clientName)
	        .build();
	  }
	
	/**
	 * Take a base64-encoded encrypted string and decrypt it to the (non-base64) original.
	 */
	
	public static String decryptString(String clientName, String projectId, String ringId,
											String keyId, String encrypted) throws IOException {

		if (encrypted != null) {
			CloudKMS kms = createAuthorizedClient(clientName);
		
			String cryptoKeyName = String.format(KEY_RES_NAME_FMT_STR,
													projectId, LOCATION, ringId, keyId);
			
			byte[] bytes = Base64.getDecoder().decode(encrypted);
			DecryptRequest request = new DecryptRequest().encodeCiphertext(bytes);
			if (request != null) {
				DecryptResponse response = kms.projects().locations().keyRings().cryptoKeys()
				          .decrypt(cryptoKeyName, request)
				          .execute();
				
				if (response != null) {
					// Note the trim() below due to the decoder adding a newline -- HR.
					return new String(response.decodePlaintext()).trim();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Get a string from a bucket; intended to be used to retrieve an encrypted password string,
	 * but has more general uses. Not intended for massive data retrieval...
	 */
	public static String getEncryptedPasswordFromBucket(String bucketName, String objectName) throws Exception {
	    final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
	    	      .initialRetryDelayMillis(10)
	    	      .retryMaxAttempts(10)
	    	      .totalRetryPeriodMillis(15000)
	    	      .build());
	    
		if (objectName != null &&  bucketName != null) {
			GcsFilename gcsFilename = new GcsFilename(bucketName, objectName);
			GcsInputChannel readChannel = gcsService.openReadChannel(gcsFilename, 0);
			InputStream input = null;
			OutputStream output = null;
			if (readChannel != null) {
				try {
					output = new ByteArrayOutputStream();
					input = Channels.newInputStream(readChannel);
					byte[] buffer = new byte[BUFFER_SIZE];
					int bytesRead = input.read(buffer);
					while (bytesRead != -1) {
					    output.write(buffer, 0, bytesRead);
					    bytesRead = input.read(buffer);
					}
					String pwd = output.toString();
					if (pwd != null) {
						return pwd.trim();
					}
			    } finally {
			      input.close();
			      output.close();
				}
			}
		}
		
		return null;
	}
}
