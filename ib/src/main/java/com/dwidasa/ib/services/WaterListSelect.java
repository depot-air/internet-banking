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

/**
 * Created with IntelliJ IDEA.
 * User: Acer
 * Date: 5/05/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class WaterListSelect extends AbstractSelectModel{

	private List<Biller> bilList; 
	
	public WaterListSelect(List<Biller> transferLists) {
        this.bilList = transferLists;
    }
	
	public List<OptionGroupModel> getOptionGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OptionModel> getOptions() {
		List<OptionModel> options = new ArrayList<OptionModel>();
        for (Biller transferList : bilList) {
            options.add(new OptionModelImpl(transferList.getBillerName(), transferList));
        }
        return options;
	}
    
	
	
}
