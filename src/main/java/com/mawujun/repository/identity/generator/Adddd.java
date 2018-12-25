package com.mawujun.repository.identity.generator;

import java.util.Calendar;

public class Adddd {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Calendar calendar = Calendar.getInstance();
         calendar.set(2018, Calendar.NOVEMBER, 1);
         calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         calendar.set(Calendar.MILLISECOND, 0);
        // EPOCH是服务器第一次上线时间点, 设置后不允许修改
        System.out.println(calendar.getTimeInMillis());
	}

}
