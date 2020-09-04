package com.java.zhanghantian;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    public String getIcon()
    {
        return imgUrl;
    }

}
