package com.mawujun.repository.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.mawujun.exception.BizException;
import com.mawujun.util.StringUtil;

/**
 * 继承List的原因是mybatis的接口只能返回list，继承list就可以以Page的形式返回了
 * 在mybatis中分页拦截的时候，必须是要参数为PageInfo才行
 * @author mawujun
 *
 * @param <T>
 */
public class Page<T> implements List<T>{//,ICondition
	
	protected int page = -1;//当前第几页，第一页默认是1
	protected int limit = 50;// 默认是每页50条
	protected int start = 0;//第一行默认是0
	protected int total = -1;//总共有几条记录
	//private Boolean success=true;
	//private String message;
	
	//private String countColumn;
	protected List<T> root = null;

//	// 这个优先级较高，如果设置了这个值，传递到后台的话，就使用这个值作为参数
//	// mybatis时候用的较多
//	protected Object params;// 具体的参数形式，可能是Map也可能是Bean
	
	//protected List result = null;
	//protected int total = -1;
	/**
	 * PageParam.getInstance().setPageSize(1).setStart(1).setParams("");
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @return
	 */
//	public static Pager<T> getInstance(){
//		
//		return new Pager<T>();
//	}
//	public static Pager<T> getInstance(int start,int limit){
//		Pager<T> param = new Pager<T>();
//		param.setStart(start);
//		param.setLimit(limit);
//		return param;
//	}
	/**
	 * 
	 * @param page 第几页,第一页是1
	 * @param limit 每页多少
	 * @return
	 */
	public static Page<Object> of_1(int page,int limit){
		if(page<1) {
			throw new IllegalArgumentException("分页的页码是从1开始");
		}
		Page<Object> param = new Page<Object>();
		
		param.setLimit(limit);
		param.setPage(page);
		//param.setStart((page-1)*limit);
		
		return param;
	}
	/**
	 * 
	 * @param start 哪一行开始
	 * @param limit 每页多少
	 * @return
	 */
	public static Page<Object> of(int start,int limit){
		Page<Object> param = new Page<Object>();
		param.setStart(start);
		param.setLimit(limit);
		param.setPage((start/limit)+1);
		return param;
	}
//	/**
//	 * 使用这个方法，map里必须要有分页的参数，否则会爆出异常。
//	 * 分页的参数名称未page和limit，或start和limit
//	 * @param params
//	 * @return
//	 */
//	public static <T> Page<T> of(Map<String, Object> params){
//		if(!params.containsKey("limit") || params.get("limit")==null) {
//			throw new BizException("请put进分页参数limit");
//		}
//		int limit=Integer.parseInt(params.get("limit").toString());
//		if(params.containsKey("page")) {
//			int page=Integer.parseInt(params.get("page").toString());
////			params.remove("page");
////			params.remove("limit");
////			params.remove("start");
//
//			return (Page<T>)Page.of_1(page, limit).setParams(params);
//		} else	if(params.containsKey("start")) {
////			params.remove("page");
////			params.remove("limit");
////			params.remove("start");
//			
//			int start=Integer.parseInt(params.get("start").toString());
//			return (Page<T>)Page.of(start, limit).setParams(params);
//		} else {
//			throw new BizException("请put进分页参数start或page");
//		}	
//		
////		Page<T> page=new Page<T>();
////		page.init(params);
////		return page;
//	}
//	public Page<T> init(Map<String, Object> params){
//		if(!params.containsKey("limit") || params.get("limit")==null) {
//			throw new BizException("请put进分页参数limit");
//		}
//		int limit=Integer.parseInt(params.get("limit").toString());
//		if(params.containsKey("page")) {
//			int page=Integer.parseInt(params.get("page").toString());
////			params.remove("page");
////			params.remove("limit");
////			params.remove("start");
//
//			return (Page<T>)Page.of_1(page, limit).setParams(params);
//		} else	if(params.containsKey("start")) {
////			params.remove("page");
////			params.remove("limit");
////			params.remove("start");
//			
//			int start=Integer.parseInt(params.get("start").toString());
//			return (Page<T>)Page.of(start, limit).setParams(params);
//		} else {
//			throw new BizException("请put进分页参数start或page");
//		}	
//	}
	
	
//	/**
//	 * 使用哪一列进行统计总数，默认是null,一般可以设置为id
//	 * 只在mybatis中有效
//	 * @param countColumn
//	 */
//	public Page<T> setCountColumn(String countColumn) {
//		this.countColumn = countColumn;
//		return this;
//	}
	
//	/**
//	 * 获得当前页,如果参数没有传递过来获取 第几页的话，这里将会自动计算当前是第几页
//	 * @author mawujun qq:16064988 mawujun1234@163.com
//	 * @return
//	 */
//	public int getPage() {
//		if(this.page==-1){
//			return getPage_cmpt();
//		}
//		return page;
//	}
//	/**
//	 * 通过计算获得当前页
//	 * @author mawujun qq:16064988 mawujun1234@163.com
//	 * @return
//	 */
//	public int getPage_cmpt() {
//		//开始计算pageNo。看getFirst()
//		this.page=Double.valueOf(Math.ceil(new Double(start)/new Double(limit))).intValue()+1;
//		return page;
//	}
//
//
//	public int getStart() {
//		if(start==0 &&page>0) {
//			return page*limit;
//		}
//		return start;
//	}
//	public Page<T> setStart(int start) {
//		this.start = start;
//		return this;
//	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getPage() {
		return page;
	}

	
