package cn.edu.neu;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.GsrSampleRate;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.sensors.SampleRate;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.neu.global.BandManager;
import cn.edu.neu.global.GlobalData;
import cn.edu.neu.util.MyWebSocketHandler;
import cn.edu.neu.util.SensorData;
import de.tavendo.autobahn.WebSocketException;

public class RecognizeActivity extends BaseActivity {

	private Button begin;
	private Button stop;
	private Button consent;
	private BandClient client;
	private TextView showdata;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static SensorData sentorjson = new SensorData();
	private static Gson gson = new Gson();
	final Handler handler = new Handler();
//	FileOutputStream out = null;
//	BufferedWriter writer = null;
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			//将GlobalData中的Sensor数据封装成json数据，使用wscon传输到服务器
			RecognizeActivity.sentorjson.setAccelerometerData(BandManager.AccelerometerData);
			RecognizeActivity.sentorjson.setAltimeterrData(BandManager.AltimeterrData);
			RecognizeActivity.sentorjson.setAmbientLightData(BandManager.AmbientLightData);
			RecognizeActivity.sentorjson.setBarometerData(BandManager.BarometerData);
//			RecognizeActivity.sentorjson.setCaloriesData(BandManager.CaloriesData);
			RecognizeActivity.sentorjson.setDistanceData(BandManager.DistanceData);
//			RecognizeActivity.sentorjson.setGsrData(BandManager.GsrData);
			RecognizeActivity.sentorjson.setGyroscopeData(BandManager.GyroscopeData);
			RecognizeActivity.sentorjson.setHeartRateData(BandManager.HeartRateData);
//			RecognizeActivity.sentorjson.setPedometerData(BandManager.PedometerData);
			RecognizeActivity.sentorjson.setRRIntervalData(BandManager.RRIntervalData);
			RecognizeActivity.sentorjson.setSkinTemperatureData(BandManager.SkinTemperatureData);
			RecognizeActivity.sentorjson.setUVData(BandManager.UVData);
			RecognizeActivity.sentorjson.setTimeStamp(formatter.format(new Date()));
			GlobalData.wscon.sendTextMessage(gson.toJson(sentorjson));
			handler.postDelayed(this, 1000);// 2ms是延时时长
		}
//		public void run() {
//			showdata.setText("1. " + BandManager.AccelerometerData + "\n2. " + BandManager.AltimeterrData + "\n3. "
//					+ BandManager.AmbientLightData + "\n4. " + BandManager.BarometerData + /*"5. " + BandManager.CaloriesData
//					+*/ "\n6. " + BandManager.DistanceData + /*"7. " + BandManager.GsrData +*/ "\n8. " + BandManager.GyroscopeData
//					+ "\n9. " + BandManager.HeartRateData + /*"10. " + BandManager.PedometerData +*/ "\n11. "
//					+ BandManager.RRIntervalData + "\n12. " + BandManager.SkinTemperatureData + "\n13. " + BandManager.UVData);
//			try {
//				writer.write(BandManager.AccelerometerData
//						+ ";" + BandManager.AltimeterrData
//						+ ";" + BandManager.AmbientLightData
//						+ ";" + BandManager.BarometerData
////						+ ";" + BandManager.CaloriesData
//						+ ";" + BandManager.DistanceData
////						+ ";" + BandManager.GsrData
//						+ ";" + BandManager.GyroscopeData
//						+ ";" + BandManager.HeartRateData
////						+ ";" + BandManager.PedometerData
//						+ ";" + BandManager.RRIntervalData
//						+ ";" + BandManager.SkinTemperatureData
//						+ ";" + BandManager.UVData+ "\n");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			handler.postDelayed(this, 200);// 0.2ms是延时时长
//		}
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionrecognize);
		
		begin = (Button) findViewById(R.id.beginRecognize);
		stop = (Button) findViewById(R.id.stopRecognize);
		consent = (Button) findViewById(R.id.consentGiven2);
		showdata = (TextView) findViewById(R.id.showData1);
		
		try {
			GlobalData.wscon.connect(GlobalData.wsuri, new MyWebSocketHandler());
		} catch (WebSocketException e) {
			appendToUI(e.getMessage());
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
				if(GlobalData.netStatus){
					registerSensorListener();
					if(GlobalData.registerResult){
						begin.setText("Recognizing...");
						begin.setEnabled(false);
						showdata.setText("");
						handler.postDelayed(runnable, 0);
					}else {
						appendToUI("BandSensorListener 注册失败");
					}
				}else {
					appendToUI("未连接到服务器");
				}
			}
		});
//		begin.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				// new SubscriptionTask().execute();
//				registerSensorListener();
//				begin.setText("Collecting...");
//				begin.setEnabled(false);
//				showdata.setText("");
//				
//				try {
//					out = getApplicationContext().openFileOutput("ar.txt", Context.MODE_PRIVATE);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//				writer = new BufferedWriter(new OutputStreamWriter(out));
////				while (!GlobalData.registerResult) {
////				}
//				handler.postDelayed(runnable, 1000);
//
//			}
//		});
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!begin.isEnabled()) {
					handler.removeCallbacks(runnable);
					if (client != null) {
						try {
							client.getSensorManager().unregisterAllListeners();
						} catch (BandIOException e) {
							appendToUI(e.getMessage());
						}
					}
					GlobalData.registerResult = false;
					RecognizeActivity.this.finish();
				} else {
					Toast.makeText(RecognizeActivity.this, "数据采集还未开始", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	private void registerSensorListener(){
		try {
			if (getConnectedBandClient()) {
				int hardwareVersion = Integer.parseInt(client.getHardwareVersion().await());
				if (hardwareVersion >= 20) {
					appendToUI("Band is connected.\n");
					client.getSensorManager().registerAccelerometerEventListener(BandManager.mAccelerometerEventListener, SampleRate.MS128);
					client.getSensorManager().registerAltimeterEventListener(BandManager.mAltimeterEventListener);
					client.getSensorManager().registerAmbientLightEventListener(BandManager.mAmbientLightEventListener);
					client.getSensorManager().registerBarometerEventListener(BandManager.mBarometerEventListener);
//					client.getSensorManager().registerCaloriesEventListener(BandManager.mBandCaloriesEventListener);
					client.getSensorManager().registerDistanceEventListener(BandManager.mBandDistanceEventListener);
//					client.getSensorManager().registerGsrEventListener(BandManager.mGsrEventListener,GsrSampleRate.MS200);
					client.getSensorManager().registerGyroscopeEventListener(BandManager.mBandGyroscopeEventListener,SampleRate.MS128);
					if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
						client.getSensorManager().registerHeartRateEventListener(BandManager.mHeartRateEventListener);
					} else {
						appendToUI("You have not given this application consent to access heart rate data yet."
								+ " Please press the Heart Rate Consent button.\n");
					}
//					client.getSensorManager().registerPedometerEventListener(BandManager.mBandPedometerEventListener);
					if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
						client.getSensorManager().registerRRIntervalEventListener(BandManager.mRRIntervalEventListener);
					} else {
						appendToUI("You have not given this application consent to access heart rate data yet."
								+ " Please press the Heart Rate Consent button.\n");
					}
					client.getSensorManager().registerSkinTemperatureEventListener(BandManager.mBandSkinTemperatureEventListener);
					client.getSensorManager().registerUVEventListener(BandManager.mBandUVEventListener);
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
											RecognizeActivity.this.finish();
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
				
			} catch (BandException e) {
				
			}
		}
		GlobalData.wscon.disconnect();
		super.onDestroy();
	}

}

