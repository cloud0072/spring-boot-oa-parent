package com.caolei.webtest.controller;

import com.caolei.base.exception.AjaxException;
import com.caolei.base.pojo.User;
import com.caolei.base.service.UserService;
import com.caolei.common.api.controller.BaseController;
import com.caolei.common.util.DateUtils;
import com.caolei.common.util.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController implements BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation("测试DateUtils是否可用")
    @GetMapping("/01")
    public Object test01(HttpServletRequest request, HttpServletResponse response) {
        return DateUtils.parseDayOfWeek("一");
    }

    @ApiOperation("测试AjaxExceptionHandler是否可用")
    @GetMapping("/02")
    public Object test02(HttpServletRequest request, HttpServletResponse response) {
        throw new AjaxException("AjaxException Test");
    }

    @ApiOperation("测试RunTimeExceptionHandler是否可用")
    @GetMapping("/03")
    public Object test03(HttpServletRequest request, HttpServletResponse response) {
        User u = null;
        u.getRoles();
        Integer i = 1 / 0;
        return null;
    }

    @ApiOperation("恢复admin密码为admin")
    @GetMapping("/06")
    public Object test06(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findUserByAccount("admin");
        user.setPassword("admin");
        userService.update(user);

        String catalina = System.getProperty("catalina.home");
        String uploadPath = FileUtils.uploadPath();
        return uploadPath;
    }


}
