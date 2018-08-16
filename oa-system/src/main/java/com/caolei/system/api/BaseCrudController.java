package com.caolei.system.api;

import com.caolei.common.api.BaseEntity;
import com.caolei.common.api.BaseLogger;
import com.caolei.common.util.EntityUtils;
import com.caolei.system.util.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.data.domain.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.caolei.common.constant.Constants.*;

/**
 * @author caolei
 * @ClassName: BaseCrudController
 * @Description: TODO
 * @date 2018/7/25 11:41
 */
@RequiresAuthentication
public interface BaseCrudController<T extends BaseEntity> extends BaseLogger {

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
     * model增强方法
     * 需要重写
     *
     * @param model
     */
    default void modelAdvice(Model model) {
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
    default String list(HttpServletRequest request, HttpServletResponse response, T t,
                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                        @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                        @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                        Model model) {
        SecurityUtils.checkOperation(t, OP_LIST);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.fromString(direction), sortField));
        Page<T> list = service().findAll(Example.of(t), pageable);
        model.addAttribute("page", list);
        model.addAttribute("search", t);
        putModel(model, OP_LIST, t, TY_ADMIN);
        return "/" + moduleName() + "/" + entityName() + "/" + entityName() + "_list";
    }

    /**
     * 跳转创建对象页
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    default String showCreateForm(HttpServletRequest request, HttpServletResponse response, Model model) {
        SecurityUtils.checkOperation(instance(), OP_CREATE);
        putModel(model, OP_CREATE, instance(), TY_ADMIN);
        return "/" + moduleName() + "/" + entityName() + "/" + entityName() + "_edit";
    }

    /**
     * 跳转 查看 更新 或 删除对象页
     */
    @RequestMapping(value = "/{operation}/{id}", method = RequestMethod.GET)
    default String showForm(HttpServletRequest request, HttpServletResponse response, Model model,
                            @PathVariable("id") String id, @PathVariable("operation") String operation) {
        T t = service().findById(id);
        SecurityUtils.checkOperation(t, operation);
        putModel(model, operation, t, TY_ADMIN);
        String prefix = operation.equals(OP_FIND) ? "_view" : "_edit";
        return "/" + moduleName() + "/" + entityName() + "/" + entityName() + prefix;
    }

    /**
     * 提交创建对象
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    default String create(HttpServletRequest request, HttpServletResponse response, T t,
                          RedirectAttributes redirectAttributes) {
        SecurityUtils.checkOperation(t, OP_CREATE);
        service().save(t);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/list";
    }

    /**
     * 提交更新对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    default String update(HttpServletRequest request, HttpServletResponse response, T t,
                          RedirectAttributes redirectAttributes) {
        SecurityUtils.checkOperation(t, OP_UPDATE);
        service().updateById(t.getId(), t);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/find/" + t.getId();
    }

    /**
     * 提交删除对象
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    default String delete(HttpServletRequest request, HttpServletResponse response, T t,
                          RedirectAttributes redirectAttributes) {
        t = service().findById(t.getId());
        SecurityUtils.checkOperation(t, OP_DELETE);
        service().deleteById(t.getId());
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/list";
    }

    /**
     * 封装方法
     *
     * @param model
     * @param operation
     * @param t
     * @param type
     */
    default void putModel(Model model, String operation, T t, String type) {
        model.addAttribute(entityName(), t);
        model.addAttribute("id", t.getId());
        model.addAttribute("op", operation);
        model.addAttribute("type", type);
        model.addAttribute("moduleName", moduleName());
        model.addAttribute("entityName", entityName());
        modelAdvice(model);
    }
}
