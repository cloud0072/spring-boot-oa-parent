package com.caolei.base.controller;

import com.caolei.base.entity.OperationLog;
import com.caolei.base.service.OperationLogService;
import com.caolei.base.service.BaseCrudService;
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
