package com.lingzhuo.textknow01.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.lingzhuo.textknow01.R;
import com.lingzhuo.textknow01.activity.EditorActivity;
import com.lingzhuo.textknow01.activity.ThemeActivity;
import com.lingzhuo.textknow01.bean.Theme;
import com.lingzhuo.textknow01.bean.ThemeStories;
import com.lingzhuo.textknow01.bean.ThemeStory;
import com.lingzhuo.textknow01.bean.ZhubianBean;
import com.lingzhuo.textknow01.utils.BitmapCache;
import com.lingzhuo.textknow01.utils.VolleyUtils;
import com.lingzhuo.textknow01.view.ItemView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Wang on 2016/5/14.
 */
public class ThemeFragment extends Fragment {
    private View view;
    private Theme theme;
    private ImageView imageView_themeTop;
    private TextView textView_themeTopTitle;
    private GridLayout gridLayout_theme;
    private LinearLayout linearLayout_zhubian;

    public ThemeFragment(Theme theme) {
        this.theme = theme;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_theme, null);
        init();
        initDate();
        getDataFromServer();
        return view;
    }

    private void initDate() {
        textView_themeTopTitle.setText(theme.getDescription());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView_themeTop, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        //可以看出来，加了缓存功能就是这里创建ImageLoader和上面不一样了，这里使用了我们光创建的缓存的工具类
        ImageLoader imageLoader = new ImageLoader(VolleyUtils.newInstance(getContext()).getRequestQueue(), new BitmapCache());
        imageLoader.get(theme.getThumbnail(), listener);
    }

    private void init() {
        imageView_themeTop = (ImageView) view.findViewById(R.id.imageView_themeTop);
        textView_themeTopTitle = (TextView) view.findViewById(R.id.textView_themeTopTitle);
        gridLayout_theme = (GridLayout) view.findViewById(R.id.gridLayout_theme);
        linearLayout_zhubian = (LinearLayout) view.findViewById(R.id.linearLayout_zhubian);
    }

    public void getDataFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://news-at.zhihu.com/api/4/theme/" + theme.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.contains("stories")) {
                            Toast.makeText(getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                        } else {
                            ThemeStories themeStories = JSON.parseObject(response, ThemeStories.class);
                            initGridLayoutData(themeStories.getStories());
                            initEditorsData(themeStories.getEditors());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyUtils.newInstance(getContext()).addQueue(stringRequest);
    }

    private void initEditorsData(List<ZhubianBean> editors) {
        if (editors.size() == 0) {
            linearLayout_zhubian.setVisibility(View.GONE);
        } else {
            for (final ZhubianBean zhubianBean : editors) {
                CircleImageView circleImageView = new CircleImageView(getContext());
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(circleImageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                //可以看出来，加了缓存功能就是这里创建ImageLoader和上面不一样了，这里使用了我们光创建的缓存的工具类
                final ImageLoader imageLoader = new ImageLoader(VolleyUtils.newInstance(getContext()).getRequestQueue(), new BitmapCache());
                imageLoader.get(zhubianBean.getAvatar(), listener);
                //这是将这些小点，添加到相应的布局中，同时设置布局的各种属性，边距，大小等等。
                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layout.leftMargin = 20;
                layout.rightMargin = 20;
                linearLayout_zhubian.addView(circleImageView, layout);


                circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(), EditorActivity.class);
                        intent.putExtra("urls",zhubianBean.getUrl());
                        getContext().startActivity(intent);
                    }
                });

            }
        }

    }

    private void initGridLayoutData(List<ThemeStory> stories) {
        for (final ThemeStory themeStory : stories) {
            ItemView itemView = new ItemView(getContext());
            TextView textView_title = (TextView) itemView.findViewById(R.id.textView_item_stories_title);
            final ImageView imageView_pic = (ImageView) itemView.findViewById(R.id.textView_item_stories_pic);
            textView_title.setText(themeStory.getTitle());
            if (themeStory.getImages() != null) {
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView_pic, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                //可以看出来，加了缓存功能就是这里创建ImageLoader和上面不一样了，这里使用了我们光创建的缓存的工具类
                ImageLoader imageLoader = new ImageLoader(VolleyUtils.newInstance(getContext()).getRequestQueue(), new BitmapCache());
                imageLoader.get(themeStory.getImages().get(0), listener);
            } else {
                imageView_pic.setVisibility(View.GONE);
            }
            gridLayout_theme.addView(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ThemeActivity.class);
                    intent.putExtra("StoryId", themeStory.getId());
                    getContext().startActivity(intent);
                }
            });
        }
    }
}
