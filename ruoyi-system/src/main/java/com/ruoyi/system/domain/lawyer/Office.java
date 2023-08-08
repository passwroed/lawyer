package com.ruoyi.system.domain.lawyer;

import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName : Office
 * @Description : 律所
 * @Author : WANGKE
 * @Date: 2023-07-20 15:07
 */
public class Office extends BaseEntity {

    private Long id;
    @NotBlank(message = "律所名称不能为空")
    private String name;
    @NotBlank(message = "律所地址不能为空")
    private String area;
    @NotNull(message = "地址code不能为空")
    private Integer areaCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }
}
