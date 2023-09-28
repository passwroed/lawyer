package com.ruoyi.system.domain.lawyer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName : Order
 * @Description : 订单
 * @Author : WANGKE
 * @Date: 2023-07-27 17:54
 */
public class Order extends BaseEntity {
    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
    private Long id;
    @Excel(name = "订单号")
    private String no;
    @NotNull(message = "商品不能为空")
    private Long goodsId;
    @Excel(name = "商品名称")
    private String goodsName;
    private String sImage;
    @Excel(name = "客户id", cellType = Excel.ColumnType.NUMERIC)
    private Long clientId;
    @Excel(name = "客户名称")
    private String clientName;
    @Excel(name = "客户手机号")
    private String clientPhone;
    private Long pid;
    @Excel(name = "客服姓名")
    private String pName;
    @Excel(name = "价格", cellType = Excel.ColumnType.NUMERIC)
    private Double money;
    @Excel(name = "类型", readConverterExp = "0=商品订单,1=充值订单")
    private Integer type;
    @Excel(name = "状态", readConverterExp = "-3=取消订单,-2=拒绝退款,-1=失效,0=待支付,1=支付完成,2=待交付,3=跟进中,4=已完成,5=申请退款,6=退款中,7=退款完成")
    private Integer status;
    private String taskNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;
    private Task task;
    private OrderLog orderLog;

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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    public OrderLog getOrderLog() {
        return orderLog;
    }

    public void setOrderLog(OrderLog orderLog) {
        this.orderLog = orderLog;
    }
}
