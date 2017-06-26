package com.cduestc.DriverHelper.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.api.ApiService;
import com.cduestc.DriverHelper.api.Tag;
import com.cduestc.DriverHelper.bean.LoginResponseBody;
import com.cduestc.DriverHelper.bean.UserLogin;
import com.cduestc.DriverHelper.utils.InputTextUtils;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<LoginResponseBody> {

    private static final String TAG = "login";

    private TextInputEditText et_username,et_password;
    private Button bt_login;
    private TextView tv_forgot,tv_register;
    private Toolbar tb_bar;
    private ApiService apiService;
    private CatLoadingView loadingView;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView(){

        et_username = (TextInputEditText) findViewById(R.id.et_username);
        et_password = (TextInputEditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_forgot = (TextView) findViewById(R.id.tv_forgot);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        bt_login.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        loadingView = new CatLoadingView();
        setToolBar();
    }

    /**
     * 初始化数据
     */
    private void initData(){
        apiService = ApiService.getInstance();
        sharedPreferences = getSharedPreferences(Tag.DRIVER_HELPER,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isAutoLogin();
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bt_login:
                if (checkLoginInfo(et_username.getText()+"",et_password.getText()+"")){
                    loadingView.show(getSupportFragmentManager(),"登录中...");
                    UserLogin user = new UserLogin(et_username.getText().toString(),et_password.getText().toString());
                    apiService.Login(this,user.toString());
                }
                break;

            case R.id.tv_register:
                Intent registerIntent = new Intent(this,RegisterActivity.class);
                startActivity(registerIntent);
                break;

            case R.id.tv_forgot:
                Intent forgotIntent = new Intent(this,ForgotActivity.class);
                startActivity(forgotIntent);
                break;
        }
    }

    /**
     * 对输入的格式等的判定
     * @param username
     * @param password
     * @return
     */
    private boolean checkLoginInfo(String username,String password){
        if (TextUtils.isEmpty(username)){
            et_username.setError("用户名不能为空");
            return false;
        }else if (!InputTextUtils.isPhoneNum(username)){
            et_username.setError("用户名格式错误");
            return false;
        }else if (TextUtils.isEmpty(password)){
            et_password.setError("密码不能为空");
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 开始主界面
     * @param body
     */
    private void startMain(LoginResponseBody body){

        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.putExtra(Tag.USER_POWER,body.getPower());
        mainIntent.putExtra(Tag.USER_UID,et_username.getText().toString());
        startActivity(mainIntent);
        putLoginInfo(body.getPower());
        finish();
    }

    /**
     *设置标题栏
     */
    private void setToolBar(){
        tb_bar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(tb_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 服务器响应成功回调
     * @param call  请求接口
     * @param response  响应体
     */
    @Override
    public void onResponse(Call<LoginResponseBody> call, Response<LoginResponseBody> response) {
        Log.i(TAG, "onResponse: \n" + response.isSuccessful()+" \n"+ response.body());
        loadingView.dismiss();

        if (!response.isSuccessful()){
            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            try {
                Log.i(TAG, "onResponse: " + response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return;
        }

        LoginResponseBody body = new Gson().fromJson(response.body().toString(),LoginResponseBody.class);
        if (body.isSuccess()){
            startMain(body);
        }else {
            Toast.makeText(LoginActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 服务器响应失败回调
     * @param call
     * @param t  异常
     */
    @Override
    public void onFailure(Call<LoginResponseBody> call, Throwable t) {
        Log.i(TAG, "onFailure: " + t.getMessage());
        loadingView.dismiss();

        Toast.makeText(LoginActivity.this, "登录超时", Toast.LENGTH_SHORT).show();
    }

    /**
     * 将登陆信息存入本地
     * @param power
     */
    private void putLoginInfo(int power){
        editor.putBoolean(Tag.AUTO_LOGIN,true);
        editor.putString(Tag.USERNAME,et_username.getText().toString());
        editor.putInt(Tag.USER_POWER,power);
        editor.putString(Tag.UID,et_username.getText().toString());
        editor.apply();
    }

    /**
     * 是否自动登录
     */
    private void isAutoLogin(){
        boolean isAutoLogin = sharedPreferences.getBoolean(Tag.AUTO_LOGIN,false);
        if (isAutoLogin){
            String uid = sharedPreferences.getString(Tag.UID,"");
            int power = sharedPreferences.getInt(Tag.USER_POWER,1);
            Intent mainIntent = new Intent(this,MainActivity.class);
            mainIntent.putExtra(Tag.USER_POWER,power);
            mainIntent.putExtra(Tag.USER_UID,uid);
            startActivity(mainIntent);
            finish();
        }
    }
}
