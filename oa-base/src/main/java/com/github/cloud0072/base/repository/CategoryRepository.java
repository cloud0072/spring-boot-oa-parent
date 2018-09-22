package com.github.cloud0072.base.repository;

import com.github.cloud0072.base.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: CategoryTypeRepository
 * @Description: TODO
 * @author caolei
 * @date 2018/9/12 19:48
 */
@Repository
public interface CategoryRepository extends BaseRepository<Category, String> {

    List<Category> findCategoriesByParent_IdIsOrderByCategoryOrderAsc(String parentId);
}
