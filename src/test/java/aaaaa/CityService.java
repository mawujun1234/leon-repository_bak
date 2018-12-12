package aaaaa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mawujun.service.BaseService;

import lombok.extern.slf4j.Slf4j;
import test.mawujun.model.City;



/**
 * @author mawujun qq:16064988 e-mail:16064988@qq.com 
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional(propagation=Propagation.REQUIRED)
@Slf4j
public class CityService  extends BaseService<CityRepository, City>{


}
