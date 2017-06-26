package com.cduestc.controller.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.cduestc.controller.R;
import com.cduestc.controller.adapter.CoachListAdapter;
import com.cduestc.controller.api.ApiService;
import com.cduestc.controller.bean.Coach;
import com.cduestc.controller.bean.ServerResponseBody;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoachInfoActivity extends AppCompatActivity implements Callback<ResponseBody> {

    private RecyclerView rv_coach_list;
    private CoachListAdapter adapter;
    private ArrayList<Coach> coaches;
    private ApiService apiService;
    private Toolbar tb_bar;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_info);
        initView();
        initData();
    }

    private void initView(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("载入数据中");
        dialog.setCanceledOnTouchOutside(false);
        rv_coach_list = (RecyclerView) findViewById(R.id.rv_coach_list);
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        tb_bar.setTitle("教练信息列表");
        tb_bar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        apiService = ApiService.getInstance();
        coaches = new ArrayList<>();
        rv_coach_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CoachListAdapter(coaches,this);
        rv_coach_list.setAdapter(adapter);
        apiService.getCoach(this);
        dialog.show();
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        dialog.dismiss();
        String json = ServerResponseBody.praseJson(response);
        Type type = new TypeToken<ServerResponseBody<ArrayList<Coach>>>(){}.getType();
        ServerResponseBody<ArrayList<Coach>> serverResponseBody = new Gson().fromJson(json,type);
        if (!serverResponseBody.isSuccess()){
            Toast.makeText(CoachInfoActivity.this, "获取教练信息失败", Toast.LENGTH_SHORT).show();
            Log.i("pull error", "onResponse: " + serverResponseBody.getMessage());
        }else {
            coaches.clear();
            coaches.addAll(serverResponseBody.getData());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        dialog.dismiss();
        Toast.makeText(CoachInfoActivity.this, "获取教练信息失败", Toast.LENGTH_SHORT).show();
        Log.i("pull error", "onFailure: " + t.getMessage());

    }
}
