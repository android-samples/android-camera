package com.example.cameratest;

import java.io.IOException;
import java.io.InputStream;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore.Images;

public class ImageUtils {
	public static Bitmap decodeUri(Context context, Uri uri){
		if(uri == null)return null;
		try{
			// 元のビットマップ生成
			InputStream input = context.getContentResolver().openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			input.close();
			
			// 回転取得
			int r = 0;
			if(uri.getScheme().equals("content")){
				String[] projection = {Images.ImageColumns.ORIENTATION};
				Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
				if(c.moveToFirst()){
					r = c.getInt(0);
				}
			}
			else{
				try{
					ExifInterface exif = new ExifInterface(uri.getPath());
					int orientation = exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);
					if(orientation == ExifInterface.ORIENTATION_ROTATE_90){
						r = 90;
					}
					else if(orientation == ExifInterface.ORIENTATION_ROTATE_180){
						r = 180;
					}
					else if(orientation == ExifInterface.ORIENTATION_ROTATE_270){
						r = 270;
					}
					else{
						r = 0;
					}
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			
			// 回転
			Matrix matrix = new Matrix();
			matrix.postRotate(r, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			return bitmap;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
