package com.dvishapps.yourshop.ui.layout.auth.signup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.FSignupBinding;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.layout.auth.AuthListener;
import com.dvishapps.yourshop.ui.viewModel.ShopViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;

public class SignUpFragment extends SFragment {
    private FSignupBinding binding;
    private AuthListener authListener;
    private UserViewModel userViewModel;

    private ShopViewModel shopViewModel;
    private Shop shop;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new UserViewModel(this.getActivity());
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_signup, container, false);

        if (getArguments() != null) {
            User user = (User) getArguments().getSerializable(Constants.USER_NEED_VERIFY);
            if (user != null) {
                userViewModel.email = user.getUser_email();
                userViewModel.username = user.getUser_name();
            }
        }
        binding.setViewModel(userViewModel);

        shopViewModel.fetchShop();
        if (SessionData.getInstance().getShop()!=null) {
            binding.appNameText.setText(SessionData.getInstance().getShop().getName());
        }
        shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {
            if (shop != null) {
                this.shop = shop;
                binding.appNameText.setText(shop.getName());
            }
        });

        binding.loginBack.setOnClickListener(v -> {
            Config.editPreferences.remove(Constants.USER_NEED_VERIFY);
            Config.editPreferences.commit();
            authListener.onSignInClick();
        });

        userViewModel.error.observe(this.getViewLifecycleOwner(), error -> {
            if (error.contains("Phone")) {
                binding.phone.setError(error);
            } else if (error.contains("password")) {
                binding.password.setError(error);
            } else if (error.contains("username")) {
                binding.username.setError(error);
            }else {
                if (error.contains("UnknownHostException")) {
                    internetSnack(binding.parentLayout);
                }else {
                    Console.toast(error);
                }
            }
        });

        userViewModel.success.observe(this.getViewLifecycleOwner(), user -> {
            saveUserData();
            authListener.onVerify(user);
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

    public void saveUserData() {
        Config.editPreferences.putString(Constants.EMAIL, binding.emailEdt.getText().toString());
        Config.editPreferences.putString(Constants.USER_NAME, binding.usernameEdt.getText().toString());
        Config.editPreferences.putString(Constants.PASSWORD, binding.passwordEdt.getText().toString());
        Config.editPreferences.apply();
    }

    public void internetSnack(View parentLayout){
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Tools.isOnline()) {
                            shopViewModel.fetchShop();
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
        shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {
            if (shop != null) {
                this.shop = shop;
                binding.appNameText.setText(shop.getName());
            }
        });

        userViewModel.signUp();

    }

}
