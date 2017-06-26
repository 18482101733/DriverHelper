package com.cduestc.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cduestc.controller.R;
import com.cduestc.controller.api.ApiService;
import com.cduestc.controller.bean.School;
import com.cduestc.controller.bean.ServerResponseBody;
import com.cduestc.controller.utils.InputTextUtils;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSchoolActivity extends AppCompatActivity implements View.OnClickListener, Callback<ResponseBody> {

    private EditText et_name,et_address,et_city,et_phone;
    private Button bt_submit;
    private ApiService apiService;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_school);
        initView();
        initData();
    }

    private void initData() {
        apiService = ApiService.getInstance();
        bt_submit.setOnClickListener(this);
    }

    private void initView() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("提交中");
        dialog.setCanceledOnTouchOutside(false);
        et_name = (EditText) findViewById(R.id.et_name);
        et_address = (EditText) findViewById(R.id.et_address);
        et_city = (EditText) findViewById(R.id.et_city);
        et_phone = (EditText) findViewById(R.id.et_phone);
        bt_submit = (Button) findViewById(R.id.bt_submit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_submit:

                School school = new School();
                school.setAddress(et_address.getText().toString());
                school.setUid(et_phone.getText().toString());
                school.setCity(et_city.getText().toString());
                school.setName(et_name.getText().toString());
                school.setPhoneNum(et_phone.getText().toString());
                if (TextUtils.isEmpty(school.getAddress()) || TextUtils.isEmpty(school.getCity())
                        || TextUtils.isEmpty(school.getName())
                        || TextUtils.isEmpty(school.getPhoneNum())
                        || TextUtils.isEmpty(school.getUid())){
                    Toast.makeText(AddSchoolActivity.this, "每项为必填信息", Toast.LENGTH_SHORT).show();
                }else if (!InputTextUtils.isPhoneNum(et_phone.getText().toString())){
                et_phone.setError("手机号格式不正确");
                }
                dialog.show();
                apiService.addSchool(this,school);

                break;
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        String json = ServerResponseBody.praseJson(response);
        dialog.dismiss();
        ServerResponseBody responseBody = new Gson().fromJson(json,ServerResponseBody.class);
        if (!responseBody.isSuccess()){
            Toast.makeText(AddSchoolActivity.this, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddSchoolActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        dialog.dismiss();
        Toast.makeText(AddSchoolActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
    }
}
