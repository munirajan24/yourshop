package com.dvishapps.yourshop.ui.layout.map;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
//import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.services.GPSTrackerService;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    Context mContext;
    TextView mLocationMarkerText;
    private LatLng mCenterLatLong;

    GPSTrackerService gpsTrackerService;
    Location location;
    Button btn_confirm_location;
    Button btn_confirm_location_disabled;

    TextView txt_change_address, txt_map_address;

    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
//    private AddressResultReceiver mResultReceiver;
    /**
     * The formatted location address.
     */
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    EditText mLocationAddress;
    TextView mLocationText;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    Toolbar mToolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AddressListAdapter adapter;
    private List<Address> addressList;
    PlacesAutocompleteTextView placesAutocomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mContext = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mLocationMarkerText = (TextView) findViewById(R.id.locationMarkertext);
        txt_change_address = (TextView) findViewById(R.id.txt_change_address);
        txt_map_address = (TextView) findViewById(R.id.txt_map_address);
        mLocationAddress = (EditText) findViewById(R.id.Address);
        mLocationText = (TextView) findViewById(R.id.Locality);
        placesAutocomplete = (PlacesAutocompleteTextView) findViewById(R.id.places_autocomplete);
        btn_confirm_location = (Button) findViewById(R.id.btn_confirm_location);
        btn_confirm_location_disabled = (Button) findViewById(R.id.btn_confirm_location_disabled);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        SessionData.getInstance().setTxtcurrentLocationMap(txt_map_address);

        setSupportActionBar(mToolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_address_list);
        layoutManager = new LinearLayoutManager(MapsActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AddressListAdapter(addressList, getApplicationContext());
        recyclerView.setAdapter(adapter);

        gpsTrackerService = new GPSTrackerService(this);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));


        mLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAutocompleteActivity();

                gpsTrackerService.getGeocoderAddress(MapsActivity.this, location);

            }


        });

        btn_confirm_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAddress();

                finish();
            }
        });
        txt_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent map_intent = new Intent(MapsActivity.this, SearchLocationActivity.class);
//                map_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(map_intent);
//                finish();
            }
        });

        placesAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull com.seatgeek.placesautocomplete.model.Place place) {

            }
        });


        mapFragment.getMapAsync(this);
//        mResultReceiver = new AddressResultReceiver(new Handler());

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
//            if (!AppUtils.isLocationEnabled(mContext)) {
//                // notify user
//                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//                dialog.setMessage("Location not enabled!");
//                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(myIntent);
//                    }
//                });
//                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//                dialog.show();
//            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }

        searchLoc();
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;


                mMap.clear();

                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

