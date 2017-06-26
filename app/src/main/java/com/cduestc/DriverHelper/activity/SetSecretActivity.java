package com.cduestc.DriverHelper.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.api.ApiService;
import com.cduestc.DriverHelper.api.Tag;
import com.cduestc.DriverHelper.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetSecretActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener,DialogInterface.OnClickListener, Callback<ResponseBody> {

    private AppCompatSpinner sp_questions;
    private Button bt_submit;
    private EditText et_answer;
    private final String[] secrets = {"你粑粑的名字","你麻麻的名字","你的出生地"};
    private AlertDialog.Builder alertDialog;
    private ApiService apiService;
    private User user;
    private int position = 0;
    private ProgressDialog dialog;
    private Toolbar tb_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_secret);
        initView();
        initData();
    }


    private void initView(){
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("提示");
        alertDialog.setMessage("密保问题不可修改,请确认");
        alertDialog.setPositiveButton("确定",this);
        alertDialog.setNegativeButton("取消",this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("提交中");
        sp_questions = (AppCompatSpinner) findViewById(R.id.sp_questions);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        et_answer = (EditText) findViewById(R.id.et_answer);
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        initToolbar();
        setListener();
    }

    private void initToolbar() {
        tb_bar.setTitle("设置密保");
        tb_bar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb_bar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initData() {
        apiService = ApiService.getInstance();
        user = (User) getIntent().getSerializableExtra(Tag.USER);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,secrets);
        sp_questions.setAdapter(adapter);
        sp_questions.setSelection(0);
    }

    /**
     * 设置点击监听
     */
    private void setListener(){
        sp_questions.setOnItemSelectedListener(this);
        bt_submit.setOnClickListener(this);
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

    /**
     * 列表点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 按钮点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_submit:
                alertDialog.create().show();
                break;
        }
    }

    /**
     * 弹窗点击事件
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:

                if ( "".equals(et_answer.getText().toString())){
                    Toast.makeText(SetSecretActivity.this, "答案不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                this.dialog.show();
                apiService.setAnswer(this,user,et_answer.getText().toString(),secrets[position]);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        dialog.dismiss();
        try{
            if (!response.isSuccessful()){
                Toast.makeText(SetSecretActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                Log.i("123", "onResponse: " + response.errorBody().string());
            }else {
                String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.optBoolean("success")){
                    Toast.makeText(SetSecretActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SetSecretActivity.this,"设置失败,或已经设置密保" , Toast.LENGTH_SHORT).show();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
