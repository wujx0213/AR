package cn.edu.neu.global;

import java.util.ArrayList;

import cn.edu.neu.R;
import de.tavendo.autobahn.WebSocketConnection;

public class GlobalData {

	public static final int BaseActionNum = 8;
	public static ArrayList<String> existDataAction = new ArrayList<String>();
	public static final String[] img_text = { "����", "����", "����", "��·", "�ܲ�", "��¥","��¥", "˯��", "�Զ���" };
	public static int[] imgs = { R.drawable.sit, R.drawable.stand,
			R.drawable.hunker, R.drawable.walk,
			R.drawable.run, R.drawable.upstairs,
			R.drawable.downstaris, R.drawable.sleep, R.drawable.more };
	public static boolean registerResult = false;
	public static final String wsuri = "ws://219.216.78.168:6002/HelloWebJava/WSHello";
    public static WebSocketConnection wscon = new WebSocketConnection();
    public static boolean netStatus = false;
    
}