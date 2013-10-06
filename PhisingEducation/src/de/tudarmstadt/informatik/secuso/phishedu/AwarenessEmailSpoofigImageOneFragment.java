package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AwarenessEmailSpoofigImageOneFragment extends Fragment {

	static AwarenessEmailSpoofigImageOneFragment init(int val) {
		AwarenessEmailSpoofigImageOneFragment imagefragment = new AwarenessEmailSpoofigImageOneFragment();
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
		View layoutView = inflater.inflate(R.layout.image_fragment_pager_relative_1, container,
				false);
		return layoutView;
	}
}
