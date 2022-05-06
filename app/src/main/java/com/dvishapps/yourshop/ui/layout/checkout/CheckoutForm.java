package com.dvishapps.yourshop.ui.layout.checkout;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.services.CheckoutService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.CheckoutFormBinding;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.ui.adapters.CheckoutStepperAdapter;
import com.dvishapps.yourshop.ui.common.SCompatActivity;
import com.dvishapps.yourshop.ui.interfaces.DataManager;
import com.dvishapps.yourshop.ui.viewModel.CheckoutViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.Tools;
import com.dvishapps.yourshop.utils.ViewUtils;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

public class CheckoutForm extends SCompatActivity implements PaymentResultListener, StepperLayout.StepperListener, DataManager<Transaction>, DialogInterface.OnDismissListener {

  private Transaction transaction = new Transaction();
  private CheckoutViewModel checkoutViewModel;
  private CheckoutStepperAdapter checkoutStepperAdapter;

  private CheckoutFormBinding binding;
  Transaction transactionHeaderUpload;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
    checkoutStepperAdapter = new CheckoutStepperAdapter(getSupportFragmentManager(), this, Config.currentUser);
    binding = DataBindingUtil.setContentView(this, R.layout.checkout_form);
    binding.setLifecycleOwner(this);
    init();
  }

  private void init() {
    binding.stepperLayout.setAdapter(checkoutStepperAdapter);
    binding.stepperLayout.setListener(this);

    binding.checkoutClose.setOnClickListener(v -> {
      this.finish();
    });
  }

  @Nullable
  @Override
  public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
    return super.onCreateView(parent, name, context, attrs);
  }

  @Override
  public void onCompleted(View completeButton) {
    ViewUtils.removeView(binding.stepperLayout);
    transactionHeaderUpload = getData();
    getData().setDetails(CheckoutService.generateBasketProducts());
    getData().setUser_id(Config.currentUser.getUser_id());
    getData().setCurrency_short_form(Config.cart.getProductsAsList().get(0).getCurrency_short_form());
    getData().setCurrency_symbol(Config.cart.getProductsAsList().get(0).getCurrency_symbol());
//        getData().setTrans_status_id(Config.cart.getProductsAsList().get(0).getStatus());
    getData().setTrans_status_id("1");
    getData().setBalance_amount(transactionHeaderUpload.getBalance_amount());
    getData().setContact_name(Config.currentUser.getUser_name());
    getData().setContact_phone(Config.currentUser.getUser_phone());
    getData().setZone_shipping_enable(transactionHeaderUpload.getZone_shipping_enable());
    getData().setAdded_date(transactionHeaderUpload.getAdded_date());
    getData().setShop_id(transactionHeaderUpload.getShop_id());
    getData().setTransaction_location(Config.sharedPreferences.getString(Constants.LAT, "0")
            +" "+Config.sharedPreferences.getString(Constants.LNG, "0"));


    Checkout checkout = new Checkout();
    checkout.setKeyID("rzp_live_zbYyb4X9eNTTub");
    Checkout.preload(getApplicationContext());
//        transactionHeaderUpload.setUser_id("usr79e93f0dd3411cf1dc7291a5d08cef18");
//        checkoutViewModel.postTransaction(transactionHeaderUpload).observe(this, rep -> {
//            if (rep != null) {
//
//                if (rep.getTrans_status_title().contains("404 error")){
//                        if (rep.getTrans_status_title().contains("UnknownHostException")) {
//                            internetSnack(binding.parentLayout,transactionHeaderUpload);
//                    }
//                }
//
//                Console.toast(getString(R.string.transaction_success));
////                ViewUtils.removeView(binding.transPb);
//                binding.transPb.setVisibility(View.GONE);
//                CheckoutResult checkoutResult = CheckoutResult.newInstance(transactionHeaderUpload, CheckoutService.getBasketProducts());
//                getSupportFragmentManager().executePendingTransactions();
//                checkoutResult.show(getSupportFragmentManager(), "result");
//            }
//        });

//        webCallFunctions(transactionHeaderUpload);
//          setUpUserOrders();

    if (getData().getIs_bank().equals("1")) {                               //TODO : Using is_bank as a parameter of razor
      checkout.setImage(R.mipmap.ic_launcher_your_shop);
      String amount = transactionHeaderUpload.getTotal_item_amount();
      Float amt = Float.parseFloat(amount);
      try {
        JSONObject options = new JSONObject();

        options.put("name", transactionHeaderUpload.getBilling_company());
        options.put("currency", "INR");
        options.put("prefill.email", Config.currentUser.getUser_name());
        options.put("prefill.contact", Config.currentUser.getUser_phone());
        options.put("amount", String.valueOf(amt * 100));//pass amount in currency subunits

        checkout.open(this, options);

      } catch (Exception e) {
        Log.e("TAG", "Error in starting Razorpay Checkout", e);
      }
    } else {
      //for test
//            transactionHeaderUpload.setIs_cod("0");
//            transactionHeaderUpload.setIs_bank("1");
//            transactionHeaderUpload.setPayment_method_nonce(Constants.PAYMENT_RAZOR);
//            transactionHeaderUpload.setRazor_payment_id("testrazorid"+System.currentTimeMillis());
//            transactionHeaderUpload.setRazor_payment_status("success");

      transactionHeaderUpload.setPayment_method_nonce("COD");
      transactionHeaderUpload.setIs_cod("1");
      webCallFunctions(transactionHeaderUpload);
    }
  }

  @Override
  public void onError(VerificationError verificationError) {
  }

  @Override
  public void onStepSelected(int newStepPosition) {

  }

  @Override
  public void onReturn() {
    finish();
  }

  @Override
  public void saveData(Transaction transaction) {
    this.transaction = transaction;
  }

  @Override
  public Transaction getData() {
    return transaction;
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    setResult(Constants.RESPONSE_CODE);
    this.finish();
  }

  public void internetSnack(View parentLayout, Transaction transaction) {
    Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry", new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                if (Tools.isOnline()) {

                } else {
                }
              }
            })
            .setBackgroundTint(parentLayout.getResources().getColor(android.R.color.black))
            .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
            .show();
  }

  @Override
  public void onPaymentSuccess(String paymentID) {
//        Toast.makeText(this, paymentID, Toast.LENGTH_SHORT).show();
    getData().setRazor_payment_id(paymentID);
    getData().setRazor_payment_status("success");
    webCallFunctions(transaction);
    Checkout.clearUserData(getApplicationContext());
  }

  @Override
  public void onPaymentError(int i, String s) {
    getData().setRazor_payment_id("");
    getData().setRazor_payment_status("failure :" + s);
    transaction.setTrans_status_id("8");
    webCallFunctions(transaction);
    Checkout.clearUserData(getApplicationContext());
//        this.finish();
  }

  private void webCallFunctions(Transaction transactionHeaderUpload) {
    checkoutViewModel.postTransaction(transactionHeaderUpload).observe(this, rep -> {
      if (rep != null) {
        binding.transPb.setVisibility(View.GONE);
        if (rep.getTrans_status_title().contains("404 error")) {
          if (rep.getTrans_status_title().contains("UnknownHostException")) {
            internetSnack(binding.parentLayout, transactionHeaderUpload);
          }else {
            Console.toast("Something went wrong!");
          }
        } else {


          if (rep.getRazor_payment_status().contains("failure")) {
            Console.toast("Transaction Failure");
            CheckoutResultFailure checkoutResult = CheckoutResultFailure.newInstance(transactionHeaderUpload, CheckoutService.getBasketProducts());
            getSupportFragmentManager().executePendingTransactions();
            checkoutResult.show(getSupportFragmentManager(), "result");

          } else {
            transactionHeaderUpload.setId(rep.getId());
            transactionHeaderUpload.setAdded_date(rep.getAdded_date());
            transactionHeaderUpload.setRazor_payment_status(rep.getRazor_payment_status());
            Console.toast(getString(R.string.transaction_success));
            CheckoutResult checkoutResult = CheckoutResult.newInstance(transactionHeaderUpload, CheckoutService.getBasketProducts());
            getSupportFragmentManager().executePendingTransactions();
            checkoutResult.show(getSupportFragmentManager(), "result");
          }
        }
      }
    });

  }

  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {

  }
}
