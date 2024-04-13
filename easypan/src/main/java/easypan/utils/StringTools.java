package easypan.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import easypan.entity.Constants.Constants;

public class StringTools {
	public static final String getRandomNumber(Integer count) {
		return RandomStringUtils.random(count,false,true);
	}
	public static final String getRandomString(Integer count) {
		return RandomStringUtils.random(count,true,true);
	}
	public static boolean isEmpty(String str) {
		if (str==null||"".equals(str)||"null".equals(str)) {
			return true;
		}else if("".equals(str.trim())) {
			return true;
		}
		return false;
	}
	public static String encodeByMd5(String originString) {
		return isEmpty(originString)?null:DigestUtils.md5Hex(originString);
	}
	public static boolean pathIsOK(String path) {
		if (StringTools.isEmpty(path)) {
			return true;
		}
		if (path.contains("../")||path.contains("..\\")) {
			return false;
		}
		return true;
	}
	public static String rename(String fileName) {
		String fileNameReal=getFileNoSuffix(fileName);
		String suffix=getFileSuffix(fileName);
		return fileNameReal+"_"+getRandomString(Constants.LENGTH_5)+suffix;
	}
	public static String getFileNoSuffix(String fileName) {
		Integer index=fileName.lastIndexOf(".");
		if (index==-1) {
			return fileName;
		}
		fileName=fileName.substring(0,index);
		return fileName;
	}
	public static String getFileSuffix(String fileName) {
		Integer index=fileName.lastIndexOf(".");
		if (index==-1) {
			return "";
		}
		String suffix=fileName.substring(index);
		return suffix;
	}
}
