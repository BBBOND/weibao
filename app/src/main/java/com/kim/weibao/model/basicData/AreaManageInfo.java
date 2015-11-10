package com.kim.weibao.model.basicData;

/**
 * 社区管理单位表
 */
public class AreaManageInfo {


    private String id;//标识
    private String areaManageName;//部门名称
    private String managerContectsName;//联系人姓名
    private String managerContectsTel;//联系人地址
    private String managerContectsAddress;//联系人地址
    private String managerContectsMail;//联系人邮箱
    private String examLevels;//审核级别

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaManageName() {
        return areaManageName;
    }

    public void setAreaManageName(String areaManageName) {
        this.areaManageName = areaManageName;
    }

    public String getManagerContectsName() {
        return managerContectsName;
    }

    public void setManagerContectsName(String managerContectsName) {
        this.managerContectsName = managerContectsName;
    }

    public String getManagerContectsTel() {
        return managerContectsTel;
    }

    public void setManagerContectsTel(String managerContectsTel) {
        this.managerContectsTel = managerContectsTel;
    }

    public String getManagerContectsAddress() {
        return managerContectsAddress;
    }

    public void setManagerContectsAddress(String managerContectsAddress) {
        this.managerContectsAddress = managerContectsAddress;
    }

    public String getManagerContectsMail() {
        return managerContectsMail;
    }

    public void setManagerContectsMail(String managerContectsMail) {
        this.managerContectsMail = managerContectsMail;
    }

    public String getExamLevels() {
        return examLevels;
    }

    public void setExamLevels(String examLevels) {
        this.examLevels = examLevels;
    }

}
