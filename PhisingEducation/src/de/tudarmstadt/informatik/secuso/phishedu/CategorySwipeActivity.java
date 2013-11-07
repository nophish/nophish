package de.tudarmstadt.informatik.secuso.phishedu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public abstract class CategorySwipeActivity extends SwipeActivity {
	

	protected abstract int[][] getLayouts();
	protected abstract int getCategory();
	protected abstract void onStartClick();
	protected abstract String startButtonText();
	
	
	@Override
	protected void checkAndHideButtons(int totalPages, int nextPage) {
		super.checkAndHideButtons(totalPages, nextPage);
		if (nextPage == totalPages - 1) {
			bStartLevel.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bStartLevel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onStartClick();				
			}
		});
		bStartLevel.setText(this.startButtonText());
	}
	
	protected int getCategoryLevel(){
		return Math.min(this.getCategory(), this.getLayouts().length-1);
	}
	
	@Override
	protected int getPageCount() {
		return this.getLayouts()[getCategoryLevel()].length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(this.getLayouts()[getCategoryLevel()][page],	container, false);
	}
	
}
