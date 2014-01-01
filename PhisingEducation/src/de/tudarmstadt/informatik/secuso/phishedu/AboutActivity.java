package de.tudarmstadt.informatik.secuso.phishedu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class AboutActivity extends PhishBaseActivity implements OnClickListener {

	@Override
	public int getLayout() {
		return R.layout.about;
	}
	
	@Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.secuso_logo:
        	Intent browserIntent = new Intent(Intent.ACTION_VIEW);
    		browserIntent.setData(Uri.parse("https://www.secuso.informatik.tu-darmstadt.de"));
    		startActivity(browserIntent);
            break;
        }
    }

	@Override
	public int[] getClickables() {
		return new int[] {
				R.id.secuso_logo
		};
	}

}
