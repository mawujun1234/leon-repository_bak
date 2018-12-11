package test.mawujun.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.ibatis.type.Alias;

/**
* @author mawujun 16064988
* @createDate ：2018年12月4日 上午11:45:19
*/
@Alias("coplxId1Entity")
@Entity
@Table(name="t_coplxid1entity")
public class CoplxId1Entity {
	@EmbeddedId 
    private CoplxId1  id;
	
	private String name;

	public CoplxId1 getId() {
		return id;
	}

	public void setId(CoplxId1 id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	


}
