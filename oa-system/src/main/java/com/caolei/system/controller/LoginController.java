package com.caolei.system.controller;

import com.caolei.system.pojo.User;
import com.caolei.system.service.UserService;
import com.caolei.system.api.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.caolei.common.constant.Constants.FORWARD_TO;

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
     * 登陆
     *
     * @param user
     * @return
     * @author cloud0072
     * @date 2018/6/12 21:53
     */
    @RequestMapping("/login")
    public String login(User user) {
        if (userService.login(user)) {
            return "index";
        }

        return FORWARD_TO + "/prepare_login";
    }

    /**
     * 退出登录
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 21:53
     */
    @RequestMapping("/logout")
    public String logout() {
        userService.logout();
        return FORWARD_TO + "/prepare_login";
    }

    /**
     * 登录页跳转
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 21:55
     */
    @RequestMapping("/prepare_login")
    public String prepareLogin() {
        return "login";
    }


}
