package com.caolei.system.api;

import com.caolei.system.constant.Constants;
import com.caolei.system.utils.ReflectUtils;
import com.caolei.system.utils.RequestUtils;
import org.springframework.data.domain.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.caolei.system.constant.Constants.*;

public interface BaseCrudController<T extends BaseEntity>
        extends BaseController {
    /**
     * 获取实例对应的服务
     *
     * @return
     */
    BaseCrudService<T> service();

    /**
     * 建议重写
     * 返回一个空对象实例
     * 用于查询和调用实例内的方法
     * @return
     */
    default T instance() {
        try {
            return (T) ReflectUtils.getInterfaceGenericType(getClass(), 0, 0).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException("泛型读取错误,请检查" + getClass().getName());
    }

    /**
     * 获取实例名
     * @return
     */
    default String entityName() {
        return instance().entityName();
    }

    /**
     * 获取模块名
     * @return
     */
    default String moduleName() {
        return instance().moduleName();
    }

    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    default String list(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                        @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                        @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                        T t, Model model) {
        RequestUtils.checkOperation(t.entityName(), OP_LIST);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.fromString(direction), sortField));
        Page<T> list = service().findAll(Example.of(t), pageable);
        model.addAttribute("page", list);
        model.addAttribute("search", t);
        model.addAttribute("op", OP_LIST);
        model.addAttribute("type", TY_ADMIN);
        modelAdvice(OP_LIST, t.getId(), t, model);
        return "/" + t.moduleName() + "/" + t.entityName() + "/" + t.entityName() + "_list";
    }

    /**
     * 跳转 查看 更新 或 删除对象页
     */
    @RequestMapping(value = "/{operation}/{id}", method = RequestMethod.GET)
    default String showForm(HttpServletRequest request, HttpServletResponse response,
                            @PathVariable("operation") String operation, @PathVariable("id") String id, T t, Model model) {
        RequestUtils.checkOperation(t.entityName(), operation, id);
        model.addAttribute(t.entityName(), service().findById(id));
        model.addAttribute("op", operation);
        model.addAttribute("type", TY_ADMIN);
        modelAdvice(operation, id, t, model);
        String prefix = operation.equals(OP_FIND) ? "_view" : "_edit";
        return "/" + t.moduleName() + "/" + t.entityName() + "/" + t.entityName() + prefix;
    }

    /**
     * 跳转创建对象页
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    default String showCreateForm(HttpServletRequest request, HttpServletResponse response,
                                  T t, Model model) {
        RequestUtils.checkOperation(t.entityName(), OP_CREATE);
        model.addAttribute(t.entityName(), t);
        model.addAttribute("op", OP_CREATE);
        model.addAttribute("type", TY_ADMIN);
        modelAdvice(OP_CREATE, null, t, model);
        return "/" + t.moduleName() + "/" + t.entityName() + "/" + t.entityName() + "_edit";
    }

    /**
     * 提交创建对象
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    default String create(HttpServletRequest request, HttpServletResponse response,
                          T t, RedirectAttributes redirectAttributes) {
        RequestUtils.checkOperation(t.entityName(), OP_CREATE);
        service().save(t);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return Constants.REDIRECT_TO + "/" + t.moduleName() + "/" + t.entityName() + "/list";
    }

    /**
     * 提交更新对象
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    default String update(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable("id") String id, T t, RedirectAttributes redirectAttributes) {
        RequestUtils.checkOperation(t.entityName(), OP_UPDATE, id);
        service().updateById(id, t);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return Constants.REDIRECT_TO + "/" + t.moduleName() + "/" + t.entityName() + "/find/" + id;
    }

    /**
     * 提交删除对象
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    default String delete(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable("id") String id, T t, RedirectAttributes redirectAttributes) {
        RequestUtils.checkOperation(t.entityName(), OP_DELETE, id);
        service().deleteById(id);
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return Constants.REDIRECT_TO + "/" + t.moduleName() + "/" + t.entityName() + "/list";
    }

    default void modelAdvice(String operation, String id, T t, Model model) {
    }

}
