package com.kim.weibao.model.system;


/**
 * 菜单表
 */
public class SysMenu {


    private String id;//标识
    private String menuName;//菜单名
    private String menuPath;//路径
    private String icon;//图标
    private Integer menuPid;//父节点id
    private Integer menuMid;//节点id
    private Integer menuIndex;//序号
    private Boolean isParent;//是否为父节点

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getMenuPid() {
        return menuPid;
    }

    public void setMenuPid(Integer menuPid) {
        this.menuPid = menuPid;
    }

    public Integer getMenuMid() {
        return menuMid;
    }

    public void setMenuMid(Integer menuMid) {
        this.menuMid = menuMid;
    }

    public Integer getMenuIndex() {
        return menuIndex;
    }

    public void setMenuIndex(Integer menuIndex) {
        this.menuIndex = menuIndex;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }
}
