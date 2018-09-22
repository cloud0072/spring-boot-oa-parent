package com.github.cloud0072.base.controller;

import com.github.cloud0072.base.model.Category;
import com.github.cloud0072.base.model.dto.CategoryDTO;
import com.github.cloud0072.base.service.BaseCrudService;
import com.github.cloud0072.base.service.CategoryService;
import com.github.cloud0072.common.constant.Operation;
import com.github.cloud0072.common.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.Path;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RequestMapping("/base/category")
@Controller
public class CategoryController
        extends BaseCrudController<Category> {

    @Autowired
    private CategoryService categoryService;

    @Override
    public BaseCrudService<Category> service() {
        return categoryService;
    }

    @ApiOperation("分页查询根分组")
    @GetMapping("/list")
    @Override
    protected String showListPage(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Category category,
                                  @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                  @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                  @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                  Model model) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.fromString(direction), sortField));
        Page<Category> list = service().findAll(
                (Specification<Category>) (root, criteriaQuery, criteriaBuilder) -> {
                    Path<Category> parent = root.get("parent");
                    return criteriaBuilder.isNull(parent);
                }, pageable);
        model.addAttribute("page", list);
        model.addAttribute("search", category);
        putModel(model, Operation.GET, category);
        return modulePath + entityPath + entityPath + "_list";
    }

    @Override
    protected ResponseEntity create(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Category category) {
        String pid = request.getParameter("pid");
        if (!StringUtils.isEmpty(pid)) {
            Category parent = categoryService.findById(pid);
            category.setParent(parent);
        }
        category = service().save(category, request, response);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "新增成功");
        map.put("url", modulePath + entityPath + "/view/" + category.getId());
        map.put("newnode", new CategoryDTO(category));
        return ResponseEntity.ok(map);
    }

    @Override
    protected ResponseEntity delete(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable("id") String id) {
        String ids = request.getParameter("ids");
        Set<String> idSet = new HashSet<>();
        idSet.add(id);
        if (!StringUtils.isEmpty(ids)) {
            idSet.addAll(Arrays.asList(ids.trim().split(",")));
        }
        categoryService.deleteAllByIds(idSet);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "删除成功");
        map.put("url", modulePath + entityPath + "/list");
        return ResponseEntity.ok(map);
    }

    @Override
    protected ResponseEntity update(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String id,
                                    Category category) {
        String pid = request.getParameter("pid");
        if (!StringUtils.isEmpty(pid)) {
            Category parent = categoryService.findById(pid);
            category.setParent(parent);
        }
        category = service().update(category, request, response);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "修改成功");
        map.put("url", modulePath + entityPath + "/view/" + category.getId());
        map.put("newnode", new CategoryDTO(category));
        return ResponseEntity.ok(map);
    }

    @GetMapping("/treeview/{id}")
    public String treeview(HttpServletRequest request,
                           HttpServletResponse response,
                           @PathVariable("id") String id,
                           Model model) {
        Category category = categoryService.findById(id);
        CategoryDTO categoryDTO = categoryService.findCategoryDTOByIdWith2Level(id);
        model.addAttribute("root", categoryDTO);

        putModel(model, Operation.GET, category);

        return modulePath + entityPath + entityPath + "_treeview";
    }


    @GetMapping("/findByParentId/{id}")
    public ResponseEntity findByParentId(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable("id") String id) {
        List<CategoryDTO> CategoryDTOs = categoryService.findCategoryDTOsByParentId(id);
        return ResponseEntity.ok(CategoryDTOs);
    }

}
