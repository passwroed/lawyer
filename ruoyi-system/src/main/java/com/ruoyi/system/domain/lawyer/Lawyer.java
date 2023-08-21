package com.ruoyi.system.domain.lawyer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName : Lawyer
 * @Description : 律师
 * @Author : WANGKE
 * @Date: 2023-07-21 17:52
 */
public class Lawyer extends BaseEntity {
    private Long id;
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotBlank(message = "手机号不能为空")
    private String phone;
    private String area;
    @NotNull(message = "区划不能为空")
    private Integer areaCode;
    private String officeName;
    @NotBlank(message = "擅长不能为空")
    private String merit;
    private Long officeId;
    private Integer status;
    private String feedBack;
    private Integer sex;
    private String licenseNum;
    private Integer type;
    private Long userId;
    private Double sunCost;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lincensStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lincensEndTime;
    private Integer taskMax;
    @NotBlank(message = "简介不能为空")
    private String info;
    private String headImg;
    private String lincensImg;
    private String idUpImg;
    private String idDownImg;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }

    public Date getLincensStartTime() {
        return lincensStartTime;
    }

    public void setLincensStartTime(Date lincensStartTime) {
        this.lincensStartTime = lincensStartTime;
    }

    public Date getLincensEndTime() {
        return lincensEndTime;
    }

    public void setLincensEndTime(Date lincensEndTime) {
        this.lincensEndTime = lincensEndTime;
    }

    public Integer getTaskMax() {
        return taskMax;
    }

    public void setTaskMax(Integer taskMax) {
        this.taskMax = taskMax;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getLincensImg() {
        return lincensImg;
    }

    public void setLincensImg(String lincensImg) {
        this.lincensImg = lincensImg;
    }

    public String getIdUpImg() {
        return idUpImg;
    }

    public void setIdUpImg(String idUpImg) {
        this.idUpImg = idUpImg;
    }

    public String getIdDownImg() {
        return idDownImg;
    }

    public void setIdDownImg(String idDownImg) {
        this.idDownImg = idDownImg;
    }

    public String getMerit() {
        return merit;
    }

    public void setMerit(String merit) {
        this.merit = merit;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getSunCost() {
        return sunCost;
    }

    public void setSunCost(Double sunCost) {
        this.sunCost = sunCost;
    }

    public Lawyer(String phone) {
        this.phone = phone;
    }
    public Lawyer() {
    }
}
