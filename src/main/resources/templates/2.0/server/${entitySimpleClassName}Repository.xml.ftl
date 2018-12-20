
<#assign simpleClassNameFirstLower = entitySimpleClassName?uncap_first>   
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//ibatis.apache.org//DTD Mapper 3.0//EN"
"http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd">

<#macro mapperEl value>${r"#{"}${value}}</#macro>
<#macro printDollar value>${r"${"}${value}}</#macro>
<#macro namespace>${basepackage}</#macro>
<!-- mawujun qq:16064988 e-mail:16064988@qq.com-->
<mapper namespace="<@namespace/>.${entitySimpleClassName}Repository">

    <sql id="BaseColumns">
		<#list propertyColumns as pc>${pc.column} as ${pc.property}<#if pc_has_next>,</#if></#list>
	</sql>
	<sql id="WhereColumns">
		<where>
			<#list propertyColumns as pc>
			<if test="${pc.property} != null">
				and ${pc.column} = #${'{'}${pc.property}${'}'}
			</if>
			</#list>
		</where>
	</sql>
	<!-- 查询语句，会自动分页-->
	<select id="listPageByMybatis" resultType="${alias}" >
        select <include refid = "BaseColumns" /> from ${entityTableName}
        <include refid="WhereColumns" />
		order by <#list idColumns as id>${id} <#if id_has_next>,</#if></#list>
    </select>
    <!-- 名称模式为：****_count,也可以不写，但如果查询叫复杂的话，自己写有助于控制查询性能-->
    <select id="listPageByMybatis1" resultType="${alias}" >
    	select <include refid = "BaseColumns" /> from ${entityTableName} order by <#list idColumns as id>${id} <#if id_has_next>,</#if></#list>
    </select>
    <select id="listPageByMybatis1_count" resultType="long" >
    	select count(<#list idColumns as id>${id}<#if id_has_next>,</#if></#list>) from ${entityTableName}
    </select>
    
	
    <select id="getById__" resultType="${alias}" >
        select
		<include refid = "BaseColumns" />
		from ${entityTableName} where 
		<#list idColumns as id>
		${id} = #${'{'}${id}${'}'}<#if id_has_next>and</#if>
		</#list>
    </select>
    <select id="getMapById__" resultType="beanmap">
       select
		<include refid = "BaseColumns" />
		from ${entityTableName} where 
		<#list idColumns as id>
		${id} = #${'{'}${id}${'}'}<#if id_has_next>and</#if>
		</#list>
    </select>
    
    <select id="listByParams__" resultType="${alias}" >
        select
		<include refid="BaseColumns" />
		from ${entityTableName}
		<include refid="WhereColumns" />
    </select>
</mapper>

