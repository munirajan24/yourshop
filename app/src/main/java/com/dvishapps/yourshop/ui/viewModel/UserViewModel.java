package com.dvishapps.yourshop.ui.viewModel;

import android.app.Activity;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.ApiResponse;
import com.dvishapps.yourshop.api.services.UserService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.dvishapps.yourshop.ui.viewModel.common.SViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.dvishapps.yourshop.utils.Tools;

import java.util.List;

public class UserViewModel extends SViewModel {
    public MutableLiveData<String> error = new MutableLiveData<>();
    public MutableLiveData<User> success = new MutableLiveData<>();
    public String username;
    public String password;
    public String phone;
    public String email;
    public String forgot_email;
    public String code;
    public String pwd;
    public String confirmPwd;
    public boolean privacy_policy = false;

    boolean signInClickable = true;
    boolean signUpClickable = true;
    boolean verifyClickable = true;
    boolean resendClickable = true;

    public ProgressDialog progressDialog;

    public UserViewModel() {
    }

    public UserViewModel(Activity activity) {
        this.progressDialog = new ProgressDialog(activity);
    }

    public void signIn() {
        if (phone == null || phone.trim().matches("")) {
//            Console.toast(Config.context.getString(R.string.error_message__blank_email));
            error.setValue("Phone number required.");
            return;
//        } else if (email == null || email.trim().matches("")) {
//            error.setValue(Config.context.getString(R.string.error_message__blank_email_required));
//            return;
        } else if (password == null || password.trim().matches("")) {
//            Console.toast(Config.context.getString(R.string.error_message__blank_password));
            error.setValue(Config.context.getString(R.string.error_message__blank_password_required));
            return;
        }/* else if (!privacy_policy) {
            Console.toast(Config.context.getString(R.string.error_message__to_check_agreement));
            error.setValue(Config.context.getString(R.string.error_message__to_check_agreement));
            return;
        }*/

        if (signInClickable) {
            signInClickable = false;
            progressDialog.startLoading();
            UserService.signIn(phone, password).observeForever(s -> {
//            progressDialog.dismiss();
                if (s != null) {
                    signInClickable = true;
                    progressDialog.dismiss();

                    if (!s.contains("404 error")) {
                        try {
                            User user = JsonUtils.fromJsonString(s, User.class);
                            success.setValue(user);


                        } catch (Exception e) {
                            error.setValue(e.getMessage());
                        }
                    } else {
                        progressDialog.dismiss();
                        String err = "error";
                        try {
                            if (s.split("404 error").length > 1)
                                err = s.split("404 error")[1];
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        error.setValue(err);
                    }
                }
            });
        }
    }

    public void signUp() {
        if (username == null || username.trim().matches("")) {
            Console.toast(Config.context.getString(R.string.error_message__blank_name));
            error.setValue(Config.context.getString(R.string.error_message__blank_name));
            return;
        } else if (phone == null || phone.trim().matches("")) {
//            Console.toast(Config.context.getString(R.string.error_message__blank_email));
            error.setValue("Phone number required.");
            return;
//        } else if (email == null || email.trim().matches("")) {
//            Console.toast(Config.context.getString(R.string.error_message__blank_email));
//            error.setValue(Config.context.getString(R.string.error_message__blank_email_required));
//            return;
        } else if (password == null || password.trim().matches("")) {
            Console.toast(Config.context.getString(R.string.error_message__blank_password));
            error.setValue(Config.context.getString(R.string.error_message__blank_password_required));
            return;
        } else if (!privacy_policy) {
            Console.toast(Config.context.getString(R.string.error_message__to_check_agreement));
            error.setValue(Config.context.getString(R.string.error_message__to_check_agreement));
            return;
        }

        if (signUpClickable) {
            signUpClickable = false;
            progressDialog.startLoading();
            UserService.signUp(email, username, password, phone).observeForever(response_string -> {
                if (response_string != null) {
                    signUpClickable = true;
                    progressDialog.dismiss();
                    if (!response_string.contains("404 error")) {
                        try {
                            User user = JsonUtils.fromJsonString(response_string, User.class);
                            Config.editPreferences.putString(Constants.USER_NEED_VERIFY, response_string);
                            Config.editPreferences.commit();
                            success.setValue(user);
                        } catch (Exception e) {
                            error.setValue(response_string);
                        }
                    } else {
                        progressDialog.dismiss();
                        String err = "error";
                        try {
                            if (response_string.split("404 error").length > 1)
                                err = response_string.split("404 error")[1];
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        error.setValue(err);
                    }
                }
            });
        }
    }

    public LiveData<Boolean> checkForVerifyUser() {
        return UserService.checkForVerifyUser();
    }

    public void verify(String user_id) {
        if (verifyClickable) {
            verifyClickable = false;
            progressDialog.startLoading();
            UserService.verify(user_id, code).observeForever(response_string -> {
                if (response_string != null) {
                    verifyClickable = true;
                    progressDialog.dismiss();
                    try {
                        User user = JsonUtils.fromJsonString(response_string, User.class);

                        Config.editPreferences.putString(Constants.LOGGED_USER, JsonUtils.toJsonString(user)).commit();
                        Config.currentUser = user;
                        Console.logError("UserViewModel verify user " + user);
                        success.setValue(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        String err = "error :" + e.getMessage();
                        try {
                            if (response_string.split("404 error").length > 1)
                                err = response_string.split("404 error")[1];
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            error.setValue(err);
                        }
                        error.setValue(err);
                    }
                }
            });
        }
    }

    public void resentCode(String user_email) {
        if (resendClickable) {
            resendClickable = false;
            progressDialog.startLoading();
            UserService.resetVerifyCode(user_email).observeForever(apiResponse -> {
                if (apiResponse != null) {
                    resendClickable = true;
                    progressDialog.dismiss();
                    if (apiResponse.getStatus().matches("success")) {
                        Console.toast(Config.context.getString(R.string.success_sms_sent));
                    }else {
                        Console.toast("Something went wrong!");
                    }
                }
            });
        }
    }

    public void forgotPassword() {
        if (Tools.isOnline()) {

            if (forgot_email == null || forgot_email.trim().matches("")) {
                Console.toast(Config.context.getString(R.string.error_message__blank_email));
                error.setValue(Config.context.getString(R.string.error_message__blank_email));
                return;
            }
            progressDialog.startLoading();
            UserService.forgetPassword(forgot_email).observeForever(apiResponse -> {
                progressDialog.dismiss();
                if (apiResponse != null) {
                    if (apiResponse.getStatus() != null) {
                        if (!apiResponse.getStatus().contains("404 error")) {
                            if (apiResponse.getStatus().matches("success")) {
                                Console.toast(Config.context.getString(R.string.success_sms_sent));
                            } else {
                                error.setValue(apiResponse.getMessage());
                            }
                        } else {
                            progressDialog.dismiss();
                            String err = "error";
                            try {

                                if (apiResponse.getStatus().split("404 error").length > 1)
                                    err = apiResponse.getStatus().split("404 error")[1];
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            error.setValue(err);
                        }
                    } else {

                    }
                }
            });
        } else {
            error.setValue("UnknownHostException");
        }
    }

    public LiveData<User> signInFb() {
        return UserService.signInFb();
    }

    public LiveData<User> signInGoogle() {
        return UserService.signInGoogle();
    }

    public LiveData<User> getLoginUser() {
        if (Config.sharedPreferences.getString(Constants.LOGGED_USER, null) == null) {
            return new MutableLiveData<>(null);
        }
        return UserService.loggedInUser();
    }

    public LiveData<List<User>> getUserInfo(String user_id) {
        return UserService.getUser(user_id);
    }

    public LiveData<User> getDeliveryStaffInfo() {
        return UserService.getDeliveryStaffDetails();
    }

    public LiveData<ApiResponse> editProfile(User user) {
        return UserService.editProfile(user);
    }

    public LiveData<Boolean> signOut() {
        return UserService.signOut();
    }

    public LiveData<Boolean> removeShop() {
        return UserService.removeShop();
    }

    public LiveData<ApiResponse> changePassword(String user_id, String pwd, String confirmPwd) {
        if ((pwd == null || pwd.isEmpty()) && (confirmPwd == null || confirmPwd.isEmpty())) {
            return new MutableLiveData<>(new ApiResponse("error", Config.context.getString(R.string.error_message__blank_password)));
        } else {
            if (pwd.matches(confirmPwd)) {
                return UserService.changePwd(user_id, pwd, confirmPwd);
            } else {
                return new MutableLiveData<>(new ApiResponse("error", Config.context.getString(R.string.error_message__password_not_equal)));
            }
        }
    }

    public void uploadUserImage(String user_id, Bitmap image) {
        UserService.uploadUserImage(user_id, image, "");
    }

    public void imageUpload(Bitmap bitmap) {
//        if (shopData != null)
//            return;
//        UserService.imageUpload(bitmap);
//        UserService.postImageWithData(bitmap);

    }
}
