package com.dwidasa.admin.transform;

import java.util.ArrayList;
import java.util.List;

import com.dwidasa.admin.view.TreasuryStageView;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.TreasuryStage;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 14/11/11
 * Time: 21:16
 */
public class TreasuryStageTransformer implements Transformer {
    private CacheManager cacheManager;

    public TreasuryStageTransformer() {
        cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");
    }

    public List transform(List entities) {
        List<TreasuryStageView> result = new ArrayList<TreasuryStageView>();

        for (Object e : entities) {
            TreasuryStage ts = (TreasuryStage) e;
            TreasuryStageView tsv = new TreasuryStageView();

            tsv.setId(ts.getId());
            tsv.setReferenceNumber(ts.getTransaction().getReferenceNumber());
            tsv.setTransactionDate(ts.getTransaction().getTransactionDate());
            tsv.setFromAccountNumber(ts.getTransaction().getFromAccountNumber());
            if (ts.getStatus().equalsIgnoreCase(Constants.QUEUED_STATUS)) {
                tsv.setStatus("Belum Ditangani");
            } else if (ts.getStatus().equalsIgnoreCase(Constants.HANDLED_STATUS)) {
                tsv.setStatus("Sedang Ditangani");
            } else if (ts.getStatus().equalsIgnoreCase(Constants.SUCCEED_STATUS)) {
                tsv.setStatus("Sukses");
            } else if (ts.getStatus().equalsIgnoreCase(Constants.FAILED_STATUS)) {
                tsv.setStatus("Gagal");
            }

            tsv.setSenderName(ts.getSender().getCustomerUsername());
//            tsv.setToAccountNumber(ts.getTransaction().getToAccountNumber());
            tsv.setToAccountNumber(ts.getCustomerRegister().getCustomerReference());
            tsv.setReceiverName(ts.getCustomerRegister().getData3());
            tsv.setBankBranch(ts.getCustomerRegister().getData4());
            tsv.setBankCity(ts.getCustomerRegister().getData5());
            tsv.setBankName(cacheManager.getBiller(Constants.TRANSFER_TREASURY_CODE, ts.getTransaction().getToBankCode()).getBillerName());
            
            if (ts.getOfficer() != null && ts.getOfficer().getUsername() != null) {
            	tsv.setOfficerName(ts.getOfficer().getUsername());
            }
            tsv.setTransactionAmount(ts.getTransaction().getTransactionAmount());
            result.add(tsv);
        }

        return result;
    }
}
