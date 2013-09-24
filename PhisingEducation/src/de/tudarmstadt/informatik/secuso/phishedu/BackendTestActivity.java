package de.tudarmstadt.informatik.secuso.phishedu;

import java.util.LinkedHashMap;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BackendTestActivity extends ListActivity  {
	public interface Test{
		void test(BackendTestActivity caller);
	}
	
	private ArrayAdapter<String> adapter;
	private LinkedHashMap<String, Test> entrys; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.entrys = new LinkedHashMap<String, Test>();
		
		entrys.put("test1", new Test(){public void test(BackendTestActivity caller){caller.testToast1();}});
		entrys.put("test2", new Test(){public void test(BackendTestActivity caller){caller.testToast2();}});		
	
		this.adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, entrys.keySet().toArray(new String[0]));
		// Assign adapter to List
        setListAdapter(adapter); 
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		this.entrys.get(this.adapter.getItem(position)).test(this);
	}
	
	public void testToast1(){
		Toast.makeText(getApplicationContext(), "test1", Toast.LENGTH_SHORT).show();
	}
	
	public void testToast2(){
		Toast.makeText(getApplicationContext(), "test2", Toast.LENGTH_SHORT).show();
	}
}
