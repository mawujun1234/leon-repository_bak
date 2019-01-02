package test.mawujun.mvc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mawujun.exception.BusinessException;

@RestController
public class MvcController {
	@RequestMapping("/throwBusinessException")
	public String test() {
		if(true) {
			throw new BusinessException("业务异常");
		}
		return "hellow111123333333";
	}
	@RequestMapping("/throwException")
	public String test2() {
		if(true) {
			throw new NullPointerException("空指针异常");
		}
		
		return "hellow33";
	}
}
