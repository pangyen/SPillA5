package com.example.fatinnabila.spilla.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fatinnabila.spilla.R;
import com.example.fatinnabila.spilla.model.AppointmentModel;

import java.util.ArrayList;

/**
 * Created by fatin nabila on 15/3/2018.
 */
public class AppointmentAdapter extends
        RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context mContext;
    private ArrayList<AppointmentModel> mData;

    private AppointmentAdapter.OnItemClick mListener;

    public AppointmentAdapter(Context context, AppointmentAdapter.OnItemClick listener) {
        mContext = context;
        mData = new ArrayList<>();

        mListener = (OnItemClick) listener;
    }

    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_holder_appointment, parent, false);
        return new AppointmentAdapter.AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppointmentAdapter.AppointmentViewHolder holder, int position) {

        AppointmentModel model = mData.get(position);

        holder.atitle.setText(model.getTitle());
        holder.adate.setText(model.getDate());
        holder.atime.setText(model.getTime());
        holder.aplace.setText(model.getPlace());
        // set description as log
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(AppointmentModel model) {
        mData.add(model);

        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();

        notifyDataSetChanged();
    }

    public AppointmentModel getItem(int position) {
        return mData.get(position);
    }

    public interface OnItemClick {
        void onClick(int pos);
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView atitle;
        private TextView adate;
        private TextView atime;
        private TextView aplace;

        AppointmentViewHolder(View itemView) {
            super(itemView);

            atitle = itemView.findViewById(R.id.tv_Atitle);
            adate = itemView.findViewById(R.id.tv_Adate);
            atime=itemView.findViewById(R.id.tv_Atime);
            aplace=itemView.findViewById(R.id.tv_Aplace);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(getAdapterPosition());
        }
    }
}
