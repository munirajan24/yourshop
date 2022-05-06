package com.dvishapps.yourshop.ui.layout.auth.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dvishapps.yourshop.ui.layout.home.HomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.FSigninBinding;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.layout.auth.AuthListener;
import com.dvishapps.yourshop.ui.viewModel.ShopViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;

import java.util.Objects;

public class SignInFragment extends SFragment {
    private FSigninBinding binding;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.f_signin, container, false);
        binding.setViewModel(userViewModel);
        shopViewModel.fetchShop();
        if (SessionData.getInstance().getShop()!=null)
        binding.appNameText.setText(SessionData.getInstance().getShop().getName());
        changeShopVisibility();

//        //TODO YOU CAN PUT THESE CODE WHERE EVERE YOU NEED TO GET PERMISSIONS FROM USER
//        try {
//            //Open the specific App Info page:
//            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.setData(Uri.parse("package:" + getContext().getPackageName()));
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            //Open the generic Apps page:
//            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
//            startActivity(intent);
//        }

        if (SessionData.getInstance().isInitialSignUpCheck()){
            SessionData.getInstance().setInitialSignUpCheck(false);
            authListener.onSignUpClick();
        }

        userViewModel.checkForVerifyUser().observe(this.getViewLifecycleOwner(), state -> {
            if (state) {
                User user = JsonUtils.fromJsonString(Config.sharedPreferences.getString(Constants.USER_NEED_VERIFY, null), User.class);
                authListener.onVerify(user);
            }
        });

        shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {
            if (shop != null) {
                this.shop = shop;
                binding.appNameText.setText(shop.getName());
            }
        });

        binding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
//                    userViewModel.signIn();
                    return true;
                }
                return false;
            }
        });

        binding.signUpBtn.setOnClickListener(v -> {
            authListener.onSignUpClick();
        });

        binding.forgotPasswordButton.setOnClickListener(v -> {
            authListener.onForgotPwd();
        });

        binding.changeShop.setOnClickListener(v -> {
            userViewModel.progressDialog.startLoading();
            userViewModel.removeShop().observe(this.getViewLifecycleOwner(), aBoolean -> {
                userViewModel.progressDialog.dismiss();

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
        });

        userViewModel.error.observe(this.getViewLifecycleOwner(), error -> {
            if (error.contains("Phone"))
                binding.email.setError(error);
            else if (error.contains("password"))
                binding.password.setError(error);
            else{
                if (error.contains("UnknownHostException")) {
                    internetSnack(binding.parentLayout);
                }else {
                    Console.toast(error);
                }
            }

        });

        userViewModel.success.observe(this.getViewLifecycleOwner(), user -> {
          saveUserData();
            authListener.onSuccess(user);
        });

//        FirebaseInstanceId.getInstance().getToken();
        return binding.getRoot();
    }
//    shivi.vishwa@gmail.com
//    dvish1234


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            authListener = (AuthListener) context;
        } catch (ClassCastException cast) {

        }
    }

    public void changeShopVisibility() {
        if (Config.sharedPreferences.getString(Constants.INDIVIDUAL_SHOP, null) != null) {
            if (Objects.equals(Config.sharedPreferences.getString(Constants.INDIVIDUAL_SHOP, null), "1")) {
                binding.changeShop.setVisibility(View.GONE);
            } else {
                binding.changeShop.setVisibility(View.VISIBLE);
            }
        } else {
            binding.changeShop.setVisibility(View.VISIBLE);
        }

    }

    public void saveUserData() {
        Config.editPreferences.putString(Constants.EMAIL, binding.email.getText().toString());
        Config.editPreferences.putString(Constants.PASSWORD, binding.password.getText().toString());
        Config.editPreferences.apply();
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

            userViewModel.signIn();


        userViewModel.error.observe(this.getViewLifecycleOwner(), error -> {
            if (error.contains("email"))
                binding.email.setError(error);
            else if (error.contains("password"))
                binding.password.setError(error);
            else{
                if (error.contains("UnknownHostException")) {
                    internetSnack(binding.parentLayout);
                }else {
                    Console.toast(error);
                }
            }

        });

        userViewModel.success.observe(this.getViewLifecycleOwner(), user -> {
            saveUserData();
            authListener.onSuccess(user);
        });
    }

}
