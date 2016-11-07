package com.qingyuan.tianya.mrbuy.bean;

/**
 * Created by gaoyanjun on 2016/8/8.
 */
public class SearchBean {

    private String iconId;
    private String title;
    private String content;
    private String comments;

    public SearchBean(String iconId, String title, String content, String comments) {
        this.iconId = iconId;
        this.title = title;
        this.content = content;
        this.comments = comments;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
