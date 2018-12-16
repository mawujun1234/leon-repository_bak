package test.mawujun.jpa.utils;
public final class M {
	/**
	* 对应的类名是：test.mawujun.model.City 
	* 对应的表名是：t_city 
	* 对应的表注释是：表注释 
	* 
	*/
public static final class City {
	/**
	*名称 
	* 
	*/
	public static final String name="name";
	/**
	*年龄 
	* 默认值是:18 
	* 
	*/
	public static final String age="age";
	/**
	*价格 
	* 默认值是:0.0 
	* 
	*/
	public static final String price="price";
	/**
	*创建时间 
	* 
	*/
	public static final String createDate="createDate";
	/**
	*创建时间 
	* 默认值是:Man 
	* 
	*/
	public static final String sex="sex";
	/**
	*主键id 
	* 
	*/
	public static final String id="id";
}
	/**
	* 对应的类名是：test.mawujun.model.CoplxId1Entity 
	* 对应的表名是：t_coplxid1entity 
	* 
	*/
public static final class CoplxId1Entity {
	 /**
	 * 对复合主键的embeddedId的属性访问，返回值为id.id1,id.id2的形式
	 */
	public static final class id {
	/**
	*id1注释 
	* 
	*/
		public static final String id1="id.id1";
	/**
	*id2注释 
	* 
	*/
		public static final String id2="id.id2";
	/**
	*idAaaaa注释 
	* 
	*/
		public static final String idAaaaa="id.idAaaaa";
			
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
	/**
	* 
	*/
	public static final String name="name";
}
	/**
	* 对应的类名是：test.mawujun.model.CoplxId2Entity 
	* 对应的表名是：t_coplxid2entity 
	* 
	*/
public static final class CoplxId2Entity {
	/**
	*id1注释0000 
	* 
	*/
	public static final String id1="id1";
	/**
	* 
	*/
	public static final String id2="id2";
	/**
	* 
	*/
	public static final String name="name";
}
}
