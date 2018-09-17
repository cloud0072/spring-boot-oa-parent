package com.caolei.base.service;

import com.caolei.base.model.Category;
import com.caolei.base.model.dto.CategoryDTO;

import java.util.List;

/**
 * @author caolei
 * @ClassName: CategoryService
 * @Description: TODO
 * @date 2018/9/12 19:52
 */
public interface CategoryService
        extends BaseCrudService<Category> {

    /**
     * 查询子类型
     *
     * @param parentId
     * @return
     */
    List<Category> findCategoriesByParent_Id(String parentId);

    /**
     * 查询分类
     *
     * @param id
     * @return dto对象
     */
    CategoryDTO findCategoryDTOById(String id);

    CategoryDTO findCategoryDTOByIdWith2Level(String id);

    /**
     * 根据父id查询分类
     *
     * @param id
     * @return dto对象
     */
    List<CategoryDTO> findCategoryDTOsByParentId(String id);


}
