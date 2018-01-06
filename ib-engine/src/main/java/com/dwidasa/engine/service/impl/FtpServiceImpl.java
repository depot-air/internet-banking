package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.FtpDao;
import com.dwidasa.engine.model.Ftp;
import com.dwidasa.engine.service.FtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/14/11
 * Time: 11:20 AM
 */
@Service("ftpService")
public class FtpServiceImpl extends GenericServiceImpl<Ftp, Long> implements FtpService {
    @Autowired
    public FtpServiceImpl(FtpDao ftpDao) {
        super(ftpDao);
    }
}
