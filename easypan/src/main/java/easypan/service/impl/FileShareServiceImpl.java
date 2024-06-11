package easypan.service.impl;

import java.util.List;

import javax.annotation.Resource;

import easypan.entity.Constants.Constants;
import easypan.entity.dto.SessionShareDto;
import easypan.entity.enums.ResponseCodeEnum;
import easypan.entity.enums.ShareValidTypeEnums;
import easypan.exception.BusinessException;
import easypan.utils.DateUtils;
import java.util.Date;

import easypan.utils.StringTools;
import org.springframework.stereotype.Service;

import easypan.entity.vo.PaginationResultVO;
import easypan.entity.po.FileShare;
import easypan.entity.enums.PageSize;
import easypan.entity.query.FileShareQuery;
import easypan.entity.query.SimplePage;
import easypan.service.FileShareService;
import easypan.mappers.FileShareMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:分享信息Service
 * @Author: 春的樱树
 * @Date: 2024-04-29
 */
@Service("fileShareService")
public class FileShareServiceImpl implements FileShareService {

	@Resource
	private FileShareMapper<FileShare ,FileShareQuery> fileShareMapper;

	/**
	 *根据条件查询列表
	 */
	@Override
	public List<FileShare> findListByParam(FileShareQuery query) {
		return this.fileShareMapper.selectList(query);
	}
	/**
	 *根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(FileShareQuery query) {
		return this.fileShareMapper.selectCount(query);
	}
	/**
	 *分页查询
	 */
	@Override
	public PaginationResultVO<FileShare> findListByPage(FileShareQuery query) {
		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null ? PageSize.SIZE15.getSize():query.getPageSize() ;
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<FileShare> list = this.findListByParam(query);
		PaginationResultVO<FileShare> result = new PaginationResultVO(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);
		return result;
	}
	/**
	 *新增
	 */
	@Override
	public Integer add(FileShare bean) {
		return this.fileShareMapper.insert(bean);
	}
	/**
	 *批量新增
	 */
	@Override
	public Integer addBatch(List<FileShare> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		 return this.fileShareMapper.insertBatch(listBean);
	}
	/**
	 *批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<FileShare> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		 return this.fileShareMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 *根据ShareId查询
	 */
	@Override
	public FileShare getByShareId(String shareId) {
		return this.fileShareMapper.selectByShareId(shareId);
	}
	/**
	 *根据ShareId更新
	 */
	@Override
	public Integer updateByShareId(FileShare bean, String shareId) {
		return this.fileShareMapper.updateByShareId(bean,shareId);
	}
	/**
	 *根据ShareId删除
	 */
	@Override
	public Integer deleteByShareId(String shareId) {
		return this.fileShareMapper.deleteByShareId(shareId);
	}

	@Override
	public void saveShare(FileShare share) {
		ShareValidTypeEnums typeEnums=ShareValidTypeEnums.getEnumByType(share.getValidType());
		if(typeEnums==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (ShareValidTypeEnums.FOREVER!=typeEnums){
			share.setExpireTime(DateUtils.getAfterDate(typeEnums.getDays()));
		}
		Date curDate=new Date();
		share.setShareTime(curDate);
		if (StringTools.isEmpty(share.getCode())){
			share.setCode(StringTools.getRandomString(Constants.LENGTH_5));
		}
		share.setShareId(StringTools.getRandomString(Constants.LENGTH_20));
		share.setViewCount(0);
		this.fileShareMapper.insert(share);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void deleteFileShareBatch(String[]shareIds, String userId) {
		Integer count =this.fileShareMapper.deleteFileShareBatch(shareIds,userId);
		if (count!=shareIds.length){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
	}

	@Override
	public SessionShareDto checkShareCode(String shareId, String code) {
		FileShare fileShare=this.fileShareMapper.selectByShareId(shareId);
		if (null==fileShare|| (fileShare.getExpireTime()!=null&&fileShare.getExpireTime().getTime()<System.currentTimeMillis())){
			throw new BusinessException(ResponseCodeEnum.CODE_902.getMsg());
		}
		if (fileShare.getCode().equals(code)){
			throw new BusinessException("提取码错误");
		}
		// 更新浏览次数
		this.fileShareMapper.updateViewShareCount(shareId);
		SessionShareDto sessionShareDto=new SessionShareDto();
		sessionShareDto.setShareId(shareId);
		sessionShareDto.setShareUserId(fileShare.getUserId());
		sessionShareDto.setFileId(fileShare.getFileId());
		sessionShareDto.setExpireTime(fileShare.getExpireTime());
		return sessionShareDto;
	}

}