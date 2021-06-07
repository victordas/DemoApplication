package com.demo.payment.helpers;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public enum PaymentType {
    CASH(Constant.paymentTypes[0], 0),
    BANK_TRANSFER(Constant.paymentTypes[1], 1),
    CREDIT_CARD(Constant.paymentTypes[2], 2);

    private String stringValue;
    private int intValue;

    private PaymentType(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return stringValue;
    }

    public int toInt() {
        return intValue;
    }

    public static PaymentType fromStringValue(String val) {
        for (PaymentType paymentType: PaymentType.values()) {
            if (paymentType.stringValue.equalsIgnoreCase(val)) {
                return paymentType;
            }
        }
        return null;
    }

}
