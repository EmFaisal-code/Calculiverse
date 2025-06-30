package com.example.calculiverse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PercepatanActivity extends AppCompatActivity {
    private EditText etKmh, etMih, etMis, etMach, etC;
    private TextView tvPerKmh, tvPerMih, tvPerMis, tvPerMach, tvPerC;
    private boolean isEditing = false;

    // Konstanta konversi
    private static final double MIH_TO_KMH = 1.609344;
    private static final double MIS_TO_KMH = 5793.6384;
    private static final double MACH_TO_KMH = 1234.8;
    private static final double C_TO_KMH = 1.0792528488E9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_percepatan);

        etKmh = findViewById(R.id.et_output_kmh);
        etMih = findViewById(R.id.et_output_mih);
        etMis = findViewById(R.id.et_output_mis);
        etMach = findViewById(R.id.et_output_mach);
        etC = findViewById(R.id.et_output_c);
        tvPerKmh = findViewById(R.id.tv_per_kmh);
        tvPerMih = findViewById(R.id.tv_per_mih);
        tvPerMis = findViewById(R.id.tv_per_mis);
        tvPerMach = findViewById(R.id.tv_per_mach);
        tvPerC = findViewById(R.id.tv_per_c);
        ImageButton clearButton = findViewById(R.id.clear_button);

        // Fungsi update label per
        Runnable updatePerLabels = () -> {
            if (etKmh.isFocused()) {
                tvPerKmh.setText("1 km/h");
                tvPerMih.setText(String.format("1 mi/h = %.5f km/h", MIH_TO_KMH));
                tvPerMis.setText(String.format("1 mi/s = %.2f km/h", MIS_TO_KMH));
                tvPerMach.setText(String.format("1 mach = %.1f km/h", MACH_TO_KMH));
                tvPerC.setText(String.format("1 c = %.1e km/h", C_TO_KMH));
            } else if (etMih.isFocused()) {
                tvPerKmh.setText(String.format("1 km/h = %.5f mi/h", 1/MIH_TO_KMH));
                tvPerMih.setText("1 mi/h");
                tvPerMis.setText(String.format("1 mi/s = %.5f mi/h", MIS_TO_KMH/MIH_TO_KMH));
                tvPerMach.setText(String.format("1 mach = %.5f mi/h", MACH_TO_KMH/MIH_TO_KMH));
                tvPerC.setText(String.format("1 c = %.5e mi/h", C_TO_KMH/MIH_TO_KMH));
            } else if (etMis.isFocused()) {
                tvPerKmh.setText(String.format("1 km/h = %.8f mi/s", 1/MIS_TO_KMH));
                tvPerMih.setText(String.format("1 mi/h = %.8f mi/s", MIH_TO_KMH/MIS_TO_KMH));
                tvPerMis.setText("1 mi/s");
                tvPerMach.setText(String.format("1 mach = %.8f mi/s", MACH_TO_KMH/MIS_TO_KMH));
                tvPerC.setText(String.format("1 c = %.2e mi/s", C_TO_KMH/MIS_TO_KMH));
            } else if (etMach.isFocused()) {
                tvPerKmh.setText(String.format("1 km/h = %.8f mach", 1/MACH_TO_KMH));
                tvPerMih.setText(String.format("1 mi/h = %.8f mach", MIH_TO_KMH/MACH_TO_KMH));
                tvPerMis.setText(String.format("1 mi/s = %.8f mach", MIS_TO_KMH/MACH_TO_KMH));
                tvPerMach.setText("1 mach");
                tvPerC.setText(String.format("1 c = %.2e mach", C_TO_KMH/MACH_TO_KMH));
            } else if (etC.isFocused()) {
                tvPerKmh.setText(String.format("1 km/h = %.10e c", 1/C_TO_KMH));
                tvPerMih.setText(String.format("1 mi/h = %.10e c", MIH_TO_KMH/C_TO_KMH));
                tvPerMis.setText(String.format("1 mi/s = %.10e c", MIS_TO_KMH/C_TO_KMH));
                tvPerMach.setText(String.format("1 mach = %.10e c", MACH_TO_KMH/C_TO_KMH));
                tvPerC.setText("1 c");
            } else {
                tvPerKmh.setText("1 km/h");
                tvPerMih.setText("1 mi/h");
                tvPerMis.setText("1 mi/s");
                tvPerMach.setText("1 mach");
                tvPerC.setText("1 c");
            }
        };

        // TextWatcher untuk masing-masing EditText
        etKmh.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (etKmh.isFocused() && !isEditing) {
                    isEditing = true;
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        try {
                            double kmh = Double.parseDouble(input);
                            etMih.setText(String.format("%.5f", kmh / MIH_TO_KMH));
                            etMis.setText(String.format("%.8f", kmh / MIS_TO_KMH));
                            etMach.setText(String.format("%.8f", kmh / MACH_TO_KMH));
                            etC.setText(String.format("%.10e", kmh / C_TO_KMH));
                        } catch (NumberFormatException e) {
                            etMih.setText(""); etMis.setText(""); etMach.setText(""); etC.setText("");
                        }
                    } else {
                        etMih.setText(""); etMis.setText(""); etMach.setText(""); etC.setText("");
                    }
                    updatePerLabels.run();
                    isEditing = false;
                }
            }
        });
        etMih.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (etMih.isFocused() && !isEditing) {
                    isEditing = true;
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        try {
                            double mih = Double.parseDouble(input);
                            double kmh = mih * MIH_TO_KMH;
                            etKmh.setText(String.format("%.5f", kmh));
                            etMis.setText(String.format("%.8f", kmh / MIS_TO_KMH));
                            etMach.setText(String.format("%.8f", kmh / MACH_TO_KMH));
                            etC.setText(String.format("%.10e", kmh / C_TO_KMH));
                        } catch (NumberFormatException e) {
                            etKmh.setText(""); etMis.setText(""); etMach.setText(""); etC.setText("");
                        }
                    } else {
                        etKmh.setText(""); etMis.setText(""); etMach.setText(""); etC.setText("");
                    }
                    updatePerLabels.run();
                    isEditing = false;
                }
            }
        });
        etMis.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (etMis.isFocused() && !isEditing) {
                    isEditing = true;
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        try {
                            double mis = Double.parseDouble(input);
                            double kmh = mis * MIS_TO_KMH;
                            etKmh.setText(String.format("%.5f", kmh));
                            etMih.setText(String.format("%.5f", kmh / MIH_TO_KMH));
                            etMach.setText(String.format("%.8f", kmh / MACH_TO_KMH));
                            etC.setText(String.format("%.10e", kmh / C_TO_KMH));
                        } catch (NumberFormatException e) {
                            etKmh.setText(""); etMih.setText(""); etMach.setText(""); etC.setText("");
                        }
                    } else {
                        etKmh.setText(""); etMih.setText(""); etMach.setText(""); etC.setText("");
                    }
                    updatePerLabels.run();
                    isEditing = false;
                }
            }
        });
        etMach.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (etMach.isFocused() && !isEditing) {
                    isEditing = true;
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        try {
                            double mach = Double.parseDouble(input);
                            double kmh = mach * MACH_TO_KMH;
                            etKmh.setText(String.format("%.5f", kmh));
                            etMih.setText(String.format("%.5f", kmh / MIH_TO_KMH));
                            etMis.setText(String.format("%.8f", kmh / MIS_TO_KMH));
                            etC.setText(String.format("%.10e", kmh / C_TO_KMH));
                        } catch (NumberFormatException e) {
                            etKmh.setText(""); etMih.setText(""); etMis.setText(""); etC.setText("");
                        }
                    } else {
                        etKmh.setText(""); etMih.setText(""); etMis.setText(""); etC.setText("");
                    }
                    updatePerLabels.run();
                    isEditing = false;
                }
            }
        });
        etC.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (etC.isFocused() && !isEditing) {
                    isEditing = true;
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        try {
                            double c = Double.parseDouble(input);
                            double kmh = c * C_TO_KMH;
                            etKmh.setText(String.format("%.5f", kmh));
                            etMih.setText(String.format("%.5f", kmh / MIH_TO_KMH));
                            etMis.setText(String.format("%.8f", kmh / MIS_TO_KMH));
                            etMach.setText(String.format("%.8f", kmh / MACH_TO_KMH));
                        } catch (NumberFormatException e) {
                            etKmh.setText(""); etMih.setText(""); etMis.setText(""); etMach.setText("");
                        }
                    } else {
                        etKmh.setText(""); etMih.setText(""); etMis.setText(""); etMach.setText("");
                    }
                    updatePerLabels.run();
                    isEditing = false;
                }
            }
        });

        // Focus change listener untuk update label per
        etKmh.setOnFocusChangeListener((v, hasFocus) -> updatePerLabels.run());
        etMih.setOnFocusChangeListener((v, hasFocus) -> updatePerLabels.run());
        etMis.setOnFocusChangeListener((v, hasFocus) -> updatePerLabels.run());
        etMach.setOnFocusChangeListener((v, hasFocus) -> updatePerLabels.run());
        etC.setOnFocusChangeListener((v, hasFocus) -> updatePerLabels.run());

        // Tombol clear
        clearButton.setOnClickListener(v -> {
            etKmh.setText("");
            etMih.setText("");
            etMis.setText("");
            etMach.setText("");
            etC.setText("");
            tvPerKmh.setText("1 km/h");
            tvPerMih.setText("1 mi/h");
            tvPerMis.setText("1 mi/s");
            tvPerMach.setText("1 mach");
            tvPerC.setText("1 c");
        });
    }
}