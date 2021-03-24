package com.example.demo_baidumap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAddCityAdapter extends BaseAdapter {
    private List<CityMsg> mDate;
    private Context mContext;

    public MyAddCityAdapter(List<CityMsg> mDate, Context mContext) {
        this.mDate = mDate;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mDate.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.add_city_item_layout,parent,false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.add_city_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mDate.get(position).getCityName());
        return convertView;
    }
    private class ViewHolder{
        TextView textView;
    }

}
