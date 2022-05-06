package com.dvishapps.yourshop.ui.layout.checkout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.ApiResponse;
import com.dvishapps.yourshop.api.services.GPSTrackerService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.CEditProfileBinding;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.common.SCompatActivity;
import com.dvishapps.yourshop.ui.common.Select;
import com.dvishapps.yourshop.ui.layout.map.MapsActivity;
import com.dvishapps.yourshop.ui.viewModel.CheckoutViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditProfileActivity extends SCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    //    private CEditProfileBinding binding;
    private UserViewModel userViewModel;
    private CheckoutViewModel checkoutViewModel;
    private User currentUser;
    private Select select;
    private String country_id;
    private static final int PICK_IMAGE = 1;

    EditText edtCitySelectionView, edt_postal_code, edt_user_billing_postal, edt_user_billing_city_;
    TextView cityTextView;
    private Button saveButton;
    private ImageView editImageBtn;
    private TextView countryTextView;
    private TextInputEditText userPhone, user_name, user_email,
            user_shipping_first_name, user_shipping_last_name, user_shipping_email,
            user_shipping_phone, user_shipping_address1;
    private TextView txt_map_address;
    private EditText edt_user_shipping_address2;
    private ImageView profileImageView;
    private GPSTrackerService gpsTracker;
    private TextView txt_change_address;
    private RelativeLayout parentLayout;

    boolean saveClickable = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_edit_profile_);


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);

        currentUser = (User) SessionData.getInstance().getCurrentUser();
        getSupportActionBar().setTitle("Edit Profile");

//        binding.setCurrentUser(currentUser);
        initializeViews();
