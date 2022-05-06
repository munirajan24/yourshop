package com.dvishapps.yourshop.ui.layout.auth;

import com.dvishapps.yourshop.models.User;

public interface AuthListener {
    void onSignInClick();

    void onForgotPwd();

    void onSignUpClick();

    void onSuccess(User user);

    void onVerify(User user);

    void onChangeEmail(User user);
}
