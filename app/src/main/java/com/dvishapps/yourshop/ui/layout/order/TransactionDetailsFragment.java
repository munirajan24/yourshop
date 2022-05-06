package com.dvishapps.yourshop.ui.layout.order;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.ui.CallActivity;
import com.dvishapps.yourshop.ui.common.ConfirmBottom;
import com.dvishapps.yourshop.ui.common.ProgressDialog;
import com.dvishapps.yourshop.ui.layout.checkout.CheckoutResult;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.ProfileTransactionDetailBinding;
import com.dvishapps.yourshop.ui.adapters.TransactionItemsAdapter;
import com.dvishapps.yourshop.ui.common.SFragment;
import com.dvishapps.yourshop.ui.viewModel.TransactionViewModel;
import com.dvishapps.yourshop.utils.Constants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransactionDetailsFragment extends SFragment implements ConfirmBottom.OnConfirmDialogAction, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private ProfileTransactionDetailBinding binding;
    private TransactionViewModel transactionViewModel;
    private TransactionItemsAdapter transactionItemsAdapter;

    Transaction transaction;
    private String tran_id;
    private String user_id;
    private ShimmerFrameLayout mShimmerViewContainer;
    private boolean from_pending = false;

    String[] items = new String[]{"Change status", "Completed by owner"};

    ArrayAdapter<String> spinnerAdapter;
    boolean enableSpinner = true;
    boolean initialCheck = false;

    private ConfirmBottom dialog;
    private boolean clickable = true;

    private GoogleMap mMap;
    private LatLng latLng;
    private Marker userMarker;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private MarkerOptions mCurrLocationMarker;
    private Marker currentLocMarkerOld;
    private double liveLat = 0;
    private double liveLng = 0;

    boolean showMap = false;
    boolean cancelClickable = true;
    private ProgressDialog progressDialog;
    String paymentMode = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        if (getArguments() != null) {
            user_id = getArguments().getString(transactionViewModel.USER_ID);
            tran_id = getArguments().getString(transactionViewModel.TRAN_ID);
            transactionViewModel.fetchTransactionDetail(user_id, tran_id);
            from_pending = getArguments().getBoolean("from_pending");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_transaction_detail, container, false);
        showMap = false;
        if (showMap) {
            if (SessionData.getInstance().getDeliveryStaffId() != null) {
                myRef = database.getReference("DeliveryStaff").child(SessionData.getInstance().getDeliveryStaffId());
            } else {
                binding.mapLayout.setVisibility(View.GONE);
            }
        } else {
            binding.mapLayout.setVisibility(View.GONE);
        }
        progressDialog = new ProgressDialog(getActivity(), "Canceling order...");

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData"));

//        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
        if (showMap) {

            SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

            mapFragment.getMapAsync(this);
            binding.mapLayout.setVisibility(View.VISIBLE);
        } else {
            binding.mapLayout.setVisibility(View.GONE);
        }
        init();