//        setCurrentAddress();

        txt_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.isOnline()) {

                    gpsTracker.getLocation();
                    if (gpsTracker.canGetGps()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);

                                return;
                            } else {

                                if (gpsTracker.getIsGPSTrackingEnabled()) {
                                    SessionData.getInstance().setMapFrom("EditProfileActivity");

                                    Intent map_intent = new Intent(EditProfileActivity.this, MapsActivity.class);
                                    startActivity(map_intent);
                                } else {
                                    gpsTracker.showSettingsAlert();
                                }
                            }
                        } else {

                            if (gpsTracker.getIsGPSTrackingEnabled()) {
                                SessionData.getInstance().setMapFrom("EditProfileActivity");

                                Intent map_intent = new Intent(EditProfileActivity.this, MapsActivity.class);
                                startActivity(map_intent);
                            } else {
                                gpsTracker.showSettingsAlert();
                            }
                        }
                    } else {
                        gpsTracker.showSettingsAlert();
                    }
                } else {
                    internetSnack(parentLayout);
                }
            }
        });

        saveButton.setOnClickListener(v -> {
            if (Tools.isOnline()) {
                if (checkPhone()) {
                    getCurrentUserDetails();
                    if (saveClickable) {
                        saveClickable = false;
                        userViewModel.editProfile(currentUser).observeForever(apiResponse -> {
                            if (apiResponse.getStatus() != null) {
                                saveClickable = true;

                                if (!apiResponse.getStatus().contains("404 error")) {
                                    if (apiResponse.getStatus().matches("success")) {
                                        setSuccessOrErrorStatus(apiResponse.getStatus(), apiResponse);
                                    } else {
                                        Console.toast(apiResponse.getMessage());
                                    }
                                } else {
                                    String err = "error";
                                    try {

                                        if (apiResponse.getStatus().split("404 error").length > 1)
                                            err = apiResponse.getStatus().split("404 error")[1];
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (err.contains("UnknownHostException")) {
                                        internetSnack(parentLayout);
                                    } else {
                                        Console.toast(err);
                                    }

                                }
                            }
                        });
                    }
                }
            } else {
                internetSnack(parentLayout);
            }
        });


        editImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

    }

    private void setSuccessOrErrorStatus(String status, ApiResponse apiResponse) {
        if (status.matches("success")) {
            Config.editPreferences.putString(Constants.LAT, SessionData.getInstance().getCurrentAddress().getLatitude() + "").apply();
            Config.editPreferences.putString(Constants.LNG, SessionData.getInstance().getCurrentAddress().getLongitude() + "").apply();

            getLiveDistance(Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0")),
                    Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0")),

                    Double.parseDouble(SessionData.getInstance().getShopLat()),
                    Double.parseDouble(SessionData.getInstance().getShopLng())
                    , apiResponse
            );
//            {
//                double distance = 0;
//
//                float deliveryCharge = 0;
//                boolean twoWayDelivery = SessionData.getInstance().isTwoWayDelivery();
//
////                double distance = Tools.distanceInKms(
////                        Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0")),
////                        Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0")),
////
////                        Double.parseDouble(SessionData.getInstance().getShopLat()),
////                        Double.parseDouble(SessionData.getInstance().getShopLng())
////                );
//
//                if (distance > 2) {
//                    if (twoWayDelivery) {
//                        //TODO : calculate with formula : distance * shipping tax * 2       ---for two way delivery      (or)
//                        deliveryCharge = 20 + SessionData.getInstance().getShopShippingTaxValue() * (int) (distance * 2 - 2);
////                                Config.cart.setDelivery_charge(20 + shopResponse.get(0).getShipping_tax_value() * (distance - 2) * 2);
//                    } else {
//                        //TODO : calculate with formula : distance * shipping tax      ---for one way delivery
////                                Config.cart.setDelivery_charge(20 + shopResponse.get(0).getShipping_tax_value() * distance);
//                        deliveryCharge = 20 + SessionData.getInstance().getShopShippingTaxValue() * (int) (distance - 2);
//                    }
//                } else {
//                    deliveryCharge = 20;
////                            Config.cart.setDelivery_charge(deliveryCharge);
//                }
//                SessionData.getInstance().setShippingTaxValue(deliveryCharge);
//                Config.cart.setDelivery_charge(deliveryCharge);
//                Console.logDebug("distance :" + distance);
//                Console.logDebug("deliveryCharge :" + deliveryCharge);
//
//                if (SessionData.getInstance().getCheckoutStep2() != null) {
//
//                    SessionData.getInstance().getCheckoutStep2().setDeliveryCharge(deliveryCharge);
//
//                }
//
//                SessionData.getInstance().getCheckoutForm1Binding().txtRequestPhone.setText(userPhone.getText());
//                SessionData.getInstance().getCheckoutForm1Binding().txtGetPhone.setText("Change Number");
//                SessionData.getInstance().getCheckoutForm1Binding().phoneInput.setText(userPhone.getText().toString());
//                SessionData.getInstance().getCheckoutForm1Binding().edtUserShippingAddress2.setText(edt_user_shipping_address2.getText().toString());
//                SessionData.getInstance().getCheckoutForm1Binding().address2Input.setText(edt_user_shipping_address2.getText().toString());
//
//                SessionData.getInstance().getCurrentUser().setUser_phone(userPhone.getText().toString());
//
//
//                SessionData.getInstance().getCurrentUser().setShipping_city(SessionData.getInstance().getCurrentAddressFormatted());
//
//                SessionData.getInstance().getCurrentUser().setShipping_address_1(user_shipping_address1.getText().toString());
//                SessionData.getInstance().getCheckoutForm1Binding().txtMapAddress.setText(SessionData.getInstance().getCurrentAddressFormatted());
//                SessionData.getInstance().getCheckoutForm1Binding().address1Input.setText(user_shipping_address1.getText().toString());
//                SessionData.getInstance().getCheckoutForm1Binding().postalInput.setText(edt_postal_code.getText());
//                SessionData.getInstance().getCheckoutForm1Binding().edtCitySelectionView.setText(SessionData.getInstance().getCurrentAddressFormatted());
////                        SessionData.getInstance().getCheckoutForm1Binding().stateInput.setText();
//                SessionData.getInstance().getCheckoutForm1Binding().countryTextView.setText(countryTextView.getText());
//
//                finish();
//                Console.toast(apiResponse.getMessage());
//            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                try {
                    Uri file = data.getData();
                    assert file != null;
                    InputStream inputStream = getContentResolver().openInputStream(file);
                    Bitmap image_bitmap = BitmapFactory.decodeStream(inputStream);
                    profileImageView.setImageBitmap(image_bitmap);

//                    userViewModel.uploadUserImage(currentUser.getUser_id(), image_bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {

            gpsTracker = new GPSTrackerService(this);
            if (!gpsTracker.getIsGPSTrackingEnabled()) {
                gpsTracker.showSettingsAlert();
            } else {
                countryTextView.setText("test " + gpsTracker.getCountryName(this));
            }
        }
    }

    public void initializeViews() {

        gpsTracker = new GPSTrackerService(this);
        edtCitySelectionView = findViewById(R.id.edtCitySelectionView);
        edt_postal_code = findViewById(R.id.edt_postal_code);
        edt_user_billing_postal = findViewById(R.id.edt_user_billing_postal_);
        edt_user_billing_city_ = findViewById(R.id.edt_user_billing_city_);
        parentLayout = findViewById(R.id.parent_layout);


        saveButton = findViewById(R.id.saveButton);
        userPhone = findViewById(R.id.user_phone);
        editImageBtn = findViewById(R.id.edit_image_btn);
        profileImageView = findViewById(R.id.profileImageView);
        countryTextView = findViewById(R.id.countryTextView);
        cityTextView = findViewById(R.id.cityTextView);

        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_shipping_first_name = findViewById(R.id.user_shipping_first_name);
        user_shipping_last_name = findViewById(R.id.user_shipping_last_name);
        user_shipping_email = findViewById(R.id.user_shipping_email);
        user_shipping_phone = findViewById(R.id.user_shipping_phone);
        user_shipping_address1 = findViewById(R.id.user_shipping_address1);
        edt_user_shipping_address2 = findViewById(R.id.edt_user_shipping_address2);
        txt_map_address = findViewById(R.id.txt_map_address);
        txt_change_address = findViewById(R.id.txt_change_address);


        user_name.setText(currentUser.getUser_name());
        user_email.setText(currentUser.getUser_email());
        userPhone.setText(currentUser.getUser_phone());
        userPhone.requestFocus();
        user_shipping_first_name.setText(currentUser.getShipping_first_name());
        user_shipping_last_name.setText(currentUser.getShipping_last_name());
        user_shipping_email.setText(currentUser.getShipping_email());
        user_shipping_phone.setText(currentUser.getShipping_phone());
        user_shipping_address1.setText(currentUser.getShipping_address_1());
        edt_user_shipping_address2.setText(currentUser.getShipping_address_2());
        txt_map_address.setText(currentUser.getShipping_city());
        SessionData.getInstance().setEdtProfileActivityAddressLocality(txt_map_address);
        SessionData.getInstance().setEdtProfileActivityAddress(user_shipping_address1);
        edtCitySelectionView.setText(currentUser.getShipping_city());
        edt_postal_code.setText(currentUser.getShipping_postal_code());


    }

    public void setCurrentAddress() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                return;
            } else {
                //            GPSTrackerService gpsTracker = new GPSTrackerService(this);
                if (!gpsTracker.getIsGPSTrackingEnabled()) {
                    gpsTracker.showSettingsAlert();
                } else {
                    countryTextView.setText(gpsTracker.getCountryName(this));
                    cityTextView.setText(gpsTracker.getLocality(this));

                    edtCitySelectionView.setText(gpsTracker.getLocality(this));
                    edt_postal_code.setText(gpsTracker.getPostalCode(this));
                    edt_user_billing_city_.setText(gpsTracker.getLocality(this));
                    edt_user_billing_postal.setText(gpsTracker.getPostalCode(this));
                }
            }
        }
    }

    public boolean checkPhone() {
        if (userPhone.getText().toString().length() == 0) {
            Console.toast("Enter phone number");
            return false;
        } else if (userPhone.getText().toString().length() < 5) {
            Console.toast("Enter a valid phone number");
            return false;
        } else if (txt_map_address.getText().toString().length() == 0) {
            Console.toast("Set location");
            return false;
        } else if (edt_user_shipping_address2.getText().toString().length() == 0) {
            Console.toast("Enter complete address");
            return false;
        }
        return true;
    }

    public void getCurrentUserDetails() {
        currentUser.setUser_name(user_name.getText().toString());
        currentUser.setUser_email(user_email.getText().toString());
        currentUser.setUser_phone(userPhone.getText().toString());
        currentUser.setShipping_first_name(user_shipping_first_name.getText().toString());
        currentUser.setShipping_last_name(user_shipping_last_name.getText().toString());
        currentUser.setShipping_email(user_shipping_email.getText().toString());
        currentUser.setShipping_phone(user_shipping_phone.getText().toString());
        currentUser.setShipping_address_1(user_shipping_address1.getText().toString());
        currentUser.setShipping_address_2(edt_user_shipping_address2.getText().toString());
        currentUser.setShipping_postal_code(edt_postal_code.getText().toString());
//        currentUser.setShipping_city(edtCitySelectionView.getText().toString());
        currentUser.setShipping_city(txt_map_address.getText().toString());


    }

    public void internetSnack(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Close", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
//                            retryFunctions();
                        } else {
//                            internetSnack(parentLayout);
                        }

                    }
                })
                .setBackgroundTint(parentLayout.getResources().getColor(android.R.color.black))
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    @NotNull
    public MutableLiveData<JsonObject> getLiveDistance(double lat1, double lon1, double lat2, double lon2, ApiResponse apiResponse) {
        MutableLiveData<JsonObject> response = new MutableLiveData<>();
        String myurl = String.format(DynamicConstants.getInstance().google_live_distance_url, lat1, lon1, lat2, lon2);

        StringRequest stringRequest = new StringRequest(myurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distanceJson = steps.getJSONObject("distance");
//                    String parsedDistance = distanceJson.getString("text");
                    String parsedDistance = distanceJson.getString("value");

//                    Console.toast("distance : " + parsedDistance);

                    {
                        double distance = 0;
                        try {
                            distance = Double.parseDouble(parsedDistance) / 1000;
                            Console.logDebug("distance : " + distance);
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
                        float deliveryCharge = 0;
                        boolean twoWayDelivery = SessionData.getInstance().isTwoWayDelivery();

//                double distance = Tools.distanceInKms(
//                        Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0")),
//                        Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0")),
//
//                        Double.parseDouble(SessionData.getInstance().getShopLat()),
//                        Double.parseDouble(SessionData.getInstance().getShopLng())
//                );

                        if (distance > 2) {
                            if (twoWayDelivery) {
                                //TODO : calculate with formula : distance * shipping tax * 2       ---for two way delivery      (or)
                                deliveryCharge = 20 + SessionData.getInstance().getShopShippingTaxValue() * (int) (distance * 2 - 2);
//                                Config.cart.setDelivery_charge(20 + shopResponse.get(0).getShipping_tax_value() * (distance - 2) * 2);
                            } else {
                                //TODO : calculate with formula : distance * shipping tax      ---for one way delivery
//                                Config.cart.setDelivery_charge(20 + shopResponse.get(0).getShipping_tax_value() * distance);
                                deliveryCharge = 20 + SessionData.getInstance().getShopShippingTaxValue() * (int) (distance - 2);
                            }
                        } else {
                            deliveryCharge = 20;
//                            Config.cart.setDelivery_charge(deliveryCharge);
                        }
                        SessionData.getInstance().setShippingTaxValue(deliveryCharge);
                        Config.cart.setDelivery_charge(deliveryCharge);
                        Console.logDebug("distance :" + distance);
                        Console.logDebug("deliveryCharge :" + deliveryCharge);

                        if (SessionData.getInstance().getCheckoutStep2() != null) {

                            SessionData.getInstance().getCheckoutStep2().setDeliveryCharge(deliveryCharge);

                        }

                        SessionData.getInstance().getCheckoutForm1Binding().txtRequestPhone.setText(userPhone.getText());
                        SessionData.getInstance().getCheckoutForm1Binding().txtGetPhone.setText("Change Number");
                        SessionData.getInstance().getCheckoutForm1Binding().phoneInput.setText(userPhone.getText().toString());
                        SessionData.getInstance().getCheckoutForm1Binding().edtUserShippingAddress2.setText(edt_user_shipping_address2.getText().toString());
                        SessionData.getInstance().getCheckoutForm1Binding().address2Input.setText(edt_user_shipping_address2.getText().toString());

                        SessionData.getInstance().getCurrentUser().setUser_phone(userPhone.getText().toString());


                        SessionData.getInstance().getCurrentUser().setShipping_city(SessionData.getInstance().getCurrentAddressFormatted());

                        SessionData.getInstance().getCurrentUser().setShipping_address_1(user_shipping_address1.getText().toString());
                        SessionData.getInstance().getCheckoutForm1Binding().txtMapAddress.setText(SessionData.getInstance().getCurrentAddressFormatted());
                        SessionData.getInstance().getCheckoutForm1Binding().address1Input.setText(user_shipping_address1.getText().toString());
                        SessionData.getInstance().getCheckoutForm1Binding().postalInput.setText(edt_postal_code.getText());
                        SessionData.getInstance().getCheckoutForm1Binding().edtCitySelectionView.setText(SessionData.getInstance().getCurrentAddressFormatted());
//                        SessionData.getInstance().getCheckoutForm1Binding().stateInput.setText();
                        SessionData.getInstance().getCheckoutForm1Binding().countryTextView.setText(countryTextView.getText());

                        finish();
                        Console.toast(apiResponse.getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Console.error("error distance : " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Config.context);
        requestQueue.add(stringRequest);

        return response;
    }

}
