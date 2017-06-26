package com.cduestc.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cduestc.controller.R;
import com.cduestc.controller.api.Tag;
import com.cduestc.controller.bean.Coach;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by c on 2017/5/2.
 */
public class CoachListAdapter extends RecyclerView.Adapter<CoachListAdapter.ViewHolder> {

    private ArrayList<Coach> coaches;
    private Context context;

    public CoachListAdapter(ArrayList<Coach> coaches,Context context) {
        this.context = context;
        this.coaches = coaches;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_info_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (coaches.get(position).getState()){
            case Tag.STATE_READ:
                holder.tv_states.setText("权限:未授权");
                break;

            case Tag.STATE_ALL:
                holder.tv_states.setText("权限:授权");
                break;
        }
        Glide.with(context)
                .load("http://119.23.126.79:10648/photo/get?useruid="+coaches.get(position).getUid()+"&random="+Math.random())
                .placeholder(R.mipmap.list_user)
                .error(R.mipmap.list_user)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.iv_user_icon);
        holder.tv_uid.setText("电话:"+coaches.get(position).getUid());
        holder.tv_address.setText("地址:"+coaches.get(position).getAddress());
        holder.tv_idNum.setText("身份证:"+coaches.get(position).getIdNum());
        holder.tv_plateNum.setText("车牌号:"+coaches.get(position).getPlateNum());
        holder.tv_name.setText("姓名:"+coaches.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return coaches.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView iv_user_icon;
        protected TextView tv_name,tv_uid,tv_idNum,tv_plateNum,tv_address,tv_states;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_user_icon = (ImageView) itemView.findViewById(R.id.iv_user_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_uid = (TextView) itemView.findViewById(R.id.tv_uid);
            tv_plateNum = (TextView) itemView.findViewById(R.id.tv_plateNum);
            tv_idNum = (TextView) itemView.findViewById(R.id.tv_idNum);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_states = (TextView) itemView.findViewById(R.id.tv_states);
        }
    }

}
