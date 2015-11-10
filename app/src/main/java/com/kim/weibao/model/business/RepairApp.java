package com.kim.weibao.model.business;

import java.util.Date;


/**
 * 维修申请单
 */
public class RepairApp {


    private String id;//标识
    private String areaId;
    private String appUserId;
    private String repairDepartmentId;
    private String machineId;//机器型号ID
    private String appCode;//维修单号
    private String appType;//申请单类型
    private String machineName;//设备名称
    private String machineType;//设备类型
    private String errorType;//错误类型
    private String infoSource;//信息源
    private String department;//部门
    private String appDescription;//描述
    private Boolean isRapadRepair;//快速维保
    private Date receiveTime;//接报时间
    private Date arriveTime;//到达时间
    private Date appTime;//申请时间
    private String appStatus;//申请状态
    private String planId;
    private int step;//流转步骤

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

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getInfoSource() {
        return infoSource;
    }

    public void setInfoSource(String infoSource) {
        this.infoSource = infoSource;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public Boolean getIsRapadRepair() {
        return isRapadRepair;
    }

    public void setIsRapadRepair(Boolean isRapadRepair) {
        this.isRapadRepair = isRapadRepair;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Date getAppTime() {
        return appTime;
    }

    public void setAppTime(Date appTime) {
        this.appTime = appTime;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }


    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getRepairDepartmentId() {
        return repairDepartmentId;
    }

    public void setRepairDepartmentId(String repairDepartmentId) {
        this.repairDepartmentId = repairDepartmentId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
