package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IntroGameWhatIsPhishingFragment extends Fragment {

	static IntroGameWhatIsPhishingFragment init(int val) {
		IntroGameWhatIsPhishingFragment imagefragment = new IntroGameWhatIsPhishingFragment();
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
				R.layout.intro_game_what_is_phishing, container, false);
		return layoutView;
	}

}
