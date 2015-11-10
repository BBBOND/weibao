package com.kim.weibao.model.basicData;

/**
 * Created by Courage on 2015/10/14.
 */

import java.util.Date;

/**
 * 社区表
 */

public class AreaInfo {
    private String id;       //标识
    private String areaCode; //社区编码
    private String areaName; //社区名称
    private String areaAddress;//社区地址
    private String areaType;//社区类型
    private String areaContactsName;//联系人姓名
    private String areaTel;//联系人电话
    private String areaMail;//联系人邮箱
    private String areaSite;//地理位置
    private String updateUserId;//更新人标识
    private Date upateTime;//更新时间
    private String areaManageId;
    private String repairDepartmentId;

    public AreaInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaAddress() {
        return areaAddress;
    }

    public void setAreaAddress(String areaAddress) {
        this.areaAddress = areaAddress;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getAreaContactsName() {
        return areaContactsName;
    }

    public void setAreaContactsName(String areaContactsName) {
        this.areaContactsName = areaContactsName;
    }

    public String getAreaTel() {
        return areaTel;
    }

    public void setAreaTel(String areaTel) {
        this.areaTel = areaTel;
    }

    public String getAreaMail() {
        return areaMail;
    }

    public void setAreaMail(String areaMail) {
        this.areaMail = areaMail;
    }

    public String getAreaSite() {
        return areaSite;
    }

    public void setAreaSite(String areaSite) {
        this.areaSite = areaSite;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpateTime() {
        return upateTime;
    }

    public void setUpateTime(Date upateTime) {
        this.upateTime = upateTime;
    }

    public String getAreaManageId() {
        return areaManageId;
    }

    public void setAreaManageId(String areaManageId) {
        this.areaManageId = areaManageId;
    }

    public String getRepairDepartmentId() {
        return repairDepartmentId;
    }

    public void setRepairDepartmentId(String repairDepartmentId) {
        this.repairDepartmentId = repairDepartmentId;
    }
}
