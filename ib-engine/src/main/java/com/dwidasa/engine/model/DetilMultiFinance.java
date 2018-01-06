package com.dwidasa.engine.model;

import java.math.BigDecimal;
import java.util.Date;

public class DetilMultiFinance {
	
		private int angsuranKe;
	    private Date tglJatuhTempo;
	    private BigDecimal nominalAngsuran;
	    private BigDecimal nominalDenda;
	    private BigDecimal minimalPembayaran;
	    
	    
	    public DetilMultiFinance() {
			// TODO Auto-generated constructor stub
		}
	    
	    public int getAngsuranKe() {
			return angsuranKe;
		}
	    
	    public void setAngsuranKe(int angsuranKe) {
			this.angsuranKe = angsuranKe;
		}
	    
	    public Date getTglJatuhTempo() {
			return tglJatuhTempo;
		}
	    
	    public void setTglJatuhTempo(Date tglJatuhTempo) {
			this.tglJatuhTempo = tglJatuhTempo;
		}
	    
	    public BigDecimal getNominalAngsuran() {
			return nominalAngsuran;
		}
	    
	    public void setNominalAngsuran(BigDecimal nominalAngsuran) {
			this.nominalAngsuran = nominalAngsuran;
		}
	    
	    public BigDecimal getNominalDenda() {
			return nominalDenda;
		}
	    
	    public void setNominalDenda(BigDecimal nominalDenda) {
			this.nominalDenda = nominalDenda;
		}
	    
	    public BigDecimal getMinimalPembayaran() {
			return minimalPembayaran;
		}
	    
	    public void setMinimalPembayaran(BigDecimal minimalPembayaran) {
			this.minimalPembayaran = minimalPembayaran;
		}

}
