package com.metagaming.allstarsolitare.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.metagaming.allstarsolitare.R;
import com.metagaming.allstarsolitare.database.DBContract;


class WidgetHelper {

    //make column name list to grab:
    private static final String[] SCORES_COLUMNS = {
            DBContract.DBEntry.COLUMN_USER,
            DBContract.DBEntry.COLUMN_POINTS/*,
            DBContract.DBEntry.COLUMN_TIME*/
    };
    //references to the returned Cursor's data by index values
    private static final int INDEX_USER = 0;
    private static final int INDEX_POINTS = 1;
    //static final int INDEX_TIME = 2;

    static RemoteViewsService.RemoteViewsFactory makeFactory(final ContentResolver contentResolver, final String packageName, final int size, final Context context){
        return new RemoteViewsService.RemoteViewsFactory() {

            Cursor data = null;
            long idToken;

            @Override
            public void onCreate() {
                //
            }

            @Override
            public void onDataSetChanged() {
                if(data != null){
                    data.close();
                }

                idToken = Binder.clearCallingIdentity();
                data = contentResolver.query(DBContract.DBEntry.URI, SCORES_COLUMNS, null,
                        null, DBContract.DBEntry.COLUMN_POINTS);
            }

            @Override
            public void onDestroy() {
                if(data != null){
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                RemoteViews views = new RemoteViews(packageName, R.layout.scores_widget_list_item);

                //
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                /*
                if(size == 2){
                    // this is where you should put the time-included widget
                    // RemoteView like "views", but instead the layout for the
                    // time-included widget
                }*/

                //GET ROW'S SCORE DATA:
                String userName = data.getString(INDEX_USER);
                int points = data.getInt(INDEX_POINTS);
                //String time = data.getString(INDEX_TIME);

                if(size == 1){
                    //insert data into the list's views
                    views.setTextViewText(R.id.scores_widget_user, userName);
                    views.setTextViewText(R.id.scores_widget_points, ""+points);
                }//else if...

                //
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                if(size == 1){
                    return new RemoteViews(packageName, R.layout.scores_widget_list_item);
                }/*else if(size == 2){
                    //MAKE IT WITH TIME
                }*/else{
                    return new RemoteViews(packageName, R.layout.scores_widget_list_item);
                }
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }

}
