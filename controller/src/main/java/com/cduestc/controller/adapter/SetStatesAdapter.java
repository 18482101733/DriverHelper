package com.cduestc.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cduestc.controller.R;
import com.cduestc.controller.api.Tag;
import com.cduestc.controller.bean.User;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by c on 2017/4/27.
 */
public class SetStatesAdapter extends RecyclerView.Adapter<SetStatesAdapter.StatesViewHolder>  {

    private Context context;
    private ArrayList<User> users;
    private ArrayList<User> filters;
    private OnItemClickListener onItemClickListener;
    private ArrayList<User> test;

    public SetStatesAdapter(ArrayList<User> users,Context context) {
        this.filters = users;
        this.users = users;
        this.context = context;

        test = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public StatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_states_list_item,parent,false);
        return new StatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StatesViewHolder holder,  int position) {
        holder.tv_name.setText("姓名 : " + filters.get(position).getName());
        holder.tv_phone.setText("电话 : " + filters.get(position).getUid());
        switch (filters.get(position).getState()){
            case Tag.STATE_READ:
                holder.tv_states.setText("权限:无");
                break;
            case Tag.STATE_ALL:
                holder.tv_states.setText("权限:可预约");
                break;
        }

        Glide.with(context).load("http://119.23.126.79:10648/photo/get?useruid="+filters.get(position).getUid()+"&random="+Math.random())
                .bitmapTransform(new CropCircleTransformation(context))
                .error(R.mipmap.list_user)
                .placeholder(R.mipmap.list_user)
                .into(holder.iv_user_icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,holder.getAdapterPosition());
            }
        });

    }

    public void filter(ArrayList<User> filters){
        this.filters = new ArrayList<>();
        this.filters.addAll(filters);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return filters.size();
    }



    class StatesViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView tv_phone;
        private TextView tv_states;
        private ImageView iv_user_icon;
        public StatesViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_states = (TextView) itemView.findViewById(R.id.tv_states);
            iv_user_icon = (ImageView) itemView.findViewById(R.id.iv_user_icon);
        }
    }


    class StatesFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String keyWord = constraint.toString();
            if (keyWord.isEmpty()){
                SetStatesAdapter.this.filters = users;
            }else {
                ArrayList<User> temp = new ArrayList<>();
                for (User user:users) {
                    if (user.getUid().contains(keyWord)){
                        temp.add(user);
                    }
                }
                filters = temp;
                test.clear();
                test.addAll(temp);
            }
            FilterResults results = new FilterResults();
            results.values = filters;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filters = (ArrayList<User>) results.values;
            filters = test;
            SetStatesAdapter.this.notifyDataSetChanged();
        }
    }


    public interface OnItemClickListener{
        void onItemClick(View v,int position);
    }

}
