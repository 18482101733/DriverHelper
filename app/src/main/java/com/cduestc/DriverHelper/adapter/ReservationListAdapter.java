package com.cduestc.DriverHelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.api.Tag;
import com.cduestc.DriverHelper.bean.ReservationBody;

import java.util.ArrayList;

/**
 * Created by c on 2017/3/20.
 */
public class ReservationListAdapter extends BaseAdapter {

    private int userPower;
    private Context context;
    private ArrayList<ReservationBody> reservationBodies;

    public ReservationListAdapter(Context context, ArrayList<ReservationBody> reservationBodies,int userPower) {
        this.context = context;
        this.reservationBodies = reservationBodies;
        this.userPower = userPower;
    }


    @Override
    public int getCount() {
        return reservationBodies.size();
    }

    @Override
    public Object getItem(int position) {
        return reservationBodies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReservationViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.reservation_list_item,parent,false);
            viewHolder = new ReservationViewHolder(
                    (ImageView)convertView.findViewById(R.id.iv_icon),
                    (TextView)convertView.findViewById(R.id.tv_reservation_notice),
                    (TextView)convertView.findViewById(R.id.tv_reservation_date),
                    (TextView)convertView.findViewById(R.id.tv_reservation_coach));
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ReservationViewHolder) convertView.getTag();
        }

        switch (userPower){
            case Tag.USER_STUDENT:
                viewHolder.tv_reservation_coach.setText("教练 : "+reservationBodies.get(position).getTeacherName());
                break;

            case Tag.USER_COACH:
                viewHolder.tv_reservation_coach.setText("学员 : " + reservationBodies.get(position).getStudentName());
                break;
        }

        viewHolder.tv_reservation_date.setText("日期 : "+reservationBodies.get(position).getAppointDate());

        switch (reservationBodies.get(position).getAppointTime()){
            case 1:
                viewHolder.tv_reservation_time.setText("上午");
                break;
            case 2:
                viewHolder.tv_reservation_time.setText("下午");
                break;

        }


        return convertView;
    }


    private class ReservationViewHolder{
        protected ImageView iv_icon;
        protected TextView tv_reservation_time;
        protected TextView tv_reservation_date;
        protected TextView tv_reservation_coach;

        public ReservationViewHolder(ImageView iv_icon, TextView tv_reservation_time, TextView tv_reservation_date,TextView tv_reservation_coach) {
            this.iv_icon = iv_icon;
            this.tv_reservation_time = tv_reservation_time;
            this.tv_reservation_date = tv_reservation_date;
            this.tv_reservation_coach =tv_reservation_coach;
        }
    }

}
