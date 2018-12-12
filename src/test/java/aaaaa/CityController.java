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
	@RequestMapping("/city/query")
	@ResponseBody
	public R query(Integer start,Integer limit,String sampleName){
		PageInfo<City> pageinfo=PageInfo.of(start,limit).eq(M.City.name, "宁波");
		cityService.listPageByPageInfo(pageinfo);
		return R.ok().data(pageinfo);
	}

	@RequestMapping("/city/listAll")
	@ResponseBody
	public List<City> listAll() {	
		List<City> cityes=cityService.listAll();
		return cityes;
	}
	

	@RequestMapping("/city/get")
	public City get(java.lang.String id) {
		return cityService.getById(id);
	}
	
	@RequestMapping("/city/create")
	@ResponseBody
	public City create(@RequestBody City city) {
		cityService.create(city);
		return city;
	}
	
	@RequestMapping("/city/update")
	@ResponseBody
	public  City update(@RequestBody City city) {
		cityService.update(city);
		return city;
	}
	
	@RequestMapping("/city/removeById")
	@ResponseBody
	public java.lang.String removeById(java.lang.String id) {
		cityService.removeById(id);
		return id;
	}
	
	@RequestMapping("/city/removeById")
	@ResponseBody
	public java.lang.String removeById(String[] ids) {
		cityService.removeByIds(ids);
		return id;
	}
	
	@RequestMapping("/city/remove")
	@ResponseBody
	public City remove(@RequestBody City city) {
		cityService.remove(city);
		return city;
	}
	
	
}
