package ${basepackage}.model;


<#include "/java_copyright.include"/>

@Data
public class ${entitySimpleClassName}Id  implements Serializable{
<#list propertyColumns as pc>

	<#if pc.isId==true>
	private ${pc.simpleClassName} ${pc.property};
	</#if>
</#list>
}
