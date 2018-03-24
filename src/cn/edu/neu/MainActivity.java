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
			// 判断是否有本地数据，没有则提示

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
				Toast.makeText(MainActivity.this, "无可删除数据", Toast.LENGTH_LONG).show();
			} else {
				final String[] actions = (String[]) GlobalData.existDataAction.toArray(new String[GlobalData.existDataAction.size()]);// ArrayList to String[]
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("请选择要删除的动作");
				/**
				 * 第一个参数指定我们要显示的一组下拉多选框的数据集合
				 * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
				 * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false,
				 * true}; 第三个参数给每一个多选项绑定一个监听器
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
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
