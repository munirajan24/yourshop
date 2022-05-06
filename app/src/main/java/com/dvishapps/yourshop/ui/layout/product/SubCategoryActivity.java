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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dvishapps.yourshop.FCM.FCMWorker;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.FSubCategoryBinding;
import com.dvishapps.yourshop.models.Photo;
import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.models.Shop;
import com.dvishapps.yourshop.models.SubCategory;
import com.dvishapps.yourshop.ui.adapters.RecycleAdapter;
import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleListener;
import com.dvishapps.yourshop.ui.interfaces.OnRecycleLongClickListener;
import com.dvishapps.yourshop.ui.interfaces.OnSlideListener;
import com.dvishapps.yourshop.ui.layout.auth.AuthActivity;
import com.dvishapps.yourshop.ui.viewModel.ProductViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.StringUtil;
import com.dvishapps.yourshop.utils.Tools;
import com.dvishapps.yourshop.utils.ViewUtils;
import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.POWER_SERVICE;
import static com.dvishapps.yourshop.app.Config.CALL_BACKGROUND_SETTINGS;

public class SubCategoryActivity extends SFragment implements OnSlideListener, OnRecycleListener, OnRecycleLongClickListener {
    private static final int PICK_IMAGE = 211;
    private static final int PICK_IMAGE2 = 2121;
    private static final int PICK_IMAGE_UPDATE = 1234;
    private RecycleAdapter sub_category_adapter;
    private ProductViewModel productViewModel;
    private FSubCategoryBinding binding;

    private String category_id;

    private ShimmerFrameLayout mShimmerViewContainer;
    private ProgressDialog progressDialog;
    private Bitmap categoryImgBitmap;
    private Bitmap categoryImgBitmap2;
    private Dialog dialog;
    private Dialog dialogEdit;
    private CircleImageView categoryImageView;
    private EditText edtCategoryName;
    private ProgressDialog progressDialogEdit;
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


    @NotNull
    public static SubCategoryActivity newInstance() {
        Bundle args = new Bundle();
        SubCategoryActivity fragment = new SubCategoryActivity();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category_id = Config.sharedPreferences.getString(Constants.CATEGORY_ID, "null");
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        progressDialog = new ProgressDialog(getActivity(), "Creating subcategory");
        productViewModel.fetchSubCategories(category_id);
        productViewModel.fetchProductCategoryTitle();

        progressDialog = new ProgressDialog(getActivity(), "Creating category");
        progressDialogEdit = new ProgressDialog(getActivity(), "Updating category");
//        productViewModel.getSubCategories().observe(this, subCategories -> {
//
//            if (subCategories != null) {
//                if (subCategories.size() > 0) {
//                    if (subCategories.get(0).getStatus().contains("404 error")) {
//                        if (subCategories.get(0).getStatus().contains("UnknownHostException")) {
//                            internetSnack(binding.parentLayout);
//                        }
//                        subCategories = null;
//                        noSubCategoriesSetUp();
//                    }else {
//                        setSubCategoriesSetUp();
//                    }
//                    if (subCategories != null) {
//
////                        backStackTitleSetup(productList);  //back press title change issue fix
//
//                        for (SubCategory subCty : subCategories) {
//
//                            productViewModel.recycleItemsList.add(new RecycleItem(subCty.getShop_sub_category_parent_id(), subCty.getName(),
//                                    StringUtil.concat(subCty.getDefault_photo().getUrl(), subCty.getDefault_photo().getImg_path())));
//                        }
//                        productViewModel.setRecycleItems();
//                    }
//                }
//            }
//
//        });
    }

    private void noSubCategoriesSetUp() {
        try {
            binding.pb.setVisibility(View.GONE);
            binding.searchBarInput.setVisibility(View.GONE);
            binding.lnrListLayout.setVisibility(View.GONE);
            binding.categoryTitle.setVisibility(View.GONE);
            binding.emptyLayout.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);
    }

