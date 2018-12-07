package test.mawujun.jpa.utils;
public final class M {
public static final class City {
	public static final String name="name";
	public static final String age="age";
	public static final String price="price";
	public static final String createDate="createDate";
	public static final String sex="sex";
	public static final String id="id";
}
public static final class CoplxId1Entity {
	 /**
	 * 返回复合主键的组成，，以对象关联的方式:id
	 */
	public static final class idClass {
		public static final String id1="id.id1";
		public static final String id2="id.id2";
			
	    /**
	    * 返回的是复合主键类的名称
	    */
	    public static String name(){ 
		    return "CoplxId1";
	    }
	}
	/**
	* 这是一个复合主键，返回的是该复合主键的属性名称，在hql中使用:id
	*/
	public static final String id="id";
	public static final String name="name";
}
public static final class CoplxId2Entity {
	public static final String id1="id1";
	public static final String id2="id2";
	public static final String name="name";
}
}
