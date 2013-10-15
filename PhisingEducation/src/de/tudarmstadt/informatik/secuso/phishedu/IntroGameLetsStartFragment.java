package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IntroGameLetsStartFragment extends Fragment {

	static IntroGameLetsStartFragment init(int val) {
		IntroGameLetsStartFragment imagefragment = new IntroGameLetsStartFragment();
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

		View layoutView = inflater.inflate(R.layout.awareness_final_04_lets_start,
				container, false);
		return layoutView;
	}
	
}
