package test.mawujun.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.ibatis.type.Alias;

import lombok.Data;

/**
* @author mawujun 16064988
* @createDate ：2018年12月4日 上午11:45:19
*/
@Alias("coplxId1Entity")
@Entity
@Table(name="t_coplxid1entity")
@Data
public class CoplxId1Entity {
	@EmbeddedId 
    private CoplxId1  id;
	
	private String name;

	

}
