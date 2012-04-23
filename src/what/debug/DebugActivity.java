package what.debug;

import what.gui.MyActivity;
import what.gui.R;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;
import api.son.MySon;

/**
 * 
 *
 */
public class DebugActivity extends MyActivity {
	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.debug, false);
		enableGestures(false);
		try {
			webView = (WebView) this.findViewById(R.id.webView);
			webView.loadData(MySon.getDebugString(), "text/html", "utf-8");
		} catch (Exception e) {
			Toast.makeText(this, "Could not load", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
}
