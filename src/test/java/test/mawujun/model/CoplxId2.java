package test.mawujun.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.mawujun.generator.code.Coldefine;

/**
* @author mawujun 16064988
* @createDate ：2018年12月4日 上午11:45:39
*/
public class CoplxId2 implements Serializable {
	
	private String id1;
	@Coldefine(comment="id1注释0000")
	private String id2;
	
	public String getId1() {
		return id1;
	}
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public String getId2() {
		return id2;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}

}
