package com.caolei.base.interceptor;


import com.caolei.base.entity.Category;
import com.caolei.base.repository.CategoryRepository;
import com.caolei.base.service.CategoryService;
import com.caolei.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 将menuId存入session中 实现菜单高亮
 */
@Component
public class MenuInterceptor implements HandlerInterceptor {

    @Autowired
    private CategoryService categoryService;

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
        if (modelAndView != null) {
            String currentMenuId = request.getParameter("menuId");
            if (!StringUtils.isEmpty(currentMenuId)) {
                //切换页面仍然不改变
                request.getSession().setAttribute("menuId", currentMenuId);
            }

            List<Category> categoryList = categoryService.findCategoriesByParent_Id(null);
            modelAndView.addObject("categories", categoryList);
        }
    }
}
