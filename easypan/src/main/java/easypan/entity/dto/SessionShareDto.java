package easypan.entity.dto;

import java.util.Date;

public class SessionShareDto {
    private  String shareId;
    private  String ShareUserId;
    private Date expireTime;

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getShareUserId() {
        return ShareUserId;
    }

    public void setShareUserId(String shareUserId) {
        ShareUserId = shareUserId;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    private  String fileId;

}
