package com.metagaming.allstarsolitare.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.metagaming.allstarsolitare.R;

public class WidgetIntentService extends IntentService {

    public WidgetIntentService() {
        super("allStarWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Get all widget instance ids
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds;
        /*if(intent.hasExtra("size")){
            if(intent.getStringExtra("size").equals("med")){
                appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                        ScoresWidgetMedium.class));
            }else{
                appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                        ScoresWidgetLarge.class));
            }
        }else{*/
            appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                    ScoresWidget.class));
        //}

        //
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.scores_widget_list);

        for(int appWidgetId : appWidgetIds){
            //get the view used and it's options bundle
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.scores_widget);
            Bundle bundle = appWidgetManager.getAppWidgetOptions(appWidgetId);
            //
            if(bundle.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)){
                if(getCellsForSize(bundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) == 2){
                    //Normal
                    views.setRemoteAdapter(R.id.scores_widget_list,
                            new Intent(WidgetIntentService.this, WidgetViewService.class));
                    Log.d("WidgetIntent", "Normal");

                }/*else if(getCellsForSize(bundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) == 3){
                    //Later make it display time as well

                    views.setRemoteAdapter(R.id.widget_list,
                            new Intent(WidgetIntentService.this, WidgetViewsServiceSmall.class));
                    Log.d("WidgetIntent", "Plus Time");

                }*/
            }

            //
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private static int getCellsForSize(int size) {
        int n = 2;
        while (70 * n - 30 < size) {
            n++;
        }
        return n - 1;
    }
}
