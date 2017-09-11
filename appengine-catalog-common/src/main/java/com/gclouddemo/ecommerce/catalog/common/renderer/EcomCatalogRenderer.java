/**
 * 
 */
package com.gclouddemo.ecommerce.catalog.common.renderer;

import java.util.List;

import com.gclouddemo.ecommerce.catalog.common.bean.CatalogItem;

/**
 * Render items and item lists into JSON or HTML, etc.
 */
public interface EcomCatalogRenderer {
	String renderItemList(List<CatalogItem> items, String prefixStr, String suffixStr);
	String renderCatalogItem(CatalogItem item);
}
