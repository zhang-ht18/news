package com.java.zhanghantian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

public class EntityAdapter extends BaseExpandableListAdapter {
    private List<EntityBean>mData;
    private List<String>label;
    private LayoutInflater mLayoutInflater;
    public EntityAdapter(Context context, List<EntityBean> list)
    {
        mLayoutInflater = LayoutInflater.from(context);
        mData = list;
        label = new ArrayList<>();
        for(int i=0;i<mData.size();i++)
        {
            label.add(mData.get(i).getLabel());
        }
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public int getGroupCount() {
        if (mData != null){
            return mData.size();
        }
        return 0;
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return label.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition,int childPosition) {
        return mData.get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        /*
        todo 上级试图
         */
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        /*
        todo 下级视图
         */
        return convertView;
    }




}
