package com.cduestc.DriverHelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.api.Tag;
import com.cduestc.DriverHelper.bean.Coach;
import com.cduestc.DriverHelper.utils.GlideCircleTransform;

import java.util.ArrayList;

/**
 * Created by c on 2017/3/4.
 */
public class CoachesListAdapter extends BaseAdapter {

    private ArrayList<Coach> coaches;
    private Context context;

    /**
     * 构造传入 上下文context
     * @param coaches
     * @param context
     */
    public CoachesListAdapter(ArrayList<Coach> coaches, Context context) {
        this.coaches = coaches;
        this.context = context;
    }

    /**
     * 适配器获取列表总个数  必须重写 不然列表无法显示
     * @return 列表元素个数
     */
    @Override
    public int getCount() {
        return coaches.size();
    }

    /**
     * 获取单个元素
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {

        return coaches.get(position);
    }

    /**
     * 获取元素ID
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 获取单个视图
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CoachSimpleViewHolder viewHolder;

        if (convertView == null){

            /**
             * 加载视图
             */
            convertView = LayoutInflater.from(context).inflate(R.layout.coach_list_item,parent,false);

            viewHolder = new CoachSimpleViewHolder((TextView) convertView.findViewById(R.id.tv_coach_name),
                    (TextView)convertView.findViewById(R.id.tv_coach_phone),
                    (TextView)convertView.findViewById(R.id.tv_coach_info),
                    (ImageView)convertView.findViewById(R.id.iv_coach_icon));
            /**
             * 设置viewHolder,避免重复findViewById方法浪费时间
             */
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (CoachSimpleViewHolder) convertView.getTag();
        }

        /**
         * 加载数据
         */
        Glide.with(context)
                .load(Tag.GET_USER_ICON + coaches.get(position).getUid()+"&random="+Math.random())
                .placeholder(R.mipmap.user_icon)
                .error(R.mipmap.user_icon)
                .transform(new GlideCircleTransform(context))
                .crossFade(500)
                .thumbnail(0.4f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.iv_coach_icon);

        viewHolder.tv_coach_info.setText( "简介 : "+coaches.get(position).getCoachInfo());
        if (coaches.get(position).getCoachInfo() == null){
            viewHolder.tv_coach_info.setText( "简介 : 无");
        }
        viewHolder.tv_coach_name.setText( "名字:"+coaches.get(position).getName());
        viewHolder.tv_coach_number.setText("电话:"+coaches.get(position).getUid());

        return convertView;
    }


    protected class CoachSimpleViewHolder {
        protected TextView tv_coach_name;
        protected TextView tv_coach_number;
        protected TextView tv_coach_info;
        protected ImageView iv_coach_icon;

        public CoachSimpleViewHolder(TextView tv_coach_name, TextView tv_coach_number, TextView tv_coach_info, ImageView iv_coach_icon) {
            this.tv_coach_name = tv_coach_name;
            this.tv_coach_number = tv_coach_number;
            this.tv_coach_info = tv_coach_info;
            this.iv_coach_icon = iv_coach_icon;
        }
    }

}
