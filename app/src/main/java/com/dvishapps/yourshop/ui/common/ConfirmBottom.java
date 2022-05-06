package com.dvishapps.yourshop.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.AlertDialogBinding;

public class ConfirmBottom extends BottomSheetDialog {
    private AlertDialogBinding binding;

    public ConfirmBottom(@NonNull Context context, OnConfirmDialogAction onConfirmDialogAction) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.alert_dialog, null, false);
        binding.alertContent.setPadding(10, 10, 10, 10);
        setContentView(binding.getRoot());

        binding.alertYes.setOnClickListener(v -> {
            onConfirmDialogAction.onConfirm();
        });

        binding.alertNo.setOnClickListener(v -> {
            onConfirmDialogAction.onClose();
        });

        configureBottomSheetBehavior(binding.getRoot());
    }

    public void saveVisible(boolean v) {
        binding.alertYes.setVisibility(v ? View.VISIBLE : View.GONE);
    }

    public void setTitle(String title) {
        this.binding.alertTitle.setText(title);
    }

    public void setDesc(String desc) {
        this.binding.alertDesc.setText(desc);
    }

    public void setCancelButton(String text) {
        this.binding.alertNo.setText(text);
    }


    public void configureBottomSheetBehavior(View view) {

    }

    public interface OnConfirmDialogAction {
        void onConfirm();

        void onClose();
    }

}
