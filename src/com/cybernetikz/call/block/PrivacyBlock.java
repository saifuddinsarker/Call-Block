package com.cybernetikz.call.block;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class PrivacyBlock extends Activity{
	
	 public void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.privacy_about);
	     
	     TextView TV = (TextView)findViewById(R.id.homeBottom);

	     Spannable WordtoSpan = new SpannableString("Developed By CyberNetikz");        

	     WordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(244,193,110)), 13, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

	     TV.setText(WordtoSpan);
	     
	     
	     TextView TT = (TextView)findViewById(R.id.title);
	     TT.setText("Terms Of Use");
	     
	     WebView CT = (WebView)findViewById(R.id.webView1);
	     CT.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	     CT.getSettings().setJavaScriptEnabled(true);
	     CT.loadUrl("file:///android_asset/privacy.html");
	 }
	
	 public void goHome(View v) {
	    	
		 Intent intent = new Intent();	
		 
		 intent.setClass(PrivacyBlock.this,MainActivity.class);
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
		 
		 intent.setClass(PrivacyBlock.this,MainActivity.class);
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
