package com.mawujun.repository.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 里面存的方法，都是分页方法
 * @author mawujun
 *
 */
public class PageMethodCache {
	public static Set<String> methodcache=new HashSet<String>();
	
	public static void add(String method) {
		methodcache.add(method);
	}
	
	public static boolean exits(String method) {
		return methodcache.contains(method);
	}

}
