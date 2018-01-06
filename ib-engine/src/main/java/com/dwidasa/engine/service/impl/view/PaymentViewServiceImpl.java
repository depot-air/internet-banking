package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.PaymentView;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.PaymentViewService;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/25/11
 * Time: 10:43 AM
 */
@Service("paymentViewService")
public class PaymentViewServiceImpl implements PaymentViewService {
    private MessageCustomizer inquiryMessageCustomizer;
    private MessageCustomizer transactionMessageCustomizer;
    private MessageCustomizer reprintMessageCustomizer;

    public PaymentViewServiceImpl() {
    }

    public MessageCustomizer getInquiryMessageCustomizer() {
        return inquiryMessageCustomizer;
    }

    public void setInquiryMessageCustomizer(MessageCustomizer inquiryMessageCustomizer) {
        this.inquiryMessageCustomizer = inquiryMessageCustomizer;
    }

    public MessageCustomizer getTransactionMessageCustomizer() {
        return transactionMessageCustomizer;
    }

    public void setTransactionMessageCustomizer(MessageCustomizer transactionMessageCustomizer) {
        this.transactionMessageCustomizer = transactionMessageCustomizer;
    }

    public MessageCustomizer getReprintMessageCustomizer() {
        return reprintMessageCustomizer;
    }

    public void setReprintMessageCustomizer(MessageCustomizer reprintMessageCustomizer) {
        this.reprintMessageCustomizer = reprintMessageCustomizer;
    }

    /**
     * {@inheritDoc}
     */
    public void preProcess(BaseView view) {
        PaymentView pv = (PaymentView) view;
        pv.setToAccountType("00");
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validate(BaseView view) {
        Boolean result = view.validate();
        if (result) {
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void composeInquiry(BaseView view, Transaction transaction) {
        inquiryMessageCustomizer.compose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean decomposeInquiry(BaseView view, Transaction transaction) {
        return inquiryMessageCustomizer.decompose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public void composeTransaction(BaseView view, Transaction transaction) {
        transactionMessageCustomizer.compose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean decomposeTransaction(BaseView view, Transaction transaction) {
        return transactionMessageCustomizer.decompose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public void composeReprint(BaseView view, Transaction transaction) {
        reprintMessageCustomizer.compose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean decomposeReprint(BaseView view, Transaction transaction) {
        return reprintMessageCustomizer.decompose(view, transaction);
    }
}
