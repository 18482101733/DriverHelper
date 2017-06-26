package com.cduestc.DriverHelper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.cduestc.DriverHelper.R;
import com.cduestc.DriverHelper.adapter.MainPagerAdapter;
import com.cduestc.DriverHelper.api.Tag;
import com.cduestc.DriverHelper.bean.User;
import com.cduestc.DriverHelper.fragment.MyPage;
import com.cduestc.DriverHelper.fragment.ReservationExam;
import com.cduestc.DriverHelper.fragment.ReservationLearnCar;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity implements MyPage.GetUserCallBack,DialogInterface.OnClickListener {

    private NavigationTabBar nt_nav;
    private ArrayList<NavigationTabBar.Model> models;
    private ArrayList<Fragment> tabs;
    private MainPagerAdapter pagerAdapter;
    private ReservationExam reservationExam;
    private ReservationLearnCar learnCar;
    private MyPage myPage;
    private ViewPager vp_tabs;
    private FragmentManager manager;
    private TextView tv_title;
    private int user_power;
    private String uid;
    private Toolbar tb_bar;
    private User user;
    private SetUserForLearn setUserForLearn;

    private AlertDialog.Builder exitDialogBuilder;
    private AlertDialog exitDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String[] colors = {"#df4c49","#63cba5","#f4af5f"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView(){
        nt_nav = (NavigationTabBar) findViewById(R.id.nt_nav);
        vp_tabs = (ViewPager) findViewById(R.id.vp_tabs);
        tb_bar = (Toolbar) findViewById(R.id.tb_bar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        exitDialogBuilder = new AlertDialog.Builder(this).setIcon(R.mipmap.exit);
        exitDialogBuilder.setMessage("确定要退出登录吗?");
        exitDialogBuilder.setPositiveButton("确定",this);
        exitDialogBuilder.setNegativeButton("取消",this);
        exitDialog = exitDialogBuilder.create();
        initToolBar();
    }

    /**
     * 初始化数据
     */
    private void initData(){
        sharedPreferences = getSharedPreferences(Tag.DRIVER_HELPER,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_power = getIntent().getIntExtra(Tag.USER_POWER,-1);
        uid = getIntent().getStringExtra(Tag.USER_UID);

        models = new ArrayList<>();
        models.add(getModel(colors[0],R.mipmap.car,"预约学车","预约学车"));
        models.add(getModel(colors[1],R.mipmap.exam,"预约考试","预约考试"));
        models.add(getModel(colors[2],R.mipmap.me,"我的主页","我的主页"));
        nt_nav.setModels(models);
        nt_nav.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        nt_nav.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        nt_nav.setIsBadged(true);
        nt_nav.setBgColor(Color.BLACK);
        nt_nav.setIsSwiped(true);
        nt_nav.setIsBadgeUseTypeface(true);
        nt_nav.setBadgeBgColor(Color.RED);
        nt_nav.setBadgeTitleColor(Color.WHITE);


        reservationExam = new ReservationExam();
        learnCar = new ReservationLearnCar();
        myPage = new MyPage();
        tabs = new ArrayList<>();
        tabs.add(learnCar);
        tabs.add(reservationExam);
        tabs.add(myPage);
        Bundle bundle = new Bundle();
        bundle.putInt(Tag.USER_POWER,user_power);
        bundle.putString(Tag.USER_UID,uid);
        myPage.setArguments(bundle);
        manager = getSupportFragmentManager();
        pagerAdapter = new MainPagerAdapter(manager,tabs,this);
        vp_tabs.setAdapter(pagerAdapter);
        vp_tabs.setCurrentItem(3);
        nt_nav.setViewPager(vp_tabs,3);

        /**
         * 设置滑动监听
         */
        nt_nav.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                nt_nav.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 2:
                        tv_title.setText("我的主页");
                        break;
                    case 1:
                        tv_title.setText("预约考试");
                        break;
                    case 0:
                        if (user_power == Tag.USER_COACH)
                            tv_title.setText("我的评价");
                        if (user_power == Tag.USER_STUDENT)
                            tv_title.setText("预约学车");

                            break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initToolBar(){
        setSupportActionBar(tb_bar);
    }

    /**
     * 获取底部Tab
     * @param color
     * @param mipmapId
     * @param title
     * @param badgeTitle
     * @return
     */
    private NavigationTabBar.Model getModel(String color,int mipmapId,String title,String badgeTitle){

        return  new NavigationTabBar.Model.Builder(
                getResources().getDrawable(mipmapId),
                Color.parseColor(color)
        ).title(title)
                .badgeTitle(badgeTitle)
                .build();
    }

    /**
     * 设置用户对象实例
     * @param user
     */
    @Override
    public void setUserInstance(User user) {
        this.user = user;
        if (learnCar instanceof SetUserForLearn){
            setUserForLearn = learnCar;
            setUserForLearn.setUser(user);
        }
    }

    /**
     * 创建菜单栏
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options_menu,menu);
        return true;
    }

    /**
     * 底部Tab的点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_config:
                Intent intent = new Intent(this,ModifyActivity.class);
                intent.putExtra(Tag.USER,user);
                startActivity(intent);
                break;

            case R.id.menu_exit:
                exitDialog.show();
                break;

            case R.id.menu_setSecret:
                if (user == null){
                    Toast.makeText(MainActivity.this, "暂时未获得用户信息,请稍后", Toast.LENGTH_SHORT).show();
                }
                Intent startSetSecretIntent = new Intent(this,SetSecretActivity.class);
                startSetSecretIntent.putExtra(Tag.USER,user);
                startActivity(startSetSecretIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 弹窗点击事件
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case AlertDialog.BUTTON_POSITIVE:
                editor.putBoolean(Tag.AUTO_LOGIN,false);
                editor.putString(Tag.USERNAME,"");
                editor.putString(Tag.UID,"");
                editor.putInt(Tag.USER_POWER,0);
                editor.apply();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
        }
    }

    /**
     * 设置用户对象接口
     */
    public interface SetUserForLearn {
        void setUser(User user);
    }

}
