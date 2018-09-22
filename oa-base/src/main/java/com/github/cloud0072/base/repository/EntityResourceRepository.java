package com.github.cloud0072.base.repository;

import com.github.cloud0072.base.model.extend.EntityResource;
import org.springframework.stereotype.Repository;

/**
 * @author cloud0072
 */
@Repository
public interface EntityResourceRepository extends BaseRepository<EntityResource, String> {

    EntityResource findEntityResourceByCode(String code);

}
