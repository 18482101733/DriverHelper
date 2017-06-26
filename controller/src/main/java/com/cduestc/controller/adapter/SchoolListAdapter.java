package com.cduestc.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cduestc.controller.R;
import com.cduestc.controller.bean.School;

import java.util.ArrayList;

/**
 * Created by c on 2017/5/2.
 */
public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.ViewHolder> {

    private ArrayList<School> schools;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SchoolListAdapter(ArrayList<School> schools) {
        this.schools = schools;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.tv_address.setText("地址:"+schools.get(position).getAddress());
        holder.tv_city.setText("城市:"+schools.get(position).getCity());
        holder.tv_name.setText("驾校名:"+schools.get(position).getName());
        holder.tv_phoneNum.setText("联系电话:"+schools.get(position).getPhoneNum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_phoneNum,tv_city,tv_address;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_phoneNum = (TextView) itemView.findViewById(R.id.tv_phoneNum);
            tv_city = (TextView) itemView.findViewById(R.id.tv_city);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

}
