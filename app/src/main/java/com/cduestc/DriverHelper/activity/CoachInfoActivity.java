package com.cduestc.DriverHelper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.adapter.CommentListAdapter;
import com.cduestc.DriverHelper.api.ApiService;
import com.cduestc.DriverHelper.api.Tag;
import com.cduestc.DriverHelper.bean.Coach;
import com.cduestc.DriverHelper.bean.Comment;
import com.cduestc.DriverHelper.bean.GetCommentsResponseBody;
import com.cduestc.DriverHelper.bean.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoachInfoActivity extends AppCompatActivity implements View.OnClickListener, Callback<ResponseBody> {

    private Toolbar tb_bar;
    private ApiService apiService;
    private ImageView iv_icon,iv_send;
    private EditText et_comment;
    private ListView lv_comments;
    private TextView tv_coach_info,tv_name,tv_no_comments;
    private Coach coach;
    private User user;
    private CommentListAdapter adapter;
    private ArrayList<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_info);
        intiView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        apiService = ApiService.getInstance();

        coach = (Coach) getIntent().getSerializableExtra(Tag.COACH);
        user = (User) getIntent().getSerializableExtra(Tag.USER);
        tv_coach_info.setText("简介 : " +coach.getCoachInfo());
        tv_name.setText("姓名 : " + coach.getName());
        iv_send.setOnClickListener(this);
        Glide.with(this)
                .load(Tag.GET_USER_ICON + coach.getUid())
                .error(R.mipmap.user_icon)
                .placeholder(R.mipmap.user_icon)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(iv_icon);
        iv_icon.setOnClickListener(this);
        comments = new ArrayList<>();
        adapter = new CommentListAdapter(comments,this);
        lv_comments.setAdapter(adapter);
        apiService.getComments(this,coach.getUid());
        Log.i("321", "initData: "+"{\"teacherId\":"+"\""+coach.getUid()+"\"}");
        tv_coach_info.requestFocus();
    }

    /**
     * 初始化视图控件
     */
    private void intiView(){
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_send = (ImageView) findViewById(R.id.iv_send);
        et_comment = (EditText) findViewById(R.id.et_comment);
        lv_comments = (ListView) findViewById(R.id.lv_comments);
        tv_coach_info = (TextView) findViewById(R.id.tv_coach_info);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_no_comments = (TextView) findViewById(R.id.tv_no_comments);
        setToolbar();
    }

    /**
     * 设置标题栏
     */
    private void setToolbar() {
        tb_bar.setTitle("评论");
        tb_bar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb_bar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 标题栏返回按钮
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_icon:

                if (user.getStates() == Tag.STATUS_ONLY_READ){
                    Toast.makeText(CoachInfoActivity.this, "权限不够,无法预约,请联系驾校", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (coach.getStates() == Tag.STATUS_ONLY_READ){
                    Toast.makeText(CoachInfoActivity.this, "该教练权限不够,无法预约", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent startReservationIntent = new Intent(this,ReservationActivity.class);
                startReservationIntent.putExtra(Tag.COACH,coach);
                startReservationIntent.putExtra(Tag.USER,user);
                startActivity(startReservationIntent);

                break;

            case R.id.iv_send:

                if ("".equals(et_comment.getText()+"")){
                    Toast.makeText(CoachInfoActivity.this, "不能发送空评论", Toast.LENGTH_SHORT).show();
                    return;
                }

                tv_coach_info.requestFocus();
                final Comment comment = new Comment();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                comment.setDiscussTime(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
                comment.setSenderID(user.getUid());
                comment.setReceiverId(coach.getUid());
                comment.setDiscussMatter(et_comment.getText()+"");
                Log.i("send", "onClick: " + comment.toString());
                apiService.sendComment(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                       try{
                           if (!response.isSuccessful()){
                               Toast.makeText(CoachInfoActivity.this, "发送失败", Toast.LENGTH_SHORT).show();

                           }else{

                               String json =  response.body().string();
                               JSONObject jsonObject = new JSONObject(json);
                               if (jsonObject.optBoolean("success") && jsonObject.optString("message").equals("录入成功")){
                                   Toast.makeText(CoachInfoActivity.this, "录入成功", Toast.LENGTH_SHORT).show();
                                   comments.add(comment);
                                   Collections.reverse(comments);
                                   adapter.notifyDataSetChanged();
                                   tv_no_comments.setVisibility(View.GONE);
                               }
                           }

                       }catch (IOException e){
                           e.printStackTrace();
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                        et_comment.setText("");

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        et_comment.setText("");
                    }
                }, comment);

                break;

        }
    }

    /**
     * 服务器响应回调
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
       try {

           if(!response.isSuccessful()){
               tv_no_comments.setText("获取教练评论失败");
               Log.i("comments", "onResponse: "+response.errorBody().string());
           } else {
               String json = response.body().string();
               GetCommentsResponseBody body = new Gson().fromJson(json,GetCommentsResponseBody.class);
               if (!body.isSuccess()){
                   tv_no_comments.setText("获取教练评论失败");
                   Log.i("comments", "onResponse: " + body.getMessage());
               }else if (body.getDiscusses().size() == 0){
                   tv_no_comments.setText("该教练尚无评论");
               }else {
                   comments.addAll(body.getDiscusses());
                   tv_no_comments.setVisibility(View.GONE);
                   Collections.reverse(comments);
                   adapter.notifyDataSetChanged();
               }
           }

       }catch (IOException e) {
           e.printStackTrace();
       }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        tv_no_comments.setText("获取教练评论失败");
    }


}
