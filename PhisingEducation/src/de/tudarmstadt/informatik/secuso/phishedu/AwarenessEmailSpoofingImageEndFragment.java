package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AwarenessEmailSpoofingImageEndFragment extends Fragment {

	 
    static AwarenessEmailSpoofingImageEndFragment init(int val) {
    	AwarenessEmailSpoofingImageEndFragment endEmailSpoofingFragment = new AwarenessEmailSpoofingImageEndFragment();
 
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        endEmailSpoofingFragment.setArguments(args);
 
        return endEmailSpoofingFragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.image_fragment_pager_relative_3,
                container, false);
        return layoutView;
    }

 

}
