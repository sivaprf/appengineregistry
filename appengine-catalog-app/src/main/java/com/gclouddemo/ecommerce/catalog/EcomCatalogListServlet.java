/**
 * 
 */
package com.gclouddemo.ecommerce.catalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gclouddemo.ecommerce.catalog.bean.CatalogItem;
import com.gclouddemo.ecommerce.catalog.cloudsql.EcomCatalogCloudSql;
import com.gclouddemo.ecommerce.catalog.renderer.EcomJsonRenderer;
import com.google.gson.Gson;

/**
 * Catalog app App Engine listing servlet REST call implementer.
 */
public class EcomCatalogListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(EcomCatalogListServlet.class.getName());
	    
    private static final String CONNECTION_TYPE_CLOUDSQL = "cloudsql";
    private static final String CONNECTION_TYPE_TEST = "test";
    
    private static final String MIME_TYPE_JSON = "application/json";
    
    private static final String PARAM_CATEGORY_NAME = "c";
    private static final String PARAM_SUBCATEGORY_NAME = "s";
    private static final String PARAM_SKU_NAME = "k";
    private static final String PARAM_PRETTYPRINT_NAME = "p";
    
    private static final String CLIENT_NAME = "GCloud Catalog App Engine Example";
    private static final String KEY_PROJECT_NAME = "keyring-project-name";
    
    private static final String KEYRING_NAME_PROP = "keyring-name";
    private static final String KEY_NAME_PROP = "key-name";
    
    private static final String BUCKET_NAME_PROP_NAME = "mysql-pwd-bucketname";
    private static final String OBJECT_NAME_PROP_NAME = "mysql-pwd-objectname";
    
    private String sqlPwd = null;
    private boolean useSql = true;
    private static final Gson gson = new Gson();
       	
    public EcomCatalogListServlet() {
    	super();
    	try {
    		// Retrieve the MySQL password from the cloud storage object:
    		
			String base64Str = KmsHelper.getEncryptedPasswordFromBucket(
											System.getProperty(BUCKET_NAME_PROP_NAME),
											System.getProperty(OBJECT_NAME_PROP_NAME));
			this.sqlPwd = KmsHelper.decryptString(CLIENT_NAME, System.getProperty(KEY_PROJECT_NAME, "none"),
											System.getProperty(KEYRING_NAME_PROP, "none"),
											System.getProperty(KEY_NAME_PROP, "none"),
											base64Str);
    	} catch (Throwable thr) {
    		LOG.log(Level.SEVERE, thr.getLocalizedMessage(), thr);
    	}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		EcomCatalogConnection conn = null;
		String categoryName = request.getParameter(PARAM_CATEGORY_NAME);
		String subCategoryName = request.getParameter(PARAM_SUBCATEGORY_NAME);
		String sku = request.getParameter(PARAM_SKU_NAME);
		boolean prettyPrint = new Boolean(request.getParameter(PARAM_PRETTYPRINT_NAME));
		
		try {

			if (useSql) {
				String url = EcomCatalogCloudSql.makeConnectionUrl(sqlPwd);
				conn = getConnection(CONNECTION_TYPE_CLOUDSQL, url);
			} else {
				// do the datastore version...
			}

			if (conn != null) {
				if (sku != null) {
					sendJson(response, new EcomJsonRenderer(prettyPrint).renderCatalogItem(getItem(conn, sku)));
				} else {
					sendJson(response,
							new EcomJsonRenderer(prettyPrint).renderItemList(
									getItemList(conn, categoryName, subCategoryName), null, null));
				}
			}	
		} catch (Throwable thr) {
			LOG.log(Level.SEVERE, thr.getLocalizedMessage(), thr);
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		EcomCatalogConnection conn = null;
		try {
			String jasonString = null;   
		    StringBuilder builder = new StringBuilder();
		    BufferedReader reader = request.getReader();
		    String line;
		    while ((line = reader.readLine()) != null) {
		        builder.append(line);
		    }
		    jasonString = builder.toString();
		    LOG.info(jasonString);
		    CatalogItem catalogItem = gson.fromJson(jasonString, CatalogItem.class);
		    LOG.info("item: " + catalogItem);
		    
		    if (useSql) {
				String url = EcomCatalogCloudSql.makeConnectionUrl(sqlPwd);
				conn = getConnection(CONNECTION_TYPE_CLOUDSQL, url);
			} else {
				// do the datastore version...
			}
		    
		    if (conn != null) {
		    	
		    }
		} catch (Throwable thr) {
			LOG.log(Level.SEVERE, thr.getLocalizedMessage());
			throw thr;
		} finally {
			
		}
	}
	
	private EcomCatalogConnection getConnection(String type, String url) {
		if (CONNECTION_TYPE_CLOUDSQL.equalsIgnoreCase(type)) {
			return new EcomCatalogCloudSql(url);
		} else if (CONNECTION_TYPE_TEST.equalsIgnoreCase(type)) {
			return new EcomCatalogCloudSql(null);
		}
		
		return null;
	}
	
	private List<CatalogItem> getItemList(EcomCatalogConnection conn, String category, String subCategory) throws Exception {
		try {
			if (conn != null) {
				conn.open();
				return conn.listItems(category, subCategory);
			} else  {
				LOG.log(Level.SEVERE, "Unable to connect to ecommerce catalog");
				return null;
			}
		} catch (Throwable thr) {
			LOG.log(Level.SEVERE, thr.getLocalizedMessage(), thr);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}
	
	private CatalogItem getItem(EcomCatalogConnection conn, String sku) throws Exception {
		try {
			if (conn != null) {
				conn.open();
				return conn.getSku(sku);
			} else  {
				LOG.log(Level.SEVERE, "Unable to connect to ecommerce catalog");
				return null;
			}
		} catch (Throwable thr) {
			LOG.log(Level.SEVERE, thr.getLocalizedMessage(), thr);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}
	
	private void sendJson(HttpServletResponse response, String jsonStr) throws Exception {		
		sendBody(response, jsonStr, MIME_TYPE_JSON);
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
