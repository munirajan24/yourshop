package com.dvishapps.yourshop.ui.layout.checkout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.services.CheckoutService;
import com.dvishapps.yourshop.api.services.GPSTrackerService;
import com.dvishapps.yourshop.databinding.CheckoutForm1Binding;
import com.dvishapps.yourshop.models.Country;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.adapters.SelectAdapter;
import com.dvishapps.yourshop.ui.common.Select;
import com.dvishapps.yourshop.ui.interfaces.DataManager;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CheckoutStep1 extends Fragment implements BlockingStep {
    private DataManager<Transaction> dataManager;
    private CheckoutForm1Binding binding;
    private User user;
    private Select select;
    private String country_id;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    private GPSTrackerService gpsTracker;
//    private EditText billingCityInput, billingPostalInput, edtCitySelectionView, edtPostalInput;

    @NotNull
    public static CheckoutStep1 newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.USER, user);
        CheckoutStep1 fragment = new CheckoutStep1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (getArguments() != null)
            user = (User) getArguments().getSerializable(Constants.USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.checkout_form_1, container, false);
        binding.setLifecycleOwner(this);
        binding.setUser(user);
        SessionData.getInstance().setCurrentUser(user);
        binding.billigLikeShipping.setOnCheckedChangeListener((buttonView, isChecked) -> {
            initBillingIputText(isChecked);
        });
        initializeViews();
//        setCurrentAddress();


        binding.billingContainer.setVisibility(View.GONE);
        binding.switchBillingLikeShipping.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.billingContainer.setVisibility(View.VISIBLE);
//                binding.nestedScrollForm.fullScroll(View.FOCUS_DOWN);
                binding.shippingData.setVisibility(View.GONE);
            } else {
                binding.billingContainer.setVisibility(View.GONE);
                binding.shippingData.setVisibility(View.VISIBLE);
            }
        });

        binding.countryTextView.setOnClickListener(v1 -> {
            CheckoutService.getZone(null).observe(this.getViewLifecycleOwner(), countries -> {
                List<SelectAdapter.SelectObj> list = new ArrayList<>();
                for (Country c : countries) {
                    list.add(new SelectAdapter.SelectObj(c.getName(), c.getId()));
                }
                select = new Select(list, "Select Country", val -> {
                    binding.countryTextView.setText(val.getTitle());
                    country_id = val.getId();
                    select.dismiss();
                });
                select.show(getParentFragmentManager(), "country");
            });
        });

        binding.cityTextView.setOnClickListener(v1 -> {
            if (country_id == null || country_id.isEmpty()) {
                Console.toast(getString(R.string.error_message__choose_country));
            } else {
                CheckoutService.getZone(country_id).observe(this.getViewLifecycleOwner(), countries -> {
                    List<SelectAdapter.SelectObj> list = new ArrayList<>();
                    for (Country c : countries) {
                        list.add(new SelectAdapter.SelectObj(c.getName(), c.getId()));
                    }
                    select = new Select(list, "Select Country", val -> {
                        binding.cityTextView.setText(val.getTitle());
                        select.dismiss();
                    });
                    select.show(getParentFragmentManager(), "city");
                });
            }
        });

        SessionData.getInstance().setTxtCurrentLocation(binding.txtMapAddress);
        SessionData.getInstance().setTxtCurrentLocationAddress(binding.address1Input);
        SessionData.getInstance().setTxtCurrentLocationAddress(binding.address1Input);
        binding.txtChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map_intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(map_intent);
