package com.becram.wirelessnakshav1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SQL extends Activity{
	
	public static final String KEY_ROWID="_id";
	public static final String KEY_DEVICEID="Device_id";
	public static final String KEY_CARRIER="service_provider";
	public static final String KEY_TIMEDATE="Time_instance";
	//public static final String KEY_DATEID="date";
	public static final String KEY_LATID="latitude";
	public static final String KEY_LONGID="longitude";
	public static final  String KEY_ASU="Strength";
	public static final  String KEY_AVAILABLEWIFI="wifis";
	public static final  String KEY_PUSHTIME="pushtime";
	
	
	
	private static final String DATABASE_NAME="Signal_data";
	private static final String DATABASE_TABLE="DataTable";
	private static final int DATABASE_VERSION=1;
	public static int row=1;
	int LastRowNo=5;
	int RowNo=2;
	
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	HttpPost httppost;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	HttpResponse response;
	Cursor c;
	public static String filename="myfile";
	
	
	
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
			            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
			            KEY_DEVICEID +" TEXT NOT NULL, "+
			            KEY_CARRIER +" TEXT NOT NULL, "+
					    KEY_TIMEDATE +" TEXT NOT NULL, "+
					   	 KEY_LATID +" TEXT NOT NULL, " +
					    KEY_LONGID  +" TEXT NOT NULL, " +
					   	 KEY_ASU +" TEXT NOT NULL, " +
					   	KEY_AVAILABLEWIFI +" TEXT NOT NULL, "+
					   	KEY_PUSHTIME +" TEXT NOT NULL);"
					
					
					);
			
			
			
		
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
			onCreate(db);
			
		}
		
	}
	

	public SQL(Context c){
		ourContext=c;
	}
	
	
	public SQL open() throws SQLException {
		ourHelper =new DbHelper(ourContext);
		ourDatabase=ourHelper.getWritableDatabase(); 
		return this;
		
	}
	
	public void close(){
		ourHelper.close();
	}
	
	public long createEntry(String deviceId,String carrier,String time,String lat,String longi,String Strength,String wifis,String pushTime){
		
		ContentValues cv=new ContentValues();
		cv.put(KEY_DEVICEID, deviceId);
		cv.put(KEY_CARRIER, carrier);
		cv.put(KEY_TIMEDATE, time);
		cv.put(KEY_LATID, lat);
		cv.put(KEY_LONGID, longi);
		cv.put(KEY_ASU, Strength);
		cv.put(KEY_AVAILABLEWIFI, wifis);
		cv.put(KEY_PUSHTIME, pushTime);
		
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
		 
	}

	public int getLastRowId(){
		String[] column=new String[]{KEY_ROWID,KEY_DEVICEID,KEY_CARRIER,KEY_TIMEDATE,KEY_LATID,KEY_LONGID,KEY_ASU,KEY_AVAILABLEWIFI,KEY_PUSHTIME};
		
		
		 c=ourDatabase.query(DATABASE_TABLE, column, null, null, null, null, null);
		// int iRow=c.getColumnIndex(KEY_ROWID);
		 if(c!=null){
				c.moveToLast();
				
				int LastRowNo=c.getInt(0);
				
				//Toast.makeText(ourContext, Integer.toString(LastRowNo)+" "+"from function", Toast.LENGTH_LONG).show();
				return LastRowNo;
			     
			}
		     		
		return 0;  }
	


	public void getDataAndPost(final int rowValue) {
		// TODO Auto-generated method stub
		//Toast.makeText(ourContext, Integer.toString(MapFragmentDisplay.row), Toast.LENGTH_SHORT).show();
		String[] column=new String[]{KEY_ROWID,KEY_DEVICEID,KEY_CARRIER,KEY_TIMEDATE,KEY_LATID,KEY_LONGID,KEY_ASU,KEY_AVAILABLEWIFI,KEY_PUSHTIME};
		  
		 
		
		 c=ourDatabase.query(DATABASE_TABLE, column, KEY_ROWID+"="+rowValue, null, null, null, null);
		String result="";
		int iRow=c.getColumnIndex(KEY_ROWID);
		int iDID=c.getColumnIndex(KEY_DEVICEID);
		int iCarrier=c.getColumnIndex(KEY_CARRIER);
		
		int iTime=c.getColumnIndex(KEY_TIMEDATE);
		int iLat=c.getColumnIndex(KEY_LATID);
		int iLon=c.getColumnIndex(KEY_LONGID);
		int iASU=c.getColumnIndex(KEY_ASU);
		int iWifis=c.getColumnIndex(KEY_AVAILABLEWIFI);
		//int iTimeP=c.getColumnIndex(KEY_PUSHTIME);
		//RowNo=Integer.parseInt(c.getString(iRow));
		 
	//	 Toast.makeText(ourContext, Integer.toString(getLastRowId())+"  " +"last row", Toast.LENGTH_LONG).show();
		if(c!=null  ){
			c.moveToFirst();
			final String ID=c.getString(iDID);
			final String cellCarrier=c.getString(iCarrier);
			final String time=c.getString(iTime);
			final String latitude=c.getString(iLat);
			final String longitude=c.getString(iLon);
			final String ASU=c.getString(iASU);
			final String wifis=c.getString(iWifis);
			result=result +ID+" "+cellCarrier+" "+RowNo+" "+time+" "+latitude+" "+longitude+" "+ASU +Integer.toString(RowNo)+" "+Integer.toString(LastRowNo)+wifis+ "\n";
			//ourDatabase.delete(DATABASE_TABLE, KEY_ROWID+"="+row, null);
		    // row++;
		   // storevalue(row);
		    // int newrow=getstoreddata();
			// Toast.makeText(ourContext, Integer.toString(newrow), Toast.LENGTH_LONG).show();
			
			// final ProgressDialog p = new ProgressDialog(ourContext).show(ourContext,"Waiting for Server", "Accessing Server");
             Thread thread = new Thread()
             {
                 @SuppressWarnings("deprecation")
					@Override
                 public void run() {
                	 
             			boolean Didiit=true;
             			String PushT = (String) DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());
                      try{
                    	  
                     	 //String data="";
                     	 
                    	  
                          httpclient=new DefaultHttpClient();
                          
                          httppost= new HttpPost("http://www.wirelessnaksha.com/input.php"); // make sure the url is correct.
                          //add your data
                          nameValuePairs = new ArrayList<NameValuePair>(1);
                          
                          // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                          nameValuePairs.add(new BasicNameValuePair("DeviceId",ID));
                          nameValuePairs.add(new BasicNameValuePair("Carrier",cellCarrier));
                          
                          nameValuePairs.add(new BasicNameValuePair("Time",time));
                          nameValuePairs.add(new BasicNameValuePair("Latitude",latitude));
                          nameValuePairs.add(new BasicNameValuePair("Longitude",longitude));// $Edittext_value = $_POST['Edittext_value'];
                          nameValuePairs.add(new BasicNameValuePair("SigStrength",ASU));
                          nameValuePairs.add(new BasicNameValuePair("WifiAvailable",wifis));
                          nameValuePairs.add(new BasicNameValuePair("PushTime",PushT));
                          httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                          //Execute HTTP Post Request
                          response=httpclient.execute(httppost);
                          Log.d("Exception raised","pusgeee");  
                          ResponseHandler<String> responseHandler = new BasicResponseHandler();
                          final String response = httpclient.execute(httppost, responseHandler);
                         // System.out.println("Response : " + response);
                         // row++;
                      //	Toast.makeText(ourContext, "Http pushed", Toast.LENGTH_LONG).show();

                          runOnUiThread(new Runnable() {
                                 public void run() {
                               //      p.dismiss();
                                    Log.d("response","right");
                                    
                                 }
                             });
                    	  }
                      catch(Exception e){
                    	  Log.d("Exception raised","right");  
                 		// Toast.makeText(ourContext, e.toString()+"No Push", Toast.LENGTH_LONG).show();

                    	  
                      }
                          runOnUiThread(new Runnable() {
                             public void run() {
                               //  p.dismiss();
                             }
                         });
                         // System.out.println("Exception : " + e.getMessage());
                      
                 }
             };
			
             thread.start();
		}
		
		
	}
	
	
	
	public String getDataForEmail(){
		
		String[] column=new String[]{KEY_ROWID,KEY_DEVICEID,KEY_CARRIER,KEY_TIMEDATE,KEY_LATID,KEY_LONGID,KEY_ASU,KEY_AVAILABLEWIFI,KEY_PUSHTIME};
		  
		 
		
		 c=ourDatabase.query(DATABASE_TABLE, column, null, null, null, null, null);
		String result="";
		int iRow=c.getColumnIndex(KEY_ROWID);
		int iDID=c.getColumnIndex(KEY_DEVICEID);
		int iCarrier=c.getColumnIndex(KEY_CARRIER);
		
		int iTime=c.getColumnIndex(KEY_TIMEDATE);
		int iLat=c.getColumnIndex(KEY_LATID);
		int iLon=c.getColumnIndex(KEY_LONGID);
		int iASU=c.getColumnIndex(KEY_ASU);
		int iWifis=c.getColumnIndex(KEY_AVAILABLEWIFI);
		for(c.moveToFirst(); !c.isAfterLast();c.moveToNext()){
			
			result=result+" "+c.getString(iRow)+" "+c.getString(iDID)+" "+c.getString(iCarrier)+" "+c.getString(iTime)+" "+c.getString(iLat)+" "+c.getString(iLon)+" "+
					c.getString(iASU)+" "+c.getString(iWifis)+"\n";
		}
		
		
		
		return result;
	}
	
	

	


              
	
	    
	}


	

	    
