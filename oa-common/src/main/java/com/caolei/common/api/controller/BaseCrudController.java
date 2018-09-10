package com.caolei.common.api.controller;

import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.annotation.ModuleInfo;
import com.caolei.common.api.entity.BaseEntity;
import com.caolei.common.api.service.BaseCrudService;
import com.caolei.common.util.ReflectUtils;
import com.caolei.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.caolei.common.constant.Constants.*;

/**
 * @author caolei
 * @ClassName: BaseCrudController
 * @Description: TODO
 * @date 2018/7/25 11:41
 */
@Api("基础增删改查方法的接口类")
@RequiresAuthentication
public abstract class BaseCrudController<T extends BaseEntity> implements BaseController {

    protected Class<T> persistentClass = persistentClass();
    protected String className = className();

    @ApiOperation("获取实例对应的服务")
    protected abstract BaseCrudService<T> service();

    @ApiOperation("返回一个当前控制器对应实体的类型")
    protected synchronized Class<T> persistentClass() {
        if (persistentClass == null) {
            try {
                persistentClass = ReflectUtils.getClassGenericType(getClass(), 0);
            } catch (Exception e) {
            }
        }

        return persistentClass;
    }

    @ApiOperation("获取类型名")
    protected synchronized String className() {
        if (StringUtils.isEmpty(className)) {
            assert persistentClass != null;
            className = StringUtils.toLowerCaseFirstOne(persistentClass.getSimpleName());
        }
        return className;
    }

    @ApiOperation("返回一个当前控制器对应实体的实例")
    protected T instance() {
        try {
            return persistentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @ApiOperation("获取实例名,和访问路径")
    protected String entityPath() {
        return AnnotationUtils.findAnnotation(persistentClass, EntityInfo.class).entityPath();
    }

    @ApiOperation("获取模块名,和访问路径")
    protected String modulePath() {
        return AnnotationUtils.findAnnotation(persistentClass, ModuleInfo.class).modulePath();
    }

    @ApiOperation("查询所有对象")
    @GetMapping("/list")
    protected String list(T t,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                          @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                          @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                          Model model) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.fromString(direction), sortField));
        Page<T> list = service().findAll(Example.of(t), pageable);
        model.addAttribute("page", list);
        model.addAttribute("search", t);
        putModel(model, OP_LIST, t, TY_ADMIN);
        return modulePath() + entityPath() + entityPath() + "_list";
    }

    @ApiOperation("跳转 创建 或删除对象页")
    @GetMapping(value = "/create")
    protected String showCreatePage(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Model model) {
        T t = instance();
//        SecurityUtils.checkOperation(t, operation);
        putModel(model, OP_CREATE, t, TY_ADMIN);
        return modulePath() + entityPath() + entityPath() + "_edit";
    }

    @ApiOperation("跳转 编辑 或删除对象页")
    @GetMapping(value = "/update/{id}")
    protected String showUpdatePage(@PathVariable("id") @NonNull String id,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    Model model) {
        T t = service().findById(id);
//        SecurityUtils.checkOperation(t, operation);
        putModel(model, OP_UPDATE, t, TY_ADMIN);
        return modulePath() + entityPath() + entityPath() + "_edit";
    }

    @ApiOperation("跳转 查看 或删除对象页")
    @GetMapping(value = "/view/{id}")
    protected String showViewPage(@PathVariable("id") @NonNull String id,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  Model model) {
        T t = id == null ? instance() : service().findById(id);
//        SecurityUtils.checkOperation(t, operation);
        putModel(model, OP_FIND, t, TY_ADMIN);
        return modulePath() + entityPath() + entityPath() + "_view";
    }

    @ApiOperation("根据对象的属性查询所有对象")
    @GetMapping
    protected ResponseEntity findAll(T t,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "查詢成功");
        map.put(className, service().findAll(Example.of(t)));
        return ResponseEntity.ok(map);
    }

    @ApiOperation("查询对象")
    @GetMapping("/{id}")
    @ResponseBody
    protected ResponseEntity find(@PathVariable("id") @NonNull String id,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        T t = service().findById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "查詢成功");
        map.put(className, t);
        return ResponseEntity.ok(map);
    }

    @ApiOperation("提交创建对象")
    @PostMapping
    @ResponseBody
    protected ResponseEntity create(T t,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
//        SecurityUtils.checkOperation(t, OP_CREATE);
        t = service().save(t, request, response);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "新增成功");
        map.put("url", modulePath() + entityPath() + "/view/" + t.getId());
        return ResponseEntity.ok(map);
    }

    @ApiOperation("提交更新对象")
    @PutMapping("/{id}")
    @ResponseBody
    protected ResponseEntity update(@PathVariable("id") @NonNull String id,
                                    T t,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
//        SecurityUtils.checkOperation(t, OP_UPDATE);
        t = service().update(t, request, response);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "修改成功");
        map.put("url", modulePath() + entityPath() + "/view/" + t.getId());
        return ResponseEntity.ok(map);
    }

    @ApiOperation("提交删除对象")
    @DeleteMapping("/{id}")
    @ResponseBody
    protected ResponseEntity delete(@PathVariable("id") @NonNull String id,
                                    T t,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        t = service().findById(t.getId());
//        SecurityUtils.checkOperation(t, OP_DELETE);

        service().deleteById(t.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("message", "删除成功");
        map.put("url", modulePath() + entityPath());
        return ResponseEntity.ok(map);
    }

    @ApiOperation("将参数放入model中")
    protected void putModel(Model model, String operation, T t, String type) {
        model.addAttribute(className, t);
        model.addAttribute("id", t.getId());
        model.addAttribute("op", operation);
        model.addAttribute("type", type);
        model.addAttribute("modulePath", modulePath());
        model.addAttribute("entityPath", entityPath());
        modelAdvice(model);
    }

    @ApiOperation("model增强方法,给子类扩展添加到model中的数据")
    protected void modelAdvice(Model model) {
    }
}
