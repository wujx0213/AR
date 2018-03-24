package cn.edu.neu;

import cn.edu.neu.global.GlobalData;
import cn.edu.websocket.SendMsg;
import android.os.Bundle;

public class TrainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//SendMsg.connectService(GlobalData.wsuri);
		SendMsg.sendMsg("¿ªÊ¼ÑµÁ·");
	}
}
