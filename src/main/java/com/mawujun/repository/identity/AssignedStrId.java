package com.mawujun.repository.identity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 用户自定义id,在保存的时候，需要用户自己去定义
 * @author admin
 *
 */
@MappedSuperclass
public abstract class AssignedStrId implements IdEntity<String> {
	@Id
	@Column(length=36)
	private String id;

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id=id;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

}
