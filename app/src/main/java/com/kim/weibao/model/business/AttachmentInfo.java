package com.kim.weibao.model.business;


import java.util.Date;


/**
 * 附件表
 */

public class AttachmentInfo {

    private String id;//标识
    private String attachmentPath;//附件路径
    private String attchmentDescription;//备注
    private String fileType;//文件类型
    private String fileName;//文件名
    private boolean isImage;//是否图片
    private Date uploadTime;//上传时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getAttchmentDescription() {
        return attchmentDescription;
    }

    public void setAttchmentDescription(String attchmentDescription) {
        this.attchmentDescription = attchmentDescription;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean isImage) {
        this.isImage = isImage;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
