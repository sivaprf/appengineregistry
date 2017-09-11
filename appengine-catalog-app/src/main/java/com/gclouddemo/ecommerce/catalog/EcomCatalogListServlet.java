/**
 * 
 */
package com.gclouddemo.ecommerce.catalog;

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

import com.gclouddemo.ecommerce.catalog.common.EcomCatalogConnection;
import com.gclouddemo.ecommerce.catalog.common.KmsHelper;
import com.gclouddemo.ecommerce.catalog.common.bean.CatalogItem;
import com.gclouddemo.ecommerce.catalog.common.cloudsql.EcomCatalogCloudSql;
import com.gclouddemo.ecommerce.catalog.common.renderer.EcomJsonRenderer;
import com.google.appengine.api.utils.SystemProperty;

import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.BUCKET_NAME_PROP_NAME;
import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.CLIENT_NAME;
import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.KEY_NAME_PROP;
import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.KEY_PROJECT_NAME;
import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.KEYRING_NAME_PROP;
import static com.gclouddemo.ecommerce.catalog.common.KmsHelper.OBJECT_NAME_PROP_NAME;

import static com.google.common.net.MediaType.JSON_UTF_8;

/**
 * Catalog app App Engine listing servlet REST call implementer class.
 */
public class EcomCatalogListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(EcomCatalogListServlet.class.getName());
	    
    private static final String CONNECTION_TYPE_CLOUDSQL = "cloudsql";
    private static final String CONNECTION_TYPE_TEST = "test";
    
    private static final String MIME_TYPE_JSON = JSON_UTF_8.toString();
    
    private static final String PARAM_CATEGORY_NAME = "c";
    private static final String PARAM_SUBCATEGORY_NAME = "s";
    private static final String PARAM_SKU_NAME = "k";
    private static final String PARAM_PRETTYPRINT_NAME = "p";
    
    private String sqlPwd = null;
    private boolean useSql = true;
    
    public EcomCatalogListServlet() {
    	super();
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
