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
import com.cduestc.controller.bean.Student;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by c on 2017/5/2.
 *
 */
public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private ArrayList<Student> students;
    private Context context;

    public StudentListAdapter(ArrayList<Student> students, Context context) {
        this.students = students;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_info_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        switch (students.get(position).getState()){
            case Tag.STATE_READ:
                holder.tv_states.setText("权限:未授权");
                break;

            case Tag.STATE_ALL:
                holder.tv_states.setText("权限:授权");
                break;
        }

        Glide.with(context)
                .load("http://119.23.126.79:10648/photo/get?useruid="+students.get(position).getUid()+"&random="+Math.random())
                .placeholder(R.mipmap.list_user)
                .error(R.mipmap.list_user)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.iv_user_icon);

        holder.tv_uid.setText("电话:"+students.get(position).getUid());
        holder.tv_address.setText("地址:"+students.get(position).getAddress());
        holder.tv_idNum.setText("身份证:"+students.get(position).getIdNum());
        holder.tv_register.setText("注册信息:"+students.get(position).getAdmissionTime());
        holder.tv_name.setText("姓名:"+students.get(position).getName());
        holder.tv_reservation.setText("学车次数:" + students.get(position).getAppointTnum());
        holder.tv_drop_reservation.setText("爽约次数:" + students.get(position).getAppointFnum());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView iv_user_icon;
        protected TextView tv_name,tv_uid,tv_idNum,tv_register,tv_address,tv_states,tv_reservation,tv_drop_reservation;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_user_icon = (ImageView) itemView.findViewById(R.id.iv_user_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_uid = (TextView) itemView.findViewById(R.id.tv_uid);
            tv_register = (TextView) itemView.findViewById(R.id.tv_register);
            tv_idNum = (TextView) itemView.findViewById(R.id.tv_idNum);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_states = (TextView) itemView.findViewById(R.id.tv_states);
            tv_reservation = (TextView) itemView.findViewById(R.id.tv_reservation);
            tv_drop_reservation = (TextView) itemView.findViewById(R.id.tv_drop_reservation);
        }
    }
}
