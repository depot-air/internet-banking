package com.dwidasa.engine.service.impl.view;

import java.text.DecimalFormat;

import org.springframework.stereotype.Service;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.view.TrainPurchaseViewService;

@Service("trainPurchaseViewService")
public class TrainPurchaseViewServiceImpl implements TrainPurchaseViewService {

	@Override
	public void preProcess(BaseView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean validate(BaseView view) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void composeInquiry(BaseView view, Transaction transaction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean decomposeInquiry(BaseView view, Transaction transaction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void composeTransaction(BaseView view, Transaction transaction) {
		TrainPurchaseView pv = (TrainPurchaseView) view;
		DecimalFormat fmt = new DecimalFormat("##0");
		transaction.setFreeData1(pv.getBookingCode() + "#E-CASH#" + fmt.format(pv.getDiscount()) + "#" + (pv.getAdult() + pv.getChild()));
	}

	@Override
	public Boolean decomposeTransaction(BaseView view, Transaction transaction) {
		TrainPurchaseView pv = (TrainPurchaseView) view;
		pv.setResponseCode(transaction.getResponseCode());
		pv.setReferenceNumber(transaction.getReferenceNumber());
		return Boolean.TRUE;
	}

	@Override
	public void composeReprint(BaseView view, Transaction transaction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean decomposeReprint(BaseView view, Transaction transaction) {
		// TODO Auto-generated method stub
		return null;
	}

}
