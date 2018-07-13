package com.caolei.system.repository;

import com.caolei.system.po.EntityResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cloud0072
 */
@Repository
public interface EntityResourceRepository extends JpaRepository<EntityResource,String> {

    /**
     * 查询
     * @param code
     * @return
     */
    EntityResource findEntityResourceByCode(String code);
    
}
