package com.ruoyi.system.domain.lawyer;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @ClassName : OrderLog
 * @Description : 订单日志
 * @Author : WANGKE
 * @Date: 2023-08-31 16:13
 */
public class OrderLog extends BaseEntity {
    private Long id;
    private Long orderId;
    private Integer status;
    private String image;
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
