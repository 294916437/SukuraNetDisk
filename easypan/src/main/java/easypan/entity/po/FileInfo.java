package easypan.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import easypan.entity.enums.DateTimePatternEnum;
import easypan.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description:
 * @Author: 春的樱树
 * @Date: 2024-03-16
 */
public class FileInfo implements Serializable {
	/**
	 *文件ID
	 */
	private String fileId;
	/**
	 *用户ID
	 */
	private String userId;
	/**
	 *文件MD5值
	 */
	private String fileMd5;
	/**
	 *父级ID
	 */
	private String filePid;
	/**
	 *文件大小
	 */
	private Long fileSize;
	/**
	 *文件名称
	 */
	private String fileName;
	/**
	 *文件封面
	 */
	private String fileCover;
	/**
	 *文件路径
	 */
	private String filePath;
	/**
	 *文件创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 *文件最近更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;
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
	/**
	 *0:转码中 1:转码失败 2:转码成功
	 */
	@JsonIgnore
	private Integer status;
	/**
	 *加入回收站的时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date recoveryTime;
	/**
	 *标记 0:删除 1:回收 2:正常
	 */
	private Integer delFlag;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	private String nickName;
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

	public void setFileMd5(String fileMd5) {
		this.fileMd5=fileMd5;
	}

	public String getFileMd5() {
		return this.fileMd5;
	}

	public void setFilePid(String filePid) {
		this.filePid=filePid;
	}

	public String getFilePid() {
		return this.filePid;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize=fileSize;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileName(String fileName) {
		this.fileName=fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileCover(String fileCover) {
		this.fileCover=fileCover;
	}

	public String getFileCover() {
		return this.fileCover;
	}

	public void setFilePath(String filePath) {
		this.filePath=filePath;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setCreateTime(Date createTime) {
		this.createTime=createTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime=lastUpdateTime;
	}

	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setFolderType(Integer folderType) {
		this.folderType=folderType;
	}

	public Integer getFolderType() {
		return this.folderType;
	}

	public void setFileCategory(Integer fileCategory) {
		this.fileCategory=fileCategory;
	}

	public Integer getFileCategory() {
		return this.fileCategory;
	}

	public void setFileType(Integer fileType) {
		this.fileType=fileType;
	}

	public Integer getFileType() {
		return this.fileType;
	}

	public void setStatus(Integer status) {
		this.status=status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setRecoveryTime(Date recoveryTime) {
		this.recoveryTime=recoveryTime;
	}

	public Date getRecoveryTime() {
		return this.recoveryTime;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag=delFlag;
	}

	public Integer getDelFlag() {
		return this.delFlag;
	}

}