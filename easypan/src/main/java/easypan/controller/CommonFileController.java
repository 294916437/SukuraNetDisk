package easypan.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import easypan.entity.vo.FolderVO;
import org.apache.commons.lang3.StringUtils;

import easypan.component.RedisComponent;
import easypan.entity.Constants.Constants;
import easypan.entity.config.AppConfig;
import easypan.entity.dto.DownloadFileDto;
import easypan.entity.enums.FileCategoryEnums;
import easypan.entity.enums.FileFolderTypeEnums;
import easypan.entity.enums.ResponseCodeEnum;
import easypan.entity.po.FileInfo;
import easypan.entity.query.FileInfoQuery;
import easypan.entity.vo.FileInfoVO;
import easypan.entity.vo.ResponseVO;
import easypan.exception.BusinessException;
import easypan.service.FileInfoService;
import easypan.utils.CopyTools;
import easypan.utils.StringTools;

public class CommonFileController extends ABaseController {
	@Resource
	private AppConfig appConfig;
	@Resource
	private FileInfoService fileInfoService;
	@Resource
	private RedisComponent redisComponent;

	protected void getImage(HttpServletResponse response, String imageFolder, String imageName) {
		if (StringTools.isEmpty(imageFolder) || StringTools.isEmpty(imageName) || !StringTools.pathIsOK(imageName)
				|| !StringTools.pathIsOK(imageFolder)) {
			return;
		}
		String imageSuffix = StringTools.getFileSuffix(imageName);
		String filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE +  imageFolder +"/"+imageName;
		imageSuffix = imageSuffix.replace(".", "");
		String contentType="image/"+imageSuffix;
		response.setContentType(contentType);
		response.setHeader("X-Frame-Options", "DENY");
		response.setHeader("Cache-Control", "no-cache,, no-store, must-revalidate, max-age=259200");
		response.setHeader("Cache-Control", "no-cache='set-cookie'");
		response.setHeader("Pragma","no-cache");
		readFile(response, filePath);
	}

	// 前端视频预览，将视频文件以分片的部分形式获取，从而实现视频的分片播放
	//先获取index.m3u8文件，之后获取每个.ts切片
	protected void getFile(HttpServletResponse response, String fileId, String userId) {
		String filePath=null ;
		if (fileId.endsWith(".ts")) {
			String[] tsArray = fileId.split("_");
			String realFileId = tsArray[0];
			FileInfo fileInfo = fileInfoService.getByFileIdAndUserId(realFileId, userId);
			if (null == fileInfo) {
				return;
			}
			String fileName = fileInfo.getFilePath();
			fileName = StringTools.getFileNoSuffix(fileName) + "/" + fileId;
			filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + fileName;
		} else {
			FileInfo fileInfo = fileInfoService.getByFileIdAndUserId(fileId, userId);
			if (null == fileInfo) {
				return;
			}
			if (FileCategoryEnums.VIDEO.getCategory().equals(fileInfo.getFileCategory())) {
				String fileNameNoSuffix = StringTools.getFileNoSuffix(fileInfo.getFilePath());
				filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + fileNameNoSuffix + "/"
						+ Constants.M3U8_NAME;
			} else {
				filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + fileInfo.getFilePath();
			}
			File file = new File(filePath);
			if (!file.exists()) {
				return;
			}
		}
		readFile(response, filePath);
	}

	protected ResponseVO getFolderInfo(String path, String userId) {
		String[] pathArray = path.split("/");
		FileInfoQuery infoQuery=new FileInfoQuery();
		infoQuery.setFolderType(FileFolderTypeEnums.FOLDER.getType());
		infoQuery.setUserId(userId);
		infoQuery.setFileIdArray(pathArray);
		//根据总path的传入顺序排序表示各级关系
		String orderBy="field(file_id,\""+StringUtils.join(pathArray,"\",\"")+"\")";
		infoQuery.setOrderBy(orderBy);
		List<FileInfo> fileInfoList=fileInfoService.findListByParam(infoQuery);
		return getSuccessResponseVO(CopyTools.copyList(fileInfoList, FolderVO.class));
	}
	protected ResponseVO createDownloadUrl(String fileId,String userId) {
		FileInfo fileInfo=fileInfoService.getByFileIdAndUserId(fileId, userId);
		if (null==fileInfo) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (FileFolderTypeEnums.FOLDER.getType().equals(fileInfo.getFolderType())) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//key:code value:fileId
		String code=StringTools.getRandomString(Constants.LENGTH_50);
		DownloadFileDto fileDto=new DownloadFileDto();
		fileDto.setDownloadCode(code);
		fileDto.setFilePath(fileInfo.getFilePath());
		fileDto.setFileName(fileInfo.getFileName());
		redisComponent.saveDownloadCode(code, fileDto);
		return getSuccessResponseVO(code);
	}
	
	protected void download(HttpServletRequest request,HttpServletResponse response,String code) throws Exception {
		DownloadFileDto downloadFileDto=redisComponent.getDownloadCode(code);
		if (null==downloadFileDto) {
			return;
		}
		String filePath=appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+downloadFileDto;
		String fileName=downloadFileDto.getFileName();
		response.setContentType("application/x-msdownload; charset=UTF-8");
		if (request.getHeader("User-Agent").toLowerCase().indexOf("msie")>0) {//IE浏览器
			fileName=URLEncoder.encode(fileName,"UTF-8");
		}else {
			fileName=new String(fileName.getBytes("UTF-8"),"ISO8859-1");
		}
		response.setHeader("Content-Disposition", "attachment;filename=\""+fileName+"\"");
		readFile(response, filePath);
	}
}
