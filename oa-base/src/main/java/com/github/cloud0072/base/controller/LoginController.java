package com.github.cloud0072.base.controller;

import com.github.cloud0072.base.service.UserService;
import com.github.cloud0072.common.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    @GetMapping("/prepare_login")
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
    @RequestMapping(value = "/index",method = {RequestMethod.GET,RequestMethod.POST})
    public String index() {
        return "index";
    }


}
