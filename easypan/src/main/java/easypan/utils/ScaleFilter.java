package easypan.utils;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScaleFilter {
	private static Logger logger = LoggerFactory.getLogger(ScaleFilter.class);

	public static void createCoverVideo(File sourceFile, Integer width, File targetFile) {
		try {
			String cmd = "ffmpeg -i %s -ss 5 -vframes 1 -vf scale=%d:%d/a %s";
			ProcessUtils.executeCommand(String.format(cmd, sourceFile.getAbsoluteFile(), width, width, targetFile.getAbsoluteFile()), false);
		} catch (Exception e) {
			logger.error("生成视频缩略图失败");
		}
	}

	public static boolean createThumbnailWidthFFmpeg(File file, int thumbnailWidth, File targetFile, boolean delSource) {
		try {
			BufferedImage src = ImageIO.read(file);
			int sorceW = src.getWidth();
			int sorceH = src.getHeight();
			//小于指定高度不压缩
			if (sorceW <= thumbnailWidth) {
				return false;
			}
			compressImage(file, thumbnailWidth, targetFile, delSource);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void compressImage(File sourceFile,Integer width,File targetFile,Boolean delSource) {
		try {
			String cmd="ffmpeg -i %s -vf scale=%d:-1 %s";
			ProcessUtils.executeCommand(String.format(cmd, sourceFile.getAbsoluteFile(),width,targetFile.getAbsoluteFile()), false);
			if (delSource){
				FileUtils.forceDelete(sourceFile);
			}
		} catch (Exception e) {
			logger.error("压缩图片失败");
		}
	}
}
