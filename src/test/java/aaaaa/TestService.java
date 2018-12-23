package com.mawujun.service;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.mawujun.service.BaseService;

import lombok.extern.slf4j.Slf4j;


import com.mawujun.model.Test;
import com.mawujun.repository.TestRepository;


/**
 * @author mawujun qq:16064988 e-mail:16064988@qq.com 
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional(propagation=Propagation.REQUIRED)
@Slf4j
public class TestService  extends BaseService<TestRepository, Test> implements ITestService{


}
