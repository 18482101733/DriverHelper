package com.cduestc.controller.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cduestc.controller.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_coach,bt_student,bt_school,bt_states;
    private Toolbar tb_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        bt_coach = (Button) findViewById(R.id.bt_coach);
        bt_student = (Button) findViewById(R.id.bt_student);
        bt_school = (Button) findViewById(R.id.bt_school);
        bt_states = (Button) findViewById(R.id.bt_states);
        initToolbar();

    }

    private void initToolbar() {
        tb_bar.setTitle("驾校后台管理");
        tb_bar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void initData(){
        bt_coach.setOnClickListener(this);
        bt_student.setOnClickListener(this);
        bt_school.setOnClickListener(this);
        bt_states.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_coach:
                startActivity(new Intent(this,CoachInfoActivity.class));
                break;

            case R.id.bt_student:
                startActivity(new Intent(this,StudentInfoActivity.class));
                break;

            case R.id.bt_school:
                startActivity(new Intent(this,SchoolManageActivity.class));

                break;


            case R.id.bt_states:
                Intent intent = new Intent(this,SetStatesActivity.class);
                startActivity(intent);
                break;
        }
    }
}
