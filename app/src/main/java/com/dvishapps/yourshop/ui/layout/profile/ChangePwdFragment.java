package com.dvishapps.yourshop.ui.layout.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.FChangePwdBinding;
import com.dvishapps.yourshop.ui.common.ConfirmBottom;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.Tools;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePwdFragment extends SFragment implements ConfirmBottom.OnConfirmDialogAction {

    private FChangePwdBinding binding;
    private UserViewModel userViewModel;
    private ConfirmBottom dialog;
    private String user_id;
    private ConfirmBottom ok;

    boolean saveClickable = true;

    ProgressDialog progressDialog;

    public ChangePwdFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        if (getArguments() != null) {
            user_id = getArguments().getString(Constants.USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_change_pwd, container, false);
        binding.setUserModel(userViewModel);

        progressDialog = new ProgressDialog(getActivity(), "Loading");
        binding.savePwd.setOnClickListener(v -> {
            if (userViewModel.pwd.equals(userViewModel.confirmPwd)) {
                dialog = new ConfirmBottom(this.getContext(), this);
                dialog.setTitle(getString(R.string.confirm));
                dialog.setDesc(getString(R.string.password_change__confirm_password));
                dialog.show();
            } else {
                Console.toast("Password does not match!");
                binding.confirmPwdLayout.setError("Password does not match!");
            }
        });
        return binding.getRoot();
    }


    @Override
    public void onConfirm() {
        dialog.dismiss();
        if (Tools.isOnline()) {
            if (saveClickable) {
                saveClickable = false;
                progressDialog.startLoading();
                userViewModel.changePassword(user_id, userViewModel.pwd, userViewModel.confirmPwd)
                        .observe(this.getViewLifecycleOwner(), apiResponse -> {
                            if (apiResponse != null) {
                                saveClickable = true;
                                progressDialog.dismiss();

                                if (apiResponse.getStatus().matches("success")) {
                                    ok = new ConfirmBottom(getContext(), new ConfirmBottom.OnConfirmDialogAction() {
                                        @Override
                                        public void onConfirm() {
                                            ok.dismiss();
                                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_profile);
                                        }

                                        @Override
                                        public void onClose() {
                                            ok.dismiss();
                                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_profile);
                                        }
                                    });
                                    ok.saveVisible(false);
                                    ok.setDesc(apiResponse.getMessage());
                                    ok.setCancelButton("OK");
                                    ok.setTitle(getString(R.string.success));
                                    ok.show();

                                } else {
                                    Console.toast(apiResponse.getMessage());
                                }
                            }
                        });
            }
        } else {
            internetSnack(binding.parentLayout);
        }
    }

    @Override
    public void onClose() {
        dialog.dismiss();
    }


    public void internetSnack(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
                            retryFunctions();
                        } else {
                            internetSnack(binding.parentLayout);
                        }
                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public void retryFunctions() {
        if (Tools.isOnline()) {
            if (saveClickable) {
                saveClickable = false;
                progressDialog.startLoading();
                userViewModel.changePassword(user_id, userViewModel.pwd, userViewModel.confirmPwd)
                        .observe(this.getViewLifecycleOwner(), apiResponse -> {
                            if (apiResponse != null) {
                                saveClickable = true;
                                progressDialog.dismiss();

                                if (apiResponse.getStatus().matches("success")) {
                                    ok = new ConfirmBottom(getContext(), new ConfirmBottom.OnConfirmDialogAction() {
                                        @Override
                                        public void onConfirm() {
                                            ok.dismiss();
                                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_profile);
                                        }

                                        @Override
                                        public void onClose() {
                                            ok.dismiss();
                                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_profile);
                                        }
                                    });
                                    ok.saveVisible(false);
                                    ok.setDesc(apiResponse.getMessage());
                                    ok.setCancelButton("OK");
                                    ok.setTitle(getString(R.string.success));
                                    ok.show();

                                } else {
                                    Console.toast(apiResponse.getMessage());
                                }
                            }
                        });
            }
        } else {
            internetSnack(binding.parentLayout);
        }
    }
}
