package com.becram.wirelessnakshav1;

import java.util.Date;

//import com.becram.wirelessnakshav1.Available_Activity.WifiScanReceiver;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Con_Activity extends DashBoardActivity{

   
   
  private static final String Tagmessage = null;
// private static final String TAG = null;
  // WifiScanReceiver wifiReciever;
   WifiInfo info;
   WifiManager wifimanager;
   String speedString;
   String mac;
   String ssid;
   String ipAddress;
   WifiManager wifiMgr;
   Button pushBUT;
   LocationManager service;
   float newlat=0;
   float newlng=0;
   ProgressBar ProBar;
   

public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_con);
      setHeader(getString(R.string.ConectedActivityTitle), true, true);
      TextView AP=(TextView) findViewById(R.id.tv);
      TextView BSSID=(TextView) findViewById(R.id.textView4);
      TextView Speed=(TextView) findViewById(R.id.GSMstrength);
      TextView Strength=(TextView) findViewById(R.id.textView8);
      TextView ipText=(TextView) findViewById(R.id.textView10);
      TextView text=(TextView) findViewById(R.id.text);
      
      WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
      Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Qarmic sans Abridged.ttf"); 
      AP.setTypeface(type);
      BSSID.setTypeface(type);
      Speed.setTypeface(type);
      Strength.setTypeface(type);
      ipText.setTypeface(type);
      text.setTypeface(type);
     // AP.setTypeface(type);
     
      
      if (wifiMgr.isWifiEnabled() == false)
      {  
          // If wifi disabled then enable it
          Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
          Toast.LENGTH_LONG).show();
          
           
          wifiMgr.setWifiEnabled(true);
          
      }
      
      
    	  WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
    	  int level=WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 100);
          int str=wifiInfo.getRssi();
          String STRENGTH=""+str;
    	  int ip = wifiInfo.getIpAddress();
    	  int speed = wifiInfo.getLinkSpeed();
    	  speedString = Integer.toString(speed);
    	  mac = wifiInfo.getMacAddress();
    	  ssid = wifiInfo.getSSID();
    	  ipAddress = Formatter.formatIpAddress(ip);
           AP.setText(ssid);
           BSSID.setText(mac);
          Speed.setText(speedString+"Mbps");
          Strength.setText(STRENGTH+"dBm");
          
           ipText.setText(ipAddress);
           text.setText("Wifi Strength is"+" "+ level+"%");
           ProBar =(ProgressBar) findViewById(R.id.progressBar2);
           ProBar.setProgress(level);
     
      }


@Override
public void onBackPressed() {
// moveTaskToBack(true);
 Intent inn=new Intent(getApplicationContext(),HomeActivity.class);
 startActivity(inn);
Log.d(Tagmessage,"intent");
}


     
      
   

}
     
