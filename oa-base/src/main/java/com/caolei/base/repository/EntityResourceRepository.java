package com.caolei.base.repository;

import com.caolei.base.extend.EntityResource;
import org.springframework.stereotype.Repository;

/**
 * @author cloud0072
 */
@Repository
public interface EntityResourceRepository extends BaseRepository<EntityResource, String> {

    /**
     * 查询
     *
     * @param code
     * @return
     */
    EntityResource findEntityResourceByCode(String code);

}
