package com.java.zhanghantian;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


class ViewHolder
{
    public TextView item_tv_time;
    public TextView item_tv_from;
    public TextView item_tv_des;
    public TextView item_tv_title;
    public ImageView item_img_icon;
}


public class NewsAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<NewsBean> mDatas;

    public NewsAdapter(Context context, List<NewsBean> newsList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDatas = newsList;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.layout_item, null);
            viewHolder = new ViewHolder();
            viewHolder.item_img_icon = (ImageView) convertView.findViewById(R.id.item_img_icon);
            ;
            viewHolder.item_tv_des = (TextView) convertView.findViewById(R.id.item_tv_des);
            viewHolder.item_tv_title = (TextView) convertView.findViewById(R.id.item_tv_title);
            viewHolder.item_tv_from = (TextView) convertView.findViewById(R.id.item_tv_from);
            viewHolder.item_tv_time = (TextView) convertView.findViewById(R.id.item_tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsBean newsBean = mDatas.get(position);
        //viewHolder.item_img_icon.set
        viewHolder.item_tv_des.setText(newsBean.getDes());
        viewHolder.item_tv_title.setText(newsBean.getTitle());
        viewHolder.item_tv_from.setText(newsBean.getFrom() + "");
        viewHolder.item_tv_time.setText(newsBean.getTime());
        return convertView;
    }
}