//                    startIntentService(mLocation);
//                    mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);
                    location = mLocation;
                    if (addressList != null) {
                        addressList.clear();
                    }
                    addressList = gpsTrackerService.getGeocoderAddress(MapsActivity.this, mLocation);

                    setAddress(addressList);//TODO: Master function


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            changeMap(mLastLocation);
            Log.d(TAG, "ON connected");

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                changeMap(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }

    private void changeMap(Location location) {

        Log.d(TAG, "Reaching map" + mMap);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(19f).tilt(70).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            List<Address> addressList = gpsTrackerService.getGeocoderAddress(this, location);
            if (addressList != null) {
                if (addressList.size() > 0) {
                    mLocationMarkerText.setText("" + addressList.get(0).getAddressLine(0));
                } else {
                    mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
                }
            } else {
                if (!Tools.isOnline()) {
                    Console.toast("No internet Connection");
                    finish();
                }
            }
//            startIntentService(location);


        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
//    class AddressResultReceiver extends ResultReceiver {
//        public AddressResultReceiver(Handler handler) {
//            super(handler);
//        }
//
//        /**
//         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
//         */
//        @Override
//        protected void onReceiveResult(int resultCode, Bundle resultData) {
//
//            // Display the address string or an error message sent from the intent service.
//            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);
//
//            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);
//
//            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
//            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);
//
//            displayAddressOutput();
//
//            // Show a toast message if an address was found.
//            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
//                //  showToast(getString(R.string.address_found));
//
//
//            }
//
//
//        }
//
//    }

    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput() {
        //  mLocationAddressTextView.setText(mAddressOutput);
        try {
            if (mAreaOutput != null)
                // mLocationText.setText(mAreaOutput+ "");

                mLocationAddress.setText(mAddressOutput);
            //mLocationText.setText(mAreaOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */

    public void getCurrentLocation() {
        GPSTrackerService gpsTrackerService = new GPSTrackerService(this);
        Console.toast("" + gpsTrackerService.getLatitude() + gpsTrackerService.getLongitude());

    }
//    protected void startIntentService(Location mLocation) {
//        // Create an intent for passing to the intent service responsible for fetching the address.
//        Intent intent = new Intent(this, FetchAddressIntentService.class);
//
//        // Pass the result receiver as an extra to the service.
//        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);
//
//        // Pass the location data as an extra to the service.
//        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);
//
//        // Start the service. If the service isn't already running, it is instantiated and started
//        // (creating a process for it if needed); if it is running then it remains running. The
//        // service kills itself automatically once all intents are processed.
//        startService(intent);
//    }


    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(MapsActivity.this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(mContext, data);

                // TODO call location based filter


                LatLng latLong;


                latLong = place.getLatLng();

                //mLocationText.setText(place.getName() + "");

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(19f).tilt(70).build();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));


            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(mContext, data);
        } else if (resultCode == RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }
    }

    public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.PlanetViewHolder> {

        List<Address> planetList;

        public AddressListAdapter(List<Address> planetList, Context context) {
            this.planetList = planetList;
        }

        @Override
        public AddressListAdapter.PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.planet_row, parent, false);
            PlanetViewHolder viewHolder = new PlanetViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(AddressListAdapter.PlanetViewHolder holder, int position) {
            holder.text.setText(planetList.get(position).getAddressLine(0).toString());
        }

        @Override
        public int getItemCount() {
            return planetList.size();
        }

        public class PlanetViewHolder extends RecyclerView.ViewHolder {

            protected TextView text;

            public PlanetViewHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.txt_address);
            }
        }
    }

    private void confirmAddress() {
        if (addressList.size() > 0) {
//                        Config.editPreferences.putString(Constants.LAT, gpsTrackerService.getLatitude() + "").apply();
//                        Config.editPreferences.putString(Constants.LNG, gpsTrackerService.getLongitude() + "").apply();

//            mLocationMarkerText.setText("" + addressList.get(0).getAddressLine(0));
//            SessionData.getInstance().setCurrentAddress(addressList.get(0));


            if (SessionData.getInstance().getMapFrom().equalsIgnoreCase("EditProfileActivity")) {
                SessionData.getInstance().getCurrentUser().setCity_id(addressList.get(0).getLatitude() + "");
                SessionData.getInstance().getCurrentUser().setCountry_id(addressList.get(0).getLongitude() + "");

                SessionData.getInstance().getCurrentUser().setShipping_city(SessionData.getInstance().getCurrentAddressFormatted());
                SessionData.getInstance().getCurrentUser().setShipping_address_1(addressList.get(0).getAddressLine(0));
                SessionData.getInstance().getEdtProfileActivityAddressLocality().setText(SessionData.getInstance().getCurrentAddressFormatted());
                SessionData.getInstance().getEdtProfileActivityAddress().setText(addressList.get(0).getAddressLine(0));

                SessionData.getInstance().getCurrentUser().setShipping_city(SessionData.getInstance().getCurrentAddressFormatted());

                SessionData.getInstance().getCurrentUser().setShipping_address_1(addressList.get(0).getAddressLine(0));
                SessionData.getInstance().getCheckoutForm1Binding().txtMapAddress.setText(SessionData.getInstance().getCurrentAddressFormatted());
                SessionData.getInstance().getCheckoutForm1Binding().edtCitySelectionView.setText(SessionData.getInstance().getCurrentAddressFormatted());
                SessionData.getInstance().getCheckoutForm1Binding().address1Input.setText(addressList.get(0).getAddressLine(0));
                SessionData.getInstance().getCheckoutForm1Binding().postalInput.setText(addressList.get(0).getPostalCode());
                SessionData.getInstance().getCheckoutForm1Binding().stateInput.setText(addressList.get(0).getAdminArea());
                SessionData.getInstance().getCheckoutForm1Binding().countryTextView.setText(addressList.get(0).getCountryName());

            } else if (SessionData.getInstance().getMapFrom().equalsIgnoreCase("EditProfileFragment")) {
                SessionData.getInstance().getCurrentUser().setCity_id(addressList.get(0).getLatitude() + "");
                SessionData.getInstance().getCurrentUser().setCountry_id(addressList.get(0).getLongitude() + "");

                SessionData.getInstance().getCurrentUser().setShipping_city(SessionData.getInstance().getCurrentAddressFormatted());
                SessionData.getInstance().getCurrentUser().setShipping_address_1(addressList.get(0).getAddressLine(0));
                SessionData.getInstance().getfEditProfileBinding().txtMapAddress.setText(SessionData.getInstance().getCurrentAddressFormatted());
                SessionData.getInstance().getfEditProfileBinding().userShippingAddress1.setText(addressList.get(0).getAddressLine(0));
//                            SessionData.getInstance().getfEditProfileBinding().edtUserShippingAddress1.setText(addressList.get(0).getAddressLine(0));
                SessionData.getInstance().getfEditProfileBinding().edtPostalCode.setText(addressList.get(0).getPostalCode());
                SessionData.getInstance().getfEditProfileBinding().edtCitySelectionView.setText(SessionData.getInstance().getCurrentAddressFormatted());
                SessionData.getInstance().getfEditProfileBinding().userShippingState.setText(addressList.get(0).getAdminArea());
                SessionData.getInstance().getfEditProfileBinding().countryTextView.setText(addressList.get(0).getCountryName());


            } else if (SessionData.getInstance().getMapFrom().equalsIgnoreCase("ShopSettingsFragment")) {


                SessionData.getInstance().getfShopSettingsBinding().shopLocation.setText(addressList.get(0).getAddressLine(0));

                SessionData.getInstance().getShopSettingsFragment().shopDetails.setAddress2(addressList.get(0).getAddressLine(0));
                SessionData.getInstance().getShopSettingsFragment().shopDetails.setLat(addressList.get(0).getLatitude() + "");
                SessionData.getInstance().getShopSettingsFragment().shopDetails.setLng(addressList.get(0).getLongitude() + "");
                SessionData.getInstance().getShopSettingsFragment().shopDetails.setPostal_code(addressList.get(0).getPostalCode());

                Config.editPreferences.putString(Constants.LAT, addressList.get(0).getLatitude() + "").apply();
                Config.editPreferences.putString(Constants.LNG, addressList.get(0).getLatitude() + "").apply();

                SessionData.getInstance().getShopSettingsFragment().setLocationClickable = true;


            } else {
                SessionData.getInstance().getCurrentUser().setShipping_city(SessionData.getInstance().getCurrentAddressFormatted());

                SessionData.getInstance().getCurrentUser().setShipping_address_1(addressList.get(0).getAddressLine(0));
                SessionData.getInstance().getCheckoutForm1Binding().txtMapAddress.setText(SessionData.getInstance().getCurrentAddressFormatted());
                SessionData.getInstance().getCheckoutForm1Binding().address1Input.setText(addressList.get(0).getAddressLine(0));
                SessionData.getInstance().getCheckoutForm1Binding().postalInput.setText(addressList.get(0).getPostalCode());
                SessionData.getInstance().getCheckoutForm1Binding().edtCitySelectionView.setText(SessionData.getInstance().getCurrentAddressFormatted());
                SessionData.getInstance().getCheckoutForm1Binding().stateInput.setText(addressList.get(0).getAdminArea());
                SessionData.getInstance().getCheckoutForm1Binding().countryTextView.setText(addressList.get(0).getCountryName());
            }


//                        SessionData.getInstance().getCheckoutForm1Binding().getUser().setShipping_address_1(addressList.get(0).getAddressLine(0));
//                        SessionData.getInstance().getCheckoutForm1Binding().getUser().setShipping_country(addressList.get(0).getCountryName());
//                        SessionData.getInstance().getCheckoutForm1Binding().getUser().setShipping_city(addressList.get(0).getLocality());
//                        SessionData.getInstance().getCheckoutForm1Binding().getUser().setShipping_state(addressList.get(0).getAdminArea());
//                        SessionData.getInstance().getCheckoutForm1Binding().getUser().setShipping_postal_code(addressList.get(0).getPostalCode());

//            txt_map_address.setText(SessionData.getInstance().getCurrentAddressFormatted());
        } else {
//                        mLocationMarkerText.setText("Lat : " + mLocation.getLatitude() + "," + "Long : " + mLocation.getLongitude());
        }
    }


    public void confirmButtonEnable(boolean enable) {
        if (enable) {
            btn_confirm_location.setVisibility(View.VISIBLE);
            btn_confirm_location_disabled.setVisibility(View.GONE);
        } else {
            btn_confirm_location.setVisibility(View.GONE);
            btn_confirm_location_disabled.setVisibility(View.VISIBLE);
        }
    }

    public void setAddress(List<Address> addressList) {
        if (addressList.size() > 0) {
            mLocationMarkerText.setText("" + addressList.get(0).getAddressLine(0));
            SessionData.getInstance().setCurrentAddress(addressList.get(0));
            txt_map_address.setText(SessionData.getInstance().getCurrentAddressFormatted());
            confirmButtonEnable(true);
        }
    }


    public void searchLoc() {
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);

        }

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);


        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


//        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        autocompleteFragment.setCountry("IN");

        autocompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(com.google.android.libraries.places.api.model.Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getLatLng());
//                Console.toast("Place: " + place.getName() + ", " + place.getLatLng());

                LatLng latLong;


                latLong = place.getLatLng();

                //mLocationText.setText(place.getName() + "");

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(19f).tilt(70).build();

                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

//                List<Address> addressList = gpsTrackerService.getGeocoderAddress(MapsActivity.this, location);
//                if (addressList != null) {
//                    if (addressList.size() > 0) {
//                        mLocationMarkerText.setText("" + addressList.get(0).getAddressLine(0));
//                    } else {
//                        mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
//                    }
//                } else {
//                    if (!Tools.isOnline()) {
//                        Console.toast("No internet Connection");
//                        finish();
//                    }
//                }

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

    }


}