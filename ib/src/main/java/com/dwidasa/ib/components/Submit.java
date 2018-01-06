package com.dwidasa.ib.components;

import org.apache.tapestry5.annotations.Mixin;

import com.dwidasa.ib.mixins.DisableAfterSubmit;

public class Submit extends org.apache.tapestry5.corelib.components.Submit {
	@Mixin
	private DisableAfterSubmit disableAfterSubmit;
	
}

