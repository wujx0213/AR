package cn.edu.neu.global;

import com.microsoft.band.InvalidBandVersionException;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandAltimeterEvent;
import com.microsoft.band.sensors.BandAltimeterEventListener;
import com.microsoft.band.sensors.BandAmbientLightEvent;
import com.microsoft.band.sensors.BandAmbientLightEventListener;
import com.microsoft.band.sensors.BandBarometerEvent;
import com.microsoft.band.sensors.BandBarometerEventListener;
import com.microsoft.band.sensors.BandCaloriesEvent;
import com.microsoft.band.sensors.BandCaloriesEventListener;
import com.microsoft.band.sensors.BandDistanceEvent;
import com.microsoft.band.sensors.BandDistanceEventListener;
import com.microsoft.band.sensors.BandGsrEvent;
import com.microsoft.band.sensors.BandGsrEventListener;
import com.microsoft.band.sensors.BandGyroscopeEvent;
import com.microsoft.band.sensors.BandGyroscopeEventListener;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandPedometerEvent;
import com.microsoft.band.sensors.BandPedometerEventListener;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.sensors.BandUVEvent;
import com.microsoft.band.sensors.BandUVEventListener;

import cn.edu.neu.util.SensorData;

public class BandManager {

	public static String AccelerometerData = "";
	public static String AltimeterrData = "";
	public static String AmbientLightData = "";
	public static String BarometerData = "";
	public static String CaloriesData = "";
	public static String DistanceData = "";
	public static String GsrData = "";
	public static String GyroscopeData = "";
	public static String HeartRateData = "";
	public static String PedometerData = "";
	public static String RRIntervalData = "";
	public static String SkinTemperatureData = "";
	public static String UVData = "";
	
	
	
