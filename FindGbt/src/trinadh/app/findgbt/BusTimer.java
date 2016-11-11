package trinadh.app.findgbt;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class BusTimer extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.tracker);
	       String route;
	       String towards;
	   Bundle b=getIntent().getExtras();
	   towards=b.getString("to");
	   route=b.getString("route");
	   Integer r=Integer.parseInt(route);
	   Integer t=Integer.parseInt(towards);
	   
	   if(r==1||r==9||r==23||r==17||r==2)
	   {
		   
		   if(t==2)
			   towards="4";
	   }
	   else
	   {
		   
		   if(t==1)
			   towards="2";
		   else
			   towards="3";
	   }
  
	     
	        WebView wv = (WebView) findViewById(R.id.webview);
	        WebSettings webSettings = wv.getSettings();
	        wv.getSettings().setJavaScriptEnabled(true);
	        webSettings.setBuiltInZoomControls(true);
	        wv.loadUrl("http://99.51.202.121/tmwebwatch/LiveADAArrivalTimes?r="+route+"&d="+towards);
	}
}
