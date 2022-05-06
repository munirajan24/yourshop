package com.dvishapps.yourshop.ui.layout.product;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dvishapps.yourshop.BuildConfig;
import com.dvishapps.yourshop.FCM.FCMWorker;
import com.dvishapps.yourshop.utils.DynamicConstants;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.api.services.GPSTrackerService;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.FCategoryBinding;
import com.dvishapps.yourshop.models.Category;
import com.dvishapps.yourshop.models.Photo;
import com.dvishapps.yourshop.models.Product;
import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.ui.MainActivity;
import com.dvishapps.yourshop.ui.adapters.RecycleAdapter;
import com.dvishapps.yourshop.ui.adapters.SlideShowAdapter;
import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleListener;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleLongClickListener;
import com.dvishapps.yourshop.ui.layout.auth.AuthActivity;
import com.dvishapps.yourshop.ui.layout.database.ShopListDb;
import com.dvishapps.yourshop.ui.viewModel.ProductViewModel;
import com.dvishapps.yourshop.ui.viewModel.ShopViewModel;
import com.dvishapps.yourshop.ui.viewModel.TransactionViewModel;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.StringUtil;
import com.dvishapps.yourshop.utils.Tools;
import com.google.gson.JsonObject;
import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.POWER_SERVICE;
import static com.dvishapps.yourshop.app.Config.CALL_BACKGROUND_SETTINGS;

public class CategoryFragment extends SFragment implements OnRecycleListener, OnRecycleLongClickListener {
    private static final int PICK_IMAGE = 102;
    private static final int PICK_IMAGE_UPDATE = 1023;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 23;
    private FCategoryBinding categoryBinding;
    private ProductViewModel productViewModel;
    private RecycleAdapter categoryAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    private ShimmerFrameLayout mShimmerViewContainerSlide;

    private ShopViewModel shopViewModel;
    private Shop shop;

    private UserViewModel userViewModel;
    private TransactionViewModel orderViewModel;
    public int ordersCount = 0;
    private boolean sec = false;
    private List<Transaction> transactionListFiltered;

    private SlideShowAdapter fProductsListViewAdapter;
    private List<Photo> products_slideShow = new ArrayList<>();
    private List<Product> featuredProducts_slideShow = new ArrayList<>();
    public final MutableLiveData<List<Product>> featuredProducts_liveData = new MutableLiveData<>();

    private ShopListDb shopListDb;

    String shopId;
    private Bitmap categoryImgBitmap;
    public ProgressDialog progressDialog;
    public ProgressDialog progressDialogEdit;
    private Dialog dialog;
    private Dialog dialogEdit;
    private CircleImageView categoryImageView;
    private EditText edtCategoryName;

    private RadioButton radioSubcategoryNeededYes;
    private RadioButton radioSubcategoryNeededNo;
    private RadioButton radio_status_show;
    private RadioButton radio_status_hide;
    private RadioButton radio_available_yes;
    private RadioButton radio_available_no;

    RecycleItem category;

    private ProgressDialog progressDialogFetchShop;
    private String status = "1";
    private String is_enable = "1";
    private String closeTime = "";
    private String openTime = "";

    String openTimeText = "";
    String closeTimeText = "";
    private EditText edtOpenTime;
    private EditText edtCloseTime;
    private boolean shopStatusOpen = true;


    @NotNull
    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///TODO:Step1 - Individual shop setup

//        setIndividualShopPreference("shopa6f642973a95fb87aa74c333c484da2a");// Blue Wave
//        setIndividualShopPreference("shopa753df3600de2dac7ec25e7aa7228ee2");// premier
//        setIndividualShopPreference("shop88e9107210cd853467b062c92fe85ead");// Akshaya Park Restaurant
//        setIndividualShopPreference("shop9d9fa7b7510b4d5211f6ae1a41a51c9c");// rhr
//        setIndividualShopPreference("shop8ccaa51728297b03d6ff966e30ad0f4a");// SRKP Chettinad Mess
//        setIndividualShopPreference("shopfbafffb733e5dc5093be448e989bb130");// A1 chips

        //un paid
//        setIndividualShopPreference("shop3a0ac42b68b0ca5dea4cd184f4c35b30");// Durai's Super Market
//        setIndividualShopPreference("shopa8c28f7d4036638835753d95f042b32e");// Sivakannan Restaurant
//        setIndividualShopPreference("shop288210b2b1a545cd0b681dd43af1a2be");// Johan Sea Foods
//        setIndividualShopPreference("shop7877e758e5f291920d3871cffbb94b41");// Arusuvai Restaurant

//        setIndividualShopPreference("shop195ebafe01167cb4b1e1cce7ed6584b5");// Ifthar Multicuisine Restaurant
//        setIndividualShopPreference("shopea967edfb367bc0a243cd1e62e748557");//Kovai President Biriyani
//        setIndividualShopPreference("shope13b6e4eac77ba98ef7b11cbc3311bc8");// Burma bhai
//        setIndividualShopPreference("shop0a07f190766445d7facb7bd33eba77fb");// Gandhipuram Original Lala Sweets
//        setIndividualShopPreference("shop22434955996c0228341f05de7643f712");// Sai Samboorna Hotel

//        setIndividualShopPreference("shop446e2e5099ec5a8b5c584ef2797ace0b");// Thandav's Multi Cuisine Restaurant

        //        setIndividualShopPreference("shop3732dc44ccf3c1f94885b3483fb14910");// thesquarekitchen
//        setIndividualShopPreference("shop26cfb9abb2bb510f455548920b38228d");// burgersquare
//        setIndividualShopPreference("shopa6d2d8450dc057baeadfa4f842b39a78");// Lakshmi Mess
//        setIndividualShopPreference("shop096d3bd1222bf6774db756894ec25fb3");// Chennais Biriyani House - R S Puram

//        setIndividualShopPreference("shopc7cd34bc9ff079aa96970146cbd76e0c");// Kootansoru
//        setIndividualShopPreference("shopa0d11f797dd7df368036373ca42af9bf");// A1  Restaurant
//        setIndividualShopPreference("shopc19a738cd483ec464a314bf22654c4c9");// M H R Biriyani house
//        setIndividualShopPreference("shop6833085a2747c9ea76496d2b03164f1e");// Ancient Village Restaurant

//        setIndividualShopPreference("shop7e51daa6f2f0538fa45b18a92cd148dc");//Hotel Yours
//        setIndividualShopPreference("shopa6d2d8450dc057baeadfa4f842b39a78");//Lakshmi mess
//        setIndividualShopPreference("shop6bfcf13985e316e394b1f50d991edf1f");//Bright Multi Services`
//        setIndividualShopPreference("shop3fc082058d262b3b6a648a2ea28a9c23");//al maas biriyani house
//        setIndividualShopPreference("shop18689e7fdc6ab30f69035fcac26cbda4");//shree aksshayam
//        setIndividualShopPreference("shop5d8c3468b4e124e978ac2d06fae14991");//hotel maharaja kidavirunthu


//        setIndividualShopPreference("shop6ede4d023cb8028bfd39acce4b590c57");// Test shop

