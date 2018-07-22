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
import static com.caolei.system.constant.Constants.OP_DELETE;

public abstract class AbstractCrudController<T extends BaseEntity>
        implements BaseController {

    private T entity;

    /**
     * 获取实例对应的服务
     *
     * @return
     */
    protected abstract BaseCrudService<T> service();

    /**
     * model增强方法
     * 需要重写
     *
     * @param model
     */
    public void modelAdvice(Model model) {
    }

    /**
     * 返回一个空对象实例
     * 用于查询和调用实例内的方法
     *
     * @return
     */
    public T instance() {
        if (entity == null) {
            try {
                entity = (T) ReflectUtils.getClassGenericType(getClass(), 0).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                error(e.getMessage());
                throw new UnsupportedOperationException("泛型读取错误,请检查" + getClass().getName());
            }
        }
        return entity;
    }

    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                       @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                       @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                       Model model, T t) {
        RequestUtils.checkOperation(OP_LIST, t);
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
    public String showCreateForm(HttpServletRequest request, HttpServletResponse response,
                                 Model model) {
        RequestUtils.checkOperation(OP_CREATE, instance());
        putModel(model, OP_CREATE, instance(), TY_ADMIN);
        return "/" + moduleName() + "/" + entityName() + "/" + entityName() + "_edit";
    }

    /**
     * 跳转 查看 更新 或 删除对象页
     */
    @RequestMapping(value = "/{operation}/{id}", method = RequestMethod.GET)
    public String showForm(HttpServletRequest request, HttpServletResponse response,
                           @PathVariable("id") String id, @PathVariable("operation") String operation, Model model) {
        T t = service().findById(id);
        RequestUtils.checkOperation(operation, t);
        putModel(model, operation, t, TY_ADMIN);
        String prefix = operation.equals(OP_FIND) ? "_view" : "_edit";
        return "/" + moduleName() + "/" + entityName() + "/" + entityName() + prefix;
    }

    /**
     * 提交创建对象
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(HttpServletRequest request, HttpServletResponse response,
                         T t, RedirectAttributes redirectAttributes) {
        RequestUtils.checkOperation(OP_CREATE, t);
        service().save(t);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return Constants.REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/list";
    }

    /**
     * 提交更新对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(HttpServletRequest request, HttpServletResponse response,
                         T t, RedirectAttributes redirectAttributes) {
        RequestUtils.checkOperation(OP_UPDATE, t);
        service().updateById(t.getId(), t);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return Constants.REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/find/" + t.getId();
    }

    /**
     * 提交删除对象
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(HttpServletRequest request, HttpServletResponse response,
                         T t, RedirectAttributes redirectAttributes) {
        t = service().findById(t.getId());
        RequestUtils.checkOperation(OP_DELETE, t);
        service().deleteById(t.getId());
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return Constants.REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/list";
    }

    /**
     * 获取实例名
     *
     * @return
     */
    protected String entityName() {
        return instance().entityName();
    }

    /**
     * 获取模块名
     *
     * @return
     */
    protected String moduleName() {
        return instance().moduleName();
    }

    /**
     * 封装方法
     *
     * @param model
     * @param operation
     * @param t
     * @param type
     */
    protected void putModel(Model model, String operation, T t, String type) {
        model.addAttribute(entityName(), t);
        model.addAttribute("id", t.getId());
        model.addAttribute("op", operation);
        model.addAttribute("type", type);
        model.addAttribute("moduleName", moduleName());
        model.addAttribute("entityName", entityName());
        modelAdvice(model);
    }

}
