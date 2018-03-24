package cn.edu.neu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.edu.neu.global.GlobalData;
import cn.edu.neu.util.FileHelper;
import cn.edu.websocket.SendMsg;

public class MainActivity extends BaseActivity implements OnClickListener {

	private Button collectButton;
	private Button trainButton;
	private Button recognizeButton;
	private Button resultButton;
	private Button clearButton;
	private FileHelper filehelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		collectButton = (Button) findViewById(R.id.collectButton);
		trainButton = (Button) findViewById(R.id.trainButton);
		recognizeButton = (Button) findViewById(R.id.recognizeButton);
		resultButton = (Button) findViewById(R.id.resultButton);
		clearButton = (Button) findViewById(R.id.clearButton);
		filehelper = FileHelper.getInstance();

		collectButton.setOnClickListener(this);
		trainButton.setOnClickListener(this);
		recognizeButton.setOnClickListener(this);
		resultButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);
		SendMsg.connectService(GlobalData.wsuri);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.collectButton:

			//SendMsg.sendMsg("hahaha");
			Intent intent = new Intent(this, DataCollectActivity.class);
			startActivity(intent);
			break;
		case R.id.trainButton:
			// �ж��Ƿ��б������ݣ�û������ʾ

			Intent intent2 = new Intent(this, TrainActivity.class);
			startActivity(intent2);
			break;
		case R.id.recognizeButton:
			Intent intent3 = new Intent(this, RecognizeActivity.class);
			startActivity(intent3);
			break;
		case R.id.resultButton:
			Intent intent4 = new Intent(this, ResultActivity.class);
			startActivity(intent4);
			break;
		case R.id.clearButton:
			final boolean[] usersChoise = new boolean[GlobalData.existDataAction.size()];
			if (GlobalData.existDataAction.isEmpty()) {
				Toast.makeText(MainActivity.this, "�޿�ɾ������", Toast.LENGTH_LONG).show();
			} else {
				final String[] actions = (String[]) GlobalData.existDataAction.toArray(new String[GlobalData.existDataAction.size()]);// ArrayList to String[]
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("��ѡ��Ҫɾ���Ķ���");
				/**
				 * ��һ������ָ������Ҫ��ʾ��һ��������ѡ������ݼ���
				 * �ڶ������������ļ���ѡ�ѡ�������null�����ʾһ������ѡ�����ϣ��ָ����һ����ѡѡ���ѡ��
				 * ��Ҫ����һ��boolean[]�����ȥ���䳤��Ҫ�͵�һ�������ĳ�����ͬ������ {true, false, false,
				 * true}; ������������ÿһ����ѡ���һ��������
				 */
				builder.setMultiChoiceItems(actions, null, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						usersChoise[which] = isChecked;
//						for(int i = 0;i<GlobalData.existDataAction.size();i++){
//							Log.d("hhhh", ""+usersChoise[i]);
//						}
					}
				});
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						for(int i = 0;i<usersChoise.length;i++){
							if(usersChoise[i]){
								filehelper.delDataFile(actions[i]+".txt");
							}
						}
						filehelper.syncGlobalData();
					}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.show();
			}
			break;
		default:
			break;
		}
	}
}
