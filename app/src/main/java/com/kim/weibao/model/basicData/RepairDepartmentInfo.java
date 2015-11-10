package com.kim.weibao.model.basicData;


/**
 * 维保部门表
 */
public class RepairDepartmentInfo {


    private String id;
    private String repairContectsName;
    private String repairContectsTel;
    private String repairContectsAddress;
    private String repairContectsMail;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepairContectsName() {
        return repairContectsName;
    }

    public void setRepairContectsName(String repairContectsName) {
        this.repairContectsName = repairContectsName;
    }

    public String getRepairContectsTel() {
        return repairContectsTel;
    }

    public void setRepairContectsTel(String repairContectsTel) {
        this.repairContectsTel = repairContectsTel;
    }

    public String getRepairContectsAddress() {
        return repairContectsAddress;
    }

    public void setRepairContectsAddress(String repairContectsAddress) {
        this.repairContectsAddress = repairContectsAddress;
    }

    public String getRepairContectsMail() {
        return repairContectsMail;
    }

    public void setRepairContectsMail(String repairContectsMail) {
        this.repairContectsMail = repairContectsMail;
    }
}
