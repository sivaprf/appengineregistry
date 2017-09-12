/**
 * 
 */
package com.gclouddemo.ecommerce.catalog.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gclouddemo.ecommerce.catalog.common.EcomCatalogConnection;
import com.gclouddemo.ecommerce.catalog.common.KmsHelper;
import com.gclouddemo.ecommerce.catalog.common.bean.CatalogItem;
import com.gclouddemo.ecommerce.catalog.common.cloudsql.EcomCatalogCloudSql;
import com.google.appengine.api.utils.SystemProperty;

import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.BUCKET_NAME_PROP_NAME;
import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.CLIENT_NAME;
import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.KEY_NAME_PROP;
import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.KEY_PROJECT_NAME;
import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.KEYRING_NAME_PROP;
import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.OBJECT_NAME_PROP_NAME;

import static com.google.common.net.MediaType.JSON_UTF_8;

/**
 *
 */
public class EcomCatalogAdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(EcomCatalogAdminServlet.class.getName());

    private static final String MIME_TYPE_JSON = JSON_UTF_8.toString();
    
    private static final String CONNECTION_TYPE_CLOUDSQL = "cloudsql";
    private static final String CONNECTION_TYPE_TEST = "test";
    
    private static final String INSERT_FAILURE_JSON = "{\"status\":\"insert failed\"}";
    
    private static final String INSERT_PATH = "insert";
        
    private String sqlPwd = null;
    private boolean useSql = true;
    
    public EcomCatalogAdminServlet() {
    	try {
    		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
    			// Retrieve the MySQL password from the cloud storage object:
        		
    			String base64Str = KmsHelper.getEncryptedPasswordFromBucket(
    											System.getProperty(BUCKET_NAME_PROP_NAME),
    											System.getProperty(OBJECT_NAME_PROP_NAME));
    			this.sqlPwd = KmsHelper.decryptString(CLIENT_NAME, System.getProperty(KEY_PROJECT_NAME, "none"),
    											System.getProperty(KEYRING_NAME_PROP, "none"),
    											System.getProperty(KEY_NAME_PROP, "none"),
    											base64Str);
    		}
    	} catch (Throwable thr) {
    		LOG.log(Level.SEVERE, thr.getLocalizedMessage(), thr);
    	}
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			sendJson(response, "{\"status\":\"still alive!!\"}");
		} catch (Exception e) {
			LOG.severe(e.getLocalizedMessage());
		}
	}
	
	private void sendJson(HttpServletResponse response, String jsonStr) throws Exception {		
		sendBody(response, jsonStr, MIME_TYPE_JSON);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EcomCatalogConnection conn = null;
		
		String jsonPayload = getJsonPayload(request);
		
		String path = request.getRequestURI();
		if (path != null) {
			if (path.endsWith(INSERT_PATH) && jsonPayload != null) {
				try {
	
					CatalogItem catalogItem = CatalogItem.makeItemFromJson(jsonPayload);
	
				    if (useSql) {
						String url = EcomCatalogCloudSql.makeConnectionUrl(sqlPwd);
						conn = getConnection(CONNECTION_TYPE_CLOUDSQL, url);
						conn.open();
						String insertResult = insertItem(conn, catalogItem);	
						sendJson(response, insertResult);
					} else {
						// do the datastore version...
					}
				} catch (Throwable thr) {
					LOG.severe(thr.getLocalizedMessage());
				} finally {
					if (conn != null) {
						conn.close();
					}
			        
			    }
			}
		}
	}
	
	private String getJsonPayload(HttpServletRequest request) throws IOException {
		StringBuilder strBuilder = new StringBuilder();
	    BufferedReader bufferedReader = request.getReader();
		try {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
			    strBuilder.append(line);
			}
		    return strBuilder.toString();
		} catch (Throwable thr) {
			LOG.severe(thr.getLocalizedMessage());
		} finally {
	        if (bufferedReader != null) {
	            try {
	            	bufferedReader.close();
	            } catch (IOException ex) {
	                // Don't need to report -- HR.
	            }
	        }
		}
		
		return null;
	}
	
	private EcomCatalogConnection getConnection(String type, String url) {
		if (CONNECTION_TYPE_CLOUDSQL.equalsIgnoreCase(type)) {
			return new EcomCatalogCloudSql(url);
		} else if (CONNECTION_TYPE_TEST.equalsIgnoreCase(type)) {
			return new EcomCatalogCloudSql(null);
		}
		
		return null;
	}
	
	private String insertItem(EcomCatalogConnection conn, CatalogItem item) throws Exception {
		if (conn != null && item != null) {
			boolean succeeded = conn.insertItem(item);
			if (succeeded) {
				return "{\"status\":\"successfully inserted item SKU "
						+ (item.getSku() == null ? "<null>" : item.getSku())
						+ "\"}";
			}
		}
		
		return INSERT_FAILURE_JSON;
	}
	
	private void sendBody(HttpServletResponse response, String bodyStr, String mimeType) throws Exception {
		response.setStatus(HttpURLConnection.HTTP_OK);
		response.setContentType(mimeType);
		PrintWriter responseWriter = response.getWriter();
		if (bodyStr != null) {
			responseWriter.println(bodyStr);
		}
		
		responseWriter.close();
	}
}
