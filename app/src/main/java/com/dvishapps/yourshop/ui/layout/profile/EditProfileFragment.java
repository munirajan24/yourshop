package com.dvishapps.yourshop.ui.layout.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.services.GPSTrackerService;
import com.dvishapps.yourshop.databinding.FEditProfileBinding;
import com.dvishapps.yourshop.models.Country;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.adapters.SelectAdapter;
import com.dvishapps.yourshop.ui.common.Select;
import com.dvishapps.yourshop.ui.layout.map.MapsActivity;
import com.dvishapps.yourshop.ui.viewModel.CheckoutViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditProfileFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    private FEditProfileBinding binding;
    private UserViewModel userViewModel;
    private CheckoutViewModel checkoutViewModel;
    private User currentUser;
    private Select select;
    private String country_id;
    private static final int PICK_IMAGE = 1;

    EditText edtCitySelectionView, edt_postal_code, edt_user_billing_postal, edt_user_billing_city_;
    TextView cityTextView;
    private GPSTrackerService gpsTracker;

    boolean saveClickable = true;
    ProgressDialog progressDialog;

    public EditProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        if (getArguments() != null) {
            currentUser = (User) getArguments().getSerializable(Constants.LOGGED_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_edit_profile, container, false);
        binding.setCurrentUser(currentUser);
        progressDialog = new ProgressDialog(getActivity(), "Loading");

        SessionData.getInstance().setCurrentUser(currentUser);
        SessionData.getInstance().setfEditProfileBinding(binding);
        gpsTracker = new GPSTrackerService(EditProfileFragment.this.getActivity());
        initializeViews();
//        setCurrentAddress();

        binding.saveButton.setOnClickListener(v -> {
            if (Tools.isOnline()) {
                if (saveClickable) {
                    saveClickable = false;
                    progressDialog.startLoading();
                    userViewModel.editProfile(currentUser).observe(this.getViewLifecycleOwner(), apiResponse -> {
                        if (apiResponse != null) {
                            saveClickable = true;
                            progressDialog.dismiss();
                            if (apiResponse.getStatus().matches("success")) {
                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_edit_profile_to_nav_profile);
                                Console.toast(apiResponse.getMessage());
                            } else {
                                Console.toast(apiResponse.getMessage());
                            }
                        }
                    });
                }
            } else {
                internetSnack(binding.parentLayout);
            }
        });

//        binding.passwordChangeButton.setOnClickListener(v -> {
//            if (Tools.isOnline()) {ma
//                Bundle data = new Bundle();
//                if (currentUser != null) {
//                    data.putString(Constants.USER_ID, currentUser.getUser_id());
//                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_edit_profile_to_changePwdFragment, data);
//                }
//            } else {
//                internetSnack(binding.parentLayout);
//            }
//        });

        binding.countryTextView.setOnClickListener(v1 -> {
            checkoutViewModel.getZone(null).observe(this.getViewLifecycleOwner(), countries -> {
                List<SelectAdapter.SelectObj> list = new ArrayList<>();
                for (Country c : countries) {
                    list.add(new SelectAdapter.SelectObj(c.getName(), c.getId()));
                }

                select = new Select(list, getString(R.string.country), val -> {
//                    binding.countryTextView.setText(val.getTitle());
                    country_id = val.getId();
                    select.dismiss();
                });
                select.show(getParentFragmentManager(), getString(R.string.country));
            });
        });

        binding.cityTextView.setOnClickListener(v1 -> {
            Console.logError(currentUser.getCountry().getName());
            if (country_id == null || country_id.isEmpty()) {
                Console.toast(getString(R.string.error_message__choose_country));
            } else {
                checkoutViewModel.getZone(country_id).observe(this.getViewLifecycleOwner(), countries -> {
                    List<SelectAdapter.SelectObj> list = new ArrayList<>();
                    for (Country c : countries) {
                        list.add(new SelectAdapter.SelectObj(c.getName(), c.getId()));
                    }
                    select = new Select(list, getString(R.string.city), val -> {
                        binding.cityTextView.setText(val.getTitle());
                        select.dismiss();
                    });
                    select.show(getParentFragmentManager(), getString(R.string.city));
                });
            }
        });

        binding.editImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        binding.txtChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                    SessionData.getInstance().setMapFrom("EditProfileFragment");
                                    Intent map_intent = new Intent(getActivity(), MapsActivity.class);
                                    startActivity(map_intent);
                                } else {
                                    gpsTracker.showSettingsAlert();
                                }
                            }
                        } else {

                            if (gpsTracker.getIsGPSTrackingEnabled()) {
                                SessionData.getInstance().setMapFrom("EditProfileFragment");
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
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                try {
                    Uri file = data.getData();
                    assert file != null;
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(file);
                    Bitmap image_bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.profileImageView.setImageBitmap(image_bitmap);
                    //TODO: upload
//                    Http http=new Http();
//                    http.uploadBitmap(image_bitmap,getContext());

                    userViewModel.imageUpload(image_bitmap);

                    userViewModel.uploadUserImage(currentUser.getUser_id(), image_bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {

            GPSTrackerService gpsTracker = new GPSTrackerService(getActivity());
            if (!gpsTracker.getIsGPSTrackingEnabled()) {
                gpsTracker.showSettingsAlert();
            } else {
                binding.countryTextView.setText("test " + gpsTracker.getCountryName(getActivity()));
            }
        }
    }

    public void initializeViews() {
        edtCitySelectionView = binding.getRoot().findViewById(R.id.edtCitySelectionView);
        edt_postal_code = binding.getRoot().findViewById(R.id.edt_postal_code);
        edt_user_billing_postal = binding.getRoot().findViewById(R.id.edt_user_billing_postal_);
        edt_user_billing_city_ = binding.getRoot().findViewById(R.id.edt_user_billing_city_);

        if (currentUser.getShipping_address_2() != null) {
            if (currentUser.getShipping_address_2().length() > 0) {
                binding.edtUserShippingAddress2.setText(currentUser.getShipping_address_2());
            }
            if (currentUser.getShipping_city().length() > 0) {
                binding.txtChangeAddress.setText("Change");
                binding.txtMapAddress.setText(currentUser.getShipping_city());
            }
        }
    }

    @SuppressLint("NewApi")
    public void setCurrentAddress() {

        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return;
        } else {
            GPSTrackerService gpsTracker = new GPSTrackerService(getActivity());
            if (!gpsTracker.getIsGPSTrackingEnabled()) {
                gpsTracker.showSettingsAlert();
            } else {
                binding.countryTextView.setText(gpsTracker.getCountryName(getActivity()));
                binding.cityTextView.setText(gpsTracker.getLocality(getActivity()));

                edtCitySelectionView.setText(gpsTracker.getLocality(getActivity()));
                edt_postal_code.setText(gpsTracker.getPostalCode(getActivity()));
                edt_user_billing_city_.setText(gpsTracker.getLocality(getActivity()));
                edt_user_billing_postal.setText(gpsTracker.getPostalCode(getActivity()));
            }
        }
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

    public void retryFunctions() {
        if (saveClickable) {
            saveClickable = false;
            progressDialog.startLoading();
            userViewModel.editProfile(currentUser).observe(this.getViewLifecycleOwner(), apiResponse -> {
                if (apiResponse != null) {
                    saveClickable = true;
                    progressDialog.dismiss();
                    if (apiResponse.getStatus().matches("success")) {
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_edit_profile_to_nav_profile);
                        Console.toast(apiResponse.getMessage());
                    } else {
                        Console.toast(apiResponse.getMessage());
                    }
                }
            });
        }
    }

}
