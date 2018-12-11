package test.mawujun.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

/**
* @author mawujun 16064988
* @createDate ：2018年12月4日 上午11:45:19
*/
@Entity
@Table(name="t_coplxid2entity")
@IdClass(CoplxId2.class)
@Data
public class CoplxId2Entity  implements Serializable{
	@Id
	private String id1;
	@Id
	private String id2;
	
	private String name;



}
