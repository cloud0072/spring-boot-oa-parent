package com.caolei.base.interceptor;


import com.caolei.common.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 将menuId存入session中 实现菜单高亮
 */
@Component
public class MenuInterceptor implements HandlerInterceptor {

    /**
     * 菜单 状态拦截器
     *
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