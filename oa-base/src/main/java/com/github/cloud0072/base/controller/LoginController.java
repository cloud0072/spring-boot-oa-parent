//package com.github.cloud0072.base.controller;
//
//import com.github.cloud0072.base.model.User;
//import com.github.cloud0072.base.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import static com.github.cloud0072.common.constant.Constants.FORWARD_TO;
//
///**
// * 用户登录控制器
// *
// * @author cloud0072
// * @date 2018/6/12 21:53
// */
//@Controller
//public class LoginController
//        implements BaseController {
//
//    @Autowired
//    private UserService userService;
//
//    /**
//     * 登录页跳转
//     *
//     * @return
//     * @author cloud0072
//     * @date 2018/6/12 21:55
//     */
//    @RequestMapping(value = "/prepare_login", method = {RequestMethod.GET, RequestMethod.POST})
//    public String prepareLogin() {
//        return "login";
//    }
//
//    /**
//     * 登陆
//     *
//     * @param user
//     * @return
//     * @author cloud0072
//     * @date 2018/6/12 21:53
//     */
//    @PostMapping(value = "/login")
//    public String login(User user) {
//        if (userService.login(user)) {
//            return "index";
//        }
//
//        return FORWARD_TO + "/prepare_login";
//    }
//
//    /**
//     * 退出登录
//     *
//     * @return
//     * @author cloud0072
//     * @date 2018/6/12 21:53
//     */
//    @GetMapping(value = "/logout")
//    public String logout() {
//        userService.logout();
//        return FORWARD_TO + "/prepare_login";
//    }
//
//}
