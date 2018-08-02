package com.caolei.test;

import com.caolei.system.utils.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @ClassName: TestController
 * @Description: TODO
 * @author caolei
 * @date 2018/8/2 19:17
 */

@RequestMapping("/test")
@RestController
public class TestController {

    @RequestMapping("/01")
    public Object test01(HttpServletRequest request, HttpServletResponse response) {
        return DateUtils.parseDayOfWeek("一");
    }


}
