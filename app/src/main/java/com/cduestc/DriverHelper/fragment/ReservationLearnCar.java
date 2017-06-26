package com.cduestc.DriverHelper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.activity.CoachInfoActivity;
import com.cduestc.DriverHelper.activity.MainActivity;
import com.cduestc.DriverHelper.adapter.CoachesListAdapter;
import com.cduestc.DriverHelper.adapter.CommentListAdapter;
import com.cduestc.DriverHelper.api.ApiService;
import com.cduestc.DriverHelper.api.Tag;
import com.cduestc.DriverHelper.bean.Coach;
import com.cduestc.DriverHelper.bean.Comment;
import com.cduestc.DriverHelper.bean.GetAllCoachResponseBody;
import com.cduestc.DriverHelper.bean.GetCommentsResponseBody;
import com.cduestc.DriverHelper.bean.Student;
import com.cduestc.DriverHelper.bean.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by c on 2017/3/2.
 */
public class ReservationLearnCar extends Fragment implements Callback<GetAllCoachResponseBody>, AdapterView.OnItemClickListener,MainActivity.SetUserForLearn {


    private Context context;
    private User user;
    private ArrayList<Coach> coaches;
    private CoachesListAdapter coachesListAdapter;
    private ApiService apiService;
    private ListView lv_coach_list;
    private boolean isLoadedSuccess = false;
    private ArrayList<Comment> comments;
    private CommentListAdapter commentListAdapter;

    private static final String TAG = "ReservationLearnCar";
    public ReservationLearnCar() {
        coaches = new ArrayList<>();
        comments = new ArrayList<>();
        apiService = ApiService.getInstance();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();
        View view = inflater.inflate(R.layout.reservation_learn_car,container,false);
        lv_coach_list = (ListView) view.findViewById(R.id.lv_coach_list);
        initData();
        return view;
    }


    private void initData(){
        //user = (User) getArguments().getSerializable(Tag.USER);
        coachesListAdapter = new CoachesListAdapter(coaches,context);
        commentListAdapter = new CommentListAdapter(comments,context);


        if (user instanceof Student){
            lv_coach_list.setAdapter(coachesListAdapter);
            lv_coach_list.setOnItemClickListener(this);
        }else if (user instanceof Coach){
            lv_coach_list.setAdapter(commentListAdapter);
        }

    }

    @Override
    public void onResponse(Call<GetAllCoachResponseBody> call, Response<GetAllCoachResponseBody> response) {
        if (!response.isSuccessful()){
            Toast.makeText(context, "获取该驾校教练信息失败", Toast.LENGTH_SHORT).show();
            try {
                Log.i(TAG, "onResponse: " +response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (!response.body().isSuccess()){
            Toast.makeText(context,"获取该驾校教练失败" + response.body().getMessage(),Toast.LENGTH_SHORT).show();
        }else if (response.body().getStaffEntities().size() == 0){
            Toast.makeText(context,"该驾校还没有教练哦",Toast.LENGTH_SHORT).show();
        }else {
            coaches.clear();
            coaches.addAll(response.body().getStaffEntities());
            coachesListAdapter.notifyDataSetChanged();
            isLoadedSuccess = true;
        }
    }

    @Override
    public void onFailure(Call<GetAllCoachResponseBody> call, Throwable t) {
        Toast.makeText(context, "获取该驾校教练失败,请检查网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent coachInfoIntent = new Intent(context, CoachInfoActivity.class);
        coachInfoIntent.putExtra(Tag.COACH,coaches.get(position));
        coachInfoIntent.putExtra(Tag.USER,user);
        startActivity(coachInfoIntent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

            if (!isLoadedSuccess && user !=null && user instanceof Student){
                apiService.getAllCoaches(this,this.user.getSchoolName());
            }

            if (!isLoadedSuccess && user != null && user instanceof Coach){
                apiService.getComments(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {

                            if(!response.isSuccessful()){
                            } else {
                                String json = response.body().string();
                                GetCommentsResponseBody body = new Gson().fromJson(json,GetCommentsResponseBody.class);
                                if (!body.isSuccess()){
                                }else if (body.getDiscusses().size() == 0){
                                    Toast.makeText(context, "暂无评论", Toast.LENGTH_SHORT).show();
                                }else {
                                    comments.clear();
                                    comments.addAll(body.getDiscusses());
                                    commentListAdapter.notifyDataSetChanged();
                                }
                            }

                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                },user.getUid());
            }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
