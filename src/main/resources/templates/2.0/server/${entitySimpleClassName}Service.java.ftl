<#assign simpleClassNameFirstLower = entitySimpleClassName?uncap_first> 
package ${basepackage}.service;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.mawujun.service.BaseService;

import lombok.extern.slf4j.Slf4j;


import ${entityClassName};
import ${basepackage}.repository.${entitySimpleClassName}Repository;


<#include "/java_copyright.include"/>

@Service
@Transactional(propagation=Propagation.REQUIRED)
<#if uselombok==true>
@Slf4j
</#if>
public class ${entitySimpleClassName}Service  extends BaseService<${entitySimpleClassName}Repository, ${entitySimpleClassName}> implements I${entitySimpleClassName}Service{


}
