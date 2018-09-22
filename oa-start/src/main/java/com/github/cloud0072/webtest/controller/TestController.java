package com.github.cloud0072webtest.controller;

import com.github.cloud0072.base.controller.BaseController;
import com.github.cloud0072.base.exception.AjaxException;
import com.github.cloud0072.base.model.User;
import com.github.cloud0072.base.service.UserService;
import com.github.cloud0072.common.autoconfig.LocationProperties;
import com.github.cloud0072.common.autoconfig.ShiroProperties;
import com.github.cloud0072.common.util.DateUtils;
import com.github.cloud0072.common.util.FileUtils;
import com.github.cloud0072.common.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


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
    @Autowired
    private ShiroProperties shiroProperties;
    @Autowired
    private LocationProperties location;
    @Value("spring.resources.static-locations")
    private String staticLocation;

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

    @ApiOperation("测试环境变量是否正确引入")
    @GetMapping("/04")
    public Object test04(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("shiro", shiroProperties);
        map.put("location", location);
        map.put("staticLocation", staticLocation);
        return map;
    }

    @ApiOperation("测试shiro通配符范围")
    @GetMapping("/05")
    public ResponseEntity test05(HttpServletRequest request, HttpServletResponse response) {
        String p1 = "base:entity:123:UPDATE";
        String p2 = "base:entity:123:FIND";
        String p3 = "base:entity:123:*";
        String p4 = "base:entity:*";
        String p5 = "base:entity:*:FIND";
        String p6 = "base:entity:*:UPDATE";

        log.info(p1 + "\t" + SecurityUtils.hasPermission(p1));
        log.info(p2 + "\t" + SecurityUtils.hasPermission(p2));
        log.info(p3 + "\t" + SecurityUtils.hasPermission(p3));
        log.info(p4 + "\t" + SecurityUtils.hasPermission(p4));
        log.info(p5 + "\t" + SecurityUtils.hasPermission(p5));
        log.info(p6 + "\t" + SecurityUtils.hasPermission(p6));

        return ResponseEntity.ok("");
    }

    @ApiOperation("恢复admin密码为admin")
    @GetMapping("/06")
    public Object test06(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findUserByAccount("admin");
        user.setPassword("admin");
        userService.update(user);

        return FileUtils.getUploadPath();
    }

    @RequestMapping("/receive")
    @ResponseBody
    public ResponseEntity test07(HttpServletRequest request, HttpServletResponse response) {

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            log.info(key + ":\t" + request.getParameter(key));
        }

        return ResponseEntity.ok("接收成功");
    }

}
