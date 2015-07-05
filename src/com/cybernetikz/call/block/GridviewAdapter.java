/*
 ===========================================================================
 Copyright (c) 2013 CyberNetikz, www.cybernetikz.com
 Project: Future Text 1.0,
 Author: Md. Saifuddin Sarker (Mishu),
 Date:12-02-2013.
 mishu@cybernetikz.com
 ===========================================================================
 */
package com.cybernetikz.call.block;

import java.util.ArrayList;




import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridviewAdapter extends BaseAdapter
{
	private ArrayList<String> listCountry;
	private ArrayList<Integer> listFlag;
	private Activity activity;

	public GridviewAdapter(Activity activity,ArrayList<String> listCountry, ArrayList<Integer> listFlag) {
		super();
		this.listCountry = listCountry;
		this.listFlag = listFlag;
		this.activity = activity;
	}


	public int getCount() {
		// TODO Auto-generated method stub
		return listCountry.size();
	}


	public String getItem(int position) {
		// TODO Auto-generated method stub
		return listCountry.get(position);
	}


	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder
	{
		public ImageView imgViewFlag;
		public TextView txtViewTitle;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder view;
		LayoutInflater inflator = activity.getLayoutInflater();

		if(convertView==null)
		{
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.gridview_row, null);
			view.txtViewTitle = (TextView) convertView.findViewById(R.id.textView1);
			view.imgViewFlag = (ImageView) convertView.findViewById(R.id.imageView1);

			convertView.setTag(view);
		}
		else
		{
			view = (ViewHolder) convertView.getTag();
		}

		view.txtViewTitle.setText(listCountry.get(position));
		view.imgViewFlag.setImageResource(listFlag.get(position));

		return convertView;
	}

}
