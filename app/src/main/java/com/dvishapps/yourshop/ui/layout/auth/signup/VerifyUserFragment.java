package com.dvishapps.yourshop.ui.layout.auth.signup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.FVerifyUserBinding;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.layout.auth.AuthListener;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.Tools;

public class VerifyUserFragment extends SFragment {

    private FVerifyUserBinding binding;
    private UserViewModel userViewModel;
    private AuthListener authListener;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new UserViewModel(this.getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_verify_user, container, false);

        if (getArguments() != null)
            user = (User) getArguments().getSerializable(Constants.USER_NEED_VERIFY);
        binding.setUser(user);
        binding.setViewModel(userViewModel);

        userViewModel.error.observe(this.getViewLifecycleOwner(), s -> {
            if (s != null) {
                if (s.contains("UnknownHostException")) {
                    internetSnack(binding.parentLayout);
                }else {
                    binding.code.setError(s);
                }
            }
        });

        userViewModel.success.observe(this.getViewLifecycleOwner(), user -> {
            Config.editPreferences.remove(Constants.USER_NEED_VERIFY).commit();
            Console.logError(" VerifyUserFragment success.observe "+user);
            authListener.onSuccess(user);
        });

        binding.changeEmail.setOnClickListener(v -> {
            authListener.onChangeEmail(user);
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            authListener = (AuthListener) context;
        } catch (ClassCastException cast) {

        }
    }

    public void internetSnack(View parentLayout){
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Tools.isOnline()) {
                            retryFunctions();
                        }else {
                            internetSnack(binding.parentLayout);
                        }
                    }
                })
                .setBackgroundTint(parentLayout.getResources().getColor(android.R.color.black ))
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }

    private void retryFunctions() {
        userViewModel.verify(binding.getUser().getUser_id());
    }

}
