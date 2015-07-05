package com.cybernetikz.call.block;

import java.lang.reflect.Method;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.telephony.PhoneNumberUtils;
import com.android.internal.telephony.ITelephony;

public class PhoneCallStateListener extends PhoneStateListener {    

private Context context;

public String name;

public DBAdapter db;

public Cursor c;


public PhoneCallStateListener(Context context){
    this.context = context;
}
@Override
public void onCallStateChanged(int state, String incomingNumber) {  
    @SuppressWarnings("unused")
	SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);
    int flag=0;
    switch (state) {
        case TelephonyManager.CALL_STATE_RINGING:       	
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE); 
            //Turn ON the mute
            audioManager.setStreamMute(AudioManager.STREAM_RING, true);    
            db = new DBAdapter(context); 
		    db.open();
		    c = db.getALLBlockByCall();
		    Log.i("Block List Total:", c.getCount()+"<-" );
		    
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                @SuppressWarnings("rawtypes")
				Class clazz = Class.forName(telephonyManager.getClass().getName());
                @SuppressWarnings("unchecked")
				Method method = clazz.getDeclaredMethod("getITelephony");
                method.setAccessible(true);
                ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);     
                //Checking incoming call number
                Log.i("Cn Call block debug","Incoming Number->"+incomingNumber);
                if (c.moveToFirst())
    		    {
    		        do { 
    		        	String num=c.getString(1);
    		        	//num = num.replace("+", "");
    		        	//num = num.replace("-", "");
    		        	if(PhoneNumberUtils.compare(num,incomingNumber)){
    		        		 //telephonyService.silenceRinger();//Security exception problem
    	                     telephonyService = (ITelephony) method.invoke(telephonyManager);
    	                     telephonyService.endCall();
    	         		     name=c.getString(2);
    	                     String time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    	                     int insert=0;
    	                     if(flag==0){
    	                    	 flag++;
    	                    	 insert=db.insertLog(incomingNumber, name, 0, 1, "Call Blocked", time);
    	                     }
    	                     Log.i("Cn Call block get Blocked","->"+insert+"-"+incomingNumber);
    	                     Thread.sleep(100);
    	                     String queryString="NUMBER="+incomingNumber; 
    	                     context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,queryString,null);
    		        	}
    		        	
    		        } while (c.moveToNext());
    		    }
                
                //incomingNumber.equalsIgnoreCase(block_number)
                /*if (c.getCount()>0) {
                    //telephonyService.silenceRinger();//Security exception problem
                     telephonyService = (ITelephony) method.invoke(telephonyManager);
                    // telephonyService.silenceRinger();
                    // Log.i("Cn Call block get Blocked","->"+incomingNumber);
                     telephonyService.endCall();
                     if (c.moveToFirst())
	         		 {
         		        do {          
         		        	 name=c.getString(2);
         		        } while (c.moveToNext());
	         		 }
                     
                     String time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                     int insert=db.insertLog(incomingNumber, name, 0, 1, "Call Blocked", time);
                     Log.i("Cn Call block get Blocked","->"+insert+"-"+incomingNumber);
                     Thread.sleep(2000);
                     String queryString="NUMBER="+incomingNumber; 
                     context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,queryString,null);
                    
                }*/
            } catch (Exception e) {
                
            	 Log.i("Cn Call block debug get Exception","->"+e.toString());
            	//Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
            db.close();
            //Turn OFF the mute     
            audioManager.setStreamMute(AudioManager.STREAM_RING, false);
            break;
        case TelephonyManager.CALL_STATE_IDLE:
        	String queryString="NUMBER="+incomingNumber; 
        	try{
        		context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,queryString,null);
        	}  catch (Exception e) {
        		
        	}
            break;
    

    }
    super.onCallStateChanged(state, incomingNumber);
}}