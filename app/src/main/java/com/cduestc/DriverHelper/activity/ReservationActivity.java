package com.cduestc.DriverHelper.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.api.ApiService;
import com.cduestc.DriverHelper.api.Tag;
import com.cduestc.DriverHelper.bean.Coach;
import com.cduestc.DriverHelper.bean.ReservationBody;
import com.cduestc.DriverHelper.bean.Student;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ReservationActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener, Callback<ResponseBody>, Toolbar.OnMenuItemClickListener {

    private Button bt_reservation,bt_set_date;
    private RadioGroup rg_time;

    private DatePickerDialog datePickerDialog;
    private Student student;
    private Coach coach;
    private int year,monthOfYear,dayOfMonth;
    private ReservationBody body;
    private String appointDate;
    private int appointTime = 1;
    private ApiService service;
    private Toolbar tb_bar;
    private TextView tv_reservation_am,tv_reservation_pm;


    private static final String TAG = "ReservationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        initView();
        initData();
    }

    private void initView(){
        tv_reservation_am = (TextView) findViewById(R.id.tv_reservation_am);
        tv_reservation_pm = (TextView) findViewById(R.id.tv_reservation_pm);
        bt_reservation = (Button) findViewById(R.id.bt_reservation);
        bt_set_date = (Button) findViewById(R.id.bt_set_date);
        rg_time = (RadioGroup) findViewById(R.id.rg_time);
        datePickerDialog = new DatePickerDialog(this,this,2017,9,20);
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        long time = System.currentTimeMillis();
        datePickerDialog.getDatePicker().setMinDate(time + 24*60*60*1000*1);
        datePickerDialog.getDatePicker().setMaxDate(time + 24*60*60*1000*7);
        setToolbar();

    }

    private void initData(){
        service = ApiService.getInstance();
        student = (Student) getIntent().getSerializableExtra(Tag.USER);
        coach = (Coach) getIntent().getSerializableExtra(Tag.COACH);
        bt_reservation.setOnClickListener(this);
        bt_set_date.setOnClickListener(this);
        rg_time.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_set_date:
                datePickerDialog.show();
                break;

            case R.id.bt_reservation:

                appointDate = year+"-"+monthOfYear+"-"+dayOfMonth;

                body = new ReservationBody(appointDate,appointTime,student.getUid(),student.getName(),coach.getUid(),coach.getName());
                service.Reservation(this,body);
                break;
        }
    }

    /**
     * 时间点击事件
     * @param view 父view
     * @param year 年
     * @param monthOfYear 月
     * @param dayOfMonth 日
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear +1;
        this.dayOfMonth = dayOfMonth;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);

        if (datePickerDialog.getDatePicker().getMaxDate() >= calendar.getTimeInMillis() && datePickerDialog.getDatePicker().getMinDate()<calendar.getTimeInMillis()){

            String date = year + "年" + (monthOfYear+1) + "月" +dayOfMonth+ "日";

            bt_set_date.setText(date);
            date = date.replaceAll("年","-");
            date = date.replaceAll("月","-");
            date = date.replaceAll("日","-");

            service.findReservation(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                   try{
                       if (!response.isSuccessful()){
                           Toast.makeText(ReservationActivity.this, "获取当天信息失败", Toast.LENGTH_SHORT).show();
                           Log.i(TAG, "onResponse: " + response.errorBody().string());
                       }else{
                           String json = response.body().string();
                           JSONObject jsonObject = new JSONObject(json);
                           if (praseJson(json)){
                               tv_reservation_am.setText("现可预约" +(4-jsonObject.optInt("numa"))+"人");
                               tv_reservation_pm.setText("现可预约" +(4-jsonObject.optInt("nump"))+"人");
                           }else {
                               Toast.makeText(ReservationActivity.this, "获取当天预约信息失败", Toast.LENGTH_SHORT).show();
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

                }
            },date,coach.getUid());

        }else {
            Toast.makeText(ReservationActivity.this, "超出预约日期", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 上午还是下午的选择按钮
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_am:
                appointTime = 1;
                break;

            case R.id.rb_pm:
                appointTime = 2;
                break;
        }
    }

    /**
     * 服务器响应回调
     * @param call 请求接口
     * @param response 响应体
     */
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        try {
            if (!response.isSuccessful()){
                Toast.makeText(this,"预约失败",Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onResponse: " + response.errorBody().string());
            }else if (!praseJson(response.body().string())){
                Toast.makeText(ReservationActivity.this, "预约失败\n人数已满或已经预约", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ReservationActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(ReservationActivity.this, "预约失败", Toast.LENGTH_SHORT).show();
    }


    /**
     * 简单解析json
     * @param json
     * @return
     */
    private boolean praseJson(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.optBoolean("success")){
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
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
     * 设置标题栏
     */
    private void setToolbar() {
        tb_bar.setTitle("预约");
        tb_bar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb_bar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
