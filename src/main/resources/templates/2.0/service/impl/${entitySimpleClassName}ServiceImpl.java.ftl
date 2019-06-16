<#assign simpleClassNameFirstLower = entitySimpleClassName?uncap_first> 
package ${basepackage}.service.impl;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.mawujun.service.BaseServiceImpl;
import ${basepackage}.service.${entitySimpleClassName}Service;

<#if extraCfg.uselombok?? && extraCfg.uselombok==true>
import lombok.extern.slf4j.Slf4j;
</#if>
import ${entityClassName};
import ${basepackage}.repository.${entitySimpleClassName}Repository;


<#include "/java_copyright.include"/>

@Service
@Transactional(propagation=Propagation.REQUIRED)
<#if extraCfg.uselombok?? && extraCfg.uselombok==true>
@Slf4j
</#if>
public class ${entitySimpleClassName}ServiceImpl  extends BaseServiceImpl<${entitySimpleClassName}Repository, ${entitySimpleClassName}> implements ${entitySimpleClassName}Service{


}
