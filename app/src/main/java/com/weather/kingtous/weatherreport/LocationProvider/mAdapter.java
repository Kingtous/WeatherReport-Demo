package com.weather.kingtous.weatherreport.LocationProvider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weather.kingtous.weatherreport.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class mAdapter extends RecyclerView.Adapter<mAdapter.myholder>  {

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }


    Context context;
    public ArrayList<String> list;

    //
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }


    mAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public void update(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_text, parent, false);
        myholder holder = new myholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final myholder holder, int position) {
        final String tmp = list.get(position);
        holder.textView.setText(tmp);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView,position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class myholder extends RecyclerView.ViewHolder {

        TextView textView;

        public myholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.CityListText);
        }
    }



    public void setFilter(ArrayList<String> words)
    {
        list=words;
        notifyDataSetChanged();
    }

}