	/**
	 * Some sensors have dynamic intervals, such as the Accelerometer (on Android and Windows),
	 * that allow developers to specify at what rate they want data to be delivered. 
	 * Other sensors deliver data only as their values change.
	 */
	//加速度，3列数据
	//有采样频率
	public static BandAccelerometerEventListener mAccelerometerEventListener = new BandAccelerometerEventListener() {
        @Override
        public void onBandAccelerometerChanged(final BandAccelerometerEvent event) {
            if (event != null) {
            	AccelerometerData = String.format("%.2f,%.2f,%.2f", event.getAccelerationX(),
            			event.getAccelerationY(), event.getAccelerationZ());
            }
        }
    };
    //高度,9列数据
    public static BandAltimeterEventListener mAltimeterEventListener = new BandAltimeterEventListener() {
        @Override
        public void onBandAltimeterChanged(final BandAltimeterEvent event) {
            if (event != null) {
            	AltimeterrData = String.format(
//            			"%d," + "%d," + "%d," + "%d," + "%d," + "%d," + "%d," + "%d",
            			 "%.2f" ,
//            			event.getTotalGain(),
//            			event.getTotalLoss(),
//            			event.getSteppingGain(),
//            			event.getSteppingLoss(),
//            			event.getStepsAscended(),
//            			event.getStepsDescended(),
//            			event.getFlightsAscended(),
//            			event.getFlightsDescended(),
            			//Method to get the current rate of ascent/descent in cm/s
            			event.getRate()
            			).toString();
            }
        }
    };
    //环境光线，1列
    public static BandAmbientLightEventListener mAmbientLightEventListener = new BandAmbientLightEventListener() {
        @Override
        public void onBandAmbientLightChanged(final BandAmbientLightEvent event) {
            if (event != null) {
            	//Method to get the current ambient light being received by the Band's ambient light sensor in lux
            	AmbientLightData = String.format("%d", event.getBrightness());
            }
        }
    };
    //气压计,2列
    public static BandBarometerEventListener mBarometerEventListener = new BandBarometerEventListener() {
        @Override
        public void onBandBarometerChanged(final BandBarometerEvent event) {
            if (event != null) {
            	BarometerData = String.format("%.2f,"/* + "%.2f"*/, event.getAirPressure()/*, event.getTemperature()*/);
            }
        }
    };
    //卡路里,3列,先不用估计没啥用
    public static BandCaloriesEventListener mBandCaloriesEventListener = new BandCaloriesEventListener(){

		@Override
		public void onBandCaloriesChanged(BandCaloriesEvent event) {
			if(event!=null){
				try {
					CaloriesData = String.format("%d,%d,%d", 
							event.getCaloriesToday(),event.getCalories(),event.getTimestamp());
				} catch (InvalidBandVersionException e) {
					e.printStackTrace();
				}
			}
		}
    };
//    //连接状态
//    public static BandContactEventListener mBandContactEventListener = new BandContactEventListener(){
//
//		@Override
//		public void onBandContactChanged(BandContactEvent event) {
//			if(event!=null){
//				event.getContactState();
//			}
//		}
//    };
    //距离,4列
    public static BandDistanceEventListener mBandDistanceEventListener = new BandDistanceEventListener(){

		@Override
		public void onBandDistanceChanged(BandDistanceEvent event) {
			if(event!=null){
					DistanceData = String.format(
//							"%d," + "%.2f," + "%.2f," + "%d",
//							event.getDistanceToday(),
////							event.getMotionType(),
//							event.getPace(),
//							event.getSpeed(),
//							event.getTotalDistance()
							//Method to get the current speed at which the band is moving in cm/s
							"%.2f",event.getSpeed()
							);
			}
		}
    	
    };
    //皮肤电反应,1列,先不用
    //有采样频率
    public static BandGsrEventListener mGsrEventListener = new BandGsrEventListener() {
        @Override
        public void onBandGsrChanged(final BandGsrEvent event) {
            if (event != null) {
            	GsrData = String.format("%d", event.getResistance());
            }
        }
    };
    //陀螺仪,6列
    //有采样频率
    public static BandGyroscopeEventListener mBandGyroscopeEventListener = new BandGyroscopeEventListener(){

		@Override
		public void onBandGyroscopeChanged(BandGyroscopeEvent event) {
			if(event!=null){
				GyroscopeData = String .format(
						"%.2f," + "%.2f," + "%.2f," + "%.2f," + "%.2f," + "%.2f",
						event.getAccelerationX(),
						event.getAccelerationY(),
						event.getAccelerationZ(),
						event.getAngularVelocityX(),
						event.getAngularVelocityY(),
						event.getAngularVelocityZ());
			}
		}
    	
    };
    //心率,1列
    public static BandHeartRateEventListener mHeartRateEventListener = new BandHeartRateEventListener() {
        @Override
        public void onBandHeartRateChanged(final BandHeartRateEvent event) {
            if (event != null) {
            	HeartRateData = String.format("%d", event.getHeartRate());
            }
        }
    };
    //计步,2列,先不用
    public static BandPedometerEventListener mBandPedometerEventListener = new BandPedometerEventListener(){

		@Override
		public void onBandPedometerChanged(BandPedometerEvent event) {
			if(event!=null){
				try {
					PedometerData = String.format("%d,"+"%d", event.getStepsToday(),event.getTotalSteps());
				} catch (InvalidBandVersionException e) {
					e.printStackTrace();
				}
			}
		}
    	
    };
    //RR间隔
    public static BandRRIntervalEventListener mRRIntervalEventListener = new BandRRIntervalEventListener() {
        @Override
        public void onBandRRIntervalChanged(final BandRRIntervalEvent event) {
            if (event != null) {
            	RRIntervalData = String.format("%.2f", event.getInterval());
            }
        }
    };
    //皮肤温度，先不用
    
    public static BandSkinTemperatureEventListener mBandSkinTemperatureEventListener = new BandSkinTemperatureEventListener(){

		@Override
		public void onBandSkinTemperatureChanged(BandSkinTemperatureEvent event) {
			if(event!=null){
				SkinTemperatureData = String.format("%.2f", event.getTemperature());
			}
		}
    };
    //紫外线，先不用
    public static BandUVEventListener mBandUVEventListener = new BandUVEventListener(){

		@Override
		public void onBandUVChanged(BandUVEvent event) {
			if(event!=null){
				try {
					UVData = String.format("%d", event.getUVExposureToday());
				} catch (InvalidBandVersionException e) {
					e.printStackTrace();
				}
			}
		}
    	
    };
}
