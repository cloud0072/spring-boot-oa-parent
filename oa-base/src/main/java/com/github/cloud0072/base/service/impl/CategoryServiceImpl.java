package com.github.cloud0072.base.service.impl;

import com.github.cloud0072.base.model.Category;
import com.github.cloud0072.base.model.dto.CategoryDTO;
import com.github.cloud0072.base.repository.BaseRepository;
import com.github.cloud0072.base.repository.CategoryRepository;
import com.github.cloud0072.base.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caolei
 * @ClassName: CategoryService
 * @Description: 分类服务类
 * @date 2018/9/12 19:52
 */
@Service
public class CategoryServiceImpl
        implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public BaseRepository<Category, String> repository() {
        return categoryRepository;
    }

    @Override
    public Category update(Category input,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        Category category = findById(input.getId());
        if (input.getParent() != null) {
            category.setParent(input.getParent());
        }
        category.setName(input.getName());
        category.setCategoryOrder(input.getCategoryOrder());
        category.setIcon(input.getIcon());
        category.setUrl(input.getUrl());
        return repository().save(category);
    }

    @Override
    public List<Category> findCategoriesByParent_Id(String parentId) {
        return categoryRepository
                .findCategoriesByParent_IdIsOrderByCategoryOrderAsc(parentId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CategoryDTO findCategoryDTOById(String id) {
        return new CategoryDTO(findById(id));
    }

    @Override
    public CategoryDTO findCategoryDTOByIdWith2Level(String id) {
        Category category = findById(id);
        CategoryDTO categoryDTO = new CategoryDTO(category);
        if (category.getChildren() != null) {
            categoryDTO.setNodes(category.getChildren().stream()
                    .map(CategoryDTO::new)
                    .collect(Collectors.toList()));
        }
        return categoryDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CategoryDTO> findCategoryDTOsByParentId(String parentId) {
        return findCategoriesByParent_Id(parentId).stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }
}
