package easypan.service;

import java.util.List;

import easypan.entity.vo.PaginationResultVO;
import easypan.entity.dto.SessionWebUserDto;
import easypan.entity.po.UserInfo;
import easypan.entity.query.UserInfoQuery;
/**
 * @Description:存储用户信息Service
 * @Author: 春的樱树
 * @Date: 2024-02-23
 */
public interface UserInfoService{

	/**
	 *根据条件查询列表
	 */
	List<UserInfo> findListByParam(UserInfoQuery param);

	/**
	 *根据条件查询数量
	 */
	Integer findCountByParam(UserInfoQuery param);

	/**
	 *分页查询
	 */
	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query);

	/**
	 *新增
	 */
	Integer add(UserInfo bean);

	/**
	 *批量新增
	 */
	Integer addBatch(List<UserInfo> listBean);

	/**
	 *批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserInfo> listBean);

	/**
	 *根据NumberAndUserId查询
	 */
	UserInfo getByNumberAndUserId(Integer number, String userId);

	/**
	 *根据NumberAndUserId更新
	 */
	Integer updateByNumberAndUserId(UserInfo bean, Integer number, String userId);

	/**
	 *根据NumberAndUserId删除
	 */
	Integer deleteByNumberAndUserId(Integer number, String userId);

	/**
	 *根据UserId查询
	 */
	UserInfo getByUserId(String userId);

	/**
	 *根据UserId更新
	 */
	Integer updateByUserId(UserInfo bean, String userId);

	/**
	 *根据UserId删除
	 */
	Integer deleteByUserId(String userId);

	/**
	 *根据Number查询
	 */
	UserInfo getByNumber(Integer number);

	/**
	 *根据Number更新
	 */
	Integer updateByNumber(UserInfo bean, Integer number);

	/**
	 *根据Number删除
	 */
	Integer deleteByNumber(Integer number);

	/**
	 *根据Email查询
	 */
	UserInfo getByEmail(String email);

	/**
	 *根据Email更新
	 */
	Integer updateByEmail(UserInfo bean, String email);

	/**
	 *根据Email删除
	 */
	Integer deleteByEmail(String email);

	/**
	 *根据NickName查询
	 */
	UserInfo getByNickName(String nickName);

	/**
	 *根据NickName更新
	 */
	Integer updateByNickName(UserInfo bean, String nickName);

	/**
	 *根据NickName删除
	 */
	Integer deleteByNickName(String nickName);
	
	void register(String email,String nickName,String password,String emailCode)  ;
	SessionWebUserDto login(String email,String password)  ;
	void resetPwd(String email,String password,String emailCode);

	void updateUserStatus(String userId,Integer status);

	void changeUserSpace(String userId,Long newSpace);

}