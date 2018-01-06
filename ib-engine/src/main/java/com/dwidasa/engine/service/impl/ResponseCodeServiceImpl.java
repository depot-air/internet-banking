package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.ResponseCodeDao;
import com.dwidasa.engine.model.ResponseCode;
import com.dwidasa.engine.service.ResponseCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:59 PM
 */
@Service("responseCodeService")
public class ResponseCodeServiceImpl extends GenericServiceImpl<ResponseCode, Long> implements ResponseCodeService {
    @Autowired
    public ResponseCodeServiceImpl(ResponseCodeDao responseCodeDao) {
        super(responseCodeDao);
    }
}
