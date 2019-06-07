package com.mawujun.repository.utils;

import java.util.Map;

/**
 * update的时候set的等式
 * @author mawujun
 *
 */
public interface IUpdate {
	/**
	 * 更新的字段
	 * @param field
	 * @param value
	 */
	public Cnd update(String field,Object value);
	/**
	 * 更新所有的字段
	 * @param field
	 * @param value
	 */
	public Cnd update(Map<String,Object> sets);
}
