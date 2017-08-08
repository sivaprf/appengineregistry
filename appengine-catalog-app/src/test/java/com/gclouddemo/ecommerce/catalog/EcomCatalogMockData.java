/**
 * 
 */
package com.gclouddemo.ecommerce.catalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gclouddemo.ecommerce.catalog.bean.CatalogItem;

/**
 *
 */
public class EcomCatalogMockData {
	
	private static final CatalogItem testItem01 = new CatalogItem(
			123,
			"Google Women's Short Sleeve Hero Tee Black",
			"100% cotton jersey fabric makes this Google t-shirt perfect for the Google hero in your life. Made in USA.",
			new BigDecimal("19.99"),
			"Women’s Apparel",
			"T-Shirts",
			"100% Cotton\n30 Singles jersey\nCrew neck\nMade in USA",
			"https://storage.googleapis.com/wombat-171617-ecimgs/women_tshirt.jpg",
			"https://storage.googleapis.com/wombat-171617-ecimgs/img01-thumb.jpg"
		);
	
	public CatalogItem getTestItem01() {
		return testItem01;
	}
	
	public List<CatalogItem> getItemList() {
		List<CatalogItem> items = new ArrayList<CatalogItem>();
		items.add(testItem01);
		return items;
	}
}
