package com.cduestc.controller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cduestc.controller.R;
import com.cduestc.controller.api.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<ResponseBody> {


    private static final String TAG = "LoginActivity";

    private Button bt_login;
    private EditText et_username;
    private EditText et_password;
    private ApiService apiService;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        bt_login = (Button) findViewById(R.id.bt_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

    }
    private void initData() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("登录中,请稍候...");
        bt_login.setOnClickListener(this);
        apiService = ApiService.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                if (checkInput()){
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                    apiService.login(this,et_password.getText().toString(),et_username.getText().toString());
                }
                break;
        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(et_username.getText())){
            et_username.requestFocus();
            et_username.setError("用户名不能为空");
            return false;
        }else if (TextUtils.isEmpty(et_password.getText())){
            et_password.requestFocus();
            et_password.setError("密码不能为空");
            return false;
        }

        return true;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        dialog.cancel();
        try{
            if (!response.isSuccessful()){
                Toast.makeText(LoginActivity.this,"登录失败", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onResponse: ");
            }else {
                String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.optBoolean("success") && (jsonObject.optInt("power") == 3)){
                    startActivity(new Intent(this,MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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
        dialog.cancel();
        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onFailure: " +t.getMessage());
    }
}
