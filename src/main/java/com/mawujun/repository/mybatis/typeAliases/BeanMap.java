package com.mawujun.repository.mybatis.typeAliases;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.ibatis.type.Alias;

import com.mawujun.utils.DateUtils;

/**
 * 不区分大小写
 * 同时有类型转换
 * @author Administrator
 *
 */
@Alias("beanmap")
//public class BeanMap implements Map<String, Object>, Serializable{
public class BeanMap extends CaseInsensitiveMap{
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 3093459857690388994L;

	//private static final Logger logger=LoggerFactory.getLogger(BeanMap.class);
//	
//	private Map result = new CaseInsensitiveMap();
//
//	@Override
//	public int size() {
//		// TODO Auto-generated method stub
//		return super.size();
//	}
//
//	@Override
//	public boolean isEmpty() {
//		// TODO Auto-generated method stub
//		return super.isEmpty();
//	}
//
//	@Override
//	public boolean containsKey(Object key) {
//		// TODO Auto-generated method stub
//		return super.containsKey(key);
//	}
//
//	@Override
//	public boolean containsValue(Object value) {
//		// TODO Auto-generated method stub
//		return super.containsValue(value);
//	}
//
//	@Override
//	public Object get(Object key) {
//		// TODO Auto-generated method stub
//		return super.get(key);
//	}
//
//	@Override
//	public Object put(Object key, Object value) {
//		// TODO Auto-generated method stub
//		return super.put(key, value);
//	}
//
//	@Override
//	public Object remove(Object key) {
//		// TODO Auto-generated method stub
//		return super.remove(key);
//	}
//
//	@Override
//	public void putAll(Map<? extends Object, ? extends Object> m) {
//		// TODO Auto-generated method stub
//		super.putAll(m);
//	}
//
//	@Override
//	public void clear() {
//		// TODO Auto-generated method stub
//		super.clear();
//		
//	}
//
//	@Override
//	public Set<String> keySet() {
//		// TODO Auto-generated method stub
//		return super.keySet();
//	}
//
//	@Override
//	public Collection<Object> values() {
//		// TODO Auto-generated method stub
//		return super.values();
//	}
//
//	@Override
//	public Set<Entry<String, Object>> entrySet() {
//		// TODO Auto-generated method stub
//		return super.entrySet();
//	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8219979051333472569L;

	public Date getDate(Object var1) {
		Object var2 = this.get(var1);
		String var3 = var2.getClass().getSimpleName();
		Date var4 = var3.equalsIgnoreCase("Date") ? (var2 != null ? (Date) var2 : null) : null;
		return var4;
	}

	public String getDateString(Object var1) {
		Date var2 = this.getDate(var1);
		String var3 = "";
		if (var2 != null) {
			var3 = DateUtils.date2String(var2);
		}

		return var3;
	}

	public double getdouble(Object var1) {
		Object var2 = this.get(var1);
		return var2 == null ? 0.0D : str2double(var2);
	}

	public Double getDouble(Object var1) {
		Object var2 = this.get(var1);
		return var2 == null ? null : str2double(var2);
	}

	public float getfloat(Object var1) {
		Object var2 = this.get(var1);
		return var2 == null ? 0.0F : str2float(var2);
	}

	public Float getFloat(Object var1) {
		Object var2 = this.get(var1);
		return var2 == null ? null : str2float(var2);
	}

	public int getInt(Object var1) {
		Object var2 = this.get(var1);
		return var2 == null ? 0 : str2int(var2);
	}
	public Integer getInteger(Object var1, int var2) {
		Integer var3 = this.getInteger(var1);
		return var3 == null ? var2 : var3;
	}

	public Integer getInteger(Object var1) {
		Integer var2 = null;
		Object var3 = this.get(var1);
		if (var3 != null) {
			var2 = str2int(var3);
		}

		return var2;
	}

	public long getlong(Object var1) {
		Object var2 = this.get(var1);
		return var2 == null ? 0L : str2long(var2);
	}

	public Long getLong(Object var1) {
		Object var2 = this.get(var1);
		return var2 == null ? null : str2long(var2);
	}
	
	public String getString(Object var1) {
		Object var2 = this.get(var1);
		return var2 != null ? var2.toString() : "";
	}
	
