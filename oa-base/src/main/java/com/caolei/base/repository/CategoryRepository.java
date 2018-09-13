package com.caolei.base.repository;

import com.caolei.base.pojo.Category;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: CategoryTypeRepository
 * @Description: TODO
 * @author caolei
 * @date 2018/9/12 19:48
 */
@Repository
public interface CategoryRepository extends BaseRepository<Category, String> {
}
