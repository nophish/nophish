package de.tudarmstadt.informatik.secuso.phishedu;

import java.util.LinkedHashMap;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerInterface;
import de.tudarmstadt.informatik.secuso.phishedu.backend.FrontendControllerInterface;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BackendTestActivity extends ListActivity implements FrontendControllerInterface  {
	public interface BackendTest{
		void test();
	}
	
	private ArrayAdapter<String> adapter;
	private LinkedHashMap<String, BackendTest> entrys;
	private BackendControllerInterface backend;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		BackendController.init(this);
		this.backend=BackendController.getInstance();
		
		this.entrys = new LinkedHashMap<String, BackendTest>();
		entrys.put("send mail", new BackendTest(){public void test(){mailSendTest();}});
		entrys.put("Start level 1", new BackendTest(){public void test(){leve1Test();}});
		
		this.adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, entrys.keySet().toArray(new String[0]));
		// Assign adapter to List
        setListAdapter(adapter); 
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		this.entrys.get(this.adapter.getItem(position)).test();
	}
	

	protected void mailSendTest() {
		this.backend.sendMail("cbergmann@schuhklassert.de", "cbergmann@schuhklassert.de", "This is a user message");
	}

	public void leve1Test(){
		this.backend.StartLevel1();
	}

	private void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void MailReturned() {
		displayToast("The Mail returned!");
	}

	@Override
	public void level1Finished() {
		displayToast("Level 1 is completed!");
	}

	@Override
	public void initDone() {
		displayToast("we are finished with initialization!");
	}

	@Override
	public Context getContext() {
		return getApplicationContext();
	}

}
