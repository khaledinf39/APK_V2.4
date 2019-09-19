package com.mpos.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mpos.Model.Recon_Model;
import com.mpos.activity.R;

import java.util.ArrayList;

/**
 * Created by m.alibraheem on 07/01/2018.
 */

public class ReconAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<Object> mData = new ArrayList<Object>();
    Typeface tfBold;
    public ReconAdapter(Context context) {
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
    public void addItem(final Object item) {
        mData.add(item);
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
            grid = inflater.inflate(R.layout.recon_row, null);
        }
        else
        {
            grid = (View) convertView;
        }
        TextView txt_Date = (TextView) grid.findViewById(R.id.t_reconDT);

        Recon_Model r_model = new Recon_Model();
        r_model=(Recon_Model) mData.get(position);
        txt_Date.setText(r_model.getReconDateTime());
        return grid;
    }
}
