package easypan.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class AppConfig {
    @Value("${spring.mail.username:}")
    private String sendUserName;

    @Value("${spring.mail.nickname}")
    private String nickName;
    @Value("${admin.emails:}")
    private String adminEmails;

    @Value("${project.folder:}")
    private String projectFolder;

    public String getNickName() {
        return nickName;
    }
    public String getProjectFolder() {
        return projectFolder;
    }

    public String getAdminEmails() {
        return adminEmails;
    }

    public String getSendUserName() {
        return sendUserName;
    }
}
