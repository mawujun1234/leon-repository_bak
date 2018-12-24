package com.mawujun.repository.identity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;

/**
 * 
l         name属性表示该表主键生成策略的名称，它被引用在@GeneratedValue中设置的“generator”值中。
l         table属性表示表生成策略所持久化的表名，例如，这里表使用的是数据库中的“tb_generator”。
l         catalog属性和schema具体指定表所在的目录名或是数据库名。
l         pkColumnName属性的值表示在持久化表中，该主键生成策略所对应键值的名称。例如在“tb_generator”中将“gen_name”作为主键的键值
l         valueColumnName属性的值表示在持久化表中，该主键当前所生成的值，它的值将会随着每次创建累加。例如，在“tb_generator”中将“gen_value”作为主键的值
l         pkColumnValue属性的值表示在持久化表中，该生成策略所对应的主键。例如在“tb_generator”表中，将“gen_name”的值为“CUSTOMER_PK”。
l         initialValue表示主键初识值，默认为0。
l         allocationSize表示每次主键值增加的大小，例如设置成1，则表示每次创建新记录后自动加1，默认为50。
 * 
 * @author mawujun
 */
//JPA 基类的标识
@MappedSuperclass
public abstract class TableId implements IdEntity<Long> {

	/**
	 * 系统中有个表叫"XT_XH"，它有两个列TYPE(主键)和XH(值)。每生成一个Student记录,这个Student的id就从XT_XH表中取。取TYPE=stu的XH（好多表的id主键都是放在这个表，以type来区分，当然要额可以一个表建立一个id生成器表）。取过之后，这个XH会加1，等着下次来取。
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "student_gen")
	@TableGenerator(name = "student_gen",table = "XT_XH",pkColumnName = "TYPE",valueColumnName = "XH",pkColumnValue = "stu",allocationSize = 50)
	protected Long id;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableId other = (TableId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}