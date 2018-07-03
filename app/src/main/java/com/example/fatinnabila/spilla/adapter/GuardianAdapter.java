package com.example.fatinnabila.spilla.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fatinnabila.spilla.R;
import com.example.fatinnabila.spilla.model.GuardianModel;


import java.util.ArrayList;

/**
 * Created by fatin nabila on 21/3/2018.
 */

public class GuardianAdapter  extends
        RecyclerView.Adapter<GuardianAdapter.GuardianViewHolder> {

    private Context mContext;
    private ArrayList<GuardianModel> mData;

    private GuardianAdapter.OnItemClick mListener;

    public GuardianAdapter(Context context, GuardianAdapter.OnItemClick listener) {
        mContext = context;
        mData = new ArrayList<>();

        mListener = listener;
    }

    @Override
    public GuardianAdapter.GuardianViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_holder_guardian, parent, false);
        return new GuardianAdapter.GuardianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuardianAdapter.GuardianViewHolder holder, int position) {

        GuardianModel model = mData.get(position);

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.email.setText(model.getEmail());
        // set description as log
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(GuardianModel model) {
        mData.add(model);

        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();

        notifyDataSetChanged();
    }

    public GuardianModel getItem(int position) {
        return mData.get(position);
    }

    public interface OnItemClick {
        void onClick(int pos);
    }

    class GuardianViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView description;
        private TextView email;

        GuardianViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_titleG);
            description = itemView.findViewById(R.id.tv_descriptionG);
            email=itemView.findViewById(R.id.tv_emailG);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(getAdapterPosition());
        }
    }
}
