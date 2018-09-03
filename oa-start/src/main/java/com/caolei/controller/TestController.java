package com.caolei.controller;

import com.caolei.common.util.DateUtils;
import com.caolei.common.util.FileUtils;
import com.caolei.system.exception.AjaxException;
import com.caolei.system.pojo.User;
import com.caolei.testpojo.Student;
import com.caolei.testpojo.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;


/**
 * @author caolei
 * @ClassName: TestController
 * @Description: TODO
 * @date 2018/8/2 19:17
 */
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {

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

    @RequestMapping("/05")
    public Object test05(HttpServletRequest request, HttpServletResponse response) {
        Teacher teacher = new Teacher();
        teacher.setName("李华");
        Set<Student> set = new HashSet<>();
        set.add(new Student("小明", teacher));
        set.add(new Student("小红", teacher));
        set.add(new Student("小黄", teacher));
        teacher.setStudents(set);
        log.info(teacher.toString());

        try {
            ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(
                    new File("D:/123/Person.txt")));
            oo.writeObject(teacher);
            oo.flush();
            oo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("序列化成功");
    }

    @RequestMapping("/06")
    public Object test06(HttpServletRequest request, HttpServletResponse response) {
        String catalina = System.getProperty("catalina.home");
        String uploadPath = FileUtils.uploadPath();
        return uploadPath;
    }


}
