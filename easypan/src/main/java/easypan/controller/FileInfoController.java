package easypan.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import easypan.entity.vo.FileInfoVO;
import easypan.entity.vo.PaginationResultVO;
import easypan.entity.vo.ResponseVO;
import easypan.annotation.GlobalInterceptor;
import easypan.annotation.VerifyParam;
import easypan.entity.dto.SessionWebUserDto;
import easypan.entity.dto.UploadResultDto;
import easypan.entity.enums.FileCategoryEnums;
import easypan.entity.enums.FileDelFlagEnums;
import easypan.entity.enums.FileFolderTypeEnums;
import easypan.entity.po.FileInfo;
import easypan.entity.query.FileInfoQuery;
import easypan.service.FileInfoService;
import easypan.utils.CopyTools;
import easypan.utils.StringTools;

/**
 * @Description:Service
 * @Author: 春的樱树
 * @Date: 2024-03-16
 */
@RestController("FileInfoController")
@RequestMapping("file")
public class FileInfoController extends CommonFileController {

    @Resource
    private FileInfoService fileInfoService;


    @RequestMapping("/loadDataList")
    @GlobalInterceptor
    public ResponseVO loadDataList(HttpSession session, FileInfoQuery query, String category) {
        FileCategoryEnums categoryEnums = FileCategoryEnums.getByCode(category);
        if (null != categoryEnums) {
            query.setFileCategory(categoryEnums.getCategory());
        }
        query.setUserId(getUserInfoFromSession(session).getUserId());
        query.setOrderBy("last_update_time desc");
        query.setDelFlag(FileDelFlagEnums.USING.getFlag());
        PaginationResultVO result = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convertPaginationVO(result, FileInfoVO.class));
    }

    @RequestMapping("/uploadFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO uploadFile(HttpSession session, String fileId, MultipartFile file,
                                 @VerifyParam(required = true) String fileName, @VerifyParam(required = true) String filePid,
                                 @VerifyParam(required = true) String fileMd5, @VerifyParam(required = true) Integer chunkIndex,
                                 @VerifyParam(required = true) Integer chunks) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        UploadResultDto resultDto = fileInfoService.uploadFile(webUserDto, fileId, file, fileName, filePid, fileMd5,
                chunkIndex, chunks);
        return getSuccessResponseVO(resultDto);
    }

    @RequestMapping("/getImage/{imageFolder}/{imageName}")
    @GlobalInterceptor(checkParams = true)
    public void getImage(HttpServletResponse response,
                         @PathVariable("imageFolder") String imageFolder,
                         @PathVariable("imageName") String imageName) {
        super.getImage(response, imageFolder, imageName);
    }

    @RequestMapping("/ts/getVideoInfo/{fileId}")
    @GlobalInterceptor(checkParams = true)
    public void getImage(HttpServletResponse response, HttpSession session, @PathVariable("fileId") String fileId) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        super.getFile(response, fileId, webUserDto.getUserId());
    }

    @RequestMapping("/getFile/{fileId}")
    @GlobalInterceptor(checkParams = true)
    public void getFile(HttpServletResponse response, HttpSession session, @PathVariable("fileId") String fileId) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        super.getFile(response, fileId, webUserDto.getUserId());
    }

    @RequestMapping("/newFolder")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO newFolder(HttpServletResponse response, HttpSession session,
                                @VerifyParam(required = true) String filePid, @VerifyParam(required = true) String fileName) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        FileInfo fileInfo = fileInfoService.newFolder(filePid, webUserDto.getUserId(), fileName);

        return getSuccessResponseVO(CopyTools.copy(fileInfo, FileInfoVO.class));
    }

    @RequestMapping("/getFolderInfo")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO getFolderInfo(HttpSession session, @VerifyParam(required = true) String path) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        return super.getFolderInfo(path, webUserDto.getUserId());
    }

    @RequestMapping("/rename")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO rename(HttpSession session, @VerifyParam(required = true) String fileId,
                             @VerifyParam(required = true) String fileName) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        FileInfo fileInfo = fileInfoService.rename(fileId, webUserDto.getUserId(), fileName);
        return getSuccessResponseVO(CopyTools.copy(fileInfo, FileInfoVO.class));
    }

    @RequestMapping("/loadAllFolder")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadAllFolder(HttpSession session, @VerifyParam(required = true) String filePid,
                                    String currentFileIds) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        FileInfoQuery infoQuery = new FileInfoQuery();
        infoQuery.setUserId(webUserDto.getUserId());
        infoQuery.setFilePid(filePid);
        infoQuery.setFolderType(FileFolderTypeEnums.FOLDER.getType());
        if (!StringTools.isEmpty(currentFileIds)) {
            infoQuery.setExcludeFileIdArray(currentFileIds.split(","));
        }
        infoQuery.setDelFlag(FileDelFlagEnums.USING.getFlag());
        infoQuery.setOrderBy("create_time desc");
        List<FileInfo> fileInfoList = this.fileInfoService.findListByParam(infoQuery);
        return getSuccessResponseVO(CopyTools.copyList(fileInfoList, FileInfoVO.class));
    }

    @RequestMapping("/changeFileFolder")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO changeFileFolder(HttpSession session,
                                       @VerifyParam(required = true) String fileIds,
                                       @VerifyParam(required = true) String filePid) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.changeFileFolder(fileIds, filePid, webUserDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/createDownloadUrl/{fileId}")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO createDownloadUrl(HttpSession session,
                                        @VerifyParam(required = true) @PathVariable("fileId") String fileId) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        return super.createDownloadUrl(fileId, webUserDto.getUserId());
    }

    @RequestMapping("/download/{code}")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @VerifyParam(required = true) @PathVariable("code") String code) throws Exception {
        super.download(request, response, code);
    }

    @RequestMapping("/delFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO delFile(HttpSession session,
                              @VerifyParam(required = true) String fileIds) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.removeFile2RecycleBatch(webUserDto.getUserId(), fileIds);
        return getSuccessResponseVO(null);
    }

}