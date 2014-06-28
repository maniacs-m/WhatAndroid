package what.whatandroid.forums.thread;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import api.forum.thread.ForumThread;
import api.forum.thread.Poll;
import api.son.MySon;
import api.soup.MySoup;
import what.whatandroid.R;
import what.whatandroid.callbacks.LoadingListener;
import what.whatandroid.callbacks.OnLoggedInCallback;
import what.whatandroid.callbacks.SetTitleCallback;
import what.whatandroid.forums.ForumActivity;
import what.whatandroid.forums.poll.PollDialog;

/**
 * Displays a paged view of posts in a thread
 */
public class ThreadFragment extends Fragment implements OnLoggedInCallback, LoadingListener<ForumThread> {
	private static final String THREAD_NAME = "what.whatandroid.forums.THREAD_NAME",
		PAGES = "what.whatandroid.forums.PAGES";

	private SetTitleCallback setTitle;
	private ThreadPagerAdapter pagerAdapter;
	private int thread, pages = 0, postId = -1;
	private String threadName;
	private boolean subscribed = false;
	private MenuItem viewPoll, toggleSubscription;
	/**
	 * We track the poll separately from the rest of the thread so we can update
	 * it without having to reload the page
	 */
	private Poll poll;

	/**
	 * Create a new thread fragment showing the first page of the thread
	 *
	 * @param thread thread id to view
	 */
	public static ThreadFragment newInstance(int thread){
		ThreadFragment f = new ThreadFragment();
		Bundle args = new Bundle();
		args.putInt(ForumActivity.THREAD_ID, thread);
		f.setArguments(args);
		return f;
	}

	/**
	 * Create a new thread fragment showing the thread at some post
	 *
	 * @param thread thread id to view
	 * @param post   post to jump to
	 */
	public static ThreadFragment newInstance(int thread, int post){
		ThreadFragment f = new ThreadFragment();
		Bundle args = new Bundle();
		args.putInt(ForumActivity.THREAD_ID, thread);
		args.putInt(ForumActivity.POST_ID, post);
		f.setArguments(args);
		return f;
	}

	public ThreadFragment(){
		//Required empty ctor
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			setTitle = (SetTitleCallback)activity;
		}
		catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement SetTitleCallback");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		thread = getArguments().getInt(ForumActivity.THREAD_ID);
		if (savedInstanceState != null){
			pages = savedInstanceState.getInt(PAGES);
			threadName = savedInstanceState.getString(THREAD_NAME);
			if (savedInstanceState.containsKey(PollDialog.POLL)){
				poll = (Poll)MySon.toObjectFromString(savedInstanceState.getString(PollDialog.POLL), Poll.class);
			}
		}
		else {
			postId = getArguments().getInt(ForumActivity.POST_ID, -1);
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		if (threadName != null){
			setTitle.setTitle(threadName);
		}
		updateMenus();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_view_pager_strip, container, false);
		ViewPager viewPager = (ViewPager)view.findViewById(R.id.pager);
		pagerAdapter = new ThreadPagerAdapter(getChildFragmentManager(), pages, thread, postId);
		viewPager.setAdapter(pagerAdapter);
		pagerAdapter.setLoadingListener(this);
		if (MySoup.isLoggedIn()){
			onLoggedIn();
		}
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt(PAGES, pages);
		if (threadName != null){
			outState.putString(THREAD_NAME, threadName);
			if (poll != null){
				outState.putString(PollDialog.POLL, MySon.toJson(poll, Poll.class));
			}
		}
		else {
			outState.putString(THREAD_NAME, "Thread");
		}
	}

	@Override
	public void onLoggedIn(){
		pagerAdapter.onLoggedIn();
	}

	@Override
	public void onLoadingComplete(ForumThread data){
		if (poll == null) {
			poll = data.getResponse().getPoll();
		}
		threadName = data.getResponse().getThreadTitle();
		pages = data.getPages();
		setTitle.setTitle(threadName);
		subscribed = data.getResponse().isSubscribed();
		updateMenus();
	}

	private void updateMenus(){
		if (viewPoll != null && toggleSubscription != null){
			if (poll != null) {
				viewPoll.setVisible(true);
			}
			if (subscribed){
				toggleSubscription.setIcon(R.drawable.ic_eye_on);
			}
			else {
				toggleSubscription.setIcon(R.drawable.ic_eye_off);
			}
		}
	}

	/**
	 * Update the cached poll for the thread
	 */
	public void updatePoll(int vote) {
		if (poll != null) {
			poll.applyVote(vote);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.forum_thread, menu);
		viewPoll = menu.findItem(R.id.action_view_poll);
		toggleSubscription = menu.findItem(R.id.action_subscription);
		updateMenus();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if (item.getItemId() == R.id.action_view_poll){
			PollDialog pollDialog = PollDialog.newInstance(poll, thread);
			pollDialog.show(getFragmentManager(), "dialog");
		}
		else if (item.getItemId() == R.id.action_subscription){
			new ToggleSubscriptionsTask().execute();
		}
		return false;
	}

	/**
	 * Async task to toggle the artist's notification status
	 */
	private class ToggleSubscriptionsTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params){
			if (subscribed){
				return ForumThread.unsubscribe(thread);
			}
			return ForumThread.subscribe(thread);
		}

		@Override
		protected void onPreExecute(){
			//Display action as successful while we load
			if (subscribed){
				toggleSubscription.setIcon(R.drawable.ic_eye_off);
			}
			else {
				toggleSubscription.setIcon(R.drawable.ic_eye_on);
			}
			if (isAdded()){
				getActivity().setProgressBarIndeterminate(true);
				getActivity().setProgressBarIndeterminateVisibility(true);
			}
		}

		@Override
		protected void onPostExecute(Boolean status){
			if (isAdded()){
				getActivity().setProgressBarIndeterminate(false);
				getActivity().setProgressBarIndeterminateVisibility(false);
			}
			if (!status){
				if (subscribed){
					Toast.makeText(getActivity(), "Could not remove notifications", Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(getActivity(), "Could not enable notifications", Toast.LENGTH_LONG).show();
				}
			}
			else {
				subscribed = !subscribed;
			}
			updateMenus();
		}
	}
}
