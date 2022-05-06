package com.dvishapps.yourshop.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.ActivityCallNewDesignBinding;
import com.dvishapps.yourshop.databinding.DialogViewMoreCallBinding;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.ui.adapters.TransactionOrderAdapter;
import com.dvishapps.yourshop.ui.viewModel.TransactionViewModel;
import com.dvishapps.yourshop.utils.Console;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.Tools;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CallActivity extends AppCompatActivity {
    //    MediaPlayer player;
    private LottieAnimationView mAnimationView;
    private RelativeLayout mRlCallAccept;
    private RelativeLayout mRlCallCancel;
    private PowerManager.WakeLock wl;

    private TransactionViewModel transactionViewModel;
    TextView customer_name;
    TextView trans_amount;
    TextView totalValue;
    TextView totalItemCountValue;
    TextView subtotalValue;
    TextView deliveryChargeValue;
    TextView packageChargeTitle;
    TextView packageChargeValue;
    TextView finalTotalValue;
    private ActivityCallNewDesignBinding binding;
    private Transaction transaction;

    boolean clickable = true;
    String paymentMode = "";

    ArrayList<Transaction> transactionArrayList = new ArrayList<>();
    private Dialog dialog;
    private Dialog dialogShowOrder;
    LinearLayout lnr_root;
    TransactionOrderAdapter pendingOrderAdapter;
    private boolean firstOrderHandled = false;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("get_out", false)) {
            finish();
        }

        SessionData.getInstance().setCallActivity(CallActivity.this);

