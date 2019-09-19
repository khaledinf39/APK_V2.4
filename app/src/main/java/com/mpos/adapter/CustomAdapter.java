package com.mpos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mpos.activity.R;
import com.mpos.mpossdk.api.CardSchemeTotals;
import com.mpos.mpossdk.api.HostTotals;
import com.mpos.mpossdk.api.TransactionTotal;

import java.util.ArrayList;
import java.util.TreeSet;

public class CustomAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<Object> mData = new ArrayList<Object>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Object item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final Object item) {
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
        if(position == 22) {
            obj = (TransactionTotal)mData.get(position);
        }


        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:

                    TransactionTotal transactionTotal =  (TransactionTotal)mData.get(position);
                    convertView = mInflater.inflate(R.layout.totals_details, null);
                    holder.txnType = (TextView) convertView.findViewById(R.id.txt_name);
                    holder.txnCount = (TextView) convertView.findViewById(R.id.txt_value);
                    holder.txnTotal = (TextView) convertView.findViewById(R.id.txn_total);

                    holder.txnType.setText(transactionTotal.getTransactionType());
                    holder.txnCount.setText(transactionTotal.getCount());
                    holder.txnTotal.setText(transactionTotal.getTotal());
                    break;

                case TYPE_SEPARATOR:
                    String header = null;
                    Object object = mData.get(position);
                    convertView = mInflater.inflate(R.layout.total_header, null);
                    holder.txnHeader = (TextView) convertView.findViewById(R.id.txt_name);
                    if(object instanceof CardSchemeTotals ) {
                        CardSchemeTotals cardSchemeTotals = (CardSchemeTotals) mData.get(position);
                        header = cardSchemeTotals.getName();
                    } else {
                        HostTotals hostTotals = (HostTotals) mData.get(position);
                        header = hostTotals.getName();
                        holder.txnHeader.setTextColor(Color.parseColor("#FFFF00"));

                    }



                    holder.txnHeader.setText(header);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        switch (rowType) {
            case TYPE_ITEM:

                TransactionTotal transactionTotal =  (TransactionTotal)mData.get(position);
                holder.txnType.setText(transactionTotal.getTransactionType());
                holder.txnCount.setText(transactionTotal.getCount());
                holder.txnTotal.setText(transactionTotal.getTotal());
                break;

            case TYPE_SEPARATOR:
                String header = null;
                Object object = mData.get(position);
                 if(object instanceof CardSchemeTotals ) {
                    CardSchemeTotals cardSchemeTotals = (CardSchemeTotals) mData.get(position);
                    header = cardSchemeTotals.getName();
                } else {
                    HostTotals hostTotals = (HostTotals) mData.get(position);
                    header = hostTotals.getName();
                    holder.txnHeader.setTextColor(Color.parseColor("#FFFF00"));

                }

                holder.txnHeader.setText(header);
                break;
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView txnHeader;
        public TextView txnType;
        public TextView txnCount;
        public TextView txnTotal;
    }

}