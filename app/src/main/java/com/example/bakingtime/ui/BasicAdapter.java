package com.example.bakingtime.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BasicAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> mRecipesData;

    BasicAdapter(Context context, List<Integer> recipesData) {
        mContext = context;
        mRecipesData = recipesData;
    }

    /**
     * Returns the number of items the adapter will display
     */
    @Override
    public int getCount() {
        if (mRecipesData == null) return 0;
        return mRecipesData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Creates a new ImageView for each item referenced by the adapter
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // If the view is not recycled, this creates a new ImageView to hold an image
            textView = new TextView(mContext);
        } else {
            textView = (TextView) convertView;
        }

        textView.setText("Nutella Pie");
        return textView;
    }
}
