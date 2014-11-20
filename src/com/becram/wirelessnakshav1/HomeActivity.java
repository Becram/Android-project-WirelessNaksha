package com.becram.wirelessnakshav1;


 




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends DashBoardActivity {
    /** Called when the activity is first created. */
	private static final String LOG_TAG = "CheckNetworkStatus";
	// private NetworkChangeReceiver receiver;
	 private boolean isConnected = false;
	 private TextView networkStatus;
	 Map_Activity mapACT;
	 TextView infor;
	 EditText edt;
	 Button sendEmail;
	 SQL newSQL;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setHeader(getString(R.string.HomeActivityTitle), false, true);
        infor=(TextView) findViewById(R.id.info);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Qarmic sans Abridged.ttf"); 
        infor.setTypeface(type);
        
       
        
        edt=(EditText) findViewById(R.id.inputemailid);
        newSQL=new SQL(this);
        
        sendEmail=(Button) findViewById(R.id.SendButton);
        sendEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// String email=edt.getText().toString();
				
				
				sendEmail();
				
				
			}
		});
        
        
    }
    
    protected void sendEmail() {
        Log.i("Send email", "");
        String email=edt.getText().toString();
        String[] TO = {email};
      //  String[] CC = {"ioeproject@googlegroups.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
       // emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "*WirelessNaksha*-> Requested Data");
       newSQL.open();
        emailIntent.putExtra(Intent.EXTRA_TEXT, newSQL.getDataForEmail());
        newSQL.close();

        try {
           startActivity(Intent.createChooser(emailIntent, "Send mail..."));
           finish();
           Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
           Toast.makeText(HomeActivity.this, 
           "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
     }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	Intent myIntent=new Intent(getApplicationContext(), Map_Activity.class);
    	startActivity(myIntent);
    }
    
    /**
     * Button click handler on Main activity
     * @param v
     */
    public void onButtonClicker(View v)
    {
    	Intent intent;
    	
    	switch (v.getId()) {
		case R.id.main_btn_map:
			intent = new Intent(this, Map_Activity.class);
			startActivity(intent);
			break;

		case R.id.main_btn_connwifi:
			Intent intent1 = new Intent(this, PhoneStatus.class);
			startActivity(intent1);
			break;
			
		/*case R.id.main_btn_wifiavailable:
			Intent intent2 = new Intent(this, Available_Activity.class);
			startActivity(intent2);
			break;*/
			
		/*case R.id.main_btn_honeycomb:
			intent = new Intent(this, Activity_Honeycomb.class);
			startActivity(intent);
			break;
			
		case R.id.main_btn_ics:
			intent = new Intent(this, Activity_ICS.class);
			startActivity(intent);
			break;
			
		case R.id.main_btn_jellybean:
			intent = new Intent(this, Activity_JellyBean.class);
			startActivity(intent);
			break;	*/
		default:
			break;
		}
    }
/*    public class NetworkChangeReceiver extends BroadcastReceiver {
    	 
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
    	        networkStatus.setText("Now you are connected to Internet!");
    	        isConnected = true;
    	      
    	        final Handler handler = new Handler();
	    		handler.postDelayed(new Runnable() {
	    		    public void run() {
	    		        
	    		   // notifyUs();
	    		    	Log.v(LOG_TAG, "Now you Inse are connected to Internet!");
	    		    	
	    		        handler.postDelayed(this,5000); //now is every 2 minutes
	    		    }
	    					
	    		 }, 5000);
    	        
    	        //do your processing here ---
    	        //if you need to post any data to the server or get status
    	        //update from the server
    	        
    	       }
    	       return true;
    	      }
    	     }
    	    }
    	   }
    	   Log.v(LOG_TAG, "You are not connected to Internet!");
    	   networkStatus.setText("You are not connected to Internet!");
    	   isConnected = false;
    	   return false;
    	  }
    	 }*/
    
}

