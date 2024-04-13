package easypan.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import easypan.entity.enums.DateTimePatternEnum;
import easypan.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description:存储用户信息
 * @Author: 春的樱树
 * @Date: 2024-02-23
 */
public class UserInfo implements Serializable {
	/**
	 *用户顺序
	 */
	private Integer number;
	/**
	 *用户ID
	 */
	private String userId;
	/**
	 *昵称
	 */
	private String nickName;
	/**
	 *邮箱
	 */
	private String email;
	/**
	 *qqOpenId
	 */
	private String qqOpenId;
	/**
	 *qq头像
	 */
	private String qqAvatar;
	/**
	 *密码
	 */
	private String password;
	/**
	 *加入时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date joinTime;
	/**
	 *最后登陆时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;
	/**
	 *0:禁用 1:启用
	 */
	@JsonIgnore
	private Integer status;
	/**
	 *使用空间
	 */
	private Long useSpace;
	/**
	 *总空间
	 */
	private Long totalSpace;
	public void setNumber(Integer number) {
		this.number=number;
	}

	public Integer getNumber() {
		return this.number;
	}

	public void setUserId(String userId) {
		this.userId=userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setNickName(String nickName) {
		this.nickName=nickName;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setEmail(String email) {
		this.email=email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setPassword(String password) {
		this.password=password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime=joinTime;
	}

	public Date getJoinTime() {
		return this.joinTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime=lastLoginTime;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setStatus(Integer status) {
		this.status=status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setUseSpace(Long useSpace) {
		this.useSpace=useSpace;
	}

	public Long getUseSpace() {
		return this.useSpace;
	}

	public void setTotalSpace(Long totalSpace) {
		this.totalSpace=totalSpace;
	}

	public Long getTotalSpace() {
		return this.totalSpace;
	}

	@Override
	public String toString() {
		return "用户顺序:" + (number == null?"空": number) + ",用户ID:" + (userId == null?"空": userId) + ",昵称:" + (nickName == null?"空": nickName) + ",邮箱:" + (email == null?"空": email) + ",密码:" + (password == null?"空": password) + ",加入时间:" + (joinTime == null?"空": DateUtils.format(joinTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",最后登陆时间:" + (lastLoginTime == null?"空": DateUtils.format(lastLoginTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",0:禁用 1:启用:" + (status == null?"空": status) + ",使用空间:" + (useSpace == null?"空": useSpace) + ",总空间:" + (totalSpace == null?"空": totalSpace);
	}

}