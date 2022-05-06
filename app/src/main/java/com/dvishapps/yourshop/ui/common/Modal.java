package com.dvishapps.yourshop.ui.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.AlertDialogBinding;

public class Modal extends AppCompatDialogFragment {
    private AlertDialog.Builder alert;
    private AlertDialog dialog;

    private String title = "alert title";
    private String content = "alert content";
    private OnDialogAction onDialogAction;

    private ModalType type;
    private AlertDialogBinding binding;

    public enum ModalType {
        CONFIRM,
        INFO
    }

    public Modal() {
    }

    public Modal(ModalType type) {
        this.type = type;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        alert = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater, R.layout.alert_dialog, null,false);

        binding.alertTitle.setText(this.title);
        binding.alertDesc.setText(this.content);

        if (type != null && type == ModalType.CONFIRM) {
            binding.alertYes.setVisibility(View.VISIBLE);

            binding.alertNo.setOnClickListener(v -> {
                dialog.dismiss();
                onDialogAction.onClose();
            });
            binding.alertYes.setOnClickListener(v -> {
                dialog.dismiss();
                onDialogAction.onConfirm();
            });

        } else {
            binding.alertNo.setOnClickListener(v -> {
                dialog.dismiss();
                onDialogAction.onClose();
            });
        }
        alert.setView(binding.getRoot());
        dialog = alert.create();
        return dialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            this.onDialogAction = (OnDialogAction) context;
        } catch (ClassCastException e) {

        }
    }

    public interface OnDialogAction {
        void onConfirm();

        void onClose();
    }
}
