package com.example.fatinnabila.spilla.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.fatinnabila.spilla.R;
import com.example.fatinnabila.spilla.model.Box1Model;

import java.util.List;

/**
 * Created by fatin nabila on 29/3/2018.
 */
public class Box1Adapter extends
        ArrayAdapter<Box1Model> {
    private Activity context;
    List<Box1Model> boxs1;

    public Box1Adapter(Activity context, List<Box1Model> pillb1) {
        super(context, R.layout.layout_box1list, pillb1);
        this.context = context;
        this.boxs1 = pillb1;
    }
    public void addData(Box1Model model) {
        boxs1.add(model);

        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_box1list, null, true);


        TextView textViewPills = (TextView) listViewItem.findViewById(R.id.textViewPills);
       // TextView textViewDose = (TextView) listViewItem.findViewById(R.id.textViewDose);

        Box1Model box1Model = boxs1.get(position);
        textViewPills.setText(box1Model.getTitle());
     //  textViewDose.setText(box1Model.getDose());

        return listViewItem;
    }

}
