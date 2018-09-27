package com.github.cloud0072.base.handle;


import com.github.cloud0072.base.exception.AjaxException;
import com.github.cloud0072.common.util.SecurityUtils;
import com.github.cloud0072.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private GlobalExceptionHandler() {
    }

    private void addErrorMessage(ModelAndView modelAndView, Exception ex) {
        String message = StringUtils.isEmpty(ex.getMessage()) ? "您的请求出错了!" : ex.getMessage();
        modelAndView.addObject("error", message);
    }

    private void printErrorMessage(Exception ex) {
        log.error(ex.getStackTrace()[0].toString());
        log.error(ex.getClass().getName());
        log.error(ex.getMessage() + "\n");
    }

    /**
     * 认证异常统一返回登录页重新登录
     * 可接受所有的 AuthenticationException 及其子类的异常
     * code = 401
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {AuthenticationException.class})
    public ModelAndView authenticationExceptionHandler(HttpServletRequest request, HttpServletResponse response,
                                                       Exception ex) {
        ModelAndView modelAndView = new ModelAndView("forward:/prepare_login");

        if (ex instanceof BadCredentialsException) {
            ex = new BadCredentialsException("用户名或密码错误");
        } else if (ex instanceof UsernameNotFoundException) {
            ex = new UsernameNotFoundException("该用户不存在");
        } else if (ex instanceof DisabledException) {
            ex = new DisabledException("用户已被禁用");
        }

        addErrorMessage(modelAndView, ex);
        printErrorMessage(ex);

        return modelAndView;
    }

    /**
     * 自定义ajaxException
     * 返回预设的json数据
     * code=400
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {AjaxException.class})
    @ResponseBody
    public ResponseEntity<Map> ajaxExceptionHandler(HttpServletRequest request, HttpServletResponse response,
                                                    Exception ex) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("message", ex.getMessage());

        printErrorMessage(ex);

        return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * 全局异常处理逻辑...默认返回 错误页
     * code=500
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public ModelAndView exceptionHandler(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Exception ex) {
        if (!SecurityUtils.isAuthenticated()) {
            return authenticationExceptionHandler(request,
                    response, new UnsupportedOperationException("账号信息异常,请先登录"));
        }

        ModelAndView modelAndView = new ModelAndView("500");
        addErrorMessage(modelAndView, ex);
        printErrorMessage(ex);

        return modelAndView;
    }

}