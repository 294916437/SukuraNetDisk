package easypan.controller;

import easypan.annotation.GlobalInterceptor;
import easypan.annotation.VerifyParam;
import easypan.entity.Constants.Constants;
import easypan.entity.dto.SessionShareDto;
import easypan.entity.dto.SessionWebUserDto;
import easypan.entity.enums.FileCategoryEnums;
import easypan.entity.enums.FileDelFlagEnums;
import easypan.entity.enums.ResponseCodeEnum;
import easypan.entity.po.FileInfo;
import easypan.entity.po.FileShare;
import easypan.entity.po.UserInfo;
import easypan.entity.query.FileInfoQuery;
import easypan.entity.vo.FileInfoVO;
import easypan.entity.vo.PaginationResultVO;
import easypan.entity.vo.ResponseVO;
import easypan.entity.vo.ShareInfoVO;
import easypan.exception.BusinessException;
import easypan.service.FileInfoService;
import easypan.service.FileShareService;
import easypan.service.UserInfoService;
import easypan.utils.CopyTools;
import easypan.utils.StringTools;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController("WebShareController")
@RequestMapping("/sharelink")
public class WebShareController extends CommonFileController {
    @Resource
    private FileShareService fileShareService;
    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/getShareLoginInfo")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO getShareLoginInfo(HttpSession session, @VerifyParam(required = true) String shareId) {
        SessionShareDto sessionShareDto = getSessionShareFromSession(session, shareId);
        if (sessionShareDto == null) {
            return getSuccessResponseVO(null);
        }
        ShareInfoVO shareInfoVO = getShareInfoCommon(shareId);
        //判断是否是当前用户
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if (userDto != null && userDto.getUserId().equals(sessionShareDto.getShareUserId())) {
            shareInfoVO.setCurrentUser(true);
        } else {
            shareInfoVO.setCurrentUser(false);
        }
        return getSuccessResponseVO(shareInfoVO);
    }

    @RequestMapping("/getShareInfo")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO getShareInfo(@VerifyParam(required = true) String shareId) {
        return getSuccessResponseVO(getShareInfoCommon(shareId));
    }

    private ShareInfoVO getShareInfoCommon(String shareId) {
        FileShare fileShare = fileShareService.getByShareId(shareId);
        if (null == fileShare || (fileShare.getExpireTime() != null && fileShare.getExpireTime().getTime() < System.currentTimeMillis())) {
            throw new BusinessException(ResponseCodeEnum.CODE_902.getMsg());
        }
        ShareInfoVO shareInfoVO = CopyTools.copy(fileShare, ShareInfoVO.class);
        FileInfo fileInfo = fileInfoService.getByFileIdAndUserId(fileShare.getFileId(), fileShare.getUserId());
        if (null == fileInfo || FileDelFlagEnums.USING.getFlag().equals(fileInfo.getDelFlag())) {
            throw new BusinessException(ResponseCodeEnum.CODE_902.getMsg());
        }
        shareInfoVO.setFileName(fileInfo.getFileName());
        UserInfo userInfo = userInfoService.getByUserId(fileShare.getUserId());
        shareInfoVO.setNickName(userInfo.getNickName());
        shareInfoVO.setUserId(userInfo.getUserId());
        return shareInfoVO;
    }

    @RequestMapping("/checkShareCode")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO checkShareCode(HttpSession session,
                                     @VerifyParam(required = true) String shareId,
                                     @VerifyParam(required = true) String code) {
        SessionShareDto sessionShareDto = fileShareService.checkShareCode(shareId, code);
        session.setAttribute(Constants.SESSION_SHARE_KEY + shareId, sessionShareDto);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadFileList")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO loadFileList(HttpSession session,
                                   @VerifyParam(required = true) String shareId,
                                   @VerifyParam(required = true) String filePid) {
        SessionShareDto sessionShareDto = checkShare(session, shareId);
        FileInfoQuery query = new FileInfoQuery();
        query.setFilePid(filePid);
        if (!StringTools.isEmpty(filePid) && Constants.ZERO_STR.equals(filePid)) {
            fileInfoService.checkRootFilePid(sessionShareDto.getFileId(), sessionShareDto.getShareUserId(), filePid);
        } else {
            query.setFileId(sessionShareDto.getFileId());
        }
        query.setUserId(sessionShareDto.getShareUserId());
        query.setOrderBy("last_update_time desc");
        query.setDelFlag(FileDelFlagEnums.USING.getFlag());
        PaginationResultVO result = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convertPaginationVO(result, FileInfoVO.class));
    }

    private SessionShareDto checkShare(HttpSession session, String shareId) {
        SessionShareDto sessionShareDto = getSessionShareFromSession(session, shareId);
        if (sessionShareDto == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_903);
        }
        if (sessionShareDto.getExpireTime() != null && sessionShareDto.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new BusinessException(ResponseCodeEnum.CODE_902);
        }
        return sessionShareDto;
    }
}
