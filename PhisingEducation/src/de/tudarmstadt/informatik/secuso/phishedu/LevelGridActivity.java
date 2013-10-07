package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class LevelGridActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_grid_view);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		ActionBar ab = getActionBar();
		ab.setTitle("Level Überblick");
		// ab.setSubtitle("sub-title");

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(LevelGridActivity.this, "Level " + position,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		
	}
	
	private class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return gridImages.length;
		}

		public Object getItem(int position) {
			/*
			 * Normally, getItem(int) should return the actual object at the
			 * specified position in the adapter, but it's ignored for this
			 * example. TODO: falls erforderlich
			 */
			return null;
		}

		public long getItemId(int position) {
			/*
			 * getItemId(int) should return the row id of the item, but it's not
			 * needed here. TODO: falls erforederlich
			 */
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(170, 170));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			/*
			 * TODO: depending on current Level, call different grid
			 */
			imageView.setImageResource(gridImages[position]);
			return imageView;
		}

		// references to our images
		private Integer[] gridImages = { R.drawable.rect, R.drawable.rect,
				R.drawable.rect, R.drawable.rect, R.drawable.rect,
				R.drawable.rect, R.drawable.rect, R.drawable.rect,
				R.drawable.rect, R.drawable.rect, R.drawable.rect,
				R.drawable.rect, R.drawable.rect, R.drawable.rect,
				R.drawable.rect };
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_grid, menu);
		return true;
	}


}
