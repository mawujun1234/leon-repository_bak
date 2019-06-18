  
<#assign simpleClassNameFirstLower = entitySimpleClassName?uncap_first> 
package ${basepackage}.controller;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import com.mawujun.common.utils.M;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mawujun.mvc.R;
import com.mawujun.repository.utils.Page;
import com.mawujun.repository.utils.Cnd;

<#if extraCfg.uselombok?? && extraCfg.uselombok==true>
import lombok.extern.slf4j.Slf4j;
</#if>
<#list cndPropertys as pc>
<#if pc.isEnum==true>
import ${pc.className};
</#if>
</#list>
import ${entityClassName};
import ${basepackage}.service.${entitySimpleClassName}Service;

<#if isCompositeId==true>
	import ${idSimpleClassName};
</#if>
<#include "/java_copyright.include"/>

<#if extraCfg.uselombok?? && extraCfg.uselombok==true>
@Slf4j
</#if>
@Controller
@RequestMapping("/${module}")
public class ${entitySimpleClassName}Controller {

	@Autowired
	private ${entitySimpleClassName}Service ${simpleClassNameFirstLower}Service;


//	/**
//	 * 请按自己的需求修改
//	 * @author mawujun email:16064988@163.com qq:16064988
//	 * @param id 是父节点的id
//	 * @return
//	 */
//	@RequestMapping("/${simpleClassNameFirstLower}/list")
//	@ResponseBody
//	@RequiresPermissions("${module}:${entitySimpleClassNameUncap}:list")
//	public R list(<#list cndPropertys as pc><#if pc.isDateProp==true>String[] ${pc.property}<#else>,${pc.simpleClassName} ${pc.property}</#if><#if pc?has_next>,</#if></#list>) {//括号里面写参数
//		Cnd cnd=Cnd.of();
		<#list cndPropertys as pc>
		<#if pc.isDateProp==true>
//		if(${pc.property}!=null && ${pc.property}.length>0) {
//			cnd.between(M.${entitySimpleClassName}.${pc.property},${pc.property}[0], ${pc.property}[1]);
//		}
		<#else>
//		cnd.add(M.${entitySimpleClassName}.${pc.property}, ${pc.property});
		</#if>		
		</#list>
//		List<${entitySimpleClassName}> ${simpleClassNameFirstLower}es=${simpleClassNameFirstLower}Service.list(cnd);
//		return R.ok(${simpleClassNameFirstLower}es);
//	}

//	@RequestMapping("/${simpleClassNameFirstLower}/listAll")
//	@ResponseBody
//	@RequiresPermissions("${module}:${entitySimpleClassNameUncap}:list")
//	public R listAll() {	
//		List<${entitySimpleClassName}> ${simpleClassNameFirstLower}es=${simpleClassNameFirstLower}Service.listAll();
//		return R.ok().data(${simpleClassNameFirstLower}es);
//	}

//	/**
//	 * 这是基于分页的几种写法,的例子，请按自己的需求修改
//	 * @author mawujun email:16064988@163.com qq:16064988
//	 * @param start
//	 * @param limit
//	 * @param userName
//	 * @return
//	 */
//	@RequestMapping("/${simpleClassNameFirstLower}/page")
//	@ResponseBody
//  @RequiresPermissions("${module}:${entitySimpleClassNameUncap}:list")
//	public R page(Integer start,Integer limit<#list cndPropertys as pc><#if pc.isDateProp==true>,String[] ${pc.property}<#else>,${pc.simpleClassName} ${pc.property}</#if></#list>) {
//	//public R page(@RequestParam Map<String, Object> params){
//	//	//Page<${entitySimpleClassName}> pager = ${simpleClassNameFirstLower}Service.queryPage(params);
//	Cnd cnd=Cnd.ofPageLimit(page, limit);//.add(M.Star.name, name);
		<#list cndPropertys as pc>
		<#if pc.isDateProp==true>
//		if(${pc.property}!=null && ${pc.property}.length>0) {
//			cnd.between(M.${entitySimpleClassName}.${pc.property},${pc.property}[0], ${pc.property}[1]);
//		}
		<#else>
//		cnd.add(M.${entitySimpleClassName}.${pc.property}, ${pc.property});
		</#if>		
		</#list>
//
//		Page<${entitySimpleClassName}> pager=${simpleClassNameFirstLower}Service.page(cnd);
//		return R.ok().data(pager);
//	}
	/**
	 * 这是基于分页的几种写法,的例子，请按自己的需求修改
	 * @author mawujun email:16064988@163.com qq:16064988
	 * @param start
	 * @param limit
	 * @param userName
	 * @return
	 */
	@RequestMapping("/${simpleClassNameFirstLower}/page")
	@ResponseBody
	@RequiresPermissions("${module}:${entitySimpleClassNameUncap}:list")
	public R page(Integer page,Integer limit<#list cndPropertys as pc><#if pc.isDateProp==true>,String[] ${pc.property}<#else>,${pc.simpleClassName} ${pc.property}</#if></#list>) {
		Cnd cnd=Cnd.ofPageLimit(page, limit);//.add(M.Star.name, name);
		<#list cndPropertys as pc>
		<#if pc.isDateProp==true>
		if(${pc.property}!=null && ${pc.property}.length>0) {
			cnd.between(M.${entitySimpleClassName}.${pc.property},${pc.property}[0], ${pc.property}[1]);
		}
		<#else>
		cnd.add(M.${entitySimpleClassName}.${pc.property}, ${pc.property});
		</#if>		
		</#list>

		Page<${entitySimpleClassName}> pager=${simpleClassNameFirstLower}Service.page(cnd);
		return R.ok().data(pager);
	}

	

