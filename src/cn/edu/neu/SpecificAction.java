package cn.edu.neu;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandIOException;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.GsrSampleRate;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.sensors.SampleRate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Notification.Action;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.neu.global.BandManager;
import cn.edu.neu.global.GlobalData;
import cn.edu.neu.util.FileHelper;
import cn.edu.websocket.*;

public class SpecificAction extends BaseActivity {

	private Button begin;
	private Button stop;
	private Button consent;
	private EditText actionSelfDef;
	private TextView showdata;
	private TextView actionHint;
	private int actionCategory;
	private BandClient client;
	private FileHelper fileHelper;
	final Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			showdata.setText("1. " + BandManager.AccelerometerData + "\n2. " + BandManager.AltimeterrData + /*"\n3. "
					+ BandManager.AmbientLightData +*/ "\n4. " + BandManager.BarometerData + /*"5. " + BandManager.CaloriesData
					+*/ "\n6. " + BandManager.DistanceData + /*"7. " + BandManager.GsrData +*/ "\n8. " + BandManager.GyroscopeData
					+ "\n9. " + BandManager.HeartRateData + /*"10. " + BandManager.PedometerData +*/ "\n11. "
					+ BandManager.RRIntervalData /*+ "\n12. " + BandManager.SkinTemperatureData + "\n13. " + BandManager.UVData*/);
			try {
				writer.write(BandManager.AccelerometerData
						+ "," + BandManager.AltimeterrData
//						+ "," + BandManager.AmbientLightData
						+ "," + BandManager.BarometerData
//						+ "," + BandManager.CaloriesData
						+ "," + BandManager.DistanceData
//						+ "," + BandManager.GsrData
						+ "," + BandManager.GyroscopeData
						+ "," + BandManager.HeartRateData
//						+ "," + BandManager.PedometerData
						+ "," + BandManager.RRIntervalData
//						+ "," + BandManager.SkinTemperatureData
//						+ "," + BandManager.UVData
						+ "," + action + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			String msg = BandManager.AccelerometerData
					+ "," + BandManager.AltimeterrData
//					+ "," + BandManager.AmbientLightData
					+ "," + BandManager.BarometerData
//					+ "," + BandManager.CaloriesData
					+ "," + BandManager.DistanceData
//					+ "," + BandManager.GsrData
					+ "," + BandManager.GyroscopeData
					+ "," + BandManager.HeartRateData
//					+ "," + BandManager.PedometerData
					+ "," + BandManager.RRIntervalData
//					+ "," + BandManager.SkinTemperatureData
//					+ "," + BandManager.UVData
					+ "," + action + "\n";
			SendMsg.sendMsg(msg);
			handler.postDelayed(this, 100);// 0.1ms是延时时长
		}
	};
//	private File file = null;
	private FileOutputStream out = null;
    private BufferedWriter writer = null;
    private String action = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specificaction);

		begin = (Button) findViewById(R.id.beginCollect);
		stop = (Button) findViewById(R.id.stopCollect);
		consent = (Button) findViewById(R.id.consentGiven);
		actionSelfDef = (EditText) findViewById(R.id.actionSelfDef);
		showdata = (TextView) findViewById(R.id.showData);
		actionHint = (TextView) findViewById(R.id.actionHint);
		fileHelper = FileHelper.getInstance();
		Intent intent = getIntent();
		actionCategory = intent.getIntExtra("position", -1);
		if (actionCategory == 8) {
			actionHint.setVisibility(View.INVISIBLE);
		} else {
			actionSelfDef.setVisibility(View.INVISIBLE);
			actionSelfDef.setEnabled(false);
			actionHint.setText(GlobalData.img_text[actionCategory]);
			action = GlobalData.img_text[actionCategory];
		}

		
		final WeakReference<Activity> reference = new WeakReference<Activity>(this);
		
		consent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new HeartRateConsentTask().execute(reference);
			}
		});
		begin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(actionCategory==8){
					action = actionSelfDef.getText().toString();
				}
				if(action.isEmpty()){
					Toast.makeText(SpecificAction.this, "请输入自定义动作", Toast.LENGTH_LONG).show();
				}else{
					
					new SubscriptionTask().execute();
					 begin.setText("采集中...");
					 begin.setEnabled(false);
					 showdata.setText("");
					 try {
						 out = getApplicationContext().openFileOutput(action + ".txt", Context.MODE_PRIVATE);
					 } catch (FileNotFoundException e) {
						 e.printStackTrace();
					 }
//					 String en=Environment.getExternalStorageState();  
//				     //获取SDCard状态,如果SDCard插入了手机且为非写保护状态  
//					if (en.equals(Environment.MEDIA_MOUNTED)) {
//						//测试下会不会覆盖确保会覆盖已有文件若没有创建新文件
//						file = new File(Environment.getExternalStorageDirectory(), action + ".txt");
//					} else {
//						// 提示用户SDCard不存在或者为写保护状态
//						Toast.makeText(SpecificAction.this, "SDCard不存在或者为写保护状态", 1).show();
//					}
					 writer = new BufferedWriter(new OutputStreamWriter(out));
					 while(!GlobalData.registerResult){}
					 handler.postDelayed(runnable, 1000);
				}
			}
		});
		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!begin.isEnabled()) {
					handler.removeCallbacks(runnable);
					try {
						if(writer!=null){
							writer.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					fileHelper.syncGlobalData();
					if (client != null) {
						try {
							client.getSensorManager().unregisterAllListeners();
						} catch (BandIOException e) {
							appendToUI(e.getMessage());
						}
					}
					GlobalData.registerResult = false;
					SpecificAction.this.finish();
				} else {
					Toast.makeText(SpecificAction.this, "数据采集还未开始", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	private class SubscriptionTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (getConnectedBandClient()) {
					int hardwareVersion = Integer.parseInt(client.getHardwareVersion().await());
					if (hardwareVersion >= 20) {
						appendToUI("Band is connected.\n");
						client.getSensorManager().registerAccelerometerEventListener(BandManager.mAccelerometerEventListener, SampleRate.MS128);
						client.getSensorManager().registerAltimeterEventListener(BandManager.mAltimeterEventListener);
//						client.getSensorManager().registerAmbientLightEventListener(BandManager.mAmbientLightEventListener);
						client.getSensorManager().registerBarometerEventListener(BandManager.mBarometerEventListener);
//						client.getSensorManager().registerCaloriesEventListener(BandManager.mBandCaloriesEventListener);
						client.getSensorManager().registerDistanceEventListener(BandManager.mBandDistanceEventListener);
//						client.getSensorManager().registerGsrEventListener(BandManager.mGsrEventListener,GsrSampleRate.MS200);
						client.getSensorManager().registerGyroscopeEventListener(BandManager.mBandGyroscopeEventListener,SampleRate.MS128);
						if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
							client.getSensorManager().registerHeartRateEventListener(BandManager.mHeartRateEventListener);
						} else {
							appendToUI("You have not given this application consent to access heart rate data yet."
									+ " Please press the Heart Rate Consent button.\n");
						}
//						client.getSensorManager().registerPedometerEventListener(BandManager.mBandPedometerEventListener);
						if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
							client.getSensorManager().registerRRIntervalEventListener(BandManager.mRRIntervalEventListener);
						} else {
							appendToUI("You have not given this application consent to access heart rate data yet."
									+ " Please press the Heart Rate Consent button.\n");
						}
//						client.getSensorManager().registerSkinTemperatureEventListener(BandManager.mBandSkinTemperatureEventListener);
//						client.getSensorManager().registerUVEventListener(BandManager.mBandUVEventListener);
						GlobalData.registerResult = true;
					}
				} else {
					appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
				}
			} catch (BandException e) {
				String exceptionMessage = "";
				switch (e.getErrorType()) {
				case UNSUPPORTED_SDK_VERSION_ERROR:
					exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
					break;
				case SERVICE_ERROR:
					exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
					break;
				default:
					exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
					break;
				}
				appendToUI(exceptionMessage);

			} catch (Exception e) {
				appendToUI(e.getMessage());
			}
			return null;
		}
	}

	private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {
		@Override
		protected Void doInBackground(WeakReference<Activity>... params) {
			try {
				if (getConnectedBandClient()) {

					if (params[0].get() != null) {
						client.getSensorManager().requestHeartRateConsent(params[0].get(),
								new HeartRateConsentListener() {
									@Override
									public void userAccepted(boolean consentGiven) {
										if(!consentGiven){
											SpecificAction.this.finish();
										}
									}
								});
					}
				} else {
					appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
				}
			} catch (BandException e) {
				String exceptionMessage = "";
				switch (e.getErrorType()) {
				case UNSUPPORTED_SDK_VERSION_ERROR:
					exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
					break;
				case SERVICE_ERROR:
					exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
					break;
				default:
					exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
					break;
				}
				appendToUI(exceptionMessage);

			} catch (Exception e) {
				appendToUI(e.getMessage());
			}
			return null;
		}
	}

	private void appendToUI(final String string) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showdata.setText(string);
			}
		});
	}

	private boolean getConnectedBandClient() throws InterruptedException, BandException {
		if (client == null) {
			BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
			if (devices.length == 0) {
				appendToUI("Band isn't paired with your phone.\n");
				return false;
			}
			client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);
		} else if (ConnectionState.CONNECTED == client.getConnectionState()) {
			return true;
		}

		appendToUI("Band is connecting...\n");
		return ConnectionState.CONNECTED == client.connect().await();
	}

	// 禁用系统的返回键，如果没开始采集数据则返回键可用，若已经开始采集数据则只能通过stop键返回
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (begin.isEnabled()) {
				return super.onKeyDown(keyCode, event);
			} else {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		if (client != null) {
			try {
				client.disconnect().await();
			} catch (InterruptedException e) {
				// Do nothing as this is happening during destroy
			} catch (BandException e) {
				// Do nothing as this is happening during destroy
			}
		}
		super.onDestroy();
	}
}
