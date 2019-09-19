package com.mpos.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mpos.Model.Transaction_Model;
import com.mpos.activity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.alibraheem on 07/01/2018.
 */

public class transactionAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<Object> mData = new ArrayList<Object>();
    Typeface tfBold;
    public transactionAdapter(Context context) {
        mContext = context;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void addItem(List<Transaction_Model> item) {
        mData.addAll(item);

    }
    public void updateTransactionList(List<Transaction_Model> newlist) {
        mData.clear();
        mData.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View grid;


        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.transaction_row, null);
        }
        else
        {
            grid = (View) convertView;
        }

        TextView txt_Amount = (TextView) grid.findViewById(R.id.t_Amount);
        TextView txt_Date = (TextView) grid.findViewById(R.id.t_Date);
        TextView txt_RNN = (TextView) grid.findViewById(R.id.t_RNN);

        Transaction_Model t = new Transaction_Model();
        t=(Transaction_Model) mData.get(position);
        txt_Amount.setText(t.getAmount());
        txt_Date.setText(t.getDate());
        txt_RNN.setText(t.getRRN());
        return grid;
    }
}
