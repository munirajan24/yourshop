package com.dvishapps.yourshop.api.services;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dvishapps.yourshop.FCM.FCMWorker;
import com.dvishapps.yourshop.ui.layout.home.HomeActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.dvishapps.yourshop.api.ApiResponse;
import com.dvishapps.yourshop.api.httpClient.Http;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.models.Cart;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.models.TestImage;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.StringUtil;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The type User service.
 */
public class UserService {
    private static Type user_type = User.class;
    private static Type user_list_type = new TypeToken<Collection<User>>() {
    }.getType();
    private static Type api_response_type = ApiResponse.class;

    /**
     * Sign in mutable live data.
     *
     * @param phone    the phone
     * @param password the password
     * @return the mutable live data
     */
    @NotNull
    public static MutableLiveData<String> signIn(String phone, String password) {
        MutableLiveData<String> userMutableLiveData = new MutableLiveData<>(null);
        JsonObject body = new JsonObject();
//        body.addProperty("user_email", email);
        body.addProperty("user_password", password);
        body.addProperty("user_phone", phone);
        body.addProperty("shop_id", SessionData.getInstance().getShopKeyValue());
        Console.logDebug(FirebaseInstanceId.getInstance().getToken());
        if (FirebaseInstanceId.getInstance().getToken() != null) {
//            if (FirebaseInstanceId.getInstance().getToken().length() > 0) {
            body.addProperty("device_token", FirebaseInstanceId.getInstance().getToken());
//            }
        } else {
            body.addProperty("device_token", "-");
        }


        Http.<User>post(DynamicConstants.getInstance().sign_in_url, JsonUtils.toJsonString(body), user_type, response -> {
            if (response != null) {
                if (response.getShop_id() != null) {
                    if (response.getShop_id().equals(Config.sharedPreferences.getString(Constants.SHOP_KEY, null))) {
                        Config.currentUser = response;
                        Config.editPreferences.putString(Constants.LOGGED_USER, JsonUtils.toJsonString(response));
                        Config.editPreferences.commit();
                        userMutableLiveData.setValue(JsonUtils.toJsonString(response));

                        if (response.getCountry_id().length() > 1) {
                            Config.editPreferences.putString(Constants.DELIVERY_CHARGE_CALCULATE_AFTER_LOGIN, "TRUE");
                            Config.editPreferences.putString(Constants.LAT, response.getCity_id());
                            Config.editPreferences.putString(Constants.LNG, response.getCountry_id());

                            ShopService.setLatLongAndCalculateDeliveryCharge(SessionData.getInstance().getShop());
                        }

                    } else {
                        userMutableLiveData.setValue("404 error" + "Sorry, User have not registered in this shop");
                    }
                }
                if (Objects.equals(response.getRole_id(), "7")) {
                    ////TODO:Step 2 - Shop Owner setup
                    Config.editPreferences.putString(Constants.SHOP_OWNER, "1").apply(); //TODO: 1-owner

                    if (SessionData.getInstance().getProfileFragment() != null) {
                        try {
                            SessionData.getInstance().getProfileFragment().changeShopSettingVisibility();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //TODO Settings change for background popup
//                    Toast.makeText(Config.context, "You need to set background permission ON\n\n Go to \"Other permissions\" -> \"Start in background\" -> \"accept\"", Toast.LENGTH_LONG).show();
                    Toast.makeText(Config.context, "You need to set background permission ON", Toast.LENGTH_LONG).show();
                    try {
                        //Open the specific App Info page:
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + Config.context.getPackageName()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Config.context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        //Open the generic Apps page:
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Config.context.startActivity(intent);
                    }
//                    //TODO: 2nd Implementation for Call Settings
//                    PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(FCMWorker.class, 15, TimeUnit.MINUTES).build();
//                    WorkManager.getInstance(getA).enqueueUniquePeriodicWork("YourShop", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
//                    if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this))
//                        AutoStartPermissionHelper.getInstance().getAutoStartPermission(this);
//                    power_management();
                } else {
                    Config.editPreferences.putString(Constants.SHOP_OWNER, "0").apply(); //TODO: 0-user
                }
            }


        }, error -> {
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                        userMutableLiveData.setValue("404 error" + jsonObject.getString("message"));
                        //                    Config.editPreferences.clear().apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (error.getMessage() == null) {
                    userMutableLiveData.setValue("404 error" + "UnknownHostException");
                } else {
                    Console.logError(error.getMessage());
                    userMutableLiveData.setValue("404 error" + error.getMessage());
                }
            }
        });

        return userMutableLiveData;
    }

