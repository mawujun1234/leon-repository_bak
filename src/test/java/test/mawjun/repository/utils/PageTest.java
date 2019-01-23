package test.mawjun.repository.utils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.mawujun.repository.utils.Page;





public class PageTest {
	
	@Test
	public void of() {
		Page page=Page.of(0, 10);
		Assert.assertEquals(0, page.getStart());
		Assert.assertEquals(10, page.getLimit());
		Assert.assertEquals(1, page.getPage());
		
		page=Page.of(10, 10);
		Assert.assertEquals(10, page.getStart());
		Assert.assertEquals(10, page.getLimit());
		Assert.assertEquals(2, page.getPage());
		
	}
	
	@Test
	public void of_1() {
		Page page=Page.of_1(1, 10);
		Assert.assertEquals(0, page.getStart());
		Assert.assertEquals(10, page.getLimit());
		Assert.assertEquals(1, page.getPage());
		
		page=Page.of_1(2, 10);
		Assert.assertEquals(10, page.getStart());
		Assert.assertEquals(10, page.getLimit());
		Assert.assertEquals(2, page.getPage());
		
	}
	


}
