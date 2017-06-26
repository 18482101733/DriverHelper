package com.cduestc.controller.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cduestc.controller.R;
import com.cduestc.controller.adapter.SetStatesAdapter;
import com.cduestc.controller.api.ApiService;
import com.cduestc.controller.bean.ServerResponseBody;
import com.cduestc.controller.bean.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetStatesActivity extends AppCompatActivity implements Callback<ResponseBody>, SearchView.OnQueryTextListener,DialogInterface.OnClickListener, SetStatesAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar tb_bar;
    private SetStatesAdapter adapter;
    private RecyclerView rv_states_list;
    private ArrayList<User> users;
    private ApiService apiService;
    private SearchView sv_search;
    private AlertDialog.Builder statesBuilder;
    private SwipeRefreshLayout srl_refresh;
    private int position;
    private int times = 0;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_states);
        initView();
        initData();
    }

    private void initData() {
        apiService = ApiService.getInstance();
        users = new ArrayList<>();
        adapter = new SetStatesAdapter(users,this);
        rv_states_list.setAdapter(adapter);
        rv_states_list.setLayoutManager(new LinearLayoutManager(this));
        srl_refresh.setRefreshing(true);
        apiService.getStudent(this);
        apiService.getCoach(this);
        adapter.setOnItemClickListener(this);
        dialog.show();
    }

    private void initView(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("载入数据中");
        dialog.setCanceledOnTouchOutside(false);
        srl_refresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        if (srl_refresh != null){
            srl_refresh.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
            srl_refresh.setOnRefreshListener(this);
        }

        statesBuilder = new AlertDialog.Builder(this);
        statesBuilder.setMessage("修改此人权限");
        statesBuilder.setPositiveButton("赋予",this);
        statesBuilder.setNeutralButton("删除",this);
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        rv_states_list = (RecyclerView) findViewById(R.id.rv_states_list);

        initToolbar();

    }

    private void initToolbar(){
        tb_bar.setTitle("设置用户权限");
        tb_bar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb_bar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //重写onCreateOptionsMenu()方法
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        sv_search = (SearchView) menu.findItem(R.id.search).getActionView();
        sv_search.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        srl_refresh.setRefreshing(false);
        dialog.dismiss();
        times++;
        String json = ServerResponseBody.praseJson(response);
        Type type = new TypeToken<ServerResponseBody<ArrayList<User>>>(){}.getType();
        ServerResponseBody<ArrayList<User>> serverResponseBody = new Gson().fromJson(json,type);
        if (!serverResponseBody.isSuccess()){
            Toast.makeText(SetStatesActivity.this, "失败", Toast.LENGTH_SHORT).show();
            Log.i("pull error", "onResponse: " + serverResponseBody.getMessage());
        }else {
            if (times%2 == 1 && times != 1)
                users.clear();
            users.addAll(serverResponseBody.getData());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        dialog.dismiss();
        Toast.makeText(SetStatesActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
        Log.i("pull error", "onFailure: " + t.getMessage());
        srl_refresh.setRefreshing(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final  ArrayList<User> temp = getFilter(users,newText);
        adapter.filter(temp);
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which){
            case DialogInterface.BUTTON_POSITIVE:

                apiService.setStates(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String json =  ServerResponseBody.praseJson(response);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.optBoolean("success")){
                                Toast.makeText(SetStatesActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SetStatesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(SetStatesActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }, users.get(position).getUid());

                break;

            case DialogInterface.BUTTON_NEUTRAL:

                apiService.deleteStates(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String json =  ServerResponseBody.praseJson(response);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.optBoolean("success")){
                                Toast.makeText(SetStatesActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SetStatesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(SetStatesActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                },users.get(position).getUid());

                break;
        }

    }

    @Override
    public void onItemClick(View v, int position) {
        this.position = position;
        statesBuilder.create().show();
    }

    public ArrayList<User> getFilter(ArrayList<User> users,String key){
        final ArrayList<User> temp = new ArrayList<>();
        for (User user :users) {
            if (user.getUid().contains(key) || user.getName().contains(key))
                temp.add(user);
        }
        return temp;
    }

    @Override
    public void onRefresh() {
        apiService.getStudent(this);
        apiService.getCoach(this);
    }
}
