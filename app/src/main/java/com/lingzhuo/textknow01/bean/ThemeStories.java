package com.lingzhuo.textknow01.bean;

import java.util.List;

/**
 * Created by Wang on 2016/5/14.
 */
public class ThemeStories {
    private List<ThemeStory> stories;
    private List<ZhubianBean> editors;

    public List<ThemeStory> getStories() {
        return stories;
    }

    public void setStories(List<ThemeStory> stories) {
        this.stories = stories;
    }

    public List<ZhubianBean> getEditors() {
        return editors;
    }

    public void setEditors(List<ZhubianBean> editors) {
        this.editors = editors;
    }
}
