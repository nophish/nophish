package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.app.Application;

public class PhishEduApp extends Application {
public void onCreate() {
        super.onCreate();
  }

  private Activity mCurrentActivity = null;
  public Activity getCurrentActivity(){
        return mCurrentActivity;
  }
  public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
  }
}