//	public Object getParams() {
//		return params;
//	}
//	/**
//	 * 设置参数，一般是作为where条件的，可以是map，bean等各种类型
//	 * 
//	 * @author mawujun email:160649888@163.com qq:16064988
//	 * @param params
//	 */
//	public Page<T> setParams(Object params) {
//		if(params==null) {
//			return this;
//		}
//		if(params instanceof Condition) {
//			this.params = params;
//		} else if(params instanceof Map) {
//			this.params = Condition.of((Map)params);
//			
//		} else {
//			this.params = params;
//		}
//		
//		return this;
//	}
//	/**
//	 * 获取map类型的params，然后添加参数
//	 * PageInfo.of(start,limit).ofParams().eq(key,value)
//	 * @return
//	 */
//	public IParams setParams(Params params) {
//		if(this.params==null || !(this.params instanceof Map)){
//			this.params=Params.of();
//		}
//		return (IParams)this.params;
//	}

//	/**
//	 * 调用这个方法，就默认参数Map类型的,
//	 * 默认null和空字符串不会添加进去
//	 * 和setParams是冲突的，只能选一个
//	 * @author mawujun email:160649888@163.com qq:16064988
//	 * @param key
//	 * @param params
//	 * @return
//	 */
//	public PageInfo<T> addParam(String key,Object value) {
//		if(value==null || "".equals(value)){
//			return this;
//		}
//		if(this.params==null || !(this.params instanceof Map)){
//			this.params=new HashMap<String,Object>();
//		}
//		((Map<String,Object>)this.params).put(key, value);
//		return this;
//	}

	public Page<T> setPage(int page) {
		this.page = page;
		this.setStart((page-1)*limit);
		return this;
	}
	public int getLimit() {
		return limit;
	}
	public Page<T> setLimit(int limit) {
		this.limit = limit;
		return this;
	}
	public int getTotal() {
		return total;
	}
	public Page<T> setTotal(int total) {
		this.total = total;
		return this;
	}
	
	public List<T> getRoot() {
		return root;
	}
	public Page<T> setRoot(List<T> root) {
		this.root = root;
		return this;
	}


	
	/**
	 * 返回结果集中的数量
	 * @author mawujun email:160649888@163.com qq:16064988
	 * @return
	 */
	public int getRootSize() {
		if(root==null){
			return 0;
		}
		return root.size();
	}
	/**
	 * 返回页数
	 * 根据pageSize与totalItems计算总页数.
	 */
	public int getTotalPages() {
		return (int) Math.ceil((double) total / (double) getLimit());

	}

	/**
	 * 是否还有下一页.
	 */
	public boolean hasNextPage() {
		return (getPage() + 1 <= getTotalPages());
	}

	/**
	 * 是否最后一页.
	 */
	public boolean isLastPage() {
		return !hasNextPage();
	}

	/**
	 * 取得下页的页号, 序号从1开始.
	 * 当前页为尾页时仍返回尾页序号.
	 */
	public int getNextPageNo() {
		if (hasNextPage()) {
			return getPage() + 1;
		} else {
			return getPage();
		}
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean hasPrePage() {
		return (getPage() > 1);
	}

	/**
	 * 是否第一页.
	 */
	public boolean isFirstPage() {
		return !hasPrePage();
	}

	/**
	 * 取得上页的页号, 序号从1开始.
	 * 当前页为首页时返回首页序号.
	 */
	public int getPrePageNo() {
		if (hasPrePage()) {
			return getPage() - 1;
		} else {
			return getPage();
		}
	}


	
	
	
	
	
	
	
	
	//==================================================================================================================================
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.getRootSize();
	}

	@Override
	public boolean isEmpty() {
		if(this.root==null || this.root.size()==0) {
			return true;
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		if(this.isEmpty()) {
			return false;
		}
		return this.root.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		if(this.root==null) {
			return null;
		}
		return this.root.iterator();
	}

	@Override
	public Object[] toArray() {
		if(this.root==null) {
			return null;
		}
		return this.root.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		if(this.root==null) {
			return null;
		}
		return this.root.toArray(a);
	}

	@Override
	public boolean add(T e) {
		if(this.root==null) {
			this.root=new ArrayList<T>();
		}
		return this.root.add(e);
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		if(this.root==null) {
			return false;
		}
		return this.root.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if(this.root==null) {
			return false;
		}
		return this.root.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		if(this.root==null) {
			this.root=new ArrayList<T>();
		}
		return this.root.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		if(this.root==null) {
			this.root=new ArrayList<T>();
		}
		return this.root.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if(this.root==null) {
			return false;
		}
		return this.root.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if(this.root==null) {
			return false;
		}
		// TODO Auto-generated method stub
		return this.root.retainAll(c);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		if(this.root==null) {
			return;
		}
		this.root.clear();
	}

	@Override
	public T get(int index) {
		if(this.root==null) {
			return null;
		}
		return this.root.get(index);
	}

	@Override
	public T set(int index, T element) {
		if(this.root==null) {
			throw new IndexOutOfBoundsException("list为null,不能set");
		}
		return this.root.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		if(this.root==null) {
			this.root=new ArrayList<T>();
		}
		this.root.add(index, element);
	}

	@Override
	public T remove(int index) {
		if(this.root==null) {
			throw new IndexOutOfBoundsException("list为null,不能remove");
		}
		return this.root.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		if(this.root==null) {
			return -1;
		}
		return this.root.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		if(this.root==null) {
			return -1;
		}
		return this.root.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		if(this.root==null) {
			return null;
		}
		return this.root.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		if(this.root==null) {
			return null;
		}
		return this.root.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		if(this.root==null) {
			this.root=new ArrayList<T>();
		}
		return this.root.subList(fromIndex, toIndex);
	}
	
	
	//=============================================================================================
//	//下面的都是参数相关的修改
//	private Condition getParamsMap() {
//		if(params==null) {
//			params=Condition.of();
//			return (Condition)params;
//		}
//		if(!(params instanceof Condition)) {
//			throw new RuntimeException("当前分页的参数不是Params类型，不能调用该方法!");
//		} 
//		return (Condition)params;
//	}

//	@Override
//	public Page<T> put(String key, Object value) {
//		//getParamsMap().put(key, value);
//		add( key,  value);
//		return this;
//	}
//	@Override
//	public Page<T> add(String key, Object value) {
//		if(!StringUtils.hasText(key) || !StringUtils.isNotEmpty(value)) {
//			return this;
//		}
//		// TODO Auto-generated method stub
//		getParamsMap().add(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> add(String key, OpEnum opEnum, Object value) {
//		// TODO Auto-generated method stub
//		getParamsMap().add(key, opEnum, value);
//		return this;
//	}
//	@Override
//	public Page<T> eq(String key, Object value) {
//		// TODO Auto-generated method stub
//		getParamsMap().eq(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> eq_i(String key, Object value) {
//		// TODO Auto-generated method stub
//		getParamsMap().eq_i(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> noteq(String key, Object value) {
//		// TODO Auto-generated method stub
//		getParamsMap().noteq(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> noteq_i(String key, Object value) {
//		// TODO Auto-generated method stub
//		getParamsMap().noteq_i(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> gt(String key, Object value) {
//		// TODO Auto-generated method stub
//		getParamsMap().gt(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> ge(String key, Object value) {
//		// TODO Auto-generated method stub
//		getParamsMap().ge(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> lt(String key, Object value) {
//		// TODO Auto-generated method stub
//		getParamsMap().lt(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> le(String key, Object value) {
//		// TODO Auto-generated method stub
//		getParamsMap().le(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> between(String key, Object value1, Object value2) {
//		// TODO Auto-generated method stub
//		getParamsMap().between(key, value1, value2);
//		return this;
//	}
//	@Override
//	public Page<T> in(String key, Object... value) {
//		// TODO Auto-generated method stub
//		getParamsMap().in(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> notin(String key, Object... value) {
//		// TODO Auto-generated method stub
//		getParamsMap().notin(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> isnull(String key) {
//		// TODO Auto-generated method stub
//		getParamsMap().isnull(key);
//		return this;
//	}
//	@Override
//	public Page<T> isnotnull(String key) {
//		// TODO Auto-generated method stub
//		getParamsMap().isnotnull(key);
//		return this;
//	}
//	@Override
//	public Page<T> like(String key, String value) {
//		// TODO Auto-generated method stub
//		getParamsMap().like(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> likeprefix(String key, String value) {
//		// TODO Auto-generated method stub
//		getParamsMap().likeprefix(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> likesuffix(String key, String value) {
//		// TODO Auto-generated method stub
//		return this;
//	}
//	@Override
//	public Page<T> like_i(String key, String value) {
//		// TODO Auto-generated method stub
//		getParamsMap().like_i(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> likeprefix_i(String key, String value) {
//		// TODO Auto-generated method stub
//		getParamsMap().likeprefix_i(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> likesuffix_i(String key, String value) {
//		// TODO Auto-generated method stub
//		getParamsMap().likesuffix_i(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> notlike(String key, String value) {
//		// TODO Auto-generated method stub
//		getParamsMap().notlike(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> notlikeprefix(String key, String value) {
//		// TODO Auto-generated method stub
//		getParamsMap().notlikeprefix(key, value);
//		return this;
//	}
//	@Override
//	public Page<T> notlikesuffix(String key, String value) {
//		// TODO Auto-generated method stub
//		getParamsMap().notlikesuffix(key, value);
//		return this;
//	}
//
//	
}
