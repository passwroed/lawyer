package com.ruoyi.system.domain.lawyer;

import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotNull;

/**
 * @ClassName : CostLog
 * @Description : 积分日志
 * @Author : WANGKE
 * @Date: 2023-07-27 17:43
 */
public class CostLog extends BaseEntity {
    private Long id;
    @NotNull(message = "积分不能为空")
    private Double cost;
    private Double sum;
    private Long taskId;
    private Long lawyerId;
    @NotNull(message = "消费类型不能为空")
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
