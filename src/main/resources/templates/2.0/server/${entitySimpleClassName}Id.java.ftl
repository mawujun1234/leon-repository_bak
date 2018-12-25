package ${basepackage}.model;

import java.io.Serializable;
<#if uselombok==true>
import lombok.Data;
</#if>
<#include "/java_copyright.include"/>

<#if uselombok==true>
@Data
</#if>
public class ${entitySimpleClassName}Id  implements Serializable{
<#list propertyColumns as pc>

	<#if pc.isId==true>
	private ${pc.simpleClassName} ${pc.property};
	</#if>
</#list>
}
