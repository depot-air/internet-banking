package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.model.view.AccountSsppView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
//import com.dwidasa.engine.model.view.AeroFlightView;
//import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.model.view.AutodebetRegistrationView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.CashInDompetkuView;
import com.dwidasa.engine.model.view.CashOutDelimaView;
import com.dwidasa.engine.model.view.CcPaymentView;
import com.dwidasa.engine.model.view.ColumbiaPaymentView;
import com.dwidasa.engine.model.view.CustomerAccountView;
import com.dwidasa.engine.model.view.EstatementRegistrationView;
import com.dwidasa.engine.model.view.HpPaymentView;
import com.dwidasa.engine.model.view.InternetPaymentView;
import com.dwidasa.engine.model.view.KartuKreditBNIPaymentView;
import com.dwidasa.engine.model.view.LoanPaymentView;
import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.engine.model.view.MncLifePurchaseView;
import com.dwidasa.engine.model.view.MobileRegistrationView;
import com.dwidasa.engine.model.view.MultiFinancePaymentView;
import com.dwidasa.engine.model.view.NonTagListPaymentView;
import com.dwidasa.engine.model.view.PlanePaymentView;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.PortfolioView;
import com.dwidasa.engine.model.view.RefundDelimaView;
import com.dwidasa.engine.model.view.SmsRegistrationView;
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.model.view.TiketKeretaDjatiPurchaseView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.model.view.TrainPurchaseView;
//import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.model.view.TvPaymentView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.model.view.WaterPaymentView;
//import com.dwidasa.engine.model.view.CashOutElmoDelimaView;
//import com.dwidasa.engine.model.view.CashToBankDelimaView;

/**
 * This class used for determine which {@link Transformer} implementation
 * used to transform specific transaction model data into <code>Transaction</code>
 *
 * @author prayugo
 */
public final class TransformerFactory {
    /**
     * Determine which {@link Transformer} meet the requested object.
     *
     * @param view specific transaction data model
     * @return real transform for requested object
     */
    public static Transformer getTransformer(BaseView view) {
        if (view instanceof VoucherPurchaseView) {
            return new VoucherPurchaseViewTransformer();
        }
        else if (view instanceof TransferView) {
            return new TransferViewTransformer();
        }
        else if (view instanceof CcPaymentView) {
            return new CcPaymentViewTransformer();
        }
        else if (view instanceof AccountView) {
            return new AccountViewTransformer();
        }
        else if (view instanceof PlnPaymentView) {
            return new PlnPaymentViewTransformer();
        }
        else if (view instanceof TelkomPaymentView) {
            return new TelkomPaymentViewTransformer();
        }
        else if (view instanceof HpPaymentView) {
            return new HpPaymentViewTransformer();
        }
        else if (view instanceof TvPaymentView) {
            return new TvPaymentViewTransformer();
        }
        else if (view instanceof WaterPaymentView) {
            return new WaterPaymentViewTransformer();
        }
        else if (view instanceof LoanPaymentView) {
            return new LoanPaymentViewTransformer();
        }
        else if (view instanceof InternetPaymentView) {
            return new InternetPaymentViewTransformer();
        }
        else if (view instanceof TrainPaymentView) {
            return new TrainPaymentViewTransformer();
        }
        else if (view instanceof PlanePaymentView) {
            return new PlanePaymentViewTransformer();
        }
        else if (view instanceof NonTagListPaymentView) {
            return new NonTagListPaymentViewTransformer();
        }
        else if (view instanceof WaterPaymentView) {
            return new WaterPaymentViewTransformer();
        }
        else if (view instanceof PlnPurchaseView) {
            return new PlnPurchaseViewTransformer();
        }
        else if (view instanceof CashInDelimaView) {
            return new CashInDelimaViewTransformer();
        }
        else if (view instanceof CashOutDelimaView) {
            return new CashOutDelimaViewTransformer();
        }
        else if (view instanceof RefundDelimaView) {
            return new RefundDelimaViewTransformer();
        }
//        else if (view instanceof CashToBankDelimaView) {
//            return new CashToBankDelimaViewTransformer();
//        }
//        else if (view instanceof CashOutElmoDelimaView) {
//            return new CashOutElmoDelimaViewTransformer();
//        }
        else if (view instanceof LotteryView) {
            return new LotteryViewTransformer();
        }
        else if (view instanceof LotteryView) {
            return new LotteryTransactionViewTransformer();
        }
        else if (view instanceof AccountSsppView) {
            return new AccountSsppViewTransformer();
        }
        else if (view instanceof SmsRegistrationView) {
            return new SmsRegistrationViewTransformer();
        }
        else if (view instanceof PortfolioView) {
            return new PortfolioViewTransformer();
        }
        else if (view instanceof MobileRegistrationView) {
            return new MobileRegistrationViewTransformer();
        }
        else if(view instanceof MncLifePurchaseView){
        	return new MncLifePurchaseViewTransformer();
        }
        else if(view instanceof CashInDompetkuView){
        	return new CashInDompetkuViewTransformer();
        }
        else if(view instanceof AeroTicketingView){
        	return new AeroTicketingViewTransformer();
        }
        else if(view instanceof AeroFlightView){
        	return new AeroFlightViewTransformer();
        }
        else if(view instanceof TiketKeretaDjatiPurchaseView){
        	return new TiketKeretaDjatiPurchaseViewTransformer();
        	
        }
        else if(view instanceof MultiFinancePaymentView){
        	return new MultiFinancePaymentViewTransformer();
        }
        else if(view instanceof TrainPurchaseView){
        	return new TrainPurchaseViewTransformer();
        }
        else if(view instanceof CustomerAccountView){
        	return new CustomerAccountViewTransformer();
        }
        
        else if(view instanceof KartuKreditBNIPaymentView){
        	return new KartuKreditBNIPaymentViewTransformer();
        }
        else if(view instanceof AutodebetRegistrationView){
        	return new AutodebetRegistrationViewTransformer();
        }else if(view instanceof ColumbiaPaymentView){
        	return new ColumbiaPaymentViewTransformer();
        }else if(view instanceof EstatementRegistrationView){
        	return new EstatementRegistrationViewTransformer();
        }
        
        throw new RuntimeException("Transformer not available for object : " + view);
    }
    
    public static Transformer getTransformer(String className) {
        Transformer result = null;
        
        if (className.equals(TransferBatch.class.getSimpleName())) {
        	result = new TransferBatchTransformer();
        }
        if (result == null) {
            throw new RuntimeException("Transformer not available for class : " + className);
        }

        return result;
    }
}
