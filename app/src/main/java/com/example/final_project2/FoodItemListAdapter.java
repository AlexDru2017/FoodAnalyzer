package com.example.final_project2;


import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FoodItemListAdapter extends ArrayAdapter<FoodItem>{

    private List<FoodItem> foodItems;

    public FoodItemListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<FoodItem> objects) {
        super(context, resource, objects);
        foodItems = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_food_item, parent, false);
        }

        FoodItem foodItem = foodItems.get(position);

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.rgb(249, 236, 223));
        } else {
            convertView.setBackgroundColor(Color.rgb(234, 219, 206));
        }

        TextView foodNameTextView = (TextView) convertView.findViewById(R.id.foodNameTextView);
        foodNameTextView.setText(foodItem.getItemName());
        TextView foodCategoryTextView = (TextView) convertView.findViewById(R.id.foodCategoryTextView);
        foodCategoryTextView.setText(foodItem.getItemCategory());

        return convertView;
    }
}
