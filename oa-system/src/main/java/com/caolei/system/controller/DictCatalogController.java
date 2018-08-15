//package com.caolei.system.controller;
//
//import com.caolei.common.constant.ColumnType;
//import com.caolei.system.extend.ColumnConfig;
//import com.caolei.system.pojo.DictCatalog;
//import com.caolei.system.pojo.User;
//import com.caolei.system.service.DictCatalogService;
//
//import com.caolei.system.util.SecurityUtils;
//import com.caolei.common.util.StringUtils;
//import com.caolei.system.api.BaseCrudController;
//import com.caolei.system.api.BaseCrudService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.api.bind.annotation.RequestMapping;
//import org.springframework.api.bind.annotation.RequestMethod;
//import org.springframework.api.bind.annotation.ResponseBody;
//import org.springframework.api.servlet.mvc.support.RedirectAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Comparator;
//import java.util.Map;
//
//import static com.caolei.common.constant.Constants.*;
//import static java.util.stream.Collectors.toList;
//
//@RequestMapping("/system/dictCatalog")
//@Controller
//public class DictCatalogController implements BaseCrudController<DictCatalog> {
//
//    @Autowired
//    private DictCatalogService dictCatalogService;
//
//    @Override
//    public BaseCrudService<DictCatalog> service() {
//        return dictCatalogService;
//    }
//
//    @Override
//    public void modelAdvice(Model extend) {
//        Map<String, Object> map = extend.asMap();
//        DictCatalog dictCatalog = (DictCatalog) map.get(entityName());
//        String operation = (String) map.get("op");
//
//        User currentUser = SecurityUtils.getCurrentUser();
//        switch (operation) {
//            case OP_DELETE:
//            case OP_UPDATE:
//            case OP_CREATE:
//            case OP_FIND:
//                extend.addAttribute("columnTypes", ColumnType.values());
//                if (dictCatalog.getConfigs() != null) {
//                    extend.addAttribute("configs", dictCatalog.getConfigs().stream()
//                            .sorted(Comparator.comparing(ColumnConfig::getFieldOrder)).collect(toList()));
//                }
//                break;
//            case OP_LIST:
//                break;
//        }
//    }
//
//    /**
//     * 提交创建对象
//     *
//     * @param request
//     * @param response
//     * @param dict_id
//     * @param config
//     * @param redirectAttributes
//     * @return
//     */
//    @RequestMapping(value = "/saveColumn", method = RequestMethod.POST)
//    public String saveColumn(HttpServletRequest request, HttpServletResponse response, String dict_id,
//                             ColumnConfig config, RedirectAttributes redirectAttributes) {
//        SecurityUtils.checkOperation(instance(), OP_CREATE);
//        if (StringUtils.isEmpty(dict_id)) {
//            throw new UnsupportedOperationException("请先保存后才能添加列设置！");
//        }
//
//        dictCatalogService.addColumn(dict_id, config);
//
//        redirectAttributes.addFlashAttribute("message", "新增成功");
//        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/update/" + dict_id;
//    }
//
//    /**
//     * 查询column的配置
//     *
//     * @param request
//     * @param response
//     * @param id       ColumnConfig的Id
//     * @return
//     */
//    @RequestMapping(value = "/updateColumn", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<ColumnConfig> updateColumn(HttpServletRequest request, HttpServletResponse response,
//                                                     String id) {
//        return ResponseEntity.ok(dictCatalogService.findColumnConfigById(id));
//    }
//
//    /**
//     * 查询column的配置
//     *
//     * @param request
//     * @param response
//     * @param id       ColumnConfig的Id
//     * @return
//     */
//    @RequestMapping(value = "/deleteColumn", method = RequestMethod.POST)
//    public String deleteColumn(HttpServletRequest request, HttpServletResponse response,
//                               String id, String dict_id, RedirectAttributes redirectAttributes) {
//        dictCatalogService.deleteColumnConfigById(id);
//        redirectAttributes.addFlashAttribute("message", "删除成功");
//        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/update/" + dict_id;
//    }
//
//}
