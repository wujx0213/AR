package cn.edu.neu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Window;
import cn.edu.neu.util.FileHelper;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		
		FileHelper.initFileHelper(getApplicationContext(), getFilesDir().getAbsolutePath());
//		FileHelper.initFileHelper(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath());
		FileHelper.getInstance().syncGlobalData();
		
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				Intent intent = new Intent(Splash.this, MainActivity.class);
				startActivity(intent);
				Splash.this.finish();
			}
		}, 2000);
	}
}