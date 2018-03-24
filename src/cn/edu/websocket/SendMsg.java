package cn.edu.websocket;

import android.app.Application;
import android.widget.Toast;
import cn.edu.neu.global.GlobalData;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

public class SendMsg {
	public String url = null;
	public String msg = null;

	public SendMsg() {

	}

	public SendMsg(String url, String msg) {
		this.msg = msg;
		this.url = url;
	}

	public static void connectService(String url) {
		try {
			GlobalData.wscon.connect(url, new WebSocketConnectionHandler() {

				@Override
				public void onOpen() {
					// TODO Auto-generated method stub
					super.onOpen();
				}

				@Override
				public void onClose(int code, String reason) {
					// TODO Auto-generated method stub
					super.onClose(code, reason);
				}

				@Override
				public void onTextMessage(String payload) {
					// TODO Auto-generated method stub
					super.onTextMessage(payload);
					System.out.println(payload);
				}

				@Override
				public void onRawTextMessage(byte[] payload) {
					// TODO Auto-generated method stub
					super.onRawTextMessage(payload);
					System.out.println(payload);
				}

				@Override
				public void onBinaryMessage(byte[] payload) {
					// TODO Auto-generated method stub
					super.onBinaryMessage(payload);
					System.out.println(payload);
				}

			});
		} catch (WebSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendMsg(String msg) {
		if (GlobalData.wscon.isConnected()) {
			GlobalData.wscon.sendTextMessage(msg);
		} else {
			System.out.println("aaaaaaa");
			// Toast.makeText(, "未连接服务器", Toast.LENGTH_SHORT).show();
		}
	}

}