    /**
     * Logged in user mutable live data.
     *
     * @return the mutable live data
     */
    @NotNull
    public static MutableLiveData<User> loggedInUser() {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>(null);
        User currentUser = JsonUtils.fromJsonString(Config.sharedPreferences.getString(Constants.LOGGED_USER, null), User.class);
        userMutableLiveData.setValue(currentUser);
        return userMutableLiveData;
    }

    @NotNull
    public static MutableLiveData<List<User>> getUser(String user_id) {
        MutableLiveData<List<User>> userData = new MutableLiveData<>();
        String url = StringUtil.setString(/*UserApis.user_info_url*/DynamicConstants.getInstance().user_info_url, /*Constants.API_KEY,*/ user_id);
        Http.<List<User>>fetch(url, null, user_list_type, response -> {
            if (response.size() > 0) {

                Config.editPreferences.putString(Constants.LOGGED_USER, JsonUtils.toJsonString(response.get(0))).commit();
                Config.currentUser = response.get(0);
                userData.setValue(response);

                if (response.get(0).getCity_id().length() > 1) {
                    Config.editPreferences.putString(Constants.LAT, response.get(0).getCity_id() + "").apply();
                    Config.editPreferences.putString(Constants.LNG, response.get(0).getCountry_id() + "").apply();
                }
            } else
                userData.setValue(null);
        }, error -> {
            ArrayList<User> userArrayList = new ArrayList<>();
            User user = new User();
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {

                    user.setStatus("404 error" + new String(error.networkResponse.data));
                    userArrayList.add(user);
                    userData.setValue(userArrayList);

                }
            } else {
                if (error.getMessage() == null) {
                    user.setStatus("404 error" + "UnknownHostException");
                    userArrayList.add(user);
                    userData.setValue(userArrayList);
                } else {
                    user.setStatus("404 error" + error.getMessage());
                    userArrayList.add(user);
                    userData.setValue(userArrayList);
                }
            }
        });
        return userData;
    }


    @NotNull
    public static MutableLiveData<User> getDeliveryStaffDetails() {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

        JsonObject body = new JsonObject();
//        body.addProperty("user_email", email);
        body.addProperty("shop_id", SessionData.getInstance().getShopKeyValue());

        Http.<User>post(DynamicConstants.getInstance().delivery_staff_url, JsonUtils.toJsonString(body), user_type, response -> {
            if (response != null) {
                SessionData.getInstance().setDeliveryStaffId(response.getUser_id());
                userMutableLiveData.setValue(response);
            }


        }, error -> {
            User user = new User();
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                        user.setStatus("404 error" + jsonObject.getString("message"));
                        //                    Config.editPreferences.clear().apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (error.getMessage() == null) {
                    user.setStatus("404 error" + "UnknownHostException");
                } else {
                    Console.logError(error.getMessage());
                    user.setStatus("404 error" + error.getMessage());
                }
            }
            userMutableLiveData.setValue(user);

        });
        return userMutableLiveData;
    }

    /**
     * Sign up mutable live data.
     *
     * @param email    the email
     * @param username the username
     * @param password the password
     * @return the mutable live data
     */
    @NotNull
    public static MutableLiveData<String> signUp(String email, String username, String password, String phone) {
        MutableLiveData<String> userMutableLiveData = new MutableLiveData<>(null);
        JsonObject body = new JsonObject();
        body.addProperty("user_email", "");
        body.addProperty("user_password", password);
        body.addProperty("user_name", username);
        body.addProperty("user_phone", phone);
        body.addProperty("shop_id", SessionData.getInstance().getShopKeyValue());

        Console.logDebug(FirebaseInstanceId.getInstance().getToken());
        if (FirebaseInstanceId.getInstance().getToken() != null) {
//            if (FirebaseInstanceId.getInstance().getToken().length() > 0) {
            body.addProperty("device_token", FirebaseInstanceId.getInstance().getToken());
//            }
        } else {
            body.addProperty("device_token", "");
        }

//        JsonArray requestArray = new JsonArray();
//
//        JsonObject objectEmail = new JsonObject();
//
//        objectEmail.addProperty("key", "user_email");
//        objectEmail.addProperty("value", email);
//
//        JsonObject objectPassword = new JsonObject();
//
//        objectPassword.addProperty("key", "user_password");
//        objectPassword.addProperty("value", password);
//
//        JsonObject objectUserName = new JsonObject();
//
//        objectUserName.addProperty("key", "user_name");
//        objectUserName.addProperty("value", username);
//
//        JsonObject objectShopId = new JsonObject();
//
//        objectShopId.addProperty("key", "shop_id");
//        objectShopId.addProperty("value", SessionData.getInstance().getShopKeyValue());
//
//        JsonObject objectDeviceToken = new JsonObject();
//
//        objectDeviceToken.addProperty("key", "device_token");
//        objectDeviceToken.addProperty("value", "123");
//
//        requestArray.add(objectEmail);
//        requestArray.add(objectPassword);
//        requestArray.add(objectUserName);
//        requestArray.add(objectShopId);
//        requestArray.add(objectDeviceToken);

        Http.<User>post(/*UserApis.sign_up_url*/ DynamicConstants.getInstance().sign_up_url, JsonUtils.toJsonString(body), user_type, response -> {
            userMutableLiveData.setValue(JsonUtils.toJsonString(response));
        }, error -> {
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                        userMutableLiveData.setValue("404 error" + jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (error.getMessage() == null) {
                    userMutableLiveData.setValue("404 error" + "UnknownHostException");
                } else {
                    userMutableLiveData.setValue("404 error" + error.getMessage());
                }

            }
        });
        return userMutableLiveData;
    }

    /**
     * Verify mutable live data.
     *
     * @param user_id the user id
     * @param code    the code
     * @return the mutable live data
     */
    @NotNull
    public static MutableLiveData<String> verify(String user_id, String code) {
        MutableLiveData<String> liveData = new MutableLiveData<>(null);
        JsonObject body = new JsonObject();
        body.addProperty("code", code);
        body.addProperty("user_id", user_id);
        body.addProperty("shop_id", SessionData.getInstance().getShopKeyValue());
        Http.<User>post(/*UserApis.sign_up_verify_url*/ DynamicConstants.getInstance().sign_up_verify_url, JsonUtils.toJsonString(body), user_type, response -> {
            liveData.setValue(JsonUtils.toJsonString(response));
        }, error -> {
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                        liveData.setValue("404 error" + jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (error.getMessage() == null) {
                    liveData.setValue("404 error" + "UnknownHostException");
                } else {
                    liveData.setValue("404 error" + error.getMessage());
                }
            }
        });
        return liveData;
    }

    /**
     * Check for verify user mutable live data.
     *
     * @return the mutable live data
     */
    @NotNull
    public static MutableLiveData<Boolean> checkForVerifyUser() {
        MutableLiveData<Boolean> verifyState = new MutableLiveData<>(null);
        User currentUser = JsonUtils.fromJsonString(Config.sharedPreferences.getString(Constants.USER_NEED_VERIFY, null), User.class);
        if (currentUser != null) {
            if ("1".equals(currentUser.getVerify_types())) {
                verifyState.setValue(true);
            } else {
                verifyState.setValue(false);
            }
        } else {
            verifyState.setValue(false);
        }
        return verifyState;
    }

    /**
     * Reset verify code mutable live data.
     *
     * @param user_email the user email
     * @return the mutable live data
     */
    @NotNull
    public static MutableLiveData<ApiResponse> resetVerifyCode(String user_email) {
        MutableLiveData<ApiResponse> response = new MutableLiveData<>(null);
        JsonObject body = new JsonObject();
        body.addProperty("user_email", user_email);
        Http.<ApiResponse>post(UserApis.sign_reset_code_url, JsonUtils.toJsonString(body), api_response_type, response::setValue, error -> {
            response.setValue(new ApiResponse(new String(error.networkResponse.data), new String(error.networkResponse.data)));
            Console.logError(new String(error.networkResponse.data));
        });
        return response;
    }

    /**
     * Sign out mutable live data.
     *
     * @param user_id the user id
     * @return the mutable live data
     */
    public static MutableLiveData<Boolean> signOut(String user_id) {

        return new MutableLiveData<>(false);
    }

    /**
     * Forget password mutable live data.
     *
     * @param user_email the user email
     * @return the mutable live data
     */
    @NotNull
    @Contract(pure = true)
    public static MutableLiveData<ApiResponse> forgetPassword(String user_email) {
        MutableLiveData<ApiResponse> response = new MutableLiveData<>(null);
        JsonObject body = new JsonObject();
//        body.addProperty("user_email", user_email);
        body.addProperty("user_phone", user_email);
        Http.<ApiResponse>post(DynamicConstants.getInstance().forget_password_url, JsonUtils.toJsonString(body), api_response_type, response::setValue, error -> {
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                        response.setValue(new ApiResponse("404 error" + jsonObject.getString("status"), jsonObject.getString("message")));
                    } catch (JSONException e) {
                        response.setValue(new ApiResponse("404 error" + error.getMessage(), error.getMessage()));
                        e.printStackTrace();
                    }
                }
            } else {
                if (error.getMessage() == null) {
                    response.setValue(new ApiResponse("404 error" + "UnknownHostException", "404 error" + "UnknownHostException"));
                } else {
                    response.setValue(new ApiResponse("404 error" + error.getMessage(), error.getMessage()));
                }
            }
        });
        return response;
    }

    /**
     * Sign in fb live data.
     *
     * @return the live data
     */
    public static LiveData<User> signInFb() {
        return new MutableLiveData<>(null);
    }

    /**
     * Sign in google live data.
     *
     * @return the live data
     */
    public static LiveData<User> signInGoogle() {
        return new MutableLiveData<>(null);
    }

    @NotNull
    public static LiveData<ApiResponse> editProfile(User user) {
        MutableLiveData<ApiResponse> data = new MutableLiveData<>();
        Console.logError(JsonUtils.toJsonString(user));
        Http.<ApiResponse>post(/*UserApis.edit_user_info_url*/DynamicConstants.getInstance().edit_user_info_url, JsonUtils.toJsonString(user), api_response_type, value -> data.setValue(value), error -> {
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                        data.setValue(new ApiResponse(jsonObject.getString("status"), jsonObject.getString("message")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (error.getMessage() == null) {
                    data.setValue(new ApiResponse("404 error" + "UnknownHostException", "404 error" + "UnknownHostException"));
                } else {
                    data.setValue(new ApiResponse("404 error" + error.getMessage(), error.getMessage()));
                }
            }

        });

        return data;
    }

    @NotNull
    @Contract(" -> new")
    public static LiveData<Boolean> signOut() {
        Config.currentUser = null;
        Config.editPreferences.remove(Constants.LOGGED_USER);
        Config.editPreferences.remove(Constants.SHOP_OWNER);
        Config.editPreferences.remove(Constants.CART);
        Config.cart = new Cart();
        Config.editPreferences.putString(Constants.CART, JsonUtils.toJsonString(Config.cart));
        Config.editPreferences.apply();
        if (Config.editPreferences.commit())
            return new MutableLiveData<>(true);
        else return new MutableLiveData<>(false);
    }

    @NotNull
    @Contract(" -> new")
    public static LiveData<Boolean> removeShop() {
        Config.currentUser = null;
        Config.editPreferences.remove(Constants.LOGGED_USER);
        Config.editPreferences.remove(Constants.SHOP_KEY);
        Config.editPreferences.remove(Constants.SHOP_MAIN_BRANCH_KEY);
        Config.editPreferences.remove(Constants.SHOP_OWNER);
        Config.editPreferences.remove(Constants.CART);
        Config.cart = new Cart();
        Config.editPreferences.putString(Constants.CART, JsonUtils.toJsonString(Config.cart));

        DynamicConstants.getInstance().clearAllData();

        if (Config.editPreferences.commit())
            return new MutableLiveData<>(true);
        else return new MutableLiveData<>(false);
    }

    @NotNull
    public static LiveData<ApiResponse> changePwd(String user_id, String pwd, String confirmPwd) {
        MutableLiveData<ApiResponse> response = new MutableLiveData<>();
        JsonObject body = new JsonObject();
        body.addProperty("user_id", user_id);
        body.addProperty("user_password", pwd);
        Http.<ApiResponse>post(/*UserApis.update_password_url*/ DynamicConstants.getInstance().update_password_url, JsonUtils.toJsonString(body), api_response_type, response::setValue, error -> {
            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.ERROR_404) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                        response.setValue(new ApiResponse(jsonObject.getString("status"), jsonObject.getString("message")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (error.getMessage() == null) {
                        response.setValue(new ApiResponse("404 error" + "UnknownHostException", "404 error" + "UnknownHostException"));
                    } else {
                        response.setValue(new ApiResponse(error.getMessage(), error.getMessage()));
                    }
                }
            }
        });
        return response;
    }


    @NotNull
    public static MutableLiveData<Shop> imageUpload(Bitmap bitmap) {
        MutableLiveData<Shop> response = new MutableLiveData<>();
        String url = "https://postman-echo.com/post";
        Http.<TestImage>postImage(/*ShopApis.shop_details_url*/ url, bitmap, /*null, testImage_type,*/ shopResponse -> {
            TestImage testImage = new TestImage();
            Console.logDebug("testImage :" + testImage + "");
        }, error -> {

            JSONObject jsonObject = null;
            Shop shop = new Shop();
            if (error.networkResponse != null) {
                try {
                    jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
                            + jsonObject.getString("message"));

                    shop.setStatus("404 error" + jsonObject.getString("message"));
                    response.setValue(shop);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (error.getMessage() == null) {
                    shop.setStatus("404 error" + "UnknownHostException");
                } else {
                    shop.setStatus("404 error" + error.getMessage());
                }
                response.setValue(shop);
            }

            Log.e("getShopDetails2", "getShopDetails2: ", error);
        });
        return response;
    }

