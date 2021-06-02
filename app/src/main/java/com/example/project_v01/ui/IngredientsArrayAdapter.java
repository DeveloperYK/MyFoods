package com.example.project_v01.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project_v01.R;
import com.example.project_v01.recipe.Ingredient;

import java.util.ArrayList;

public class IngredientsArrayAdapter extends ArrayAdapter<Ingredient>  {

    private Context context;
    private ArrayList<Ingredient> IngredientList;

    public IngredientsArrayAdapter(Context context, int resource,ArrayList<Ingredient> IngredientList) {
        super(context,resource,IngredientList);
        this.context = context;
        this.IngredientList = IngredientList;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {  // inflate the view (course_List_item) activity


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.ingredients_layout, null); // here we inflate it


        TextView textView = (TextView) view.findViewById(R.id.ingredientName);// set the content of the selected image onto the fragment both image and its name.
        textView.setText(IngredientList.get(position).toString());


        return view;

    }

}
