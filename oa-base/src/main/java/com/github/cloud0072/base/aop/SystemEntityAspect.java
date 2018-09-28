package com.github.cloud0072.base.aop;

import com.github.cloud0072.base.model.SystemEntity;
import com.github.cloud0072.base.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;

/**
 * 暂时不生效 正在排查原因
 */
//@Aspect
//@Component
@Slf4j
public class SystemEntityAspect {

    @Pointcut("execution(* com.github.cloud0072.base.repository.BaseRepository+.delete(..))")
    public void delete() {
    }

    @Before("delete()")
    public void before(JoinPoint joinPoint) {
        Object o = joinPoint.getArgs()[0];
        if (o instanceof SystemEntity) {
            SystemEntity entity = (SystemEntity) o;
            //非超级管理员无权删除系统实体
            if (entity.isSystemEntity() && !UserUtils.getCurrentUser().isSuperUser()) {
                throw new AccessDeniedException("您无权删除系统数据");
            }
        }
    }
}
