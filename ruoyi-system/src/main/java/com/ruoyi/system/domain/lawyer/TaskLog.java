package com.ruoyi.system.domain.lawyer;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @ClassName : TaskLog
 * @Description : 任务日志
 * @Author : WANGKE
 * @Date: 2023-08-16 11:11
 */
public class TaskLog extends BaseEntity {
    private Long id;
    private Long taskId;
    private Integer status;
    private String content;
    private Integer willing;
    private Integer lawyerType;
    private String lawyerName;
    private Long lawyerId;
    private Long FastLawyerId;
    private String FastLawyerName;
    private Integer FastLawyerType;
    private Double profit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getWilling() {
        return willing;
    }

    public void setWilling(Integer willing) {
        this.willing = willing;
    }

    public Integer getLawyerType() {
        return lawyerType;
    }

    public void setLawyerType(Integer lawyerType) {
        this.lawyerType = lawyerType;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public void setLawyerName(String lawyerName) {
        this.lawyerName = lawyerName;
    }

    public Long getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getFastLawyerName() {
        return FastLawyerName;
    }

    public void setFastLawyerName(String fastLawyerName) {
        FastLawyerName = fastLawyerName;
    }

    public Integer getFastLawyerType() {
        return FastLawyerType;
    }

    public void setFastLawyerType(Integer fastLawyerType) {
        FastLawyerType = fastLawyerType;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Long getFastLawyerId() {
        return FastLawyerId;
    }

    public void setFastLawyerId(Long fastLawyerId) {
        FastLawyerId = fastLawyerId;
    }
}
