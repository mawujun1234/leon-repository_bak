package test.mawujun.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mawujun.generator.code.Coldefine;
import com.mawujun.generator.code.FK;
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
@Table(name="t_city")
@org.hibernate.annotations.Table(comment="表注释", appliesTo = "t_city") 
@Data
public class City extends UUIDEntityValidate{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3172844311839975513L;
	@Column(length=30)
	@Coldefine(comment="名称",defaultValue="")
	private String name;
	
	@FK(table=T.t_city.tablename__)
	@Coldefine(comment="年龄",defaultValue="18")
	private Integer age;
	
	@Column(precision = 10,scale = 2)
	@Coldefine(comment="价格",defaultValue="0.0")
	private Double price;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	//@CreationTimestamp
	//@UpdateTimestamp
	@Coldefine(comment="创建时间")
	private Date createDate;
	
	@Enumerated(EnumType.STRING)
	@Column(length=10)
	@Coldefine(comment="创建时间",defaultValue="Man")
	private Sex sex;
	
	
}
