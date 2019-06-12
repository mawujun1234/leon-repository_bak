  
<#assign simpleClassNameFirstLower = entitySimpleClassName?uncap_first> 
package ${basepackage}.controller;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import javax.annotation.Resource;
import com.mawujun.common.utils.M;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mawujun.mvc.R;
import com.mawujun.repository.utils.Page;
import com.mawujun.repository.utils.Cnd;

import ${entityClassName};
import ${basepackage}.service.${entitySimpleClassName}Service;

<#if isCompositeId==true>
	import ${idSimpleClassName};
</#if>
<#include "/java_copyright.include"/>

<#if uselombok==true>
@Slf4j
</#if>
@Controller
@RequestMapping("/${module}")
public class ${entitySimpleClassName}Controller {

	@Resource
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
//	public R list() {//括号里面写参数
//		Cnd cnd=Cnd.of();//Cnd.of().like(M.${entitySimpleClassName}.name, "test");
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
//	public R page(Integer start,Integer limit<#list cndPropertys as pc>,${pc.simpleClassName} ${pc.property}<#if pc?has_next>,</#if></#list>) {
//	//public R list(@RequestParam Map<String, Object> params){
//	//	//Page<${entitySimpleClassName}> pager = ${simpleClassNameFirstLower}Service.queryPage(params);
//		Page<${entitySimpleClassName}> pager=${simpleClassNameFirstLower}Service.page(Cnd.ofStartLimit(start, limit)
//			<#list cndPropertys as pc>
//			.add(M.${entitySimpleClassName}.${pc.property}, ${pc.property})
//			</#list>
//			);
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
	public R page(Integer page,Integer limit<#list cndPropertys as pc>,${pc.simpleClassName} ${pc.property}<#if pc?has_next>,</#if></#list>) {
		Page<${entitySimpleClassName}> pager=${simpleClassNameFirstLower}Service.page(Cnd.ofPageLimit(page, limit)
			<#list cndPropertys as pc>
			.add(M.${entitySimpleClassName}.${pc.property}, ${pc.property})
			</#list>
			);
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
	public ${entitySimpleClassName} remove(@RequestBody ${entitySimpleClassName} ${simpleClassNameFirstLower}) {
		${simpleClassNameFirstLower}Service.remove(${simpleClassNameFirstLower});
		return ${simpleClassNameFirstLower};
	}
	
	
}
