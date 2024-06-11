package easypan.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import easypan.entity.enums.DateTimePatternEnum;
import easypan.utils.DateUtils;

/**
 * @Description:分享信息
 * @Author: 春的樱树
 * @Date: 2024-04-29
 */
public class FileShare implements Serializable {
	/**
	 *分享ID
	 */
	private String shareId;
	/**
	 *文件ID
	 */
	private String fileId;
	/**
	 *分享人ID
	 */
	private String userId;
	/**
	 *有效期类型 0:1天 1:7天 2:30天 4:永久有效
	 */
	private Integer validType;
	/**
	 *失效时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expireTime;
	/**
	 *分享时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date shareTime;
	/**
	 *提取码
	 */
	private String code;
	/**
	 *浏览次数
	 */
	private Integer viewCount;
	/**
	 *文件名称
	 */
	private String fileName;

	/**
	 *文件封面
	 */
	private String fileCover;

	/**
	 *0:文件 1:目录
	 */
	private Integer folderType;
	/**
	 *文件分类 1:视频 2:音频 3:图片 4:文档 5:其他
	 */
	private Integer fileCategory;
	/**
	 *1:视频 2:音频 3:图片 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:其他
	 */
	private Integer fileType;
	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getFileCategory() {
		return fileCategory;
	}

	public void setFileCategory(Integer fileCategory) {
		this.fileCategory = fileCategory;
	}

	public Integer getFolderType() {
		return folderType;
	}

	public void setFolderType(Integer folderType) {
		this.folderType = folderType;
	}

	public String getFileCover() {
		return fileCover;
	}

	public void setFileCover(String fileCover) {
		this.fileCover = fileCover;
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

	@Override
	public String toString() {
		return "分享ID:" + (shareId == null?"空": shareId) + ",文件ID:" + (fileId == null?"空": fileId) + ",分享人ID:" + (userId == null?"空": userId) + ",有效期类型 0:1天 1:7天 2:30天 4:永久有效:" + (validType == null?"空": validType) + ",失效时间:" + (expireTime == null?"空": DateUtils.format(expireTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",分享时间:" + (shareTime == null?"空": DateUtils.format(shareTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",提取码:" + (code == null?"空": code) + ",浏览次数:" + (viewCount == null?"空": viewCount);
	}
}