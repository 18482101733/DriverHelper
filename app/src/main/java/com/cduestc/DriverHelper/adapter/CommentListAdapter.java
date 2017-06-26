package com.cduestc.DriverHelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.bean.Comment;

import java.util.ArrayList;

/**
 * Created by c on 2017/3/16.
 */
public class CommentListAdapter  extends BaseAdapter{

    private ArrayList<Comment> comments;
    private Context context;

    /**
     * 构造传入 上下文context
     * @param comments
     * @param context
     */
    public CommentListAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentViewHolder viewHolder;

        if (convertView == null){

            convertView = LayoutInflater.from(context).inflate(R.layout.comment_list_item,parent,false);
            viewHolder = new CommentViewHolder(
                    (TextView)convertView.findViewById(R.id.tv_comment_name),
                    (TextView)convertView.findViewById(R.id.tv_comment_content),
                    (TextView)convertView.findViewById(R.id.tv_comment_time));
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (CommentViewHolder) convertView.getTag();
        }

        viewHolder.tv_comment_content.setText(comments.get(position).getDiscussMatter());
        viewHolder.tv_comment_name.setText(comments.get(position).getSenderID());
        viewHolder.tv_comment_time.setText(comments.get(position).getDiscussTime());

        return convertView;
    }

    private class CommentViewHolder{

        private TextView tv_comment_name;
        private TextView tv_comment_content;
        private TextView tv_comment_time;

        public CommentViewHolder(TextView tv_comment_name, TextView tv_comment_content, TextView tv_comment_time) {
            this.tv_comment_name = tv_comment_name;
            this.tv_comment_content = tv_comment_content;
            this.tv_comment_time = tv_comment_time;
        }
    }
}
