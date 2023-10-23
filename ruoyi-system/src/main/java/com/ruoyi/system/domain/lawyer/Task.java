package com.ruoyi.system.domain.lawyer;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.jackson.DesensitizationData;
import com.ruoyi.common.jackson.DesensitizationEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName : Task
 * @Description : 任务
 * @Author : WANGKE
 * @Date: 2023-07-27 17:57
 */
public class Task extends BaseEntity {
    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
    private Long id;
    @Excel(name = "任务编号")
    private String no;
    @NotBlank(message = "任务名称不能为空")
    @Excel(name = "任务名称")
    private String name;
    @Excel(name = "微信号")
    private String wxNum;
    @NotNull(message = "任务类型不能为空")
    private Integer type;
    @Excel(name = "状态", readConverterExp = "-3=取消订单,-2=拒绝退款,-1=失效,0=待支付,1=支付完成,2=待交付,3=跟进中,4=已完成,5=申请退款,6=退款中,7=退款完成")
    private Integer status;
    private Integer payStatus;
    @Excel(name = "订单编号")
    private String orderNo;
    @Excel(name = "客户id", cellType = Excel.ColumnType.NUMERIC)
    private Long cid;
    @NotBlank(message = "客户不能为空")
    @Excel(name = "客户名称")
    private String cName;
    @Excel(name = "客服id")
    private Long pid;
    @Excel(name = "客服名称")
    private String pName;
    @NotBlank(message = "客户手机号不能为空")
    @Excel(name = "客户手机号")
//    @DesensitizationData(function = DesensitizationEnum.MOBILE_PHONE)
    private String phone;
    @NotBlank(message = "需求不能为空")
    @Excel(name = "需求")
    private String need;
    @NotNull(message = "省份不能为空")
    private Integer povinceId;
    @Excel(name = "区划")
    private String povince;
    @Excel(name = "产品金额", cellType = Excel.ColumnType.NUMERIC)
    private Double money;
    @Excel(name = "案件金额", cellType = Excel.ColumnType.NUMERIC)
    private Double profit;
    @Excel(name = "积分", cellType = Excel.ColumnType.NUMERIC)
    private Double cost;
    @Excel(name = "内容")
    private String content;
    @Excel(name = "大厅任务", readConverterExp = "0=非大厅任务,1=大厅任务")
    private Integer isHall;
    @Excel(name = "委托任务", readConverterExp = "0=非委托任务,1=委托任务")
    private Integer entrust;
    private Long lawyerId;
    @Excel(name = "办件律师")
    private String lawyerName;
    private Integer lawyerType;
    private Integer willing;
    private Long FastLawyerId;
    @Excel(name = "中台律师")
    private String FastLawyerName;
    private Integer FastLawyerType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public Integer getPovinceId() {
        return povinceId;
    }

    public void setPovinceId(Integer povinceId) {
        this.povinceId = povinceId;
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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsHall() {
        return isHall;
    }

    public void setIsHall(Integer isHall) {
        this.isHall = isHall;
    }

    public Integer getEntrust() {
        return entrust;
    }

    public void setEntrust(Integer entrust) {
        this.entrust = entrust;
    }

    public Long getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public void setLawyerName(String lawyerName) {
        this.lawyerName = lawyerName;
    }

    public Integer getLawyerType() {
        return lawyerType;
    }

    public void setLawyerType(Integer lawyerType) {
        this.lawyerType = lawyerType;
    }

    public Integer getWilling() {
        return willing;
    }

    public void setWilling(Integer willing) {
        this.willing = willing;
    }

    public Long getFastLawyerId() {
        return FastLawyerId;
    }

    public void setFastLawyerId(Long fastLawyerId) {
        FastLawyerId = fastLawyerId;
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

    public String getWxNum() {
        return wxNum;
    }

    public void setWxNum(String wxNum) {
        this.wxNum = wxNum;
    }
}
