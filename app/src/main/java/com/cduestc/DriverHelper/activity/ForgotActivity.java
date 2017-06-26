package com.cduestc.DriverHelper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.api.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar tb_bar;
    private AppCompatSpinner sp_SecretSecurity;
    private EditText et_answer,et_uid;
    private Button bt_submit,bt_submit_new_password;
    private TextInputEditText et_new_password;
    private RelativeLayout rl_equal_password;
    private final String[] secrets = {"你粑粑的名字","你麻麻的名字","你的出生地"};
    private String question;
    private String uid;
    private ApiService apiService;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        initView();
        initData();

    }

    /**
     * 初始化视图
     */
    private void initView() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("提交中");
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        sp_SecretSecurity = (AppCompatSpinner) findViewById(R.id.sp_SecretSecurity);
        et_uid = (EditText) findViewById(R.id.et_uid);
        et_answer = (EditText) findViewById(R.id.et_answer);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit_new_password = (Button) findViewById(R.id.bt_submit_new_password);
        et_new_password  = (TextInputEditText) findViewById(R.id.et_new_password);
        rl_equal_password = (RelativeLayout) findViewById(R.id.rl_equal_password);
        setToolbar();
        setListener();
    }

    /**
     * 设置标题栏
     */
    private void setToolbar() {
        apiService = ApiService.getInstance();
        tb_bar.setTitle("忘记密码");
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
     * 设置按钮的点击监听
     */
    private void setListener() {
        bt_submit.setOnClickListener(this);
        bt_submit_new_password.setOnClickListener(this);
        sp_SecretSecurity.setOnItemSelectedListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,secrets);
        sp_SecretSecurity.setAdapter(adapter);
        sp_SecretSecurity.setSelection(0);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_submit:
                loadingDialog.show();
                uid = et_uid.getText().toString();
                apiService.passwordAnswer(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loadingDialog.dismiss();
                       try{

                           if (!response.isSuccessful()){
                               Toast.makeText(ForgotActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                               Log.i("123", "onResponse: "+response.errorBody().string());
                           }else {
                               String json = response.body().string();
                               JSONObject jsonObject = new JSONObject(json);
                               if (jsonObject.optBoolean("success")){
                                   Toast.makeText(ForgotActivity.this, "验证成功,请设置新密码", Toast.LENGTH_SHORT).show();
                                   rl_equal_password.setVisibility(View.GONE);
                                   et_new_password.setVisibility(View.VISIBLE);
                                   bt_submit_new_password.setVisibility(View.VISIBLE);

                               }else {
                                   Toast.makeText(ForgotActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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
                        loadingDialog.dismiss();
                    }
                },uid,question,et_answer.getText().toString());

                break;

            case R.id.bt_submit_new_password:
                loadingDialog.show();
                apiService.setNewPassword(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loadingDialog.dismiss();
                        try{
                            if (!response.isSuccessful()){
                                Toast.makeText(ForgotActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                Log.i("123", "onResponse: " + response.errorBody().string());
                            }else {
                                String json = response.body().string();
                                JSONObject jsonObject = new JSONObject(json);
                                if (jsonObject.optBoolean("success")){
                                    Toast.makeText(ForgotActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    Intent startIntent = new Intent(ForgotActivity.this,LoginActivity.class);
                                    startActivity(startIntent);
                                }else {
                                    Toast.makeText(ForgotActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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
                        loadingDialog.dismiss();

                    }
                },uid,et_new_password.getText().toString());
                break;
        }
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
        question = secrets[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
}
