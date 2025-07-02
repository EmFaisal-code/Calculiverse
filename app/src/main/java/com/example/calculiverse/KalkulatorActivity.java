package com.example.calculiverse;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.GridLayout;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import java.util.Stack;

public class KalkulatorActivity extends AppCompatActivity {
    TextView txtHasil, txtAngka;
    String container = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulator);

        BottomNavigationView bottomNav = findViewById(R.id.bot_nav);
        bottomNav.setSelectedItemId(R.id.navigation_calculator); // Aktifkan item kalkulator

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.navigation_calculator) {
                // Sudah di sini, tidak perlu berpindah lagi
                return true;
            } else if (id == R.id.navigation_favorite) {
                startActivity(new Intent(this, FavoriteActivity.class));
                return true;
            }
            return false;
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtHasil = findViewById(R.id.txtHasil);
        txtAngka = findViewById(R.id.txtAngka);
    }

    public void nol(View view) {
        container = container + "0";
        txtAngka.setText(container);
    }

    public void satu(View view) {
        container = container + "1";
        txtAngka.setText(container);
    }

    public void dua(View view) {
        container = container + "2";
        txtAngka.setText(container);
    }

    public void tiga(View view) {
        container = container + "3";
        txtAngka.setText(container);
    }

    public void empat(View view) {
        container = container + "4";
        txtAngka.setText(container);
    }

    public void lima(View view) {
        container = container + "5";
        txtAngka.setText(container);
    }

    public void enam(View view) {
        container = container + "6";
        txtAngka.setText(container);
    }

    public void tujuh(View view) {
        container = container + "7";
        txtAngka.setText(container);
    }

    public void delapan(View view) {
        container = container + "8";
        txtAngka.setText(container);
    }

    public void sembilan(View view) {
        container = container + "9";
        txtAngka.setText(container);
    }
    public void allClear(View view){
        txtHasil.setText("0");
        txtAngka.setText("");
        container = "";
    }
    public void plusMinus(View view){
        container = "(-"+container+ ")";
        txtAngka.setText(container);
    }
    public void dot(View view){
        container = container+".";
        txtAngka.setText(container);
    }
    public void delete(View view){
        if(container.length()>0) {
            container = container.substring(0, container.length() - 1);
            txtAngka.setText(container);
        }
    }
    public void add(View view){
        container = container+"+";
        txtAngka.setText(container);
    }
    public void subtrack(View view){
        container = container+"-";
        txtAngka.setText(container);
    }
    public void dividedby(View view){
        container = container+":";
        txtAngka.setText(container);
    }
    public void multiply(View view){
        container = container+"*";
        txtAngka.setText(container);
    }
    public void modulo(View view){
        container = container+"%";
        txtAngka.setText(container);
    }
    public void result(View view){
        txtHasil.setText(eval(container)+"");
        container = "";
    }
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else if (eat('%')) x %= parseFactor(); // modulo
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
} 