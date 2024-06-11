package easypan.component;

import javax.annotation.Resource;

import easypan.entity.po.UserInfo;
import easypan.entity.query.UserInfoQuery;
import easypan.mappers.UserInfoMapper;
import org.springframework.stereotype.Component;

import easypan.entity.Constants.Constants;
import easypan.entity.dto.DownloadFileDto;
import easypan.entity.dto.SysSettingDto;
import easypan.entity.dto.UserSpaceDto;
import easypan.entity.po.FileInfo;
import easypan.entity.query.FileInfoQuery;
import easypan.mappers.FileInfoMapper;

@Component("redisComponent")
public class RedisComponent {
	@Resource
	private RedisUtils redisUtils;
	@Resource
	private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;
	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
	public  void saveSysSettingDto(SysSettingDto sysSettingDto){
		redisUtils.set(Constants.REDIS_KEY_SYS_SETTING,sysSettingDto);
	}
	public SysSettingDto getSysSettingDto() {
		SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
		if (null == sysSettingDto) {
			sysSettingDto = new SysSettingDto();
			redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto);
		}
		return sysSettingDto;
	}
	public void saveUserSpaceUse(String userId, UserSpaceDto userSpaceDto) {
		redisUtils.setex(Constants.REDIS_KEY_USER_SPACE_USE + userId, userSpaceDto, Constants.REDIS_KEY_EXPIRES_DAY);
	}
	public UserSpaceDto updateUserSpaceUse(String userId) {
		UserSpaceDto userSpaceDto=new UserSpaceDto();
		Long useSpace=this.fileInfoMapper.selectUseSpace(userId);
		userSpaceDto.setUseSpace(useSpace);
		UserInfo useInfo=userInfoMapper.selectByUserId(userId);
		userSpaceDto.setTotalSpace(useInfo.getTotalSpace());
		redisUtils.setex(Constants.REDIS_KEY_USER_SPACE_USE+userId,userSpaceDto,Constants.REDIS_KEY_EXPIRES_DAY);
		return userSpaceDto;
	}
	public UserSpaceDto getUserSpace(String userId) {
		UserSpaceDto spaceDto = (UserSpaceDto) redisUtils.get(Constants.REDIS_KEY_USER_SPACE_USE + userId);
		if (null == spaceDto) {
			spaceDto = new UserSpaceDto();
			Long useSpace = fileInfoMapper.selectUseSpace(userId);
			spaceDto.setUseSpace(useSpace);
			spaceDto.setTotalSpace(getSysSettingDto().getUserInitUseSpace() * Constants.MB);
			saveUserSpaceUse(userId, spaceDto);

		}
		return spaceDto;
	}
	public void saveTempFileSize(String userId, String fileId,Long fileSize) {
		Long currentSize=getFileTempSize(userId, fileId);
		redisUtils.setex(Constants.REDIS_KEY_USER_FILE_TEMP_SIZE+userId+fileId, currentSize+fileSize,Constants.REDIS_KEY_EXPIRES_HOUR);
	}
	//获取临时文件大小
	public Long getFileTempSize(String userId, String fileId) {
		Long currentSize = getFileSizeFromRedis(Constants.REDIS_KEY_USER_FILE_TEMP_SIZE + userId + fileId);
		return currentSize;
	}

	private Long getFileSizeFromRedis(String key) {
		Object sizeObj = redisUtils.get(key);
		if (sizeObj == null) {
			return 0L;
		}
		if (sizeObj instanceof Integer) {
			return ((Integer) sizeObj).longValue();
		} else if (sizeObj instanceof Long) {
			return (Long) sizeObj;
		}
		return 0L;
	}
	public void saveDownloadCode(String code,DownloadFileDto downloadFileDto) {
		redisUtils.setex(Constants.REDIS_KEY_DOWNLOAD+code, downloadFileDto, Constants.REDIS_KEY_EXPIRES_FIVE_MIN);
	}
	public DownloadFileDto getDownloadCode(String code) {
		return (DownloadFileDto) redisUtils.get(Constants.REDIS_KEY_DOWNLOAD+code);
	}
}
