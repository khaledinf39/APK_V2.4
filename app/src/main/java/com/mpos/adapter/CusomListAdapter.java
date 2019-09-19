package com.mpos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mpos.activity.R;

import java.util.ArrayList;
import java.util.TreeSet;

public class CusomListAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<Object> mData = new ArrayList<Object>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public CusomListAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String name, final String value) {
        mData.add(new KeyValue(name, value));
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);
        Object obj =null;

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.list_details, null);
                    holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
                    holder.txtValue = (TextView) convertView.findViewById(R.id.txt_value);
                    break;

                case TYPE_SEPARATOR:
                    String header = null;
                    Object object = mData.get(position);
                    convertView = mInflater.inflate(R.layout.list_header, null);
                    holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);

                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (rowType) {
            case TYPE_ITEM:

                KeyValue keyValue =  (KeyValue)mData.get(position);
                holder.txtName.setText(keyValue.getKey());
                holder.txtValue.setText(keyValue.getValue());
                break;

            case TYPE_SEPARATOR:
                String header = (String)mData.get(position);;
                holder.txtName.setText(header);
                break;
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView txtName;
        public TextView txtValue;
    }

}