package com.caolei.base.model.dto;

import com.caolei.base.model.Category;
import lombok.Data;

import java.util.List;

/**
 * 分类 数据传输对象
 */
@Data
public class CategoryDTO {

    private String id;
    private String pid;
    private String text;
    private Integer categoryOrder;
    private String icon;
    private String url;
    private boolean lazyLoad;
    private List<CategoryDTO> nodes;

    public CategoryDTO(Category category) {
        if (category != null) {
            this.id = category.getId();
            this.text = category.getName();
            this.categoryOrder = category.getCategoryOrder();
            this.icon = category.getIcon();
            this.url = category.getUrl();
            this.lazyLoad = category.getChildren() != null
                    && category.getChildren().size() > 0;
            if (category.getParent() != null) {
                this.pid = category.getParent().getId();
            }
        }
    }

}
