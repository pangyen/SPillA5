package com.example.fatinnabila.spilla.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fatinnabila.spilla.R;
import com.example.fatinnabila.spilla.model.Box1Model;

import java.util.ArrayList;

/**
 * Created by fatin nabila on 29/3/2018.
 */

public class Box1Adapter extends RecyclerView.Adapter<Box1Adapter.Box1ViewHolder>{
    private Context mContext;
    private ArrayList<Box1Model> mData;

    private Box1Adapter.OnItemClick mListener;

    public Box1Adapter(Context context, Box1Adapter.OnItemClick listener) {
        mContext = context;
        mData = new ArrayList<>();

        mListener = listener;
    }

    @Override
    public Box1Adapter.Box1ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_holder_box1, parent, false);
        return new Box1Adapter.Box1ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Box1Adapter.Box1ViewHolder holder, int position) {

        Box1Model model = mData.get(position);

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        // set description as log
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(Box1Model model) {
        mData.add(model);

        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();

        notifyDataSetChanged();
    }

    public Box1Model getItem(int position) {
        return mData.get(position);
    }

    public interface OnItemClick {
        void onClick(int pos);
    }

    class Box1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView description;

        Box1ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            description = itemView.findViewById(R.id.tv_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(getAdapterPosition());
        }
    }
}
