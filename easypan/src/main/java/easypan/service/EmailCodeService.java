package easypan.service;

import java.util.List;


import easypan.entity.vo.PaginationResultVO;
import easypan.exception.BusinessException;
import easypan.entity.po.EmailCode;
import easypan.entity.query.EmailCodeQuery;
/**
 * @Description:Service
 * @Author: 春的樱树
 * @Date: 2024-02-23
 */
public interface EmailCodeService{

	/**
	 *根据条件查询列表
	 */
	List<EmailCode> findListByParam(EmailCodeQuery param);

	/**
	 *根据条件查询数量
	 */
	Integer findCountByParam(EmailCodeQuery param);

	/**
	 *分页查询
	 */
	PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query);

	/**
	 *新增
	 */
	Integer add(EmailCode bean);

	/**
	 *批量新增
	 */
	Integer addBatch(List<EmailCode> listBean);

	/**
	 *批量新增或修改
	 */
	Integer addOrUpdateBatch(List<EmailCode> listBean);

	/**
	 *根据EmailAndCode查询
	 */
	EmailCode getByEmailAndCode(String email, String code);

	/**
	 *根据EmailAndCode更新
	 */
	Integer updateByEmailAndCode(EmailCode bean, String email, String code);

	/**
	 *根据EmailAndCode删除
	 */
	Integer deleteByEmailAndCode(String email, String code);
	/**
	 *发送邮箱验证码
	 */
	void sendEmailCode(String email,Integer type) throws BusinessException;
	void checkCode(String email,String code) throws BusinessException;
}