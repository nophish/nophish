package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AwarenessEmailSpoofigImageTwoFragment extends Fragment {

	static AwarenessEmailSpoofigImageTwoFragment init(int val) {
		AwarenessEmailSpoofigImageTwoFragment imagefragment = new AwarenessEmailSpoofigImageTwoFragment();
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
		View layoutView = inflater.inflate(R.layout.awareness_email_spoofing_fragment_pager_2, container,
				false);
		return layoutView;
	}
}
