package com.cybernetikz.call.block;

import java.util.Calendar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;



public class SMSReceiver extends BroadcastReceiver {
	private String number;
	private String body = "";
	
	private String bnumber;
	private String bname ;
	
	public DBAdapter db;

	public Cursor c;


	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		Object messages[] = (Object[]) bundle.get("pdus");
		SmsMessage smsMessage[] = new SmsMessage[messages.length];
		
		

		for (int n = 0; n < messages.length; n++) {
			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
			number = smsMessage[n].getOriginatingAddress();
			body += smsMessage[n].getDisplayMessageBody();
		}
		
		db = new DBAdapter(context); 
	    db.open();
	    c = db.getALLBlockByText();

		/*if(c.getCount()>0){
			if (c.moveToFirst())
		    {
		        do {          
		        	bnumber=c.getString(1);
		        	bname=c.getString(2);
		        } while (c.moveToNext());
		    }
			blockMessage(context);
			
		}*/
		
		
		if (c.moveToFirst())
	    {
	        do { 
	        	String num=c.getString(1);
	        	 Log.i("Block Sms:", num+"<-" );
	        	//num = num.replace("+", "");
	        	//num = num.replace("-", "");
	        	if(PhoneNumberUtils.compare(num,number)){
	        		bnumber=c.getString(1);
		        	bname=c.getString(2);
		        	blockMessage(context);
		        	 Log.i("Blocked success Sms:", num+"<-" );
	        	}
	        	
	        } while (c.moveToNext());
	    }
		
	    db.close();
		
	}

	private void blockMessage(Context context) {
		abortBroadcast();
		db = new DBAdapter(context); 
	    db.open();
	    String time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
		db.insertLog(bnumber, bname, 1, 0, body, time);
		db.close();
	}
}