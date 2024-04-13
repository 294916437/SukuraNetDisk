package easypan.aspect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import easypan.annotation.GlobalInterceptor;
import easypan.annotation.VerifyParam;
import easypan.entity.Constants.Constants;
import easypan.entity.dto.SessionWebUserDto;
import easypan.entity.enums.ResponseCodeEnum;
import easypan.exception.BusinessException;
import easypan.service.impl.EmailCodeServiceImpl;
import easypan.utils.StringTools;
import easypan.utils.VerifyUtils;

@Aspect
@Component("globalOperationAspect")
public class GlobalOperationAspect {
	private static final Logger logger = LoggerFactory.getLogger(GlobalOperationAspect.class);
	private static final String TYPE_STRING="java.lang.String";
	private static final String TYPE_INTEGER="java.lang.Integer";
	private static final String TYPE_LONG="java.lang.Long";
	@Pointcut("@annotation(easypan.annotation.GlobalInterceptor)")
	private void requestInterceptor() {

	}

	@Before("requestInterceptor()")
	public void InterceptorDo(JoinPoint point)throws	BusinessException {
			try {
				Object target=point.getTarget();
				Object[] arguments=point.getArgs();
				String methodName=point.getSignature().getName();
				Class<?>[]paramterTypes=((MethodSignature)point.getSignature()).getMethod().getParameterTypes();
				Method method=target.getClass().getMethod(methodName, paramterTypes);
				GlobalInterceptor interceptor=method.getAnnotation(GlobalInterceptor.class);
				if(null==interceptor) {
					return;
				}
				if (interceptor.checkLogin()||interceptor.checkAdmin()) {
					checkLogin(interceptor.checkAdmin());
				}
				if(interceptor.checkParams()) {
					validateParams(method,arguments);
				}
			} catch (BusinessException e) {
				logger.error("全局拦截器异常",e);
				throw new BusinessException(ResponseCodeEnum.CODE_500); 
			}catch (Exception e) {
				logger.error("全局拦截器异常",e);
				throw new BusinessException(ResponseCodeEnum.CODE_500);
			}
	}
	private void checkLogin(boolean checkAdmin) {
		HttpServletRequest request=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session=request.getSession();
		SessionWebUserDto useDto=(SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
		if (null==useDto) {
			throw new BusinessException(ResponseCodeEnum.CODE_901);
		}
		if (checkAdmin&&!useDto.getIsAdmin()) {
			throw new BusinessException(ResponseCodeEnum.CODE_404);
		}
	}
	private void validateParams(Method m, Object[] arguments) throws BusinessException{
		Parameter[] paramters=m.getParameters();
		for (int i = 0; i < paramters.length; i++) {
			Parameter parameter=paramters[i];
			Object value=arguments[i];
			VerifyParam verifyParam=parameter	.getAnnotation(VerifyParam.class);
			if(verifyParam==null) {
				continue;
			}
			// 基本数据类型
			if(TYPE_STRING.equals(parameter.getParameterizedType().getTypeName())||TYPE_INTEGER.equals(parameter.getParameterizedType().getTypeName())||TYPE_LONG.equals(parameter.getParameterizedType().getTypeName())) {
				checkValue(value,verifyParam);
			}else {
				checkObjValue(parameter,value);
			}
		}
		
		
	}

	private void checkObjValue(Parameter parameter, Object value) throws BusinessException {
		try {
			String typeName=parameter.getParameterizedType().getTypeName();
			Class classz=Class.forName(typeName);
			Field[] fields=classz.getDeclaredFields();
			for (Field field : fields) {
				VerifyParam fieldVerifyParam=field.getAnnotation(VerifyParam.class);
				if (fieldVerifyParam==null) {
					continue;
				}
				field.setAccessible(true);
				Object resultValue=field	.get(value);
				checkValue(resultValue, fieldVerifyParam);
			}
		} catch (BusinessException e) {
			logger.error("参数校验失败",e);
			throw e;
		}catch (Exception e) {
			logger.error("参数校验失败",e);
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		
	}

	private void checkValue(Object value, VerifyParam verifyParam) throws BusinessException{
		Boolean isEmpty=value==null||StringTools.isEmpty(value.toString());
		Integer length=value==null? 0:value.toString().length();
		//校验是否为空
		if (isEmpty&&verifyParam.required()) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//校验长度
		if (!isEmpty&&(verifyParam.max()!=-1&&verifyParam.max()<length||verifyParam.min()!=-1&&verifyParam.min()>length)) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//校验正则
		if (!isEmpty&&!StringTools.isEmpty(verifyParam.regex().getRegex())&&!VerifyUtils.verify(verifyParam.regex(), String.valueOf(value))) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
	}
}
