package com.caolei.base.controller;

import com.caolei.base.pojo.Category;
import com.caolei.base.pojo.User;
import com.caolei.base.service.BaseCrudService;
import com.caolei.base.service.CategoryService;
import com.caolei.base.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/base/category")
@Controller
public class CategoryController
        extends BaseCrudController<Category> {

    @Autowired
    private CategoryService categoryService;

    @Override
    public BaseCrudService<Category> service() {
        return categoryService;
    }

}
