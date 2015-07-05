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


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


@SuppressLint("Registered")
public class DBAdapter extends Activity  
{
    public static final String KEY_BL_ID = "id";
    public static final String KEY_BL_NUMBER = "number";
    public static final String KEY_BL_NAME = "name";
    public static final String KEY_BL_CALL = "call";
    public static final String KEY_BL_TEXT = "text";
   
    
    public static final String KEY_LOG_ID = "id";
    public static final String KEY_LOG_NUMBER = "number";
    public static final String KEY_LOG_NAME = "name";
    public static final String KEY_LOG_TEXT_STATUS = "text_status";
    public static final String KEY_LOG_CALL_STATUS = "call_status";
    public static final String KEY_LOG_TEXT = "text";
    public static final String KEY_LOG_TIME = "time";
    
    
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "BlockCallSms";
    private static final String TABLE_BLOCK = "block";
    private static final String TABLE_LOG = "log";

    private static final int DATABASE_VERSION = 1;
    
    
    private static final String DATABASE_CREATE_BLOCK =
            "create table "+TABLE_BLOCK+" (id integer primary key autoincrement, "
            +KEY_BL_NUMBER+" text not null," 
            +KEY_BL_NAME+" text not null," 
            +KEY_BL_CALL +" integer default 0," 
            +KEY_BL_TEXT+" integer default 0);";
    
    private static final String DATABASE_CREATE_LOG =
            "create table  "+TABLE_LOG+" (id integer primary key autoincrement, "
            +KEY_LOG_NUMBER+" text not null,"
            +KEY_LOG_NAME+" text not null,"
            +KEY_LOG_TEXT_STATUS+" integer default 0," 
            +KEY_LOG_CALL_STATUS +" integer default 0,"
            +KEY_LOG_TEXT+" text not null,"
            +KEY_LOG_TIME+" text not null);";
 

