package com.caolei.base.aop;

import com.caolei.base.model.OperationLog;
import com.caolei.base.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 项目对shiro有一定的封装 避免直接使用shiro原有的方法
 * 切面只能切spring生成的对象 而且 不能切静态方法??? 太菜了吧
 *
 * @author caolei
 * @ClassName: WebLogAop
 * @Description: TODO
 * @date 2018/9/7 14:26
 */
@Aspect
@Configuration
@Slf4j
public class WebLogAspect {

    @Autowired
    OperationLogService operationLogService;

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 使用 BaseController+ 表示他的所有子类
     */
    @Pointcut("execution(* com.caolei.base.controller.BaseController+.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();

        // 记录下请求内容
        log.info("URL :\t" + request.getRequestURL().toString());
        log.info("IP :\t" + request.getRemoteAddr());
        log.info("HTTP_METHOD\t: " + request.getMethod());
        log.info("CLASS :\t" + signature.getDeclaringTypeName());
        log.info("METHOD :\t" + signature.getName());
        log.info("ARGS :\t" + Arrays.toString(joinPoint.getArgs()));
        try {
            operationLogService.save(new OperationLog(request), request, null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

//        // 验证权限
//        String path = WebUtils.getPathWithinApplication(request);
//        path = path.startsWith("/")? path.replaceFirst("/", "") : path;
//        if (!StringUtils.isEmpty(path)){
//            String permission = path.replaceAll("/",":");
//            SecurityUtils.getSubject().checkPermission(permission);
//        }

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
        log.error("TIME_SPEND : " + (System.currentTimeMillis() - startTime.get()) + "\n");
        startTime.remove();
    }

//  @Around() 方法进行切面 可以控制方法是否执行，和修改执行后的返回值
//

}
