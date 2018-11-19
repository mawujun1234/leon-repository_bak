package com.mawujun.repository.identity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 用户自定义id,在保存的时候，需要用户自己去定义,一般是全局id
 * 使用雪花模型的id生成器SnowFlake来获取id
 * @author admin
 *
 */
@MappedSuperclass
public abstract class AssignedLongIdEntity implements IdEntity<Long> {
	@Id
	private Long id;

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
