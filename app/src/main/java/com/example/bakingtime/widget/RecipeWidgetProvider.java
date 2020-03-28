package com.example.bakingtime.widget;

/*
    Copyright (C) 2020 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.RemoteViews;

import com.example.bakingtime.R;
import com.example.bakingtime.ui.MainActivity;
import com.example.bakingtime.ui.RecipeIngredientsActivity;
import com.example.bakingtime.utils.ActionBarUtils;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent mainActivityPendingIntent =
                PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent ingredientsActivityIntent = new Intent(context, RecipeIngredientsActivity.class);
        ingredientsActivityIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.intent_widget));
        PendingIntent ingredientsActivityPendingIntent =
                PendingIntent.getActivity(context, 0, ingredientsActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent serviceIntent = new Intent(context, ListWidgetService.class);

        String recipeName = ActionBarUtils.getSharedPreferencesRecipeName(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        views.setPendingIntentTemplate(R.id.widget_list_view, ingredientsActivityPendingIntent);

        Resources resources = context.getResources();
        if (recipeName.equals(resources.getString(R.string.pref_default_recipe_name))) {
            views.setOnClickPendingIntent(R.id.widget_recipe_name, mainActivityPendingIntent);
        } else {
            views.setOnClickPendingIntent(R.id.widget_recipe_name, ingredientsActivityPendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

