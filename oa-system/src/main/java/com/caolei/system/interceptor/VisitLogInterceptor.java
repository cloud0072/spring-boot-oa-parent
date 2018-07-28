package com.caolei.system.interceptor;

import com.caolei.system.api.BaseLogger;
import com.caolei.system.pojo.OperationLog;
import com.caolei.system.service.OperationLogService;
import com.caolei.system.utils.RequestUtils;
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
     * basePathURL
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        info("RequestURL:\t" + request.getRequestURL().toString());

        OperationLog log = new OperationLog(
                RequestUtils.getCurrentUser(),
                request.getMethod(),
                request.getRequestURL().toString(),
                RequestUtils.IPAddress(request),
                RequestUtils.getSessionId());

        operationLogService.save(log);
    }


}
