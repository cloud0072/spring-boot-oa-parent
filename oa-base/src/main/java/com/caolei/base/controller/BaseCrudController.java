package com.caolei.base.controller;

import com.caolei.base.entity.BaseEntity;
import com.caolei.base.service.BaseCrudService;
import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.annotation.ModuleInfo;
import com.caolei.common.util.ReflectUtils;
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
@Api("基础增删改查方法的抽象类")
@RequiresAuthentication
public abstract class BaseCrudController<T extends BaseEntity> implements BaseController {

    protected final Class<T> persistentClass = ReflectUtils.getClassGenericType(getClass(), 0);
    protected final String entityName = AnnotationUtils.findAnnotation(persistentClass, EntityInfo.class).entityName();
    protected final String entityPath = AnnotationUtils.findAnnotation(persistentClass, EntityInfo.class).entityPath();
    protected final String modulePath = AnnotationUtils.findAnnotation(persistentClass, ModuleInfo.class).modulePath();

    @ApiOperation("返回一个当前控制器对应实体的实例")
    protected T instance() {
        try {
            return persistentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @ApiOperation("将参数放入model中")
    protected void putModel(Model model, String operation, T t) {
        model.addAttribute(entityName, t);
        model.addAttribute("id", t.getId());
        model.addAttribute("op", operation);
        model.addAttribute("type", TY_ADMIN);
        model.addAttribute("modulePath", modulePath);
        model.addAttribute("entityPath", entityPath);
        modelAdvice(model, operation, t);
    }

    @ApiOperation("model增强方法,给子类扩展添加到model中的数据")
    protected void modelAdvice(Model model, String operation, T t) {
    }

    @ApiOperation("获取实例对应的服务")
    protected abstract BaseCrudService<T> service();

    /******************************************************************************************************************/

    @ApiOperation("查询所有对象")
    @GetMapping("/list")
    protected String list(HttpServletRequest request,
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
        putModel(model, OP_LIST, t);
        return modulePath + entityPath + entityPath + "_list";
    }

    @ApiOperation("跳转 创建 或删除对象页")
    @GetMapping(value = "/create")
    protected String showCreatePage(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Model model) {
        T t = instance();
//        SecurityUtils.checkOperation(t, operation);
        putModel(model, OP_CREATE, t);
        return modulePath + entityPath + entityPath + "_edit";
    }

    @ApiOperation("跳转 编辑 或删除对象页")
    @GetMapping(value = "/update/{id}")
    protected String showUpdatePage(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable("id") @NonNull String id,
                                    Model model) {
        T t = service().findById(id);
//        SecurityUtils.checkOperation(t, operation);
        putModel(model, OP_UPDATE, t);
        return modulePath + entityPath + entityPath + "_edit";
    }

    @ApiOperation("跳转 查看 或删除对象页")
    @GetMapping(value = "/view/{id}")
    protected String showViewPage(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @PathVariable("id") @NonNull String id,
                                  Model model) {
        T t = id == null ? instance() : service().findById(id);
//        SecurityUtils.checkOperation(t, operation);
        putModel(model, OP_FIND, t);
        return modulePath + entityPath + entityPath + "_view";
    }

    /******************************************************************************************************************/

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

    @ApiOperation("提交创建对象")
    @PostMapping
    @ResponseBody
    protected ResponseEntity create(HttpServletRequest request,
                                    HttpServletResponse response,
                                    T t) {
//        SecurityUtils.checkOperation(t, OP_CREATE);
        t = service().save(t, request, response);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "新增成功");
        map.put("url", modulePath + entityPath + "/view/" + t.getId());
        return ResponseEntity.ok(map);
    }

    @ApiOperation("提交更新对象")
    @PutMapping("/{id}")
    @ResponseBody
    protected ResponseEntity update(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable("id") @NonNull String id,
                                    T t) {
//        SecurityUtils.checkOperation(t, OP_UPDATE);
        t = service().update(t, request, response);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "修改成功");
        map.put("url", modulePath + entityPath + "/view/" + t.getId());
        return ResponseEntity.ok(map);
    }

    @ApiOperation("提交删除对象")
    @DeleteMapping("/{id}")
    @ResponseBody
    protected ResponseEntity delete(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable("id") @NonNull String id,
                                    T t) {
        t = service().findById(t.getId());
//        SecurityUtils.checkOperation(t, OP_DELETE);

        service().deleteById(t.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("message", "删除成功");
        map.put("url", modulePath + entityPath);
        return ResponseEntity.ok(map);
    }

}
