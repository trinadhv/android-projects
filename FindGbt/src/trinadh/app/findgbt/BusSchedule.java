package trinadh.app.findgbt;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class BusSchedule extends Activity{
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.tracker);

	        WebView wv = (WebView) findViewById(R.id.webview);
	        WebSettings webSettings = wv.getSettings();
	        wv.getSettings().setJavaScriptEnabled(true);
	        webSettings.setBuiltInZoomControls(true);
	        wv.loadUrl("http://99.51.202.121/TMWebWatch/Schedule");
	    }
}
