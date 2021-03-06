package com.cybernetikz.call.block;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class List extends Activity  implements OnItemClickListener{
	private DBAdapter db;
	private Cursor c;
	private ListviewAdapter mAdapter;
	final Context context = this;
	private ListView listview_episodes;
	SimpleAdapter sadapter;
	private ArrayList<String> episode_names = new ArrayList<String>();
	private ArrayList<String> episode_numbers = new ArrayList<String>();
	private ArrayList<Integer> episode_text = new ArrayList<Integer>();
	private ArrayList<Integer> episode_call = new ArrayList<Integer>();
	private ArrayList<Integer> episode_ids = new ArrayList<Integer>();
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.list);
	        
	        TextView TV = (TextView)findViewById(R.id.homeBottom);

		     Spannable WordtoSpan = new SpannableString("Developed By CyberNetikz");        

		     WordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(244,193,110)), 13, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		     TV.setText(WordtoSpan);
		     
	        
	        prepareList();        
	        mAdapter = new ListviewAdapter(this,episode_names,episode_numbers);
	        
	        listview_episodes = (ListView) findViewById(R.id.listview_episodes);
	        
	        listview_episodes.setAdapter(mAdapter);
	        
	        listview_episodes.setOnItemClickListener(this);
	        
	        
	        
	 }
	 
	  // fetching from Database
	    private void prepareList(){
	    	
	    	db = new DBAdapter(this); 
		    
		    db.open(); 
		    c = db.getALLBlock();

		    
		    Log.i("Block List Total:", c.getCount()+"<-" );
		     
		    if (c.moveToFirst())
		    {
		        do {          
		        	 Log.i("Future SMS Schedule :", c.getInt(0)+"<->"+c.getString(1)+"<->"+c.getString(2)+"<->"+c.getString(3)+"<->"+c.getString(4)+"<-" );
		        	 episode_ids.add(c.getInt(0));
		        	 episode_numbers.add(c.getString(1));
		        	 episode_names.add(c.getString(2));
		        	 episode_call.add(c.getInt(3));
		        	 episode_text.add(c.getInt(4));
	        	 	
		        } while (c.moveToNext());
		    }
		    
		    db.close(); 
		    
	    }
	   
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			 Log.i("Future SMS selet :", episode_numbers.get(position)+"-" );
			 
			 final Dialog dialogDetails = new Dialog(context);
			 
			 dialogDetails.getWindow();
			 dialogDetails.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogDetails.setContentView(R.layout.dialog);
			 
			 final int s_id=episode_ids.get(position);
			 
			 
			 TextView name = (TextView) dialogDetails.findViewById(R.id.to_name);
			 TextView number = (TextView) dialogDetails.findViewById(R.id.to_number);
			 TextView callblock = (TextView) dialogDetails.findViewById(R.id.sms);
			 TextView smsblock = (TextView) dialogDetails.findViewById(R.id.to_date);
			 
			 name.setText("To: "+episode_names.get(position));
			 number.setText("Number: "+episode_numbers.get(position));
			 if(episode_call.get(position)==1){
				 callblock.setText("Call Blocked: YES");
			 } else {
				 callblock.setText("Call Blocked: NO");
			 }
			 
			 
			 if(episode_text.get(position)==1){
				 smsblock.setText("Text Blocked: YES");
			 } else {
				 smsblock.setText("Text Blocked: NO");
			 }
			
			 
			/* Button Edit = (Button) dialogDetails.findViewById(R.id.edit);
			 Edit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialogDetails.dismiss();
					Intent refresh =new Intent(context, .class);
		        	refresh.putExtra("id", s_id);
		        	startActivity(refresh);
				}
			 });
			 */
			 Button Close = (Button) dialogDetails.findViewById(R.id.close);
			 Close.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					 dialogDetails.dismiss();
				}
			 });
			 
			 
			 Button Delete = (Button) dialogDetails.findViewById(R.id.delete);
			 Delete.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);

				    builder.setTitle("Confirm");
				    builder.setMessage("Are you sure want to delete this Number From Blocklist?");

				    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

				        public void onClick(DialogInterface dialog, int which) {
				        	db = new DBAdapter(context); 
				    	    db.open(); 
				    	    db.deleteBlock(s_id);
				    	    db.close(); 
				        	Intent refresh =new Intent(context, List.class);
				        	refresh.putExtra("tab", 1);
				        	startActivity(refresh);
				        }

				    });

				    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				            dialog.dismiss();
				        }
				    });

				    builder.create();
				    builder.show();
					
				}
			 });
			 
			 dialogDetails.show();
			
		
		}
	    
	    public void deleteAll(View v) {
	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(context);

		    builder.setTitle("Confirm");
		    builder.setMessage("Are you sure want to delete All Log?");

		    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

		        public void onClick(DialogInterface dialog, int which) {
		        	db = new DBAdapter(context); 
		    	    db.open(); 
		    	    db.deleteAllBlock();
		    	    db.close(); 
		        	Intent refresh =new Intent(context, List.class);
		        	startActivity(refresh);
		        }

		    });
		    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            dialog.dismiss();
		        }
		    });
		    builder.create();
		    builder.show();			
	     } 
	    
	    public void goHome(View v) {
	    	
			 Intent intent = new Intent();	
			 
			 intent.setClass(List.this,MainActivity.class);
				startActivity(intent);

		   } 
	    
	    @Override
		 public boolean onKeyDown(int keyCode, KeyEvent event)  {
		     if (keyCode == KeyEvent.KEYCODE_BACK
		             && event.getRepeatCount() == 0) {
		         onBackPressed();
		     }

		     return super.onKeyDown(keyCode, event);
		 }
		 @Override
		 public void onBackPressed() {
		     

			 Intent intent = new Intent();	
			 
			 intent.setClass(List.this,MainActivity.class);
				startActivity(intent);
		     return;
		 }
		 
		 public void onClick(View v) {
		    	
		    	Intent viewIntent =
		    	          new Intent("android.intent.action.VIEW",
		    	            Uri.parse("http://www.cybernetikz.com/"));
		    	          startActivity(viewIntent);

		      } 

}
