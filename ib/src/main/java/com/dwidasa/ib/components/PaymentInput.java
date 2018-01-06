package com.dwidasa.ib.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 21/07/11
 * Time: 11:56
 */
public class PaymentInput {
    @Parameter
    @Property
    private String billerCode;

    @Parameter
    @Property
    private String productCode;

    @Parameter
    @Property
    private String providerCode;

    @Parameter
    @Property
    private String transactionType;

    @Parameter
    @Property
    private String chooseValue;

    @Parameter
    @Property
    private boolean saveBoxValue;

    @Parameter
    @Property
    private String customerReference1;

    @Parameter
    @Property
    private String customerReference2;

    @Parameter
    @Property
    private String accountNumber;

    @Parameter
    @Property
    private String cardNumber;

    @Parameter
    @Property
    private String token;

    @Parameter
    @Property
    private int tokenType;

    @Parameter
    @Property
    private String tokenChallenge;
}
