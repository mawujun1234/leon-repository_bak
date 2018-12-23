package ${basepackage}.model;

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
@Data
public class ${entitySimpleClassName} {
<#list propertyColumns as pc>

	<#if pc.isId==true>@Id</#if>
	<#if pc.columnAnnotation?? && pc.columnAnnotation != "">
	${pc.columnAnnotation}
	</#if>
	<#if pc.colDefine?? && pc.colDefine != "">
	${pc.colDefine}
	</#if>
	private ${pc.simpleClassName} ${pc.property};
</#list>
}
