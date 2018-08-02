package com.caolei.system.service.impl;

import com.caolei.system.pojo.DictCatalog;
import com.caolei.system.repository.DictCatalogRepository;
import com.caolei.system.service.DictCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @author cloud0072
 * @date 2018/6/12 22:47
 */
@Service
public class DictCatalogServiceImpl
        implements DictCatalogService {

    @Autowired
    private DictCatalogRepository dictCatalogRepository;

    @Override
    public JpaRepository<DictCatalog, String> repository() {
        return dictCatalogRepository;
    }

    @Override
    public DictCatalog updateById(String id, DictCatalog input) {
        DictCatalog dictCatalog = findById(id);

        dictCatalog.setName(input.getName());
        dictCatalog.setDescription(input.getDescription());
        if (input.getColumnConfig() != null && !input.getColumnConfig().isEmpty()) {
            dictCatalog.setColumnConfig(input.getColumnConfig());
        }

        return save(dictCatalog);
    }

    @Override
    public DictCatalog findById(String id) {
        DictCatalog dictCatalog = repository().findById(id).orElseThrow(UnsupportedOperationException::new);
        if (dictCatalog.getColumnConfig() != null) {
            dictCatalog.getColumnConfig().size();
        }
        return dictCatalog;
    }
}