   private Context context; 
   
   

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
   
    
    
    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
       
        
    }
            
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        
  	
    	DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
         
         }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
	        
        	
			db.execSQL(DATABASE_CREATE_BLOCK);
		
			   
			db.execSQL(DATABASE_CREATE_LOG);
		
            
        }     

		@Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
		
		
		
		
    }  
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    

	//---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    

    
    
    //---retrieves a All Block---
    public Cursor getALLBlock() throws SQLException 
    {
        Cursor mCursor =
                db.query(TABLE_BLOCK, new String[] {
                		KEY_BL_ID,
                		KEY_BL_NUMBER,
                		KEY_BL_NAME,
                		KEY_BL_CALL,
                		KEY_BL_TEXT
                		}, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
  //---retrieves a All Block By Call---
    public Cursor getALLBlockByCall() throws SQLException 
    {
        Cursor mCursor =
                db.query(TABLE_BLOCK, new String[] {
                		KEY_BL_ID,
                		KEY_BL_NUMBER,
                		KEY_BL_NAME
                		}, 
                		KEY_BL_CALL+"=1",
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    
  //---retrieves a All Block By Call---
    public Cursor getALLBlockByCallByNumber(String number) throws SQLException 
    {
        Cursor mCursor =
                db.query(TABLE_BLOCK, new String[] {
                		KEY_BL_ID,
                		KEY_BL_NUMBER,
                		KEY_BL_NAME
                		}, 
                		KEY_BL_CALL+"=1 and ("+KEY_BL_NUMBER+"='"+number+"' or "+KEY_BL_NUMBER+" LIKE '%"+number+"')",
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
  //---retrieves a All Block By Text---
    public Cursor getALLBlockByText() throws SQLException 
    {
        Cursor mCursor =
                db.query(TABLE_BLOCK, new String[] {
                		KEY_BL_ID,
                		KEY_BL_NUMBER,
                		KEY_BL_NAME
                		}, 
                		KEY_BL_TEXT+"=1",
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
  //---retrieves a All Block By Text and number---
    public Cursor getALLBlockByTextByNumber(String number) throws SQLException 
    {
        Cursor mCursor =
                db.query(TABLE_BLOCK, new String[] {
                		KEY_BL_ID,
                		KEY_BL_NUMBER,
                		KEY_BL_NAME
                		}, 
                		KEY_BL_TEXT+"=1  and "+KEY_BL_NUMBER+"='"+number+"'",
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    
    
    //INSERTING DATA ON BLOCK TABLE
    public int insertBlock(String number,String name,int callStatus,int smsStatus)
	 {
		 
		 ContentValues initialValues = new ContentValues();
         initialValues.put(KEY_BL_NUMBER, number);
         initialValues.put(KEY_BL_NAME, name);
		 initialValues.put(KEY_BL_CALL, callStatus);
		 initialValues.put(KEY_BL_TEXT, smsStatus);
		 return (int)db.insert(TABLE_BLOCK, null, initialValues);
	
		 
	 }
    //UPDATING DATA ON BLOCK TABLE
    public int updateBlock(int id,String number,String name,int callStatus,int smsStatus)
	 {
    	 String strFilter = KEY_BL_ID + "=" + id;
		 ContentValues initialValues = new ContentValues();
		 initialValues.put(KEY_BL_NUMBER, number);
         initialValues.put(KEY_BL_NAME, name);
		 initialValues.put(KEY_BL_CALL, callStatus);
		 initialValues.put(KEY_BL_TEXT, smsStatus);
		 return db.update(TABLE_BLOCK,initialValues, strFilter, null);
		 
	 }
    //DELETING DATA ON BLOCK TABLE
    public int deleteBlock(int id)
	 {
   	     String strFilter = KEY_BL_ID + "=" + id;
   	     Log.i("Delete Block","->"+id);
   	     return db.delete(TABLE_BLOCK,strFilter,null);
		// return db.update(TABLE_SCHEDULE,initialValues, strFilter, null);
		 
	 }
    
    //DELETING DATA ON BLOCK TABLE
    public void deleteAllBlock()
	 {
   	     
   	      db.delete(TABLE_BLOCK,"",null);
		// return db.update(TABLE_SCHEDULE,initialValues, strFilter, null);
		 
	 }
    
    
    
    //GEtting ALL  LOG
    public Cursor getAllLog() throws SQLException 
    {
        Cursor mCursor =
                db.query(TABLE_LOG, new String[] {
                		KEY_LOG_ID,
                	    KEY_LOG_NUMBER,
                	    KEY_LOG_NAME,
                	    KEY_LOG_TEXT_STATUS,
                	    KEY_LOG_CALL_STATUS,
                	    KEY_LOG_TEXT,
                	    KEY_LOG_TIME,
                		},
                		null,
                		null, 
                		KEY_LOG_NUMBER,
                		KEY_LOG_CALL_STATUS +"=1", 
                		KEY_LOG_ID +" DESC");
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    //GEtting ALL  LOG
    public Cursor getAllLogS() throws SQLException 
    {
        Cursor mCursor =
                db.query(TABLE_LOG, new String[] {
                		KEY_LOG_ID,
                	    KEY_LOG_NUMBER,
                	    KEY_LOG_NAME,
                	    KEY_LOG_TEXT_STATUS,
                	    KEY_LOG_CALL_STATUS,
                	    KEY_LOG_TEXT,
                	    KEY_LOG_TIME,
                		},
                		KEY_LOG_TEXT_STATUS +"=1",
                		null, 
                		null,
                		null, 
                		KEY_LOG_ID +" DESC");
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor getAllLogByCall() throws SQLException 
    {
        Cursor mCursor =
                db.query(TABLE_LOG, new String[] {
                		KEY_LOG_ID,
                	    KEY_LOG_NUMBER,
                	    KEY_LOG_NAME,
                	    KEY_LOG_TEXT_STATUS,
                	    KEY_LOG_CALL_STATUS,
                	    KEY_LOG_TEXT,
                	    KEY_LOG_TIME,
                		},
                		KEY_LOG_CALL_STATUS +"=1",
                		null, 
                		null, 
                		null, 
                		KEY_LOG_ID +" DESC");
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor getAllLogByText() throws SQLException 
    {
        Cursor mCursor =
                db.query(TABLE_LOG, new String[] {
                		KEY_LOG_ID,
                	    KEY_LOG_NUMBER,
                	    KEY_LOG_NAME,
                	    KEY_LOG_TEXT_STATUS,
                	    KEY_LOG_CALL_STATUS,
                	    KEY_LOG_TEXT,
                	    KEY_LOG_TIME,
                		},
                		KEY_LOG_TEXT_STATUS +"=1",
                		null, 
                		null, 
                		null, 
                		KEY_LOG_ID +" DESC");
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    

   
    
  //INSERTING DATA ON LOG TABLE
    public int insertLog(String number,String name,int textStatus,int callStatus,String text,String time)
	 {		 
		
    	 ContentValues initialValues = new ContentValues();
         initialValues.put(KEY_LOG_NUMBER, number);
         initialValues.put(KEY_LOG_NAME, name);
         initialValues.put(KEY_LOG_TEXT_STATUS, textStatus);
         initialValues.put(KEY_LOG_CALL_STATUS, callStatus);
         initialValues.put(KEY_LOG_TEXT, text);
         initialValues.put(KEY_LOG_TIME, time);

		 return (int)db.insert(TABLE_LOG, null, initialValues);
	
		 
	 }

    //DELETING DATA ON TEMPLATE TABLE
    public int deleteLog(String id)
	 {
   	     String strFilter = KEY_BL_NUMBER + "='" + id +"'";
   	     return db.delete(TABLE_LOG,strFilter,null);
		
	 }
    
    public int deleteLogS(int id)
	 {
  	     String strFilter = KEY_LOG_ID + "=" + id;
  	     return db.delete(TABLE_LOG,strFilter,null);
		
	 }
    
    //DELETING DATA ON TEMPLATE TABLE
    public void deleteAllLog(int a)
	 {
   	    
   	    if(a==1){ 
   	    	db.delete(TABLE_LOG, KEY_LOG_CALL_STATUS+"=?",
   	             new String[] {"1"});
   	    	Log.i("Block List Total:", "DElete Call log" );
   	    	//db.delete(TABLE_LOG,"",null);
   	    } else if(a==2) {
   	    	db.delete(TABLE_LOG, KEY_LOG_TEXT_STATUS+"=?",
  	             new String[] {"1"});
   	    	Log.i("Block List Total:", "DElete SMS log" );
   	    }
	 }
    
    
}
