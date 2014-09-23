package com.example.cameratest;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView m_imageView;
	
	private final int MY_CAMERA = 1;
	private final int MY_ALBUM = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// イメージビュー取得しておく
		m_imageView = (ImageView)findViewById(R.id.imageView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Uri m_imageUri = null;
	
	// カメラ起動
	public void buttonMethodCamera(View button){
		// ファイル名
		String filename = System.currentTimeMillis() + ".jpg";
		
		// 保存場所（内部ストレージのAnimCalcフォルダ）
		File dir = new File(Environment.getExternalStorageDirectory(), "AnimCalc");
		dir.mkdirs();
		
		// ファイルのパス
		File file = new File(dir, filename);
		m_imageUri = Uri.fromFile(file);
		
		// カメラ呼び出し
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, m_imageUri);
		startActivityForResult(intent, MY_CAMERA);
	}
	
	// アルバム起動
	public void buttonMethodAlbum(View button){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, MY_ALBUM);
	}

	// アプリに戻ってきたときの処理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK)return;
		Uri uri = null;
		if(requestCode == MY_CAMERA){
			if(data == null || data.getData() == null){
				uri = m_imageUri;
			}
			else{
				uri = data.getData();
			}
		}
		else if(requestCode == MY_ALBUM){
			uri = data.getData();
		}
		Bitmap bitmap = ImageUtils.decodeUri(this, uri);
		m_imageView.setImageBitmap(bitmap);
		//m_imageView.setImageURI(uri);
		//super.onActivityResult(requestCode, resultCode, data);
	}
}
