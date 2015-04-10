package com.chuck.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;


 /**
 * @Title：FashionDIY
 * @Description：
 * @date 2014-12-15 上午11:54:40
 * @author Administrator
 * @version 1.0
 */

public class MainActivity extends Activity {
	
	 private FlipViewController flipView;
	 private DisplayMetrics screenMetrics;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("翻页效果demo");
		
		screenMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(screenMetrics);


	    flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);

	    flipView.setAdapter(new TravelAdapter(this , screenMetrics));

	    setContentView(flipView);
	  }

	  @Override
	  protected void onResume() {
	    super.onResume();
	    flipView.onResume();
	  }

	  @Override
	  protected void onPause() {
	    super.onPause();
	    flipView.onPause();
	  }
	
}
