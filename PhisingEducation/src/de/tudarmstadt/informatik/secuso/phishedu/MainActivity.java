package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    
	/** Called when the user clicks the send button */
	public void goToNextActivity(View view){
		
		Intent intent = new Intent(this, DisplayYouDontBelieveUsActivity.class);
		startActivity(intent);
	}
	
    //to be called when browser is called
//    public void redirectToPage(View view){
//    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://clemens.schuhklassert.de"));
//    	startActivity(browserIntent);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
