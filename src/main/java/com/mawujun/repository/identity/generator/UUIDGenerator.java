package com.mawujun.repository.identity.generator;

import java.util.UUID;

public class UUIDGenerator  {
	
	public static String generate(){
		return UUID.randomUUID().toString();
	}
}
