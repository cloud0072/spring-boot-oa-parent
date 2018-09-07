package com.caolei.webtest.controller;

import com.caolei.common.util.DateUtils;
import com.caolei.common.util.FileUtils;
import com.caolei.system.api.BaseController;
import com.caolei.system.exception.AjaxException;
import com.caolei.system.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author caolei
 * @ClassName: TestController
 * @Description: TODO
 * @date 2018/8/2 19:17
 */
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController implements BaseController {

    @RequestMapping("/01")
    public Object test01(HttpServletRequest request, HttpServletResponse response) {
        return DateUtils.parseDayOfWeek("一");
    }

    @RequestMapping("/02")
    public Object test02(HttpServletRequest request, HttpServletResponse response) {
        throw new AjaxException("AjaxException Test");
    }

    @RequestMapping("/03")
    public Object test03(HttpServletRequest request, HttpServletResponse response) {
        User u = null;
        u.getRoles();
        Integer i = 1 / 0;
        return null;
    }

    @RequestMapping("/04")
    public Object test04(HttpServletRequest request, HttpServletResponse response) {
        return DateUtils.parseDayOfWeek("周一");
    }

    @RequestMapping("/06")
    public Object test06(HttpServletRequest request, HttpServletResponse response) {
        String catalina = System.getProperty("catalina.home");
        String uploadPath = FileUtils.uploadPath();
        return uploadPath;
    }


}
