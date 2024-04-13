package easypan.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import easypan.exception.BusinessException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ProcessUtils {
	private static Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

	public static String executeCommand(String cmd, Boolean outprintLog) {
		if (StringTools.isEmpty(cmd)) {
			logger.error("--- 指令执行失败，因为ffmpeg指令为空 ---");
			return null;
		}
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
			PrintStream errorStream = new PrintStream(process.getErrorStream());
			PrintStream inputStream = new PrintStream(process.getInputStream());
			errorStream.start();
			inputStream.start();
			process.waitFor();
			String result = errorStream.stringBuilder.append(inputStream.stringBuilder + "\n").toString();
			if (outprintLog) {
				logger.info("执行命令;{},已执行结束，执行结果:{}", cmd, result);
			} else {
				logger.info("执行命令;{},已执行结束", cmd);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("视频转换失败");
		} finally {
			if (null != process) {
				Processkiller ffmpegKiller = new Processkiller(process);
				runtime.addShutdownHook(ffmpegKiller);
			}
		}
	}

	/**
	 * 在程序退出前结束已有的ffmpeg进程
	 */
	private static class Processkiller extends Thread {
		private Process process;

		public Processkiller(Process process) {
			this.process = process;
		}

		@Override
		public void run() {
			this.process.destroy();
		}
	}

	/**
	 * 用于取出ffmpeg线程执行过程中产生的各种输出和错误流的信息
	 */
	static class PrintStream extends Thread {
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = new StringBuilder();

		public PrintStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		@Override
		public void run() {
			try {
				if (null == inputStream) {
					return;
				}
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} catch (Exception e) {
				logger.error("读取输入流出错了！错误信息:" + e.getMessage());
			} finally {
				try {
					if (null != bufferedReader) {
						bufferedReader.close();
					}
					if (null != inputStream) {
						inputStream.close();
					}
				} catch (IOException e) {
					logger.error("关闭流错误");
				}
			}
		}
	}
}
