package com.demo.payment.helpers;

import android.content.Context;
import android.util.Log;

import com.demo.payment.model.Payment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOHelper {
    private static final String fileName = "payments.json";

    public boolean savePaymentInfo(Context ctx, Payment payment) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = new BufferedWriter(new FileWriter(new File(ctx.getFilesDir(), fileName)));
            gson.toJson(payment, writer);
            writer.close();
            return true;
        } catch (Exception e) {
            Log.e("IO", "savePaymentInfo: " + e.toString());
            return false;
        }
    }

    public Payment fetchPaymentInfo(Context ctx) {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(new File(ctx.getFilesDir(), fileName)));
            Payment payment = gson.fromJson(reader, Payment.class);
            reader.close();
            return payment;
        } catch (Exception e) {
            Log.e("IO", "savePaymentInfo: " + e.toString());
            return null;
        }
    }
}
