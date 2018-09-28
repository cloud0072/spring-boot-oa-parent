package com.github.cloud0072.base.controller;

import com.github.cloud0072.base.model.BaseEntity;
import com.github.cloud0072.base.service.BaseCrudService;
import com.github.cloud0072.common.annotation.EntityInfo;
import com.github.cloud0072.common.annotation.ModuleInfo;
import com.github.cloud0072.common.constant.Operation;
import com.github.cloud0072.common.util.ReflectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.github.cloud0072.common.constant.Constants.TY_ADMIN;

/**
 * @author caolei
 * @ClassName: BaseCrudController
 * @Description: TODO
 * @date 2018/7/25 11:41
 */
@Api("基础增删改查方法的抽象类")
public abstract class BaseCrudController<T extends BaseEntity> implements BaseController {

    public final Class<T> persistentClass = ReflectUtils.getClassGenericType(getClass(), 0);
    public final String entityName = AnnotationUtils.findAnnotation(persistentClass, EntityInfo.class).entityName();
    public final String entityPath = AnnotationUtils.findAnnotation(persistentClass, EntityInfo.class).entityPath();
    public final String modulePath = AnnotationUtils.findAnnotation(persistentClass, ModuleInfo.class).modulePath();

    @ApiOperation("返回一个当前控制器对应实体的实例")
    protected T instance() {
        try {
            return persistentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @ApiOperation("将参数放入model中")
    protected void putModel(Model model, Operation operation, T t) {
        model.addAttribute(entityName, t);
        model.addAttribute("id", t.getId());
        model.addAttribute("op", operation);
        model.addAttribute("type", TY_ADMIN);
        model.addAttribute("modulePath", modulePath);
        model.addAttribute("entityPath", entityPath);
        modelAdvice(model, operation, t);
    }

    @ApiOperation("model增强方法,给子类扩展添加到model中的数据")
    protected void modelAdvice(Model model, Operation operation, T t) {
    }

    @ApiOperation("获取实例对应的服务")
    abstract BaseCrudService<T> service();

    /******************************************************************************************************************/

    @ApiOperation("跳转 创建 或删除对象页")
    @GetMapping(value = "/create")
    protected String showCreatePage(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Model model) {
//        MySecurityUtils.checkOperation(entityName, Operation.POST);
        T t = instance();
        putModel(model, Operation.POST, t);
        return modulePath + entityPath + entityPath + "_edit";
    }

    @ApiOperation("跳转 编辑 或删除对象页")
    @GetMapping(value = "/update/{id}")
    protected String showUpdatePage(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable("id") @NonNull String id,
                                    Model model) {
//        MySecurityUtils.checkOperation(entityName, Operation.PUT, id);
        T t = service().findById(id);
        putModel(model, Operation.PUT, t);
        return modulePath + entityPath + entityPath + "_edit";
    }

    @ApiOperation("跳转 查看 或删除对象页")
    @GetMapping(value = "/view/{id}")
    protected String showViewPage(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @PathVariable("id") @NonNull String id,
                                  Model model) {
        T t = service().findById(id);
        putModel(model, Operation.GET, t);
        return modulePath + entityPath + entityPath + "_view";
    }

    @ApiOperation("查询所有对象")
    @GetMapping("/list")
    protected String showListPage(HttpServletRequest request,
                                  HttpServletResponse response,
                                  T t,
                                  @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                  @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                  @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                  Model model) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.fromString(direction), sortField));
        Page<T> list = service().findAll(Example.of(t), pageable);
        model.addAttribute("page", list);
        model.addAttribute("search", t);
        putModel(model, Operation.GET, t);
        return modulePath + entityPath + entityPath + "_list";
    }

    /******************************************************************************************************************/

    @ApiOperation("提交创建对象")
    @PostMapping
    @ResponseBody
    protected ResponseEntity create(HttpServletRequest request,
                                    HttpServletResponse response,
                                    T t) {
        t = service().save(t, request, response);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "新增成功");
        map.put("url", modulePath + entityPath + "/view/" + t.getId());
        return ResponseEntity.ok(map);
    }

    @ApiOperation("提交删除对象")
    @DeleteMapping("/{id}")
    @ResponseBody
    protected ResponseEntity delete(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable("id") @NonNull String id) {
        service().deleteById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "删除成功");
        map.put("url", modulePath + entityPath + "/list");
        return ResponseEntity.ok(map);
    }

    @ApiOperation("提交更新对象")
    @PutMapping("/{id}")
    @ResponseBody
    protected ResponseEntity update(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable("id") @NonNull String id,
                                    T t) {
        t = service().update(t, request, response);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "修改成功");
        map.put("url", modulePath + entityPath + "/view/" + t.getId());
        return ResponseEntity.ok(map);
    }

    @ApiOperation("查询对象")
    @GetMapping("/{id}")
    @ResponseBody
    protected ResponseEntity find(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @PathVariable("id") @NonNull String id) {
        T t = service().findById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "查询成功");
        map.put(entityName, t);
        return ResponseEntity.ok(map);
    }

    @ApiOperation("根据对象的属性查询所有对象")
    @GetMapping
    @ResponseBody
    protected ResponseEntity findAll(HttpServletRequest request,
                                     HttpServletResponse response,
                                     T t) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "查询成功");
        map.put(entityName, service().findAll(Example.of(t)));
        return ResponseEntity.ok(map);
    }

}