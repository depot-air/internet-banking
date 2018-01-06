package com.dwidasa.admin.transform;

import com.dwidasa.admin.view.TransactionStageView;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.TransactionStage;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 14/11/11
 * Time: 21:16
 */
public class TransactionStageTransformer implements Transformer {
    private CacheManager cacheManager;

    public TransactionStageTransformer() {
        cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");
    }

    public List transform(List entities) {
        List<TransactionStageView> result = new ArrayList<TransactionStageView>();

        for (Object e : entities) {
            TransactionStage ts = (TransactionStage) e;
            TransactionStageView tsv = new TransactionStageView();

            tsv.setId(ts.getId());
            tsv.setReferenceNumber(ts.getTransaction().getReferenceNumber());
            tsv.setTransactionDate(ts.getTransaction().getTransactionDate());
            tsv.setFromAccountNumber(ts.getTransaction().getFromAccountNumber());
            if (ts.getStatus().equalsIgnoreCase("DELIVERED")) {
                tsv.setStatus("Belum Ditangani");
            } else if (ts.getStatus().equalsIgnoreCase("HANDLED")) {
                tsv.setStatus("Sedang Ditangani");
            } else if (ts.getStatus().equalsIgnoreCase("SUCCEED")) {
                tsv.setStatus("Sukses");
            } else if (ts.getStatus().equalsIgnoreCase("FAILED")) {
                tsv.setStatus("Gagal");
            }

            tsv.setToAccountNumber(ts.getTransaction().getToAccountNumber());

            tsv.setBankName(cacheManager.getBiller(Constants.TRANSFER_TREASURY_CODE, ts.getTransaction().getToBankCode()).getBillerName());
            tsv.setTransactionAmount(ts.getTransaction().getTransactionAmount());
            result.add(tsv);
        }

        return result;
    }
}
