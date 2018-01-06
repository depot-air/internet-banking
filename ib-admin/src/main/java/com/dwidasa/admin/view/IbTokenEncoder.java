package com.dwidasa.admin.view;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.model.IbToken;
import com.dwidasa.engine.service.IbTokenService;

public class IbTokenEncoder implements ValueEncoder<IbToken> {

    @Inject
    private IbTokenService ibTokenService;

	public IbTokenEncoder(IbTokenService ibTokenService) {
		this.ibTokenService = ibTokenService;
	}

	@Override
	public String toClient(IbToken value) {
		return String.valueOf(value.getId());
	}

	@Override
	public IbToken toValue(String id) {
		IbToken ibToken = ibTokenService.getById(Long.parseLong(id));
		return ibToken;
	}

}
