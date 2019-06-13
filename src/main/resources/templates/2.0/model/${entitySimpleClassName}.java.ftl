package ${basepackage}.model;

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
<#if uselombok==true>
import lombok.Data;
</#if>
<#include "/java_copyright.include"/>

@Alias("${alias}")
@Entity
@Table(name="${entityTableName}")
<#if comment??  && comment != "">
@TableDefine(comment="${comment}")
</#if>
<#if isCompositeId==true>
@IdClass( ${entitySimpleClassName}Id.class)
</#if>
<#if uselombok==true>
@Data
</#if>
public class ${entitySimpleClassName} {
<#list propertyColumns as pc>
	<#if pc.isId==true>
	@Id
	</#if>
	<#if pc.idGenEnum=="uuid">
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	</#if>
	<#if pc.idGenEnum=="identity">
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	</#if>
	<#if pc.columnAnnotation?? && pc.columnAnnotation != "">
	${pc.columnAnnotation}
	</#if>
	<#if pc.colDefine?? && pc.colDefine != "">
	${pc.colDefine}
	</#if>
	<#if pc.isVersion==true>
	@Version
	</#if>
	<#if pc.isLogicDelete==true>
	@LogicDelete
	</#if>
	private ${pc.simpleClassName} ${pc.property};
</#list>

<#list propertyColumns as pc>
	public ${pc.simpleClassName} get${pc.property?cap_first}() {
		return ${pc.property};
	}

	public void set${pc.property?cap_first}(${pc.simpleClassName} ${pc.property}) {
		this.${pc.property} = ${pc.property};
	}
</#list>
}
