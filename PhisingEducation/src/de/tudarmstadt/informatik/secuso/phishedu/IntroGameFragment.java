package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IntroGameFragment extends Fragment {

	static IntroGameFragment init(int val) {
		IntroGameFragment imagefragment = new IntroGameFragment();
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
		View layoutView = inflater.inflate(R.layout.intro_game, container,
				false);
		return layoutView;
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    // Set title
	    ((FragmentActivity) getActivity()).getActionBar()
	        .setTitle(R.string.title_intro_rules);
	}
}
