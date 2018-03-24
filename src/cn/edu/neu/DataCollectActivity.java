package cn.edu.neu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class DataCollectActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.grid);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new MyGridAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(DataCollectActivity.this, "" + position,Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(DataCollectActivity.this,SpecificAction.class);
				intent.putExtra("position", position);
				startActivity(intent);
			}
	    });
	}
}
