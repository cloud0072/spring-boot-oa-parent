package com.github.cloud0072.base.controller;

import com.github.cloud0072.base.model.OperationLog;
import com.github.cloud0072.base.service.OperationLogService;
import com.github.cloud0072.base.service.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/base/operationLog")
@Controller
public class OperationLogController extends BaseCrudController<OperationLog> {

    @Autowired
    private OperationLogService operationLogService;

    @Override
    public BaseCrudService<OperationLog> service() {
        return operationLogService;
    }

}
