package com.caolei.system.service.impl;

import com.caolei.system.pojo.OperationLog;
import com.caolei.system.repository.OperationLogRepository;
import com.caolei.system.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @author cloud0072
 * @date 2018/6/12 22:47
 */
@Service
public class OperationLogServiceImpl
        implements OperationLogService {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Override
    public JpaRepository<OperationLog, String> repository() {
        return operationLogRepository;
    }

    @Override
    public OperationLog updateById(String id, OperationLog input) {
        return null;
    }


}
