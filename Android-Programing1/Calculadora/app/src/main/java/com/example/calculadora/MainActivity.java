package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private Button btnC, btnMultiplicar, btnBorrar, btnDividir, btnSumar, btnDecimal, btnIgual, btnRestar,
            btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0;

    private double primerValor = Double.NaN;
    private double segundoValor;
    private char operadorActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recogerLayout();
        setClickListeners();
    }

    private void recogerLayout() {
        tvResult = findViewById(R.id.tvResult);
        btnC = findViewById(R.id.btnC);
        btnMultiplicar = findViewById(R.id.btnMultiplicar);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btnDividir = findViewById(R.id.btnDividir);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btnRestar = findViewById(R.id.btnRestar);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btnSumar = findViewById(R.id.btnSumar);
        btnDecimal = findViewById(R.id.btnDecimal);
        btn0 = findViewById(R.id.btn0);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnIgual = findViewById(R.id.btnIgual);
    }

    // Método para asignar escuchadores de eventos a los botones
    private void setClickListeners() {
        View.OnClickListener buttonClickListener = v -> {
            Button button = (Button) v;
            String input = button.getText().toString();
            String currentText = tvResult.getText().toString();

            if (input.equals("C")) {
                tvResult.setText(""); // Se borra el text view y se establecen todas las variables a 0 o nulo
                primerValor = Double.NaN;
                segundoValor = Double.NaN;
                operadorActual = '\0';
            } else if (input.equals("D")) { // Funcion para caracter a caracter
                if (currentText.length() < 1) {
                    tvResult.setText("");
                } else {
                    tvResult.setText(currentText.substring(0, currentText.length() - 1));
                }
            } else if (input.equals("=")) { // Si se pulsa igual
                if (!Double.isNaN(primerValor)) {      //Pattern.quote sirve para que si el operador es un + lo escape.
                    String[] values = currentText.split(Pattern.quote(String.valueOf(operadorActual)));
                    if (values.length > 1) {
                        segundoValor = Double.parseDouble(values[1]);
                        double resultado = calcular(primerValor, segundoValor, operadorActual);
                        tvResult.setText(resultado == Math.round(resultado) ? String.format("%d", (long) resultado) : String.format("%.2f", resultado));
                        primerValor = resultado == Math.round(resultado) ? (long) resultado : resultado;
                        segundoValor = Double.NaN;
                        operadorActual = '\0';
                    }
                }
            } else if (input.equals("+") || input.equals("-") || input.equals("×") || input.equals("÷")) {
                if (!currentText.isEmpty()) {
                    if (!Double.isNaN(primerValor) && operadorActual == '\0') {
                        operadorActual = input.charAt(0);
                        tvResult.setText(primerValor == Math.round(primerValor)
                                ? String.format("%d", (long) primerValor) + " " + input + " "
                                : String.format("%.2f", primerValor) + " " + input + " ");
                    } else if (Double.isNaN(primerValor)) {
                        primerValor = Double.parseDouble(currentText);
                        operadorActual = input.charAt(0);
                        tvResult.setText(currentText + " " + input + " ");
                    }
                }
            } else {
                tvResult.setText(currentText + input);
            }
        };

        btnC.setOnClickListener(buttonClickListener);
        btnMultiplicar.setOnClickListener(buttonClickListener);
        btnBorrar.setOnClickListener(buttonClickListener);
        btnDividir.setOnClickListener(buttonClickListener);
        btnSumar.setOnClickListener(buttonClickListener);
        btnDecimal.setOnClickListener(buttonClickListener);
        btnIgual.setOnClickListener(buttonClickListener);
        btnRestar.setOnClickListener(buttonClickListener);
        btn1.setOnClickListener(buttonClickListener);
        btn2.setOnClickListener(buttonClickListener);
        btn3.setOnClickListener(buttonClickListener);
        btn4.setOnClickListener(buttonClickListener);
        btn5.setOnClickListener(buttonClickListener);
        btn6.setOnClickListener(buttonClickListener);
        btn7.setOnClickListener(buttonClickListener);
        btn8.setOnClickListener(buttonClickListener);
        btn9.setOnClickListener(buttonClickListener);
        btn0.setOnClickListener(buttonClickListener);
    }

    private double calcular(double primerValor, double segundoValor, char operador) {
        switch (operador) {
            case '+':
                return primerValor + segundoValor;
            case '-':
                return primerValor - segundoValor;
            case '×':
                return primerValor * segundoValor;
            case '÷':
                if (segundoValor == 0) {
                    Toast.makeText(this, "No se puede dividir por cero", Toast.LENGTH_SHORT).show();
                    return 0;
                }
                return primerValor / segundoValor;
            default:
                return 0;
        }
    }
}