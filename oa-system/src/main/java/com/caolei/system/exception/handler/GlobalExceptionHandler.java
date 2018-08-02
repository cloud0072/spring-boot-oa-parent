package com.caolei.system.exception.handler;

import com.caolei.system.api.BaseLogger;
import com.caolei.system.constant.Constants;
import com.caolei.system.utils.RequestUtils;
import com.caolei.system.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler implements BaseLogger {

    /**
     * 认证异常统一返回登录页重新登录
     * 可接受所有的 AuthenticationException 及其子类的异常
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {AuthenticationException.class})
    public ModelAndView authenticationExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ModelAndView modelAndView = new ModelAndView("login");
        String message = ex.getMessage();

        if (ex instanceof IncorrectCredentialsException) {
            message = "用户名或密码错误";
        }

        if (!StringUtils.isEmpty(message)) {
            error(message);
            modelAndView.addObject("error", message);
        }

        return modelAndView;
    }

    /**
     * 全局异常处理逻辑...默认返回 错误页
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public ModelAndView exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        if(RequestUtils.isSubjectAvailable()){
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                return authenticationExceptionHandler(request, response, new AuthenticationException("账号信息异常,请先登录"));
            }
        }

        ModelAndView modelAndView = new ModelAndView("error");
        String message = ex.getMessage();
        if (!StringUtils.isEmpty(message)) {
            error(message);
            modelAndView.addObject("error", message);
        } else {
            error(ex.getStackTrace()[0].toString());
            error(ex.getClass().getName());
        }

        return modelAndView;
    }

}
