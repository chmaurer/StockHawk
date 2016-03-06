package com.sam_chordas.android.stockhawk.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 Created by Christian on 06.03.2016.
 */
public class QuoteWidgetProvider extends AppWidgetProvider {

    public static int widget_id = 0;
    private AppWidgetManager mappWidgetManager;

    @Override public void onReceive (Context context, Intent intent) {
        super.onReceive (context, intent);
        if (widget_id == 0) {
            widget_id = intent.getIntExtra (AppWidgetManager.EXTRA_APPWIDGET_IDS, 0);
        }
        int widgets[] = {widget_id};
        if (intent.getAction ().equals ("android.appwidget.action.APPWIDGET_UPDATE")) {
            onUpdate (context, mappWidgetManager, widgets);
        }
    }

    @Override public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews (context, appWidgetManager, widgetId);
            if (!Utils.isNetworkAvailable (context)) {
                mView.setTextViewText (R.id.widget_head, context.getResources ().getString (R.string.widget_title) + " " + context.getResources ().getString (R.string.out_of_date));
            }
            if (mappWidgetManager != null) {
                mappWidgetManager.updateAppWidget (widgetId, mView);
            }
        }
        super.onUpdate (context, mappWidgetManager, appWidgetIds);
    }

    @SuppressWarnings ("deprecation") @SuppressLint ("NewApi") private RemoteViews initViews (
            Context context, AppWidgetManager widgetManager, int widgetId) {
        mappWidgetManager = widgetManager;

        RemoteViews mView = new RemoteViews (context.getPackageName (), R.layout.widget_collection);

        Intent intent = new Intent (context, WidgetService.class);
        intent.putExtra (AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);


        intent.setData (Uri.parse (intent.toUri (Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter (widgetId, R.id.widget_list, intent);

        return mView;
    }
}
