package com.github.cloud0072.base.controller;

import com.github.cloud0072.base.service.UserService;
import com.github.cloud0072.base.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户登录控制器
 *
 * @author cloud0072
 * @date 2018/6/12 21:53
 */
@Controller
public class LoginController
        implements BaseController {

    @Autowired
    private UserService userService;

    /**
     * 登录页跳转
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 21:55
     */
    @RequestMapping(value = "/prepare_login",method = {RequestMethod.GET,RequestMethod.POST})
    public String prepareLogin() {
        return "login";
    }

    /**
     * 登录页跳转
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 21:55
     */
    @RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.POST})
    public String index() {
        return "index";
    }

}