	@RequestMapping("/${simpleClassNameFirstLower}/get/{id}")
	@ResponseBody
	@RequiresPermissions("${module}:${entitySimpleClassNameUncap}:get")
	public R get(@PathVariable("id")${idClassName} id) {
		${entitySimpleClassName} ${simpleClassNameFirstLower}=${simpleClassNameFirstLower}Service.getById(id);
		return R.ok().data(${simpleClassNameFirstLower});
	}
	
	@RequestMapping("/${simpleClassNameFirstLower}/create")
	@ResponseBody
	@RequiresPermissions("${module}:${entitySimpleClassNameUncap}:create")
	public R create(@RequestBody ${entitySimpleClassName} ${simpleClassNameFirstLower}) {
		${simpleClassNameFirstLower}Service.create(${simpleClassNameFirstLower});
		return R.ok().data(${simpleClassNameFirstLower});
	}
	
	@RequestMapping("/${simpleClassNameFirstLower}/update")
	@ResponseBody
	@RequiresPermissions("${module}:${entitySimpleClassNameUncap}:update")
	public  R update(@RequestBody ${entitySimpleClassName} ${simpleClassNameFirstLower}) {
		${simpleClassNameFirstLower}Service.update(${simpleClassNameFirstLower});
		return R.ok().data(${simpleClassNameFirstLower});
	}
	
	@RequestMapping("/${simpleClassNameFirstLower}/removeById/{id}")
	@ResponseBody
	@RequiresPermissions("${module}:${entitySimpleClassNameUncap}:remove")
	public R removeById(@PathVariable("id")${idClassName} id) {
		${simpleClassNameFirstLower}Service.removeById(id);
		return R.ok().data(id);
	}
	
	@RequestMapping("/${simpleClassNameFirstLower}/removeByIds")
	@ResponseBody
	@RequiresPermissions("${module}:${entitySimpleClassNameUncap}:remove")
	public R removeByIds(@RequestBody ${idSimpleClassName}[] ids) {
		${simpleClassNameFirstLower}Service.removeByIds(ids);
		return R.ok().data(ids);
	}
	
	@RequestMapping("/${simpleClassNameFirstLower}/remove")
	@ResponseBody
	@RequiresPermissions("${module}:${entitySimpleClassNameUncap}:remove")
	public R remove(@RequestBody ${entitySimpleClassName} ${simpleClassNameFirstLower}) {
		${simpleClassNameFirstLower}Service.remove(${simpleClassNameFirstLower});
		return R.ok().data(${simpleClassNameFirstLower});
	}
	
	
}
