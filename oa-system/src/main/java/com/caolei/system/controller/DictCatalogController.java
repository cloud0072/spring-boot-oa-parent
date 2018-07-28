package com.caolei.system.controller;

import com.caolei.system.api.AbstractCrudController;
import com.caolei.system.api.BaseCrudService;
import com.caolei.system.pojo.DictCatalog;
import com.caolei.system.pojo.User;
import com.caolei.system.service.DictCatalogService;
import com.caolei.system.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static com.caolei.system.constant.Constants.*;

@RequestMapping("/system/dictCatalog")
@Controller
public class DictCatalogController extends AbstractCrudController<DictCatalog> {

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
                break;
            case OP_LIST:
                break;
        }
    }
}
