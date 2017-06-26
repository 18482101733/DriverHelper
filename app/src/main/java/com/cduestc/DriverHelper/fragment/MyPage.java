package com.cduestc.DriverHelper.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.adapter.ReservationListAdapter;
import com.cduestc.DriverHelper.api.ApiService;
import com.cduestc.DriverHelper.api.Tag;
import com.cduestc.DriverHelper.bean.Coach;
import com.cduestc.DriverHelper.bean.GetUserResponseBody;
import com.cduestc.DriverHelper.bean.ReservationBody;
import com.cduestc.DriverHelper.bean.ReservationResponse;
import com.cduestc.DriverHelper.bean.Student;
import com.cduestc.DriverHelper.bean.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by c on 2017/3/2.
 */
public class MyPage extends Fragment implements Callback<ResponseBody>, AdapterView.OnItemClickListener, View.OnClickListener ,
        DialogInterface.OnClickListener{

    private static final String MEDIA_TYPE = "image/*";

    private ApiService apiService;
    private Context context;
    private int userPower;
    private TextView tv_name, tv_address,tv_phone,tv_school;
    private ImageView iv_icon;
    private ListView lv_reservation;
    private User user;
    private Student student;
    private Coach coach;
    private String uid;
    private GetUserCallBack callBack;
    private ReservationListAdapter adapter;
    private ArrayList<ReservationBody> data;
    private TextView tv_reservation_dire,tv_reservation;
    private String path = "";
    private ProgressDialog progressDialog;
    private int random = 0;
    private AlertDialog dropReservationDialog;
    private AlertDialog.Builder builder;
    private int position ;

    public MyPage() {
        apiService = ApiService.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();

        View view = inflater.inflate(R.layout.my_page,container,false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        uid = getArguments().getString(Tag.USER_UID);
        userPower = getArguments().getInt(Tag.USER_POWER);
        data = new ArrayList<>();
        adapter = new ReservationListAdapter(context, data,userPower);
        lv_reservation.setAdapter(adapter);

        Glide.with(this)
                .load(Tag.GET_USER_ICON+uid+"&random="+random)
                .placeholder(R.mipmap.user_icon)
                .error(R.mipmap.user_icon)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(iv_icon);

        tv_reservation.setText("我的学车");
        iv_icon.setOnClickListener(this);

        if (userPower == Tag.USER_STUDENT){
            lv_reservation.setOnItemClickListener(this);
            apiService.getStudentInfo(this,uid);
            apiService.getMyReservation(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try{
                        if (!response.isSuccessful()){
                            Toast.makeText(context,"获取预约信息失败",Toast.LENGTH_SHORT).show();
                            tv_reservation_dire.setText("获取预约信息失败");
                            Log.i("mypage", "onResponse: " + response.errorBody().string());
                        }else {
                            String json = response.body().string();
                            Log.i("mypage", "onResponse: " + json);
                            if (new JSONObject(json).optBoolean("success")){
                                ReservationResponse reservationResponse = new Gson().fromJson(json,ReservationResponse.class);
                                data.clear();
                                data.addAll(reservationResponse.getAppoints());

                                if (data.size() == 0){
                                    tv_reservation_dire.setText("暂无预约信息");
                                }else{
                                    Collections.sort(data, new Comparator<ReservationBody>() {
                                        @Override
                                        public int compare(ReservationBody lhs, ReservationBody rhs) {
                                            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            try {
                                                Date before = dataFormat.parse(lhs.getAppointDate());
                                                Date after = dataFormat.parse(rhs.getAppointDate());
                                                if (before.getTime() >= after.getTime())
                                                    return 1;
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            return -1;
                                        }
                                    });

                                    for (int i = 0; i < data.size(); i++) {
                                        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Date before = dataFormat.parse(data.get(i).getAppointDate());
                                        if (new Date().getTime() > before.getTime()){
                                            data.remove(i);
                                        }
                                    }

                                    adapter.notifyDataSetChanged();
                                    tv_reservation_dire.setVisibility(View.GONE);
                                    lv_reservation.setVisibility(View.VISIBLE);
                                }

                            }else{
                                tv_reservation_dire.setText("获取预约信息失败");
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    tv_reservation_dire.setText("暂无预约信息");
                }
            },uid);

        }else if (userPower == Tag.USER_COACH){
            apiService.getCoachInfo(this,uid);
            apiService.getStudentReservation(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try{
                        if (!response.isSuccessful()){
                            Toast.makeText(context,"获取预约信息失败",Toast.LENGTH_SHORT).show();
                            tv_reservation_dire.setText("获取预约信息失败");
                            Log.i("mypage", "onResponse: " + response.errorBody().string());
                        }else {
                            String json = response.body().string();
                            if (new JSONObject(json).optBoolean("success")){

                                ReservationResponse reservationResponse = new Gson().fromJson(json,ReservationResponse.class);
                                data.clear();
                                data.addAll(reservationResponse.getAppoints());

                                if (data.size() == 0){
                                    tv_reservation_dire.setText("暂无预约信息");
                                }else{

                                    for (int i = 0; i < data.size(); i++) {
                                        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Date before = dataFormat.parse(data.get(i).getAppointDate());
                                        if (new Date().getTime() < before.getTime()){
                                            data.remove(i);
                                        }
                                    }

                                    Collections.sort(data, new Comparator<ReservationBody>() {
                                        @Override
                                        public int compare(ReservationBody lhs, ReservationBody rhs) {
                                            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            try {
                                                Date before = dataFormat.parse(lhs.getAppointDate());
                                                Date after = dataFormat.parse(rhs.getAppointDate());
                                                if (before.getTime() > after.getTime())
                                                    return -1;
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            return 1;
                                        }
                                    });
                                    adapter.notifyDataSetChanged();
                                    tv_reservation_dire.setVisibility(View.GONE);
                                    lv_reservation.setVisibility(View.VISIBLE);
                                }
                            }else{
                                tv_reservation_dire.setText("获取预约信息失败");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            },uid);
        }
    }


    private void initView(View view){

        builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("确定取消当前日期预约吗?");
        builder.setPositiveButton("确定", this);
        builder.setNegativeButton("取消",this);
        dropReservationDialog = builder.create();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("上传中");
        tv_name  = (TextView) view.findViewById(R.id.tv_name);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_school = (TextView) view.findViewById(R.id.tv_school);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        lv_reservation = (ListView) view.findViewById(R.id.lv_reservation);
        tv_reservation_dire = (TextView) view.findViewById(R.id.tv_reservation_dire);
        tv_reservation = (TextView) view.findViewById(R.id.tv_reservation_pm);
    }

    private void setData(User user){
        tv_address.setText("地址: " + user.getAddress());
        tv_name.setText("姓名: " + user.getName());
        tv_phone.setText("电话: " + user.getUid());
        tv_school.setText("学校:" + user.getSchoolName());
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()){
            ResponseBody body = response.body();
            try {
                String json = body.string();

                switch (userPower){
                    case Tag.USER_COACH:
                        Type coachType = new TypeToken<GetUserResponseBody<Coach>>(){}.getType();
                        GetUserResponseBody<Coach> userCoach = new Gson().fromJson(json,coachType);
                        user = userCoach.getUser();
                        if (user!= null)
                            setData(user);
                        callBack.setUserInstance(user);
                        break;

                    case Tag.USER_STUDENT:
                        Type studentType = new TypeToken<GetUserResponseBody<Student>>(){}.getType();
                        GetUserResponseBody<Student> userStudent = new Gson().fromJson(json,studentType);
                        user = userStudent.getUser();
                        if(user == null){
                            Toast.makeText(context, "获取个人信息失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        setData(user);
                        callBack.setUserInstance(user);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(context, "拉取个人数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(context, "拉取个人数据失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {

        if (context instanceof GetUserCallBack){
            callBack = (GetUserCallBack) context;
        }

        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (activity instanceof GetUserCallBack) {
                callBack = (GetUserCallBack) activity;
            } else {
                throw new RuntimeException(activity.toString()
                        + " must implement GetUserCallBack");
            }
        }
        super.onAttach(activity);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dropReservationDialog.show();
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_icon:
                Intent pickUserIcon = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickUserIcon.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MEDIA_TYPE);
                startActivityForResult(pickUserIcon,Tag.GET_SYSTEM_PHOTO);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            if (data == null){
                return;
            }
            switch (requestCode){
                case Tag.GET_SYSTEM_PHOTO:
                    Cursor cursor = context.getContentResolver().query(data.getData(),new String[]{MediaStore.Images.Media.DATA},null,null,null);
                    if(cursor!=null&&cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        path = cursor.getString(column_index);
                        cursor.close();
                    }
                    if ("".equals(path))
                        return;
                    progressDialog.show();
                    apiService.uploadIcon(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            try{
                                if (!response.isSuccessful()){
                                    Toast.makeText(context, "获取信息失败", Toast.LENGTH_SHORT).show();
                                    Log.i("123", "onResponse: " + response.errorBody().string());
                                }else{
                                    String json = response.body().string();
                                    JSONObject jsonObject = new JSONObject(json);
                                    if (jsonObject.optBoolean("success")){
                                        Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();

                                        Random tempRandom = new Random();
                                        random = tempRandom.nextInt(1000);
                                        Glide.with(context)
                                                .load(new File(path))
                                                .placeholder(R.mipmap.user_icon)
                                                .error(R.mipmap.user_icon)
                                                .bitmapTransform(new CropCircleTransformation(context))
                                                .into(iv_icon);
                                    }else {
                                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                            progressDialog.dismiss();
                            Log.i("123", "onFailure: " + t.getMessage());
                        }
                    }, user, path);

                    break;
            }
        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                apiService.dropReservation(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       try{
                           if (!response.isSuccessful()){
                               Toast.makeText(context, "取消预约失败", Toast.LENGTH_SHORT).show();
                               Log.i("123", "onResponse: " +response.errorBody().string());
                           }else {
                               String json = response.body().string();
                               JSONObject jsonObject = new JSONObject(json);
                               if (jsonObject.optBoolean("success")){
                                   Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
                                   data.remove(position);
                                   adapter.notifyDataSetChanged();
                               }else {
                                   Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
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
                },data.get(position).getAppointDate(),data.get(position).getAppointTime(),data.get(position).getStudentId());
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;

        }
    }

    public interface GetUserCallBack {
        void setUserInstance(User user);
    }

}
