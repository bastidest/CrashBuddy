package hackatum.de.checkcrash;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class EmergencyWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.emergency_widget);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onEnabled(Context context) {
        //Log.v("toggle_widget","Enabled is being called");

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        //retrieve a ref to the manager so we can pass a view update

        Intent i = new Intent();
        i.setClassName("hackatum.de.checkcrash", "hackatum.de.checkcrash.EmergencyActivity");
        PendingIntent myPI = PendingIntent.getActivity(context, 0, i, 0);
        //intent to start service

        // Get the layout for the App Widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.emergency_widget);

        //attach the click listener for the service start command intent
        views.setOnClickPendingIntent(R.id.widget_layout, myPI);

        //define the componenet for self
        ComponentName comp = new ComponentName(context.getPackageName(), EmergencyWidget.class.getName());

        //tell the manager to update all instances of the toggle widget with the click listener
        mgr.updateAppWidget(comp, views);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

