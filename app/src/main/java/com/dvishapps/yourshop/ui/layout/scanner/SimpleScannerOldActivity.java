package com.dvishapps.yourshop.ui.layout.scanner;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.ui.MainActivity;
import com.dvishapps.yourshop.ui.layout.auth.AuthActivity;
import com.dvishapps.yourshop.ui.layout.auth.AuthListener;
import com.dvishapps.yourshop.ui.layout.database.ShopListDb;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class SimpleScannerOldActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 102;
    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;
    int permission = 0;

    private String myurl = "";
    private String shopId = "";
    private SimpleScannerOldActivity resultHandler;
    ShopListDb shopListDb;

    private AuthListener authListener;
    private UserViewModel userViewModel;
    boolean signed = false;
    String task = "";
    private String scannedResult="";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);// Set the scanner view as the content view

        userViewModel = new UserViewModel(SimpleScannerOldActivity.this);
        shopListDb = new ShopListDb(SimpleScannerOldActivity.this);


//        testScanAuth("shopa753df3600de2dac7ec25e7aa7228ee2");// premiere
//        testScanAuth("shop896978b3878e172c4a7822998d203065");//farm

//        scanWithShopId2("shop896978b3878e172c4a7822998d203065");
//        scanWithShopId2("shop01672de80f1b2f0f3fa803b58c23d11d");
//        scanWithShopId2("shop671b23c780d73a75e5a2131068505ef9"); //selvam chettinad/
//        scanWithShopId2("shop14957a69f18e0633e70fc9de0b5add9f"); //kangeyam
//        scanWithShopId2("shop1ae2b8c209340ac0666fe9b13620ee5c"); //amudham
//        scannedResult="shop14957a69f18e0633e70fc9de0b5add9f";

//        permission = getPermissionStatus(this, Manifest.permission.CAMERA);
        if (checkCameraPermission()) {
            //main logic or main code
//            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
//            setContentView(mScannerView);// Set the scanner view as the content view
//        } else if (permission == DENIED) {
////            requestPermission();
//            showMessageOKCancel("You need to allow access permission to use this application",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                requestPermission();
//                            }
//                        }
//                    });
//        } else if (permission == BLOCKED_OR_NEVER_ASKED) {
//            showMessageOKCancel("You should allow access permission to" +
//                            " use this application manually from settings",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                                                    requestPermission();
//                                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
//                            }
//                        }
//                    });
        } else {
            requestPermission();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("SCAN", rawResult.getText()); // Prints scan results
        Log.v("SCAN", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        if (rawResult != null) {
            if (rawResult.getText() != null) {
                scanWithShopId2(rawResult.getText());
                scannedResult=rawResult.getText();
            }
        }
        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    showMessageOKCancel("You need to allow access permission to use this application",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermission();
                                    }
                                }
                            });

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                                != PackageManager.PERMISSION_GRANTED) {
//                            if (getPermissionStatus(SimpleScannerActivity.this, Manifest.permission.CAMERA) == BLOCKED_OR_NEVER_ASKED) {
//                                showMessageOKCancel("You should allow access permission to" +
//                                                " use this application manually from settings",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                                                    requestPermission();
//                                                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
//                                                }
//                                            }
//                                        });
//                            } else if (getPermissionStatus(SimpleScannerActivity.this, Manifest.permission.CAMERA) == DENIED) {
//                                showMessageOKCancel("You need to allow access permission to use this application",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                    requestPermission();
//                                                }
//                                            }
//                                        });
//                            }
//                        }
//                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SimpleScannerOldActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);
    }


    public static int getPermissionStatus(AppCompatActivity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return BLOCKED_OR_NEVER_ASKED;
            }
            return DENIED;
        }
        return GRANTED;
    }

    public void scanWithShopId2(String shop_id) {

        String shop_details_urlCustomtest = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/shops/get/api_key/raviteja/shop_id/" + shop_id;
        myurl = shop_details_urlCustomtest;
        shopId = shop_id;
        new RequestTask().execute();
    }


    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            String responseString = null;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    // Do normal input or output stream reading
                    responseString = shopId;
//                    scannedResult = shopId;

                } else {
                    responseString = null;
//                    response = "FAILED"; // See documentation for more info on response handling
                }
//            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                ScanWebCheck(result);
            } else {
                // If you would like to resume scanning, call this method below:
                mScannerView.resumeCameraPreview(SimpleScannerOldActivity.this);
                if (!Tools.isOnline()){
                    internetSnack(mScannerView,scannedResult);
                }else {
                    Console.toast("Scan a valid shop");
                }
            }
        }
    }

    public void automaticSignUp() {

        userViewModel.email = Config.sharedPreferences.getString(Constants.EMAIL, null);
        userViewModel.password = Config.sharedPreferences.getString(Constants.PASSWORD, null);
        userViewModel.privacy_policy = true;

        userViewModel.error.observeForever(error -> {
            if (task.equals("sign_in")) {

                SessionData.getInstance().setInitialSignUp(true);
                Intent intent = new Intent(SimpleScannerOldActivity.this, AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        });

        userViewModel.success.observeForever(user -> {
            if (task.equals("sign_in")) {
                Config.editPreferences.putString(Constants.LOGGED_USER, JsonUtils.toJsonString(user)).apply();
                Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_KEY, null));
                Intent intent = new Intent(SimpleScannerOldActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        });
        task = "sign_in";
        userViewModel.signIn();

    }

    public void testScanAuth(String result) {
        Config.editPreferences.putString(Constants.SHOP_KEY, result).apply();
        Config.editPreferences.remove(Constants.LOGGED_USER).apply();
        SessionData.getInstance().setShopKeyValue(result);
        DynamicConstants.getInstance().clearAllData();
        Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_KEY, null));

        if (Config.sharedPreferences.getString(Constants.EMAIL, null) != null) {
            automaticSignUp();
        } else {

            Intent intent = new Intent(SimpleScannerOldActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    public void vibrateOnScan() {
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void internetSnack(View parentLayout,String result) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
                            mScannerView.resumeCameraPreview(SimpleScannerOldActivity.this);
//                            ScanWebCheck(result);
                        } else {
                            internetSnack(mScannerView,result);
                        }
                    }
                })
                .setBackgroundTint(parentLayout.getResources().getColor(android.R.color.black ))
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private void ScanWebCheck(String result) {
        vibrateOnScan();

        Config.editPreferences.putString(Constants.SHOP_KEY, result).apply();
        Config.editPreferences.remove(Constants.LOGGED_USER).apply();
        SessionData.getInstance().setShopKeyValue(result);
        DynamicConstants.getInstance().clearAllData();
        Console.logDebug("" + Config.sharedPreferences.getString(Constants.SHOP_KEY, null));

        if (Config.sharedPreferences.getString(Constants.EMAIL, null) != null) {
            automaticSignUp();
        } else {
            Intent intent = new Intent(SimpleScannerOldActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }


    }

}