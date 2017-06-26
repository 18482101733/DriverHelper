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
import com.cduestc.controller.adapter.StudentListAdapter;
import com.cduestc.controller.api.ApiService;
import com.cduestc.controller.bean.ServerResponseBody;
import com.cduestc.controller.bean.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentInfoActivity extends AppCompatActivity implements Callback<ResponseBody> {

    private Toolbar tb_bar;
    private RecyclerView rv_student_list;
    private ArrayList<Student> students;
    private StudentListAdapter studentListAdapter;
    private ApiService apiService;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        initView();
        initData();
    }

    private void initView(){
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("获取数据中");
        rv_student_list = (RecyclerView) findViewById(R.id.rv_student_list);
        tb_bar.setTitle("学生信息");
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

    private void initData(){
        students = new ArrayList<>();
        studentListAdapter = new StudentListAdapter(students,this);
        rv_student_list.setAdapter(studentListAdapter);
        rv_student_list.setLayoutManager(new LinearLayoutManager(this));
        apiService = ApiService.getInstance();
        dialog.show();
        apiService.getStudent(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        dialog.dismiss();
        String json = ServerResponseBody.praseJson(response);
        Type type = new TypeToken<ServerResponseBody<ArrayList<Student>>>(){}.getType();
        ServerResponseBody<ArrayList<Student>> serverResponseBody = new Gson().fromJson(json,type);
        if (!serverResponseBody.isSuccess()){
            Toast.makeText(this, "获取学生信息失败", Toast.LENGTH_SHORT).show();
            Log.i("pull error", "onResponse: " + serverResponseBody.getMessage());
        }else {
            students.clear();
            students.addAll(serverResponseBody.getData());
            studentListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        dialog.dismiss();
        Toast.makeText(this, "获取学生信息失败", Toast.LENGTH_SHORT).show();
        Log.i("pull error", "onFailure: " + t.getMessage());
    }
}
