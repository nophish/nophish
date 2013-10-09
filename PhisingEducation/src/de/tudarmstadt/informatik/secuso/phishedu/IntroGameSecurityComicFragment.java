package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IntroGameSecurityComicFragment extends Fragment {

	static IntroGameSecurityComicFragment init(int val) {
		IntroGameSecurityComicFragment imagefragment = new IntroGameSecurityComicFragment();
		// Supply val input as an argument.
		Bundle args = new Bundle();
		args.putInt("val", val);
		imagefragment.setArguments(args);
		return imagefragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layoutView = inflater.inflate(
				R.layout.intro_game_fake_websites_info, container, false);

		return layoutView;
	}
}
