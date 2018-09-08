package com.caolei.common.api.controller;

import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.annotation.ModuleInfo;
import com.caolei.common.api.entity.BaseEntity;
import com.caolei.common.api.service.BaseCrudService;
import com.caolei.common.util.ReflectUtils;
import com.caolei.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.core.annotation.AnnotationUtils;
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
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    protected String list(HttpServletRequest request, HttpServletResponse response, T t,
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


    @ApiOperation("跳转创建对象页")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    protected String showCreateForm(HttpServletRequest request, HttpServletResponse response, Model model) {
//        SecurityUtils.checkOperation(instance(), OP_CREATE);
        putModel(model, OP_CREATE, instance(), TY_ADMIN);
        return modulePath() + entityPath() + entityPath() + "_edit";
    }


    @ApiOperation("跳转 查看 更新 或 删除对象页")
    @RequestMapping(value = "/{operation}/{id}", method = RequestMethod.GET)
    protected String showForm(HttpServletRequest request, HttpServletResponse response, Model model,
                              @PathVariable("id") String id, @PathVariable("operation") String operation) {
        T t = service().findById(id);
//        SecurityUtils.checkOperation(t, operation);
        putModel(model, operation, t, TY_ADMIN);
        String prefix = operation.equals(OP_FIND) ? "_view" : "_edit";
        return modulePath() + entityPath() + entityPath() + prefix;
    }


    @ApiOperation("提交创建对象")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    protected String create(HttpServletRequest request, HttpServletResponse response, T t,
                            RedirectAttributes redirectAttributes) {
//        SecurityUtils.checkOperation(t, OP_CREATE);
        service().save(t);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return REDIRECT_TO + "/" + modulePath() + "/" + entityPath() + "/list";
    }


    @ApiOperation("提交更新对象")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    protected String update(HttpServletRequest request, HttpServletResponse response, T t,
                            RedirectAttributes redirectAttributes) {
//        SecurityUtils.checkOperation(t, OP_UPDATE);
        service().updateById(t.getId(), t);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return REDIRECT_TO + "/" + modulePath() + "/" + entityPath() + "/find/" + t.getId();
    }

    @ApiOperation("提交删除对象")
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    protected String delete(HttpServletRequest request, HttpServletResponse response, T t,
                            RedirectAttributes redirectAttributes) {
        t = service().findById(t.getId());
//        SecurityUtils.checkOperation(t, OP_DELETE);
        service().deleteById(t.getId());
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_TO + "/" + modulePath() + "/" + entityPath() + "/list";
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
