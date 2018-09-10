package com.caolei.base.controller;

import com.caolei.common.api.controller.BaseCrudController;
import com.caolei.common.api.service.BaseCrudService;
import com.caolei.base.pojo.OperationLog;
import com.caolei.base.service.OperationLogService;
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