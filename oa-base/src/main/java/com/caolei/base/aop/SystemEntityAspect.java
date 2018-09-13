package com.caolei.base.aop;

import com.caolei.base.util.UserUtils;
import com.caolei.base.entity.SystemEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

//@Aspect
//@Component
@Slf4j
public class SystemEntityAspect {

    @Pointcut("execution(* org.springframework.data.jpa.repository.support.SimpleJpaRepository.delete(..))")
    public void delete() {
    }

    @Before("delete()")
    public void before(JoinPoint joinPoint) {
        Object o = joinPoint.getArgs()[0];
        if (o instanceof SystemEntity) {
            SystemEntity entity = (SystemEntity) o;
            //非超级管理员无权删除系统实体
            if (entity.isSystemEntity() && !UserUtils.getCurrentUser().isSuperUser()) {
                throw new UnauthenticatedException("您无权删除系统数据");
            }
        }
    }
}
