package aaaaa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.GenericGenerator;

import com.mawujun.generator.annotation.ColDefine;
import com.mawujun.generator.annotation.TableDefine;

/**
 * @author mawujun qq:16064988 e-mail:16064988@qq.com 
 * @version 1.0
 * @since 1.0
 */
@Alias("test")
@Entity
@Table(name="test_test")
@TableDefine(comment="标记注释")
@IdClass( TestId.class)
public class Test {
	@Id
	@Column(length=36)
	private String id1;
	@Id
	@Column(length=36)
	private String id2;
	@Column(length=100)
	private String name;
	@Column(length=11)
	@ColDefine(defaultValue="1")
	private Integer age;
	@Column(precision=10,scale=2)
	@ColDefine(defaultValue="1.10")
	private BigDecimal price;
	@Column(precision=4,scale=0)
	private Float test;
	@Column(precision=10,scale=2)
	@ColDefine(comment="测试和注释")
	private Double timee;

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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Float getTest() {
		return test;
	}

	public void setTest(Float test) {
		this.test = test;
	}
	public Double getTimee() {
		return timee;
	}

	public void setTimee(Double timee) {
		this.timee = timee;
	}
}
