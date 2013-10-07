package de.tudarmstadt.informatik.secuso.phishedu;

import java.util.LinkedHashMap;

import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.games.GamesClient;
import com.google.example.games.basegameutils.BaseGameActivity;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.FrontendControllerInterface;

import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BackendTestActivity extends BaseGameActivity implements FrontendControllerInterface, View.OnClickListener  {
	public interface BackendTest{
		void test();
	}

	private ArrayAdapter<String> adapter;
	private LinkedHashMap<String, BackendTest> entrys;
	//private final ListView listview = (ListView) findViewById(R.id.);

	public BackendTestActivity() {
		// request AppStateClient and GamesClient
		super(BaseGameActivity.CLIENT_APPSTATE |
				BaseGameActivity.CLIENT_GAMES);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		BackendController.getInstance().init(this);
		
		
		setContentView(R.layout.activity_backend_test);
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this); 

		entrys = new LinkedHashMap<String, BackendTest>();
		entrys.put("send mail", new BackendTest(){public void test(){mailSendTest();}});
		entrys.put("Start level 1", new BackendTest(){public void test(){leve1Test();}});
		entrys.put("Show me a URL of level 1", new BackendTest(){public void test(){urlTest();}});
		entrys.put("Show Achievements", new BackendTest(){public void test(){showAchievments();}});
		entrys.put("Show Leaderboard Rate", new BackendTest(){public void test(){showLeaderboardRate();}});
		entrys.put("Show Leaderboard Total", new BackendTest(){public void test(){showLeaderboardTotal();}});

		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, entrys.keySet().toArray(new String[0]));

		final ListView listview = (ListView) findViewById(R.id.backendtestListView);
		listview.setAdapter(this.adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				entrys.get(adapter.getItem(position)).test();
			}

		});

		// Assign adapter to List 

	}

	public void showLeaderboardRate() {
		if(this.getGamesClient().isConnected()){
			startActivityForResult(getGamesClient().getLeaderboardIntent(getResources().getString(R.string.leaderboard_detection_rate)), 1);
		}else{
			displayToast("not connected");
		}
	}

	public void showLeaderboardTotal() {
		if(this.getGamesClient().isConnected()){
			startActivityForResult(getGamesClient().getLeaderboardIntent(getResources().getString(R.string.leaderboard_detected_phishing_urls)), 1);
		}else{
			displayToast("not connected");
		}

	}

	public void showAchievments() {
		if(this.getGamesClient().isConnected()){
			startActivityForResult(getGamesClient().getAchievementsIntent(), 0);
		}else{
			displayToast("not connected");
		}

	}

	protected void mailSendTest() {
		BackendController.getInstance().sendMail("cbergmann@schuhklassert.de", "cbergmann@schuhklassert.de", "This is a user message");
	}

	public void leve1Test(){
		BackendController.getInstance().StartLevel1();
	}

	public void urlTest(){
		BackendController.getInstance().setLevel(3);
		displayToast(BackendController.getInstance().getNextUrl().toString());
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

	@Override
	public void initProgress(int percent) {
		if(percent%10 == 0){
			displayToast("init Progress:"+percent);
		}
	}

	@Override
	public void onSignInFailed() {
		// Sign in has failed. So show the user the sign-in button.
		findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
		findViewById(R.id.sign_out_button).setVisibility(View.GONE);
	}

	public void onSignInSucceeded() {
		// show sign-out button, hide the sign-in button
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

		// (your code here: update UI, enable functionality that depends on sign in, etc)
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			// start the asynchronous sign in flow
			beginUserInitiatedSignIn();
		}
		else if (view.getId() == R.id.sign_out_button) {
			// sign out.
			signOut();

			// show sign-in button, hide the sign-out button
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_out_button).setVisibility(View.GONE);
		}
	}

	@Override
	public GamesClient getGamesClient(){
		return super.getGamesClient();
	}


	@Override
	public void onLevelChange(int level) {
		displayToast("Level Changed:"+level);
	}

	public AppStateClient getAppStateClient(){
		return super.getAppStateClient();
	}

	@Override
	public void startBrowser(Uri url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, url);
		this.startActivity(browserIntent);
	}
}
