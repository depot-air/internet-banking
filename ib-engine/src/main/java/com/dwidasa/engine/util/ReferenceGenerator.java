package com.dwidasa.engine.util;

import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.service.ServiceLocator;
import org.apache.commons.lang.StringUtils;

/**
 * Reference number generator.
 *
 * @author rk
 */
public class ReferenceGenerator {
    private static final TransactionDao transctionDao;

    static {
        transctionDao = (TransactionDao) ServiceLocator.getService("transactionDao");
    }

    public static String generate() {
        return StringUtils.leftPad(String.valueOf(transctionDao.nextRrn()), 12, "0");
    }

    public static String format(String str) {
        return str.substring(0, 4) + "-" + str.substring(4, 8) + "-" + str.substring(8);
    }
}
