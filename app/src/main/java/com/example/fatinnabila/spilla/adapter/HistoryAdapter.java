package com.example.fatinnabila.spilla.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fatinnabila.spilla.R;
import com.example.fatinnabila.spilla.model.HistoryModel;

import java.util.ArrayList;

/**
 * Created by fatin nabila on 16/3/2018.
 */
public class HistoryAdapter extends
        RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mContext;
    private ArrayList<HistoryModel> mData;

    private OnItemClick mListener;

    public HistoryAdapter(Context context, OnItemClick listener) {
        mContext = context;
        mData = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_holder_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {

        HistoryModel model = mData.get(position);
        holder.htitle.setText(model.getTitle());
        holder.hdes.setText(model.getDescription());
        holder.hstart.setText(model.getStart());
        holder.hend.setText(model.getEnd());
        // set description as log
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(HistoryModel model) {
        mData.add(model);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public HistoryModel getItem(int position) {
        return mData.get(position);
    }

    public interface OnItemClick {
        void onClick(int pos);
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView htitle;
        private TextView hdes;
        private TextView hstart;
        private TextView hend;

        HistoryViewHolder(View itemView) {
            super(itemView);

            htitle = itemView.findViewById(R.id.tv_Htitle);
            hdes = itemView.findViewById(R.id.tv_Hdes);
            hstart = itemView.findViewById(R.id.tv_Hstart);
            hend = itemView.findViewById(R.id.tv_Hend);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(getAdapterPosition());
        }
    }
}
