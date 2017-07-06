package de.tudarmstadt.informatik.secuso.phishedu2;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.EnumMap;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.FrontendController;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishResult;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishURL;

/**
 * Created by Philipp on 17.08.2015.
 */

public class NoPhishWidgetProvider extends AppWidgetProvider implements FrontendController, BackendController.BackendInitListener {


    private static final String OnClick1 = "phish";
    private static final String OnClick2 = "nophish";

    private boolean inited = false;

    private int maxLevel = -1;
    private EnumMap<PhishAttackType, PhishURL[]> urlCache = new EnumMap<PhishAttackType, PhishURL[]>(PhishAttackType.class);;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        for(int i=0; i<appWidgetIds.length; i++) {
            int currentWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.nophish_appwidget);

            views.setOnClickPendingIntent(R.id.phish, getPendingSelfIntent(context, OnClick1));
            views.setOnClickPendingIntent(R.id.nophish, getPendingSelfIntent(context, OnClick2));

            if(!BackendControllerImpl.getInstance().isInitDone()) {
                views.setTextViewText(R.id.showUrl, "Um dieses Widget nutzen zu können, müssen Sie mindestens einmal die NoPhish App gestartet haben.");
            }
            else if(!inited){
                maxLevel = BackendControllerImpl.getInstance().getMaxUnlockedLevel();
                urlCache = BackendControllerImpl.getInstance().getURLCache();
                inited = true;
            }

            PhishURL[] urls = urlCache.get(PhishAttackType.NoPhish);

            for(PhishURL test: urls) {
                Log.e("TEST", test.toString());
            }

            String nextUrl = urls[0].toString();

            views.setTextViewText(R.id.showPhishType, "Max Level: " + Integer.toString(maxLevel));
            views.setTextViewText(R.id.showUrl, nextUrl);

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
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.nophish_appwidget);

//            PhishURL[] urls = urlCache.get(PhishAttackType.NoPhish);
//
//            Random r = new Random();
//            int i1 = r.nextInt(10 - 1) + 1;
//
//            String nextUrl = urls[1].toString();

            views.setTextViewText(R.id.showUrl, "Updated");
            updateWidget(context, views);

            Toast.makeText(context, "Phish", Toast.LENGTH_SHORT).show();
        } else if (OnClick2.equals(intent.getAction())) {
            Toast.makeText(context, "No Phish", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateWidget (Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context,
                NoPhishWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    @Override
    public Context getContext() {
        return this.getContext();
    }

    @Override
    public Activity getBaseActivity() {

        return this.getBaseActivity();
    }

    @Override
    public void startBrowser(Uri url) {

    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT);
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
    /*
    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }
    */

    @Override
    public void initProgress(int percent) {

    }

    @Override
    public void onInitDone() {

    }
}