	public Boolean getBoolean(Object var1) {
		boolean var2 = false;
		Object var3 = this.get(var1);
		if (var3 == null) {
			return var2;
		} else {
			if (var3 instanceof Boolean) {
				var2 = (Boolean) var3;
			} else {
				String var4 = null2string(var3);
				var2 = var4.equalsIgnoreCase("true") || var4.equalsIgnoreCase("y") || var4.equalsIgnoreCase("1");
			}

			return var2;
		}
	}

	public boolean getboolean(Object var1) {
		return this.getBoolean(var1);
	}
	//===================================================================================================
	public static boolean isNumber(Object var0) {
		boolean var1 = false;
		return var0 == null ? var1 : var0.toString().trim().matches("^-?\\d+[\\.\\d]*$");
	}
	public static boolean isEmpty(Object var0) {
		return var0 == null || var0.toString().trim().length() == 0;
	}
	public static boolean isEmpty(Object var0, boolean var1) {
		if (var1) {
			if (var0 == null) {
				return true;
			} else {
				String var2 = var0.toString().trim();
				return var2.length() == 0 || var2.equals("\"\"") || var2.equals("''") || var2.equals("null")
						|| var2.equals("undefined");
			}
		} else {
			return var0 == null || var0.toString().trim().length() == 0;
		}
	}
	public static double str2double(Object var0) {
		return str2double(var0, 0.0D);
	}
	public static double str2double(Object var0, double var1) {
		double var3 = var1;
		if (var0 == null) {
			return var1;
		} else if (var0 instanceof Number) {
			return ((Number) var0).doubleValue();
		} else {
			try {
				if (!isNumber(var0)) {
					return var3;
				}

				var3 = Double.parseDouble(var0.toString().trim());
			} catch (Exception e) {
				//_$14.warn("转换{" + var0.toString() + "}为double时异常!");
				e.printStackTrace();
				var3 = var1;
			}

			return var3;
		}
	}
	
	public static float str2float(Object var0) {
		return str2float(var0, 0.0F);
	}

	public static float str2float(Object var0, float var1) {
		if (var0 == null) {
			return var1;
		} else if (var0 instanceof Number) {
			return ((Number) var0).floatValue();
		} else {
			float var2;
			try {
				var2 = Float.parseFloat(var0.toString());
			} catch (Exception e) {
				//_$14.warn("转换{" + var0.toString() + "}为float时异常!");
				e.printStackTrace();
				var2 = var1;
			}

			return var2;
		}
	}
	public static int str2int(Object var0) {
		return object2int(var0, 0);
	}

	public static int str2int(Object var0, int var1) {
		return object2int(var0, var1);
	}

	public static int object2int(Object var0, int var1) {
		if (isEmpty(var0, true)) {
			return var1;
		} else {
			int var2;
			try {
				if (var0 instanceof Number) {
					return ((Number) var0).intValue();
				}

				if (isNumber(var0)) {
					var2 = (int) str2double(var0);
				} else {
					var2 = Integer.parseInt(var0.toString().trim());
				}
			} catch (Exception e) {
				//String var4 = "object2int:转换obj={" + var0 + "}时异常!";
				e.printStackTrace();

				var2 = var1;
			}

			return var2;
		}
	}
	
	public static long str2long(Object var0) {
		return object2long(var0, 0L);
	}
	public static long object2long(Object var0, long var1) {
		if (isEmpty(var0, true)) {
			return var1;
		} else {
			long var3;
			if (var0 instanceof Number) {
				var3 = Long.parseLong(var0.toString());
			} else {
				try {
					if (isNumber(var0)) {
						var3 = (long) str2double(var0);
					} else {
						var3 = Long.parseLong(var0.toString().trim());
					}
				} catch (Exception e) {
					//String var6 = "object2long(" + var0 + ")转换为long型异常!";
					System.out.println(e);

					var3 = var1;
				}
			}

			return var3;
		}
	}
	
	public static String null2string(Object var0) {
		return var0 == null ? "" : var0.toString();
	}

	public static String null2string(Object var0, String var1) {
		return isEmpty(var0) ? var1 : var0.toString();
	}
}
