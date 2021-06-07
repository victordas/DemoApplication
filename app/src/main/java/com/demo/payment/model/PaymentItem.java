package com.demo.payment.model;

import com.demo.payment.helpers.PaymentType;

public class PaymentItem {
    private PaymentType paymentType;
    private long paymentAmount;
    private String paymentProvider;
    private String paymentTransactionRef;

    public PaymentItem() {
    }

    public PaymentItem(PaymentType paymentType, long paymentAmount) {
        this.paymentType = paymentType;
        this.paymentAmount = paymentAmount;
    }

    public PaymentItem(PaymentType paymentType, long paymentAmount, String paymentProvider, String paymentTransactionRef) {
        this.paymentType = paymentType;
        this.paymentAmount = paymentAmount;
        this.paymentProvider = paymentProvider;
        this.paymentTransactionRef = paymentTransactionRef;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public long getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public String getPaymentTransactionRef() {
        return paymentTransactionRef;
    }

    public void setPaymentTransactionRef(String paymentTransactionRef) {
        this.paymentTransactionRef = paymentTransactionRef;
    }
}
