package com.caolei.system.interceptor;

import com.caolei.system.web.BaseLogger;
import com.caolei.system.pojo.OperationLog;
import com.caolei.system.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class VisitLogInterceptor implements HandlerInterceptor, BaseLogger {

    private final OperationLogService operationLogService;

    @Autowired
    public VisitLogInterceptor(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 访问日志
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        info("RequestURL:\t" + request.getRequestURL().toString());
        operationLogService.save(new OperationLog(request));
        return true;
    }
}
