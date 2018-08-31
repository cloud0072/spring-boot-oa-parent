package com.caolei.testpojo;


import java.io.Serializable;
import java.util.Set;

/**
 * @ClassName: Teacher
 * @Description: TODO
 * @author caolei
 * @date 2018/8/16 18:31
 */
public class Teacher implements Serializable {

    private String name;

    private Set<Student> students;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
