package com.widget.ushidatchi;

import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

public class UshiDatchiService extends Service{
	
		public final static String PREFS = "options";
		public final static String BUTTON = "button";
	
	  @Override
	  public void onStart(Intent intent, int startId) {
		  String LOG = "Log";
	    Log.i(LOG, "Called");
	    // Create some random data

	    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
	        .getApplicationContext());

	    int[] allWidgetIds = intent
	        .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
	    if(intent.getAction()!=null){
	    String thing = intent.getAction().toString();
	    System.out.println(thing);
	    }

	    ComponentName thisWidget = new ComponentName(getApplicationContext(),
	        UshiDatchi.class);
	    
	    // Try the sharedpreferences
	    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PREFS, 0);

	    
	    
	    
	    int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
	    Log.w(LOG, "From Intent" + String.valueOf(allWidgetIds.length));
	    Log.w(LOG, "Direct" + String.valueOf(allWidgetIds2.length));
	    
	    if(intent.getStringExtra(BUTTON)!=null){
	    Log.w("Check", intent.getStringExtra(BUTTON));
	    }
	      // Increment shared prefs
	      sharedPreferences.edit().putInt("hi", sharedPreferences.getInt("hi", 0)+1).commit();
	      
	      // All buttons refresh
	      // Check extras to see if left or right pressed
	      

	    for (int widgetId : allWidgetIds) {
	      // Create some random data
	      int number = sharedPreferences.getInt("hi", 0);

	      RemoteViews remoteViews = new RemoteViews(this
	          .getApplicationContext().getPackageName(),
	          R.layout.egg_layout);
	      Log.w("WidgetExample", String.valueOf(number));
	      // Set the text
	      remoteViews.setTextViewText(R.id.update,
	          "Random: " + sharedPreferences.getInt("hi", 0));
	      if(number%2==1){
	    	  remoteViews.setImageViewResource(R.id.topMenu, R.drawable.af02);
	      } else {
	    	  remoteViews.setImageViewResource(R.id.topMenu, R.drawable.menu0);
	      }


	      
	      // Register an onClickListener for Right
	      Intent clickIntentRight = new Intent(this.getApplicationContext(),
	          UshiDatchi.class);
	      clickIntentRight.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	      clickIntentRight.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
	          allWidgetIds);
	      clickIntentRight.putExtra(BUTTON, "right");
	      
	      PendingIntent pendingIntentRight = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntentRight,
		          PendingIntent.FLAG_UPDATE_CURRENT);
	      
	      // Register an onClickListener for Left
	      Intent clickIntentLeft = new Intent(this.getApplicationContext(),
		          UshiDatchi.class);
		      clickIntentLeft.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		      clickIntentLeft.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
		          allWidgetIds);
		      clickIntentRight.putExtra(BUTTON, "right");
	      
		      PendingIntent pendingIntentLeft = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntentLeft,
			          PendingIntent.FLAG_UPDATE_CURRENT);
		      

	      
		     
		      
		      remoteViews.setOnClickPendingIntent(R.id.buttonRight, pendingIntentRight);
		      remoteViews.setOnClickPendingIntent(R.id.buttonLeft, pendingIntentLeft);
	      appWidgetManager.updateAppWidget(widgetId, remoteViews);
	    }
	    stopSelf();

	    super.onStart(intent, startId);
	  }

	  @Override
	  public IBinder onBind(Intent intent) {
	    return null;
	  }
	} 
