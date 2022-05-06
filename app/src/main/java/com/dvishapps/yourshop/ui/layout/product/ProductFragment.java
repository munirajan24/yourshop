package com.dvishapps.yourshop.ui.layout.product;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dvishapps.yourshop.FCM.FCMWorker;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.FProductBinding;
import com.dvishapps.yourshop.models.LimitData;
import com.dvishapps.yourshop.models.Product;
import com.dvishapps.yourshop.models.Search;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.ui.adapters.ProductAdapter;
import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.interfaces.OnSearchListener;
import com.dvishapps.yourshop.ui.layout.auth.AuthActivity;
import com.dvishapps.yourshop.ui.viewModel.ProductViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.Context.POWER_SERVICE;
import static com.dvishapps.yourshop.app.Config.CALL_BACKGROUND_SETTINGS;

public class ProductFragment extends SFragment implements OnSearchListener {

    private static final int PICK_IMAGE = 321;
    private static final int PICK_IMAGE_UPDATE = 323;
    private static final int PICK_MORE_IMAGE = 123;
    private static final int PICK_IMAGE2 = 322;
    private FProductBinding binding;
    private ProductViewModel productViewModel;
    private ProductAdapter productAdapter;
    private String categoryID;
    private String subCateID;
    private List<Product> prods;
    private boolean isSearch = false;

    private ShimmerFrameLayout mShimmerViewContainer;
    private ImageView categoryImageView;
    private Bitmap categoryImgBitmap;
    private Dialog dialog;
    private Dialog dialogEdit;
    private EditText edtCategoryName;
    private EditText edtOriginalPrice;
    private EditText edtOffer_price;
    private EditText edtProductDesc;
    private ProgressDialog progressDialogAdding;
    private ProgressDialog progressDialogDelete;
    private ProgressDialog progressDialogEdit;
    private ProgressDialog progressDialog;
    private Bitmap categoryImgBitmap2;
    private String selected_product_id = "";
    private ProgressDialog progressDialogFetchShop;
    private RadioButton radio_status_show;
    private RadioButton radio_status_hide;
    private RadioButton radio_available_yes;
    private RadioButton radio_available_no;
    private String status = "";
    private String is_enable = "1";

    private String closeTime = "";
    private String openTime = "";

    String openTimeText = "";
    String closeTimeText = "";
    private EditText edtOpenTime;
    private EditText edtCloseTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        categoryID = Config.sharedPreferences.getString(Constants.CATEGORY_ID, "null");
        subCateID = Config.sharedPreferences.getString(Constants.SUB_CATEGORY_ID, "null");

        progressDialog = new ProgressDialog(getActivity(), "Creating product");
        progressDialogEdit = new ProgressDialog(getActivity(), "Updating product");
        progressDialogAdding = new ProgressDialog(getActivity(), "Adding product image");
        progressDialogDelete = new ProgressDialog(getActivity(), "Deleting product");

//        setTitle(Config.sharedPreferences.getString(Constants.SUB_CATEGORY_NAME, "null"));
        productViewModel.fetchProductSubCategoryTitle();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_product, container, false);
        changeOwnerVisibility();
        mShimmerViewContainer = binding.shimmerViewContainer;
        mShimmerViewContainer.startShimmer();
        mShimmerViewContainer.setVisibility(View.VISIBLE);

