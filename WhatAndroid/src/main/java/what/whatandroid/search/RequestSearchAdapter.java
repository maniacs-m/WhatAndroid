package what.whatandroid.search;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import api.cli.Utils;
import api.search.requests.Request;
import api.soup.MySoup;
import com.nostra13.universalimageloader.core.ImageLoader;
import what.whatandroid.R;
import what.whatandroid.callbacks.ViewRequestCallbacks;
import what.whatandroid.imgloader.ImageLoadingListener;
import what.whatandroid.settings.SettingsActivity;

import java.util.Date;

/**
 * Adapter to display request search results
 */
public class RequestSearchAdapter extends ArrayAdapter<Request> implements AdapterView.OnItemClickListener {
	private final Context context;
	private final LayoutInflater inflater;
	/**
	 * Callbacks to view the selected request
	 */
	private ViewRequestCallbacks callbacks;

	/**
	 * Construct the empty adapter. A new search can be set to be displayed via viewSearch
	 */
	public RequestSearchAdapter(Context context, View footer){
		super(context, R.layout.list_request);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		try {
			callbacks = (ViewRequestCallbacks)context;
		}
		catch (ClassCastException e){
			throw new ClassCastException(context.toString() + " must implement ViewRequestCallbacks");
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		if (convertView != null){
			holder = (ViewHolder)convertView.getTag();
		}
		else {
			convertView = inflater.inflate(R.layout.list_request_search, parent, false);
			holder = new ViewHolder();
			holder.art = (ImageView)convertView.findViewById(R.id.art);
			holder.spinner = (ProgressBar)convertView.findViewById(R.id.loading_indicator);
			holder.listener = new ImageLoadingListener(holder.spinner);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.year = (TextView)convertView.findViewById(R.id.year);
			holder.votes = (TextView)convertView.findViewById(R.id.votes);
			holder.bounty = (TextView)convertView.findViewById(R.id.bounty);
			holder.created = (TextView)convertView.findViewById(R.id.created);
			convertView.setTag(holder);
		}
		Request r = getItem(position);
		holder.title.setText(r.getTitle());
		holder.votes.setText(r.getVoteCount().toString());
		holder.bounty.setText(Utils.toHumanReadableSize(r.getBounty().longValue()));
		Date createDate = MySoup.parseDate(r.getTimeAdded());
		holder.created.setText(DateUtils.getRelativeTimeSpanString(createDate.getTime(),
			new Date().getTime(), DateUtils.WEEK_IN_MILLIS));

		String imgUrl = r.getImage();
		if (SettingsActivity.imagesEnabled(context) && imgUrl != null && !imgUrl.isEmpty()){
			ImageLoader.getInstance().displayImage(imgUrl, holder.art, holder.listener);
		}
		else {
			holder.art.setVisibility(View.GONE);
			holder.spinner.setVisibility(View.GONE);
		}
		if (r.getYear().intValue() != 0){
			holder.year.setText(r.getYear().toString());
		}
		else {
			holder.year.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		//Clicking the footer gives us an out of bounds event so account for this
		if (position - 1 < getCount()){
			callbacks.viewRequest(getItem(position - 1).getRequestId().intValue());
		}
	}

	private class ViewHolder {
		public ImageView art;
		public ProgressBar spinner;
		public ImageLoadingListener listener;
		public TextView title, year, votes, bounty, created;
	}
}
