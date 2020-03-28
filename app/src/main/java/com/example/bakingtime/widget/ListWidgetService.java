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

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingtime.R;
import com.example.bakingtime.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext());
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private List<String> mIngredients;

        ListRemoteViewsFactory(Context context) {
            mContext = context;
            mIngredients = new ArrayList<>();
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            mIngredients = StringUtils.getSharedPreferencesIngredientsList(mContext);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mIngredients == null) return 0;
            return mIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_ingredients_list_item);
            views.setTextViewText(R.id.ingredient_details, mIngredients.get(position));
            views.setOnClickFillInIntent(R.id.ingredient_details, new Intent());
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
