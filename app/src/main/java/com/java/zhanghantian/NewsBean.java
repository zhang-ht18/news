package com.java.zhanghantian;

public class NewsBean {  //todo 新闻内容也放在其中
    private int _id;
    private String type;// 列表分类依据
    private String title;
    private String category;//基本只有“疫情”一种
    private String time;
    private String lang;
    private int influence;
    private boolean isFavorite;
    private boolean isSeen;

    public NewsBean(int _id, String type, String title, String category, String time, String lang, int influence) {
        this._id = _id; this.type=type; this.title=title; this.category=category; this.time=time; this.lang=lang; this.influence=influence;
        this.isFavorite = false; this.isSeen = false;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite() {
        isFavorite = true;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = true;
    }

    public int get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getInfluence() {
        if(influence==0) return "";
        return "影响力："+influence;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getLang() {
        return lang;
    }

}
