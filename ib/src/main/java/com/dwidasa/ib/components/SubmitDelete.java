package com.dwidasa.ib.components;

import org.apache.tapestry5.annotations.Mixin;

import com.dwidasa.ib.mixins.DeleteConfirmation;

public class SubmitDelete extends org.apache.tapestry5.corelib.components.Submit {
	@Mixin
	private DeleteConfirmation deleteConfirmation;
}
