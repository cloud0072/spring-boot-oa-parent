package com.caolei.system.controller;

import com.caolei.system.constant.ColumnType;
import com.caolei.system.constant.Constants;
import com.caolei.system.model.ColumnConfigModel;
import com.caolei.system.model.ColumnEntity;
import com.caolei.system.pojo.DictCatalog;
import com.caolei.system.pojo.User;
import com.caolei.system.service.DictCatalogService;
import com.caolei.system.util.RequestUtils;
import com.caolei.system.util.SecurityUtils;
import com.caolei.system.util.StringUtils;
import com.caolei.system.web.BaseCrudController;
import com.caolei.system.web.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.caolei.system.constant.Constants.*;

@RequestMapping("/system/dictCatalog")
@Controller
public class DictCatalogController implements BaseCrudController<DictCatalog> {

    private final DictCatalogService dictCatalogService;

    @Autowired
    public DictCatalogController(DictCatalogService dictCatalogService) {
        this.dictCatalogService = dictCatalogService;
    }

    @Override
    public BaseCrudService<DictCatalog> service() {
        return dictCatalogService;
    }

    @Override
    public void modelAdvice(Model model) {
        Map<String, Object> map = model.asMap();
        DictCatalog dictCatalog = (DictCatalog) map.get(entityName());
        String operation = (String) map.get("op");

        User currentUser = RequestUtils.getCurrentUser();
        switch (operation) {
            case OP_DELETE:
            case OP_UPDATE:
            case OP_CREATE:
            case OP_FIND:
                model.addAttribute("columnTypes", Arrays.asList(ColumnType.values()));
                break;
            case OP_LIST:
                break;
        }
    }

    /**
     * 提交创建对象
     */
    @RequestMapping(value = "/addColumn", method = RequestMethod.POST)
    public String addColumn(HttpServletRequest request, HttpServletResponse response,
                            DictCatalog dictCatalog, ColumnConfigModel model, RedirectAttributes redirectAttributes) {
        SecurityUtils.checkOperation(dictCatalog, OP_CREATE);

        if (StringUtils.isEmpty(dictCatalog.getId())) {
            //create
            dictCatalog.setColumnConfig(Arrays.asList(new ColumnEntity()));
            dictCatalog = service().save(dictCatalog);
        } else {
            dictCatalog.getColumnConfig().addAll(model.getColumnEntity().stream()
                    .filter(e -> !e.equals(new ColumnEntity())).collect(Collectors.toList()));
            dictCatalog.getColumnConfig().add(new ColumnEntity());
            service().updateById(dictCatalog.getId(), dictCatalog);
        }

        redirectAttributes.addFlashAttribute("message", "新增成功");
        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() +
                "/update/" + dictCatalog.getId();
    }


}
