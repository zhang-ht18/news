package com.java.zhanghantian;

import android.os.Build;

import java.util.List;

public class EntityBean {
    //实体名称
    String label;
    //实体介绍
    String intro;
    //实体图片
    String imgUrl;
    //属性
    List<String>properties_a;
    //属性值
    List<String>properties_b;
    //关系类别
    List<String>relation;
    //前向或后向
    List<Boolean>forward;
    //关系标签
    List<String>relation_label;

    public EntityBean(String _label, String _intro, String _imgUrl,
                      List<String>_properties_a, List<String>_properties_b,
                      List<String>_relation, List<Boolean>_forward, List<String>_relation_label)
    {
        label = _label; intro = _intro; imgUrl = _imgUrl; properties_a = _properties_a;
        properties_b = _properties_b; relation = _relation; forward = _forward; relation_label = _relation_label;
    }

    public String getLabel(){return label;}
    public String getIntro(){return intro;}
    public String getImgUrl(){return imgUrl;}
    public List<String>getProperties_a(){return properties_a;}
    public List<String>getProperties_b(){return properties_b;}
    public List<String>getRalation(){return relation;}
    public List<Boolean>getForward(){return forward;}
    public List<String>getRelation_label(){return relation_label;}
}

