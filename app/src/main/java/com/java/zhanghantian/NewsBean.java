package com.java.zhanghantian;

public class NewsBean {
    //时间
    private String time;
    //标题
    private String title;
    //来源
    private String from;
    //内容
    private String content;
    //图片
    private String iconUrl;
    //描述
    private String des;

    public NewsBean(String _title, String _des, String _from, String _time)
    {
        this.title = _title; this.des = _des; this.from = _from; this.time = _time;
    }

    public String getTime(){return time;}
    public String getTitle(){return title;}
    public String getFrom(){return from;}
    public String getContent(){return content;}
    public String getDes(){return des;}
    public String getIconUrl(){return iconUrl;}
}
