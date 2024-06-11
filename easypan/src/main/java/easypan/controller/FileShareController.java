package easypan.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import easypan.annotation.VerifyParam;
import easypan.entity.dto.SessionWebUserDto;
import easypan.entity.vo.PaginationResultVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import easypan.entity.vo.ResponseVO;
import easypan.entity.po.FileShare;
import easypan.entity.query.FileShareQuery;
import easypan.service.FileShareService;

/**
 * @Description:分享信息Service
 * @Author: 春的樱树
 * @Date: 2024-04-29
 */
@RestController("shareController")
@RequestMapping("share")
public class FileShareController extends ABaseController {

	@Resource
	private FileShareService fileShareService;

	@RequestMapping("/loadShareList")
	public ResponseVO loadShareList(HttpSession session,FileShareQuery query) {
		query.setOrderBy("share_time desc");
		SessionWebUserDto webUserDto=getUserInfoFromSession(session);
		query.setUserId(webUserDto.getUserId());
		query.setQueryFileName(true);
		PaginationResultVO result=fileShareService.findListByPage(query);
		return getSuccessResponseVO(result);
	}
	@RequestMapping("/shareFile")
	public ResponseVO shareFile(HttpSession session,
								@VerifyParam(required = true)String fileId,
								@VerifyParam(required = true)Integer validType,
								String code) {
		SessionWebUserDto webUserDto=getUserInfoFromSession(session);
		FileShare share=new FileShare();
		share.setValidType(validType);
		share.setCode(code);
		share.setFileId(fileId);
		share.setUserId(webUserDto.getUserId());
		fileShareService.saveShare(share);
		return getSuccessResponseVO(share);
	}
	@RequestMapping("/cancelShare")
	public ResponseVO cancelShare(HttpSession session,
								@VerifyParam(required = true)String shareIds) {
		SessionWebUserDto webUserDto=getUserInfoFromSession(session);
		fileShareService.deleteFileShareBatch(shareIds.split(","),webUserDto.getUserId());
		return getSuccessResponseVO(null);
	}

}