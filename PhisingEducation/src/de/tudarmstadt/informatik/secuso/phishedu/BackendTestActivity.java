package de.tudarmstadt.informatik.secuso.phishedu;

import java.util.LinkedHashMap;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendCallback;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BackendTestActivity extends ListActivity implements BackendCallback  {
	public interface BackendTest{
		void test();
	}
	
	private ArrayAdapter<String> adapter;
	private LinkedHashMap<String, BackendTest> entrys;
	private BackendController backend;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.backend = new BackendController(this);
		this.entrys = new LinkedHashMap<String, BackendTest>();
		
		entrys.put("test1", new BackendTest(){public void test(){testBackendTest();}});
		
		this.adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, entrys.keySet().toArray(new String[0]));
		// Assign adapter to List
        setListAdapter(adapter); 
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		this.entrys.get(this.adapter.getItem(position)).test();
	}
	
	public void testBackendTest(){
		this.backend.test("testparameter");
	}

	private void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void test_returned(String result) {
		this.displayToast(result);
	}
}
