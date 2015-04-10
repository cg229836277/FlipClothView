package com.chuck.demo;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.flipclothviewdemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TravelAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	private int repeatCount = 1;

	private String FileDir = Environment.getExternalStorageDirectory() + File.separator + "Mydream";
	private List<File> files = new ArrayList<File>();

	private List<ImageView> imageViews;

	private DisplayMetrics mMetrics;

	BitmapUtil bitmapUtil;

	public TravelAdapter(Context context, DisplayMetrics metrics) {
		inflater = LayoutInflater.from(context);

		this.mMetrics = metrics;

		File filesDir = new File(FileDir);
		if (filesDir != null) {
			File[] currentFiles = filesDir.listFiles();
			if(currentFiles != null && currentFiles.length > 0){
				for(int i = 0 ; i < currentFiles.length ; i++){
					files.add(currentFiles[i]);
				}
			}
		}

		bitmapUtil = new BitmapUtil(context);
	}

	@Override
	public int getCount() {
		if (files != null && files.size() > 0) {
			if (files.size() % 4 != 0) {
				return files.size() / 4 + 1;
			} else {
				return files.size() / 4;
			}
		}
		return 0;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View layout = convertView;

		if (convertView == null) {
			layout = inflater.inflate(R.layout.complex1, null);
		}
		
		if(imageViews != null && imageViews.size() > 0){
			imageViews.clear();	
			imageViews = null;
		}
		
		imageViews = new ArrayList<ImageView>();
		
		imageViews.add((ImageView) layout.findViewById(R.id.first_image));
		imageViews.add((ImageView) layout.findViewById(R.id.second_image));
		imageViews.add((ImageView) layout.findViewById(R.id.third_image));
		imageViews.add((ImageView) layout.findViewById(R.id.fourth_image));

		int flag = position * 4;
		
		for (int i = flag; i < (flag + 4); i++) {
			if(i < files.size()){
				File currentFile = files.get(i);
				if (currentFile != null) {
					float resWidth = mMetrics.widthPixels / 2 - 20 * mMetrics.density;
					float resHeight = mMetrics.heightPixels / 2 - 20 * mMetrics.density;
					String filePath = currentFile.getAbsolutePath();
					bitmapUtil.loadBitmap(filePath, imageViews.get(i%4) , (int)resWidth , (int)resHeight);
				}
			}
		}

		return layout;
	}
}
