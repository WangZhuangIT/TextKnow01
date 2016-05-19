package com.lingzhuo.textknow01.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lingzhuo.textknow01.R;
import com.lingzhuo.textknow01.bean.StartFlash;
import com.lingzhuo.textknow01.utils.BitmapCache;
import com.lingzhuo.textknow01.utils.VolleyUtils;

import org.json.JSONObject;

public class FlashActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private String imgUrl;
    private  AnimatorSet animatorSet;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flash);
        init();
        getStartFlash();
    }

    private void init() {
        imageView= (ImageView) findViewById(R.id.imageView_flash_activity);
        textView= (TextView) findViewById(R.id.textView_flash_activity);
    }

    public void getStartFlash() {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "http://news-at.zhihu.com/api/4/start-image/1080*1776", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        StartFlash startFlash= JSON.parseObject(response.toString(),StartFlash.class);
                        textView.setText(startFlash.getText());
                        imgUrl=startFlash.getImg();
                        getImage();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.mipmap.default_flash);
                        startAnimator();
                    }
                });
        VolleyUtils.newInstance(getApplicationContext()).addQueue(jsonObjectRequest);
    }

    private void startAnimator() {
        ObjectAnimator animatorX=ObjectAnimator.ofFloat(imageView,"ScaleX",1.0f,1.2f);
        ObjectAnimator animatorY=ObjectAnimator.ofFloat(imageView,"ScaleY",1.0f,1.2f);
        animatorSet=new AnimatorSet().setDuration(3000);
        animatorSet.playTogether(animatorX,animatorY);
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startMainActivity();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public void getImage() {
        final ImageRequest imageRequest=new ImageRequest(imgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        startAnimator();
                    }
                }, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.mipmap.default_flash);
                        startAnimator();
                    }
                });
        VolleyUtils.newInstance(getApplicationContext()).addQueue(imageRequest);
    }

    public void startMainActivity(){
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }
}
