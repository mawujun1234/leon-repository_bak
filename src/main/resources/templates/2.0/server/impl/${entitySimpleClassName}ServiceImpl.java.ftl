<#assign simpleClassNameFirstLower = entitySimpleClassName?uncap_first> 
package ${basepackage}.service.impl;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.mawujun.service.BaseService;
import com.mawujun.biz.service.${entitySimpleClassName}Service;

import lombok.extern.slf4j.Slf4j;


import ${entityClassName};
import ${basepackage}.repository.${entitySimpleClassName}Repository;


<#include "/java_copyright.include"/>

@Service
@Transactional(propagation=Propagation.REQUIRED)
<#if uselombok==true>
@Slf4j
</#if>
public class ${entitySimpleClassName}ServiceImpl  extends BaseService<${entitySimpleClassName}Repository, ${entitySimpleClassName}> implements ${entitySimpleClassName}Service{


}
