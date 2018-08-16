package com.caolei.system.api;

import com.caolei.common.api.BaseEntity;
import com.caolei.common.util.EntityUtils;
import com.caolei.system.util.SecurityUtils;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.caolei.common.constant.Constants.*;

@RestController
public interface BaseRestController<T extends BaseEntity> extends BaseController {

    /**
     * 获取实例对应的服务
     *
     * @return
     */
    BaseCrudService<T> service();

    /**
     * 返回一个空对象实例
     * 用于查询和调用实例内的方法
     *
     * @return
     */
    default T instance() {
        return (T) EntityUtils.interfaceGenericTypeInstance(getClass(), 0, 0);
    }

    /**
     * 获取实例名
     *
     * @return
     */
    default String entityName() {
        return instance().entityName();
    }

    /**
     * 获取模块名
     *
     * @return
     */
    default String moduleName() {
        return instance().moduleName();
    }

    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    default Object list(HttpServletRequest request, HttpServletResponse response, T t,
                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                        @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                        @RequestParam(value = "sortField", defaultValue = "id") String sortField) {
        SecurityUtils.checkOperation(t, OP_LIST);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.fromString(direction), sortField));
        Page<T> list = service().findAll(Example.of(t), pageable);
        return ResponseEntity.ok(list);
    }

    @RequestMapping(value = "/find/${id}", method = RequestMethod.GET)
    default Object find(HttpServletRequest request, HttpServletResponse response,
                        @PathVariable(name = "id") String id) {
        T t = service().findById(id);
        SecurityUtils.checkOperation(t, OP_FIND);
        return ResponseEntity.ok(t);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    default Object create(HttpServletRequest request, HttpServletResponse response, T t) {
        SecurityUtils.checkOperation(t, OP_CREATE);
        t = service().save(t);
        return ResponseEntity.ok(t);
    }

    /**
     * 提交更新对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    default Object update(HttpServletRequest request, HttpServletResponse response, T t) {
        SecurityUtils.checkOperation(t, OP_UPDATE);
        t = service().updateById(t.getId(), t);
        return ResponseEntity.ok(t);
    }

    /**
     * 提交删除对象
     */
    @RequestMapping(value = "/delete/${id}", method = RequestMethod.POST)
    default Object delete(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable(name = "id") String id) {
        T t = service().findById(id);
        SecurityUtils.checkOperation(t, OP_DELETE);
        service().deleteById(id);
        return ResponseEntity.ok("delete success!");
    }

}
