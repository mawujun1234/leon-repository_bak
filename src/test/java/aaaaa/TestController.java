package com.mawujun.controller;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mawujun.mvc.R;
import com.mawujun.repository.utils.PageInfo;
import com.mawujun.repository.utils.Params;

import com.mawujun.model.Test;
import com.mawujun.service.TestService;

	import TestId;
/**
 * @author mawujun qq:16064988 e-mail:16064988@qq.com 
 * @version 1.0
 * @since 1.0
 */
@Controller
//@RequestMapping("/test")
public class TestController {

	@Resource
	private TestService testService;


	/**
	 * 请按自己的需求修改
	 * @author mawujun email:16064988@163.com qq:16064988
	 * @param id 是父节点的id
	 * @return
	 */
	@RequestMapping("/test/list")
	@ResponseBody
	public R list() {//括号里面写参数
		Params params=Params.of();//Params.of().like(M.Test.name, "test");
		List<Test> testes=testService.listByMap(params);
		return R.ok(testes);
	}

	/**
	 * 这是基于分页的几种写法,的例子，请按自己的需求修改
	 * @author mawujun email:16064988@163.com qq:16064988
	 * @param start
	 * @param limit
	 * @param userName
	 * @return
	 */
	@RequestMapping("/test/listPage")
	@ResponseBody
	public R listPage(Integer start,Integer limit){
		//PageInfo<Test> pageinfo=PageInfo.of(start,limit).eq(M.Test.name, "宁波");
		PageInfo<Test> pageinfo=PageInfo.of(start,limit);
		testService.listPageByPageInfo(pageinfo);
		return R.ok().data(pageinfo);
	}

	@RequestMapping("/test/listAll")
	@ResponseBody
	public R listAll() {	
		List<Test> testes=testService.listAll();
		return R.ok().data(testes);
	}
	

	@RequestMapping("/test/get")
	@ResponseBody
	public R get(com.mawujun.model.TestId id) {
		Test test=testService.getById(id);
		return R.ok().data(test);
	}
	
	@RequestMapping("/test/create")
	@ResponseBody
	public R create(@RequestBody Test test) {
		testService.create(test);
		return R.ok().data(test);
	}
	
	@RequestMapping("/test/update")
	@ResponseBody
	public  R update(@RequestBody Test test) {
		testService.update(test);
		return R.ok().data(test);
	}
	
	@RequestMapping("/test/removeById")
	@ResponseBody
	public R removeById(com.mawujun.model.TestId id) {
		testService.removeById(id);
		return R.ok().data(id);
	}
	
	@RequestMapping("/test/removeByIds")
	@ResponseBody
	public R removeByIds(String[] ids) {
		testService.removeByIds(ids);
		return R.ok().data(ids);
	}
	
	@RequestMapping("/test/remove")
	@ResponseBody
	public Test remove(@RequestBody Test test) {
		testService.remove(test);
		return test;
	}
	
	
}
