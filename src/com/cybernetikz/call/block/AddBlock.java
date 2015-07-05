package com.cybernetikz.call.block;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddBlock extends Activity {
	
	 
	 CheckBox  text_chk;
	 CheckBox  call_chk;
	 
	 int text_block=1;
	 int call_block=1;
	
	 ProgressDialog loadDialog;
	 Button save;
	 Button back;
	 
	 private boolean isDefaultSelection = true;
	 
	 public EditText phone_number;
	 public EditText name;
	
	 public ArrayList<String> dname = new ArrayList<String>();
	 public ArrayList<String> phone = new ArrayList<String>();
	 Spinner phoneSpinner;
	 
	 public DBAdapter db;
	  
	 public Cursor c;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_block);
        
        TextView TV = (TextView)findViewById(R.id.homeBottom);

	     Spannable WordtoSpan = new SpannableString("Developed By CyberNetikz");        

	     WordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(244,193,110)), 13, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

	     TV.setText(WordtoSpan);
	     
        
        phone_number = (EditText)findViewById(R.id.phone);
        name = (EditText)findViewById(R.id.name);
        
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        
        Log.i("Country IsoCode:",tm.getSimCountryIso());
        
        text_chk=(CheckBox)findViewById(R.id.blockText);
        call_chk=(CheckBox)findViewById(R.id.blockCall);
        
        // populate Contact Spinner
	    dname.add(0,"Select from Contact");
        phone.add(0,"");
        
        PrepareContacts();
        
      
        
        phoneSpinner = (Spinner)findViewById(R.id.phoneSp);
        
        phoneSpinner.setAdapter(new MyAdapter(this, R.layout.spinner, dname));
        
        phoneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {          
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              
            	if(isDefaultSelection) {    
            		isDefaultSelection=false;
            	} else {
            		 int itemPosition = phoneSpinner.getSelectedItemPosition();
            		 phone_number.setText(phone.get(itemPosition));
            		 name.setText(dname.get(itemPosition));  		 
	                // Toast.makeText(AddSchedule.this, "Slected: " + itemPosition + " : ->"+dname.get(itemPosition)+" num:"+phone.get(itemPosition), Toast.LENGTH_SHORT).show();	
            	}
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //User selected same item. Nothing to do.
            }
        });
        
        save = (Button)findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					int error=0;
		    		String number=phone_number.getText().toString();
		    		String full_name=name.getText().toString();
		    		if(number.equals("") || number.equals(null)){
		    			error++;
		    			final AlertDialog alertDialog = new AlertDialog.Builder(AddBlock.this).create();
						
					     alertDialog.setTitle("Notice");
					        // Setting Dialog Message
					     alertDialog.setMessage("Please Choose or Enter Phone Number (e.g. +123877455.) ");
					        // Setting Icon to Dialog
					     alertDialog.setIcon(R.drawable.ic_launcher);
					        // Setting OK Button
					      alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int which) {
					         	       alertDialog.dismiss();
					                }
					       });			     
					       alertDialog.show();
		    			
		    		}
		    		
		    		
		    		
		    		if (text_chk.isChecked()) {
		    			text_block=1;
					} else{
						text_block=0;		
					}
					
					
					if (call_chk.isChecked()) {
						call_block=1;
					} else{
						call_block=0;		
					}
		    		
					
					if(full_name.equals("")){
				    	full_name="Unknown";
				    }
		    		
		    		
		    		if(error==0){
		    			int insert;
					    db = new DBAdapter(AddBlock.this); 
					    db.open();
					    insert=db.insertBlock(number, full_name, call_block, text_block);
					    Log.i("Mishu-Call-block","Inserting -result="+insert);
					    db.close();
					    
					    final AlertDialog alertDialog = new AlertDialog.Builder(AddBlock.this).create();
						
					    alertDialog.setTitle("Information");
					        // Setting Dialog Message
					    alertDialog.setMessage("Number Added to Blocklist");
					        // Setting Icon to Dialog
					    alertDialog.setIcon(R.drawable.top_call_block);
					        // Setting OK Button
					    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"Go To Block List", new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int which) {
					                	Intent intent = new Intent();
					         	       alertDialog.dismiss();
					         	       intent.setClass(AddBlock.this,List.class);
						    		   startActivity(intent);
						         	      
					                }
					    });	
					    
					    alertDialog.show();
					  
		    		}
				}
		 });
		// Save button
        
        
	        
	 }
	
	private void PrepareContacts() {
    	
    	loadDialog = new ProgressDialog(AddBlock.this);
    	loadDialog.setMessage("Please Wait, Loading Contacts");
    	loadDialog.setIndeterminate(true);
    	loadDialog.setCancelable(false);
    	loadDialog.show();
    	//Start an ASync Thread to take care of Downloading Feed
    	new PrepareContacts().execute("Do");
    }
    
    private class PrepareContacts extends AsyncTask<String, Integer, String> {
		
		@Override
		protected String doInBackground(String... url) {
			try{

		    	ContentResolver cr = getContentResolver();
		        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
		                null, null, null, null);
		        int c=0;
		        if (cur.getCount() > 0) {
		        	while (cur.moveToNext()) {
		        		String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		        		String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		        		if (Integer.parseInt(cur.getString(
		                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
		                     Cursor pCur = cr.query(
		                    		 ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
		                    		 null, 
		                    		 ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
		                    		 new String[]{id}, null);
		                     while (pCur.moveToNext()) {
		                    	 String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		                    	 dname.add(name);
		                    	 phone.add(phoneNo);
		                    	 c++;
		                    	
		                     } 
		      	        pCur.close();
		      	    }
		          
		          }
		        }
		        
		        Log.i("Phone Record Found:","Total ="+c);
			 
			}catch(Exception e){
				Log.e("Future", "exception", e);
				return "Interrupted";
			}
	          
			return "Done"; 
		}
		
		@Override
	    protected void onPostExecute(String result) {
		    Log.i("Cn-on-Asyc : ",result);
			loadDialog.dismiss();
			
	    }
	}
    
    
  //spinner for contact
	 public class MyAdapter extends ArrayAdapter<String>
	    {

	        public MyAdapter(Context context, int textViewResourceId,
	        		ArrayList<String> objects) {
	              super(context, textViewResourceId, objects);
	              // TODO Auto-generated constructor stub
	        }
	        @Override
	        public View getDropDownView(int position, View convertView,ViewGroup parent) {
	            return getCustomView(position, convertView, parent);
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            return getCustomView(position, convertView, parent);
	        }

	        public View getCustomView(int position, View convertView, ViewGroup parent) {
	            LayoutInflater inflater=getLayoutInflater();
	            View row=inflater.inflate(R.layout.spinner, parent, false);
	            TextView label=(TextView)row.findViewById(R.id.textView1);
	            label.setText(dname.get(position));

	            TextView sub=(TextView)row.findViewById(R.id.textView2);
	            sub.setText(phone.get(position));

	         
	            return row;
	            }
	       
	   }
	 
	 public void ContactonClick(View v) {
	    	
		   phoneSpinner.performClick();
		
		  
	 }
	 public void onClick(View v) {
	    	
	    	Intent viewIntent =
	    	          new Intent("android.intent.action.VIEW",
	    	            Uri.parse("http://www.cybernetikz.com/"));
	    	          startActivity(viewIntent);

	      } 
	 public void goHome(View v) {
	    	
		 Intent intent = new Intent();	
		 
		 intent.setClass(AddBlock.this,MainActivity.class);
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
		 
		 	intent.setClass(AddBlock.this,MainActivity.class);
			startActivity(intent);

	 }
	 
	 @Override
	 public Object onRetainNonConfigurationInstance() {
	    return null;
	 } 

}
