package com.dvishapps.yourshop.utils;

import android.graphics.Bitmap;
import android.location.Address;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dvishapps.yourshop.databinding.FShopSettingsBinding;
import com.dvishapps.yourshop.ui.CallActivity;
import com.dvishapps.yourshop.ui.layout.checkout.CheckoutResult;
import com.dvishapps.yourshop.ui.layout.order.OrdersAFragment;
import com.dvishapps.yourshop.ui.layout.profile.ShopSettingsFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.dvishapps.yourshop.databinding.CheckoutForm1Binding;
import com.dvishapps.yourshop.databinding.CheckoutForm2Binding;
import com.dvishapps.yourshop.databinding.FEditProfileBinding;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.layout.checkout.CheckoutStep2;
import com.dvishapps.yourshop.ui.layout.order.OrdersTabFragment;
import com.dvishapps.yourshop.ui.layout.order.PendingOrdersFragment;
import com.dvishapps.yourshop.ui.layout.product.CategoryFragment;
import com.dvishapps.yourshop.ui.layout.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class SessionData {
    private static SessionData instance;

    String AuthFrom = "";
    String MapFrom = "";
    FEditProfileBinding fEditProfileBinding;
    ProfileFragment profileFragment;
    OrdersTabFragment ordersTabFragment;

    PendingOrdersFragment pendingOrdersFragment;
    OrdersAFragment ordersAFragment;
    User CurrentUser;
    CategoryFragment categoryFragment;
    private MeowBottomNavigation bottomNavigation;
    int ordersCount;
    Shop shop;

    Address currentAddress;
    CheckoutForm1Binding checkoutForm1Binding;

    TextView txtCurrentLocation;
    TextView txtCurrentLocationAddress;
    TextView txtcurrentLocationMap;

    TextView edtProfileActivityAddressLocality;
    TextInputEditText edtProfileActivityAddress;

    public String shopKeyValue = "";
    public Shop selectedShop;
    public String shopLat = "";
    public String shopLng = "";
    public float overAllTaxValue = 0;
    public float shippingTaxValue = 0;
    public float shopShippingTaxValue = 0;
    public CheckoutForm2Binding checkoutForm2Binding;
    public CheckoutStep2 checkoutStep2;
    private boolean twoWayDelivery = false;
    private boolean initialSignUp = false;
    private boolean initialSignUpCheck = false;

    LatLng latlong = new LatLng(-33.852, 151.211);
    double lat = 0;
    double lng = 0;

    String latitude ="";
    String longitude ="";
    private Bitmap categoryImgBitmap;
    private TextView success_text;

    private boolean fromScan=true;
    List<Shop> shopDetailsListTopChoiceFiltered = new ArrayList<>();
    List<Shop> shopDetailsListAvailableFiltered = new ArrayList<>();
    String LocationText;
    String deliveryStaffId;

    CallActivity callActivity;
    CheckoutResult checkoutResult;
    ProgressBar progressBar;
    String shopPostalCode ="";

    FShopSettingsBinding fShopSettingsBinding;
    ShopSettingsFragment shopSettingsFragment;
    public static SessionData getInstance() {
        if (instance == null) {
            SessionData.instance = new SessionData();
        }
        return instance;
    }

    public void setInstance(SessionData instance) {
        SessionData.instance = instance;
    }

    public OrdersTabFragment getOrdersTabFragment() {
        return ordersTabFragment;
    }

    public void setOrdersTabFragment(OrdersTabFragment ordersTabFragment) {
        this.ordersTabFragment = ordersTabFragment;
    }

    public ProfileFragment getProfileFragment() {
        return profileFragment;
    }

    public void setProfileFragment(ProfileFragment profileFragment) {
        this.profileFragment = profileFragment;
    }

    public User getCurrentUser() {
        return CurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        CurrentUser = currentUser;
    }

    public String getAuthFrom() {
        return AuthFrom;
    }

    public void setAuthFrom(String authFrom) {
        AuthFrom = authFrom;
    }

    public CategoryFragment getCategoryFragment() {
        return categoryFragment;
    }

    public void setCategoryFragment(CategoryFragment categoryFragment) {
        this.categoryFragment = categoryFragment;
    }

    public MeowBottomNavigation getBottomNavigation() {
        return bottomNavigation;
    }

    public void setBottomNavigation(MeowBottomNavigation bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }

    public int getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(int ordersCount) {
        this.ordersCount = ordersCount;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public LatLng getLatlong() {
        return latlong;
    }

    public void setLatlong(LatLng latlong) {
        this.latlong = latlong;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public String getCurrentAddressFormatted() {
        String address = "";
        if (currentAddress != null) {
            if (currentAddress.getSubLocality() != null) {
                if (!currentAddress.getSubLocality().isEmpty())
                    address = currentAddress.getSubLocality();
            }
            if (currentAddress.getLocality() != null) {
                if (!currentAddress.getLocality().isEmpty())
                    if (!address.isEmpty()) {
                        address = address + ", " + currentAddress.getLocality();
                    } else {
                        address = currentAddress.getLocality();

                    }
            }
        }
        return address;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }

    public TextView getTxtCurrentLocation() {
        return txtCurrentLocation;
    }

    public void setTxtCurrentLocation(TextView txtCurrentLocation) {
        this.txtCurrentLocation = txtCurrentLocation;
    }

    public TextView getTxtcurrentLocationMap() {
        return txtcurrentLocationMap;
    }

    public void setTxtcurrentLocationMap(TextView txtcurrentLocationMap) {
        this.txtcurrentLocationMap = txtcurrentLocationMap;
    }

    public TextView getTxtCurrentLocationAddress() {
        return txtCurrentLocationAddress;
    }

    public void setTxtCurrentLocationAddress(TextView txtCurrentLocationAddress) {
        this.txtCurrentLocationAddress = txtCurrentLocationAddress;
    }

    public CheckoutForm1Binding getCheckoutForm1Binding() {
        return checkoutForm1Binding;
    }

    public void setCheckoutForm1Binding(CheckoutForm1Binding checkoutForm1Binding) {
        this.checkoutForm1Binding = checkoutForm1Binding;
    }

    public PendingOrdersFragment getPendingOrdersFragment() {
        return pendingOrdersFragment;
    }

    public void setPendingOrdersFragment(PendingOrdersFragment pendingOrdersFragment) {
        this.pendingOrdersFragment = pendingOrdersFragment;
    }

    public String getMapFrom() {
        return MapFrom;
    }

    public void setMapFrom(String mapFrom) {
        MapFrom = mapFrom;
    }

    public FEditProfileBinding getfEditProfileBinding() {
        return fEditProfileBinding;
    }

    public void setfEditProfileBinding(FEditProfileBinding fEditProfileBinding) {
        this.fEditProfileBinding = fEditProfileBinding;
    }

    public TextView getEdtProfileActivityAddressLocality() {
        return edtProfileActivityAddressLocality;
    }

    public void setEdtProfileActivityAddressLocality(TextView edtProfileActivityAddressLocality) {
        this.edtProfileActivityAddressLocality = edtProfileActivityAddressLocality;
    }

    public TextInputEditText getEdtProfileActivityAddress() {
        return edtProfileActivityAddress;
    }

    public void setEdtProfileActivityAddress(TextInputEditText edtProfileActivityAddress) {
        this.edtProfileActivityAddress = edtProfileActivityAddress;
    }

    public String getShopKeyValue() {
        return shopKeyValue;
    }

    public void setShopKeyValue(String shopKeyValue) {
        this.shopKeyValue = shopKeyValue;
    }

    public boolean isInitialSignUp() {
        return initialSignUp;
    }

    public void setInitialSignUp(boolean initialSignUp) {
        this.initialSignUp = initialSignUp;
    }

    public float getOverAllTaxValue() {
        return overAllTaxValue;
    }

    public void setOverAllTaxValue(float overAllTaxValue) {
        this.overAllTaxValue = overAllTaxValue;
    }

    public float getShippingTaxValue() {

        return shippingTaxValue;
    }

    public void setShippingTaxValue(float shippingTaxValue) {
        this.shippingTaxValue = shippingTaxValue;
    }

    public boolean isInitialSignUpCheck() {
        return initialSignUpCheck;
    }

    public void setInitialSignUpCheck(boolean initialSignUpCheck) {
        this.initialSignUpCheck = initialSignUpCheck;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isTwoWayDelivery() {
        return twoWayDelivery;
    }

    public void setTwoWayDelivery(boolean twoWayDelivery) {
        this.twoWayDelivery = twoWayDelivery;
    }

    public float getShopShippingTaxValue() {
        return shopShippingTaxValue;
    }

    public void setShopShippingTaxValue(float shopShippingTaxValue) {
        this.shopShippingTaxValue = shopShippingTaxValue;
    }

    public String getShopLat() {
        return shopLat;
    }

    public void setShopLat(String shopLat) {
        this.shopLat = shopLat;
    }

    public String getShopLng() {
        return shopLng;
    }

    public void setShopLng(String shopLng) {
        this.shopLng = shopLng;
    }

    public CheckoutForm2Binding getCheckoutForm2Binding() {
        return checkoutForm2Binding;
    }

    public void setCheckoutForm2Binding(CheckoutForm2Binding checkoutForm2Binding) {
        this.checkoutForm2Binding = checkoutForm2Binding;
    }

    public CheckoutStep2 getCheckoutStep2() {
        return checkoutStep2;
    }

    public void setCheckoutStep2(CheckoutStep2 checkoutStep2) {
        this.checkoutStep2 = checkoutStep2;
    }

    public Bitmap getCategoryImgBitmap() {
        return categoryImgBitmap;
    }

    public void setCategoryImgBitmap(Bitmap categoryImgBitmap) {
        this.categoryImgBitmap = categoryImgBitmap;
    }

    public OrdersAFragment getOrdersAFragment() {
        return ordersAFragment;
    }

    public void setOrdersAFragment(OrdersAFragment ordersAFragment) {
        this.ordersAFragment = ordersAFragment;
    }

    public TextView getSuccess_text() {
        return success_text;
    }

    public void setSuccess_text(TextView success_text) {
        this.success_text = success_text;
    }

    public boolean isFromScan() {
        return fromScan;
    }

    public void setFromScan(boolean fromScan) {
        this.fromScan = fromScan;
    }

    public List<Shop> getShopDetailsListTopChoiceFiltered() {
        return shopDetailsListTopChoiceFiltered;
    }

    public void setShopDetailsListTopChoiceFiltered(List<Shop> shopDetailsListTopChoiceFiltered) {
        this.shopDetailsListTopChoiceFiltered = shopDetailsListTopChoiceFiltered;
    }

    public List<Shop> getShopDetailsListAvailableFiltered() {
        return shopDetailsListAvailableFiltered;
    }

    public void setShopDetailsListAvailableFiltered(List<Shop> shopDetailsListAvailableFiltered) {
        this.shopDetailsListAvailableFiltered = shopDetailsListAvailableFiltered;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocationText() {
        return LocationText;
    }

    public void setLocationText(String locationText) {
        LocationText = locationText;
    }

    public Shop getSelectedShop() {
        return selectedShop;
    }

    public void setSelectedShop(Shop selectedShop) {
        this.selectedShop = selectedShop;
    }

    public String getDeliveryStaffId() {
        return deliveryStaffId;
    }

    public void setDeliveryStaffId(String deliveryStaffId) {
        this.deliveryStaffId = deliveryStaffId;
    }

    public CallActivity getCallActivity() {
        return callActivity;
    }

    public void setCallActivity(CallActivity callActivity) {
        this.callActivity = callActivity;
    }

    public CheckoutResult getCheckoutResult() {
        return checkoutResult;
    }

    public void setCheckoutResult(CheckoutResult checkoutResult) {
        this.checkoutResult = checkoutResult;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public String getShopPostalCode() {
        return shopPostalCode;
    }

    public void setShopPostalCode(String shopPostalCode) {
        this.shopPostalCode = shopPostalCode;
    }

    public FShopSettingsBinding getfShopSettingsBinding() {
        return fShopSettingsBinding;
    }

    public void setfShopSettingsBinding(FShopSettingsBinding fShopSettingsBinding) {
        this.fShopSettingsBinding = fShopSettingsBinding;
    }

    public ShopSettingsFragment getShopSettingsFragment() {
        return shopSettingsFragment;
    }

    public void setShopSettingsFragment(ShopSettingsFragment shopSettingsFragment) {
        this.shopSettingsFragment = shopSettingsFragment;
    }
}

