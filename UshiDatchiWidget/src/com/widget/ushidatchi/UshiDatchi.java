package com.widget.ushidatchi;


import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.opengl.Visibility;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;

public class UshiDatchi extends AppWidgetProvider {
	
	public final static String PREFS = "options";
	public final static String APP_WIDGET_NEXT = "next button";
	public final static String APP_WIDGET_PREV = "cancel button";
	public final static String APP_WIDGET_MID = "confirm button";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		final int n = appWidgetIds.length;
		Log.i("Activating","Beginning update");
		for(int i=0;i<n;i++){
			int widgetId = appWidgetIds[i];
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.egg_layout);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
			
			//Intents
			Intent clickRight = new Intent(context,UshiDatchi.class);
			clickRight.setAction(APP_WIDGET_PREV);
			PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(context, 0, clickRight, 0);
	
			Intent clickLeft = new Intent(context,UshiDatchi.class);
			clickLeft.setAction(APP_WIDGET_NEXT);
			PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context, 0, clickLeft, 0);
			
			Intent clickMid = new Intent(context,UshiDatchi.class);
			clickMid.setAction(APP_WIDGET_MID);
			PendingIntent pendingIntentMid = PendingIntent.getBroadcast(context, 0, clickMid, 0);
			
			remoteViews.setOnClickPendingIntent(R.id.buttonLeft, pendingIntentNext);
			remoteViews.setOnClickPendingIntent(R.id.buttonRight, pendingIntentPrevious);
			remoteViews.setOnClickPendingIntent(R.id.buttonMid, pendingIntentMid);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	/*
	 @Override
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	      int[] appWidgetIds) {
		
	    Log.w("hi", "onUpdate method called");
	    // Get all ids
	    ComponentName thisWidget = new ComponentName(context,
	        UshiDatchi.class);
	    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
	    


	    // Build the intent to call the service
	    Intent intent = new Intent(context.getApplicationContext(),
	        UshiDatchiService.class);
	    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
	    intent.putExtra(BUTTON, "yellow");

	    // Update the widgets via the service
	    context.startService(intent);
	  }
	 */
	
	 @Override 
	 public void onReceive(Context context, Intent intent){
		 super.onReceive(context, intent);
		 System.out.println("Got here" + intent.getAction().toString());
		 
		 	// Set up vibrator, LOL
			Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			
		 SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, 0);
		 
		 RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.egg_layout);
	//	remoteViews.setTextViewText(R.id.update, String.valueOf(3));
	//	remoteViews.setViewVisibility(R.id.update, 0);
	
		 MediaPlayer mPlay = MediaPlayer.create(context, R.raw.beep);  
		 if(intent.getAction().equals(APP_WIDGET_NEXT)){
			 vibrator.vibrate(20);
			 mPlay.start(); 
			 clickNext(remoteViews, sharedPreferences);
		 }  else if (intent.getAction().equals(APP_WIDGET_MID)){
			 vibrator.vibrate(20);
			 mPlay.start(); 
			 clickMid(context, remoteViews, sharedPreferences);
		 }  else if (intent.getAction().equals(APP_WIDGET_PREV)){
			 vibrator.vibrate(20);
			 mPlay.start(); 
			 clickPrev(remoteViews, sharedPreferences);
			 
		 }

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		    ComponentName thisAppWidget = new ComponentName(context.getPackageName(), UshiDatchi.class.getName());
		    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		    appWidgetManager.updateAppWidget(thisAppWidget, remoteViews);

	 }
	 
	 private void clickNext(RemoteViews remoteViews,
			SharedPreferences sharedPreferences) {
		 
		 // Get current selection and current view
		 int curSelection = sharedPreferences.getInt("curSelection", 0); // will be an index
		 String curView = sharedPreferences.getString("curView", "mainMenu"); // string
		 
		 // Start with if curView is not statMenu or any other that goes through screens
		 if(!curView.equals("statMenu")){ 
			 curSelection++; 
			 System.out.println(curSelection);
			 redraw(remoteViews, sharedPreferences, curSelection, curView);
		 }
		 
	}

	private void redraw(RemoteViews remoteViews,
			SharedPreferences sharedPreferences, int curSelection,
			String curView) {
		
		sharedPreferences.edit().putString("curView",curView).commit(); // Put new view as current
		hideAllOthers(curView, remoteViews); // Hide all other views
		if(curSelection!=-1){ // -1 can be for views without options
			int[] imgToDisplay = {}; // list of possible images to display from view
			int viewToChange = -1; // the id of the image to change
			if(curView.equals("mainMenu")){
				imgToDisplay = new int[]{ R.drawable.menu0, R.drawable.menu1, R.drawable.menu2, 
							R.drawable.menu3, R.drawable.menu4, R.drawable.menu5,
							R.drawable.menu6, R.drawable.menu7, R.drawable.menu8, R.drawable.menu9};
			
			} else if (curView.equals("eatMenu")){
				imgToDisplay = new int[]{ R.drawable.eatmenu00, R.drawable.eatmenu01 };	
				viewToChange = R.id.eatMenu;
		
			} else if (curView.equals("lightMenu")){
				//imgToDisplay = new int[]{ R.drawable.lightmenu00, R.drawable.lightmenu01 };
			}
			if(curSelection >= imgToDisplay.length){ curSelection = 0; }
			// Menu should be part of the same image, so prob delete this next part when that is so
			if(curView.equals("mainMenu")){
				if(curSelection<6){
					 remoteViews.setImageViewResource(R.id.topMenu, imgToDisplay[curSelection]);
					 remoteViews.setImageViewResource(R.id.botMenu, R.drawable.menu10);
				} else {
					remoteViews.setImageViewResource(R.id.topMenu, R.drawable.menu0);
					 remoteViews.setImageViewResource(R.id.botMenu, imgToDisplay[curSelection]);
				}
			} else {
			// Finally, change the image
				System.out.println("Here we are");
				System.out.println(viewToChange + " " + imgToDisplay[curSelection]);
			//remoteViews.setImageViewResource(imgToChange, imgToDisplay[curSelection]);
			remoteViews.setImageViewResource(R.id.eatMenu, imgToDisplay[curSelection]);
			}
			sharedPreferences.edit().putInt("curSelection", curSelection).commit(); // Make sure the curSelection is updated
		}
		
	}

	private void clickMid(Context context, RemoteViews remoteViews,
			SharedPreferences sharedPreferences) {
		
		 int curSelection = sharedPreferences.getInt("curSelection", 0); // will be an index
		 String curView = sharedPreferences.getString("curView", "mainMenu"); // string
		 
		 if(curView.equals("mainMenu")){
			 if(curSelection==2){
				 //sharedPreferences.edit().putInt("curSelection", 0).commit();
				 redraw(remoteViews,sharedPreferences,0,"eatMenu");
			 }
		 } else if(curView.equals("eatMenu")){

			 playAnim(context, "eat",curSelection);
			 
		 }
	}

	private void playAnim(Context context, String anim, int curSelection) {
		if(anim.equals("eat")){
			int animation[] = new int[] { R.drawable.eat01, R.drawable.eat02, R.drawable.eat03 }; // Change to the ids to the things to be animated
			// Can't actually use an animation so have to do an animation list manually
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new animTask(context,animation), 0, 1000);
			
			
		}
	}
	
	private class animTask extends TimerTask {
		AppWidgetManager appWidgetManager;
		RemoteViews remoteViews;
		ComponentName thisAppWidget;
		int[] anim;
		int i;
		
		public animTask(Context context, int[] animation){
			appWidgetManager = AppWidgetManager.getInstance(context);
			thisAppWidget = new ComponentName(context.getPackageName(), UshiDatchi.class.getName());
		    remoteViews = new RemoteViews(context.getPackageName(), R.layout.egg_layout);
		    this.anim = animation;
		    hideAllOthers("animView", remoteViews);
		    i = 0;
		}

		public void run() {
			System.out.println("This is it: " + i);
				remoteViews.setImageViewResource(R.id.anim, anim[i]);
				i++;
				if(i==anim.length){ cancel(); }
				appWidgetManager.updateAppWidget(thisAppWidget, remoteViews);
		}
		
	}

	private void clickPrev(RemoteViews remoteViews,
			SharedPreferences sharedPreferences) {
		
		redraw(remoteViews,sharedPreferences,0,"mainMenu");
	}
	
	private void hideAllOthers(String curView, RemoteViews rV){
		if(curView.equals("mainMenu")){ rV.setViewVisibility(R.id.normal,0); } 
		else { rV.setViewVisibility(R.id.normal,10); }
		if(curView.equals("eatMenu")){ rV.setViewVisibility(R.id.blank,0); } 
		else { rV.setViewVisibility(R.id.blank,10); }
		if(curView.equals("animView")){ rV.setViewVisibility(R.id.anim_view,0); } 
		else { rV.setViewVisibility(R.id.anim_view,10); }
		
		//if(R.id.eatMenu!=visible){ rV.setViewVisibility(R.id.eatMenu,10); } 
		// etc
	}

	@Override
	 public void onEnabled(Context context){
		 SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, 0);
		 sharedPreferences.edit().putInt("curSelection", 0).commit();
		 sharedPreferences.edit().putString("curView", "mainMenu").commit();

		 super.onEnabled(context);
	 }
	 
}
