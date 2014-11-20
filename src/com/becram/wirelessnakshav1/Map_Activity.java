package com.becram.wirelessnakshav1;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;




import com.becram.wirelessnakshav1.SQL;
import com.becram.wirelessnakshav1.Map_Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.R.integer;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Map_Activity extends FragmentActivity implements LocationListener {
	
	
	public int ASU=0;
	  String str;
	  static String filename="MySharedString";
	   
	   float newlat=0;
	   float newlng=0;
	  String SignalType="" ;
	  TelephonyManager Tel ;
	  TextView tv;
	  public static SQL entry;
	  public static int wifi=0;
	  String locLat="not found gps";
	  
	  TextView DBdata;
	  
	  HttpPost httppost;
	  StringBuffer buffer;
	  HttpResponse response;
	  HttpClient httpclient;
	  List<NameValuePair> nameValuePairs;
	  ProgressDialog p;
	  
	  SharedPreferences app_preferences;
	 SharedPreferences lat_preferences;
	 SharedPreferences long_preferences;
	  Button pushBUT;
	  Button pushDB;
	  Button sqlPush;
	  public static String Wifis = "11"; 
	  public static  String DeviceID = "12";
	  public static String carrierName = "122";
	  public static String wifiavailable="no wifi";
	  WifiManager mainWifi;
	    WifiReceiver receiverWifi;
	    List<ScanResult> wifiList;
	    StringBuilder sb = new StringBuilder();
	   public static  TextView wifi12;
	 //  private static final String LOG_TAG = "CheckNetworkStatus";
	   private NetworkChangeReceiver receiver;
	   private boolean isConnected = false;
	  
	  
	  
	  String towers;
	  SQL sql;
	  private static final String LOG_TAG = "CheckNetworkStatus";
		
	  

	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	@SuppressWarnings("unused")
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9002;
	public static GoogleMap mMap;

	
	private static final float DEFAULTZOOM = 10;
	@SuppressWarnings("unused")
	private static final String LOGTAG = "Maps";
	private LocationManager service;
	int count=1;
	public static Location location;
	public static String bestProvider ;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		
	     Criteria criteria = new Criteria();
	     
		service= (LocationManager) getSystemService(LOCATION_SERVICE);
		bestProvider = service.getBestProvider(criteria, true);
		   service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000L,  0.5f, this);
		   location=service.getLastKnownLocation(bestProvider);
	   // location = service.getLastKnownLocation(bestProvider);
	    DBdata =(TextView) findViewById(R.id.textView1);
	    /*newlat=(float) location.getLatitude();
	    newlng=(float) location.getLongitude();*/
	    sql=new SQL(this);
	   
	    Log.v("after lat", "stopped");
	   // DeviceID=Tel.getDeviceId();
		//carrierName=Tel.getNetworkOperatorName();
		// Wifis=sb.toString();
	    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		  receiver = new NetworkChangeReceiver();
		  registerReceiver(receiver, filter);
		  
		   sql.open();
	      sql.createEntry("sqltest","sqltest","sqltest","sqltest","sqltest","sqltest","sqltest","sqltest");
	      sql.close();
		boolean isGPS = service.isProviderEnabled (LocationManager.GPS_PROVIDER);
	    //SQL musql=new SQL(this);
	   // row=musql.getstoreddata();
		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	       receiverWifi = new WifiReceiver();
	       registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	       mainWifi.startScan();
		 
	 //     wifi12=(TextView) findViewById(R.id.textView3);
		app_preferences =  PreferenceManager.getDefaultSharedPreferences(this);

			    count = app_preferences.getInt("count", 1);
			    SharedPreferences.Editor editor = app_preferences.edit();
			    editor.putInt("count", count);
			    editor.commit();
		/*	    
         lat_preferences =  PreferenceManager.getDefaultSharedPreferences(this);

			    newlat = lat_preferences.getFloat("lat", 27);
			    SharedPreferences.Editor editorLat = lat_preferences.edit();
			    editorLat.putFloat("lat", newlat);
			    editorLat.commit();
	    long_preferences =  PreferenceManager.getDefaultSharedPreferences(this);

			    newlng = long_preferences.getFloat("long", 85);
			    SharedPreferences.Editor editorlong = long_preferences.edit();
			    editorlong.putFloat("long", newlng);
			    editorlong.commit();*/
			    Log.v("after lat", "stopped2");	   
		if (isGPS==false){
			
			startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
		}
           if (servicesOK()) {
			
			// initMap();
		    //  mMap.setMyLocationEnabled(true);
			   pushDB=(Button) findViewById(R.id.DBbut);

				pushDB.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						// TODO Auto-generated method stub
						Intent gohome=new Intent(getApplicationContext(), HomeActivity.class);
						startActivity(gohome);
						
					}
				});
				pushBUT=(Button) findViewById(R.id.MyButton);
				pushBUT.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                 
		                final ProgressDialog p = new ProgressDialog(v.getContext()).show(v.getContext(),"Please Wait!!", "Pushing to Server");
		                Thread thread = new Thread()
		                {
		                    @SuppressWarnings("deprecation")
							@Override
		                    public void run() {
		                    	
		                         try{
		                        	 sql.open();
		                        	 if(isConnected){
		                        	 if(count<sql.getLastRowId() ){
		                        	for(count=1;count<sql.getLastRowId();count++){
		                        		
		                        		sql.getDataAndPost(count);
		                        		
		                        	}
		                        	 }
		                        	 }
		                        	 else{
		                        		 Toast.makeText(getBaseContext(),"No Internet ", Toast.LENGTH_LONG).show();
		                        	 }
		                        	 
		                             runOnUiThread(new Runnable() {
		                                    public void run() {
		                                        p.dismiss();
				                  	    		 DBdata.setText(Integer.toString(count)+" " +"out of" +" "+Integer.toString(sql.getLastRowId())+" " +"Data Pushed.Thank you for your contribution");
				                  	    		
				                  	    		 sql.close();
		                                    }
		                                });
		                             
		                         }catch(final Exception e){
		                               
		                             runOnUiThread(new Runnable() {
		                                public void run() {
		                                    p.dismiss();
		                                    DBdata.setText("No Data");

		                                }
		                            });
		                             System.out.println("Exception : " + e.getMessage());
		                         }
		                    }
		                };
		 
		                thread.start();
		                 
		                
		            }
		        });
				/*sqlPush=(Button) findViewById(R.id.sql);
				sqlPush.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						SqlPush();
					}
				});*/
				 
				//setHeader(getString(R.string.MapActivityTitle), true, true);
				/*ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton2);
				toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				        if (isChecked) {
				        	
				        	
				        	final Handler handler = new Handler();
				    		handler.postDelayed(new Runnable() {
				    		    public void run() {
				    		       SqlPush();
				    		   // notifyUs();
				    		      // new SqlAsyncTask().execute("nothing");
				    		        handler.postDelayed(this,5000); //now is every 2 minutes
				    		    }
				    					
				    		 },5000);
				        	 
				        }
				         else {
				            // The toggle is disabled
				        }
				    }
				});*/
				 Log.v("after lat", "stopped4");
			MyPhoneStateListener MyListener   = new MyPhoneStateListener();
		    TelephonyManager Tel = ( TelephonyManager )getSystemService(Context.TELEPHONY_SERVICE);
		    Tel.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		    Log.v("after lat", "stopped3");
		  // wifiavailable=sb.toString();
			  DeviceID=Tel.getDeviceId();
			  carrierName=Tel.getNetworkOperatorName();
		   if (initMap()) {
				mMap.setMyLocationEnabled(true);
			}
			else {
				Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
			}
		 }
		      else {
			       setContentView(R.layout.main);
		      }
     	
         /*  final Handler handler = new Handler();
   		handler.postDelayed(new Runnable() {
   		    public void run() {
   		    	
   		       SqlPush();
   		   // notifyUs();
   		      // new SqlAsyncTask().execute("nothing");
   		        handler.postDelayed(this,240000); //now is every 2 minutes
   		    }
   					
   		 }, 240000);*/
           
           
        /*  final Handler handler = new Handler();
   		handler.postDelayed(new Runnable() {
   		    public void run() {
   		       SqlPush();
   		   // notifyUs();
   		      // new SqlAsyncTask().execute("nothing");
   		        handler.postDelayed(this,5000); //now is every 2 minutes
   		    }
   					
   		 },5000);*/
		
	} // oncreate ends here
           
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiverWifi);
		super.onPause();
		SharedPreferences.Editor editor = app_preferences.edit();
	      editor.putInt("count", count);
	      editor.commit();
	    /*  SharedPreferences.Editor editorlat = lat_preferences.edit();
	      editorlat.putFloat("lat", newlat);
	      editorlat.commit();
	      SharedPreferences.Editor editorlong = long_preferences.edit();
	      editorlong.putFloat("long", newlng);
	      editorlong.commit();*/
	}
    
	private void setHeader(String string, boolean b, boolean c) {
		// TODO Auto-generated method stub
		ViewStub stub = (ViewStub) findViewById(R.id.vsHeader);
		View inflated = stub.inflate();
      
		TextView txtTitle = (TextView) inflated.findViewById(R.id.txtHeading);
		txtTitle.setText(string);
		
		Button btnHome = (Button) inflated.findViewById(R.id.btnHome);
		if(!b)
			btnHome.setVisibility(View.INVISIBLE);
		
		Button btnFeedback = (Button) inflated.findViewById(R.id.btnFeedback);
		if(!c)
			btnFeedback.setVisibility(View.INVISIBLE);
		
		
	}
	

	public class MyPhoneStateListener extends PhoneStateListener
	  {
	    /* Get the Signal strength from the provider, each time there is an update */
	    @Override
	    public void onSignalStrengthsChanged(SignalStrength signalStrength)
	    {
	    	
	       super.onSignalStrengthsChanged(signalStrength);
	       ASU =  signalStrength.getGsmSignalStrength();
	     /* if(isConnected){
	    	  do{
	    		  sql.open();
	    		  if(sql.getLastRowId()>2){
	    		  PushToServer();
	    		  sql.close();
	    		  Toast.makeText(getBaseContext(), "pushed to server", Toast.LENGTH_SHORT).show();
	    		  }
	    	  }while(isConnected);
	      }*/
	     
	      
	    }
	  }//phonestatelistener end here
	
	
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Map_Activity.location = location;
        //   SqlPush();
           if (location!=null){
                
                newlat= (float) location.getLatitude();
                newlng = (float) location.getLongitude();
               locLat  = String.valueOf(newlat)+","+String.valueOf(newlng);
              // SqlPush();
              // Toast.makeText(getBaseContext(),locLat , Toast.LENGTH_LONG).show();
               SqlPush();
            }
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean initMap() {
		if (mMap == null) {
			SupportMapFragment mapFrag =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			mMap = mapFrag.getMap();
			
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.6298, 85.5222)).title("Banepa").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.89599996, 85.14661113)).title("Battar").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.7756, 85.7133)).title("Chautara").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.9111, 84.8931)).title("Dhading").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.6188, 85.5532)).title("Dhulikhel").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.8603, 85.5667)).title("Dubochaur").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.8044, 84.8804)).title("Gajuri").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.728, 85.1484)).title("Khanikhola").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.9717, 85.9621)).title("Kodari").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.87708334, 85.89155558)).title("Last_Resort").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.8123, 84.8322)).title("Malekhu").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.7237, 85.5257)).title("Nagarkot").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.6542, 85.5077)).title("Nalla").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.5845, 85.513)).title("Panauti").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.6392, 85.4748)).title("Sanga").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.7616, 85.038)).title("Simile").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.9221, 85.1444)).title("Trishuli").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.8173, 85.1708)).title("Belkot (Arukharka)").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.8043, 84.9563)).title("Batase_Danda").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.8122, 84.7858)).title("Benighat").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.6239, 85.7549)).title("Bhumledanda").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.9113, 85.1418)).title("Bidur").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(28.1127, 85.2962)).title("Dhunche").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.8009, 84.7196)).title("Jogimara").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.8845, 84.9489)).title("Khalte (Transit)").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.5738, 85.5265)).title("Khopasi").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.8436, 84.8952)).title("Kumpurdanda").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.7902, 85.2654)).title("Mudhu Dhoka").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.6234, 85.5393)).title("Bakhundole").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.8371, 85.0662)).title("Due Piple_Koltak").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.6667, 85.5283)).title("Chunatal Ugrachandi").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
			mMap.addMarker(new MarkerOptions().position(new LatLng(27.6687, 85.5618)).title("Chohare Devitar").icon(BitmapDescriptorFactory.fromResource(R.drawable.tower)));
		}
		
		return (mMap != null);
	}
	
	
	
	

	public boolean servicesOK() {
		int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (isAvailable == ConnectionResult.SUCCESS) {
			return true;
		}
		else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		}
		else {
			Toast.makeText(this, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	
	
	@SuppressWarnings("unused")
	private void gotoLocation(double lat, double lng) {
		LatLng ll = new LatLng(lat, lng);
		CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
		mMap.moveCamera(update);
	}

	private void gotoLocation(double lat, double lng,
			float zoom) {
		LatLng ll = new LatLng(lat, lng);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
		mMap.moveCamera(update);
	}

	public void geoLocate(View v) throws IOException {
		hideSoftKeyboard(v);

		EditText et = (EditText) findViewById(R.id.inputemailid);
		String location = et.getText().toString();

		Geocoder gc = new Geocoder(this);
		List<Address> list = gc.getFromLocationName(location, 1);
		Address add = list.get(0);
		String locality = add.getLocality();
		Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

		newlat = (float) add.getLatitude();
		newlng = (float) add.getLongitude();

		gotoLocation(newlat, newlng, DEFAULTZOOM);

	

	    }
	
	private void hideSoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	@Override
	public void onBackPressed() {
	// moveTaskToBack(true);
		moveTaskToBack(true);
	
		//Log.d(Tagmessage,"intent");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		//moveTaskToBack(true);
		
		/*MapStateManager mgr = new MapStateManager(this);
		mgr.saveMapState(mMap);*/
	}
     /* @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	
    }*/
	@Override
	protected void onResume() {
		super.onResume();
		/*MapStateManager mgr = new MapStateManager(this);
		CameraPosition position = mgr.getSavedCameraPosition();
		if (position != null) {
			CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
			mMap.moveCamera(update);
			//			This is part of the answer to the code challenge
			mMap.setMapType(mgr.getSavedMapType());
		}
		onRequestUpdates();*/
	}

	private void onRequestUpdates() {
		// TODO Auto-generated method stub
		if(service !=null){
			
			service.requestLocationUpdates(bestProvider,10000L, 0.5f, this);
		}
	}
	protected void gotoCurrentLocation() {
		
	}
	 public void SqlPush(){
			// service = (LocationManager) getSystemService(LOCATION_SERVICE);
			 
			 
				 
				     
					   boolean diditwork=true;
					   String currentTime = (String) DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());
					   
							// Toast.makeText(getBaseContext(),"sqlPush", Toast.LENGTH_LONG).show();
                       
					   
					   
					try{  
						   Log.v("after lat", "try");
						   
					       SQL entry=new SQL(Map_Activity.this);
					      
					      if(newlat!=0 && newlng!=0){
					     // Toast.makeText(getBaseContext(),"sqlPush", Toast.LENGTH_LONG).show();  
					      entry.open();
					      entry.createEntry(DeviceID,carrierName,currentTime, Float.toString(newlat), Float.toString(newlng),Integer.toString(ASU),wifiavailable,"no time");
					      entry.close();
					      }else{
					    	  diditwork=false;
					      }
					}catch (NullPointerException e){
						  
							  Toast.makeText(Map_Activity.this, "Gps location not located", Toast.LENGTH_SHORT).show();
						   diditwork=false;
					   }finally{
						   if(diditwork){
							   /*Dialog d =new Dialog(this);
							   d.setTitle("Yessss!!!!");
							   TextView tv=new TextView(this);
							   
							   tv.setText(DeviceID+carrierName+currentTime+"  "+Float.toString(newlat)+"  "+Float.toString(newlng)+" "+Integer.toString(ASU)+wifiavailable);
							   
							   d.setContentView(tv);
							   d.show();*/
   							 //Toast.makeText(getBaseContext(),currentTime+"  "+Float.toString(newlat)+"  "+Float.toString(newlng)+" "+Integer.toString(ASU)+wifiavailable , Toast.LENGTH_LONG).show();

							   
						   
					   }
				 
			 }
			 
			 
		}
	 
private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		 
		 private String resp="Data sent";
	        @Override
	        protected String doInBackground(String... urls) {
	        	
	        	    	
	        	             Log.d("yes","good");
	        		        	boolean diditwork=true;
	        					 try{
	        						
	        						 //  SQL entry=new SQL(getBaseContext());
	        						   sql.open();
	        						   sql.getDataAndPost(count);
	        						         						   
	        						  
	        						   Log.d("yes","good2");
	        						   
	        						  // Toast.makeText(getBaseContext(),entry.getDataAndPost(count), Toast.LENGTH_LONG).show();
	        						   sql.close();
	        						 }catch (Exception e){
	        							   Log.d("no",e.toString());
	        							   diditwork=false;
	        							   
	        						   }finally{
	        							   if(diditwork){
	        								   
	        								 
	        								  
	        								   
	        								   
	        							   }
	        						   }
	        					 
	        					 return resp; 
	        }
	        	    	
	        	    
	        	
	        protected void onPostExecute(String result) {
	        	  // SQL sql=new SQL(Map_Activity.this);
	        	   
	        	   
	        	  // Toast.makeText(getBaseContext(),Integer.toString(count)+"push to server count", Toast.LENGTH_LONG).show();
	        	  // Toast.makeText(getBaseContext(),Integer.toString(sql.getLastRowId())  , Toast.LENGTH_LONG).show();
	        	  }  
	            
	            
	        }



