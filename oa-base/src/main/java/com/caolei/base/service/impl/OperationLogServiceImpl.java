package com.caolei.base.service.impl;

import com.caolei.base.pojo.OperationLog;
import com.caolei.base.repository.OperationLogRepository;
import com.caolei.base.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public OperationLog update(OperationLog input,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        return null;
    }
}
