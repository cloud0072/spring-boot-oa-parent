package com.caolei.common.api.controller;

import com.caolei.common.api.entity.BaseEntity;
import com.caolei.common.api.service.BaseCrudService;
import com.caolei.common.util.ReflectUtils;
import com.caolei.common.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public abstract class BaseRestController<T extends BaseEntity> implements BaseController {

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

    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    protected Object list(HttpServletRequest request, HttpServletResponse response, T t,
                          @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                          @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                          @RequestParam(value = "sortField", defaultValue = "id") String sortField) {
//        SecurityUtils.checkOperation(t, OP_LIST);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.fromString(direction), sortField));
        Page<T> list = service().findAll(Example.of(t), pageable);
        return ResponseEntity.ok(list);
    }

    @RequestMapping(value = "/find/${id}", method = RequestMethod.GET)
    protected Object find(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable(name = "id") String id) {
        T t = service().findById(id);
//        SecurityUtils.checkOperation(t, OP_FIND);
        return ResponseEntity.ok(t);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    protected Object create(HttpServletRequest request, HttpServletResponse response, T t) {
//        SecurityUtils.checkOperation(t, OP_CREATE);
        t = service().save(t, request, response);
        return ResponseEntity.ok(t);
    }

    /**
     * 提交更新对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    protected Object update(HttpServletRequest request, HttpServletResponse response, T t) {
//        SecurityUtils.checkOperation(t, OP_UPDATE);
        t = service().update(t, request, response);
        return ResponseEntity.ok(t);
    }

    /**
     * 提交删除对象
     */
    @RequestMapping(value = "/delete/${id}", method = RequestMethod.POST)
    protected Object delete(HttpServletRequest request, HttpServletResponse response,
                            @PathVariable(name = "id") String id) {
        T t = service().findById(id);
//        SecurityUtils.checkOperation(t, OP_DELETE);
        service().deleteById(id);
        return ResponseEntity.ok("delete success!");
    }

}
