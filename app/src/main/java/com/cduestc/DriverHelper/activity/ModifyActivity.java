package com.cduestc.DriverHelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.api.ApiService;
import com.cduestc.DriverHelper.api.Tag;
import com.cduestc.DriverHelper.bean.Coach;
import com.cduestc.DriverHelper.bean.ModifyResponseBody;
import com.cduestc.DriverHelper.bean.Student;
import com.cduestc.DriverHelper.bean.User;
import com.cduestc.DriverHelper.utils.InputTextUtils;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;

import java.io.IOException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyActivity extends AppCompatActivity implements View.OnClickListener, Callback<ResponseBody> {

    private Toolbar tb_bar;
    private EditText et_address,et_idNum,et_plate_num,et_coach_info,et_name;
    private TextView tv_submit;
    private ImageView iv_user_icon;
    private User user;
    private Student student;
    private Coach coach;
    private CatLoadingView catLoadingView;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        initView();
        initData();
    }

    private void initData() {
        apiService = ApiService.getInstance();
        user = (User) getIntent().getSerializableExtra(Tag.USER);

        et_address.setText(user.getAddress());
        et_idNum.setText(user.getIdNum());
        et_name.setText(user.getName());


        if (user instanceof Student){
            et_coach_info.setVisibility(View.GONE);
            et_plate_num.setVisibility(View.GONE);
            student = (Student) user;
        }else if (user instanceof Coach){
            coach = (Coach) user;
            et_coach_info.setText(coach.getCoachInfo());
            et_plate_num.setText(coach.getPlateNum());
        }



        catLoadingView = new CatLoadingView();


        Glide.with(this)
                .load(Tag.GET_USER_ICON + user.getUid()+"&random="+Math.random())
                .error(R.mipmap.user_icon)
                .placeholder(R.mipmap.user_icon)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(iv_user_icon);

        tv_submit.setOnClickListener(this);
        iv_user_icon.setOnClickListener(this);

    }

    private void initView() {
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        et_address = (EditText) findViewById(R.id.et_address);
        et_idNum = (EditText) findViewById(R.id.et_idNum);
        et_plate_num = (EditText) findViewById(R.id.et_plate_num);
        et_coach_info = (EditText) findViewById(R.id.et_coach_info);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        iv_user_icon = (ImageView) findViewById(R.id.iv_user_icon);
        et_name = (EditText) findViewById(R.id.et_name);
        setToolbar();
    }

    /**
     * 设置标题栏
     */
    private void setToolbar(){
        tb_bar.setTitle("修改界面");
        setSupportActionBar(tb_bar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 标题栏的返回按钮
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
     * 输入是否符合规则
     * @return
     */
    private boolean checkInPut(){

        if (et_address.getText() == null ){
            et_address.setError("住址不能为空");
            return false;
        }else if (et_idNum.getText() == null || InputTextUtils.isIdCard(et_idNum.getText().toString())){
            et_idNum.setError("身份证号格式错误");
            return true;
        }


       if (user instanceof Student){

       }else if (user instanceof Coach){
           if (et_plate_num.getText() == null || InputTextUtils.isPlate(et_plate_num.getText().toString())){
               et_plate_num.setError("车牌号格式错误");
               return false;
           }
       }

        return true;
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_submit:
                if (checkInPut()){
                    setInfo();
                    submitModify();
                    catLoadingView.show(getSupportFragmentManager(),"");
                    //catLoadingView.dismissAllowingStateLoss();
                }

                break;

            case R.id.iv_user_icon:

                break;


        }
    }

    /**
     * 提交修改
     */
    private void submitModify() {
        if (user instanceof  Student){
            apiService.modifyInformation(this,student);
        }else if (user instanceof Coach){
            apiService.modifyInformation(this,coach);
        }
    }

    /**
     * 设置用户信息
     */
    public void setInfo(){
       if (user instanceof Student){

           student.setAddress(et_address.getText().toString());
           student.setIdNum(et_idNum.getText().toString());
           student.setName(et_name.getText().toString());
           student.setStuInfo("");

       }else if (user instanceof Coach){
           coach.setAddress(et_address.getText().toString());
           coach.setIdNum(et_idNum.getText().toString());
           coach.setName(et_name.getText().toString());
           coach.setCoachInfo(et_coach_info.getText().toString());
           coach.setPlateNum(et_plate_num.getText().toString());
        }
        Log.i("password", "setInfo: " + user.toString());
    }


    /**
     * 服务器响应回调
     * @param call 请求接口
     * @param response 响应体
     */
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        catLoadingView.dismiss();

        try{
            if (response.isSuccessful()){

                ModifyResponseBody body = new Gson().fromJson(response.body().string(),ModifyResponseBody.class);
                if (body.isSuccess()){
                    Toast.makeText(ModifyActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                    Intent startMainIntent = new Intent(this ,MainActivity.class);

                    if (user instanceof Student){
                        startMainIntent.putExtra(Tag.USER_UID,student.getUid());
                        startMainIntent.putExtra(Tag.USER_POWER,Tag.USER_STUDENT);
                    }else if (user instanceof Coach){
                        startMainIntent.putExtra(Tag.USER_UID,coach.getUid());
                        startMainIntent.putExtra(Tag.USER_POWER,Tag.USER_COACH);
                    }
                    startActivity(startMainIntent);
                    finish();

                }else {
                    Toast.makeText(ModifyActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }else {
                Log.i("modify", "onResponse: "+response.errorBody().string());
                Toast.makeText(ModifyActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 服务器响应失败回调
     * @param call 请求体
     * @param t 异常
     */
    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(ModifyActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
        catLoadingView.dismiss();
    }
}
