/*
 * Based off of the sample code by Jake Wharton found here https://github.com/JakeWharton/Android-ViewPagerIndicator
 * 
 * 
 */

package what.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public final class MyFragment extends SherlockFragment {
	private static final String KEY_CONTENT = "MyFragment:Content";
	private String mContent = "???";

	public static MyFragment newInstance(String content) {
		MyFragment fragment = new MyFragment();

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			builder.append(content).append(" ");
		}
		builder.deleteCharAt(builder.length() - 1);
		fragment.mContent = builder.toString();

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}

		TextView text = new TextView(getActivity());
		text.setGravity(Gravity.CENTER);
		text.setText(mContent);
		text.setTextSize(20 * getResources().getDisplayMetrics().density);
		text.setPadding(20, 20, 20, 20);

		LinearLayout layout = new LinearLayout(getActivity());
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setGravity(Gravity.CENTER);
		layout.addView(text);

		return layout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		Toast.makeText(getActivity(), String.valueOf(System.currentTimeMillis() / 1000), Toast.LENGTH_SHORT).show();
		super.onResume();
	}

}