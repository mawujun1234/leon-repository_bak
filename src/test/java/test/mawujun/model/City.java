package test.mawujun.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mawujun.generator.annotation.ColDefine;
import com.mawujun.generator.annotation.FK;
import com.mawujun.generator.annotation.TableDefine;
import com.mawujun.repository.identity.UUIDEntityValidate;

import lombok.Data;
import test.mawujun.jpa.utils.T;

/**
 * 1：时间映射,@Temporal注解
 *  CreationTimestamp创建时的默认值，UpdateTimestamp在更新的时候会生成默认值
 * 2：枚举映射,@Enumerated
 * 3：生成默认值的注解：
 * 
	@org.hibernate.annotations.ColumnDefault("1.00")
	@org.hibernate.annotations.Generated(
	org.hibernate.annotations.GenerationTime.INSERT
	)
 * 4：二进制和大型值类型映射，使用@Lob注解，可注解的类型包括
 *  byte[],Byte[]--VARBINARY;String--CLOB,java.sql.Clob--CLOB,java.sql.Blob--BLOB,java.io.Serializable--VARBINARY
 *  
 * @author Administrator
 *
 */
@Alias("city")
@Entity
@Table(name="t_city")//,uniqueConstraints= {@UniqueConstraint(columnNames= {T.t_city.age})}
@TableDefine(comment="表注释",fks= {@FK(columnNames= {T.t_city.name,T.t_city.sex},refEntity=CoplxId1Entity.class,refColumnNames= {T.t_coplxid1entity.id1,T.t_coplxid1entity.id2})})
//@org.hibernate.annotations.Table(comment="表注释", appliesTo = "t_city") 
@Data
public class City extends UUIDEntityValidate{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3172844311839975513L;
	@Column(length=30)
	@ColDefine(comment="名称注释",defaultValue="",label="名称")
	private String name;
	
	@ColDefine(comment="年龄",defaultValue="18")
	private Integer age;
	
	@Column(precision = 10,scale = 2)
	@ColDefine(comment="价格",defaultValue="0.0")
	private Double price;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	//@CreationTimestamp
	//@UpdateTimestamp
	@ColDefine(comment="创建时间")
	private Date createDate;
	
	@Enumerated(EnumType.STRING)
	@Column(length=10)
	@ColDefine(comment="性别",defaultValue="Man")
	private Sex sex;


	
	
}
