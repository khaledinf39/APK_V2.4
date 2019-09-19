package com.mpos.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.Model.Recon_Model;
import com.mpos.activity.R;
import com.mpos.adapter.ReconAdapter;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.ReconciliationResult;
import com.mpos.utils.DataBaseHelper;
import com.mpos.utils.SharedClass;

import java.util.ArrayList;

/**
 * Created by m.alibraheem on 07/01/2018.
 */

public class ReconciliationHistoryFragment extends Fragment {

    GridView gridView;

    ArrayList<Recon_Model> lst_Recon_model;
    String Id , Date ,XML;
    TextView hed_Date;
    ReconAdapter adapter;
    Cursor res;
    DataBaseHelper obj_db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recon_listview, container, false);
        obj_db = new DataBaseHelper(getActivity());
        Bundle _bundle = this.getArguments();
        boolean menu = _bundle.getBoolean("menu");
//bundle.putString("Date",obj_sharedClass.getCurrentDateTime(getActivity()));
        if(menu)
        {
            res = obj_db.getAllData("Reconciliation","1","10");
            hed_Date = (TextView) view.findViewById(R.id.hed_t_ReconDT);
            hed_Date.setText("Reconciliation Date Time");


            if(res != null)
            {
                lst_Recon_model = new ArrayList<Recon_Model>();
                while (res.moveToNext())
                {
                    Id = res.getString(0);
                    Date=res.getString(1);
                    XML =res.getString(2);
                    Recon_Model r_model = new Recon_Model(Date,XML);
                    lst_Recon_model.add(r_model);
                }
                adapter = new ReconAdapter(getActivity() );
            }

            String testDate;
            for (int k = 0; k < lst_Recon_model.size(); k++) {
                testDate =lst_Recon_model.get(k).getReconDateTime();
                adapter.addItem(lst_Recon_model.get(k));
            }
            gridView = (GridView) view.findViewById(R.id.gridReconciliation);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try
                    {
                        //   Toast.makeText(getActivity(),Integer.toString (i+1),Toast.LENGTH_LONG).show();
                        DataBaseHelper obj_DataBaseHelper = new DataBaseHelper(getActivity());
                        SharedClass obj_sharedClass = new SharedClass();
                        String XML ="";
                        Cursor pokedrow ;
                        pokedrow = obj_db.pokedRow("Reconciliation",i);
                        if(pokedrow !=null)
                        {
                            while (pokedrow.moveToNext()) {
                                Date =pokedrow.getString(1);
                                XML=pokedrow.getString(2);
                            }
                        }
                        else
                        {
                        }
                        if(XML != "") {

                            String xmlResponse = (String) XML;
                            Bundle bundle = new Bundle();
                            bundle.putString("page","ReconciliationHistory");
                            bundle.putString("Date",Date);
                            ReconciliationResult reconciliationResult = MPOSService.getInstance(getActivity()).parseReconcilationResult(xmlResponse);

                            if (reconciliationResult != null) {
                                ReconciliationReportFragment fragment = new ReconciliationReportFragment();
                                fragment.setArguments(bundle);
                                fragment.setReconciliationResult( reconciliationResult);

                                getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "ReconciliationHistory")
                                        .commit();
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"NO XML FOUND",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return view;
    }
}
