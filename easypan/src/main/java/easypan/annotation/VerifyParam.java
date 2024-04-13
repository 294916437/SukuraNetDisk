package easypan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import easypan.entity.dto.VerifyRegexEnum;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
public @interface VerifyParam {
	int min() default -1;
	int max() default -1;
	boolean required() default false;
	VerifyRegexEnum regex() default VerifyRegexEnum.NO;
}