//        binding.map.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                        binding.scrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//                    case MotionEvent.ACTION_DOWN:
//                        binding.scrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        binding.scrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                        binding.scrollView.requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                return binding.map.onTouchEvent(event);
//            }
//        });

        transactionViewModel.getTransaction_data().observe(this.getViewLifecycleOwner(), transaction -> {
            if (transaction != null) {

                if (transaction.getTransaction_location().length() != 0) {

                    try {
                        String[] separated = transaction.getTransaction_location().split("\\s+");

                        latLng = new LatLng(Double.parseDouble(separated[0]), Double.parseDouble(separated[1]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (showMap) {
                    if (SessionData.getInstance().getDeliveryStaffId() != null) {
                        this.transaction = transaction;
                        if (userMarker == null) {
                            if (latLng != null) {
                                if (mMap != null) {
                                    setMyPosition();
                                }
                            }
                        }
                    } else {
                        binding.mapLayout.setVisibility(View.GONE);
                    }
                } else {
                    binding.mapLayout.setVisibility(View.GONE);
                }
                if (!Config.sharedPreferences.getString(Constants.COD_CHARGE_FROM, "").equals("0")) {

                    transaction.setTotal_item_amount(
                            (Double.parseDouble(transaction.getTotal_item_amount())
                                    - Double.parseDouble(transaction.getShipping_amount())
                            ) + ""
                    );

                    transaction.setShipping_amount("Free");
                }

                if (transaction.getTax_percent().equals("0")) {
                    binding.taxText.setVisibility(View.GONE);
                    binding.taxValue.setVisibility(View.GONE);
                }

                if (transaction.getPackage_charges().length() > 0) {
                    if (Float.parseFloat(transaction.getPackage_charges()) > 0) {
                        binding.packageChargeValue.setText(transaction.getCurrency_symbol() + " " + transaction.getPackage_charges());
                    } else {
                        binding.packageChargeValue.setVisibility(View.GONE);
                        binding.packageChargeTitle.setVisibility(View.GONE);
                    }
                } else {
                    binding.packageChargeValue.setVisibility(View.GONE);
                    binding.packageChargeTitle.setVisibility(View.GONE);
                }
                if (transaction.getShipping_email().length() > 0){
                    binding.sEmailText.setVisibility(View.GONE);
                    binding.sEmailValue.setVisibility(View.GONE);
                }
                if (Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0").equals("1")) { //for owner
                    if (transaction.getTrans_status_id().equals("9")
                            || transaction.getTrans_status_id().equals("3")
                            || transaction.getTrans_status_id().equals("7")
                            || transaction.getTrans_status_id().equals("8")
                            || transaction.getTrans_status_id().equals("5")) {
                        binding.spinnerLayout.setVisibility(View.GONE);
                        binding.shareLayout.setVisibility(View.GONE);
                    } else {
                        binding.shareLayout.setVisibility(View.VISIBLE);
                        binding.spinnerLayout.setVisibility(View.VISIBLE);
                    }
//                    if (from_pending)
//                    binding.shareLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.spinnerLayout.setVisibility(View.GONE);
                    binding.shareLayout.setVisibility(View.GONE);
                }

                if (transaction.getRazor_payment_status().length() == 0) {
                    binding.txtPaymentModeValue.setText("Cash On Delivery");
                    paymentMode = "Cash On Delivery";
                } else {
                    binding.txtPaymentModeValue.setText("Online Payment");
                    paymentMode = "Online Payment";
                }

                if (!Config.sharedPreferences.getString(Constants.SHOP_OWNER, "0").equals("1")) {//for customer
                    if (transaction.getTrans_status_id().length() > 0) {
                        if (transaction.getTrans_status_id().equals("1")) {
                            binding.orderCancelBtn.setVisibility(View.VISIBLE);

                        } else if (transaction.getTrans_status_id().equals("7")) {
                            binding.orderCancelBtn.setText("Order Cancelled");
                            binding.orderCancelBtn.setClickable(false);
                            binding.orderCancelBtn.setVisibility(View.VISIBLE);

                        } else {
                            binding.orderCancelBtn.setVisibility(View.GONE);
                        }

                    } else {
                        binding.orderCancelBtn.setVisibility(View.GONE);
                    }

                } else {
                    binding.orderCancelBtn.setVisibility(View.GONE);
                }
                binding.txtTimeValue.setText(Tools.getDateAndTime(transaction.getAdded_date()));
//                binding.txtDistanceValue.setText(transaction);

//                transaction.setTax_percent(SessionData.getInstance().getOverAllTaxValue() + "");
                binding.setTransaction(transaction);
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                binding.constDetails.setVisibility(View.VISIBLE);
                transactionViewModel.getTransaction_product_list_data().observe(this.getViewLifecycleOwner(), transactionDetails -> {
                    if (transactionDetails != null) {
                        transactionItemsAdapter.setItems(transactionDetails, transaction);
                    }
                });

                binding.txtShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*Create an ACTION_SEND Intent*/
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        /*This will be the actual content you wish you share.*/
                        String shareBody = "Order details (" + transaction.getTrans_code() + ")\n" + "\n" +
                                "Customer Name : " + transaction.getContact_name() + "\n" + "\n" +
                                "Total Amount : Rs." + transaction.getTotal_item_amount() + "\n" + "\n" +
                                "Date and Time : " + Tools.getDateAndTime(transaction.getAdded_date()) + "\n" + "\n" +
                                "Payment Mode : " + paymentMode + "\n" + "\n" +
                                "Transaction No. : " + transaction.getTrans_code() + "\n" + "\n" +
                                "Total item count : " + transaction.gettotal_item_count() + "\n" +
                                "Sub total : " + transaction.getSub_total_amount() + "\n" +
                                "Tax (" + transaction.getTax_percent() + ") : " + transaction.getTax_amount() + "\n" +
                                "Delivery Charges : " + transaction.getShipping_amount() + "\n" +
                                "Package Charges : " + transaction.getPackage_charges() + "\n" +
                                "Sub total : " + transaction.getSub_total_amount() + "\n" +
                                "Total : Rs." + transaction.getTotal_item_amount() + "\n" + "\n" + "\n" +
                                "Shipping Address  " + "\n" + "\n" +
                                "Phone : " + transaction.getShipping_phone() + "\n" + "\n" +
                                "Written Address : \n" + transaction.getShipping_address_2() + "\n" + "\n" +
                                "Map Address : \n" + transaction.getShipping_address_1();
                        /*The type of the content is text, obviously.*/
                        intent.setType("text/plain");
                        /*Applying information Subject and Body.*/
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Delivery order Details");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        /*Fire!*/
                        startActivity(Intent.createChooser(intent, "Share order"));
                    }
                });
                binding.imgShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*Create an ACTION_SEND Intent*/
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        /*This will be the actual content you wish you share.*/
                        String shareBody = "Order details (" + transaction.getTrans_code() + ")\n" + "\n" +
                                "Customer Name : " + transaction.getContact_name() + "\n" + "\n" +
                                "Total Amount : Rs." + transaction.getTotal_item_amount() + "\n" + "\n" +
                                "Date and Time : " + Tools.getDateAndTime(transaction.getAdded_date()) + "\n" + "\n" +
                                "Payment Mode : " + paymentMode + "\n" + "\n" +
                                "Transaction No. : " + transaction.getTrans_code() + "\n" + "\n" +
                                "Total item count : " + transaction.gettotal_item_count() + "\n" +
                                "Sub total : " + transaction.getSub_total_amount() + "\n" +
                                "Tax (" + transaction.getTax_percent() + ") : " + transaction.getTax_amount() + "\n" +
                                "Delivery Charges : " + transaction.getShipping_amount() + "\n" +
                                "Package Charges : " + transaction.getPackage_charges() + "\n" +
                                "Sub total : " + transaction.getSub_total_amount() + "\n" +
                                "Total : Rs." + transaction.getTotal_item_amount() + "\n" + "\n" + "\n" +
                                "Shipping Address  " + "\n" + "\n" +
                                "Phone : " + transaction.getShipping_phone() + "\n" + "\n" +
                                "Written Address : \n" + transaction.getShipping_address_2() + "\n" + "\n" +
                                "Map Address : \n" + transaction.getShipping_address_1();
                        /*The type of the content is text, obviously.*/
                        intent.setType("text/plain");
                        /*Applying information Subject and Body.*/
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Delivery order Details");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        /*Fire!*/
                        startActivity(Intent.createChooser(intent, "Share order"));
                    }
                });
                binding.imgWhatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*Create an ACTION_SEND Intent*/
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        /*This will be the actual content you wish you share.*/
                        String shareBody = "*Order details (" + transaction.getTrans_code() + ")*\n" + "\n" +
                                "Customer Name : " + transaction.getContact_name() + "\n" + "\n" +
                                "Total Amount : _Rs." + transaction.getTotal_item_amount() + "_\n" + "\n" +
                                "Date and Time : " + Tools.getDateAndTime(transaction.getAdded_date()) + "\n" + "\n" +
                                "Payment Mode : _" + paymentMode + "_\n" + "\n" +
                                "Transaction No. : " + transaction.getTrans_code() + "\n" + "\n" +
                                "Total item count : " + transaction.gettotal_item_count() + "\n" +
                                "Sub total : " + transaction.getSub_total_amount() + "\n" +
                                "Tax (" + transaction.getTax_percent() + ") : " + transaction.getTax_amount() + "\n" +
                                "Delivery Charges : " + transaction.getShipping_amount() + "\n" +
                                "Package Charges : " + transaction.getPackage_charges() + "\n" +
                                "Sub total : " + transaction.getSub_total_amount() + "\n" +
                                "Total : Rs." + transaction.getTotal_item_amount() + "\n" + "\n" + "\n" +
                                "Shipping Address  " + "\n" + "\n" +
                                "Phone : " + transaction.getShipping_phone() + "\n" + "\n" +
                                "Written Address : \n" + transaction.getShipping_address_2() + "\n" + "\n" +
                                "Map Address : \n" + transaction.getShipping_address_1();
                        /*The type of the content is text, obviously.*/
                        intent.setType("text/plain");
                        intent.setPackage("com.whatsapp");           // so that only Whatsapp reacts and not the chooser
                        /*Applying information Subject and Body.*/
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Delivery order Details");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        /*Fire!*/
                        startActivity(Intent.createChooser(intent, "Share order"));
                    }
                });
            }
        });



        return binding.getRoot();
    }

    private void init() {
        binding.setIsFromPending(from_pending);
        if (enableSpinner) {
            initialCheck = true;
        } else {
            initialCheck = true;
            binding.spinner1.setEnabled(false);
        }
        spinnerAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, items);

        binding.spinner1.setAdapter(spinnerAdapter);

        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                if (enableSpinner) {
                    if (!initialCheck) {
                        if (Tools.isOnline()) {
                            if (((String) parent.getItemAtPosition(position)).equals("Change status")) {
                                //Do nothing
                            } else if (((String) parent.getItemAtPosition(position)).equals("Completed by owner")) {
                                statusDialog();
                            }
                        }

                    } else {
                        initialCheck = false;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//        binding.setOverAllTaxValue(SessionData.getInstance().getOverAllTaxValue());
//        if (SessionData.getInstance().getOverAllTaxValue() == 0) {
//            binding.taxText.setVisibility(View.GONE);
//            binding.taxValue.setVisibility(View.GONE);
//        }
        RecyclerView transItemsList = binding.transItemsList;
        transactionItemsAdapter = new TransactionItemsAdapter(transactionViewModel.getTransaction_product_list_data().getValue(), transactionViewModel.getTransaction_data().getValue());
        transItemsList.setLayoutManager(getLinearLayout());
        transItemsList.setAdapter(transactionItemsAdapter);

        mShimmerViewContainer = binding.shimmerViewContainer;
        mShimmerViewContainer.startShimmer();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        binding.constDetails.setVisibility(View.GONE);

        binding.rlCallAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable) {
                    clickable = false;
                    binding.callAcceptLayout.setVisibility(View.GONE);
                    binding.layoutProgress.setVisibility(View.VISIBLE);
                    binding.txtProgressAccept.setVisibility(View.VISIBLE);
                    binding.txtProgressReject.setVisibility(View.GONE);
                    transactionViewModel.updateTransactionStatus(tran_id, "4").observeForever(apiResponse -> {
                        if (apiResponse != null) {
                            if (apiResponse.getMessage().contains("Successfully Updated")) {
                                getActivity().onBackPressed();
                                Console.toast("Order accepted");
                            } else {
                                clickable = true;
                                binding.callAcceptLayout.setVisibility(View.VISIBLE);
                                binding.layoutProgress.setVisibility(View.GONE);
                                Console.toast(apiResponse.getMessage());
                            }
                        }
                    });
//                finish();
                }
            }
        });
        binding.rlCallCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable) {
                    clickable = false;
                    binding.callAcceptLayout.setVisibility(View.GONE);
                    binding.layoutProgress.setVisibility(View.VISIBLE);
                    binding.txtProgressAccept.setVisibility(View.GONE);
                    binding.txtProgressReject.setVisibility(View.VISIBLE);

                    transactionViewModel.updateTransactionStatus(tran_id, "5").observeForever(apiResponse -> {
                        if (apiResponse != null) {
                            if (apiResponse.getMessage().contains("Successfully Updated")) {
                                getActivity().onBackPressed();
                                Console.toast("Order rejected");
                            } else {
                                clickable = true;
                                binding.callAcceptLayout.setVisibility(View.VISIBLE);
                                binding.layoutProgress.setVisibility(View.GONE);
                                Console.toast(apiResponse.getMessage());
                            }
                        }
                    });
//                finish();
                }
            }
        });

        if (showMap) {
            if (SessionData.getInstance().getDeliveryStaffId() != null) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setCameraPositionHandler();
                    }
                }, 5000);
            } else {
                binding.mapLayout.setVisibility(View.GONE);
            }
        } else {
            binding.mapLayout.setVisibility(View.GONE);
        }



        binding.orderCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelClickable) {
                    cancelClickable = false;
                    cancelOrder(tran_id);
                }
            }
        });
    }

    private void cancelOrder(String tran_id) {
        customAlertNewCancelOrder(getActivity(), "Are you sure to cancel your order?", "Cancel Order", "OK", "Close", tran_id);
    }

    public void customAlertNewCancelOrder(Context mContext, String message, String title, String strOk, String strCancel, String tran_id) { // nORMAL SCAN
        Dialog dialog;
        dialog = new Dialog(mContext);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
        TextView ok = (TextView) dialog.findViewById(R.id.dialog_Ok);
        TextView cancel = (TextView) dialog.findViewById(R.id.dialog_cancel);
        RelativeLayout rl_header = dialog.findViewById(R.id.rl_header);
        LinearLayout lnr_root = dialog.findViewById(R.id.lnr_root);
        View view_ok = (View) dialog.findViewById(R.id.view_ok);
        View view_cancel = (View) dialog.findViewById(R.id.view_cancel);

        ImageView closeImg = dialog.findViewById(R.id.close_img);

        txtTitle.setText(title);
        txtMessage.setText(message);
        ok.setText(strOk);
        cancel.setText(strCancel);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateStatus("7", tran_id);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClickable = true;

                dialog.dismiss();

            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClickable = true;

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void updateStatus(String status_id, String tran_id) {
        progressDialog.startLoading();

        transactionViewModel.updateTransactionStatus(tran_id, status_id)
                .observe(this.getViewLifecycleOwner(), apiResponse -> {
                    if (apiResponse != null) {
                        cancelClickable = true;
                        progressDialog.dismiss();
                        if (!Tools.isOnline()) {
                            //                    if (apiResponse.getStatus().contains("404 error")){
                            //                        internetSnack(binding.parentLayout);
                            Console.toast("No internet Connection");
                        } else {
                            //                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_profile);
                            if (apiResponse != null) {
                                if (apiResponse.getMessage().toLowerCase().contains("success")) {
                                    Console.toast("Order cancelled");
//                                    setUpUserOrders();
                                    binding.orderCancelBtn.setVisibility(View.GONE);
                                    getActivity().onBackPressed();

//                                    success_text.setText("Your order cancelled");
//                                    img_failure.setVisibility(View.VISIBLE);
//                                    img_success.setVisibility(View.GONE);
//                                    total_item_text_view.setVisibility(View.GONE);
//                                    success_text.setTextColor(Color.parseColor("#FF1100"));

                                } else {
                                    Console.toast(apiResponse.getMessage());
                                }

                                //      TODO : move to selected fragment
//                                Bundle bundle = new Bundle();
//                                bundle.putString("ORDER_TAB", status_id);
//                                SessionData.getInstance().setOrderTabSelection(status_id);
//                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_transaction_detail_to_OrdersSFragment, bundle);

                            } else {
//                                dialog.dismiss();
                            }

                        }
                    }
                });
    }

    public void updateStatus(String status_id) {

        transactionViewModel.updateTransactionStatus(tran_id, status_id)
                .observe(this.getViewLifecycleOwner(), apiResponse -> {
                    if (apiResponse != null) {
                        if (!Tools.isOnline()) {
                            //                    if (apiResponse.getStatus().contains("404 error")){
                            //                        internetSnack(binding.parentLayout);
                            Console.toast("No internet Connection");
                        } else {
                            //                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_to_profile);
                            if (apiResponse != null) {
                                Console.toast(apiResponse.getMessage());
                                getActivity().onBackPressed();
                                //      TODO : move to selected fragment
//                                Bundle bundle = new Bundle();
//                                bundle.putString("ORDER_TAB", status_id);
//                                SessionData.getInstance().setOrderTabSelection(status_id);
//                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_transaction_detail_to_OrdersSFragment, bundle);

                            } else {
//                                dialog.dismiss();
                            }

                        }
                    }
                });
    }

    public void statusDialog() {
        dialog = new ConfirmBottom(this.getContext(), this);
        dialog.setTitle(getString(R.string.confirm));
        dialog.setDesc("Are you sure to deliver with your own staff?");
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if (enableSpinner) {
//                    if (status.equals("1")) {
//                        binding.spinner1.setSelection(0);
//                    } else if (status.equals("2")) {
//                        binding.spinner1.setSelection(1);
//                    } else if (status.equals("3")) {
//                        binding.spinner1.setSelection(2);
//                    } else {
//                        binding.spinner1.setSelection(0);
//                    }

                    binding.spinner1.setSelection(0);

                }
            }
        });
    }

    @Override
    public void onConfirm() {
//        dialog.dismiss();
        updateStatus("9");
        dialog.dismiss();
    }

    @Override
    public void onClose() {
        dialog.dismiss();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("TAG", "OnMapReady");
        if (showMap) {
            if (SessionData.getInstance().getDeliveryStaffId() != null) {
                mMap = googleMap;
                mMap.setOnMarkerClickListener(this);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                if (userMarker == null) {
                    if (latLng != null) {
                        if (transaction != null)

                            setMyPosition();
                    }
                }

                getLiveLocationFromFirebase();
            } else {
                binding.mapLayout.setVisibility(View.GONE);
            }
        } else {
            binding.mapLayout.setVisibility(View.GONE);
        }

    }

    public void getLiveLocationFromFirebase() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String location = snapshot.getValue(String.class);

                try {
                    String lat = location.split(" ")[0];
                    String lng = location.split(" ")[1];

                    if (userMarker == null) {
                        if (latLng != null) {
                            if (transaction != null)
                                setMyPosition();
                        }
                    }
                    setLiveLocation(Double.parseDouble(lat), Double.parseDouble(lng));
                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(Config.context, "User not Available", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Console.logDebug(error.getMessage());
            }
        });
    }

    public void setLiveLocation(double lat, double lng) {
        mCurrLocationMarker = (new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title("Delivery staff is here")
//                .snippet(lat + "," + lng)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_delivery_man_2)));

        if (currentLocMarkerOld != null) {
            currentLocMarkerOld.remove();
        }
        liveLat = lat;
        liveLng = lng;

        Marker currentLocMarker = mMap.addMarker(mCurrLocationMarker);
        currentLocMarkerOld = currentLocMarker;
        if (transaction != null) {
            if (userMarker != null) {
                setCameraPositionHandler();
            } else {
                setCameraPositionHandler();
            }
        } else {
            setCameraPositionHandler();
        }
    }


    public void setCameraPositionHandler() {

        if (liveLat > 0) {
            if (userMarker != null) {
                setCameraPosition();
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(liveLat, liveLng), 15));
            }
        } else if (userMarker != null) {
            if (liveLat > 0) {
                setCameraPosition();
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        }
    }

    public void setCameraPosition() {
        try {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (Marker marker : markerList) {
            builder.include(currentLocMarkerOld.getPosition());
            builder.include(userMarker.getPosition());
//        }
            LatLngBounds bounds = builder.build();

//            Console.toast(currentLocMarkerOld.getPosition().latitude + " -currentLocMarkerOld lat");
//            Console.toast(userMarker.getPosition().latitude + " -userMarker lat");
            int padding = 200; // offset from edges of the map in pixels

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            mMap.animateCamera(cu);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setMyPosition() {
        userMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .snippet(transaction.getShipping_address_2())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_location))
                .title("You"));

        setCameraPositionHandler();
//        if (liveLat > 0) {
//            setLiveLocation(liveLat, liveLng);
//        } else {
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCameraPositionHandler();
            }
        }, 5000);

        return false;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getExtras().getString("textResponse") != null) {
                    if (intent.getExtras().getString("textResponse").toLowerCase().contains("cancelled")) {
//                        String transaction = intent.getExtras().getString("transactionResponse");
                        String transactionId = intent.getExtras().getString("transactionId");
//                        Transaction transaction1 = JsonUtils.fromJsonString(transaction, Transaction.class);
                        if (tran_id.equals(transactionId)) {
                            binding.callAcceptLayout.setVisibility(View.GONE);
                            binding.layoutProgress.setVisibility(View.VISIBLE);
                            binding.txtProgressAccept.setVisibility(View.GONE);
                            binding.txtProgressReject.setVisibility(View.VISIBLE);
                            binding.txtProgressReject.setText("Order cancelled by user");
                            getActivity().onBackPressed();
                        }

                    }
                }

                if (intent.getExtras().getString("removeCancel") != null) {
                    if (intent.getExtras().getString("removeCancel").toLowerCase().contains("true")) {
                        String id = intent.getExtras().getString("transactionId");

                        if (TransactionDetailsFragment.this.tran_id.equals(id)) {
                            binding.orderCancelBtn.setVisibility(View.GONE);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);

    }

}
