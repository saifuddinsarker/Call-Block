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



import android.widget.BaseAdapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListviewAdapter extends BaseAdapter {
	private ArrayList<String> listname;
	private ArrayList<String> listnumber;
	private Activity activity;

	public ListviewAdapter(Activity activity,ArrayList<String> listName,ArrayList<String> listNumber) {
		super();
		this.listname = listName;
		this.listnumber = listNumber;
		this.activity = activity;
	}


	public int getCount() {
		// TODO Auto-generated method stub
		return listname.size();
	}


	public String getItem(int position) {
		// TODO Auto-generated method stub
		return listname.get(position);
	}


	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder
	{
		public TextView txtViewDate;
		public TextView txtViewTitle;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder view;
		LayoutInflater inflator = activity.getLayoutInflater();

		if(convertView==null)
		{
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.list_row, null);

			view.txtViewTitle = (TextView)convertView.findViewById(R.id.name);
			view.txtViewDate = (TextView)convertView.findViewById(R.id.sdate);

			convertView.setTag(view);
		}
		else
		{
			view = (ViewHolder) convertView.getTag();
		}

		view.txtViewTitle.setText("Name : "+listname.get(position));
		view.txtViewDate.setText("Number: "+listnumber.get(position));

		return convertView;
	}
}