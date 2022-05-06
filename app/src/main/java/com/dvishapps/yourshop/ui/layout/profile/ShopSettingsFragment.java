package com.dvishapps.yourshop.ui.layout.profile;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.services.GPSTrackerService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.FShopSettingsBinding;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.ui.common.ConfirmBottom;
import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.layout.map.MapsActivity;
import com.dvishapps.yourshop.ui.viewModel.ShopViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopSettingsFragment extends SFragment implements ConfirmBottom.OnConfirmDialogAction {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    private FShopSettingsBinding binding;
    private UserViewModel userViewModel;
    private ConfirmBottom dialog;
    private String user_id;
    private ConfirmBottom ok;
    private ProgressDialog progressDialogFetchShop;
    private ShopViewModel shopViewModel;

    public Shop shopDetails;

    String lat = "";
    String lng = "";

    String openTime = "";
    String closeTime = "";

    String openTimeText = "";
    String closeTimeText = "";
    private GPSTrackerService gpsTracker;
    private ProgressDialog progressDialog;

    boolean saveClickable = true;
    public boolean setLocationClickable = true;
    private boolean submitClickable = true;

    public ShopSettingsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        if (getArguments() != null) {
            user_id = getArguments().getString(Constants.USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_shop_settings, container, false);
        binding.setUserModel(userViewModel);
        SessionData.getInstance().setfShopSettingsBinding(binding);
        SessionData.getInstance().setShopSettingsFragment(ShopSettingsFragment.this);

        progressDialogFetchShop = new ProgressDialog(getActivity(), "Loading...");
        progressDialogFetchShop.startLoading();
        progressDialog = new ProgressDialog(getActivity(), "Fetching location...");

        gpsTracker = new GPSTrackerService(getActivity());

        shopViewModel.fetchShop();
        shopViewModel.getShopData().observe(getViewLifecycleOwner(), shop -> {
            progressDialogFetchShop.dismiss();
            shopDetails = shop;
            setShopDetails(shop);
        });
        binding.savePwd.setOnClickListener(v -> {
            dialog = new ConfirmBottom(this.getContext(), this);
            dialog.setTitle(getString(R.string.confirm));
            dialog.setDesc("Are you sure to save changes?");
            dialog.show();
        });

        binding.shopOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM;
                        String min;
                        openTime = selectedHour + ":" + selectedMinute + "";
                        if (selectedHour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }

                        if (selectedHour > 12) {
                            selectedHour = selectedHour - 12;
                        }
                        if ((selectedMinute + "").length() < 2) {
                            min = "0" + selectedMinute;
                        } else {
                            min = selectedMinute + "";
                        }
                        binding.shopOpenTime.setText(selectedHour + ":" + min + " " + AM_PM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select open time");
                mTimePicker.show();
            }
        });


        binding.shopCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM;
                        String min;
                        closeTime = selectedHour + ":" + selectedMinute + "";
                        if (selectedHour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }

                        if (selectedHour > 12) {
                            selectedHour = selectedHour - 12;
                        }
                        if ((selectedMinute + "").length() < 2) {
                            min = "0" + selectedMinute;
                        } else {
                            min = selectedMinute + "";
                        }
                        binding.shopCloseTime.setText(selectedHour + ":" + min + " " + AM_PM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select close time");
                mTimePicker.show();
            }
        });

        binding.txtChangeAddress.setOnClickListener(view -> {
            if (setLocationClickable) {
//                setLocationClickable = false;
//                setLocation();
                setLocationFromMap();

            }
//            setSavedLocation();
        });
