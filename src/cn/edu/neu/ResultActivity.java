package cn.edu.neu;

import cn.edu.neu.global.GlobalData;
import cn.edu.websocket.SendMsg;
import android.os.Bundle;

public class ResultActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//SendMsg.connectService(GlobalData.wsuri);
		SendMsg.sendMsg("欢迎查询最后结果");
		
	}
}
