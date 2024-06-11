package easypan.controller;

import easypan.annotation.GlobalInterceptor;
import easypan.annotation.VerifyParam;
import easypan.component.RedisComponent;
import easypan.entity.dto.SysSettingDto;
import easypan.entity.query.FileInfoQuery;
import easypan.entity.query.UserInfoQuery;
import easypan.entity.vo.*;
import easypan.service.FileInfoService;
import easypan.service.UserInfoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@RestController("adminController")
@RequestMapping("admin")
public class AdminController extends CommonFileController{
    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private UserInfoService userInfoService;
    @RequestMapping("/getSysSettings")
    @GlobalInterceptor(checkAdmin = true,checkParams = true)
    public ResponseVO getSysSettings() {
        return getSuccessResponseVO(redisComponent.getSysSettingDto());
    }
    @RequestMapping("/saveSysSettings")
    @GlobalInterceptor(checkAdmin = true,checkParams = true)
    public ResponseVO saveSysSettings(@VerifyParam(required = true)String registerEmailTitle,
                                      @VerifyParam(required = true)String registerEmailContent,
                                      @VerifyParam(required = true)Integer userInitUseSpace) {
        SysSettingDto sysSettingDto=new SysSettingDto();
        sysSettingDto.setRegisterEmailContent(registerEmailContent);
        sysSettingDto.setRegisterEmailTitle(registerEmailTitle);
        sysSettingDto.setUseInitUseSpace(userInitUseSpace);
        redisComponent.saveSysSettingDto(sysSettingDto);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/loadUserList")
    @GlobalInterceptor(checkAdmin = true,checkParams = true)
    public ResponseVO loadUserList(UserInfoQuery query) {
        query.setOrderBy("join_time desc");
        PaginationResultVO resultVO=userInfoService.findListByPage(query);
        return getSuccessResponseVO(convertPaginationVO(resultVO, UserInfoVO.class));
    }
    @RequestMapping("/updateUserStatus")
    @GlobalInterceptor(checkAdmin = true,checkParams = true)
    public ResponseVO updateUserStatus(@VerifyParam(required = true)String userId,
                                       @VerifyParam(required = true)Integer status) {
        userInfoService.updateUserStatus(userId,status);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/updateUserSpace")
    @GlobalInterceptor(checkAdmin = true,checkParams = true)
    public ResponseVO updateUserSpace(@VerifyParam(required = true)String userId,
                                      @VerifyParam(required = true)Long changeSpace) {
        userInfoService.changeUserSpace(userId,changeSpace);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/loadFileList")
    @GlobalInterceptor(checkAdmin = true,checkParams = true)
    public ResponseVO loadFileList(FileInfoQuery query) {
        query.setOrderBy("last_update_time desc");
        query.setQueryNickName(true);
        PaginationResultVO result = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convertPaginationVO(result, FileInfoAdminVO.class));
    }
    @RequestMapping("/getFolderInfo")
    @GlobalInterceptor(checkAdmin = true,checkParams = true)
    public ResponseVO getFolderInfo(@VerifyParam(required = true)String path) {
        return super.getFolderInfo(path,null);
    }
    @RequestMapping("/getFile/{userId}/{fileId}")
    @GlobalInterceptor(checkAdmin = true,checkParams = true)
    public void getFile(HttpServletResponse response,
                        @PathVariable("userId") String userId,
                        @PathVariable("fileId") String fileId) {
        super.getFile(response, fileId, userId);
    }
    @RequestMapping("/ts/getVideoInfo/{userId}/{fileId}")
    @GlobalInterceptor(checkAdmin = true,checkParams = true)
    public void getImage(HttpServletResponse response,
                         @PathVariable("userId") String userId,
                         @PathVariable("fileId") String fileId) {
        super.getFile(response, fileId, userId);
    }
    @RequestMapping("/createDownloadUrl/{userId}/{fileId}")
    @GlobalInterceptor(checkAdmin = true,checkParams = true)
    public ResponseVO createDownloadUrl(
                                        @PathVariable("fileId") String fileId,
                                        @PathVariable("userId") String userId) {
        return super.createDownloadUrl(fileId, userId);
    }
    @RequestMapping("/download/{code}")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @VerifyParam(required = true) @PathVariable("code") String code) throws Exception {
        super.download(request, response, code);
    }
    @RequestMapping("/delFile")
    @GlobalInterceptor(checkParams = true,checkAdmin = true)
    public ResponseVO delFile(HttpSession session,@VerifyParam (required = true) String fileIdsAndUserIds) {
        String[] fileIdAndUserIdArray = fileIdsAndUserIds.split(",");
        for (String fileIdAndUserId : fileIdAndUserIdArray) {
            String[] itemArray = fileIdAndUserId.split("_");
            fileInfoService.delFileBatch(itemArray[0], itemArray[1],true);
        }
        return getSuccessResponseVO(null);
    }
}
