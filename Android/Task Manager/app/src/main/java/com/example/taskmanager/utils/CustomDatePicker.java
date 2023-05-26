package com.example.taskmanager.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class CustomDatePicker extends DatePickerDialog {
    private final Calendar currentDate;
    private final OnDateSetListener callback;

    public CustomDatePicker(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, null, year, monthOfYear, dayOfMonth);

        currentDate = Calendar.getInstance();
        this.callback = callBack;
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.ok), (OnClickListener) null);
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.cancel), (OnClickListener) null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getButton(BUTTON_POSITIVE).setOnClickListener(view -> {
            DatePicker datePicker = getDatePicker();
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int dayOfMonth = datePicker.getDayOfMonth();

            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            if (selectedDate.before(currentDate)) {
                // Fecha seleccionada es anterior a la fecha actual
                Toast.makeText(getContext(), "No puedes seleccionar una fecha pasada", Toast.LENGTH_LONG).show();
            } else {
                // Fecha seleccionada es válida
                if (callback != null) {
                    callback.onDateSet(datePicker, year, month, dayOfMonth);
                }
                dismiss();
            }
        });
    }
}
