package com.mawujun.repository.identity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

public class SnowFlakeId  implements IdEntity<Long>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5731890808057268584L;
	
	/**
	 * 只能用单机，如果是双机集群的话，就得修改，得配置SnowFlakeGenerator中的workerId 
	 */
	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = "com.mawujun.repository.identity.generator.SnowFlakeGenerator")
	//@Column(length=36,updatable=false,unique=true)
	protected Long id;

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}
}
