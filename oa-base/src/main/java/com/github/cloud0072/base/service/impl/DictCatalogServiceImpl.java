//package com.github.cloud0072.base.service.impl;
//
////import com.github.cloud0072.base.extend.ColumnConfig;
////import com.github.cloud0072.base.model.DictCatalog;
//import com.github.cloud0072.base.repository.ColumnConfigRepository;
//import com.github.cloud0072.base.repository.DictCatalogRepository;
////import com.github.cloud0072.base.service.DictCatalogService;
//import org.springframework.beans.factory.annotation.Autowired;
//import com.github.cloud0072.base.repository.BaseRepository;
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
//    public BaseRepository<DictCatalog, String> repository() {
//        return dictCatalogRepository;
//    }
//
//    @Override
//    public DictCatalog update(String id, DictCatalog input) {
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