//        setContentView(R.layout.activity_call_new_design);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_new_design);
//        ActivityCallNewDesignBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_call_new_design);

        final Window win = getWindow();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
        wl.acquire();

        LocalBroadcastManager.getInstance(CallActivity.this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData"));

        mAnimationView = findViewById(R.id.animationView);
        mRlCallAccept = findViewById(R.id.rlCallAccept);
        mRlCallCancel = findViewById(R.id.rlCallCancel);

        customer_name = findViewById(R.id.customer_name);
        trans_amount = findViewById(R.id.trans_amount);
        totalValue = findViewById(R.id.totalValue);
        finalTotalValue = findViewById(R.id.finalTotalValue);
        totalItemCountValue = findViewById(R.id.totalItemCountValue);
        subtotalValue = findViewById(R.id.subtotalValue);
        deliveryChargeValue = findViewById(R.id.deliveryChargeValue);
        packageChargeTitle = findViewById(R.id.packageChargeTitle);
        packageChargeValue = findViewById(R.id.packageChargeValue);

        String transaction_id = getIntent().getStringExtra("transaction");
        transaction = JsonUtils.fromJsonString(transaction_id, Transaction.class);
        binding.setTransaction(transaction);
//        Console.toast("Total :Rs"+transaction.getTotal_item_amount());
        binding.customerName.setText(transaction.getContact_name() + "'s Order");
        binding.transAmount.setText("Total Amount : Rs." + transaction.getTotal_item_amount());

        if (transaction.getRazor_payment_status().length() == 0) {
            binding.txtPaymentModeValue.setText("Cash On Delivery");
            paymentMode = "Cash On Delivery";
        } else {
            binding.txtPaymentModeValue.setText("Online Payment");
            paymentMode = "Online Payment";
        }
//        totalValue.setText("Rs." + transaction.getTotal_item_amount());
//        finalTotalValue.setText("Rs." + transaction.getTotal_item_amount());
//        totalItemCountValue.setText(transaction.getTotal_item_count());
//        subtotalValue.setText(transaction.getSub_total_amount());
//        deliveryChargeValue.setText(transaction.getShipping_amount());

        if (transaction.getTax_percent().equals("0")) {
            binding.taxText.setVisibility(View.GONE);
            binding.taxValue.setVisibility(View.GONE);
        }
        if (transaction.getShipping_email().equals("")) {
            binding.sEmailText.setVisibility(View.GONE);
            binding.sEmailValue.setVisibility(View.GONE);
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

        binding.txtTimeValue.setText(Tools.getDateAndTime(transaction.getAdded_date()));

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        binding.rlCallAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickable) {
                    Constants.stop_ringtone(getApplicationContext());
                    clickable = false;
                    binding.callAcceptLayout.setVisibility(View.GONE);
                    binding.layoutProgress.setVisibility(View.VISIBLE);
                    binding.txtProgressAccept.setVisibility(View.VISIBLE);
                    binding.txtProgressReject.setVisibility(View.GONE);
                    transactionViewModel.updateTransactionStatus(transaction.getId(), "4").observeForever(apiResponse -> {
                        if (apiResponse != null) {
                            if (apiResponse.getMessage().contains("Successfully Updated")) {
                                Console.toast("Order accepted");
//                                finish();
                                firstOrderHandled = true;
                                if (transactionArrayList.size() > 0) {
                                    showPendingOrdersDialog(transactionArrayList);
                                } else {
                                    get_out();
//                                    finish();
                                }
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
            public void onClick(View v) {
                if (clickable) {
                    Constants.stop_ringtone(getApplicationContext());
                    clickable = false;
                    binding.callAcceptLayout.setVisibility(View.GONE);
                    binding.layoutProgress.setVisibility(View.VISIBLE);
                    binding.txtProgressAccept.setVisibility(View.GONE);
                    binding.txtProgressReject.setVisibility(View.VISIBLE);

                    transactionViewModel.updateTransactionStatus(transaction.getId(), "5").observeForever(apiResponse -> {
                        if (apiResponse != null) {
                            if (apiResponse.getMessage().contains("Successfully Updated")) {
                                Console.toast("Order rejected");
//                                finish();
                                firstOrderHandled = true;

                                if (transactionArrayList.size() > 0) {
                                    showPendingOrdersDialog(transactionArrayList);
                                } else {
                                    get_out();
//                                    finish();
                                }
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

    @Override
    protected void onStop() {
        try {
//            player.release();
            wl.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
        SessionData.getInstance().setCallActivity(null);
        LocalBroadcastManager.getInstance(CallActivity.this).unregisterReceiver(mMessageReceiver);

    }

    @Override
    protected void onDestroy() {
//        player.release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
    }

    @Override
    protected void onPause() {
//        player.release();
        super.onPause();
    }

    @Override
    protected void onStart() {
        Constants.play_ringtone(getApplicationContext());
        try {
//            player.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    private void get_out() {
        Intent i = new Intent(CallActivity.this, CallActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("get_out", true);
        startActivity(i);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getExtras().getString("textResponse") != null) {
                    if (intent.getExtras().getString("textResponse").toLowerCase().contains("cancelled")) {
                        Constants.stop_ringtone(getApplicationContext());
                        String transaction = intent.getExtras().getString("transactionResponse");
                        String transactionId = intent.getExtras().getString("transactionId");
//                        Transaction transaction1 = JsonUtils.fromJsonString(transaction, Transaction.class);
                        if (pendingOrderAdapter == null) {
                            if (CallActivity.this.transaction.getId().equals(transactionId)) {
                                binding.callAcceptLayout.setVisibility(View.GONE);
                                binding.layoutProgress.setVisibility(View.VISIBLE);
                                binding.txtProgressAccept.setVisibility(View.GONE);
                                binding.txtProgressReject.setVisibility(View.VISIBLE);
                                binding.txtProgressReject.setText("Order cancelled by user");

                                if (transactionArrayList.size() > 0) {
                                    Console.toast("Order rejected.");
                                    showPendingOrdersDialog(transactionArrayList);
                                } else {
                                    finish();
                                }

                            } else {

                                for (int i = 0; i < transactionArrayList.size(); i++) {
                                    if (transactionArrayList.get(i).getId().equals(transactionId)) {
                                        transactionArrayList.remove(i);
                                    }
                                }

                                if (transactionArrayList.size() > 0) {
//                                    if (!firstOrderHandled) {
//                                        showPendingOrdersDialog(transactionArrayList);
//                                    }
                                } else {        //TODO : This case is impossible
                                    if (firstOrderHandled) {
                                        finish();
                                    }
                                }

                            }
                        } else {

                            pendingOrderAdapter.removeItem(transactionId);
                            if (pendingOrderAdapter.getItemCount() == 0) {
                                finish();
                            }

                        }

                    } else if (intent.getExtras().getString("textResponse").toLowerCase().contains("more order")) {
                        String transaction = intent.getExtras().getString("transactionResponse");
                        String transactionId = intent.getExtras().getString("transactionId");
                        Transaction transaction1 = JsonUtils.fromJsonString(transaction, Transaction.class);

//                        transactionArrayList.add(transaction1);
                        if (pendingOrderAdapter != null) {
                            pendingOrderAdapter.addItem(transaction1);
                        } else {
                            transactionArrayList.add(transaction1);
                        }


//                        Console.toast(" One more order has arrived.");
                        if (pendingOrderAdapter != null) {
                            if (lnr_root != null) {
                                newOrderSnack(lnr_root, transactionArrayList.size());
                            } else {
                                newOrderSnack(binding.parentLayout, transactionArrayList.size());
                            }
                        } else {
                            newOrderSnack(binding.parentLayout, transactionArrayList.size());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void showPendingOrdersDialog(ArrayList<Transaction> transactionList) {
        dialog = new Dialog(CallActivity.this);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pending_orders_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView closeImg = dialog.findViewById(R.id.close_img);
        RecyclerView recycle_orders_list = dialog.findViewById(R.id.orders_list);
        lnr_root = dialog.findViewById(R.id.lnr_root);
        LinearLayoutManager manager = new LinearLayoutManager(CallActivity.this, RecyclerView.VERTICAL, false);
        recycle_orders_list.setLayoutManager(manager);
        pendingOrderAdapter = new TransactionOrderAdapter(transactionList, transaction -> {
            showDialog(transaction);
        });
        recycle_orders_list.setAdapter(pendingOrderAdapter);

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (transactionList.size() == 1) {
                    showMessageOKCancel("Are you sure to close?", "You have one order in pending.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                } else {
                    showMessageOKCancel("Are you sure to close?", "You have " + transactionList.size() + " orders in pending.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                }


            }
        });


        dialog.show();
    }


    private void showDialog(Transaction transaction) {
        Constants.play_ringtone(getApplicationContext());
        clickable = true;
//        dialogShowOrder = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialogShowOrder = new Dialog(this);
//        dialogShowOrder.setContentView(R.layout.dialog_view_more_call_);

        DialogViewMoreCallBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_view_more_call, null, false);
        dialogShowOrder.setContentView(binding.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogShowOrder.getWindow();
        dialogShowOrder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialogShowOrder.getWindow().setGravity(Gravity.BOTTOM);
        dialogShowOrder.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;

        binding.setTransaction(transaction);
//        Console.toast("Total :Rs"+transaction.getTotal_item_amount());
        binding.customerName.setText(transaction.getContact_name() + "'s Order");
        binding.transAmount.setText("Total Amount : Rs." + transaction.getTotal_item_amount());

        if (transaction.getRazor_payment_status().length() == 0) {
            binding.txtPaymentModeValue.setText("Cash On Delivery");
            paymentMode = "Cash On Delivery";
        } else {
            binding.txtPaymentModeValue.setText("Online Payment");
            paymentMode = "Online Payment";
        }


        if (transaction.getTax_percent().equals("0")) {
            binding.taxText.setVisibility(View.GONE);
            binding.taxValue.setVisibility(View.GONE);
        }
        if (transaction.getShipping_email().equals("")) {
            binding.sEmailText.setVisibility(View.GONE);
            binding.sEmailValue.setVisibility(View.GONE);
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
        binding.txtTimeValue.setText(Tools.getDateAndTime(transaction.getAdded_date()));

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        binding.rlCallAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Console.toast("Clicked");
                if (clickable) {
                    Constants.stop_ringtone(getApplicationContext());
                    clickable = false;
                    binding.callAcceptLayout.setVisibility(View.GONE);
                    binding.layoutProgress.setVisibility(View.VISIBLE);
                    binding.txtProgressAccept.setVisibility(View.VISIBLE);
                    binding.txtProgressReject.setVisibility(View.GONE);
                    transactionViewModel.updateTransactionStatus(transaction.getId(), "4").observeForever(apiResponse -> {
                        if (apiResponse != null) {
                            if (apiResponse.getMessage().contains("Successfully Updated")) {
                                Console.toast("Order accepted");
//                                finish();

                                if (pendingOrderAdapter.getItemCount() > 0) {
                                    if (dialogShowOrder != null) {
                                        dialogShowOrder.dismiss();
                                    }

                                    pendingOrderAdapter.removeItem(transaction.getId());

                                    if (pendingOrderAdapter.getItemCount() == 0) {
                                        if (dialog != null) {
                                            dialog.dismiss();
                                        }
                                        get_out();
//                                        finish();
                                    }
                                } else {
                                    if (dialogShowOrder != null) {
                                        dialogShowOrder.dismiss();
                                    }
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                    get_out();
//                                    finish();
                                }
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
            public void onClick(View v) {
                if (clickable) {
                    Constants.stop_ringtone(getApplicationContext());
                    clickable = false;
                    binding.callAcceptLayout.setVisibility(View.GONE);
                    binding.layoutProgress.setVisibility(View.VISIBLE);
                    binding.txtProgressAccept.setVisibility(View.GONE);
                    binding.txtProgressReject.setVisibility(View.VISIBLE);

                    transactionViewModel.updateTransactionStatus(transaction.getId(), "5").observeForever(apiResponse -> {
                        if (apiResponse != null) {
                            if (apiResponse.getMessage().contains("Successfully Updated")) {
                                Console.toast("Order rejected");
//                                finish();
                                if (pendingOrderAdapter.getItemCount() > 0) {
                                    if (dialogShowOrder != null) {
                                        dialogShowOrder.dismiss();
                                    }

                                    pendingOrderAdapter.removeItem(transaction.getId());

                                    if (pendingOrderAdapter.getItemCount() == 0) {
                                        if (dialog != null) {
                                            dialog.dismiss();
                                        }
                                        get_out();
//                                        finish();
                                    }
                                } else {
                                    if (dialogShowOrder != null) {
                                        dialogShowOrder.dismiss();
                                    }
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                    get_out();
//                                    finish();
                                }
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
        dialogShowOrder.show();
    }

    private void showMessageOKCancel(String title, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CallActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void newOrderSnack(View parentLayout, int count) {
        if (count == 1) {
            Snackbar.make(parentLayout, "One more order arrived.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Console.toast("Orders list will be shown when you accept or reject this order");
                        }
                    })
                    .setBackgroundTint(parentLayout.getResources().getColor(android.R.color.black))
                    .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                    .show();
        } else {
            Snackbar.make(parentLayout, count + " more orders arrived.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Console.toast("Orders list will be shown when you accept or reject this order");
                        }
                    })
                    .setBackgroundTint(parentLayout.getResources().getColor(android.R.color.black))
                    .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light))
                    .show();
        }
    }
}