package com.dwidasa.engine.model.view;

import com.dwidasa.engine.model.CustomerRegister;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: IB
 * Date: 1/31/12
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class LotteryTransactionView extends LotteryView implements BaseView {
	
    //Baru
    private String kodeUndian;
    private String jenisUndian;
    private BigDecimal posisiAwal;
    private BigDecimal totalData;
    private Date awalPeriod;
    private Date akhirPeriod;
    private String nextAvailableFlag;
    private String namaUndian;
    private String nomorUndian;
    private String toAccountType;
   
    //private List<LotteryTransactionView> lotteryViews = new ArrayList<LotteryTransactionView>();
    /**
     * Bit #48 of ISO message. Internal field.
     */
    protected String bit48;

    public LotteryTransactionView() {
    }
    
    
    public String getKodeUndian() {
		return kodeUndian;
	}
    
    public BigDecimal getPosisiAwal() {
		return posisiAwal;
	}
    
    public BigDecimal getTotalData() {
		return totalData;
	}
    
    public Date getAwalPeriod() {
		return awalPeriod;
	}
    
    
    public Date getAkhirPeriod() {
		return akhirPeriod;
	}
    
    public String getNextAvailableFlag() {
		return nextAvailableFlag;
	}
    
    
    public void setKodeUndian(String kodeUndian) {
		this.kodeUndian = kodeUndian;
	}
    
    public void setPosisiAwal(BigDecimal posisiAwal) {
		this.posisiAwal = posisiAwal;
	}
    
    public void setTotalData(BigDecimal totalData) {
		this.totalData = totalData;
	}
    
    public void setAwalPeriod(Date awalPeriod) {
		this.awalPeriod = awalPeriod;
	}
    
    public void setAkhirPeriod(Date akhirPeriod) {
		this.akhirPeriod = akhirPeriod;
	}
    
    public void setNextAvailableFlag(String nextAvailableFlag) {
		this.nextAvailableFlag = nextAvailableFlag;
	}
    

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

   

    public String getBit48() {
        return bit48;
    }

    public void setBit48(String bit48) {
        this.bit48 = bit48;
    }

    
    public void setJenisUndian(String jenisUndian) {
		this.jenisUndian = jenisUndian;
	}
    
    public String getJenisUndian() {
		return jenisUndian;
	}
    
    public void setNamaUndian(String namaUndian) {
		this.namaUndian = namaUndian;
	}
    
    public String getNamaUndian() {
		return namaUndian;
	}
    
    @Override
    public BigDecimal getAmount() {
        return null;
    }

    
    @Override
    public Boolean validate() {
        return Boolean.TRUE;
    }

    @Override
    public CustomerRegister transform() {
        return null;
    }

    
//    public void setLotteryTransactionViews(List<LotteryTransactionView> lotteryViews) {
//		this.lotteryViews = lotteryViews;
//	}
//    
//    public List<LotteryTransactionView> getLotteryTransactionViews() {
//		return lotteryViews;
//	}
//    
    public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}
    
    public String getToAccountType() {
		return toAccountType;
	}
    
    public void setNomorUndian(String nomorUndian) {
		this.nomorUndian = nomorUndian;
	}
    
    public String getNomorUndian() {
		return nomorUndian;
	}
    
   
   
}
