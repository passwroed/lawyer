package com.ruoyi.system.domain.lawyer;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @ClassName : Appeal
 * @Description : 述求
 * @Author : WANGKE
 * @Date: 2023-09-03 12:32
 */
public class Appeal extends BaseEntity {
    private Long id;
    private Integer type;
    private Integer status;
    private Long cid;
    private String cName;
    private String need;
    private Long povinceId;
    private String phone;
    private String povince;
    private Double money;

    public Long getId() {
        return id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public Long getPovinceId() {
        return povinceId;
    }

    public void setPovinceId(Long povinceId) {
        this.povinceId = povinceId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPovince() {
        return povince;
    }

    public void setPovince(String povince) {
        this.povince = povince;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
