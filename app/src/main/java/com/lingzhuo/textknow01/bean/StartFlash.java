package com.lingzhuo.textknow01.bean;

/**
 * Created by Wang on 2016/5/5.
 */
public class StartFlash {
    private String text;
    private String img;

    public StartFlash() {
    }

    public StartFlash(String text, String img) {
        this.text = text;
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
