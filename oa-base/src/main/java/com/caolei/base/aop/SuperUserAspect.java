package com.caolei.base.aop;

import com.caolei.base.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 项目对shiro有一定的封装 避免直接使用shiro原有的方法
 */
@Aspect
@Configuration
@Slf4j
public class SuperUserAspect {
    /**
     * 如果不是管理员继续校验 如果是管理员则直接通过
     * 在增强处理方法体内，调用ProceedingJoinPoint的proceed()方法才会执行目标方法
     */
    public Object superUser(ProceedingJoinPoint jp, Object o) throws Throwable {
        if (!UserUtils.getCurrentUser().isSuperUser()) {
            return jp.proceed();
        } else {
            return o;
        }
    }

    @Pointcut("execution(boolean com.caolei.common.util.SecurityUtils.has*(..))")
    public void has() {
    }

    @Around("execution(void com.caolei.common.util.SecurityUtils.check*(..))")
    public Object aroundCheck(ProceedingJoinPoint jp) throws Throwable {
        return superUser(jp, null);
    }

    @Around("has()")
    public Object aroundHas(ProceedingJoinPoint jp) throws Throwable {
        return superUser(jp, true);
    }

    @Around("execution(boolean[] com.caolei.common.util.SecurityUtils.has*(..))")
    public Object aroundHas2(ProceedingJoinPoint jp) throws Throwable {
        boolean[] ret = new boolean[jp.getArgs().length];
        Arrays.fill(ret, true);
        return superUser(jp, ret);
    }

}

