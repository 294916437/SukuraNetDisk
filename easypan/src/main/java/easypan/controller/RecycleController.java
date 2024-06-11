package easypan.controller;

import easypan.annotation.GlobalInterceptor;
import easypan.annotation.VerifyParam;
import easypan.entity.dto.SessionWebUserDto;
import easypan.entity.enums.FileCategoryEnums;
import easypan.entity.enums.FileDelFlagEnums;
import easypan.entity.query.FileInfoQuery;
import easypan.entity.vo.FileInfoVO;
import easypan.entity.vo.PaginationResultVO;
import easypan.entity.vo.ResponseVO;
import easypan.service.FileInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


@RestController("recycleController")
@RequestMapping("/recycle")
public class RecycleController extends ABaseController{
    @Resource
    private FileInfoService fileInfoService;
    @RequestMapping("/loadRecycleList")
    @GlobalInterceptor
    public ResponseVO loadRecycleList(HttpSession session, Integer pageNo,Integer pageSize) {
        FileInfoQuery query=new FileInfoQuery();
        query.setPageNo(pageNo);
        query.setPageSize(pageSize);
        query.setUserId(getUserInfoFromSession(session).getUserId());
        query.setOrderBy("recovery_time desc");
        query.setDelFlag(FileDelFlagEnums.RECYCLE.getFlag());
        PaginationResultVO result = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convertPaginationVO(result, FileInfoVO.class));
    }

    @RequestMapping("/recoverFile")
    @GlobalInterceptor
    public ResponseVO recoverFile(HttpSession session, @VerifyParam(required = true)String fileIds) {
        SessionWebUserDto webUserDto=getUserInfoFromSession(session);
        fileInfoService.recoverFileBatch(webUserDto.getUserId(),fileIds);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/delFile")
    @GlobalInterceptor
    public ResponseVO delFile(HttpSession session, @VerifyParam(required = true)String fileIds) {
        SessionWebUserDto webUserDto=getUserInfoFromSession(session);
        fileInfoService.delFileBatch(webUserDto.getUserId(),fileIds,false);
        return getSuccessResponseVO(null);
    }

}
