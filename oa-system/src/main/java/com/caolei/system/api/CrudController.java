package com.caolei.system.api;

import com.caolei.system.constant.Constants;
import com.caolei.system.constant.Operation;
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
import java.io.Serializable;

import static com.caolei.system.constant.Constants.*;

public interface CrudController<T extends BaseEntity, ID extends Serializable>
        extends BaseController {

    CrudService<T, ID> getService();

    String getModulePath();

    String getEntityName();

    /**
     * 查询所有对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    default String list(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                        @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                        @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                        Model model, T t) {
        checkOperation(OP_LIST);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.fromString(direction), sortField));
        Page<T> list = getService().findAll(Example.of(t), pageable);
        model.addAttribute("page", list);
        model.addAttribute("search", t);
        model.addAttribute("op", OP_LIST);
        model.addAttribute("type", TY_ADMIN);
        methodAdvice(OP_LIST, null, model);
        return getModulePath() + "/" + getEntityName() + "/" + getEntityName() + "_list";
    }

    /**
     * 跳转 查看 更新 或 删除对象页
     */
    @RequestMapping(value = "/{operation}/{id}", method = RequestMethod.GET)
    default String showForm(HttpServletRequest request, HttpServletResponse response,
                            @PathVariable("id") ID id, @PathVariable("operation") String operation, Model model) {
        checkOperationAndId(operation, id);
        model.addAttribute(getEntityName(), getService().findById(id));
        model.addAttribute("op", operation);
        model.addAttribute("type", TY_ADMIN);
        methodAdvice(operation, id, model);
        String prefix = operation.equals(OP_FIND) ? "_view" : "_edit";
        return getModulePath() + "/" + getEntityName() + "/" + getEntityName() + prefix;
    }

    /**
     * 跳转创建对象页
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    default String showCreateForm(HttpServletRequest request, HttpServletResponse response,
                                  Model model) throws IllegalAccessException, InstantiationException {
        checkOperation(OP_CREATE);
        model.addAttribute(getEntityName(), ReflectUtils
                .getInterfaceGenericType(getClass(), 0, 0).newInstance());
        model.addAttribute("op", OP_CREATE);
        model.addAttribute("type", TY_ADMIN);
        methodAdvice(OP_CREATE, null, model);
        return getModulePath() + "/" + getEntityName() + "/" + getEntityName() + "_edit";
    }

    /**
     * 提交创建对象
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    default String create(HttpServletRequest request, HttpServletResponse response,
                          T t, RedirectAttributes redirectAttributes) {
        checkOperation(OP_CREATE);
        getService().save(t);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return Constants.REDIRECT_TO + getModulePath() + "/" + getEntityName() + "/list";
    }

    /**
     * 提交更新对象
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    default String update(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable("id") ID id, T t, RedirectAttributes redirectAttributes) {
        checkOperationAndId(OP_UPDATE, id);
        getService().update(id, t);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return Constants.REDIRECT_TO + getModulePath() + "/" + getEntityName() + "/find/" + id;
    }

    /**
     * 提交删除对象
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    default String delete(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable("id") ID id, RedirectAttributes redirectAttributes) {
        checkOperationAndId(OP_DELETE, id);
        getService().deleteById(id);
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return Constants.REDIRECT_TO + getModulePath() + "/" + getEntityName() + "/list";
    }

    /**
     * 判断当前用户是否有权限进行操作 默认为所有元素操作
     * 适用于 create 和 list
     */
    default void checkOperation(String operation) {
        String en = getEntityName();
        String op = Operation.of(operation).name();
        String permission = en + ":" + op + ":*";
        RequestUtils.checkAnyPermission(permission);
    }

    /**
     * 判断当前用户是否有权限进行操作 OP_LIST 需要 FIND_ALL 权限
     * FIXME: 另一种策略是 有多少权限就能查到多少元素，待完善
     */
    default void checkOperationAndId(String operation, ID resourceId) {
        String en = getEntityName();
        String op = Operation.of(operation).name();
        String id = resourceId == null ? "*" : (String) resourceId;
        String permission = en + ":" + op + ":" + id;
        RequestUtils.checkAnyPermission(permission);
    }

    default void methodAdvice(String operation, ID id, Model model) {
    }


}
