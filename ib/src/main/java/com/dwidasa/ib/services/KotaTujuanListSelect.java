package com.dwidasa.ib.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.util.AbstractSelectModel;

import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.view.TiketKeretaDjatiPurchaseView;

/**
 * Created with IntelliJ IDEA.
 * User: Acer
 * Date: 5/05/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class KotaTujuanListSelect extends AbstractSelectModel{

	private List<TiketKeretaDjatiPurchaseView> bilList; 
	
	public KotaTujuanListSelect(List<TiketKeretaDjatiPurchaseView> transferLists) {
        this.bilList= transferLists;
    }
	
	public List<OptionGroupModel> getOptionGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OptionModel> getOptions() {
		List<OptionModel> options = new ArrayList<OptionModel>();
      
        for (TiketKeretaDjatiPurchaseView transferList : bilList) {
        	if(!transferList.getDestinationKotaTujuan().equals("") || !transferList.getDestinationKotaTujuan().equals(null)){
        		 options.add(new OptionModelImpl(transferList.getDestinationKotaTujuan()));
        	}else{
        		options.add(new OptionModelImpl("Kota Tujuan"));
        	}
           
        }
        
        return options;
	}
    
	
	
}
