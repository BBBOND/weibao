package com.kim.weibao.model.business;

import java.util.Date;

/**
 * 维保方案表
 */

public class RepairPlan {

    private String id;//标识
    private String appId;
    private String appCode;//申请单号
    private String planDescription;//描述
    private long planMoney;//预算
    private Date planTime;//预计完成时间
    private Date submitTime;//提交时间
    private String planType;//方案类型
    private String exceptionMess;//异常
    private String planUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public long getPlanMoney() {
        return planMoney;
    }

    public void setPlanMoney(long planMoney) {
        this.planMoney = planMoney;
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getExceptionMess() {
        return exceptionMess;
    }

    public void setExceptionMess(String exceptionMess) {
        this.exceptionMess = exceptionMess;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppCode() {
        return appCode;
    }

    public String getPlanUserId() {
        return planUserId;
    }

    public void setPlanUserId(String planUserId) {
        this.planUserId = planUserId;
    }
}
