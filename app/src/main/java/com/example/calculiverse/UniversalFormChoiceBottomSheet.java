package com.example.calculiverse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.List;

public class UniversalFormChoiceBottomSheet {

    public interface OnOptionSelectedListener {
        void onOptionSelected(int index, String option);
    }

    public static void show(Context context, List<String> options, OnOptionSelectedListener listener) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.bottomsheet_form_choice, null);
        LinearLayout layoutOptions = sheetView.findViewById(R.id.layout_options);

        for (int i = 0; i < options.size(); i++) {
            String optionText = options.get(i);
            TextView option = new TextView(context);
            option.setText(optionText);
            option.setTextSize(18);
            option.setPadding(24, 24, 24, 24);
            int finalI = i;
            option.setOnClickListener(v -> {
                listener.onOptionSelected(finalI, optionText);
                bottomSheetDialog.dismiss();
            });
            layoutOptions.addView(option);
        }

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
} 