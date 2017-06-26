package com.cduestc.DriverHelper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.cduestc.DriverHelper.bean.City;
import com.cduestc.DriverHelper.bean.Coach;
import com.cduestc.DriverHelper.bean.GetSchoolResponseBody;
import com.cduestc.DriverHelper.bean.Province;
import com.cduestc.DriverHelper.bean.RegisterResponseBody;
import com.cduestc.DriverHelper.bean.School;
import com.cduestc.DriverHelper.bean.Student;
import com.cduestc.DriverHelper.utils.InputTextUtils;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        Callback<GetSchoolResponseBody>,DialogInterface.OnClickListener {

    private Toolbar tb_bar;
    private TextInputEditText et_username;
    private TextInputEditText et_password;
    private TextInputEditText et_phone;
    private TextInputEditText et_idNum;
    private TextInputEditText et_address;

    private ApiService apiService;

    private AppCompatSpinner sp_province;
    private AppCompatSpinner sp_city;
    private AppCompatSpinner sp_schools;

    private Button bt_register;

    private CatLoadingView loadingView;

    private ArrayList<City> citys;
    private ArrayList<Province> provinces;
    private ArrayList<School> schools;

    private ArrayAdapter<String> citysAdapter;
    private ArrayAdapter<String> provincesAdapter;
    private ArrayAdapter<String> schoolsAdapter;

    private ArrayList<String> citysName;
    private ArrayList<String> provincesName;
    private ArrayList<String> schoolsName;

    private AlertDialog.Builder checkIdentifyDialog;
    private AlertDialog.Builder inPutPlateNumDialogBuilder;
    private AlertDialog inPutPlateNumDialog;
    private AlertDialog.Builder inPutInviteCode;

    private AlertDialog.Builder ecretSecurity;

    private Coach coach;
    private Student student;

    private boolean isStudent = true;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();
    }

    private void initData() {

        citys = Tag.citys;
        provinces = Tag.provinces;
        apiService = ApiService.getInstance();

        citysName = new ArrayList<>();
        provincesName = new ArrayList<>();
        schoolsName = new ArrayList<>();
        for (City cityName:citys) {
            citysName.add(cityName.getName());
        }

        for (Province provinceName:provinces){
            provincesName.add(provinceName.getName());
        }


        citysAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,citysName);
        citysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provincesAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,provincesName);
        provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,schoolsName);
        schoolsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_city.setAdapter(citysAdapter);
        sp_province.setAdapter(provincesAdapter);
        sp_schools.setAdapter(schoolsAdapter);

        sp_city.setOnItemSelectedListener(this);
        sp_province.setOnItemSelectedListener(this);
        sp_schools.setOnItemSelectedListener(this);
        bt_register.setOnClickListener(this);
        sp_province.setSelection(27);



    }

    private void initView() {
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        et_username = (TextInputEditText) findViewById(R.id.et_username);
        et_password = (TextInputEditText) findViewById(R.id.et_password);
        et_phone = (TextInputEditText) findViewById(R.id.et_phone);
        et_idNum = (TextInputEditText) findViewById(R.id.et_idNum);
        et_address = (TextInputEditText) findViewById(R.id.et_address);
        sp_province = (AppCompatSpinner) findViewById(R.id.sp_province);
        sp_city = (AppCompatSpinner) findViewById(R.id.sp_city);
        sp_schools = (AppCompatSpinner) findViewById(R.id.sp_schools);
        bt_register = (Button) findViewById(R.id.bt_register);
        loadingView = new CatLoadingView();

        checkIdentifyDialog = new  AlertDialog.Builder(this);
        checkIdentifyDialog.setIcon(R.mipmap.user);
        checkIdentifyDialog.setTitle("请选择您的身份");
        checkIdentifyDialog.setPositiveButton("学生", this);
        checkIdentifyDialog.setNeutralButton("教练",this);


        final EditText et_inviteCode = new EditText(this);
        inPutInviteCode = new AlertDialog.Builder(this);
        inPutInviteCode.setTitle("请输入邀请码");
        inPutInviteCode.setView(et_inviteCode);
        inPutInviteCode.setIcon(R.mipmap.invite_code);
        inPutInviteCode.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                loadingView.show(getSupportFragmentManager(),"");
                loadingView.dismissAllowingStateLoss();
                if (isStudent){
                    apiService.Register(new RegisterCallBack(), student.toRegisterString(), isStudent, et_inviteCode.getText() + "");
                }else {
                    apiService.Register(new RegisterCallBack(),coach.toRegisterString(),isStudent,et_inviteCode.getText().toString());
                }
            }
        });

        inPutInviteCode.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        final EditText et_plateNum = new EditText(this);

        inPutPlateNumDialogBuilder = new AlertDialog.Builder(this);
        inPutPlateNumDialogBuilder.setIcon(R.mipmap.plate_num);
        inPutPlateNumDialogBuilder.setView(et_plateNum);
        inPutPlateNumDialogBuilder.setTitle("请输入您的车牌号");
        inPutPlateNumDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (InputTextUtils.isPlate(et_plateNum.getText()+"")){
                    coach = new Coach(et_username.getText().toString(),
                            et_idNum.getText().toString(),
                            et_phone.getText().toString(),
                            et_address.getText().toString(),
                            et_password.getText().toString(),
                            schoolsName.get(position),
                            et_plateNum.getText().toString());

                    inPutInviteCode.show();
                }else{
                    Toast.makeText(RegisterActivity.this, "车牌号格式不正确", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        inPutPlateNumDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inPutPlateNumDialogBuilder.setView(null);

                inPutPlateNumDialog.cancel();
            }
        });

        inPutPlateNumDialog = inPutPlateNumDialogBuilder.create();


        setToolBar();

    }

    private void setToolBar(){
        tb_bar.setTitle("");
        setSupportActionBar(tb_bar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    private boolean checkInput(){
        if (TextUtils.isEmpty(et_phone.getText())){
            et_phone.setError("手机号码不能为空");
            return false;

        }else if (!InputTextUtils.isPhoneNum(et_phone.getText().toString())){
            et_phone.setError("手机号格式不正确");
            return false;

        }else if (TextUtils.isEmpty(et_username.getText())){
            et_username.setError("姓名不能为空");
            return false;

        }else if (TextUtils.isEmpty(et_password.getText())){
            et_password.setError("密码不能为空");
            return false;

        }else if (TextUtils.isEmpty(et_address.getText())){
            et_address.setError("住址不能为空");
            return false;
        }else if (!InputTextUtils.isIdCard(et_idNum.getText().toString())){
            et_idNum.setError("身份证号格式不正确");
            return false;
        }

        return true;
    }

    /**
     * 按钮点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_register:
                if (checkInput()){
                    checkIdentifyDialog.show();
                }
                break;
        }
    }


    /**
     * spinner选择事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){

            case R.id.sp_province:
                Province province = provinces.get(position);
                citysName.clear();

                for (City eachCity:citys){
                    if (eachCity.getProID() == province.getProID()){
                        citysName.add(eachCity.getName());
                    }
                }

                citysAdapter.notifyDataSetChanged();
                break;

            case R.id.sp_city:
                Log.i("register", "onItemSelected: requestCityName:" + citysName.get(position));
                String cityName = citysName.get(position).replace("市","");
                Log.i("register", "onItemSelected: cityName:" + cityName);
                apiService.getSchools(this,cityName);
                break;

            case R.id.sp_schools:
                this.position = position;
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 拉取城市驾校Http响应
     * @param call 回调
     * @param response 响应
     */
    @Override
    public void onResponse(Call<GetSchoolResponseBody> call, Response<GetSchoolResponseBody> response) {

        Log.i("register", "onResponse: " + response.body()+"\n"+response.raw().toString());

        if (response.isSuccessful()){
            schools = (ArrayList<School>) response.body().getData();
            
            if (schools.size() == 0){
                Toast.makeText(RegisterActivity.this, "该地区没有合作驾校", Toast.LENGTH_SHORT).show();
            }
            schoolsName.clear();
            for (School school:schools) {
                schoolsName.add(school.getName());
            }

            schoolsAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(RegisterActivity.this, "拉取城市驾校失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<GetSchoolResponseBody> call, Throwable t) {
        Toast.makeText(RegisterActivity.this, "拉取城市驾校失败\n请检查网络", Toast.LENGTH_SHORT).show();
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
                isStudent = true;
                student = new Student(et_phone.getText().toString(),
                        et_address.getText().toString(),
                        et_idNum.getText().toString(),
                        schoolsName.get(position),
                        et_username.getText().toString(),
                        et_password.getText().toString());
                inPutInviteCode.show();
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                isStudent = false;
                inPutPlateNumDialog.show();
                break;
        }
    }


    /**
     * 注册点击回调
     */
    private class RegisterCallBack implements Callback<RegisterResponseBody>{

        @Override
        public void onResponse(Call<RegisterResponseBody> call, Response<RegisterResponseBody> response) {
            loadingView.dismiss();

            if (response.isSuccessful()){
                RegisterResponseBody body = response.body();
                if (body.isSuccess()){
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(RegisterActivity.this, "注册失败,请检查信息", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<RegisterResponseBody> call, Throwable t) {
            loadingView.dismiss();
            Toast.makeText(RegisterActivity.this, "注册失败,请检查网络", Toast.LENGTH_SHORT).show();
        }
    }


}
