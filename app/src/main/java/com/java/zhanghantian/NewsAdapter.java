package com.java.zhanghantian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Iterator;


class ViewHolder
{
    public TextView item_tv_title;
    public TextView item_tv_influence;
    public TextView item_tv_time;
    public TextView item_tv_cat_type;// category+" "+type
}


public class NewsAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayDeque<NewsBean> mDatas;

    public NewsAdapter(Context context,  ArrayDeque<NewsBean>  newsList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDatas = newsList;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position)
    {
        Iterator it =mDatas.iterator();
        for(int i=0;i<position-1; i++){
            if(it.hasNext()) it.next();
            else return null;
        }
        if(it.hasNext()) return it.next();
        else return null;
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
            viewHolder.item_tv_title = (TextView) convertView.findViewById(R.id.item_tv_title);
            viewHolder.item_tv_cat_type = (TextView) convertView.findViewById(R.id.item_tv_cat);
            viewHolder.item_tv_influence = (TextView) convertView.findViewById(R.id.item_tv_inf);
             viewHolder.item_tv_time = (TextView) convertView.findViewById(R.id.item_tv_time);
             convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();//error??
        }
        NewsBean newsBean = (NewsBean) getItem(position);
        viewHolder.item_tv_influence.setText(newsBean.getInfluence());
        viewHolder.item_tv_title.setText(newsBean.getTitle());
        viewHolder.item_tv_time.setText(newsBean.getTime());
        viewHolder.item_tv_cat_type.setText(newsBean.getCategory()+ " "+newsBean.getType());
        if(newsBean.isSeen()){
            viewHolder.item_tv_title.setTextColor(0x666666);
            viewHolder.item_tv_time.setTextColor(0x666666);
            viewHolder.item_tv_cat_type.setTextColor(0x666666);
        }
        return convertView;
    }
}