package com.cduestc.controller.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cduestc.controller.R;
import com.cduestc.controller.adapter.SchoolListAdapter;
import com.cduestc.controller.api.ApiService;
import com.cduestc.controller.bean.School;
import com.cduestc.controller.bean.ServerResponseBody;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolManageActivity extends AppCompatActivity implements Callback<ResponseBody>,SchoolListAdapter.OnItemClickListener,DialogInterface.OnClickListener, View.OnClickListener {
    private Toolbar tb_bar;
    private RecyclerView rv_school_list;
    private SchoolListAdapter schoolListAdapter;
    private ArrayList<School> schools;
    private ApiService apiService;
    private int position;
    private AlertDialog.Builder deleteDialog;
    private ProgressDialog dialog;
    private ImageView iv_add_school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_manage);
        initView();
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        apiService.getAllSchool(this);
    }

    private void initView(){
        deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("警告");
        deleteDialog.setMessage("删除此驾校?");
        deleteDialog.setPositiveButton("确定", this);
        deleteDialog.setNegativeButton("取消",  this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("载入中");
        dialog.setCanceledOnTouchOutside(false);
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        rv_school_list = (RecyclerView) findViewById(R.id.rv_school_list);
        iv_add_school = (ImageView) findViewById(R.id.iv_add_school);
        initToolbar();
    }

    private void initData() {
        iv_add_school.setOnClickListener(this);
        apiService = ApiService.getInstance();
        apiService.getAllSchool(this);

        schools = new ArrayList<>();

        schoolListAdapter = new SchoolListAdapter(schools);
        schoolListAdapter.setOnItemClickListener(this);
        rv_school_list.setAdapter(schoolListAdapter);
        rv_school_list.setLayoutManager(new LinearLayoutManager(this));
        dialog.show();
    }

    private void initToolbar(){
        tb_bar.setTitle("驾校信息管理");
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

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        dialog.dismiss();
        String json = ServerResponseBody.praseJson(response);
        Type type = new TypeToken<ServerResponseBody<ArrayList<School>>>(){}.getType();
        ServerResponseBody<ArrayList<School>> serverResponse = new Gson().fromJson(json,type);
        if (!serverResponse.isSuccess()){
            Toast.makeText(SchoolManageActivity.this, "获取学校数据失败", Toast.LENGTH_SHORT).show();
            Log.i("pull error", "onResponse: " + serverResponse.getMessage());
        }else {
            schools.clear();
            schools.addAll(serverResponse.getData());
            schoolListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        dialog.dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        this.position = position;
        deleteDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                apiService.deleteSchool(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String json = ServerResponseBody.praseJson(response);
                        ServerResponseBody responseBody = new Gson().fromJson(json,ServerResponseBody.class);
                        if (!responseBody.isSuccess()){
                            Toast.makeText(SchoolManageActivity.this, "删除驾校失败", Toast.LENGTH_SHORT).show();
                            Log.i("pull error", "onResponse: " + responseBody.getMessage());
                        }else {
                            Toast.makeText(SchoolManageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            apiService.getAllSchool(this);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(SchoolManageActivity.this, "删除驾校失败", Toast.LENGTH_SHORT).show();

                    }
                },schools.get(position).getUid());
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,AddSchoolActivity.class));
    }
}
