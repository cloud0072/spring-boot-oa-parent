package com.github.cloud0072.base.service.impl;

import com.github.cloud0072.base.model.OperationLog;
import com.github.cloud0072.base.repository.OperationLogRepository;
import com.github.cloud0072.base.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.cloud0072.base.repository.BaseRepository;
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
    public BaseRepository<OperationLog, String> repository() {
        return operationLogRepository;
    }

    @Override
    public OperationLog update(OperationLog input,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        return null;
    }
}
