package com.mpos.Model;

/**
 * Created by m.alibraheem on 07/01/2018.
 */

public class Recon_Model {

    private String ReconDateTime;
    private String Reconciliation_XML;

    public Recon_Model(String Date ,String XML)
    {
        this.ReconDateTime = Date;
        this.Reconciliation_XML = XML;
    }
    public Recon_Model()
    {
    }


    public void setReconDateTime(String _ReconDateTime) {
        ReconDateTime = _ReconDateTime;
    }
    public String getReconDateTime() {
        return ReconDateTime;
    }

    public void setReconciliation_XML(String _Reconciliation_XML) {
        Reconciliation_XML = _Reconciliation_XML;
    }
    public String getReconciliation_XML() {
        return Reconciliation_XML;
    }
}