//    @NotNull
//    public static MutableLiveData<Shop> postImageWithData(Bitmap bitmap) {
//        MutableLiveData<Shop> response = new MutableLiveData<>();
//        String url = DynamicConstants.HTTP_SERVER_URL+"/index.php/rest/categories/add/api_key/raviteja";
//        Http.<TestImage>postImageWithData(/*ShopApis.shop_details_url*/ url, bitmap, /*null, testImage_type,*/ shopResponse -> {
//            try {
//                JSONObject obj = new JSONObject(new String(shopResponse.data));
//                Console.logDebug(obj.getString("id"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            TestImage testImage = new TestImage();
//            Console.logDebug("testImage :" + shopResponse + "");
//        }, error -> {
//
//            JSONObject jsonObject = null;
//            Shop shop = new Shop();
//            if (error.networkResponse != null) {
//                try {
//                    jsonObject = new JSONObject(new String(error.networkResponse.data));
//                    Console.logError(" getShopDetails2 error : " + error.networkResponse.statusCode + " "
//                            + jsonObject.getString("message"));
//
//                    shop.setStatus("404 error" + jsonObject.getString("message"));
//                    response.setValue(shop);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                if (error.getMessage() == null) {
//                    shop.setStatus("404 error" + "UnknownHostException");
//                } else {
//                    shop.setStatus("404 error" + error.getMessage());
//                }
//                response.setValue(shop);
//            }
//
//            Log.e("getShopDetails2", "getShopDetails2: ", error);
//        });
//        return response;
//    }


    public static void uploadUserImage(String user_id, Bitmap image, String platform) {
        JsonObject body = new JsonObject();
//
//        File file = new File(filePath);
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part bodyFile =
//                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//
//        RequestBody fullName =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), file.getName());
//
//        RequestBody platformRB =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), platform);
//
//        RequestBody useIdRB =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), user_id);
//
        MutableLiveData<User> response = new MutableLiveData<>();
        body.addProperty("user_id", user_id);
        body.addProperty("file", imageToString(image));
        body.addProperty("platform_name", "");
        Http.post(/*UserApis.upload_image_url*/DynamicConstants.getInstance().upload_image_url, JsonUtils.toJsonString(body), api_response_type, resp -> {
            Console.logError(resp.toString());
        }, error -> {
            error.printStackTrace();
//            if (error.networkResponse.statusCode == Constants.ERROR_404) {
//                try {
//                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
//                    Console.log(jsonObject.getString("message"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
        });
    }


    public static String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageByte = outputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    /**
     * The type User apis.
     */
    public static class UserApis {
        /**
         * The constant sign_in_url.
         */
        public static String sign_in_url = StringUtil.setString("%s/rest/users/login/api_key/%s", Constants.HTTP, Constants.API_KEY);
        /**
         * The constant sign_up_url.
         */
        public static String sign_up_url = StringUtil.setString("%s/rest/users/add/api_key/%s", Constants.HTTP, Constants.API_KEY);
//        http://www.dvishapps.com/arabian/index.php/rest/users/add/api_key/raviteja
//        http://www.dvishapps.com/arabian/index.php/rest/users/verify/api_key/raviteja
        /**
         * The constant sign_up_verify_url.
         */
        public static String sign_up_verify_url = StringUtil.setString("%s/rest/users/verify/api_key/%s", Constants.HTTP, Constants.API_KEY);
        /**
         * The constant sign_reset_code_url.
         */
        public static String sign_reset_code_url = StringUtil.setString("%s/rest/users/request_code/api_key/%s", Constants.HTTP, Constants.API_KEY);
        /**
         * The constant forget_password_url.
         */
        public static String forget_password_url = StringUtil.setString("%s/rest/users/reset/api_key/%s", Constants.HTTP, Constants.API_KEY);
        /**
         * The constant sign_up_register_url.
         */
        public static String sign_up_register_url = StringUtil.setString("%s/rest/users/register/api_key/%s", Constants.HTTP, Constants.API_KEY);
        /**
         * The constant sign_out_url.
         */
        public static String sign_out_url = "";
        /**
         * The constant sign_in_fb_url.
         */
        public static String sign_in_fb_url = "";
        /**
         * The constant sign_in_google_url.
         */
        public static String sign_in_google_url = "";

//        public static String user_info_url = "http://www.dvishapps.com/shiva/index.php/rest/users/get/api_key/%s/user_id/%s";

        public static String user_info_url = Constants.HTTP + "/rest/users/get/api_key/%s/user_id/%s";

        public static String edit_user_info_url = StringUtil.setString("%s/rest/users/profile_update/api_key/%s", Constants.HTTP, Constants.API_KEY);

        public static String update_password_url = StringUtil.setString("%s/rest/users/password_update/api_key/%s", Constants.HTTP, Constants.API_KEY);

        public static String upload_image_url = StringUtil.setString("%s/rest/images/upload/api_key/%s", Constants.HTTP, Constants.API_KEY);
    }
}
