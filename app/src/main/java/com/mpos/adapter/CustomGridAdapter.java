package com.mpos.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpos.activity.R;

@SuppressLint("InflateParams")
public class CustomGridAdapter extends BaseAdapter {
	private Context mContext;
	private final String[] web;
	private final int[] Imageid;

	Typeface tfBold;

	public CustomGridAdapter(Context context, String[] web, int[] Imageid) {
		mContext = context;
		this.Imageid = Imageid;
		this.web = web;

		tfBold = Typeface.createFromAsset(context.getAssets(),
				"Interstate.ttf");
	}

	@Override
	public int getCount() {
		return web.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View grid;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			grid = new View(mContext);
			grid = inflater.inflate(R.layout.grid_items, null);
			TextView textView = (TextView) grid.findViewById(R.id.grid_text);
			textView.setTypeface(tfBold, Typeface.BOLD);
			ImageView imageView = (ImageView) grid
					.findViewById(R.id.grid_image);
			textView.setText(web[position]);
			imageView.setImageResource(Imageid[position]);
		} else {
			grid = (View) convertView;
		}
		return grid;
	}
}
