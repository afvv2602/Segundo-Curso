package com.example.login;

import android.content.Context;
import android.widget.Toast;

public class UtilsController {
    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