    private void setSubCategoriesSetUp() {
        try {
            binding.pb.setVisibility(View.GONE);
            binding.searchBarInput.setVisibility(View.VISIBLE);
            binding.lnrListLayout.setVisibility(View.VISIBLE);
            binding.categoryTitle.setVisibility(View.VISIBLE);
            binding.emptyLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_sub_category, container, false);
//        setTitle(Config.sharedPreferences.getString(Constants.CATEGORY_NAME, "null"));
        changeOwnerVisibility();

        mShimmerViewContainer = binding.shimmerViewContainer;
        mShimmerViewContainer.startShimmer();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        webCallFunctions();

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sub_category_adapter.getFilter().filter(s);
            }
        });


        initSubCategories();
        return binding.getRoot();
    }

    @Override
    public void onItemClick(@NotNull RecycleItem subCategory, int pos) {

        if (subCategory.getIs_enable().equals("1")) {
            Config.editPreferences.putString(Constants.CATEGORY_ID, category_id);
            Config.editPreferences.putString(Constants.SUB_CATEGORY_ID, subCategory.getId());
            Config.editPreferences.putString(Constants.SUB_CATEGORY_NAME, subCategory.getName());
            Config.editPreferences.apply();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_subCategoryActivity_to_productFragment);
        } else {
            if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {
                Config.editPreferences.putString(Constants.CATEGORY_ID, category_id);
                Config.editPreferences.putString(Constants.SUB_CATEGORY_ID, subCategory.getId());
                Config.editPreferences.putString(Constants.SUB_CATEGORY_NAME, subCategory.getName());
                Config.editPreferences.apply();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_subCategoryActivity_to_productFragment);
            } else {
                Console.toastLong("This subcategory is not available now.\n\n" +
                        "It will be available from " + Tools.parseTime(subCategory.getOpenTime()) + " to " + Tools.parseTime(subCategory.getCloseTime()) + ".");
            }
        }
    }

    private void initSubCategories() {

        RecyclerView sub_category_view = binding.categoriesListView;
        sub_category_view.setLayoutManager(verticalGridLayout);
        sub_category_adapter = new RecycleAdapter(productViewModel.getRecycleItems().getValue(), this, this, getActivity());
        sub_category_view.setLayoutManager(getGridLayout());
        sub_category_view.setAdapter(sub_category_adapter);


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

    @Override
    public void onPhotoClick(@NotNull Photo photo, int position) {
        Config.editPreferences.putString(Constants.PRODUCT_ID, photo.getImg_parent_id());
        Config.editPreferences.apply();
        Intent intent = new Intent(this.getContext(), DetailActivity.class);
        startActivity(intent);
    }

    public void internetSnack(View parentLayout) {
        Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.isOnline()) {

                            productViewModel.fetchSubCategories(category_id);
                            productViewModel.fetchProductCategoryTitle();
                            webCallFunctions();
                        } else {
                            internetSnack(binding.parentLayout);
                        }
                    }
                })
                .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public void webCallFunctions() {

        productViewModel.getProductCategoryTitle().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                setTitle(s);
            }
        });

        productViewModel.getRecycleItems().observe(this.getViewLifecycleOwner(), recycleItems -> {
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);

            sub_category_adapter.setItems(recycleItems);
            ViewUtils.removeView(binding.pb);
        });

        productViewModel.getSubCategories().observe(this.getViewLifecycleOwner(), subCategories -> {
            productViewModel.recycleItemsList.clear();
            if (subCategories != null) {
                if (subCategories.size() > 0) {
                    if (subCategories.get(0).getStatus().contains("404 error")) {
                        if (subCategories.get(0).getStatus().contains("UnknownHostException")) {
                            internetSnack(binding.parentLayout);
                        }
                        subCategories = null;
                        noSubCategoriesSetUp();
                    } else {
                        setSubCategoriesSetUp();
                    }
                    if (subCategories != null) {
                        for (SubCategory subCty : subCategories) {
                            if (subCty.getStatus().equals("1")) {
                                if (subCty.getShop_sub_category_parent_id() != null) {
                                    if (subCty.getShop_sub_category_parent_id().length() != 0) {
                                        productViewModel.recycleItemsList.add(new RecycleItem(subCty.getShop_sub_category_parent_id(), subCty.getName(),
                                                StringUtil.concat(subCty.getDefault_photo().getUrl(), subCty.getDefault_photo().getImg_path()), "", subCty.getStatus()
                                                , subCty.getSub_category_open_time(), subCty.getSub_category_close_time(), subCty.getIs_enable()
                                        ));
                                    } else {
                                        productViewModel.recycleItemsList.add(new RecycleItem(subCty.getId(), subCty.getName(),
                                                StringUtil.concat(subCty.getDefault_photo().getUrl(), subCty.getDefault_photo().getImg_path()), "", subCty.getStatus()
                                                , subCty.getSub_category_open_time(), subCty.getSub_category_close_time(), subCty.getIs_enable()
                                        ));
                                    }
                                } else {
                                    productViewModel.recycleItemsList.add(new RecycleItem(subCty.getShop_sub_category_parent_id(), subCty.getName(),
                                            StringUtil.concat(subCty.getDefault_photo().getUrl(), subCty.getDefault_photo().getImg_path()), "", subCty.getStatus()
                                            , subCty.getSub_category_open_time(), subCty.getSub_category_close_time(), subCty.getIs_enable()
                                    ));
                                }
                            } else {
                                if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0"), "1")) {
                                    //Owner
                                    {
                                        if (subCty.getShop_sub_category_parent_id() != null) {
                                            if (subCty.getShop_sub_category_parent_id().length() != 0) {
                                                productViewModel.recycleItemsList.add(new RecycleItem(subCty.getShop_sub_category_parent_id(), subCty.getName(),
                                                        StringUtil.concat(subCty.getDefault_photo().getUrl(), subCty.getDefault_photo().getImg_path()), "", subCty.getStatus()
                                                        , subCty.getSub_category_open_time(), subCty.getSub_category_close_time(), subCty.getIs_enable()
                                                ));
                                            } else {
                                                productViewModel.recycleItemsList.add(new RecycleItem(subCty.getId(), subCty.getName(),
                                                        StringUtil.concat(subCty.getDefault_photo().getUrl(), subCty.getDefault_photo().getImg_path()), "", subCty.getStatus()
                                                        , subCty.getSub_category_open_time(), subCty.getSub_category_close_time(), subCty.getIs_enable()
                                                ));
                                            }
                                        } else {
                                            productViewModel.recycleItemsList.add(new RecycleItem(subCty.getShop_sub_category_parent_id(), subCty.getName(),
                                                    StringUtil.concat(subCty.getDefault_photo().getUrl(), subCty.getDefault_photo().getImg_path()), "", subCty.getStatus()
                                                    , subCty.getSub_category_open_time(), subCty.getSub_category_close_time(), subCty.getIs_enable()
                                            ));
                                        }
                                    }
                                }
                            }
                        }
                        if (productViewModel.recycleItemsList.size() == 0) {
                            noSubCategoriesSetUp();
                        }
                        productViewModel.setRecycleItems();
                    }
                }
            }
        });
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
        binding.lnrStatus2.setVisibility(View.VISIBLE);
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
        if (requestCode == CALL_BACKGROUND_SETTINGS) {
            callSettingsCheck();
        }

    }


    public void addCategory() {
        if (Config.currentUser != null) {
            if (categoryImgBitmap != null) {
                if (binding.edtCategoryName.length() > 0) {
                    if (binding.shopOpenTime.length() > 0) {
                        if (binding.shopCloseTime.length() > 0) {
                            if (Tools.isOnline()) {
                                progressDialog.startLoading();
                                productViewModel.createSubCategory(categoryImgBitmap, categoryImgBitmap,
                                        binding.edtCategoryName.getText().toString(),
                                        category_id,
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

                                                binding.categoryImageView.setImageResource(R.drawable.default_profile);
                                                categoryImgBitmap = null;
                                                binding.edtCategoryName.setText("");

                                                changeAddLayoutVisibility();
                                                productViewModel.fetchSubCategories(category_id);
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
                    Console.toast("Please enter category name");
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
                    if (binding.shopOpenTime2.length() > 0) {
                        if (binding.shopCloseTime2.length() > 0) {
                            if (Tools.isOnline()) {
                                progressDialog.startLoading();
                                productViewModel.createSubCategory(categoryImgBitmap2, categoryImgBitmap2,
                                        binding.edtCategoryName2.getText().toString(),
                                        category_id,
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

                                                binding.categoryImageView2.setImageResource(R.drawable.default_profile);
                                                categoryImgBitmap2 = null;
                                                binding.edtCategoryName2.setText("");
                                                binding.shopOpenTime2.setText("");
                                                binding.shopCloseTime2.setText("");

                                                productViewModel.fetchSubCategories(category_id);
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
                    Console.toast("Please enter category name");
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
        update_category.setText("UPDATE SUBCATEGORY");
        edtCategoryName = dialogEdit.findViewById(R.id.edt_category_name);
        categoryImageView = dialogEdit.findViewById(R.id.categoryImageView);

        radio_status_show = dialogEdit.findViewById(R.id.radio_status_show);
        radio_status_hide = dialogEdit.findViewById(R.id.radio_status_hide);
        radio_available_yes = dialogEdit.findViewById(R.id.radio_available_yes);
        radio_available_no = dialogEdit.findViewById(R.id.radio_available_no);
        LinearLayout lnr_status = dialogEdit.findViewById(R.id.lnr_status);

        edtOpenTime = dialogEdit.findViewById(R.id.shop_open_time);
        edtCloseTime = dialogEdit.findViewById(R.id.shop_close_time);
        LinearLayout lnr_sub_cat_need_layout = dialogEdit.findViewById(R.id.lnr_sub_cat_need_layout);
        lnr_sub_cat_need_layout.setVisibility(View.GONE);

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
                        }
                    });
        } catch (Exception e) {
            progressDialogFetchShop.dismiss();
            Console.toast("Image error");
            dialogEdit.dismiss();
        }

        edtCategoryName.setText(category.getName());

        lnr_status.setVisibility(View.VISIBLE);
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

    public void updateCategory(RecycleItem category) {

        if (Config.currentUser != null) {
            if (categoryImgBitmap != null) {
                if (edtCategoryName.length() > 0) {
                    if (edtOpenTime.length() > 0) {
                        if (edtCloseTime.length() > 0) {
                            progressDialogEdit.startLoading();
                            productViewModel.updateSubCategory(categoryImgBitmap, categoryImgBitmap,
                                    edtCategoryName.getText().toString(),
                                    category.getId(),
                                    category_id,
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

                                            dialogEdit.dismiss();
                                            dialog.dismiss();
                                            productViewModel.fetchSubCategories(category_id);
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
            startActivityForResult(auth_intent,CALL_BACKGROUND_SETTINGS);
        }

    }


    public void deleteCategory(RecycleItem category) {

        if (Config.currentUser != null) {

            progressDialogEdit.startLoading();
            productViewModel.deleteSubCategory(categoryImgBitmap, categoryImgBitmap,
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
                            productViewModel.fetchSubCategories(category_id);
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

    @Override
    public void onItemLongClick(RecycleItem category, int position) {
        if (Config.sharedPreferences.getString(Constants.SHOP_OWNER, null) != null)
            if (Objects.equals(Config.sharedPreferences.getString(Constants.SHOP_OWNER, null), "1"))
                customAlert(category, position);
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
