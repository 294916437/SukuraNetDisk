package easypan.entity.dto;

public enum VerifyRegexEnum {
	NO("","不校验"),
	EMAIL("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+$)","邮箱"),
	PASSWORD("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&~_])[A-Za-z\\d@$!%*?&~_]{8,18}$","只能由数字、字母、特殊字符的8-18位组合");
	private String regex;
	private String desc;

	VerifyRegexEnum(String regex,String desc){
		this.regex=regex;
		this.desc=desc;
	}
	public String getRegex() {
		return regex;
	}
	public String getDesc() {
		return desc;
	}
}
