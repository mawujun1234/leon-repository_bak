package test.mawujun.repository;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mawujun.repository.identity.UUIDEntityValidate;

@Alias("city")
@Entity
@Table(name="t_city")
public class City extends UUIDEntityValidate{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3172844311839975513L;
	@Column(length=30)
	private String name;
	
	private Integer age;
	
	@Column(precision = 10,scale = 2)
	private Double price;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createDate;
	
	@Enumerated(EnumType.STRING)
	@Column(length=10)
	private Sex sex;
	
	
	
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}

}

enum Sex {
	Man("男"),Women("女");
	
	private String name;
	Sex(String name ){
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
