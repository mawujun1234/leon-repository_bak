package test.mawujun.jpa;

import org.apache.ibatis.annotations.Mapper;

import com.mawujun.repository.mybatis.IRepository;

import test.mawujun.model.CoplxId2;
import test.mawujun.model.CoplxId2Entity;

@Mapper
public interface CoplxId2EntityMapper extends IRepository<CoplxId2Entity,CoplxId2>{


}