public void PushToServer(){
    sql=new SQL(getBaseContext());
    try {



         sql.open();
        if(count <= sql.getLastRowId()){
         // DBdata.setText(Integer.toString(count)+" "+"of"+" "+Integer.toString(sql.getLastRowId())+" "+"pushedto server.Press PushToServer Button Again");
         // Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Qarmic sans Abridged.ttf"); 
        //  DBdata.setTypeface(type);
            //Toast.makeText(getBaseContext(), Integer.toString(sql.getLastRowId())+" "+"Main", Toast.LENGTH_SHORT).show();
              String urlPost = "http://www.wirelessnaksha.com/input.php";
              new HttpAsyncTask().execute(urlPost);
              count++;
}else{
	//Toast.makeText(getBaseContext(), "count exceeds", Toast.LENGTH_SHORT).show();
}
sql.close();


} catch (SQLException e) {
// TODO Auto-generated catch block
e.printStackTrace();


 }

  } //push toserver
     class WifiReceiver extends BroadcastReceiver {
         public void onReceive(Context c, Intent intent) {
             sb = new StringBuilder();
             wifiList = mainWifi.getScanResults();
             for(int i = 0; i < wifiList.size(); i++){
                 sb.append(new Integer(i+1).toString() + ".");
                 sb.append(wifiList.get(i).SSID+" "+wifiList.get(i).level+"  ");
                // sb.append("\n");
             }
           // wifi12.setText(sb);
            wifiavailable=sb.toString();
         }
     }
	        
     public class NetworkChangeReceiver extends BroadcastReceiver {
    	 
    	  @Override
    	  public void onReceive(final Context context, final Intent intent) {
    	 
    	   Log.v(LOG_TAG, "Receieved notification about network status");
    	   isNetworkAvailable(context);
    	 
    	  }
    	 
    	 
    	  private boolean isNetworkAvailable(Context context) {
    	   ConnectivityManager connectivity = (ConnectivityManager) 
    	     context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	   if (connectivity != null) {
    	    NetworkInfo[] info = connectivity.getAllNetworkInfo();
    	    if (info != null) {
    	     for (int i = 0; i < info.length; i++) {
    	      if (info[i].getState() == NetworkInfo.State.CONNECTED) {
    	       if(!isConnected){
    	        Log.v(LOG_TAG, "Now you are connected to Internet!");
    	        DBdata.setText("Internet Available! Press button Below to send data");
    	        isConnected = true;
    	      //  context.startActivity(new Intent(getApplicationContext(),Map_Activity.class));
    	       
    	        //update from the server
    	       /* do{
    	        PushToServer();
    	        }while(isConnected);*/
    	       }
    	       return true;
    	      }
    	     }
    	    }
    	   }
    	   Log.v(LOG_TAG, "You are not connected to Internet!");
    	DBdata.setText("No Internet Connection");
    	   isConnected = false;
    	   return false;
    	  }
    	 }
    

}

