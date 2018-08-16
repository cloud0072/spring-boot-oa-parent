package com.caolei.system.exception;

import com.caolei.common.api.BaseLogger;
import com.caolei.common.util.StringUtils;
import com.caolei.system.util.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler implements BaseLogger {

    private GlobalExceptionHandler() {
    }

    private void addErrorMessage(ModelAndView modelAndView, Exception ex) {
        String message = !StringUtils.isEmpty(ex.getMessage()) ? "您的请求出错了!" : ex.getMessage();
        modelAndView.addObject("error", message);
    }

    private void printErrorMessage(Exception ex) {
        logger().error(ex.getStackTrace()[0].toString());
        logger().error(ex.getClass().getName());
        logger().error(ex.getMessage());
    }

    /**
     * 认证异常统一返回登录页重新登录
     * 可接受所有的 AuthenticationException 及其子类的异常
     * code = 401
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {AuthenticationException.class})
    public ModelAndView authenticationExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {

        ModelAndView modelAndView = new ModelAndView("forward:/prepare_login");
        if (ex instanceof IncorrectCredentialsException) {
            ex = new IncorrectCredentialsException("用户名或密码错误");
        }

        addErrorMessage(modelAndView, ex);
        printErrorMessage(ex);

        return modelAndView;
    }

    /**
     * 授权异常返回错误页
     * 可接受所有的 AuthenticationException 及其子类的异常
     * code = 400
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {UnauthorizedException.class})
    public ModelAndView unauthorizedExceptionExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {

        ModelAndView modelAndView = new ModelAndView("500");

        addErrorMessage(modelAndView, ex);
        printErrorMessage(ex);

        return modelAndView;
    }

    /**
     * 自定义ajaxException
     * 返回预设的json数据
     * code=400
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {AjaxException.class})
    public ResponseEntity<Map> ajaxExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 400);
        resultMap.put("message", ex.getMessage());

        printErrorMessage(ex);

        return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * 全局异常处理逻辑...默认返回 错误页
     * code=500
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public ModelAndView exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        if (SecurityUtils.isSubjectAvailable() && !SecurityUtils.getSubject().isAuthenticated()) {
            return authenticationExceptionHandler(request, response, new AuthenticationException("账号信息异常,请先登录"));
        }
        ModelAndView modelAndView = new ModelAndView("500");

        addErrorMessage(modelAndView, ex);
        printErrorMessage(ex);

        return modelAndView;
    }

}
