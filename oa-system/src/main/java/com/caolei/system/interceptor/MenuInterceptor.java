package com.caolei.system.interceptor;

import com.caolei.system.api.BaseLogger;
import com.caolei.system.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 将menuId存入session中 实现菜单高亮
 */
@Component
public class MenuInterceptor implements HandlerInterceptor, BaseLogger {

    /**
     * 菜单 状态拦截器
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String currentMenuId = request.getParameter("menuId");
        if (!StringUtils.isEmpty(currentMenuId)) {
            request.getSession().setAttribute("menuId", currentMenuId);
        }
    }
}