//        setTitle(Config.sharedPreferences.getString(Constants.SUB_CATEGORY_NAME, "null"));

        binding.setIsLoading(true);
        binding.setLifecycleOwner(this);
        prods = new ArrayList<>();


        webCallFunctions();

        productViewModel.filterProducts();

        productViewModel.getFilteredProducts().observe(this.getViewLifecycleOwner(), productList -> {
            if (productList == null) {
                productViewModel.setLoadingState(false);
            } else {

                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);

                prods = productList;
                productAdapter.setItems(productList);
                binding.setCart(Config.cart);
            }
            binding.setIsLoading(false);
        });

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (productAdapter != null)
                    productAdapter.getFilter().filter(s);
            }
        });
        init();
        return binding.getRoot();
    }

    private void init() {


        LinearLayoutManager linearLayoutManager = getLinearLayout();
        RecyclerView p_filter_list = binding.productList;
        productAdapter = new ProductAdapter(productViewModel.getProducts().getValue(), getChildFragmentManager(), getActivity(), clickPos -> {
//            Config.editPreferences.putString(Constants.PRODUCT_ID, prods.get(pos).getId());
//            Config.editPreferences.putString(Constants.PRODUCT_NAME, prods.get(pos).getName());
//            Config.editPreferences.apply();
//            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_productFragment_to_detailActivity);
        }, longClickPos -> {
            if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {
                customAlert(productAdapter.products.get(longClickPos), longClickPos);
            } else {

            }


        });
        p_filter_list.setLayoutManager(linearLayoutManager);
        p_filter_list.setAdapter(productAdapter);

        p_filter_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScroll = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                childCount = linearLayoutManager.getChildCount();
                itemCount = linearLayoutManager.getItemCount();
                visibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (isScroll && productViewModel.loadMore && (itemCount - childCount == visibleItemPosition)) {
                    isScroll = false;
                    if (isSearch) {
//                        productViewModel.getSearchLimitData().offset += Config.LIMIT;
//                        productViewModel.setSearchLimitData(new LimitData(Config.LIMIT, productViewModel.getProductLimitData().offset));
                    } else {
                        productViewModel.getProductLimitData().offset += Config.LIMIT;
                        productViewModel.setProductLimitData(new LimitData(Config.LIMIT, productViewModel.getProductLimitData().offset));
                    }
                    binding.setIsLoading(true);
                }
            }
        });

        binding.edtOriginalPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showNumberPicker();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSearch(@NotNull Search search) {
        search.setCat_id(categoryID);
        search.setSub_cat_id(subCateID);
        productViewModel.setSearchData(search);
        binding.setIsLoading(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.search_menu).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search_menu) {
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.setOnSearchListener(this);
            searchFragment.show(getChildFragmentManager(), Constants.SEARCH_TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void internetSnack(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {

                            if (!Objects.equals(subCateID, "")) {
                                productViewModel.fetchProducts(categoryID, subCateID);
                            } else {
                                productViewModel.fetchProductsWithNoSubcategory(categoryID);
                            }
                            productViewModel.fetchProductSubCategoryTitle();
                            webCallFunctions();
                        } else {
                            internetSnack(binding.parentLayout);
                        }
                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public void noProductListSetup() {
        binding.textInputLayout.setVisibility(View.GONE);
        binding.lnrAddBtn.setVisibility(View.GONE);
        binding.emptyLayout.setVisibility(View.VISIBLE);
        binding.layoutAllProducts.setVisibility(View.GONE);

    }

    public void setProductListSetup() {
        binding.layoutAllProducts.setVisibility(View.VISIBLE);
        binding.textInputLayout.setVisibility(View.VISIBLE);
        binding.lnrAddBtn.setVisibility(View.VISIBLE);
        binding.emptyLayout.setVisibility(View.GONE);
    }

    public void webCallFunctions() {

        if (!Objects.equals(subCateID, "")) {
            productViewModel.fetchProducts(categoryID, subCateID);
        } else {
            productViewModel.fetchProductsWithNoSubcategory(categoryID);
        }

        productViewModel.getProductSubCategoryTitle().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                setTitle(s);
            }
        });

        productViewModel.getProducts().observe(this.getViewLifecycleOwner(), productList -> {
            prods.clear();
            if (productList != null) {
                productViewModel.setLoadingState(false);
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                if (productList.size() > 0) {
                    if (productList.get(0).getStatus().contains("404 error")) {
                        if (productList.get(0).getStatus().contains("UnknownHostException")) {
                            internetSnack(binding.parentLayout);
                        }
                        productList.clear();
                        noProductListSetup();
                    } else {
                        setProductListSetup();
                    }
                    //TODO: add only status enabled
                    prods.clear();
                    for (int i = 0; i < productList.size(); i++) {
                        if (productList.get(i).getStatus().equals("1")) {
                            prods.add(productList.get(i));
                        }else {
                            if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")){
                                //Owner
                                prods.add(productList.get(i));
                            }
                        }
                    }

//                    prods.addAll(productList);//TODO: not used
                    productAdapter.clear();
                    productAdapter.addItems(prods);
                    if (prods.size() == 0) {
                        noProductListSetup();
                    }
                    if (productList.size() == 0) {
                        binding.setCurrencySymbol(null);
                    } else {
                        binding.setCurrencySymbol(productList.get(0).getCurrency_symbol());
                    }
                    binding.setCart(Config.cart);
                }
            }
            binding.setIsLoading(false);
        });

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
                    binding.categoryImageView.setImageBitmap(categoryImageBitmap);
                    //TODO: upload
                    categoryImgBitmap = categoryImageBitmap;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_IMAGE2) {
            if (data != null) {
                try {
                    Uri file = data.getData();
                    assert file != null;
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(file);
                    Bitmap categoryImageBitmap = BitmapFactory.decodeStream(inputStream);
                    categoryImageBitmap = Tools.imageReSize(categoryImageBitmap, 100);
                    binding.categoryImageView2.setImageBitmap(categoryImageBitmap);
                    //TODO: upload
                    categoryImgBitmap2 = categoryImageBitmap;

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
        if (requestCode == PICK_MORE_IMAGE) {
            if (data != null) {
                try {
                    Uri file = data.getData();
                    assert file != null;
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(file);
                    Bitmap categoryImageBitmap = BitmapFactory.decodeStream(inputStream);
                    categoryImageBitmap = Tools.imageReSize(categoryImageBitmap, 100);
                    if (categoryImageBitmap != null) {
                        try {
                            addMoreImages(categoryImageBitmap, selected_product_id);
                            selected_product_id = "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                selected_product_id = "";
            }
        }
        if (requestCode == CALL_BACKGROUND_SETTINGS) {
            callSettingsCheck();
        }
    }

    public void changeOwnerVisibility() {
        if (Config.sharedPreferences.getString(Constants.SHOP_OWNER, null) != null) {
            if (!Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, null), "1")) {
                binding.lnrAdd.setVisibility(View.GONE);
                binding.lnrLayoutAddCategory.setVisibility(View.GONE);
                binding.layoutEmptyAdd.setVisibility(View.GONE);

            } else {
                binding.lnrAdd.setVisibility(View.VISIBLE);
                binding.lnrLayoutAddCategory.setVisibility(View.GONE);
                binding.layoutEmptyAdd.setVisibility(View.VISIBLE);
            }
        } else {
            binding.lnrAdd.setVisibility(View.GONE);
            binding.lnrLayoutAddCategory.setVisibility(View.GONE);
            binding.layoutEmptyAdd.setVisibility(View.GONE);
        }
        //Todo : Visible only for owner


        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAddLayoutVisibility();
            }
        });

        binding.addBtnExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAddLayoutVisibility();
            }
        });

        binding.categoryImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        binding.categoryImageView2.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE2);
        });
        binding.lnrStatus.setVisibility(View.VISIBLE);

        binding.radioStatusShow2.setChecked(true);
        binding.radioAvailableYes2.setChecked(true);
        binding.addCategory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.radioStatusShow2.isChecked()) {
                    status = "1";
                } else {
                    status = "0";
                }
                if (binding.radioAvailableYes2.isChecked()) {
                    is_enable = "1";
                } else {
                    is_enable = "0";
                }
                addCategory2();
            }
        });

        binding.radioStatusShow.setChecked(true);
        binding.radioAvailableYes.setChecked(true);
        binding.addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.radioStatusShow.isChecked()) {
                    status = "1";
                } else {
                    status = "0";
                }
                if (binding.radioAvailableYes.isChecked()) {
                    is_enable = "1";
                } else {
                    is_enable = "0";
                }
                addCategory();
            }
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


        binding.shopOpenTime2.setOnClickListener(new View.OnClickListener() {
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
                        binding.shopOpenTime2.setText(selectedHour + ":" + min + " " + AM_PM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select open time");
                mTimePicker.show();
            }
        });

        binding.shopCloseTime2.setOnClickListener(new View.OnClickListener() {
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
                        binding.shopCloseTime2.setText(selectedHour + ":" + min + " " + AM_PM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select close time");
                mTimePicker.show();
            }
        });


    }


    public void addCategory() {
        if (Config.currentUser != null) {
            if (categoryImgBitmap != null) {
                if (binding.edtCategoryName.length() > 0) {
                    if (binding.edtProductDesc.length() > 0) {
                        if (binding.edtOriginalPrice.length() > 0) {
                            if (binding.shopOpenTime.length() > 0) {
                                if (binding.shopCloseTime.length() > 0) {
                                    if (Tools.isOnline()) {
                                        progressDialog.startLoading();
                                        productViewModel.createProduct(categoryImgBitmap, categoryImgBitmap,
                                                binding.edtCategoryName.getText().toString(),
                                                binding.edtProductDesc.getText().toString(),//TODO : product_measurement
                                                binding.edtOriginalPrice.getText().toString(),//TODO : original_price
                                                binding.edtOriginalPrice.getText().toString(),//TODO : unit_price
                                                subCateID,
                                                categoryID,
                                                is_enable,
                                                status,
                                                "1",
                                                Config.currentUser.getUser_id(),
                                                SessionData.getInstance().getShopKeyValue(),
                                                SessionData.getInstance().getShop().getShop_bussiness_type(),
                                                openTime,
                                                closeTime,
                                                binding.offerPrice.getText().toString()

                                        ).observe(getViewLifecycleOwner(), new Observer<Shop>() {
                                            @Override
                                            public void onChanged(Shop shop) {
                                                status = "1";
                                                progressDialog.dismiss();
                                                if (shop != null) {
                                                    if (shop.getStatus().contains("Successfully added")) {
                                                        Console.toast("Successfully added");

                                                        binding.categoryImageView.setImageResource(R.drawable.default_profile);
                                                        categoryImgBitmap = null;
                                                        binding.edtCategoryName.setText("");
                                                        binding.edtProductDesc.setText("");
                                                        binding.edtOriginalPrice.setText("");
                                                        changeAddLayoutVisibility();
                                                        webCallFunctions();
                                                    } else {
                                                        if (shop.getStatus().contains("404 error")) {
                                                            if (shop.getStatus().contains("UnknownHostException")) {
                                                                internetSnack(binding.parentLayout);
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
                                        internetSnack(binding.parentLayout);
                                    }
                                } else {
                                    Console.toast("Please enter close time");
                                }
                            } else {
                                Console.toast("Please enter open time");
                            }
                        } else {
                            Console.toast("Please enter product price");
                        }
                    } else {
                        Console.toast("Please enter product measurement");
                    }
                } else {
                    Console.toast("Please enter product name");
                }
            } else {
                Console.toast("Please select image");
            }
        } else {
//            Console.toast("Please login");
            Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
            startActivityForResult(auth_intent,CALL_BACKGROUND_SETTINGS);
        }

    }

    public void addCategory2() {
        if (Config.currentUser != null) {
            if (categoryImgBitmap2 != null) {
                if (binding.edtCategoryName2.length() > 0) {
                    if (binding.edtProductDesc2.length() > 0) {
                        if (binding.edtOriginalPrice2.length() > 0) {
                            if (binding.shopOpenTime2.length() > 0) {
                                if (binding.shopCloseTime2.length() > 0) {
                                    if (Tools.isOnline()) {
                                        progressDialog.startLoading();
                                        productViewModel.createProduct(categoryImgBitmap2, categoryImgBitmap2,
                                                binding.edtCategoryName2.getText().toString(),
                                                binding.edtProductDesc2.getText().toString(),//TODO : product_measurement
                                                binding.edtOriginalPrice2.getText().toString(),//TODO : original_price
                                                binding.edtOriginalPrice2.getText().toString(),//TODO : unit_price
                                                subCateID,
                                                categoryID,
                                                is_enable,
                                                status,
                                                "1",
                                                Config.currentUser.getUser_id(),
                                                SessionData.getInstance().getShopKeyValue(),
                                                SessionData.getInstance().getShop().getShop_bussiness_type(),
                                                openTime,
                                                closeTime,
                                                binding.offerPrice2.getText().toString()

                                        ).observe(getViewLifecycleOwner(), new Observer<Shop>() {
                                            @Override
                                            public void onChanged(Shop shop) {
                                                progressDialog.dismiss();
                                                status = "1";
                                                if (shop != null) {
                                                    if (shop.getStatus().contains("Successfully added")) {
                                                        Console.toast("Successfully added");

                                                        binding.categoryImageView2.setImageResource(R.drawable.default_profile);
                                                        categoryImgBitmap2 = null;
                                                        binding.edtCategoryName2.setText("");
                                                        binding.edtProductDesc2.setText("");
                                                        binding.edtOriginalPrice2.setText("");

                                                        webCallFunctions();
                                                    } else {
                                                        if (shop.getStatus().contains("404 error")) {
                                                            if (shop.getStatus().contains("UnknownHostException")) {
                                                                internetSnack(binding.parentLayout);
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
                                        internetSnack(binding.parentLayout);
                                    }
                                } else {
                                    Console.toast("Please enter close time");
                                }
                            } else {
                                Console.toast("Please enter open time");
                            }
                        } else {
                            Console.toast("Please enter product price");
                        }
                    } else {
                        Console.toast("Please enter product description");
                    }
                } else {
                    Console.toast("Please enter product name");
                }
            } else {
                Console.toast("Please select image");
            }
        } else {
//            Console.toast("Please login");
            Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
            startActivityForResult(auth_intent,CALL_BACKGROUND_SETTINGS);
        }

    }


    public void changeAddLayoutVisibility() {
        if (binding.addBtn.getVisibility() == View.VISIBLE) {
            binding.addBtnExpanded.setVisibility(View.VISIBLE);
            binding.lnrLayoutAddCategory.setVisibility(View.VISIBLE);
            binding.addBtn.setVisibility(View.GONE);
        } else {
            binding.addBtnExpanded.setVisibility(View.GONE);
            binding.lnrLayoutAddCategory.setVisibility(View.GONE);
            binding.addBtn.setVisibility(View.VISIBLE);
        }
    }


    public void customAlert(Product product, int position) {

        dialog = new Dialog(getActivity());

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_3);


        TextView add_more_images = (TextView) dialog.findViewById(R.id.dialog_add_more_images);
        TextView edit = (TextView) dialog.findViewById(R.id.dialog_edit);
        TextView delete = (TextView) dialog.findViewById(R.id.dialog_delete);
//        txtTitle.setText(title);
//        txtMessage.setText(message);
//        ok.setText(strOk);
//        cancel.setText(strCancel);


        add_more_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                selected_product_id = product.getId();
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_MORE_IMAGE);
                dialog.dismiss();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customAlertEdit(product, position);

                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteCategory(product);
                dialog.dismiss();

            }
        });


        dialog.show();
    }

    private void addMoreImages(Bitmap productImageBitmap, String product_id) {
        progressDialogAdding.startLoading();
        productViewModel.addMoreImagesWithData(productImageBitmap, product_id).observe(getViewLifecycleOwner(), shop -> {
            progressDialogAdding.dismiss();
            if (shop != null) {
                if (shop.getStatus().contains("Successfully added")) {
                    Console.toast("Successfully Added");
                    dialog.dismiss();
                } else {
                    if (shop.getStatus().contains("404 error")) {
                        if (shop.getStatus().contains("UnknownHostException")) {
                            internetSnack(binding.parentLayout);
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
        });
    }

    public void customAlertEdit(Product product, int position) {

        dialogEdit = new Dialog(getActivity());

        dialogEdit.setCancelable(true);
        dialogEdit.setCanceledOnTouchOutside(true);

        dialogEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEdit.setContentView(R.layout.custom_dialog_edit_product);


        Button update_category = dialogEdit.findViewById(R.id.update_category);
        edtCategoryName = dialogEdit.findViewById(R.id.edt_category_name);
        categoryImageView = dialogEdit.findViewById(R.id.categoryImageView);
        edtProductDesc = dialogEdit.findViewById(R.id.edt_product_desc);
        edtOriginalPrice = dialogEdit.findViewById(R.id.edt_original_price);

        radio_status_show = dialogEdit.findViewById(R.id.radio_status_show);
        radio_status_hide = dialogEdit.findViewById(R.id.radio_status_hide);
        radio_available_yes = dialogEdit.findViewById(R.id.radio_available_yes);
        radio_available_no = dialogEdit.findViewById(R.id.radio_available_no);
        edtOffer_price = dialogEdit.findViewById(R.id.offer_price);
        LinearLayout lnr_status = dialogEdit.findViewById(R.id.lnr_status);

        edtOpenTime = dialogEdit.findViewById(R.id.shop_open_time);
        edtCloseTime = dialogEdit.findViewById(R.id.shop_close_time);
//        LinearLayout lnr_sub_cat_need_layout = dialogEdit.findViewById(R.id.lnr_sub_cat_need_layout);
//        lnr_sub_cat_need_layout.setVisibility(View.GONE);

        categoryImgBitmap = null;


        try {
            progressDialogFetchShop = new ProgressDialog(getActivity(), "Loading...");
            progressDialogFetchShop.startLoading();
            Picasso.get().load(Constants.IMG_URL.concat(product.getDefault_photo().getImg_path()))
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


        edtCategoryName.setText(product.getName());
        edtProductDesc.setText(product.getProduct_measurement());
        edtOriginalPrice.setText(product.getOriginal_price() + "");
        edtOffer_price.setText(product.getOffer_price() + "");

        lnr_status.setVisibility(View.VISIBLE);
        if (product.getStatus().equals("1")) {
            radio_status_show.setChecked(true);
            radio_status_hide.setChecked(false);
        } else {
            radio_status_show.setChecked(false);
            radio_status_hide.setChecked(true);
        }
        if (product.getIs_enable().equals("1")) {
            radio_available_yes.setChecked(true);
            radio_available_no.setChecked(false);
        } else {
            radio_available_yes.setChecked(false);
            radio_available_no.setChecked(true);
        }

        openTime = product.getProduct_open_time();
        closeTime = product.getProduct_close_time();

        openTimeText = Tools.parseTime(openTime);
        closeTimeText = Tools.parseTime(closeTime);

        edtOpenTime.setText(openTimeText);
        edtCloseTime.setText(closeTimeText);
        edtOffer_price.setText(product.getOffer_price()+"");

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
                    product.setStatus("1");
                } else {
                    product.setStatus("0");
                }

                if (radio_available_yes.isChecked()) {
                    product.setIs_enable("1");
                } else {
                    product.setIs_enable("0");
                }

                updateCategory(product);
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

    public void updateCategory(Product product) {

        if (Config.currentUser != null) {
            if (categoryImgBitmap != null) {
                if (edtCategoryName.getText().length() > 0) {
                    if (edtProductDesc.getText().length() > 0) {
                        if (edtOriginalPrice.getText().length() > 0) {
                            if (edtOpenTime.length() > 0) {
                                if (edtCloseTime.length() > 0) {
                                    if (edtOffer_price.length() > 0) {
                                        progressDialogEdit.startLoading();
                                        productViewModel.updateProduct(categoryImgBitmap, categoryImgBitmap,
                                                product.getId(),
                                                edtCategoryName.getText().toString(),
                                                edtProductDesc.getText().toString(),
                                                edtOriginalPrice.getText().toString(),
                                                edtOriginalPrice.getText().toString(),
                                                subCateID,
                                                categoryID,
                                                product.getIs_enable(),
                                                product.getStatus(),
                                                "1",
                                                Config.currentUser.getUser_id(),
                                                SessionData.getInstance().getShopKeyValue(),
                                                SessionData.getInstance().getShop().getShop_bussiness_type(),
                                                openTime,
                                                closeTime,
                                                edtOffer_price.getText().toString()
                                                ).observe(getViewLifecycleOwner(), new Observer<Shop>() {
                                            @Override
                                            public void onChanged(Shop shop) {
                                                progressDialogEdit.dismiss();
                                                if (shop != null) {
                                                    if (shop.getStatus().contains("Successfully added")) {
                                                        Console.toast("Successfully updated");

                                                        categoryImageView.setImageResource(R.drawable.default_profile);
                                                        categoryImgBitmap2 = null;
                                                        edtCategoryName.setText("");
                                                        edtProductDesc.setText("");
                                                        edtOriginalPrice.setText("");

                                                        dialogEdit.dismiss();
                                                        dialog.dismiss();
                                                        webCallFunctions();
                                                    } else {
                                                        if (shop.getStatus().contains("404 error")) {
                                                            if (shop.getStatus().contains("UnknownHostException")) {
                                                                internetSnack(binding.parentLayout);
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
                                        Console.toast("Please enter offer price");
                                    }
                                } else {
                                    Console.toast("Please enter close time");
                                }
                            } else {
                                Console.toast("Please enter open time");
                            }
                        } else {
                            Console.toast("Please enter product price");
                        }
                    } else {
                        Console.toast("Please enter product description");
                    }
                } else {
                    Console.toast("Please enter product name");
                }
            } else {
                Console.toast("Please select image");
            }
        } else {
//            Console.toast("Please login");
            Intent auth_intent = new Intent(this.getContext(), AuthActivity.class);
            startActivityForResult(auth_intent,CALL_BACKGROUND_SETTINGS);
        }

    }


    public void deleteCategory(Product product) {

        if (Config.currentUser != null) {

            progressDialogDelete.startLoading();

            productViewModel.deleteProduct(categoryImgBitmap, categoryImgBitmap,
                    "",
                    product.getId(),
                    "1",
                    Config.currentUser.getUser_id(),
                    SessionData.getInstance().getShopKeyValue()
            ).observe(getViewLifecycleOwner(), new Observer<Shop>() {
                @Override
                public void onChanged(Shop shop) {
                    progressDialogDelete.dismiss();
                    if (shop != null) {
                        if (shop.getStatus().contains("Successfully deleted")) {
                            Console.toast("Successfully deleted");

                            dialog.dismiss();
                            webCallFunctions();
                        } else {
                            if (shop.getStatus().contains("404 error")) {
                                if (shop.getStatus().contains("UnknownHostException")) {
                                    internetSnack(binding.parentLayout);
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
            startActivityForResult(auth_intent,CALL_BACKGROUND_SETTINGS);
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
