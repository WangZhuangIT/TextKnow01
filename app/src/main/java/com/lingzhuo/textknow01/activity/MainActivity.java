package com.lingzhuo.textknow01.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lingzhuo.textknow01.R;
import com.lingzhuo.textknow01.bean.Theme;
import com.lingzhuo.textknow01.bean.ThemeAllBean;
import com.lingzhuo.textknow01.db.KnowDB;
import com.lingzhuo.textknow01.fragment.MainFragment;
import com.lingzhuo.textknow01.fragment.ThemeFragment;
import com.lingzhuo.textknow01.utils.VolleyUtils;
import com.lingzhuo.textknow01.view.MainTitle;
import com.lingzhuo.textknow01.view.SlidingMenuTheme;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SlidingMenu slidingMenu;
    private MainTitle mainTitle;
    private RelativeLayout relativeLayout_main;
    private boolean isNight = false;
    private FragmentManager manager;
    private Fragment mainFragment,themeFragment;
    private RelativeLayout relativeLayout;
    private GridLayout gridLayout_slidingMenu;
    private TextView textView_mainTitle;
    private UpdateSlidingMenuThemReceiver updateSlidingMenuThemReceiver;
    private IntentFilter intentFilter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initSlidingMenu();
        initMainTitle();

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_main, mainFragment);
        transaction.commit();
        getSlidingThemeData();
    }

    private void init() {
        relativeLayout_main = (RelativeLayout) findViewById(R.id.relativeLayout_main);
        updateSlidingMenuThemReceiver=new UpdateSlidingMenuThemReceiver();
        intentFilter=new IntentFilter();
        intentFilter.addAction("UpdateSlidingMenuThem");
        registerReceiver(updateSlidingMenuThemReceiver,intentFilter);
    }

    private void initMainTitle() {
        mainTitle = (MainTitle) findViewById(R.id.mainTitle);
        ImageView imageView_home = (ImageView) mainTitle.findViewById(R.id.mainTitle_imageView_home);
        imageView_home.setOnClickListener(this);
        ImageView imageView_setting = (ImageView) mainTitle.findViewById(R.id.mainTitle_imageView_setting);
        imageView_setting.setOnClickListener(this);
        textView_mainTitle= (TextView) mainTitle.findViewById(R.id.mainTitle_textView_title);
        textView_mainTitle.setText("首页");
        mainFragment = new MainFragment();
    }

    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        //设置滑动菜单在左边还是右边
        slidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        slidingMenu.setMenu(R.layout.layout_slidingmenu);


        relativeLayout = (RelativeLayout) slidingMenu.findViewById(R.id.slidingMenu_home);
        relativeLayout.setOnClickListener(this);
        gridLayout_slidingMenu = (GridLayout) slidingMenu.findViewById(R.id.gridLayout_slidingMenu);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainTitle_imageView_home:
                slidingMenu.toggle();
                break;
            case R.id.mainTitle_imageView_setting:
                if (isNight) {
                    relativeLayout_main.setBackgroundColor(Color.rgb(255, 255, 255));
                    isNight = false;
                } else {
                    relativeLayout_main.setBackgroundColor(Color.rgb(72, 71, 71));
                    isNight = true;
                }
                break;
            case R.id.slidingMenu_home:
                mainFragment = new MainFragment();
                FragmentTransaction transaction_home = manager.beginTransaction();
                transaction_home.replace(R.id.fragment_main, mainFragment);
                transaction_home.commit();
                textView_mainTitle.setText("首页");
                slidingMenu.toggle();
                break;
        }
    }

    //用于获取侧滑菜单的栏目信息的方法
    public void getSlidingThemeData() {
        if (KnowDB.newInstance(getApplicationContext()).loadTheme().size() == 0) {
            getThemeDataFromServer();
        } else {
            initGridLayout();
        }
    }

    private void initGridLayout() {
        List<Theme> themes = KnowDB.newInstance(getApplicationContext()).loadTheme();
        for (Theme theme : themes) {
            final SlidingMenuTheme slidingMenuTheme = new SlidingMenuTheme(getApplicationContext());
            TextView textView = (TextView) slidingMenuTheme.findViewById(R.id.slidingmune_themeName);
            textView.setText(theme.getName());
            gridLayout_slidingMenu.addView(slidingMenuTheme);
            slidingMenuTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Theme themeClick=KnowDB.newInstance(getApplicationContext()).loadTheme().get(gridLayout_slidingMenu.indexOfChild(slidingMenuTheme)-1);
                     themeFragment= new ThemeFragment(themeClick);
                    FragmentTransaction transaction_theme = manager.beginTransaction();
                    transaction_theme.replace(R.id.fragment_main, themeFragment);
                    transaction_theme.commit();
                    textView_mainTitle.setText(themeClick.getName());
                    slidingMenu.toggle();
                }
            });

        }
    }

    private void getThemeDataFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://news-at.zhihu.com/api/4/themes",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("{\"limit\":1000,\"subscribed\":[],\"others\"")) {
                            ThemeAllBean themeAllBean = JSON.parseObject(response, ThemeAllBean.class);
                            KnowDB.newInstance(getApplicationContext()).deleteTheme();
                            for (Theme theme : themeAllBean.getOthers()) {
                                KnowDB.newInstance(getApplicationContext()).saveTheme(theme);
                            }
                            getSlidingThemeData();
                        }else {
                            Toast.makeText(MainActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyUtils.newInstance(getApplicationContext()).addQueue(stringRequest);
    }

    class UpdateSlidingMenuThemReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            getSlidingThemeData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateSlidingMenuThemReceiver);
    }
}
