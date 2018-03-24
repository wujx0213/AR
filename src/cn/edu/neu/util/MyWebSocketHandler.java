package cn.edu.neu.util;

import android.widget.Toast;
import cn.edu.neu.global.GlobalData;
import de.tavendo.autobahn.WebSocketConnectionHandler;

public class MyWebSocketHandler extends WebSocketConnectionHandler{

	@Override
	public void onBinaryMessage(byte[] payload) {
		super.onBinaryMessage(payload);
	}

	@Override
	public void onClose(int code, String reason) {
		GlobalData.netStatus = false;
		Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),
				"已断开与服务器的连接", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onOpen() {
		GlobalData.netStatus = true;
	}

	@Override
	public void onRawTextMessage(byte[] payload) {
		super.onRawTextMessage(payload);
	}

	@Override
	public void onTextMessage(String payload) {
		super.onTextMessage(payload);
	}

}
