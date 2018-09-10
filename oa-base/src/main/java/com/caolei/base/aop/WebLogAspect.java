package com.caolei.base.aop;

import com.caolei.base.pojo.OperationLog;
import com.caolei.base.service.OperationLogService;
import com.caolei.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * @author caolei
 * @ClassName: WebLogAop
 * @Description: TODO
 * @date 2018/9/7 14:26
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Autowired
    OperationLogService operationLogService;

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 使用 BaseController+ 表示他的所有子类
     */
    @Pointcut("execution(* com.caolei.common.api.controller.BaseController+.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());

        //接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();

        // 记录下请求内容
        log.info("URL : " + request.getRequestURL().toString());
        log.info("IP : " + request.getRemoteAddr());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("CLASS_METHOD : " + signature.getDeclaringTypeName() + "." + signature.getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        operationLogService.save(new OperationLog(request));
    }

    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " 执行成功");
        log.info("RESPONSE : " + ret);
        log.info("TIME_SPEND : " + (System.currentTimeMillis() - startTime.get()) + "\n");
        startTime.remove();
    }

    @AfterThrowing(value = "webLog()", throwing = "thr")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable thr) {
        log.error("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " 执行失败");
        log.error("TIME_SPEND : " + (System.currentTimeMillis() - startTime.get()));
        startTime.remove();
    }

}
