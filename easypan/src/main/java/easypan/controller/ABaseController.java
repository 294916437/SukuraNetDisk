package easypan.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import easypan.entity.Constants.Constants;
import easypan.entity.dto.SessionWebUserDto;
import easypan.entity.enums.ResponseCodeEnum;
import easypan.entity.vo.PaginationResultVO;
import easypan.entity.vo.ResponseVO;
import easypan.exception.BusinessException;
import easypan.utils.CopyTools;
import easypan.utils.StringTools;

public class ABaseController {
	private static Logger logger = LoggerFactory.getLogger(ABaseController.class);
	protected static final String STATUS_SUCCESS = "success";
	protected static final String STATUS_ERROR = "error";
	protected static final String CONTENT_TYPE = "Content-Type";
	protected static final String CONTENT_TYPE_VALUE = "application/json;charset=utf-8";

	protected <T> ResponseVO getSuccessResponseVO(T t) {
		ResponseVO<T> responseVO = new ResponseVO<>();
		responseVO.setStatus(STATUS_SUCCESS);
		responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
		responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
		responseVO.setData(t);
		return responseVO;
	}

	protected <T> ResponseVO getBusinessErrorResponseVO(BusinessException e, T t) {
		ResponseVO vo = new ResponseVO();
		vo.setStatus(STATUS_ERROR);
		if (e.getCode() == null) {
			vo.setCode(ResponseCodeEnum.CODE_600.getCode());
		} else {
			vo.setCode(e.getCode());
		}
		vo.setInfo(e.getMessage());
		vo.setData(t);
		return vo;
	}

	protected <T> ResponseVO getServerErrorResponseVO(T t) {
		ResponseVO vo = new ResponseVO();
		vo.setStatus(STATUS_ERROR);
		vo.setCode(ResponseCodeEnum.CODE_500.getCode());
		vo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
		vo.setData(t);
		return vo;
	}
	protected <S,T> PaginationResultVO<T> convertPaginationVO(PaginationResultVO<S>result,Class<T>classz) {
		PaginationResultVO<T>resultVO=new PaginationResultVO<>();
		resultVO.setList(CopyTools.copyList(result.getList(), classz));
		resultVO.setPageNo(result.getPageNo());
		resultVO.setPageSize(result.getPageSize());
		resultVO.setPageTotal(result.getPageTotal());
		resultVO.setTotalCount(result.getTotalCount());
		return resultVO;
	}
	protected void readFile(HttpServletResponse response, String filePath) {
		if (!StringTools.pathIsOK(filePath)) {
			return;
		}
		OutputStream out = null;
		FileInputStream in = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				return;
			}
			in = new FileInputStream(file);
			byte[] byteData = new byte[1024];
			out = response.getOutputStream();
			int len = 0;
			while ((len = in.read(byteData)) != -1) {
				out.write(byteData, 0, len);
			}
			out.flush();
		} catch (Exception e) {
			logger.error("读取文件异常", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("IO异常", e);
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("IO异常", e);
				}
			}
		}
	}

	protected SessionWebUserDto getUserInfoFromSession(HttpSession session) {
		SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
		return sessionWebUserDto;
	}
}
