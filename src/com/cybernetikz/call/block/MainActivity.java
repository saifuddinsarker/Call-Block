package com.cybernetikz.call.block;

import java.util.ArrayList;




import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

public class MainActivity extends Activity {
	
	private GridviewAdapter mAdapter;	
	private ArrayList<String> listChannelName;
	private ArrayList<Integer> listChannelIcon;
	private GridView gridView; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        String locale = this.getResources().getConfiguration().locale.getCountry();
        Log.i("Country Code",locale);
        prepareList();
        
        TextView TV = (TextView)findViewById(R.id.homeBottom);

	     Spannable WordtoSpan = new SpannableString("Developed By CyberNetikz");        

	     WordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(244,193,110)), 13, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

	     TV.setText(WordtoSpan);
	     
        
        mAdapter = new GridviewAdapter(this,listChannelName, listChannelIcon);
        gridView = (GridView) findViewById(R.id.menuIcon);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent();	
				switch (position) {
					case 0:
		    			intent.setClass(MainActivity.this,AddBlock.class);
		    			startActivity(intent);
						break;
					case 1:
						intent.setClass(MainActivity.this,List.class);
		    			startActivity(intent);
						break;
					case 2:
						intent.setClass(MainActivity.this,CallLog.class);
		    			startActivity(intent);
						break;
					case 3:
						intent.setClass(MainActivity.this,SmsLog.class);
		    			startActivity(intent);
						break;
					case 4:
						intent.setClass(MainActivity.this,AboutBlock.class);
		    			startActivity(intent);
						break;
					case 5:
						intent.setClass(MainActivity.this,PrivacyBlock.class);
		    			startActivity(intent);
						break;
					default:
						break;
					}
								
			}
		});
    }
    
    
    
    
    
    
    public void prepareList()
    {
    	listChannelName = new ArrayList<String>();
    	  
    	  listChannelName.add("Add");
    	  listChannelName.add("List");
    	  listChannelName.add("Call Log");
    	  listChannelName.add("SMS Log");
    	  listChannelName.add("About Us");
    	  listChannelName.add("Terms of Use");
    	  
    	  
          
    	  listChannelIcon = new ArrayList<Integer>();
          
          listChannelIcon.add(R.drawable.add_button);
          listChannelIcon.add(R.drawable.list_button);
          listChannelIcon.add(R.drawable.log_button);
          listChannelIcon.add(R.drawable.settings_button);
          listChannelIcon.add(R.drawable.about_us_button);
          listChannelIcon.add(R.drawable.privacy_button);
          
        
    }
    
    
 public void onClick(View v) {
    	
    	Intent viewIntent =
    	          new Intent("android.intent.action.VIEW",
    	            Uri.parse("http://www.cybernetikz.com/"));
    	          startActivity(viewIntent);

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
     

	 Intent startMain = new Intent(Intent.ACTION_MAIN);
	 startMain.addCategory(Intent.CATEGORY_HOME);
	 startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 startActivity(startMain);
     return;
 }

    
}
