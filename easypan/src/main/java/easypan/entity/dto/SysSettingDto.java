package easypan.entity.dto;

import java.io.Serializable;

public class SysSettingDto implements Serializable {

    private String email = "";
    private String registerEmailTitle = "邮箱验证码";
    private String registerEmailContent = "您好,你的邮箱验证码是:%s 有效期5分钟";
    private Integer userInitUseSpace = 1024;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisterEmailTitle() {
        return registerEmailTitle;
    }

    public void setRegisterEmailTitle(String registerEmailTitle) {
        this.registerEmailTitle = registerEmailTitle;
    }

    public String getRegisterEmailContent() {
        return registerEmailContent;
    }

    public void setRegisterEmailContent(String registerEmailContent) {
        this.registerEmailContent = registerEmailContent;
    }

    public Integer getUserInitUseSpace() {
        return userInitUseSpace;
    }

    public void setUseInitUseSpace(Integer userInitUseSpace) {
        this.userInitUseSpace = userInitUseSpace;
    }
}
