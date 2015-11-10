package com.kim.weibao.model.business;

import java.util.Date;

/**
 * Created by Courage on 2015/10/14.
 */


/**
 * 工程验收表
 */

public class ProjectCheck {


    private String id;//标识
    private String appCode;//申请单号
    private Date checkTime;//验收时间自动生成
    private String checkResult;//验收结果
    private String assessUserName;//服务评价人姓名
    private String serviceLevel;//服务等级
    private String seviceSuggest;//服务评价
    private Date assessTime;//评价时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getAssessUserName() {
        return assessUserName;
    }

    public void setAssessUserName(String assessUserName) {
        this.assessUserName = assessUserName;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getSeviceSuggest() {
        return seviceSuggest;
    }

    public void setSeviceSuggest(String seviceSuggest) {
        this.seviceSuggest = seviceSuggest;
    }

    public Date getAssessTime() {
        return assessTime;
    }

    public void setAssessTime(Date assessTime) {
        this.assessTime = assessTime;
    }
}
