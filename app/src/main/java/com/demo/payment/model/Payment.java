package com.demo.payment.model;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class Payment extends ViewModel {
    private long totalAmount = 0;
    private List<PaymentItem> paymentItemList = new ArrayList();
    private boolean hasPaymentItem = false;

    public boolean isHasPaymentItem() {
        return hasPaymentItem;
    }

    public void setHasPaymentItem(boolean hasPaymentItem) {
        this.hasPaymentItem = hasPaymentItem;
    }

    public Payment() {
    }

    public Payment(long totalAmount, List<PaymentItem> paymentItemList, boolean hasPaymentItem) {
        this.totalAmount = totalAmount;
        this.paymentItemList = paymentItemList;
        this.hasPaymentItem = hasPaymentItem;
    }

    public long getTotalAmount() {
        long sum = 0;
        for (PaymentItem paymentItem :
                this.paymentItemList) {
            sum = sum + paymentItem.getPaymentAmount();
        }
        return  sum;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<PaymentItem> getPaymentItemList() {
        return paymentItemList;
    }

    public void setPaymentItemList(List<PaymentItem> paymentItemList) {
        this.paymentItemList = paymentItemList;
    }

    public void addPayment(PaymentItem newPaymyentItem) {
        this.paymentItemList.add(newPaymyentItem);
    }

    public void removePayment(int index) {
        this.paymentItemList.remove(index);
    }
}