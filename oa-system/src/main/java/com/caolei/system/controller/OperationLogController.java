package com.caolei.system.controller;

import com.caolei.system.api.BaseCrudController;
import com.caolei.system.api.BaseCrudService;
import com.caolei.system.pojo.OperationLog;
import com.caolei.system.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/system/operationLog")
@Controller
public class OperationLogController implements BaseCrudController<OperationLog> {

    @Autowired
    private OperationLogService operationLogService;

    @Override
    public BaseCrudService<OperationLog> service() {
        return operationLogService;
    }

}
