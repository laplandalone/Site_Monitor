package com.site.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * 工具类
 * 
 */
public class ImageUtil {

	/**
	 * 压缩图片质量,返回图片名字
	 * 
	 * @param activity
	 * @param uri
	 * @return author xuchun
	 */
	public static String compressBitmap(Activity activity, String path) {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		if (file.length() / 1024 <= 200) {
			return file.getName();
		}
		Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
		if (bitmap == null) { // 若获取图片失败就取消压缩
			return file.getName();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// 压缩质量因数
		// 越低代表图片质量越差，但图片质量会减小
		// 为100时不减少图片质量
		// 经测试，从60开始比较不错
		int options = 60;
		// 这里的200只是一个参考数
		// 压缩后的图片质量在此值左右浮动，浮动值很小
		while (baos.toByteArray().length / 1024 > 200) {
			baos.reset();
			options -= 1;
			if (options == 1) {
				break;
			}
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		// 注意：
		// 这里千万别用BitmapFactory.decodeStream(is)去生成Bitmap后再去保存
		// 图片稍微大一点就会OOM
		try {
			// 压缩后的图片覆盖掉之前的图片
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(baos.toByteArray());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getName();
	}

}
