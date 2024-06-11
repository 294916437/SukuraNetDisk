package easypan.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.multipart.MultipartFile;

import easypan.entity.vo.PaginationResultVO;
import easypan.entity.dto.SessionWebUserDto;
import easypan.entity.dto.UploadResultDto;
import easypan.entity.po.FileInfo;
import easypan.entity.query.FileInfoQuery;
/**
 * @Description:Service
 * @Author: 春的樱树
 * @Date: 2024-03-16
 */
public interface FileInfoService{

	/**
	 *根据条件查询列表
	 */
	List<FileInfo> findListByParam(FileInfoQuery param);

	/**
	 *根据条件查询数量
	 */
	Integer findCountByParam(FileInfoQuery param);

	/**
	 *分页查询
	 */
	PaginationResultVO<FileInfo> findListByPage(FileInfoQuery query);

	/**
	 *新增
	 */
	Integer add(FileInfo bean);

	/**
	 *批量新增
	 */
	Integer addBatch(List<FileInfo> listBean);

	/**
	 *批量新增或修改
	 */
	Integer addOrUpdateBatch(List<FileInfo> listBean);

	/**
	 *根据FileIdAndUserId查询
	 */
	FileInfo getByFileIdAndUserId(String fileId, String userId);

	/**
	 *根据FileIdAndUserId更新
	 */
	Integer updateByFileIdAndUserId(FileInfo bean, String fileId, String userId);

	/**
	 *根据FileIdAndUserId删除
	 */
	Integer deleteByFileIdAndUserId(String fileId, String userId);
	
	UploadResultDto uploadFile(SessionWebUserDto webUserDto,String fileId,MultipartFile file,String fileName,
			String filePid,String fileMd5,Integer chunkIndex,Integer chunks);
	FileInfo newFolder(String filePid,String userId,String folderName);
	FileInfo rename(String fileId,String userId,String fileName);
	void changeFileFolder(String fileIds,String filePid,String userId);
	void removeFile2RecycleBatch(String userId,String fileIds);
	void recoverFileBatch(String userId,String fileIds);
	void delFileBatch(String userId,String fileIds,boolean adminOp);

	void checkRootFilePid(String rootFilePid,String userId,String fileId);
}