//package com.caolei.system.mongodb;
//
//import org.springframework.data.annotation.Id;
//
///**
// * @ClassName: DynamicForm
// * @Description: TODO
// * @author caolei
// * @date 2018/8/21 10:21
// */
//public class DynamicForm {
//
//    @Id
//    private String id;
//    private String name;
//    private String formData;
//
//    public DynamicForm() {
//    }
//
//    public DynamicForm(String name, String formData) {
//        this.name = name;
//        this.formData = formData;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getFormData() {
//        return formData;
//    }
//
//    public void setFormData(String formData) {
//        this.formData = formData;
//    }
//
//    @Override
//    public String toString() {
//        return "DynamicForm{" +
//                "id='" + id + '\'' +
//                ", name='" + name + '\'' +
//                ", formData=" + formData +
//                '}';
//    }
//}
