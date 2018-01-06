package com.dwidasa.engine.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class MoneyUtils {

	static public String getMoney(BigDecimal amount) {
		NumberFormat defaultFormat = NumberFormat.getInstance(Locale.GERMAN);	
		String formatted = defaultFormat.format(amount.doubleValue());
		return "Rp. " + formatted;
	}

}
