package com.common.tools;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.common.app.MyApplication;
import com.common.app.StaticMethod;

import java.util.Random;

public class AuthCode {
	
	private static final char[] CHARS = {
		'2', '3', '4', '5', '6', '7', '8', '9', '0', '1'
	};
	
	private static AuthCode bmpCode;
	
	public static AuthCode getInstance() {
		if(bmpCode == null)
			bmpCode = new AuthCode();
		return bmpCode;
	}
	
	//default settings
	private static final int DEFAULT_CODE_LENGTH = 6;
	private static final int DEFAULT_FONT_SIZE = StaticMethod.dip2px(MyApplication.getInstance(),12);
	private static final int DEFAULT_LINE_NUMBER = 2;
	private static final int DEFAULT_WIDTH = StaticMethod.dip2px(MyApplication.getInstance(),100);
	private static final int DEFAULT_HEIGHT =  StaticMethod.dip2px(MyApplication.getInstance(),40);
	private static final int BASE_PADDING_LEFT = StaticMethod.dip2px(MyApplication.getInstance(),10),
							 RANGE_PADDING_LEFT = StaticMethod.dip2px(MyApplication.getInstance(),10),
							BASE_PADDING_TOP =DEFAULT_HEIGHT/2, RANGE_PADDING_TOP = StaticMethod.dip2px(MyApplication.getInstance(),5);
	//settings decided by the layout xml
	//canvas width and height
	private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;

	/**每个字最多width*/
	private int perwidth = DEFAULT_WIDTH/DEFAULT_CODE_LENGTH;
	//random word space and pading_top
	private int base_padding_left = BASE_PADDING_LEFT,
				range_padding_left = RANGE_PADDING_LEFT,
				base_padding_top = BASE_PADDING_TOP,
				range_padding_top = RANGE_PADDING_TOP;

	//number of chars, lines; font size
	private int codeLength = DEFAULT_CODE_LENGTH,
				line_number = DEFAULT_LINE_NUMBER,
				font_size = DEFAULT_FONT_SIZE;
	
	//variables
	private String code;
	private int padding_left, padding_top;
	private Random random = new Random();
	//验证码图片
	public Bitmap createBitmap() {
		return createBitmap(createCode());
	}

	public Bitmap createBitmap(String code) {
		this.code =code;
		padding_left = 0;
		Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas c = new Canvas(bp);

		c.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setTextSize(font_size);

		for (int i = 0; i < code.length(); i++) {
			randomTextStyle(paint);
			randomPadding();
			int size= (int) paint.measureText(code.charAt(i)+"");
			if (padding_left+size>(i+1)*perwidth)
				padding_left=padding_left-((padding_left+size)-(i+1)*perwidth);
//			L.MyLog("验证码：","padding_left:"+padding_left+"---width:"+width);
			c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
		}

		for (int i = 0; i < line_number; i++) {
			drawLine(c, paint);
		}

		c.save( Canvas.ALL_SAVE_FLAG );//保存
		c.restore();//
		return bp;
	}
	
	public String getCode() {
		return code;
	}
	
	//验证码
	public String createCode() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < codeLength; i++) {
			buffer.append(CHARS[random.nextInt(CHARS.length)]);
		}
		return buffer.toString();
	}
	
	private void drawLine(Canvas canvas, Paint paint) {
		int color = randomColor();
		int startX = random.nextInt(width);
		int startY = random.nextInt(height);
		int stopX = random.nextInt(width);
		int stopY = random.nextInt(height);
		paint.setStrokeWidth(1);
		paint.setColor(color);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}
	
	private int randomColor() {
		return randomColor(1);
	}

	private int randomColor(int rate) {
		int red = random.nextInt(256) / rate;
		int green = random.nextInt(256) / rate;
		int blue = random.nextInt(256) / rate;
		return Color.rgb(red, green, blue);
	}
	
	private void randomTextStyle(Paint paint) {
		int color = randomColor();
		paint.setColor(color);
		paint.setFakeBoldText(random.nextBoolean());  //true为粗体，false为非粗体
		float skewX = random.nextInt(11) / 10;
		skewX = random.nextBoolean() ? skewX : -skewX;
		paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
//		paint.setUnderlineText(true); //true为下划线，false为非下划线
//		paint.setStrikeThruText(true); //true为删除线，false为非删除线
	}
	
	private void randomPadding() {
		padding_left += base_padding_left + random.nextInt(range_padding_left);
		padding_top = base_padding_top + random.nextInt(range_padding_top);


	}
}

