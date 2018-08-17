package com.caolei.testpojo;

import java.io.Serializable;

/**
 * @ClassName: BufferedImg
 * @Description: TODO
 * @author caolei
 * @date 2018/8/16 18:00
 */
public class Student implements Serializable {
    private String name;

    private Teacher teacher;

    public Student() {
    }

    public Student(String name, Teacher teacher) {
        this.name = name;
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
