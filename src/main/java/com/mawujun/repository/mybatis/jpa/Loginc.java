package com.mawujun.repository.mybatis.jpa;

public class Loginc {
	private String name;
	private Object defaultValue;//未删除的时候的值
	private Object deleteValue;//逻辑删除后的值
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Object getDeleteValue() {
		return deleteValue;
	}
	public void setDeleteValue(Object deleteValue) {
		this.deleteValue = deleteValue;
	}
}
