package com.dvishapps.yourshop.ui.layout.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.utils.SessionData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import static com.dvishapps.yourshop.ui.layout.home.HomeActivity.PLACE_AUTOCOMPLETE_STRING_REQUEST_CODE;

public class LocationSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        getSupportActionBar().hide();

        searchLoc();
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

        autocompleteFragment.setText(SessionData.getInstance().getLocationText());

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

                Intent resultIntent = new Intent();
                resultIntent.putExtra(PLACE_AUTOCOMPLETE_STRING_REQUEST_CODE, latLong.latitude+","+latLong.longitude+","+place.getName());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                if (ActivityCompat.checkSelfPermission(LocationSearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationSearchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }


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

                finish();
            }


        });

    }

}