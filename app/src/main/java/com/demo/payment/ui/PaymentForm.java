package com.demo.payment.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.demo.payment.MainActivity;
import com.demo.payment.R;
import com.demo.payment.helpers.Constant;
import com.demo.payment.helpers.PaymentType;
import com.demo.payment.model.PaymentItem;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PaymentForm extends DialogFragment {



    private Spinner paymentTypeOptionSpinner;

    public static PaymentForm newInstance(String[] availableOptions) {
        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Dialog);
        Bundle args = new Bundle();
        args.putStringArray(Constant.availableOptions, availableOptions);
        paymentForm.setArguments(args);
        return paymentForm;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        View paymentFormView = inflater.inflate(R.layout.payment_form, container, false);

        paymentTypeOptionSpinner = paymentFormView.findViewById(R.id.paymentTypeOptions);
        String[] availableOptions = getArguments().getStringArray(Constant.availableOptions);

        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, availableOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentTypeOptionSpinner.setAdapter(adapter);

        paymentTypeOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PaymentType paymentType = PaymentType.fromStringValue(paymentTypeOptionSpinner.getSelectedItem().toString());
                if(paymentType.toInt() == 0) {
                    paymentFormView.findViewById(R.id.addtitionalInfo).setVisibility(View.GONE);
                } else {
                    paymentFormView.findViewById(R.id.addtitionalInfo).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        paymentFormView.findViewById(R.id.dropDownButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentTypeOptionSpinner.performClick();
            }
        });

        paymentFormView.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        paymentFormView.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputPaymentAmount = paymentFormView.findViewById(R.id.inputPaymentAmount);
                String inputPaymentAmountValue = inputPaymentAmount.getText().toString().trim();
                if (inputPaymentAmountValue.isEmpty() || Long.parseLong(inputPaymentAmountValue) <= 0) {
                    inputPaymentAmount.setError("Please enter a valid amount!");
                    return;
                } else {
                    long newInputPaymentAmount = Long.parseLong(inputPaymentAmountValue);
                    PaymentType paymentType = PaymentType.fromStringValue(paymentTypeOptionSpinner.getSelectedItem().toString());
                    PaymentItem newPayment = new PaymentItem();
                    newPayment.setPaymentAmount(newInputPaymentAmount);
                    newPayment.setPaymentType(paymentType);
                    if (paymentType.toInt() > 0) {
                        EditText inputPaymentProvider = paymentFormView.findViewById(R.id.paymentProviderInput);
                        EditText inputTransactionRef = paymentFormView.findViewById(R.id.transactionReferenceInput);
                        newPayment.setPaymentProvider(inputPaymentProvider.getText().toString().trim());
                        newPayment.setPaymentTransactionRef(inputTransactionRef.getText().toString().trim());
                    }
                    ((MainActivity)getActivity()).addPayment(newPayment);
                    dismiss();
                }
            }
        });

        return paymentFormView;
    }
}