
<#assign simpleClassNameFirstLower = simpleClassName?uncap_first>   
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//ibatis.apache.org//DTD Mapper 3.0//EN"
"http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd">

<#macro mapperEl value>${r"#{"}${value}}</#macro>
<#macro printDollar value>${r"${"}${value}}</#macro>
<#macro namespace>${basepackage}</#macro>
<!-- mawujun qq:16064988 e-mail:16064988@qq.com-->
<mapper namespace="<@namespace/>.${simpleClassName}Repository">
	<!-- 查询语句，会自动分页-->
    <select id="queryPage" resultType="${className}" parameterType="map">
    	select * from ${tableName}
    </select> 
     <!-- 名称模式为：****_count,也可以不写，但如果查询叫复杂的话，自己写有助于控制查询性能-->
    <select id="queryPage_count" resultType="int" parameterType="map">
    	select count(a.id)
		from ${tableName}
    </select>
</mapper>

