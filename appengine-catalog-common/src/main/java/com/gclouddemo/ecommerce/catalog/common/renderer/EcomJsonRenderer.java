/**
 * 
 */
package com.gclouddemo.ecommerce.catalog.common.renderer;

import java.util.List;

import com.gclouddemo.ecommerce.catalog.common.bean.CatalogItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 */
public class EcomJsonRenderer implements EcomCatalogRenderer {
	
	private static final Gson gson = new Gson();
	private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
	
	private boolean prettyPrint = false;
	
	public EcomJsonRenderer(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	@Override
	public String renderItemList(List<CatalogItem> items, String prefixStr, String suffixStr) {
		if (items != null) {
			return (this.prettyPrint ? gsonPretty.toJson(items) : gson.toJson(items));
		}
		
		return null;
	}

	@Override
	public String renderCatalogItem(CatalogItem item) {
		if (item != null) {
			return (this.prettyPrint ? gsonPretty.toJson(item) : gson.toJson(item));
		}
		return null;
	}


}
