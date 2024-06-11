package easypan.service;

import java.util.List;

import javax.annotation.Resource;

import easypan.entity.dto.SessionShareDto;
import easypan.entity.vo.PaginationResultVO;
import easypan.entity.po.FileShare;
import easypan.entity.query.FileShareQuery;
/**
 * @Description:分享信息Service
 * @Author: 春的樱树
 * @Date: 2024-04-29
 */
public interface FileShareService{

	/**
	 *根据条件查询列表
	 */
	List<FileShare> findListByParam(FileShareQuery param);

	/**
	 *根据条件查询数量
	 */
	Integer findCountByParam(FileShareQuery param);

	/**
	 *分页查询
	 */
	PaginationResultVO<FileShare> findListByPage(FileShareQuery query);

	/**
	 *新增
	 */
	Integer add(FileShare bean);

	/**
	 *批量新增
	 */
	Integer addBatch(List<FileShare> listBean);

	/**
	 *批量新增或修改
	 */
	Integer addOrUpdateBatch(List<FileShare> listBean);

	/**
	 *根据ShareId查询
	 */
	FileShare getByShareId(String shareId);

	/**
	 *根据ShareId更新
	 */
	Integer updateByShareId(FileShare bean, String shareId);

	/**
	 *根据ShareId删除
	 */
	Integer deleteByShareId(String shareId);
	void saveShare(FileShare share);
	void deleteFileShareBatch(String[] fileIds,String userId);

	SessionShareDto checkShareCode(String shareId,String  code);
}