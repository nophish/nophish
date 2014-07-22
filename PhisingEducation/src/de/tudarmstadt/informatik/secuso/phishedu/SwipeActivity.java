package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;


public abstract class SwipeActivity extends PhishBaseActivity implements ViewPager.OnPageChangeListener
{

	//abstract functions
	protected abstract int getPageCount();
	protected abstract View getPage(int page, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	private boolean gotostart=false;

	@Override
	public void onResume() {
		super.onResume();
		final ViewPager mPager= (ViewPager) getActivity().findViewById(R.id.pager);
		if(mPager!=null && gotostart){
			mPager.setCurrentItem(0);
			gotostart=false;
		}
	}

	@Override
	public void onSwitchTo() {
		gotostart=true;
		super.onSwitchTo();
	}

	//hooks
	protected void onClickPage(int page){}
	protected void onStartClick(){}
	protected String startButtonText(){
		return null;
	}

	void updateUI(Activity v){
		super.updateUI(v);

		final ViewPager mPager= (ViewPager) v.findViewById(R.id.pager);
		mPager.setAdapter(new SwipePageAdapter(getFragmentManager(),this));
		mPager.setOnPageChangeListener(this);

		ImageView imgPrevious = (ImageView) v.findViewById(R.id.game_intro_arrow_back);
		imgPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(mPager.getCurrentItem()-1);			
			}
		});
		imgPrevious.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mPager.setCurrentItem(0);	
				return true;
			}
		});
		ImageView imgNext = (ImageView) v.findViewById(R.id.game_intro_arrow_forward);
		imgNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(mPager.getCurrentItem()+1);
			}
		});
		imgNext.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mPager.setCurrentItem(getPageCount()-1);	
				return true;
			}
		});

		Button bStartLevel = (Button) v.findViewById(R.id.game_intro_start_button);
		bStartLevel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onStartClick();				
			}
		});
		bStartLevel.setText(this.startButtonText());

		checkAndHideButtons(0);
	}

	@Override
	public int getLayout() {
		return R.layout.fragment_pager;
	}

	protected void checkAndHideButtons(int page) {
		ImageView imgPrevious = (ImageView) getActivity().findViewById(R.id.game_intro_arrow_back);
		ImageView imgNext = (ImageView) getActivity().findViewById(R.id.game_intro_arrow_forward);
		Button bStartLevel = (Button) getActivity().findViewById(R.id.game_intro_start_button);
		imgNext.setVisibility(View.VISIBLE);
		imgPrevious.setVisibility(View.VISIBLE);
		bStartLevel.setVisibility(View.INVISIBLE);
		if (page == getPageCount() - 1) {
			imgNext.setVisibility(View.INVISIBLE);
			if(this.startButtonText()!=null){
				bStartLevel.setVisibility(View.VISIBLE);
			}
		}
		if (page == 0 ) {
			imgPrevious.setVisibility(View.INVISIBLE);
		}
	}

	protected void setUrlText(TextView urlText) {
		String[] urlParts = BackendControllerImpl.getInstance().getUrl().getParts();
		StringBuilder builder = new StringBuilder();

		for(int i=0; i< urlParts.length; i++){
			String urlpart = urlParts[i];
			builder.append(urlpart);
		}

		urlText.setText(builder.toString());
	}

	private class SwipePageAdapter extends PagerAdapter {
		private class ClickListener implements View.OnClickListener{
			private int page;

			public ClickListener(int level){
				this.page=level;
			}

			@Override
			public void onClick(View v) {
				SwipeActivity.this.onClickPage(page);
			}

		}

		SwipeActivity activity;
		public SwipePageAdapter(FragmentManager fragmentManager, SwipeActivity activity) {
			super();
			this.activity = activity;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = activity.getPage(position, activity.getLayoutInflater(getArguments()), container, getArguments());
			view.setOnClickListener(new ClickListener(position));
			updateScore(view);
			for (int i : getClickables()) {
				View clickview = view.findViewById(i);
				if(clickview != null){
					clickview.setOnClickListener(activity);	
				}
			}
			container.addView(view);
			return view;

		};

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return activity.getPageCount();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int position) {
		checkAndHideButtons(position);
	}

}
