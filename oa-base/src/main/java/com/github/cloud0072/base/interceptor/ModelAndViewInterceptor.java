package com.github.cloud0072.base.interceptor;


import com.github.cloud0072.base.config.Global;
import com.github.cloud0072.base.model.Category;
import com.github.cloud0072.base.service.CategoryService;
import com.github.cloud0072.base.service.UserService;
import com.github.cloud0072.base.util.UserUtils;
import com.github.cloud0072.common.util.SecurityUtils;
import com.github.cloud0072.common.util.StringUtils;
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
public class ModelAndViewInterceptor implements HandlerInterceptor {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private Global global;

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

            //分类管理中直接列出顶级分类名称
            List<Category> categoryList = categoryService.findCategoriesByParent_Id(null);
            modelAndView.addObject("categories", categoryList);

            modelAndView.addObject("Global", global);
        }
    }
}