//                {
//                    SessionData.getInstance().setMapFrom("CheckoutStep1");
//                    gpsTracker.getLocation();
//                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                MY_PERMISSIONS_REQUEST_LOCATION);
//
//                        return;
//                    } else {
//
//                        if (gpsTracker.getIsGPSTrackingEnabled()) {
//                            Intent map_intent = new Intent(getActivity(), MapsActivity.class);
//                            startActivity(map_intent);
//                        } else {
//                            gpsTracker.showSettingsAlert();
//                        }
//                    }
//                }
            }
        });

        binding.txtGetPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map_intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(map_intent);
            }
        });
        return binding.getRoot();
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
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        if (checkForm1()) {
            // not completed
//            dataManager.getData().setShipping_first_name(binding.firstNameInput.getText().toString());
//            dataManager.getData().setShipping_last_name(binding.lastNameInput.getText().toString());
//            dataManager.getData().setShipping_email(binding.emailInput.getText().toString());
//            dataManager.getData().setShipping_phone(binding.phoneInput.getText().toString());
//            dataManager.getData().setShipping_company(binding.companyInput.getText().toString());
//            dataManager.getData().setShipping_address_1(binding.address1Input.getText().toString());
//            dataManager.getData().setShipping_address_2(binding.address2Input.getText().toString());
//            dataManager.getData().setShipping_country(binding.countryTextView.getText().toString());
//            dataManager.getData().setShipping_state(binding.stateInput.getText().toString());
//            dataManager.getData().setShipping_city(binding.edtCitySelectionView.getText().toString());
//            dataManager.getData().setShipping_postal_code(binding.edtPostalInput.getText().toString());
//
//            dataManager.getData().setBilling_first_name(binding.billingFirstNameInput.getText().toString());
//            dataManager.getData().setBilling_last_name(binding.billingLastNameInput.getText().toString());
//            dataManager.getData().setBilling_email(binding.billingEmailInput.getText().toString());
//            dataManager.getData().setBilling_phone(binding.billingPhoneInput.getText().toString());
//            dataManager.getData().setBilling_company(binding.billingCompanyInput.getText().toString());
//            dataManager.getData().setBilling_address_1(binding.billingAddress1Input.getText().toString());
//            dataManager.getData().setBilling_address_2(binding.billingAddress2Input.getText().toString());
//            dataManager.getData().setBilling_country(binding.billingCountryInput.getText().toString());
//            dataManager.getData().setBilling_state(binding.billingStateInput.getText().toString());
//            dataManager.getData().setBilling_city(binding.billingCityInput.getText().toString());
//            dataManager.getData().setBilling_postal_code(binding.billingPostalInput.getText().toString());

            selectAddressForShipping();

            callback.goToNextStep();
        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        Console.logError("onCompleteClicked");

        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    private boolean checkForm1() {

//        if (binding.switchBillingLikeShipping.isChecked()) {
//
//            if (Tools.isEmpty(binding.billingEmailInput)) {
//                Console.toast("Please Enter Your Email");
//                Console.log("Please Enter Your Email");
//                return false;
//            } else if (Tools.isEmpty(binding.billingAddress1Input)) {
//                Console.toast("Please Enter Your Address");
//                return false;
//            }
//
//        }

//        if (Tools.isEmpty(binding.emailInput)) {
//            Console.toast("Please Enter Your Email");
//            Console.logError("Please Enter Your Email");
//            return false;
//        } else
        if ((binding.address1Input.getText().toString().isEmpty())) {
            Console.toast("Please set Your Location");
            return false;
        } else if (Tools.isEmpty(binding.edtUserShippingAddress2)) {
            Console.toast("Please enter complete address");
            return false;
        } else if (Tools.isEmpty(binding.phoneInput)) {
            Console.toast("Please set your phone number");
            return false;
        }

        return true;
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

    private void initBillingIputText(boolean check) {
        if (check) {
            binding.billingPostalInput.setText(binding.edtPostalInput.getText());
            binding.billingCityInput.setText(binding.edtCitySelectionView.getText());
            binding.billingStateInput.setText(binding.stateInput.getText());
            binding.billingCountryInput.setText(binding.countryTextView.getText());
            binding.billingAddress1Input.setText(binding.address1Input.getText());
            binding.billingAddress2Input.setText(binding.address2Input.getText());
            binding.billingCompanyInput.setText(binding.companyInput.getText());
            binding.billingEmailInput.setText(binding.emailInput.getText());
            binding.billingPhoneInput.setText(binding.phoneInput.getText());
            binding.billingLastNameInput.setText(binding.lastNameInput.getText());
            binding.billingFirstNameInput.setText(binding.firstNameInput.getText());
        } else {
            for (int i = 0; i < binding.billingContainer.getChildCount(); i++) {
                if (binding.billingContainer.getChildAt(i) instanceof EditText)
                    ((EditText) binding.billingContainer.getChildAt(i)).setText("");
            }
        }
    }

    public void initializeViews() {
//        edtCitySelectionView = binding.getRoot().findViewById(R.id.edtCitySelectionView);
//        edtPostalInput = binding.getRoot().findViewById(R.id.edt_postalInput);
//        billingCityInput = binding.getRoot().findViewById(R.id.billingCityInput);
//        billingPostalInput = binding.getRoot().findViewById(R.id.billingPostalInput);

        gpsTracker = new GPSTrackerService(getActivity());

        if (user.getShipping_address_1().isEmpty()) {
            binding.address1Input.setText(user.getShipping_address_1());
        }
        if (user.getShipping_email().isEmpty()) {
            binding.emailInput.setEnabled(true);
        }

        if (user.getUser_phone() != null) {
            if (user.getUser_phone().length() != 0) {
                binding.txtRequestPhone.setText(user.getUser_phone());
                binding.txtGetPhone.setText("Change Number");
            }
        }
        if (user.getShipping_city().length() != 0) {
            binding.txtMapAddress.setText(user.getShipping_city());
            binding.txtChangeAddress.setText("Change");
        }
        SessionData.getInstance().setCheckoutForm1Binding(binding);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==MY_PERMISSIONS_REQUEST_LOCATION){
//
//        }
    }

    public void setCurrentAddress() {

        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return;
        } else {
            if (!gpsTracker.getIsGPSTrackingEnabled()) {
                gpsTracker.showSettingsAlert();
            } else {
//                binding.countryTextView.setText(gpsTracker.getCountryName(getActivity()));
//                binding.cityTextView.setText(gpsTracker.getLocality(getActivity()));

                user.setShipping_city(gpsTracker.getLocality(getActivity()));
                user.setShipping_postal_code(gpsTracker.getPostalCode(getActivity()));
                user.setBilling_city(gpsTracker.getLocality(getActivity()));
                user.setBilling_postal_code(gpsTracker.getPostalCode(getActivity()));

                binding.setUser(user);

                binding.edtCitySelectionView.setText(gpsTracker.getLocality(getActivity()));
                binding.edtPostalInput.setText(gpsTracker.getPostalCode(getActivity()));
                binding.billingCityInput.setText(gpsTracker.getLocality(getActivity()));
                binding.billingPostalInput.setText(gpsTracker.getPostalCode(getActivity()));
            }
        }
    }

    public void selectAddressForShipping() {

        if (binding.switchBillingLikeShipping.isChecked()) {
            //Custom Address
            dataManager.getData().setShipping_first_name(binding.billingFirstNameInput.getText().toString());
            dataManager.getData().setShipping_last_name(binding.billingLastNameInput.getText().toString());
            dataManager.getData().setShipping_email(binding.billingEmailInput.getText().toString());
            dataManager.getData().setShipping_phone(binding.billingPhoneInput.getText().toString());
            dataManager.getData().setShipping_company(binding.billingCompanyInput.getText().toString());
            dataManager.getData().setShipping_address_1(binding.billingAddress1Input.getText().toString());
            dataManager.getData().setShipping_address_2(binding.billingAddress2Input.getText().toString());
            dataManager.getData().setShipping_country(binding.billingCountryInput.getText().toString());
            dataManager.getData().setShipping_state(binding.billingStateInput.getText().toString());
            dataManager.getData().setShipping_city(binding.billingCityInput.getText().toString());
            dataManager.getData().setShipping_postal_code(binding.billingPostalInput.getText().toString());

            dataManager.getData().setBilling_first_name(binding.billingFirstNameInput.getText().toString());
            dataManager.getData().setBilling_last_name(binding.billingLastNameInput.getText().toString());
            dataManager.getData().setBilling_email(binding.billingEmailInput.getText().toString());
            dataManager.getData().setBilling_phone(binding.billingPhoneInput.getText().toString());
            dataManager.getData().setBilling_company(binding.billingCompanyInput.getText().toString());
            dataManager.getData().setBilling_address_1(binding.billingAddress1Input.getText().toString());
            dataManager.getData().setBilling_address_2(binding.billingAddress2Input.getText().toString());
            dataManager.getData().setBilling_country(binding.billingCountryInput.getText().toString());
            dataManager.getData().setBilling_state(binding.billingStateInput.getText().toString());
            dataManager.getData().setBilling_city(binding.billingCityInput.getText().toString());
            dataManager.getData().setBilling_postal_code(binding.billingPostalInput.getText().toString());

        } else {
            //Default Address
            dataManager.getData().setShipping_first_name(binding.firstNameInput.getText().toString());
            dataManager.getData().setShipping_last_name(binding.lastNameInput.getText().toString());
            dataManager.getData().setShipping_email(binding.emailInput.getText().toString());
            dataManager.getData().setShipping_phone(binding.phoneInput.getText().toString());
            dataManager.getData().setShipping_company(binding.companyInput.getText().toString());
            dataManager.getData().setShipping_address_1(binding.address1Input.getText().toString());
            dataManager.getData().setShipping_address_2(binding.edtUserShippingAddress2.getText().toString());
            dataManager.getData().setShipping_country(binding.countryTextView.getText().toString());
            dataManager.getData().setShipping_state(binding.stateInput.getText().toString());
            dataManager.getData().setShipping_city(binding.edtCitySelectionView.getText().toString());
            dataManager.getData().setShipping_postal_code(binding.edtPostalInput.getText().toString());


            dataManager.getData().setBilling_first_name(binding.firstNameInput.getText().toString());
            dataManager.getData().setBilling_last_name(binding.lastNameInput.getText().toString());
            dataManager.getData().setBilling_email(binding.emailInput.getText().toString());
            dataManager.getData().setBilling_phone(binding.phoneInput.getText().toString());
            dataManager.getData().setBilling_company(binding.companyInput.getText().toString());
            dataManager.getData().setBilling_address_1(binding.address1Input.getText().toString());
            dataManager.getData().setBilling_address_2(binding.edtUserShippingAddress2.getText().toString());
            dataManager.getData().setBilling_country(binding.countryTextView.getText().toString());
            dataManager.getData().setBilling_state(binding.stateInput.getText().toString());
            dataManager.getData().setBilling_city(binding.edtCitySelectionView.getText().toString());
            dataManager.getData().setBilling_postal_code(binding.edtPostalInput.getText().toString());
            dataManager.getData().setShop_id(SessionData.getInstance().getShopKeyValue());
        }

    }

}
