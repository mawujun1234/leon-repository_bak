package com.mawujun.generator.code;

import java.util.Comparator;

/**
 * 对属性进行排序，hidden=true的放在后面
 * @author mawujun
 *
 */
public class PropertyColumnComparator implements Comparator<PropertyColumn> {

	@Override
	public int compare(PropertyColumn o1, PropertyColumn o2) {
//		// TODO Auto-generated method stub
//		//隐藏的列放在最后
//		if(o1.getHidden_notrans() && !o2.getHidden_notrans()){
//			return 1;
//		} else if(!o1.getHidden_notrans() && o2.getHidden_notrans()){
//			return -1;
//		} else if(o1.getSort()<o2.getSort()){
//			return 1;
//		} else if(o1.getSort()>o2.getSort()){
//			return -1;
//		}
		return 0;
	}

}
