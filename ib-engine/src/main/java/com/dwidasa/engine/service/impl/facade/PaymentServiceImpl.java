package com.dwidasa.engine.service.impl.facade;

import java.util.List;

import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.service.facade.PaymentService;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/22/11
 * Time: 2:55 PM
 */
@Service("paymentService")
public class PaymentServiceImpl extends BaseTransactionServiceImpl implements PaymentService {
    public PaymentServiceImpl() {
    }
}
