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
import com.example.fatinnabila.spilla.model.OtherModel;

import java.util.ArrayList;

public class OtherAdapter extends
        RecyclerView.Adapter<OtherAdapter.OtherViewHolder> {

    private Context mContext;
    private ArrayList<OtherModel> mData;

    private OnItemClick mListener;

    public OtherAdapter(Context context, OnItemClick listener) {
        mContext = context;
        mData = new ArrayList<>();

        mListener = listener;
    }

    @Override
    public OtherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_holder_other, parent, false);
        return new OtherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OtherViewHolder holder, int position) {

        OtherModel model = mData.get(position);

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        // set description as log
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(OtherModel model) {
        mData.add(model);

        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();

        notifyDataSetChanged();
    }

    public OtherModel getItem(int position) {
        return mData.get(position);
    }

    public interface OnItemClick {
        void onClick(int pos);
    }

    class OtherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView description;

        OtherViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_Vtitle);
            description = itemView.findViewById(R.id.tv_Vdescription);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(getAdapterPosition());
        }
    }
}
