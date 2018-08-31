package com.caolei.system.interceptor;


import com.caolei.system.pojo.OperationLog;
import com.caolei.system.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class VisitLogInterceptor implements HandlerInterceptor {

    private final OperationLogService operationLogService;

    @Autowired
    public VisitLogInterceptor(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 访问日志
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("RequestURL:\t" + request.getRequestURL().toString());
        operationLogService.save(new OperationLog(request));
        return true;
    }
}
