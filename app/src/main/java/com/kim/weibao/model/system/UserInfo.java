package com.kim.weibao.model.system;


import java.util.Date;

/**
 * 用户表
 */


/**
 * 对于用户的多向关联我没有设计双向关联，其他所有的我都设计了双向管理
 */

public class UserInfo {

    private String id;//标识
    private String account;//账户
    private String password;//密码
    private String userName;//用户姓名
    private String userTel;//电话
    private String userMail;//邮箱
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    private boolean isManager;//是否管理员
    private String examLevel;//审核级别

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean isManager) {
        this.isManager = isManager;
    }

    public String getExamLevel() {
        return examLevel;
    }

    public void setExamLevel(String examLevel) {
        this.examLevel = examLevel;
    }
}
