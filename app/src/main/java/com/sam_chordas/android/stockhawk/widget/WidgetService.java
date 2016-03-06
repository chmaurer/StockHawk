package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 Created by Christian on 06.03.2016.
 */
public class WidgetService extends RemoteViewsService {

    @Override public RemoteViewsFactory onGetViewFactory (Intent intent) {

        WidgetDataProvider dataProvider = new WidgetDataProvider (getApplicationContext (), intent);
        return dataProvider;
    }

}