package com.kim.weibao.model.basicData;


/**
 * 设备表
 */
public class MachineInfo {


    private String id;//标识
    private String areaId;

    private String machineCode;//设备编码
    private String machineName;//设备名称
    private String testCircle;//巡检周期
    private String circleUnit;//单位
    private String testDescription;//描述

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getTestCircle() {
        return testCircle;
    }

    public void setTestCircle(String testCircle) {
        this.testCircle = testCircle;
    }

    public String getCircleUnit() {
        return circleUnit;
    }

    public void setCircleUnit(String circleUnit) {
        this.circleUnit = circleUnit;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

}
