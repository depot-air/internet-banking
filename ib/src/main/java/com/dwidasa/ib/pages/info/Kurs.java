package com.dwidasa.ib.pages.info;

/**
 * Created by IntelliJ IDEA.
 * User: Frida
 * Date: 9/15/11
 * Time: 9:30 AM
 */

import com.dwidasa.engine.model.ExchangeRate;
import com.dwidasa.engine.service.ExchangeRateService;
import com.dwidasa.ib.common.EvenOdd;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Kurs {

    @Property
    private List<ExchangeRate> allExchangeRates;

    @Property
    private ExchangeRate exchangeRate;

    @Property
    private String nowDate;

    @Inject
    private ExchangeRateService exchangeRateService;

     @Property
	private EvenOdd evenOdd;


    public void pageAttached() {
        evenOdd = new EvenOdd();
    }

    public void setupRender(){
         allExchangeRates = exchangeRateService.getAllWithCurrency();
         setExchangeRateDate();
    }

     private void setExchangeRateDate() {
        Date date = new Date();
        SimpleDateFormat sdate = new SimpleDateFormat("dd MM yyyy / hh:mm:ss");
        nowDate = sdate.format(date);
        nowDate = nowDate + " WIB";
    }

}
