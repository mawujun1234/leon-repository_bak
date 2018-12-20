<#assign simpleClassNameFirstLower = simpleClassName?uncap_first> 
<#-- //所在模块-->
<#assign module = basepackage?substring(basepackage?last_index_of(".")+1)> 
Ext.defineModel("Ems.${module}.${simpleClassName}",{
	extend:"Ext.data.Model",
	fields:[
	<#list propertyColumns as propertyColumn>
		<#if propertyColumn.jsType=='date'>
		{name:'${propertyColumn.property}',type:'${propertyColumn.jsType!"auto"}', dateFormat: 'Y-m-d'}<#if propertyColumn_has_next>,</#if>
		<#else>
		{name:'${propertyColumn.property}',type:'${propertyColumn.jsType!"auto"}'}<#if propertyColumn_has_next>,</#if>
		</#if>
	</#list>      
	]
});