package com.caolei.base.service.impl;

import com.caolei.base.pojo.Category;
import com.caolei.base.repository.CategoryRepository;
import com.caolei.base.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: CategoryService
 * @Description: 分类服务类
 * @author caolei
 * @date 2018/9/12 19:52
 */
@Service
public class CategoryServiceImpl
        implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public JpaRepository<Category, String> repository() {
        return categoryRepository;
    }

    @Override
    public Category update(Category input,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        return null;
    }


}
