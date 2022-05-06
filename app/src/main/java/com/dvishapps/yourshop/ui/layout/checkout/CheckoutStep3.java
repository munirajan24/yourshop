package com.dvishapps.yourshop.ui.layout.checkout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dvishapps.yourshop.BuildConfig;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.CheckoutForm3Binding;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.dvishapps.yourshop.ui.interfaces.DataManager;
import com.dvishapps.yourshop.ui.viewModel.ShopViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.dvishapps.yourshop.utils.ViewUtils;
import com.google.gson.JsonObject;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class CheckoutStep3 extends Fragment implements BlockingStep {
    private DataManager<Transaction> dataManager;
    private CardView oldCardView = null;

    private CheckoutForm3Binding binding;
    private String PM = "";

    private ShopViewModel shopViewModel;
    private Shop shop;

    boolean submitClickable = true;

    public ProgressDialog progressDialog;

    @NotNull
    public static CheckoutStep3 newInstance() {
        Bundle args = new Bundle();
        CheckoutStep3 fragment = new CheckoutStep3();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.checkout_form_3, container, false);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        if (SessionData.getInstance().getShop().getBanktransfer_enabled().equals("0")) {
            binding.razorCardView.setVisibility(View.GONE);
        } else {
            binding.razorCardView.setVisibility(View.VISIBLE);
        }
        if (SessionData.getInstance().getShop().getCod_enabled().equals("0")) {
            binding.cashCardView.setVisibility(View.GONE);
        } else {
            binding.cashCardView.setVisibility(View.VISIBLE);
        }


        if (SessionData.getInstance().getShop().getBanktransfer_enabled().length() == 0) {
            binding.razorCardView.setVisibility(View.VISIBLE);
        }
        if (SessionData.getInstance().getShop().getCod_enabled().length() == 0) {
            binding.cashCardView.setVisibility(View.VISIBLE);
        }
        binding.cashCardView.setVisibility(View.VISIBLE);//for test
//        binding.razorCardView.setVisibility(View.VISIBLE);//for test

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cashCardEnable();

        binding.stripeCardView.setOnClickListener(v1 -> {
            ViewUtils.changeViewColor(binding.stripeCardView, ContextCompat.getColor(this.getContext(), R.color.md_orange_600), oldCardView,
                    ContextCompat.getColor(this.getContext(), R.color.ms_white));
            oldCardView = binding.stripeCardView;
            PM = Constants.PAYMENT_STRIPE;
        });

        binding.cashCardView.setOnClickListener(v1 -> {
            PM = Constants.PAYMENT_CASH_ON_DELIVERY;
            cashCardEnable();
        });

        binding.razorCardView.setOnClickListener(v1 -> {
            PM = Constants.PAYMENT_RAZOR;
            cashCardEnable();
        });

        binding.paypalCardView.setOnClickListener(v1 -> {
            ViewUtils.changeViewColor(binding.paypalCardView, ContextCompat.getColor(this.getContext(), R.color.md_orange_600), oldCardView,
                    ContextCompat.getColor(this.getContext(), R.color.ms_white));
            oldCardView = binding.paypalCardView;
            PM = Constants.PAYMENT_PAYPAL;
        });

        binding.bankCardView.setOnClickListener(v1 -> {
            ViewUtils.changeViewColor(binding.bankCardView, ContextCompat.getColor(this.getContext(), R.color.md_orange_600), oldCardView,
                    ContextCompat.getColor(this.getContext(), R.color.ms_white));
            oldCardView = binding.bankCardView;
            PM = Constants.PAYMENT_BANK;
        });

    }

    private boolean checkForm3() {
        if (PM == null || PM.matches("") || oldCardView == null) {
            Console.logError(Constants.PAYMENT_METHOD_ERROR);
            Console.toast(Constants.PAYMENT_METHOD_ERROR);
            return false;
        }
//        if (!binding.checkBox.isChecked()) {
//            Console.log(Constants.PAYMENT_CONDITION_ERROR);
//            return false;
//        }
        return true;
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        if (checkForm3()) {
            if (submitClickable) {
                submitClickable = false;
                if (PM.matches(Constants.PAYMENT_PAYPAL)) {
                    dataManager.getData().setIs_paypal("1");
                    dataManager.getData().setIs_bank("0");
                    dataManager.getData().setIs_cod("0");
                    dataManager.getData().setIs_stripe("0");
                    //                dataManager.getData().setIs_rozar("0");
                } else if (PM.matches(Constants.PAYMENT_BANK)) {
                    dataManager.getData().setIs_paypal("0");
                    dataManager.getData().setIs_bank("0");
                    dataManager.getData().setIs_cod("1");
                    dataManager.getData().setIs_stripe("0");
                    //                dataManager.getData().setIs_rozar("0");
                } else if (PM.matches(Constants.PAYMENT_CASH_ON_DELIVERY)) {
                    dataManager.getData().setIs_paypal("0");
                    dataManager.getData().setIs_bank("0");
                    dataManager.getData().setIs_cod("1");
                    dataManager.getData().setIs_stripe("0");
                    //                dataManager.getData().setIs_rozar("0");
                } else if (PM.matches(Constants.PAYMENT_STRIPE)) {
                    dataManager.getData().setIs_paypal("0");
                    dataManager.getData().setIs_bank("0");
                    dataManager.getData().setIs_cod("0");
                    dataManager.getData().setIs_stripe("1");
                    //                dataManager.getData().setIs_rozar("0");
                } else if (PM.matches(Constants.PAYMENT_RAZOR)) {
                    dataManager.getData().setIs_paypal("0");
                    dataManager.getData().setIs_bank("1");//TODO : Using is_bank as a parameter of razor
                    dataManager.getData().setIs_cod("0");
                    dataManager.getData().setIs_stripe("0");
                    //                dataManager.getData().setIs_rozar("1");
                }

                dataManager.getData().setMemo(binding.memoEditText.getText().toString());
                dataManager.getData().setPayment_method_nonce(PM);

                checkShopClosedAndProceed(callback);
            }
        }
    }

    @Override
    public void onBackClicked(@NotNull StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DataManager) {
            dataManager = (DataManager<Transaction>) context;
        } else {
            throw new IllegalStateException("Activity must implement DataManager interface!");
        }
    }


    public void cashCardEnable() {
        ViewUtils.changeViewColor(binding.cashCardView, ContextCompat.getColor(this.getContext(), R.color.ms_white), oldCardView,
                ContextCompat.getColor(this.getContext(), R.color.ms_white));

        binding.cashTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.ms_black));
        binding.cashImageView.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.baseline_money_black));
        ViewUtils.changeViewColor(binding.razorCardView, ContextCompat.getColor(this.getContext(), R.color.ms_white), oldCardView,
                ContextCompat.getColor(this.getContext(), R.color.ms_white));

        binding.razorTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.ms_black));
        binding.razorImageView.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_mobile_payment));

        if (PM.matches(Constants.PAYMENT_CASH_ON_DELIVERY)) {

            ViewUtils.changeViewColor(binding.cashCardView, ContextCompat.getColor(this.getContext(), R.color.orangeBackground), oldCardView,
                    ContextCompat.getColor(this.getContext(), R.color.orangeBackground));

            binding.cashTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.ms_black));
            binding.cashImageView.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.baseline_money_black));

            oldCardView = binding.cashCardView;
            PM = Constants.PAYMENT_CASH_ON_DELIVERY;


        } else if (PM.matches(Constants.PAYMENT_RAZOR)) {
            ViewUtils.changeViewColor(binding.razorCardView, ContextCompat.getColor(this.getContext(), R.color.blueBackground), oldCardView,
                    ContextCompat.getColor(this.getContext(), R.color.ms_white));

            binding.razorTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.md_white_1000));
            try {
                binding.razorImageView.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_mobile_payment));
            } catch (Exception e) {
                binding.razorImageView.setImageDrawable(AppCompatResources.getDrawable(this.getContext(), R.drawable.ic_mobile_payment));
                e.printStackTrace();
            }

            oldCardView = binding.cashCardView;
            PM = Constants.PAYMENT_RAZOR;

        } else {
            ViewUtils.changeViewColor(binding.cashCardView, ContextCompat.getColor(this.getContext(), R.color.ms_white), oldCardView,
                    ContextCompat.getColor(this.getContext(), R.color.ms_white));

            binding.cashTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.ms_black));
            binding.cashImageView.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.baseline_money_black));
            ViewUtils.changeViewColor(binding.razorCardView, ContextCompat.getColor(this.getContext(), R.color.ms_white), oldCardView,
                    ContextCompat.getColor(this.getContext(), R.color.ms_white));

            binding.razorTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.ms_black));
            binding.razorImageView.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_mobile_payment));
            oldCardView = null;
            PM = "";
        }
    }

    public void razorCardEnable() {
        if (PM.matches(Constants.PAYMENT_RAZOR)) {
            ViewUtils.changeViewColor(binding.razorCardView, ContextCompat.getColor(this.getContext(), R.color.blueBackground), oldCardView,
                    ContextCompat.getColor(this.getContext(), R.color.ms_white));

            binding.razorTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.md_white_1000));
            try {
                binding.razorImageView.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_mobile_payment));
            } catch (Exception e) {
                binding.razorImageView.setImageDrawable(AppCompatResources.getDrawable(this.getContext(), R.drawable.ic_mobile_payment));
                e.printStackTrace();
            }

            oldCardView = binding.cashCardView;
            PM = Constants.PAYMENT_RAZOR;

        } else {

        }
    }

    public void checkShopClosedAndProceed(StepperLayout.OnCompleteClickedCallback callback) {

        shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {

            if (shop != null) {
                submitClickable = true;
                boolean proceed = true;     //TODO: This is for only show one dialog in one time
                this.shop = shop;
                if (shop.getShop_app_force_update().equals("1")) {
                    if (shop.getShop_current_version().length() > 0) {
                        if (!shop.getShop_current_version().equals(BuildConfig.VERSION_NAME)) {
                            if (Objects.equals(Config.sharedPreferences.getString(Constants.INDIVIDUAL_SHOP, "0"), "1")) {                            //test condition
                                proceed = false;
                                customAlertForcedUpdate(getActivity(), shop.getShop_current_version());
                            }
                        }
                    }
                }

//                if (proceed) {
//                    if (shop.getIs_enable().equals("0")) {
//                        proceed = false;
//                        customAlertShopClose(getActivity(), shop.getShop_open_time(), shop.getShop_close_time());
//                    }
//                }
//
//                if (proceed) {
//                    if (!Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {
//                        if (shop.getStatus().equals("0")) {
//                            proceed = false;
//                            customAlertShopClose(getActivity(), shop.getShop_open_time(), shop.getShop_close_time());
//                        }
//                    }
//                }
//                proceed=true;//for test

                if (proceed) {
                    getLiveDistance(Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0")),
                            Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0")),

                            Double.parseDouble(SessionData.getInstance().getShopLat()),
                            Double.parseDouble(SessionData.getInstance().getShopLng()),
                            callback

                    );
                }
//
//                if (proceed) {
//                    callback.complete();
//                }
            }
        });

    }


    public void customAlertForcedUpdate(Context mContext, String version) { // nORMAL SCAN
        Dialog dialog;
        dialog = new Dialog(mContext);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_forced_update);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
        TextView ok = (TextView) dialog.findViewById(R.id.dialog_Ok);
        RelativeLayout rl_header = dialog.findViewById(R.id.rl_header);
        LinearLayout lnr_root = dialog.findViewById(R.id.lnr_root);
        View view_ok = (View) dialog.findViewById(R.id.view_ok);

        if (version.length() > 0) {
            txtMessage.setText("Sorry, You need to update the latest version (" + version + ") to proceed.");
        } else {
            txtMessage.setText("Sorry, You need to update the latest version to proceed.");
        }


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
//                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void customAlertShopClose(Context mContext, String openTime, String closeTime) { // nORMAL SCAN
        Dialog dialog;
        dialog = new Dialog(mContext);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_forced_update);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
        TextView ok = (TextView) dialog.findViewById(R.id.dialog_Ok);
        RelativeLayout rl_header = dialog.findViewById(R.id.rl_header);
        LinearLayout lnr_root = dialog.findViewById(R.id.lnr_root);
        View view_ok = (View) dialog.findViewById(R.id.view_ok);

        txtTitle.setText("Shop is closed");
        ok.setText("OK");

        if (openTime.length() > 0) {
            txtMessage.setText("Shop is closed now. Open later.\n\nOpen time : " + Tools.parseTime(openTime) + "\nClose time : " + Tools.parseTime(closeTime));
        } else {
            txtMessage.setText("Shop is closed now. Open later");
        }


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
//                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @NotNull
    public MutableLiveData<JsonObject> getLiveDistance(double lat1, double lon1, double lat2, double lon2, StepperLayout.OnCompleteClickedCallback callback) {
        MutableLiveData<JsonObject> response = new MutableLiveData<>();
        String myurl = String.format(DynamicConstants.getInstance().google_live_distance_url, lat1, lon1, lat2, lon2);

        submitClickable = false;
        progressDialog = new ProgressDialog(getActivity(), "Loading...");
        progressDialog.startLoading();
        StringRequest stringRequest = new StringRequest(myurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                submitClickable = true;
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distanceJson = steps.getJSONObject("distance");
//                    String parsedDistance = distanceJson.getString("text");
                    String parsedDistance = distanceJson.getString("value");

                    double distance = 0;
                    try {
                        distance = Double.parseDouble(parsedDistance) / 1000;
                        Console.logDebug("Distance checkout3 : " + distance);
                    } catch (Exception e) {
                        e.printStackTrace();
                        distance = Tools.distanceInKms(
                                Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0")),
                                Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0")),

                                Double.parseDouble(SessionData.getInstance().getShopLat()),
                                Double.parseDouble(SessionData.getInstance().getShopLng())
                        );
                    }
                    distance = Math.ceil(distance);

                    if (distance > 20) {
                        customAlertShopNotAvailableForDistance(getActivity());
                        Config.editPreferences.putString(Constants.AVAILABLE_DISTANCE, "FALSE").apply();
                    } else {
                        Config.editPreferences.putString(Constants.AVAILABLE_DISTANCE, "TRUE").apply();
                        callback.complete();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                submitClickable = true;
                progressDialog.dismiss();

                // Anything you want
                Console.error("error distance : " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Config.context);
        requestQueue.add(stringRequest);

        return response;
    }

    public void customAlertShopNotAvailableForDistance(Context mContext) { // nORMAL SCAN
        Dialog dialog;
        dialog = new Dialog(mContext);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_forced_update);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
        TextView ok = (TextView) dialog.findViewById(R.id.dialog_Ok);
        RelativeLayout rl_header = dialog.findViewById(R.id.rl_header);
        LinearLayout lnr_root = dialog.findViewById(R.id.lnr_root);
        View view_ok = (View) dialog.findViewById(R.id.view_ok);

        txtTitle.setText("Delivery is not available");

        ok.setText("Close");

        txtMessage.setText("Delivery is not available for your location");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().finish();
                dialog.dismiss();
            }
        });


        dialog.show();
    }

}

