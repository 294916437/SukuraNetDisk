package easypan.entity.query;

import java.util.Date;

/**
 * @Description:分享信息查询对象
 * @Author: 春的樱树
 * @Date: 2024-04-29
 */
public class FileShareQuery extends BaseQuery {
	/**
	 *分享ID
	 */
	private String shareId;
	private String shareIdFuzzy;
	/**
	 *文件ID
	 */
	private String fileId;
	private String fileIdFuzzy;
	/**
	 *分享人ID
	 */
	private String userId;
	private String userIdFuzzy;
	/**
	 *有效期类型 0:1天 1:7天 2:30天 4:永久有效
	 */
	private Integer validType;
	/**
	 *失效时间
	 */
	private Date expireTime;
	private String expireTimeStart;
	private String expireTimeEnd;
	/**
	 *分享时间
	 */
	private Date shareTime;
	private String shareTimeStart;
	private String shareTimeEnd;
	/**
	 *提取码
	 */
	private String code;
	private String codeFuzzy;
	/**
	 *浏览次数
	 */
	private Integer viewCount;

	private  boolean queryFileName;

	public boolean getQueryFileName() {
		return this.queryFileName;
	}

	public void setQueryFileName(boolean queryFileName) {
		this.queryFileName = queryFileName;
	}
	public void setShareId(String shareId) {
		this.shareId=shareId;
	}

	public String getShareId() {
		return this.shareId;
	}

	public void setFileId(String fileId) {
		this.fileId=fileId;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setUserId(String userId) {
		this.userId=userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setValidType(Integer validType) {
		this.validType=validType;
	}

	public Integer getValidType() {
		return this.validType;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime=expireTime;
	}

	public Date getExpireTime() {
		return this.expireTime;
	}

	public void setShareTime(Date shareTime) {
		this.shareTime=shareTime;
	}

	public Date getShareTime() {
		return this.shareTime;
	}

	public void setCode(String code) {
		this.code=code;
	}

	public String getCode() {
		return this.code;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount=viewCount;
	}

	public Integer getViewCount() {
		return this.viewCount;
	}

	public void setShareIdFuzzy(String shareIdFuzzy) {
		this.shareIdFuzzy=shareIdFuzzy;
	}

	public String getShareIdFuzzy() {
		return this.shareIdFuzzy;
	}

	public void setFileIdFuzzy(String fileIdFuzzy) {
		this.fileIdFuzzy=fileIdFuzzy;
	}

	public String getFileIdFuzzy() {
		return this.fileIdFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy=userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setExpireTimeStart(String expireTimeStart) {
		this.expireTimeStart=expireTimeStart;
	}

	public String getExpireTimeStart() {
		return this.expireTimeStart;
	}

	public void setExpireTimeEnd(String expireTimeEnd) {
		this.expireTimeEnd=expireTimeEnd;
	}

	public String getExpireTimeEnd() {
		return this.expireTimeEnd;
	}

	public void setShareTimeStart(String shareTimeStart) {
		this.shareTimeStart=shareTimeStart;
	}

	public String getShareTimeStart() {
		return this.shareTimeStart;
	}

	public void setShareTimeEnd(String shareTimeEnd) {
		this.shareTimeEnd=shareTimeEnd;
	}

	public String getShareTimeEnd() {
		return this.shareTimeEnd;
	}

	public void setCodeFuzzy(String codeFuzzy) {
		this.codeFuzzy=codeFuzzy;
	}

	public String getCodeFuzzy() {
		return this.codeFuzzy;
	}

}