        ///TODO:Step 2 - Shop Owner setup
//        Config.editPreferences.putString(Constants.SHOP_OWNER, "0").apply(); //TODO: 0-user
//        Config.editPreferences.putString(Constants.SHOP_OWNER, "1").apply(); //TODO: 1-owner

        ///TODO: then it will change URLS
        SessionData.getInstance().setShopKeyValue(Config.sharedPreferences.getString(Constants.SHOP_KEY, null));

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        if (SessionData.getInstance().getShopKeyValue() != null) {
            productViewModel.fetchCategories();
            productViewModel.fetchFeaturedProducts();
        } else {
//            Console.toast("shop id Error");
        }

        progressDialog = new ProgressDialog(getActivity(), "Creating category");
        progressDialogEdit = new ProgressDialog(getActivity(), "Updating category");
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.fetchShop();

        orderViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        userViewModel = new UserViewModel(this.getActivity());
        transactionListFiltered = new ArrayList<>();
        SessionData.getInstance().setCategoryFragment(this);

        callSettingsCheck();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        categoryBinding = DataBindingUtil.inflate(inflater, R.layout.f_category, container, false);
        changeOwnerVisibility();


        if (Config.sharedPreferences.getString(Constants.SHOP_KEY, null) != null) {
            SessionData.getInstance().setShopKeyValue(Config.sharedPreferences.getString(Constants.SHOP_KEY, null));
        } else {
            Console.toast(" Fetching Failed");
        }
        shopListDb = new ShopListDb(getActivity());
        init();
        setUpOrdersCount();

        webCallFunctions();

        productViewModel.getRecycleItems().observe(this.getViewLifecycleOwner(), recycleItems -> {
            categoryAdapter.setItems(recycleItems);
//            ViewUtils.removeView(categoryBinding.pb);

            categoryBinding.pb.setVisibility(View.INVISIBLE);
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);

        });

        featuredProducts_liveData.observe(this.getViewLifecycleOwner(), productList -> {
            if (featuredProducts_slideShow.size() == 0) {
                noSlideViewSetUp();
            }
        });

        categoryBinding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                categoryAdapter.getFilter().filter(s);
            }
        });

