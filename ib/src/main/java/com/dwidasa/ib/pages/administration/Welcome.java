package com.dwidasa.ib.pages.administration;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/8/11
 * Time: 00:00 am
 * To change this template use File | Settings | File Templates.
 */
public class Welcome {

    Object onSelectedFromActive(){
        return ActivateSoftTokenInput.class;
    }

    Object onSelectedFromSendActiveCode(){
        return RequestSoftTokenCode.class;
    }
}
