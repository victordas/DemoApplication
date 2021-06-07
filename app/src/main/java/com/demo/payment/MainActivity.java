package com.demo.payment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.demo.payment.helpers.Constant;
import com.demo.payment.helpers.IOHelper;
import com.demo.payment.model.Payment;
import com.demo.payment.model.PaymentItem;
import com.demo.payment.ui.PaymentForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView totalAmountValue, noPayment;
    LinearLayout paymentItems;
    Button addPaymentButton, savePaymentsButton;
    IOHelper ioHelper = new IOHelper();
    Payment payment;
    private  static final int REQUEST_WRITE_STORAGE_REQUEST_CODE = Constant.REQUEST_WRITE_STORAGE_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestAppPermissions();
        initView();
        payment = new ViewModelProvider(this).get(Payment.class);
        Payment fetchedPayment = ioHelper.fetchPaymentInfo(getApplicationContext());
        if (fetchedPayment != null && fetchedPayment.getPaymentItemList().size() > 0) {
            payment.setPaymentItemList(fetchedPayment.getPaymentItemList());
            payment.setHasPaymentItem(true);
            for (PaymentItem paymentItem: payment.getPaymentItemList()) {
                addPaymentItemUI(paymentItem);
            }
        }
        totalAmountValue.setText(String.valueOf(payment.getTotalAmount()));
        if(payment.isHasPaymentItem()) {
            noPayment.setVisibility(View.GONE);
        }
    }



    private void initView() {
        totalAmountValue = findViewById(R.id.totalAmountValue);
        noPayment = findViewById(R.id.noPaymentLabel);
        addPaymentButton = findViewById(R.id.addPaymentButton);
        savePaymentsButton = findViewById(R.id.savePaymentsButton);
        paymentItems = findViewById(R.id.paymentItems);
        addPaymentButton.setOnClickListener(this);
        savePaymentsButton.setOnClickListener(this);
    }

    public void addPayment(PaymentItem paymentItem) {
        payment.addPayment(paymentItem);
        payment.setHasPaymentItem(true);
        totalAmountValue.setText(String.valueOf(payment.getTotalAmount()));
        noPayment.setVisibility(View.GONE);
        if (payment.getPaymentItemList().size() == 3) {
            addPaymentButton.setVisibility(View.INVISIBLE);
        }
        addPaymentItemUI(paymentItem);
    }

    public void addPaymentItemUI(PaymentItem paymentItem) {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View paymentItemView = inflater.inflate(R.layout.row_payment_item, null);
        paymentItemView.setTag(paymentItem.getPaymentType());

        TextView paymentItemLabel = paymentItemView.findViewById(R.id.paymentItemLabel);
        Resources resources = getResources();
        String labelResource = paymentItem.getPaymentType().toInt() > 0 ? resources.getString(R.string.payment_item_others_label) : resources.getString(R.string.payment_item_cash_label);
        paymentItemLabel.setText(String.format(labelResource, paymentItem.getPaymentType().toString(), paymentItem.getPaymentAmount()));
        ImageButton deletePaymentButton = paymentItemView.findViewById(R.id.deletePaymentButton);
        deletePaymentButton.setOnClickListener(view -> {
            int index = payment.getPaymentItemList().indexOf(paymentItem);
            payment.removePayment(index);
            totalAmountValue.setText(String.valueOf(payment.getTotalAmount()));
            paymentItems.removeView(paymentItemView);
            addPaymentButton.setVisibility(View.VISIBLE);
            if (payment.getPaymentItemList().size() == 0) {
                noPayment.setVisibility(View.VISIBLE);
            }
        });
        paymentItems.addView(paymentItemView);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.addPaymentButton) {
            ArrayList<String> usedOptions = new ArrayList<>();
            for(PaymentItem paymentItem: payment.getPaymentItemList()) {
                usedOptions.add(paymentItem.getPaymentType().toString());
            }
            Set<String> usedOptionSet = new HashSet<>(usedOptions);
            Set<String> allOptionSet = new HashSet<>(Arrays.asList(Constant.paymentTypes));
            allOptionSet.removeAll(usedOptionSet);

            String[] availableOptions = allOptionSet.toArray(new String[0]);
            DialogFragment paymentFormFragment = PaymentForm.newInstance(availableOptions);
            paymentFormFragment.show(getSupportFragmentManager(), "paymentForm");
        } else {
            if (payment.getPaymentItemList().size() == 0) {
                Toast.makeText(getApplicationContext(), "Nothing to save!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ioHelper.savePaymentInfo(getApplicationContext(), payment)) {
                Toast.makeText(getApplicationContext(), "Payment details saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Unable to save, please try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}