//        categoryBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                productViewModel.createCategory();
//            }
//        });

        return categoryBinding.getRoot();
    }

    private void setOwnerButtonsVisible(boolean visibility) {
        if (visibility) {
            categoryBinding.lnrAdd.setVisibility(View.VISIBLE);
        } else {
            categoryBinding.lnrAdd.setVisibility(View.GONE);
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
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(file);
                    Bitmap categoryImageBitmap = BitmapFactory.decodeStream(inputStream);

                    categoryImageBitmap = Tools.imageReSize(categoryImageBitmap, 100);

                    categoryBinding.categoryImageView.setImageBitmap(categoryImageBitmap);
                    //TODO: upload
                    categoryImgBitmap = categoryImageBitmap;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_IMAGE_UPDATE) {
            if (data != null) {
                try {
                    Uri file = data.getData();
                    assert file != null;
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(file);
                    Bitmap categoryImageBitmap = BitmapFactory.decodeStream(inputStream);
                    categoryImageBitmap = Tools.imageReSize(categoryImageBitmap, 100);
                    if (categoryImageBitmap != null) {
                        categoryImageView.setImageBitmap(categoryImageBitmap);
                    }
                    //TODO: upload
                    categoryImgBitmap = categoryImageBitmap;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CALL_BACKGROUND_SETTINGS) {
            callSettingsCheck();
        }
    }


    private void init() {


        mShimmerViewContainer = categoryBinding.shimmerViewContainer;
        mShimmerViewContainer.startShimmer();
        mShimmerViewContainer.setVisibility(View.VISIBLE);

        mShimmerViewContainerSlide = categoryBinding.shimmerViewContainerSlide;
        mShimmerViewContainerSlide.startShimmer();
        mShimmerViewContainerSlide.setVisibility(View.VISIBLE);

        RecyclerView category_list = categoryBinding.categoryList;
        category_list.setLayoutManager(getGridLayout());
        categoryAdapter = new RecycleAdapter(productViewModel.getRecycleItems().getValue(), this, this, getActivity());
        category_list.setAdapter(categoryAdapter);

        categoryBinding.radioSubcategoryNeededYes.setChecked(true);

//        setLatLngInitial();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*featuredList*/

        fProductsListViewAdapter = new SlideShowAdapter(
                this.getContext(), categoryBinding, featuredProducts_slideShow, categoryBinding.featuredProductsListView);
        categoryBinding.featuredProductsListView.setAdapter(fProductsListViewAdapter);

        /*featuredList*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        openTimeText=Tools.parseTime(openTime);
//        closeTimeText=Tools.parseTime(closeTime);

        categoryBinding.shopOpenTime.setOnClickListener(new View.OnClickListener() {
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
                        categoryBinding.shopOpenTime.setText(selectedHour + ":" + min + " " + AM_PM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select open time");
                mTimePicker.show();
            }
        });

        categoryBinding.shopCloseTime.setOnClickListener(new View.OnClickListener() {
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
                        categoryBinding.shopCloseTime.setText(selectedHour + ":" + min + " " + AM_PM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select close time");
                mTimePicker.show();
            }
        });

    }


    @Override
    public void onItemClick(@NotNull RecycleItem category, int pos) {
        if (Config.sharedPreferences.getString(Constants.LAT, "0").equals("0")) {
            this.category = category;
            setLatLng();

        } else {

        }
        categoryClick(category);

    }

    public void onResume() {
        super.onResume();

        // Set title bar
        if (shop != null) {
            ((MainActivity) getActivity())
                    .changeTitle(shop.getName());
        }
    }

    public void setUpOrdersCount() {
        if (Config.currentUser != null) {
            userViewModel.getUserInfo(Config.currentUser.getUser_id()).observe(this.getViewLifecycleOwner(), user -> {
                if (user != null && user.size() > 0) {
                    orderViewModel.fetchOrders(user.get(0).getUser_id());
                    orderViewModel.user = user.get(0);
                    orderViewModel.getTransactions_data().observe(this.getViewLifecycleOwner(), transactions -> {
                        if (transactions != null) {
                            if (transactions.size() > 0) {

                                if (transactions.get(0).getTrans_status_title().contains("404 error")) {
                                    transactions.clear();
                                }
                                transactionListFiltered.clear();
                                for (int i = 0; i < transactions.size(); i++) {
                                    if (transactions.get(i).getTrans_status_title().equalsIgnoreCase("Pending")) {
                                        transactionListFiltered.add(transactions.get(i));
                                    }
                                }
                            }
                        }

                        ordersCount = transactionListFiltered.size();
                        SessionData.getInstance().setOrdersCount(ordersCount);
//                        if (ordersCount > 0) {
//                            SessionData.getInstance().getBottomNavigation().setCount(R.id.nav_order, ordersCount + "");
//                        } else {
//                            if (sec) {
//                                SessionData.getInstance().getBottomNavigation().clearCount(R.id.nav_order);
//                            }
//                            sec = true;
//
//                        }
                    });

                }
            });
        }
    }

    public void checkShopKeyChange() {

        if (Config.sharedPreferences.getString(Constants.SHOP_KEY, null) == null) {
            Config.editPreferences.clear().apply();
            Config.editPreferences.putString(Constants.SHOP_KEY, shopId).apply();
        } else if (Config.sharedPreferences.getString(Constants.SHOP_KEY, null) != (shopId)) {
            Config.editPreferences.clear().apply();
            Config.editPreferences.putString(Constants.SHOP_KEY, shopId).apply();
        }
    }

    public void noCategoriesSetUp() {
        categoryBinding.pb.setVisibility(View.INVISIBLE);
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);

//        categoryBinding.categoryTitle.setVisibility(View.GONE);
        categoryBinding.noData.setVisibility(View.VISIBLE);
    }

    public void setCategoriesSetUp() {
        categoryBinding.pb.setVisibility(View.INVISIBLE);
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);

//        categoryBinding.categoryTitle.setVisibility(View.GONE);
        categoryBinding.noData.setVisibility(View.GONE);
    }

    public void noSlideViewSetUp() {
        categoryBinding.pb.setVisibility(View.INVISIBLE);

        mShimmerViewContainerSlide.stopShimmer();
        mShimmerViewContainerSlide.setVisibility(View.GONE);
        categoryBinding.parentSlideView.setVisibility(View.GONE);
    }

    public void setSlideViewSetUpOn() {
        categoryBinding.pb.setVisibility(View.INVISIBLE);

        mShimmerViewContainerSlide.stopShimmer();
        mShimmerViewContainerSlide.setVisibility(View.GONE);
        categoryBinding.parentSlideView.setVisibility(View.VISIBLE);
    }


    public void internetSnack(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
                            productViewModel.fetchCategoriesRetry();
                            productViewModel.fetchFeaturedProductsRetry();
                            shopViewModel.fetchShop();
                            setUpOrdersCount();
                            webCallFunctions();
                        } else {
                            internetSnack(categoryBinding.parentLayout);
                        }
                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public void internetSnackForAddCategory(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {
                            productViewModel.fetchCategoriesRetry();
                            productViewModel.fetchFeaturedProductsRetry();
                            shopViewModel.fetchShop();
                            setUpOrdersCount();
                            webCallFunctions();
                            addCategory();
                        } else {
                            internetSnack(categoryBinding.parentLayout);
                        }
                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public void webCallFunctions() {

        shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {

            if (shop != null) {
//                categoryBinding.setShop(shop);
                this.shop = shop;
                if (shop.getShop_app_force_update().equals("1")) {
                    if (shop.getShop_current_version().length() > 0) {
                        if (!shop.getShop_current_version().equals(BuildConfig.VERSION_NAME)) {
                            if (Objects.equals(Config.sharedPreferences.getString(Constants.INDIVIDUAL_SHOP, "0"), "1")) {                         //test condition
//                                customAlertForcedUpdate(getActivity(), shop.getShop_current_version());
                            }
                        }
                    }
                }


                if (shop.getIs_enable().equals("0")) {
                    categoryBinding.lnrCloseShop.setVisibility(View.VISIBLE);
                    final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
                    animator.setRepeatCount(ValueAnimator.INFINITE);
                    animator.setInterpolator(new LinearInterpolator());
                    animator.setDuration(10000L);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            final float progress = (float) animation.getAnimatedValue();
                            final float width = categoryBinding.closeShopText1.getWidth();
                            final float translationX = width * progress;
                            categoryBinding.closeShopText1.setTranslationX(translationX);
                            categoryBinding.closeShopText2.setTranslationX(translationX - width);
                        }
                    });
                    animator.start();

                    if (shop.getShop_open_time().equals(shop.getShop_close_time())) {
                        categoryBinding.closeShopText2.setText("Shop is closed");
                    } else {
                        categoryBinding.closeShopText2.setText("                  Open time : " + Tools.parseTime(shop.getShop_open_time()) + "   Close time : " + Tools.parseTime(shop.getShop_close_time()));
                    }
                } else {
                    categoryBinding.lnrCloseShop.setVisibility(View.GONE);
                }


                if (shop.getStatus().equals("0")) {
                    categoryBinding.lnrCloseShop.setVisibility(View.VISIBLE);
                    final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
                    animator.setRepeatCount(ValueAnimator.INFINITE);
                    animator.setInterpolator(new LinearInterpolator());
                    animator.setDuration(10000L);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            final float progress = (float) animation.getAnimatedValue();
                            final float width = categoryBinding.closeShopText2.getWidth();
                            final float translationX = width * progress;
                            categoryBinding.closeShopText1.setTranslationX(translationX);
                            categoryBinding.closeShopText2.setTranslationX(translationX - width);
                        }
                    });
                    animator.start();
                    categoryBinding.closeShopText2.setText("Shop is closed");

                }

                if (shop.getLat().length() > 1) {
//                   checkDistance(shop);
                    setLatLngInitial(shop);
                }


                if (shop.getStatus().contains("404 errorNo more records")) {
                    setClosedShop();
                }

                if (shop.getIndividual_app().equals("1")) {
                    Config.editPreferences.putString(Constants.INDIVIDUAL_SHOP, "1").apply();
                }

                try {
                    ((MainActivity) getActivity())
                            .changeTitle(shop.getName());
                    if (!shopListDb.CheckIsShopExist(shop.getId())) {
//                        if (shop.getDefault_photo().getImg_path().length() == 0) {
//                            shop.getDefault_photo().setUrl("https://www.clipartmax.com/png/middle/291-2911410_business-store-front-shop-vector-icon-stock-vector-storefront-icon.png");
//                        }
                        shopListDb.insertRecords(shop.getId(), shop.getName(), shop.getDefault_photo().getUrl() + shop.getDefault_photo().getImg_path());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SessionData.getInstance().setShop(shop);
            }
        });

        productViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            productViewModel.recycleItemsList.clear();
            if (categories != null) {
                if (categories.size() > 0) {
                    if (categories.get(0).getStatus() != null) {
                        if (categories.get(0).getStatus().contains("404 error")) {
                            if (categories.get(0).getStatus().contains("UnknownHostException")) {
                                internetSnack(categoryBinding.parentLayout);
                            }
                            categories = null;
                            noCategoriesSetUp();
                        } else {
                            if (shopStatusOpen) {
                                setCategoriesSetUp();
                            } else {
                                noCategoriesSetUp();
                            }
                        }
                    }
                    if (categories != null) {
                        for (Category c : categories) {
                            if (c.getStatus().equals("1")) {
                                if (c.getShop_category_parent_id() != null) {
                                    if (c.getShop_category_parent_id().length() != 0) {
                                        productViewModel.recycleItemsList.add(new RecycleItem(c.getShop_category_parent_id(), c.getName(), StringUtil.concat(c.getDefault_photo().getUrl(), c.getDefault_photo().getImg_path()), c.getSubcategory_need(), c.getStatus(), c.getCategory_open_time(), c.getCategory_close_time(), c.getIs_enable()));
                                    } else {
                                        productViewModel.recycleItemsList.add(new RecycleItem(c.getId(), c.getName(), StringUtil.concat(c.getDefault_photo().getUrl(), c.getDefault_photo().getImg_path()), c.getSubcategory_need(), c.getStatus(), c.getCategory_open_time(), c.getCategory_close_time(), c.getIs_enable()));
                                    }
                                } else {
                                    productViewModel.recycleItemsList.add(new RecycleItem(c.getId(), c.getName(), StringUtil.concat(c.getDefault_photo().getUrl(), c.getDefault_photo().getImg_path()), c.getSubcategory_need(), c.getStatus(), c.getCategory_open_time(), c.getCategory_close_time(), c.getIs_enable()));
                                }
                            } else {
                                if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {
                                    //Owner
                                    {
                                        if (c.getShop_category_parent_id() != null) {
                                            if (c.getShop_category_parent_id().length() != 0) {
                                                productViewModel.recycleItemsList.add(new RecycleItem(c.getShop_category_parent_id(), c.getName(), StringUtil.concat(c.getDefault_photo().getUrl(), c.getDefault_photo().getImg_path()), c.getSubcategory_need(), c.getStatus(), c.getCategory_open_time(), c.getCategory_close_time(), c.getIs_enable()));
                                            } else {
                                                productViewModel.recycleItemsList.add(new RecycleItem(c.getId(), c.getName(), StringUtil.concat(c.getDefault_photo().getUrl(), c.getDefault_photo().getImg_path()), c.getSubcategory_need(), c.getStatus(), c.getCategory_open_time(), c.getCategory_close_time(), c.getIs_enable()));
                                            }
                                        } else {
                                            productViewModel.recycleItemsList.add(new RecycleItem(c.getId(), c.getName(), StringUtil.concat(c.getDefault_photo().getUrl(), c.getDefault_photo().getImg_path()), c.getSubcategory_need(), c.getStatus(), c.getCategory_open_time(), c.getCategory_close_time(), c.getIs_enable()));
                                        }
                                    }
                                }
                            }
                        }
                        if (productViewModel.recycleItemsList.size() == 0) {
                            noCategoriesSetUp();
                        }
                        productViewModel.setRecycleItems();
                    }
                }
            }
        });

        productViewModel.getFeaturedProducts().observe(getViewLifecycleOwner(), featuredProducts -> {

            if (featuredProducts != null) {
                if (featuredProducts.size() > 0) {
                    if (featuredProducts.get(0).getStatus() != null) {
                        if (featuredProducts.get(0).getStatus().contains("404 error")) {
                            featuredProducts.clear();
                            noSlideViewSetUp();
                        } else {
                            if (shopStatusOpen) {
                                setSlideViewSetUpOn();
                            }
                            featuredProducts_slideShow.clear();
                            featuredProducts_slideShow.addAll(featuredProducts);
                            featuredProducts_liveData.postValue(featuredProducts_slideShow);

                            //                for (Product product : featuredProducts) {
                            //                    this.products_slideShow.add(
                            //                            new Photo(
                            //                                    product.getDefault_photo().getImg_id(),
                            //                                    Constants.IMG_URL,
                            //                                    product.getDefault_photo().getImg_path(),
                            //                                    product.getDefault_photo().getImg_desc()
                            //                            ));
                            //                }
                            fProductsListViewAdapter.notifyDataSetChanged();
                            //                fProductsListViewAdapter.setPagerIcons(categoryBinding.viewPagerDot);
                        }
                    }
                } else {
                    noSlideViewSetUp();
                }
            }
        });
    }

    private void setClosedShop() {
        categoryBinding.parentSlideView.setVisibility(View.GONE);
        categoryBinding.categoryList.setVisibility(View.GONE);
        categoryBinding.noData.setVisibility(View.VISIBLE);
        categoryBinding.rlCatHeader.setVisibility(View.GONE);
//        categoryBinding.noData.setText("This shop is not founded");
        categoryBinding.noData.setText("Could not find this shop");
        shopStatusOpen = false;
        customAlertShopPermanentClose(getActivity());
        noSlideViewSetUp();
    }


    public void changeAddLayoutVisibility() {
        if (categoryBinding.addBtn.getVisibility() == View.VISIBLE) {
            categoryBinding.addBtnExpanded.setVisibility(View.VISIBLE);
            categoryBinding.lnrLayoutAddCategory.setVisibility(View.VISIBLE);
            categoryBinding.addBtn.setVisibility(View.GONE);
        } else {
            categoryBinding.addBtnExpanded.setVisibility(View.GONE);
            categoryBinding.lnrLayoutAddCategory.setVisibility(View.GONE);
            categoryBinding.addBtn.setVisibility(View.VISIBLE);
        }

        categoryBinding.categoryImageView.setImageResource(R.drawable.default_profile);
        categoryImgBitmap = null;
        categoryBinding.edtCategoryName.setText("");
        categoryBinding.shopOpenTime.setText("");
        categoryBinding.shopCloseTime.setText("");

    }

    public void setIndividualShopPreference(String shop_id) {

        Config.editPreferences.putString(Constants.SHOP_KEY, shop_id).apply();

        SessionData.getInstance().setShopKeyValue(shop_id);
        Config.editPreferences.putString(Constants.INDIVIDUAL_SHOP, "1").apply();
    }

    public void changeOwnerVisibility() {
        if (Config.sharedPreferences.getString(Constants.SHOP_OWNER, null) != null) {
            if (!Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, null), "1")) {
                categoryBinding.lnrAdd.setVisibility(View.GONE);
                categoryBinding.lnrLayoutAddCategory.setVisibility(View.GONE);
            } else {
                categoryBinding.lnrAdd.setVisibility(View.VISIBLE);
                categoryBinding.lnrLayoutAddCategory.setVisibility(View.GONE);
            }
        } else {
            categoryBinding.lnrAdd.setVisibility(View.GONE);
            categoryBinding.lnrLayoutAddCategory.setVisibility(View.GONE);
        }
        //Todo : Visible only for owner


        categoryBinding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAddLayoutVisibility();
            }
        });

        categoryBinding.addBtnExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAddLayoutVisibility();
            }
        });

        categoryBinding.categoryImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });
        categoryBinding.lnrStatus.setVisibility(View.VISIBLE);
        categoryBinding.radioStatusShow.setChecked(true);
        categoryBinding.addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryBinding.radioStatusShow.isChecked()) {
                    status = "1";
                } else {
                    status = "0";
                }

                if (categoryBinding.radioAvailableYes.isChecked()) {
                    is_enable = "1";
                } else {
                    is_enable = "0";
                }

                addCategory();
            }
        });


    }

    public void addCategory() {

        if (Config.currentUser != null) {
            if (categoryImgBitmap != null) {
                if (categoryBinding.edtCategoryName.length() > 0) {
                    if (categoryBinding.shopOpenTime.length() > 0) {
                        if (categoryBinding.shopCloseTime.length() > 0) {
                            progressDialog.startLoading();
                            productViewModel.createCategory(categoryImgBitmap, categoryImgBitmap,
                                    categoryBinding.edtCategoryName.getText().toString(),
                                    categoryBinding.radioSubcategoryNeededNo.isChecked() ? "0" : "1",
                                    is_enable,
                                    status,
                                    Config.currentUser.getUser_id(),
                                    SessionData.getInstance().getShopKeyValue(),
                                    SessionData.getInstance().getShop().getShop_bussiness_type(),
                                    openTime,
                                    closeTime
                            ).observe(getViewLifecycleOwner(), new Observer<Shop>() {
                                @Override
                                public void onChanged(Shop shop) {
                                    status = "1";
                                    is_enable = "1";
                                    progressDialog.dismiss();
                                    if (shop != null) {
                                        if (shop.getStatus().contains("Successfully added")) {
                                            Console.toast("Successfully added");

                                            categoryBinding.categoryImageView.setImageResource(R.drawable.default_profile);
                                            categoryImgBitmap = null;
                                            categoryBinding.edtCategoryName.setText("");
                                            categoryBinding.shopOpenTime.setText("");
                                            categoryBinding.shopCloseTime.setText("");

                                            changeAddLayoutVisibility();
                                            productViewModel.fetchCategoriesRetry();
                                            webCallFunctions();
                                        } else {
                                            if (shop.getStatus().contains("404 error")) {
                                                if (shop.getStatus().contains("UnknownHostException")) {
                                                    internetSnack(categoryBinding.parentLayout);
                                                }

//                                        noSubCategoriesSetUp();
                                            } else {
                                                try {
                                                    Console.toast(shop.getStatus());
                                                } catch (Exception e) {
                                                    Console.toast("Error");
                                                    e.printStackTrace();
                                                }
                                            }

                                        }

                                    }
                                }
                            });
                        } else {
                            Console.toast("Please enter close time");
                        }
                    } else {
                        Console.toast("Please enter open time");
                    }
                } else {
                    Console.toast("Please enter category name");
                }
            } else {
                Console.toast("Please select image");
            }
        } else {
//            Console.toast("Please login");
            Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
            startActivityForResult(auth_intent, CALL_BACKGROUND_SETTINGS);
        }

    }

    public void updateCategory(RecycleItem category) {

        if (Config.currentUser != null) {
            if (categoryImgBitmap != null) {
                if (edtCategoryName.length() > 0) {
                    if (edtOpenTime.length() > 0) {
                        if (edtCloseTime.length() > 0) {
                            progressDialogEdit.startLoading();
                            productViewModel.updateCategory(categoryImgBitmap, categoryImgBitmap,
                                    edtCategoryName.getText().toString(),
                                    radioSubcategoryNeededNo.isChecked() ? "0" : "1",//TODO :  set default value
                                    category.getId(),
                                    category.getIs_enable(),
                                    category.getStatus(),
                                    Config.currentUser.getUser_id(),
                                    SessionData.getInstance().getShopKeyValue(),
                                    SessionData.getInstance().getShop().getShop_bussiness_type(),
                                    openTime,
                                    closeTime
                            ).observe(getViewLifecycleOwner(), new Observer<Shop>() {
                                @Override
                                public void onChanged(Shop shop) {
                                    progressDialogEdit.dismiss();
                                    if (shop != null) {
                                        if (shop.getStatus().contains("Successfully added")) {
                                            Console.toast("Successfully updated");

                                            categoryImageView.setImageResource(R.drawable.default_profile);
                                            categoryImgBitmap = null;
                                            edtCategoryName.setText("");
                                            edtOpenTime.setText("");
                                            edtCloseTime.setText("");

                                            dialogEdit.dismiss();
                                            dialog.dismiss();
                                            productViewModel.fetchCategoriesRetry();
                                            webCallFunctions();
                                        } else {
                                            if (shop.getStatus().contains("404 error")) {
                                                if (shop.getStatus().contains("UnknownHostException")) {
                                                    internetSnack(categoryBinding.parentLayout);
                                                }

//                                        noSubCategoriesSetUp();
                                            } else {
                                                try {
                                                    Console.toast(shop.getStatus());
                                                } catch (Exception e) {
                                                    Console.toast("Error");
                                                    e.printStackTrace();
                                                }
                                            }

                                        }

                                    }
                                }
                            });
                        } else {
                            Console.toast("Please enter close time");
                        }
                    } else {
                        Console.toast("Please enter open time");
                    }
                } else {
                    Console.toast("Please enter category name");
                }
            } else {
                Console.toast("Please select image");
            }
        } else {
//            Console.toast("Please login");
            Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
            startActivityForResult(auth_intent, CALL_BACKGROUND_SETTINGS);
        }

    }

    public void deleteCategory(RecycleItem category) {

        if (Config.currentUser != null) {

            progressDialogEdit.startLoading();
            productViewModel.deleteCategory(categoryImgBitmap, categoryImgBitmap,
                    "",
                    category.getId(),
                    "1",
                    Config.currentUser.getUser_id(),
                    SessionData.getInstance().getShopKeyValue()
            ).observe(getViewLifecycleOwner(), new Observer<Shop>() {
                @Override
                public void onChanged(Shop shop) {
                    progressDialogEdit.dismiss();
                    if (shop != null) {
                        if (shop.getStatus().contains("Successfully deleted")) {
                            Console.toast("Successfully deleted");

                            dialog.dismiss();
                            productViewModel.fetchCategoriesRetry();
                            webCallFunctions();
                        } else {
                            if (shop.getStatus().contains("404 error")) {
                                if (shop.getStatus().contains("UnknownHostException")) {
                                    internetSnack(categoryBinding.parentLayout);
                                }

//                                        noSubCategoriesSetUp();
                            } else {
                                try {
                                    Console.toast(shop.getStatus());
                                } catch (Exception e) {
                                    Console.toast("Error");
                                    e.printStackTrace();
                                }
                            }

                        }

                    }
                }
            });

        } else {
//            Console.toast("Please login");
            Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
            startActivityForResult(auth_intent, CALL_BACKGROUND_SETTINGS);
        }

    }


    public void customAlert(RecycleItem category, int position) {

        dialog = new Dialog(getActivity());

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_2);


        TextView edit = (TextView) dialog.findViewById(R.id.dialog_edit);
        TextView delete = (TextView) dialog.findViewById(R.id.dialog_delete);
//        txtTitle.setText(title);
//        txtMessage.setText(message);
//        ok.setText(strOk);
//        cancel.setText(strCancel);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customAlertEdit(category, position);

                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteCategory(category);
                dialog.dismiss();

            }
        });


        dialog.show();
    }

    public void customAlertEdit(RecycleItem category, int position) {

        dialogEdit = new Dialog(getActivity());

        dialogEdit.setCancelable(true);
        dialogEdit.setCanceledOnTouchOutside(true);

        dialogEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEdit.setContentView(R.layout.custom_dialog_edit);


        Button update_category = dialogEdit.findViewById(R.id.update_category);
        radioSubcategoryNeededYes = dialogEdit.findViewById(R.id.radio_subcategory_needed_yes);
        radioSubcategoryNeededNo = dialogEdit.findViewById(R.id.radio_subcategory_needed_no);

        update_category.setText("UPDATE CATEGORY");
        edtCategoryName = dialogEdit.findViewById(R.id.edt_category_name);
        categoryImageView = dialogEdit.findViewById(R.id.categoryImageView);
        LinearLayout lnr_sub_cat_need_layout = dialogEdit.findViewById(R.id.lnr_sub_cat_need_layout);
        radio_status_show = dialogEdit.findViewById(R.id.radio_status_show);
        radio_status_hide = dialogEdit.findViewById(R.id.radio_status_hide);
        radio_available_yes = dialogEdit.findViewById(R.id.radio_available_yes);
        radio_available_no = dialogEdit.findViewById(R.id.radio_available_no);
        LinearLayout lnr_status = dialogEdit.findViewById(R.id.lnr_status);
        edtOpenTime = dialogEdit.findViewById(R.id.shop_open_time);
        edtCloseTime = dialogEdit.findViewById(R.id.shop_close_time);


        categoryImgBitmap = null;


        try {
            progressDialogFetchShop = new ProgressDialog(getActivity(), "Loading...");
            progressDialogFetchShop.startLoading();
            Picasso.get().load(category.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(categoryImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressDialogFetchShop.dismiss();
                            try {
                                categoryImgBitmap = ((BitmapDrawable) categoryImageView.getDrawable()).getBitmap();
                                categoryImgBitmap = Tools.imageReSize(categoryImgBitmap, 100);
                                categoryImageView.setImageBitmap(categoryImgBitmap);
                                SessionData.getInstance().setCategoryImgBitmap(categoryImgBitmap);
                            } catch (Exception e) {
                                Console.toast("Image error");
                                dialogEdit.dismiss();
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Exception e) {
                            progressDialogFetchShop.dismiss();
                            Console.toast("Image error");
                            dialogEdit.dismiss();
//                            getActivity().finish();
                        }
                    });
        } catch (Exception e) {
            progressDialogFetchShop.dismiss();
            Console.toast("Image error");
            dialogEdit.dismiss();

//            getActivity().finish();
        }


        lnr_sub_cat_need_layout.setVisibility(View.VISIBLE);
        lnr_status.setVisibility(View.VISIBLE);
        edtCategoryName.setText(category.getName());
        if (category.getSubcategory_need().equals("0")) {
            radioSubcategoryNeededNo.setChecked(true);
            radioSubcategoryNeededYes.setChecked(false);
        } else {
            radioSubcategoryNeededYes.setChecked(true);
        }

        if (category.getStatus().equals("1")) {
            radio_status_show.setChecked(true);
            radio_status_hide.setChecked(false);
        } else {
            radio_status_show.setChecked(false);
            radio_status_hide.setChecked(true);
        }

        if (category.getIs_enable().equals("1")) {
            radio_available_yes.setChecked(true);
            radio_available_no.setChecked(false);
        } else {
            radio_available_yes.setChecked(false);
            radio_available_no.setChecked(true);
        }

        openTime = category.getOpenTime();
        closeTime = category.getCloseTime();

        openTimeText = Tools.parseTime(openTime);
        closeTimeText = Tools.parseTime(closeTime);

        edtOpenTime.setText(openTimeText);
        edtCloseTime.setText(closeTimeText);

        categoryImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_UPDATE);
        });

        update_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_status_show.isChecked()) {
                    category.setStatus("1");
                } else {
                    category.setStatus("0");
                }

                if (radio_available_yes.isChecked()) {
                    category.setIs_enable("1");
                } else {
                    category.setIs_enable("0");
                }

                updateCategory(category);
            }
        });


        edtOpenTime.setOnClickListener(new View.OnClickListener() {
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
                        edtOpenTime.setText(selectedHour + ":" + min + " " + AM_PM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select open time");
                mTimePicker.show();
            }
        });

        edtCloseTime.setOnClickListener(new View.OnClickListener() {
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
                        edtCloseTime.setText(selectedHour + ":" + min + " " + AM_PM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select close time");
                mTimePicker.show();
            }
        });


//        txtTitle.setText(title);
//        txtMessage.setText(message);
//        ok.setText(strOk);
//        cancel.setText(strCancel);


        dialogEdit.show();
    }

    @Override
    public void onItemLongClick(RecycleItem category, int position) {
        if (Config.sharedPreferences.getString(Constants.SHOP_OWNER, null) != null)
            if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, null), "1"))
                customAlert(category, position);

    }

    public void setLatLng() {
        if (Config.sharedPreferences.getString(Constants.LAT, "0").equals("0")) {
            GPSTrackerService gpsTracker = new GPSTrackerService(getActivity());

            gpsTracker.getLocation();
            if (gpsTracker.canGetGps()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                        Console.toast("Need to set Location permission");

                        return;
                    } else {

                        if (gpsTracker.getIsGPSTrackingEnabled()) {
                            if (gpsTracker.getLatitude() > 0) {
                                Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
                                Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();
                                Console.logDebug("got lat lng");
                                if (category != null) {
                                    shopViewModel.fetchShop();
                                    shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {
//                                        categoryClick(category);
                                    });
                                }
                            } else {
                                Console.logDebug("lat lng not available");
                            }
                        } else {
                            gpsTracker.showSettingsAlert();
                        }
                    }
                } else {

                    if (gpsTracker.getIsGPSTrackingEnabled()) {
                        if (gpsTracker.getLatitude() > 0) {
                            Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
                            Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();
                            Console.logDebug("got lat lng");
                            if (category != null) {
                                shopViewModel.fetchShop();
                                shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {
//                                    categoryClick(category);
                                });
                            }
                        } else {
                            Console.logDebug("lat lng not available");
                        }
                    } else {
                        gpsTracker.showSettingsAlert();
                    }
                }
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }

    public void setLatLngInitial(Shop shop) {
        if (Config.sharedPreferences.getString(Constants.CURRENT_LAT, "0").equals("0")) {
            GPSTrackerService gpsTracker = new GPSTrackerService(getActivity());

            gpsTracker.getLocation();
            if (gpsTracker.canGetGps()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                        Console.toast("Need to set Location permission");

                        return;
                    } else {

                        if (gpsTracker.getIsGPSTrackingEnabled()) {
                            if (gpsTracker.getLatitude() > 0) {
                                Config.editPreferences.putString(Constants.CURRENT_LAT, gpsTracker.getLatitude() + "").apply();
                                Config.editPreferences.putString(Constants.CURRENT_LNG, gpsTracker.getLongitude() + "").apply();
                                Console.logDebug("got lat lng");
                                checkDistance(shop);

//                                if (category != null) {
//                                    shopViewModel.fetchShop();
//                                    shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {
//                                        categoryClick(category);
//                                    });
//                                }
                            } else {
                                Console.logDebug("lat lng not available");
                            }
                        } else {
                            gpsTracker.showSettingsAlert();
                        }
                    }
                } else {

                    if (gpsTracker.getIsGPSTrackingEnabled()) {
                        if (gpsTracker.getLatitude() > 0) {
                            Config.editPreferences.putString(Constants.CURRENT_LAT, gpsTracker.getLatitude() + "").apply();
                            Config.editPreferences.putString(Constants.CURRENT_LNG, gpsTracker.getLongitude() + "").apply();
                            Console.logDebug("got lat lng");
                            checkDistance(shop);

//                            if (category != null) {
//                                shopViewModel.fetchShop();
//                                shopViewModel.getShopData().observe(this.getViewLifecycleOwner(), shop -> {
//                                    categoryClick(category);
//                                });
//                            }
                        } else {
                            Console.logDebug("lat lng not available");
                        }
                    } else {
                        gpsTracker.showSettingsAlert();
                    }
                }
            } else {
                gpsTracker.showSettingsAlert();
            }
        } else {
            checkDistance(shop);
        }
    }

    public void categoryClick(RecycleItem category) {
        if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {
            if (category.getSubcategory_need() != null) {
                if (!category.getSubcategory_need().equalsIgnoreCase("0")) {
                    Config.editPreferences.putString(Constants.CATEGORY_ID, category.getId());
                    Config.editPreferences.putString(Constants.CATEGORY_NAME, category.getName());
                    Config.editPreferences.apply();
                    Navigation.findNavController(categoryBinding.getRoot()).navigate(R.id.action_nav_home_to_subCategoryActivity);
                } else {
                    Config.editPreferences.putString(Constants.CATEGORY_ID, category.getId());
                    Config.editPreferences.putString(Constants.CATEGORY_NAME, category.getName());
                    Config.editPreferences.putString(Constants.SUB_CATEGORY_ID, "");
                    Config.editPreferences.putString(Constants.SUB_CATEGORY_NAME, category.getName());
                    Config.editPreferences.apply();
                    Navigation.findNavController(categoryBinding.getRoot()).navigate(R.id.action_nav_home_to_productFragment);
                }
            }
        } else {
//            if (Objects.equals(Config.sharedPreferences.getString(Constants.AVAILABLE_DISTANCE, "FALSE"), "TRUE")) {
            if (category.getIs_enable().equals("1")) {
                if (category.getSubcategory_need() != null) {
                    if (!category.getSubcategory_need().equalsIgnoreCase("0")) {
                        Config.editPreferences.putString(Constants.CATEGORY_ID, category.getId());
                        Config.editPreferences.putString(Constants.CATEGORY_NAME, category.getName());
                        Config.editPreferences.apply();
                        Navigation.findNavController(categoryBinding.getRoot()).navigate(R.id.action_nav_home_to_subCategoryActivity);
                    } else {
                        Config.editPreferences.putString(Constants.CATEGORY_ID, category.getId());
                        Config.editPreferences.putString(Constants.CATEGORY_NAME, category.getName());
                        Config.editPreferences.putString(Constants.SUB_CATEGORY_ID, "");
                        Config.editPreferences.putString(Constants.SUB_CATEGORY_NAME, category.getName());
                        Config.editPreferences.apply();
                        Navigation.findNavController(categoryBinding.getRoot()).navigate(R.id.action_nav_home_to_productFragment);
                    }
                }
            } else {
                Console.toastLong("This category is not available now.\n\n" +
                        "It will be available from " + Tools.parseTime(category.getOpenTime()) + " to " + Tools.parseTime(category.getCloseTime()) + ".");
            }
//            } else {
//                setLatLngInitial(SessionData.getInstance().getShop());
//            }
        }
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
            txtMessage.setText("You need to update the latest version (" + version + ") to proceed.");
        } else {
            txtMessage.setText("You need to update the latest version to proceed.");
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
        ok.setText("Close");

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

    public void customAlertShopPermanentClose(Context mContext) { // nORMAL SCAN
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

        txtTitle.setText("Not available");
        ok.setText("Close");

        txtMessage.setText("Could not find this shop");


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
//                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void checkDistance(Shop shop) {
        double lat = 0;
        double lng = 0;
        double distance = 0;
        if (Config.sharedPreferences.getString(Constants.LAT, "0").length() > 1) {

            lat = Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0"));
            lng = Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0"));
//            distance = Tools.distanceInKms(
//                    Double.parseDouble(Config.sharedPreferences.getString(Constants.LAT, "0")),
//                    Double.parseDouble(Config.sharedPreferences.getString(Constants.LNG, "0")),
//
//                    Double.parseDouble(shop.getLat()),
//                    Double.parseDouble(shop.getLng())
//            );
        } else {
            lat = Double.parseDouble(Config.sharedPreferences.getString(Constants.CURRENT_LAT, "0"));
            lng = Double.parseDouble(Config.sharedPreferences.getString(Constants.CURRENT_LNG, "0"));
//            distance = Tools.distanceInKms(
//                    Double.parseDouble(Config.sharedPreferences.getString(Constants.CURRENT_LAT, "0")),
//                    Double.parseDouble(Config.sharedPreferences.getString(Constants.CURRENT_LNG, "0")),
//
//                    Double.parseDouble(shop.getLat()),
//                    Double.parseDouble(shop.getLng())
//            );
        }
//        getLiveDistance(
//                lat, lng,
//                Double.parseDouble(shop.getLat()),
//                Double.parseDouble(shop.getLng())
//                , shop
//        );
        if (lat != 0) {
            distance = Tools.distanceInKms(
                    lat, lng,
                    Double.parseDouble(shop.getLat()),
                    Double.parseDouble(shop.getLng())
            );

            if (distance > 20) {
//                customAlertShopNotAvailableForDistance(getActivity());
                showOverDistanceScreen();

                Config.editPreferences.putString(Constants.AVAILABLE_DISTANCE, "FALSE").apply();
            } else {
                Config.editPreferences.putString(Constants.AVAILABLE_DISTANCE, "TRUE").apply();
            }
        } else {
            distance = 0;
        }

    }
//    public void setLatLng() {
//        if (Config.sharedPreferences.getString(Constants.LAT, "0").equals("0")) {
//            GPSTrackerService gpsTracker = new GPSTrackerService(getActivity());
//
//            gpsTracker.getLocation();
//            if (gpsTracker.canGetGps()) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                MY_PERMISSIONS_REQUEST_LOCATION);
//
//                        return;
//                    } else {
//
//                        if (gpsTracker.getIsGPSTrackingEnabled()) {
//                            if (gpsTracker.getLatitude() > 0) {
//                                Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
//                                Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();
//                                Console.logDebug("got lat lng");
////                                resetShopDetails();
//                            } else {
//                                Console.logDebug("lat lng not available");
//                            }
//                        } else {
//                            gpsTracker.showSettingsAlert();
//                        }
//                    }
//                } else {
//
//                    if (gpsTracker.getIsGPSTrackingEnabled()) {
//                        if (gpsTracker.getLatitude() > 0) {
//                            Config.editPreferences.putString(Constants.LAT, gpsTracker.getLatitude() + "").apply();
//                            Config.editPreferences.putString(Constants.LNG, gpsTracker.getLongitude() + "").apply();
//                            Console.logDebug("got lat lng");
////                            resetShopDetails();
//                        } else {
//                            Console.logDebug("lat lng not available");
//                        }
//                    } else {
//                        gpsTracker.showSettingsAlert();
//                    }
//                }
//            } else {
//                gpsTracker.showSettingsAlert();
//            }
//        }else {
//            resetShopDetails();
//        }
//    }


    @NotNull
    public MutableLiveData<JsonObject> getLiveDistance(double lat1, double lon1, double lat2, double lon2, Shop shop) {
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

                    double distance = 0;
                    try {
                        distance = Double.parseDouble(parsedDistance) / 1000;
                        Console.logDebug("Distance category : " + distance);
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
//                        customAlertShopNotAvailableForDistance(getActivity());
                        showOverDistanceScreen();
                        Config.editPreferences.putString(Constants.AVAILABLE_DISTANCE, "FALSE").apply();
                    } else {
                        Config.editPreferences.putString(Constants.AVAILABLE_DISTANCE, "TRUE").apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
//                Console.error("error distance : " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Config.context);
        requestQueue.add(stringRequest);

        return response;
    }

    public void showOverDistanceScreen() {
        if (SessionData.getInstance().getShop() != null) {
            if (!SessionData.getInstance().getShop().getStatus().equals("0") && !SessionData.getInstance().getShop().getIs_enable().equals("0")) {
                categoryBinding.lnrCloseShop.setVisibility(View.VISIBLE);
                final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(10000L);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        final float progress = (float) animation.getAnimatedValue();
                        final float width = categoryBinding.closeShopText1.getWidth();
                        final float translationX = width * progress;
                        categoryBinding.closeShopText1.setTranslationX(translationX);
                        categoryBinding.closeShopText2.setTranslationX(translationX - width);
                    }
                });
                animator.start();

                categoryBinding.closeShopText2.setText("Delivery is not available.");
                categoryBinding.closeShopText1.setText("Delivery is not available.");

                categoryBinding.shopCloseTimeLayout2.setVisibility(View.VISIBLE);
                final ValueAnimator animator2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                animator2.setRepeatCount(ValueAnimator.INFINITE);
                animator2.setInterpolator(new LinearInterpolator());
                animator2.setDuration(10000L);
                animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        final float progress = (float) animation.getAnimatedValue();
                        final float width = categoryBinding.closeShopText1.getWidth();
                        final float translationX = width * progress;
                        categoryBinding.closeShopText12.setTranslationX(translationX);
                        categoryBinding.closeShopText22.setTranslationX(translationX - width);
                    }
                });
                animator2.start();

                categoryBinding.closeShopText22.setText("You are far away more than 20 km from the shop");
                categoryBinding.closeShopText12.setText("You are far away more than 20 km from the shop");
            }
        }
    }


    private void callSettingsCheck() {
//        Console.toast("Call setting Triggered : ProfileFragment");
//        Console.logDebug("Call setting Triggered : ProfileFragment");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callSettingsShow();
            }
        }, 1000);
    }

    private void callSettingsShow() {
        if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {
            //TODO: 2nd Implementation for Call Settings
            PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(FCMWorker.class, 15, TimeUnit.MINUTES).build();
            WorkManager.getInstance(getActivity()).enqueueUniquePeriodicWork("YourShop", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
            if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(getActivity()))
                AutoStartPermissionHelper.getInstance().getAutoStartPermission(getActivity());
            power_management();
        }
    }

    private void power_management() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getActivity().getPackageName();
            Intent in = new Intent();
            in.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            in.setData(Uri.parse("package:" + packageName));
            PowerManager pm = (PowerManager) getActivity().getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Your Shop").setMessage("Enable Battery Optimization")
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            startActivity(in);
                        })
                        .setCancelable(false)
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create().show();
            }
        }
    }
}
