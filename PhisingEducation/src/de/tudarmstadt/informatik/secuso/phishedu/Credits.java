package de.tudarmstadt.informatik.secuso.phishedu;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

public class Credits extends PhishBaseActivity implements OnClickListener {

	@Override
	int getTitle() {
		return R.string.title_activity_credits;
	}
	
	@Override
	public int getLayout() {
		return R.layout.credits;
	}
	
	@Override
    public void onClick(View view) {
        int id = view.getId();
        //TODO:
		Intent browserIntent = new Intent(Intent.ACTION_VIEW);

		if (id == R.id.back_text || id == R.id.back_icon) {
			browserIntent.setData(Uri.parse("http://www.iconarchive.com/show/mini-2-icons-by-custom-icon-design/Back-icon.html"));
		} else if(id == R.id.forward_text || id == R.id.forward_icon){
			browserIntent.setData(Uri.parse("http://www.iconarchive.com/show/mini-2-icons-by-custom-icon-design/Forward-icon.html"));
		}
		
			
		startActivity(browserIntent);

    }

	@Override
	public int[] getClickables() {
		return new int[] {
				R.id.back_text,
				R.id.back_icon,
				R.id.forward_icon,
				R.id.forward_text
		};
	}

}
