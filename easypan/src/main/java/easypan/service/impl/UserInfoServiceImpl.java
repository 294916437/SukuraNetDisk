package easypan.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import easypan.entity.vo.PaginationResultVO;
import easypan.exception.BusinessException;
import easypan.entity.po.FileInfo;
import easypan.entity.po.UserInfo;
import easypan.component.RedisComponent;
import easypan.entity.Constants.Constants;
import easypan.entity.config.AppConfig;
import easypan.entity.dto.SessionWebUserDto;
import easypan.entity.dto.SysSettingDto;
import easypan.entity.dto.UserSpaceDto;
import easypan.entity.enums.PageSize;
import easypan.entity.enums.UserStatusEnum;
import easypan.entity.query.UserInfoQuery;
import easypan.entity.query.FileInfoQuery;
import easypan.entity.query.SimplePage;
import easypan.service.EmailCodeService;
import easypan.service.UserInfoService;
import easypan.utils.StringTools;
import easypan.mappers.FileInfoMapper;
import easypan.mappers.UserInfoMapper;

/**
 * @Description:存储用户信息Service
 * @Author: 春的樱树
 * @Date: 2024-02-23
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
	@Resource
	private EmailCodeService emailCodeService;
	@Resource
	private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;
	@Resource
	private RedisComponent redisComponent;
	@Resource
	private AppConfig appConfig;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserInfo> findListByParam(UserInfoQuery query) {
		return this.userInfoMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(UserInfoQuery query) {
		return this.userInfoMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserInfo> list = this.findListByParam(query);
		PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(),
				page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserInfo bean) {
		return this.userInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据NumberAndUserId查询
	 */
	@Override
	public UserInfo getByNumberAndUserId(Integer number, String userId) {
		return this.userInfoMapper.selectByNumberAndUserId(number, userId);
	}

	/**
	 * 根据NumberAndUserId更新
	 */
	@Override
	public Integer updateByNumberAndUserId(UserInfo bean, Integer number, String userId) {
		return this.userInfoMapper.updateByNumberAndUserId(bean, number, userId);
	}

	/**
	 * 根据NumberAndUserId删除
	 */
	@Override
	public Integer deleteByNumberAndUserId(Integer number, String userId) {
		return this.userInfoMapper.deleteByNumberAndUserId(number, userId);
	}

	/**
	 * 根据UserId查询
	 */
	@Override
	public UserInfo getByUserId(String userId) {
		return this.userInfoMapper.selectByUserId(userId);
	}

	/**
	 * 根据UserId更新
	 */
	@Override
	public Integer updateByUserId(UserInfo bean, String userId) {
		return this.userInfoMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public Integer deleteByUserId(String userId) {
		return this.userInfoMapper.deleteByUserId(userId);
	}

	/**
	 * 根据Number查询
	 */
	@Override
	public UserInfo getByNumber(Integer number) {
		return this.userInfoMapper.selectByNumber(number);
	}

	/**
	 * 根据Number更新
	 */
	@Override
	public Integer updateByNumber(UserInfo bean, Integer number) {
		return this.userInfoMapper.updateByNumber(bean, number);
	}

	/**
	 * 根据Number删除
	 */
	@Override
	public Integer deleteByNumber(Integer number) {
		return this.userInfoMapper.deleteByNumber(number);
	}

	/**
	 * 根据Email查询
	 */
	@Override
	public UserInfo getByEmail(String email) {
		return this.userInfoMapper.selectByEmail(email);
	}

	/**
	 * 根据Email更新
	 */
	@Override
	public Integer updateByEmail(UserInfo bean, String email) {
		return this.userInfoMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据Email删除
	 */
	@Override
	public Integer deleteByEmail(String email) {
		return this.userInfoMapper.deleteByEmail(email);
	}

	/**
	 * 根据NickName查询
	 */
	@Override
	public UserInfo getByNickName(String nickName) {
		return this.userInfoMapper.selectByNickName(nickName);
	}

	/**
	 * 根据NickName更新
	 */
	@Override
	public Integer updateByNickName(UserInfo bean, String nickName) {
		return this.userInfoMapper.updateByNickName(bean, nickName);
	}

	/**
	 * 根据NickName删除
	 */
	@Override
	public Integer deleteByNickName(String nickName) {
		return this.userInfoMapper.deleteByNickName(nickName);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void register(String email, String nickName, String password, String emailCode) throws BusinessException {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if (userInfo != null) {
			throw new BusinessException("该邮箱账号已存在");
		}
		UserInfo nickNameUser = this.userInfoMapper.selectByNickName(nickName);
		if (nickNameUser != null) {
			throw new BusinessException("昵称已存在");
		}
		// 校验邮箱验证码
		emailCodeService.checkCode(email, emailCode);
		String userId = StringTools.getRandomNumber(Constants.LENGTH_15);
		userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setNickName(nickName);
		userInfo.setEmail(email);
		userInfo.setPassword(StringTools.encodeByMd5(password));
		userInfo.setJoinTime(new Date());
		userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
		userInfo.setUseSpace(0L);
		SysSettingDto sysSettingDto = new SysSettingDto();
		sysSettingDto.setEmail(email);
		redisComponent.saveSysSettingDto(sysSettingDto);
		userInfo.setTotalSpace(sysSettingDto.getUserInitUseSpace() * Constants.MB);
		this.userInfoMapper.insert(userInfo);
		
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public SessionWebUserDto login(String email,String password) throws BusinessException {
		UserInfo userInfo=this.userInfoMapper.selectByEmail(email);
		if(null==userInfo||!userInfo.getPassword().equals(password)) {
			throw new BusinessException("账号或密码错误");
		}
		if(UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
			throw new BusinessException("账号已被禁用");
		}
		UserInfo updateInfo=new UserInfo();
		updateInfo.setLastLoginTime(new Date());
		this.userInfoMapper.updateByUserId(updateInfo, userInfo.getUserId());
		SessionWebUserDto sessionWebUserDto=new SessionWebUserDto();
		sessionWebUserDto.setNickName(userInfo.getNickName());
		sessionWebUserDto.setUserId(userInfo.getUserId());
		if(ArrayUtils.contains(appConfig.getAdminEmails().split(","), email)) {
			sessionWebUserDto.setIsAdmin(true);
		}else {
			sessionWebUserDto.setIsAdmin(false);
		}
		//redis存储用户空间
		UserSpaceDto userSpaceDto=new UserSpaceDto();
		Long useSpace=fileInfoMapper.selectUseSpace(userInfo.getUserId());
		userSpaceDto.setUseSpace(useSpace);
		userSpaceDto.setTotalSpace(userInfo.getTotalSpace());
		redisComponent.saveUserSpaceUse(userInfo.getUserId(),userSpaceDto);
		return sessionWebUserDto;
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void resetPwd(String email,String password,String emailCode) {
		UserInfo userInfo=this.userInfoMapper.selectByEmail(email);
		if(null==userInfo) {
			throw new BusinessException("账号邮箱不存在");
		}
		emailCodeService.checkCode(email, emailCode);
		UserInfo updateInfo=new UserInfo();
		updateInfo.setPassword(StringTools.encodeByMd5(password));
		this.userInfoMapper.updateByEmail(updateInfo, email);
	}
	
}