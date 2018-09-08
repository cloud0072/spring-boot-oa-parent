//package com.caolei.system.service.impl;
//
////import com.caolei.system.extend.ColumnConfig;
////import com.caolei.system.pojo.DictCatalog;
//import com.caolei.system.repository.ColumnConfigRepository;
//import com.caolei.system.repository.DictCatalogRepository;
////import com.caolei.system.service.DictCatalogService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * @author cloud0072
// * @date 2018/6/12 22:47
// */
//@Service
//public class DictCatalogServiceImpl
//        implements DictCatalogService {
//
//    @Autowired
//    private DictCatalogRepository dictCatalogRepository;
//    @Autowired
//    private ColumnConfigRepository columnConfigRepository;
//
//    @Override
//    public JpaRepository<DictCatalog, String> repository() {
//        return dictCatalogRepository;
//    }
//
//    @Override
//    public DictCatalog updateById(String id, DictCatalog input) {
//        DictCatalog dictCatalog = findById(id);
//
//        dictCatalog.setName(input.getName());
//        dictCatalog.setDescription(input.getDescription());
//
//        return save(dictCatalog);
//    }
//
//    @Override
//    public DictCatalog findById(String id) {
//        DictCatalog dictCatalog = repository().findById(id).orElseThrow(UnsupportedOperationException::new);
//        dictCatalog.getConfigs().size();
//        return dictCatalog;
//    }
//
//    @Transactional
//    @Override
//    public DictCatalog addColumn(String id, ColumnConfig config) {
//        DictCatalog catalog = findById(id);
//        //多端保存
//        config.setDictCatalog(catalog);
//        columnConfigRepository.save(config);
//        return catalog;
//    }
//
//    @Override
//    public ColumnConfig findColumnConfigById(String id) {
//        return columnConfigRepository.findById(id).orElseThrow(UnsupportedOperationException::new);
//    }
//
//    @Override
//    public void deleteColumnConfigById(String id) {
//        ColumnConfig config = columnConfigRepository.findById(id).orElseThrow(UnsupportedOperationException::new);
//        columnConfigRepository.delete(config);
//    }
//}
