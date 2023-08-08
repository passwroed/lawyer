package com.ruoyi.system.domain.lawyer;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @ClassName : Goods
 * @Description : 商品
 * @Author : WANGKE
 * @Date: 2023-07-27 17:47
 */
public class Goods extends BaseEntity {
    private Long id;
    private String name;
    private Integer type;
    private String sImage;
    private Double money;
    private String info;
    private String notice;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
