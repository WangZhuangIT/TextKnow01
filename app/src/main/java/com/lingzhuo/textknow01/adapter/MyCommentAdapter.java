package com.lingzhuo.textknow01.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.lingzhuo.textknow01.R;
import com.lingzhuo.textknow01.bean.Comment;
import com.lingzhuo.textknow01.utils.BitmapCache;
import com.lingzhuo.textknow01.utils.VolleyUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Wang on 2016/5/13.
 */
public class MyCommentAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> commentList;

    public MyCommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView_commentPic = (ImageView) convertView.findViewById(R.id.comment_pic);
            viewHolder.textView_commentName = (TextView) convertView.findViewById(R.id.comment_name);
            viewHolder.textView_commentContent = (TextView) convertView.findViewById(R.id.comment_content);
            viewHolder.textView_commentTime = (TextView) convertView.findViewById(R.id.comment_time);
            viewHolder.textView_commentZanNum = (TextView) convertView.findViewById(R.id.zan_comment_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment = commentList.get(position);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.imageView_commentPic, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        //可以看出来，加了缓存功能就是这里创建ImageLoader和上面不一样了，这里使用了我们光创建的缓存的工具类
        ImageLoader imageLoader = new ImageLoader(VolleyUtils.newInstance(context).getRequestQueue(), new BitmapCache());
        imageLoader.get(comment.getAvatar(), listener);
        viewHolder.textView_commentName.setText(comment.getAuthor());
        if (comment.getContent().length() > 70) {
            viewHolder.textView_commentContent.setText(comment.getContent().substring(0, 70) + "......(点击查看全部)");
        } else {
            viewHolder.textView_commentContent.setText(comment.getContent());
        }
        viewHolder.textView_commentTime.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(1463126683)));
        viewHolder.textView_commentZanNum.setText(comment.getLikes());
        return convertView;
    }

    class ViewHolder {
        ImageView imageView_commentPic;
        TextView textView_commentName, textView_commentContent, textView_commentTime, textView_commentZanNum;
    }

}
