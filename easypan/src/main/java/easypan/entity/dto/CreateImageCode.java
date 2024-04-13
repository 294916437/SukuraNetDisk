package easypan.entity.dto;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class CreateImageCode {
	private int width = 160;
	private int height = 40;
	private int codeCount = 4;
	private int lineCount = 20;
	private String code = null;
	private BufferedImage buffImg = null;
	Random random = new Random();

	public CreateImageCode() {
		createImage();
	}

	public  CreateImageCode(int width, int height) {
		this.width = width;
		this.height = height;
		createImage();
	}

	public  CreateImageCode(int width, int height, int codeCount) {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		createImage();
	}

	public  CreateImageCode(int width, int height, int codeCount, int lineCount) {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		this.lineCount = lineCount;
		createImage();
	}

	// 生成图片
	private void createImage() {
		int fontWidth = width / codeCount;//字体宽度
		int fontHeight = height - 5;//字体高度
		int codeY = height - 8;
		// 图像buffer
		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = buffImg.getGraphics();
		// 设置背景色
		g.setColor(getRandomColor(200, 250));
		g.fillRect(0, 0, width, height);
		// 设置字体
		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		g.setFont(font);
		// 设置干扰线
		for (int i = 0; i < lineCount; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width);
			int ye = ys + random.nextInt(height);
			g.setColor(getRandomColor(1, 255));
			g.drawLine(xs, ys, xe, ye);
		}
		// 添加噪点
		float yawpRate = 0.01f;// 噪声率
		int area = (int) (yawpRate * width * height);
		for (int j = 0; j < area; j++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			buffImg.setRGB(x, y, random.nextInt(255));
		}
		String str1 = randomStr(codeCount);
		this.code = str1;
		for (int k = 0; k < codeCount; k++) {
			String strRand = str1.substring(k, k + 1);
			g.setColor(getRandomColor(1, 255));
			g.drawString(strRand, k * fontWidth + 3, codeY);

		}

	}

	// 给定范围生成随机颜色
	private Color getRandomColor(int i, int j) {
		if (i > 255)
			i = 255;
		if (j > 255)
			j = 255;
		int r = i + random.nextInt(j - i);
		int g = i + random.nextInt(j - i);
		int b = i + random.nextInt(j - i);
		return new Color(r, g, b);
	}

	// 生成随机字符
	private String randomStr(int n) {
		String str1 = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz123456789";
		String str2 = "";
		int len = str1.length() - 1;
		double r;
		for (int i = 0; i < n; i++) {
			r = (Math.random()) * len;
			str2 = str2 + str1.charAt((int) r);
		}
		return str2;
	}
	public void write(OutputStream sos) throws IOException {
		ImageIO.write(buffImg, "png", sos);
		sos.close();
	}
	public BufferedImage getBuffImg() {
		return buffImg;
	}
	public String getCode() {
		return code.toLowerCase();
	}

}
