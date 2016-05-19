package com.lingzhuo.textknow01.bean;

import java.util.List;

/**
 * Created by Wang on 2016/5/13.
 */
public class CommentListBean {
    private List<Comment> comments;

    public CommentListBean() {
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
