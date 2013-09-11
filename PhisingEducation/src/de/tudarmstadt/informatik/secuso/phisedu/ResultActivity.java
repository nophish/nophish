package de.tudarmstadt.informatik.secuso.phisedu;

import de.tudarmstadt.informatik.secuso.phisedu.R;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	Intent request = getIntent();
    	Uri data = request.getData();
    	Toast popup = Toast.makeText(getApplicationContext(), data.getHost(), Toast.LENGTH_SHORT);
    	popup.show();
    }
    
}
