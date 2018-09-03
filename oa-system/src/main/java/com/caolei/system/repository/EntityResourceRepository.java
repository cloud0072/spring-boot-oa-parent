package com.caolei.system.repository;

import com.caolei.system.api.BaseRepository;
import com.caolei.system.extend.EntityResource;
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
