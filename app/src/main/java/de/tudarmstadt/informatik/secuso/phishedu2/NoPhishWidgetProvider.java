package de.tudarmstadt.informatik.secuso.phishedu2;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.FrontendController;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishResult;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishURL;

/**
 * Created by Philipp on 17.08.2015.
 */

public class NoPhishWidgetProvider extends AppWidgetProvider implements FrontendController, BackendController.BackendInitListener {


    private static final String OnClick1 = "phish";
    private static final String OnClick2 = "nophish";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        for(int i=0; i<appWidgetIds.length; i++) {
            int currentWidgetId = appWidgetIds[i];
            String url = "http://www.tutorialspoint.com";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(url));

            PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.nophish_appwidget);

            views.setOnClickPendingIntent(R.id.phish, getPendingSelfIntent(context, OnClick1));
            views.setOnClickPendingIntent(R.id.nophish, getPendingSelfIntent(context, OnClick2));

            if(!BackendControllerImpl.getInstance().isInitDone()) {
                BackendControllerImpl.getInstance().init(this,this);
            }
            //int maxLevel = BackendControllerImpl.getInstance().getMaxUnlockedLevel();
            //views.setTextViewText(R.id.showUrl, Integer.toString(maxLevel));

            appWidgetManager.updateAppWidget(currentWidgetId, views);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (OnClick1.equals(intent.getAction())) {
            // your onClick action is here
            Toast.makeText(context, "Phish", Toast.LENGTH_SHORT).show();
        } else if (OnClick2.equals(intent.getAction())) {
            Toast.makeText(context, "No Phish", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public Activity getBaseActivity() {
        return null;
    }

    @Override
    public void startBrowser(Uri url) {

    }

    @Override
    public void displayToast(String message) {

    }

    @Override
    public void displayToast(int Rid) {

    }

    @Override
    public void displayToastScore(int score) {

    }

    @Override
    public void updateUI() {

    }

    @Override
    public void resultView(PhishResult result) {

    }

    @Override
    public void vibrate(long miliseconds) {

    }

    @Override
    public void showMainMenu() {

    }

    @Override
    public void showProofActivity() {

    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    @Override
    public void initProgress(int percent) {

    }

    @Override
    public void onInitDone() {

    }
}
