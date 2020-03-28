package com.example.bakingtime.utils;

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
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.example.bakingtime.R;

import androidx.appcompat.app.ActionBar;

public class ActionBarUtils {

    public static void setToolbarTitle(Context context, ActionBar actionBar, boolean homeAsUpEnabled) {
        actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        actionBar.setTitle(getSharedPreferencesRecipeName(context));
    }

    public static String getSharedPreferencesRecipeName(Context context) {
        Resources resources = context.getResources();
        String prefRecipeNameKey = resources.getString(R.string.pref_recipe_name_key);
        String prefDefaultRecipeName = resources.getString(R.string.pref_default_recipe_name);
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(prefRecipeNameKey, prefDefaultRecipeName);
    }
}
