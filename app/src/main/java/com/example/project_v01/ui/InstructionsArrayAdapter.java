package com.example.project_v01.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project_v01.R;
import com.example.project_v01.recipe.Ingredient;

import java.util.ArrayList;

public class InstructionsArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> InstructionsArrayList;

    public InstructionsArrayAdapter(Context context, int resource, ArrayList<String> InstructionsArrayList) {
        super(context, resource, InstructionsArrayList);
        this.context = context;
        this.InstructionsArrayList = InstructionsArrayList;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {  // inflate the view (course_List_item) activity


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.instructions_layout, null); // here we inflate it


        TextView textView = (TextView) view.findViewById(R.id.Instruction_textview);
        textView.setText(InstructionsArrayList.get(position).toString());


        return view;
    }
}
