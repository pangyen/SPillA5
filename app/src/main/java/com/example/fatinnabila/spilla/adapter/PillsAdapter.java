package com.example.fatinnabila.spilla.adapter;

/**
 * Created by fatin nabila on 8/3/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fatinnabila.spilla.R;
import com.example.fatinnabila.spilla.model.PillsModel;

import java.util.ArrayList;

public class PillsAdapter extends RecyclerView.Adapter<PillsAdapter.PillsViewHolder> {

    private Context mContext;
    private ArrayList<PillsModel> mData;

    private OnItemClick mListener;

    public PillsAdapter(Context context, OnItemClick listener) {
        mContext = context;
        mData = new ArrayList<>();

        mListener = listener;
    }

    @Override
    public PillsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_holder_pills, parent, false);
        return new PillsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PillsViewHolder holder, int position) {

        PillsModel model = mData.get(position);

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        // set description as log
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(PillsModel model) {
        mData.add(model);

        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();

        notifyDataSetChanged();
    }

    public PillsModel getItem(int position) {
        return mData.get(position);
    }

    public interface OnItemClick {
        void onClick(int pos);
    }

    class PillsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView description;

        PillsViewHolder(View itemView) {
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
