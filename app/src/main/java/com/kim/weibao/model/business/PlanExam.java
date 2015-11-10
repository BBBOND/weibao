package com.kim.weibao.model.business;

import java.util.Date;

/**
 * Created by Courage on 2015/10/14.
 */

/**
 * 方案审核表
 */
public class PlanExam {

    private String id;//标识
    private String appCode;//申请单号
    private String examStatus;//审核状态（通过/未通过）
    private int examLevel;//审核级别（1，2，3）
    private Date examTime;//产生日期
    private String examDescription;//描述


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

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }

    public int getExamLevel() {
        return examLevel;
    }

    public void setExamLevel(int examLevel) {
        this.examLevel = examLevel;
    }

    public Date getExamTime() {
        return examTime;
    }

    public void setExamTime(Date examTime) {
        this.examTime = examTime;
    }

    public String getExamDescription() {
        return examDescription;
    }

    public void setExamDescription(String examDescription) {
        this.examDescription = examDescription;
    }


}
