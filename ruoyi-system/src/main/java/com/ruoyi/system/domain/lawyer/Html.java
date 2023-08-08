package com.ruoyi.system.domain.lawyer;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName : Html
 * @Description : 富文本
 * @Author : WANGKE
 * @Date: 2023-07-16 23:15
 */
public class Html extends BaseEntity {

    private Long id;

    @NotBlank(message = "html")
    private String html;

    @NotNull(message = "type")
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