//        if (shopDetails.getAddress2().length()>0) {
//            binding.progressLocation.setVisibility(View.GONE);
//        }
        binding.shopLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    if (!shopDetails.getAddress2().equals(charSequence.toString())) {
                        binding.progressLocation.setVisibility(View.GONE);
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return binding.getRoot();
    }

    public void setShopDetails(Shop shop) {

        openTime = shop.getShop_open_time();
        closeTime = shop.getShop_close_time();

        openTimeText = Tools.parseTime(openTime);
        closeTimeText = Tools.parseTime(closeTime);

        binding.shopOpenTime.setText(openTimeText);
        binding.shopCloseTime.setText(closeTimeText);
        binding.shopVersion.setText(shop.getShop_current_version());
        binding.shopPackagingCharge.setText(shop.getPackage_charges());
        binding.shopLocation.setText(shop.getAddress2());


        binding.numberButtonShippingCost.setRange(5, 10000000);
        binding.numberButtonShippingCost.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                try {
                    shopDetails.setShipping_tax_value(Float.parseFloat(binding.numberButtonShippingCost.getNumber()));
                } catch (NumberFormatException e) {
                    binding.numberButtonShippingCost.setNumber("5");
                    e.printStackTrace();
                }
            }
        });

        binding.numberButtonTax.setNumber("0");
        binding.numberButtonTax.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                try {
                    shopDetails.setOverall_tax_value(Float.parseFloat(binding.numberButtonTax.getNumber()));
                } catch (NumberFormatException e) {
                    binding.numberButtonTax.setNumber("0");
                    e.printStackTrace();
                }
            }
        });

        if (shopDetails.getShipping_tax_value() > 5) {
            try {
                binding.numberButtonShippingCost.setNumber(String.valueOf(Math.round(shopDetails.getShipping_tax_value())));
            } catch (Exception e) {
                binding.numberButtonShippingCost.setNumber("5");
                e.printStackTrace();
            }
        } else {
            binding.numberButtonShippingCost.setNumber("5");
        }

        if (shopDetails.getOverall_tax_value() > 0) {
            try {
                binding.numberButtonTax.setNumber(String.valueOf(Math.round(shopDetails.getOverall_tax_value())));
            } catch (Exception e) {
                binding.numberButtonTax.setNumber("0");
                e.printStackTrace();
            }
        } else {
            binding.numberButtonTax.setNumber("0");
        }
    }


    @Override
    public void onConfirm() {
        dialog.dismiss();
        //TODO save function
        saveShopSettings();
    }

    private void saveShopSettings() {
        progressDialogFetchShop.startLoading();
        if (saveClickable) {
            saveClickable = false;
            shopViewModel.setShopSettings(
                    openTime,
                    closeTime,
                    "" + shopDetails.getBranch_code(),
                    "" + shopDetails.getShop_main_branch_id(),
                    "" + shopDetails.getShop_current_version(),
                    "" + shopDetails.getShop_app_force_update(),
                    "" + shopDetails.getId(),
                    binding.shopPackagingCharge.getText().length() == 0 ? "0" : binding.shopPackagingCharge.getText().toString(),
                    shopDetails.getAddress2(),
                    shopDetails.getLat(),
                    shopDetails.getLng(),
                    shopDetails.getPostal_code(),
                    shopDetails.getShipping_tax_value() + "",
                    shopDetails.getOverall_tax_value() + ""

            ).observeForever(shop -> {
                if (shop != null) {
                    saveClickable = true;
                    progressDialogFetchShop.dismiss();
                    if (shop.getStatus().contains("404 error")) {
                        Console.toast("Some thing went wrong");
                    } else {
                        Console.toast("Updated successfully");
                        getActivity().onBackPressed();
                    }
                }
            });
        }
    }

    @Override
    public void onClose() {
        dialog.dismiss();
    }


    public void internetSnack(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
//                            retryFunctions();
                        } else {
                            internetSnack(binding.parentLayout);
                        }
                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private void setLocation() {
//        binding.progressLocation.setVisibility(View.VISIBLE);
        progressDialog.dismiss();
        progressDialog.startLoading();
//        Console.toast("Fetching current location");
        lat = "";
        try {
            if (Tools.isOnline()) {
                gpsTracker.getLocation();
                if (gpsTracker.canGetGps()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                            progressDialog.dismiss();
                            setLocationClickable = true;

                            return;
                        } else {
                            //                        progressDialog.dismiss();

                            if (gpsTracker.getIsGPSTrackingEnabled()) {
//                                binding.progressLocation.setVisibility(View.VISIBLE);
//                                SessionData.getInstance().setProgressBar(binding.progressLocation);
                                String address2 = gpsTracker.getAddressLine(getActivity());

                                if (address2 != null) {

                                    binding.shopLocation.setText(gpsTracker.getAddressLine(getActivity()));

                                    shopDetails.setAddress2(address2);
                                    shopDetails.setLat(gpsTracker.getLatitude() + "");
                                    shopDetails.setLng(gpsTracker.getLongitude() + "");
                                    shopDetails.setPostal_code(gpsTracker.getPostalCode(getActivity()));

                                    lat = gpsTracker.getLatitude() + "";
                                    if (lat.length() > 0) {
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
//                                            binding.progressLocation.setVisibility(View.GONE);

                                                progressDialog.dismiss();
                                                setLocationClickable = true;
                                            }
                                        }, 1000);
                                    } else {
                                        progressDialog.dismiss();
                                        setLocationClickable = true;
                                        setSavedLocation();
                                    }
                                } else {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
//                                            binding.progressLocation.setVisibility(View.GONE);

                                            progressDialog.dismiss();
                                            setLocationClickable = true;
                                            setApiLocation();
                                        }
                                    }, 1000);
                                }
                                //                            Intent map_intent = new Intent(getActivity(), MapsActivity.class);
                                //                            startActivity(map_intent);
                            } else {
                                gpsTracker.showSettingsAlert();
                            }
                        }
                    } else {

                        if (gpsTracker.getIsGPSTrackingEnabled()) {

                            String address2 = gpsTracker.getAddressLine(getActivity());

                            if (address2 != null) {

                                binding.shopLocation.setText(gpsTracker.getAddressLine(getActivity()));

                                shopDetails.setAddress2(address2);
                                shopDetails.setLat(gpsTracker.getLatitude() + "");
                                shopDetails.setLng(gpsTracker.getLongitude() + "");
                                shopDetails.setPostal_code(gpsTracker.getPostalCode(getActivity()));

                                lat = gpsTracker.getLatitude() + "";
                                if (lat.length() > 0) {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
//                                            binding.progressLocation.setVisibility(View.GONE);

                                            progressDialog.dismiss();
                                            setLocationClickable = true;
                                            setSavedLocation();
                                        }
                                    }, 1000);
                                } else {
                                    progressDialog.dismiss();
                                    setLocationClickable = true;
                                    setSavedLocation();
                                }
                            } else {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
//                                            binding.progressLocation.setVisibility(View.GONE);

                                        progressDialog.dismiss();
                                        setLocationClickable = true;
                                    }
                                }, 1000);
                            }
                            //                        Intent map_intent = new Intent(getActivity(), MapsActivity.class);
                            //                        startActivity(map_intent);/
                        } else {
//                            binding.progressLocation.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            gpsTracker.showSettingsAlert();
                        }
                    }
                } else {
//                    binding.progressLocation.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    gpsTracker.showSettingsAlert();
                }
            } else {
//                binding.progressLocation.setVisibility(View.GONE);
                progressDialog.dismiss();
                internetSnack(binding.parentLayout);
            }
        } catch (Exception e) {
//            binding.progressLocation.setVisibility(View.GONE);
            progressDialog.dismiss();
            setLocationClickable = true;
            setSavedLocation();
            e.printStackTrace();
        }
    }

    private void setLocationFromMap() {
        if (Tools.isOnline()) {
            gpsTracker.getLocation();
            if (gpsTracker.canGetGps()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);

                        return;
                    } else {

                        if (gpsTracker.getIsGPSTrackingEnabled()) {
                            SessionData.getInstance().setMapFrom("ShopSettingsFragment");
                            Intent map_intent = new Intent(getActivity(), MapsActivity.class);
                            startActivity(map_intent);
                        } else {
                            gpsTracker.showSettingsAlert();
                        }
                    }
                } else {

                    if (gpsTracker.getIsGPSTrackingEnabled()) {
                        SessionData.getInstance().setMapFrom("ShopSettingsFragment");
                        Intent map_intent = new Intent(getActivity(), MapsActivity.class);
                        startActivity(map_intent);
                    } else {
                        gpsTracker.showSettingsAlert();
                    }
                }
            } else {
                gpsTracker.showSettingsAlert();
            }
        } else {
            internetSnackMapClick(binding.parentLayout);
        }
    }

    public void setSavedLocation() {
//        binding.progressLocation.setVisibility(View.VISIBLE);
        progressDialog.startLoading();
        try {
            if (Config.sharedPreferences.getString(Constants.LAT, "").length() > 5) {
                String address = getAddressLine(getContext(), Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "")), Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "")));

                if (address != null) {

                    binding.shopLocation.setText(address);

                    shopDetails.setAddress2(address);
                    shopDetails.setLat(Config.sharedPreferences.getString(Constants.LAT, ""));
                    shopDetails.setLng(Config.sharedPreferences.getString(Constants.LNG, ""));
                    if (SessionData.getInstance().getShopPostalCode()!=null) {
                        shopDetails.setPostal_code(SessionData.getInstance().getShopPostalCode());
                    }

                    lat = Config.sharedPreferences.getString(Constants.LAT, "") + "";
                    if (lat.length() > 0) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                            binding.progressLocation.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                setLocationClickable = true;

                            }
                        }, 2000);
                    } else {
//                    setLocation();
                        progressDialog.dismiss();
                        setLocationClickable = true;
                        Console.toast("Can not fetch location!");
                    }
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                                            binding.progressLocation.setVisibility(View.GONE);

                            progressDialog.dismiss();
                            setLocationClickable = true;
                            setApiLocation();
                        }
                    }, 1000);
                }
            }
        } catch (Exception e) {
//            binding.progressLocation.setVisibility(View.GONE);
            progressDialog.dismiss();
            setLocationClickable = true;
            e.printStackTrace();
        }

    }


    private void setApiLocation() {
//        binding.progressLocation.setVisibility(View.VISIBLE);
        progressDialog.startLoading();
        try {
            if (gpsTracker.getLatitude() > 5) {
                try {
                    getAddress((gpsTracker.getLatitude()), gpsTracker.getLongitude());
                } catch (Exception e) {
                    progressDialog.dismiss();
                    setLocationClickable = true;
                    e.printStackTrace();
                }
            } else if (Config.sharedPreferences.getString(Constants.LAT, "").length() > 5) {
                try {
                    getAddress(Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "")), Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "")));
                } catch (Exception e) {
                    progressDialog.dismiss();
                    setLocationClickable = true;
                    e.printStackTrace();
                }
            } else {
                Console.toast("Can not fetch location!");
            }
        } catch (Exception e) {
//            binding.progressLocation.setVisibility(View.GONE);
            progressDialog.dismiss();
            setLocationClickable = true;
            e.printStackTrace();
        }

    }


    public String getAddressLine(Context context, double lat, double lng) {
        List<Address> addresses = getGeocoderAddress(context, lat, lng);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);
            SessionData.getInstance().setShopPostalCode(address.getPostalCode());
            return addressLine;
        } else {
            return null;
        }
    }

    public List<Address> getGeocoderAddress(Context context, double lat, double lng) {

        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

        try {
            /**
             * Geocoder.getFromLocation - Returns an array of Addresses
             * that are known to describe the area immediately surrounding the given latitude and longitude.
             */
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            return addresses;
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("TAG", "Impossible to connect to Geocoder", e);
        }

        return null;
    }

    @NotNull
    public MutableLiveData<JsonObject> getAddress(double lat1, double lon1) {
        MutableLiveData<JsonObject> response = new MutableLiveData<>();
        String myurl = String.format(DynamicConstants.getInstance().google_address_conversion_url, lat1, lon1);

        submitClickable = false;
        StringRequest stringRequest = new StringRequest(myurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                submitClickable = true;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    JSONObject routes = resultsArray.getJSONObject(0);
                    String parsedAddress = routes.getString("formatted_address");
//                    String address_components = routes.getString("formatted_address");
//                    JSONObject geometry = routes.getJSONObject("geometry");
//                    JSONObject location = routes.getJSONObject("location");
//                    String dis = legs.getString("distance");
//                    String lat = location.getString("lat");
//                    String lng = location.getString("lng");
                    JSONArray address_componentsArray = routes.getJSONArray("address_components");
                    JSONObject postalCodeObject = address_componentsArray.getJSONObject(7);
                    String postalCode = postalCodeObject.getString("long_name");


                    binding.shopLocation.setText(parsedAddress);

                    shopDetails.setAddress2(parsedAddress);
                    shopDetails.setLat(lat1 + "");
                    shopDetails.setLng(lon1 + "");
                    shopDetails.setPostal_code(postalCode);

                    parsedAddress = Config.sharedPreferences.getString(Constants.LAT, "") + "";
                    if (parsedAddress.length() > 0) {
                        progressDialog.dismiss();
                        setLocationClickable = true;
                    } else {
//                    setLocation();
                        progressDialog.dismiss();
                        setLocationClickable = true;
                        Console.toast("Can not fetch location!");
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    setLocationClickable = true;
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                submitClickable = true;
                // Anything you want
                progressDialog.dismiss();
                setLocationClickable = true;
                Console.error("error distance : " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Config.context);
        requestQueue.add(stringRequest);

        return response;
    }

    public void internetSnackMapClick(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
//                            retryFunctions();
                        } else {
                            internetSnack(binding.parentLayout);
                        }
                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

}
