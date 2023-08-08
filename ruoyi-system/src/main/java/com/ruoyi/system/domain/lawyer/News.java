package com.ruoyi.system.domain.lawyer;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName : News
 * @Description : 新闻
 * @Author : WANGKE
 * @Date: 2023-07-11 17:16
 */
public class News extends BaseEntity {
    /** 参数主键 */
    @Excel(name = "id", cellType = Excel.ColumnType.NUMERIC)
    private Long id;
    @Excel(name = "缩略图")
    @NotBlank(message = "缩略图不能为空")
    private String smallIcon;
    @Excel(name = "新闻名称")
    @NotBlank(message = "新闻名称不能为空")
    private String name;
    @Excel(name = "新闻类型")
    @NotNull(message = "新闻类型不能为空")
    private Integer type;
    @Excel(name = "作者")
    @NotBlank(message = "作者不能为空")
    private String author;
    @Excel(name = "简介")
    @NotBlank(message = "简介不能为空")
    private String synopsis;
    @Excel(name = "详情")
    @NotBlank(message = "详情不能为空")
    private String info;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
