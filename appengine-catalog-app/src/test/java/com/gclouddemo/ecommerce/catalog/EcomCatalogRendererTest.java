/**
 * 
 */
package com.gclouddemo.ecommerce.catalog;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

import com.gclouddemo.ecommerce.catalog.bean.CatalogItem;
import com.gclouddemo.ecommerce.catalog.renderer.EcomCatalogRenderer;
import com.gclouddemo.ecommerce.catalog.renderer.EcomJsonRenderer;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 *
 */

@RunWith(JUnit4.class)
public class EcomCatalogRendererTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper();
	
	private EcomCatalogRenderer renderer;
    private EcomCatalogMockData mockData = new EcomCatalogMockData();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	    helper.setUp();
	    
	    renderer = new EcomJsonRenderer(true);
	}
	
	@After
	public void tearDown() {
		helper.tearDown();
	}
	
	@Test
	public void doTestJsonListRenderer() throws Exception {
		List<CatalogItem> itemList = this.mockData.getItemList();
		
		assertThat(itemList).isNotNull();
		assertThat(itemList.size()).isGreaterThan(0);
		String renderedStr = renderer.renderItemList(itemList, null, null);
		assertThat(renderedStr).isNotNull();
		assertThat(renderedStr.length()).isGreaterThan(0);
		List<CatalogItem> items = new Gson().fromJson(renderedStr, new TypeToken<List<CatalogItem>>(){}.getType());
		assertThat(items).isNotNull();
		assertThat(items.size()).isEqualTo(itemList.size());
		assertThat(compare(items.get(0), itemList.get(0))).isTrue();
	}
	
	private boolean compare(CatalogItem item01, CatalogItem item02) {
		if (item01 != null && item02 != null) {
			if (!safeCompare(item01.getCategory(), item02.getCategory())) {
				return false;
			}
			if (!safeCompare(item01.getSubcategory(), item02.getSubcategory())) {
				return false;
			}
			if (!safeCompare(item01.getDescription(), item02.getDescription())) {
				return false;
			}
			if (!safeCompare(item01.getSummary(), item02.getSummary())) {
				return false;
			}
			if (!safeCompare(item01.getPrice().toPlainString(), item02.getPrice().toPlainString())) {
				return false;
			}
			if (!safeCompare(item01.getDetails(), item02.getDetails())) {
				return false;
			}
			if (!safeCompare(item01.getImage(), item02.getImage())) {
				return false;
			}
			if (!safeCompare(item01.getThumb(), item02.getThumb())) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean safeCompare(String str1, String str2) {
		if (str1 != null && str2 != null || str1.equals(str2)) {
			return true;
		}
		return false;
	}
}
