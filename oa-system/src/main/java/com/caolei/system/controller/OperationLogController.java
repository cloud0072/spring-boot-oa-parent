package com.caolei.system.controller;

import com.caolei.system.api.AbstractCrudController;
import com.caolei.system.api.BaseCrudService;
import com.caolei.system.pojo.OperationLog;
import com.caolei.system.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/system/operationLog")
@Controller
public class OperationLogController extends AbstractCrudController<OperationLog> {

    private final OperationLogService operationLogService;

    @Autowired
    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @Override
    public BaseCrudService<OperationLog> service() {
        return operationLogService;
    }

    @Override
    public void modelAdvice(Model model) {
        /*Map<String, Object> map = model.asMap();
        OperationLog operationLog = (OperationLog) map.get(entityName());
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
        }*/
    }
}
