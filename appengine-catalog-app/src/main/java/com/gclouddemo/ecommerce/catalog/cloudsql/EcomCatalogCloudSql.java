/**
 * 
 */
package com.gclouddemo.ecommerce.catalog.cloudsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gclouddemo.ecommerce.catalog.EcomCatalogConnection;
import com.gclouddemo.ecommerce.catalog.bean.CatalogItem;
import com.google.apphosting.api.ApiProxy;

/**
 *
 */
public class EcomCatalogCloudSql implements EcomCatalogConnection {
	
	private static final Logger LOG = Logger.getLogger(EcomCatalogCloudSql.class.getName());
	
	/** SKU used to flag non-display (test) entries */
	private static final String TEST_SKU = "ZZ-0000-Z";
	
	private static final String PRODUCTS_TABLE_NAME = "gclouddemo_catalog.products";
	
	private static final String LIST_QUERY_ALL_STR = "SELECT * FROM " + PRODUCTS_TABLE_NAME;
	private static final String LIST_QUERY_CAT_STR =
			"SELECT * FROM " + PRODUCTS_TABLE_NAME + " WHERE category=?";
	private static final String LIST_QUERY_CAT_SUBCAT_STR =
			"SELECT * FROM " + PRODUCTS_TABLE_NAME + " WHERE category=? AND subcategory=?";
	
	private static final String SKU_ITEM_QUERY_STR =
			"SELECT * FROM " + PRODUCTS_TABLE_NAME + " WHERE sku=?";
	
	private static final String ITEM_INSERT_QUERY = "INSERT INTO " + PRODUCTS_TABLE_NAME
			+ " VALUES (summary, sku, description, price, thumb, image, category, subcategory, details)"
			+ "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final int PRODUCTS_ID_COL = 1;
	private static final int PRODUCTS_SUMMARY_COL = 2;
	private static final int PRODUCTS_SKU_COL = 3;
	private static final int PRODUCTS_DESCRIPTION_COL = 4;
	private static final int PRODUCTS_PRICE_COL = 5;
	private static final int PRODUCTS_THUMB_COL = 6;
	private static final int PRODUCTS_IMAGE_COL = 7;
	private static final int PRODUCTS_CATEGORY_COL = 9;
	private static final int PRODUCTS_SUBCATEGORY_COL = 10;
	private static final int PRODUCTS_DETAILS_COL = 11;
	
    private static final String HOSTNAME_PROP_NAME = "com.google.appengine.runtime.default_version_hostname";
    
    private static final String LOCAL_CONN_PROP_NAME = "cloudsql-local";
    private static final String DEPLOYED_CONN_PROP_NAME = "cloudsql-deployed";

	private final String url;
	private Connection conn = null;
	
	public static String makeConnectionUrl(String pwd) {

		ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
		Map<String, Object> attr = env.getAttributes();
		String hostName = (String) attr.get(HOSTNAME_PROP_NAME);
		
		if (hostName != null && !hostName.isEmpty()) {
			if (!hostName.contains("localhost")) {
				String urlTemplate = System.getProperty(DEPLOYED_CONN_PROP_NAME);
				if (urlTemplate != null) {
					return urlTemplate + (pwd == null ? "" : pwd);
				}
			}
		} else {
			return System.getProperty(LOCAL_CONN_PROP_NAME);
		}
		
		return null;
	}
	
	public EcomCatalogCloudSql(String url) {
		this.url = url;
	}

	@Override
	public boolean open() throws Exception {
		if (this.url != null) {
			this.conn = DriverManager.getConnection(url);
		}
		
		return true;
	}

	@Override
	public void close() {
		if (this.conn != null) {
			try {
				if (!this.conn.isClosed()) {
					conn.close();
				}
			} catch (Throwable thr) {
				LOG.log(Level.SEVERE, thr.getLocalizedMessage(), thr);
			}
		}
	}

	@Override
	public List<CatalogItem> listItems(String category, String subCategory) throws Exception {
		List<CatalogItem> items = new ArrayList<CatalogItem>();
		ResultSet rs = null;
		PreparedStatement prep = null;
		
		try {
			if (conn != null) {
				
				if (category == null) {
					prep = conn.prepareStatement(LIST_QUERY_ALL_STR);
				} else if (subCategory == null) {
					prep = conn.prepareStatement(LIST_QUERY_CAT_STR);
					prep.setString(1, category);
				} else {
					prep = conn.prepareStatement(LIST_QUERY_CAT_SUBCAT_STR);
					prep.setString(1, category);
					prep.setString(2, subCategory);
				}
				
				rs = prep.executeQuery();
				
				if (rs != null) {
					while (rs.next()) {
						CatalogItem item = constructItem(rs);
						if (item != null && !TEST_SKU.equalsIgnoreCase(item.getSku())) {
							items.add(constructItem(rs));
						}
					}
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			
			if (prep != null) {
				prep.close();
			}
		}
		
		return items;
	}
	

	@Override
	public boolean insertItem(CatalogItem catalogItem) throws Exception {
		PreparedStatement preparedStatement = null;
		try {
			if (conn != null && catalogItem != null) {
				preparedStatement = conn.prepareStatement(ITEM_INSERT_QUERY);
				preparedStatement.setString(1, catalogItem.getSummary());
				preparedStatement.setString(2, catalogItem.getSku());
				preparedStatement.setString(3, catalogItem.getDescription());
				preparedStatement.setBigDecimal(4, catalogItem.getPrice());
				preparedStatement.setString(5, catalogItem.getThumb());
				preparedStatement.setString(6, catalogItem.getImage());
				preparedStatement.setString(7, catalogItem.getCategory());
				preparedStatement.setString(8, catalogItem.getSubcategory());
				preparedStatement.setString(9, catalogItem.getDetails());
				
				preparedStatement.execute();
				return true;
			}
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
		
		return false;
	}
	
	private CatalogItem constructItem(ResultSet rs) throws SQLException {
		CatalogItem item = new CatalogItem();
		
		if (rs != null) {
			item.setId(rs.getInt(PRODUCTS_ID_COL));
			item.setSummary(rs.getString(PRODUCTS_SUMMARY_COL));
			item.setSku(rs.getString(PRODUCTS_SKU_COL));
			item.setDescription(rs.getString(PRODUCTS_DESCRIPTION_COL));
			item.setPrice(rs.getBigDecimal(PRODUCTS_PRICE_COL));
			item.setThumb(rs.getString(PRODUCTS_THUMB_COL));
			item.setImage(rs.getString(PRODUCTS_IMAGE_COL));
			item.setCategory(rs.getString(PRODUCTS_CATEGORY_COL));
			item.setSubcategory(rs.getString(PRODUCTS_SUBCATEGORY_COL));
			item.setDetails(rs.getString(PRODUCTS_DETAILS_COL));
		}
		
		return item;
	}

	@Override
	public List<CatalogItem> searchItems(String searchString) throws Exception {
		return null;
	}

	@Override
	public CatalogItem getSku(String sku) throws Exception {
		if (conn != null && sku != null) {
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try {
				preparedStatement = conn.prepareStatement(SKU_ITEM_QUERY_STR);
				preparedStatement.setString(1, sku);
				rs = preparedStatement.executeQuery();
				if (rs.next()) {
					// Only want the first one here -- HR.
					return constructItem(rs);
				}
			} finally {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}
		}
		
		return null;
	}
}
