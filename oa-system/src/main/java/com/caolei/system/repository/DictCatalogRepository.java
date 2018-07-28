package com.caolei.system.repository;

import com.caolei.system.pojo.DictCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictCatalogRepository extends JpaRepository<DictCatalog, String> {
}
