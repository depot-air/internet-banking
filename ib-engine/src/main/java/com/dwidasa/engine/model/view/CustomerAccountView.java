package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:39
 */
public class CustomerAccountView extends PaymentView {

    private String digitRekening;
    private String digitNoKartu;
    private List<CustomerAccountView> accountViews = new ArrayList<CustomerAccountView>();
    private String dataAccount;
    
    
    public void setDataAccount(String dataAccount) {
		this.dataAccount = dataAccount;
	}
    
    public String getDataAccount() {
		return dataAccount;
	}

    public void setDigitRekening(String digitRekening) {
		this.digitRekening = digitRekening;
	}
    
    public String getDigitRekening() {
		return digitRekening;
	}
    
    public void setDigitNoKartu(String digitNoKartu) {
		this.digitNoKartu = digitNoKartu;
	}
    
    public String getDigitNoKartu() {
		return digitNoKartu;
	}
    
    public void setAccountViews(List<CustomerAccountView> accountViews) {
		this.accountViews = accountViews;
	}
    public List<CustomerAccountView> getAccountViews() {
		return accountViews;
	}
  

}
