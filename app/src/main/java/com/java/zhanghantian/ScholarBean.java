package com.java.zhanghantian;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
public class ScholarBean {
    //头像的url
    String imgUrl;
    //姓名
    String name;
    //数据
    List<Double>data;
    //学位
    String postion;
    //职位
    String affiliation;
    //简介
    String intro;
    //是否离世
    boolean isGone;

    public ScholarBean(String _name, String _position, String _affiliation, String _intro, String _imgUrl, boolean _isGone, List<Double>_data)
    {
        name = _name; postion = _position; affiliation = _affiliation; intro = _intro;
        imgUrl = _imgUrl; isGone = _isGone; data = _data;
    }

    public String getName(){return name;}
    public String getPostion(){return postion;}
    public String getAffiliation(){return affiliation;}
    public String getIntro(){return intro;}
    public boolean getIsGone(){return isGone;}
    public String getData()
    {
        String dataInfo = "学术成就:"+data.get(0)+" 活跃度:"+data.get(1)+" 学术合作:"+data.get(2)
                +" 引用数:"+data.get(3)+" 论文数:"+data.get(4);
        return dataInfo;
    }

    public Bitmap getIcon()
    {
        URL url = null; Bitmap bitmap = null;
        try {
            url = new URL(imgUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
