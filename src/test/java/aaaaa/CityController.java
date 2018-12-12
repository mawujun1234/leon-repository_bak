package aaaaa;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mawujun.mvc.R;
import com.mawujun.repository.utils.PageInfo;
import com.mawujun.repository.utils.Params;

import test.mawujun.jpa.utils.M;
import test.mawujun.model.City;

/**
 * @author mawujun qq:16064988 e-mail:16064988@qq.com 
 * @version 1.0
 * @since 1.0
 */
@Controller
//@RequestMapping("/city")
public class CityController {

	@Resource
	private CityService cityService;


	/**
	 * 请按自己的需求修改
	 * @author mawujun email:16064988@163.com qq:16064988
	 * @param id 是父节点的id
	 * @return
	 */
	@RequestMapping("/city/list")
	@ResponseBody
	public R list(String id) {
		Params params=Params.of().like(M.City.name, "test");
		List<City> cityes=cityService.listByMap(params);
		return R.ok(cityes);
	}

	/**
	 * 这是基于分页的几种写法,的例子，请按自己的需求修改
	 * @author mawujun email:16064988@163.com qq:16064988
	 * @param start
	 * @param limit
	 * @param userName
	 * @return
	 */
	@RequestMapping("/city/listPage")
	@ResponseBody
	public R listPage(Integer start,Integer limit,String sampleName){
		PageInfo<City> pageinfo=PageInfo.of(start,limit).eq(M.City.name, "宁波");
		cityService.listPageByPageInfo(pageinfo);
		return R.ok().data(pageinfo);
	}

	@RequestMapping("/city/listAll")
	@ResponseBody
	public R listAll() {	
		List<City> cityes=cityService.listAll();
		return R.ok().data(cityes);
	}
	

	@RequestMapping("/city/get")
	public R get(java.lang.String id) {
		City city=cityService.getById(id);
		return R.ok().data(city);
	}
	
	@RequestMapping("/city/create")
	@ResponseBody
	public R create(@RequestBody City city) {
		cityService.create(city);
		return R.ok().data(city);
	}
	
	@RequestMapping("/city/update")
	@ResponseBody
	public  R update(@RequestBody City city) {
		cityService.update(city);
		return R.ok().data(city);
	}
	
	@RequestMapping("/city/removeById")
	@ResponseBody
	public R removeById(java.lang.String id) {
		cityService.removeById(id);
		return R.ok().data(id);
	}
	
	@RequestMapping("/city/removeByIds")
	@ResponseBody
	public R removeByIds(String[] ids) {
		cityService.removeByIds(ids);
		return R.ok().data(ids);
	}
	
	@RequestMapping("/city/remove")
	@ResponseBody
	public City remove(@RequestBody City city) {
		cityService.remove(city);
		return city;
	}
	
	
}
