package com.metagaming.allstarsolitare.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetViewService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return WidgetHelper.makeFactory(getContentResolver(), getPackageName(), 1, this);
    }
}
