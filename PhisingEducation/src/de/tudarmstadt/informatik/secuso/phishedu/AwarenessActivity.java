package de.tudarmstadt.informatik.secuso.phishedu;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class AwarenessActivity extends FragmentActivity {

	 static final int ITEMS = 3;
	    MyAdapter mAdapter;
	    ViewPager mPager;
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.blank_layout);
	        mAdapter = new MyAdapter(getSupportFragmentManager());
	        mPager = (ViewPager) findViewById(R.id.pager);
	        mPager.setAdapter(mAdapter);
	        
	    }
	 
	    public static class MyAdapter extends FragmentPagerAdapter {
	        public MyAdapter(FragmentManager fragmentManager) {
	            super(fragmentManager);
	        }
	 
	        @Override
	        public int getCount() {
	            return ITEMS;
	        }
	 
	        @Override
	        public Fragment getItem(int position) {
	            switch (position) {
	            case 0: // Fragment # 0 - This will show image
	                return AwarenessEmailSpoofigImageOneFragment.init(position);
	            case 1: // Fragment # 1 - This will show image
	                return AwarenessEmailSpoofigImageTwoFragment.init(position);
	            default:// Fragment # 3 - Will show image with button
	                return AwarenessEmailSpoofingImageEndFragment.init(position);
	            }
	        }
	    }
	 
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	 

}
