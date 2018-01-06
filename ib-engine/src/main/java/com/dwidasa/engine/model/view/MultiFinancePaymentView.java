package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.DetilMultiFinance;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/10/11
 * Time: 2:36 PM
 */
public class MultiFinancePaymentView extends PaymentView {
    /**
     * Number of passenger, output field.
     */
    private String numKontrak;
    /**
     * Train name, output field.
     */
    private String namaPelanggan;
    /**
     * Contain origin and destination, output field.
     */
    private String deskripsi;
    /**
     * Date of departure
     */
    private int jumlahTagihan;
   
    public List<DetilMultiFinance> financePaymentViews;
    

    public MultiFinancePaymentView() {
        super();
    }

   
    public void setFinancePaymentViews(
			List<DetilMultiFinance> financePaymentViews) {
		this.financePaymentViews = financePaymentViews;
	}
    
    public List<DetilMultiFinance> getFinancePaymentViews() {
		return financePaymentViews;
	}
    
    public String getNumKontrak() {
		return numKontrak;
	}
    
    public void setNumKontrak(String numKontrak) {
		this.numKontrak = numKontrak;
	}
    
    public String getNamaPelanggan() {
		return namaPelanggan;
	}
    
    public void setNamaPelanggan(String namaPelanggan) {
		this.namaPelanggan = namaPelanggan;
	}
    
    public String getDeskripsi() {
		return deskripsi;
	}
    
    public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
    
    public void setJumlahTagihan(int jumlahTagihan) {
		this.jumlahTagihan = jumlahTagihan;
	}
    
    public int getJumlahTagihan() {
		return jumlahTagihan;
	}
    
   
    
}
