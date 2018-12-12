package aaaaa;

import org.springframework.stereotype.Repository;
import java.util.UUID;
import com.mawujun.repository.mybatis.IRepository;
import org.apache.ibatis.annotations.Mapper;

import test.mawujun.model.City;
/**
 * @author mawujun qq:16064988 e-mail:16064988@qq.com 
 * @version 1.0
 * @since 1.0
 */
@Mapper
public interface CityRepository extends IRepository<City>{


}
