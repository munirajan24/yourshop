package com.dvishapps.yourshop.ui.layout.checkout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.databinding.CheckoutForm2Binding;
import com.dvishapps.yourshop.models.Transaction;
import com.dvishapps.yourshop.ui.interfaces.DataManager;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.SessionData;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CheckoutStep2 extends Fragment implements BlockingStep {
    private View v;
    public DataManager<Transaction> dataManager;

    private double tax_amount;
    private double final_total;
    //    private double delivery_charge;
    private double shipping_tax_amount;
    private double shipping_cost;


    private CheckoutForm2Binding binding;

    @NotNull
    public static CheckoutStep2 newInstance() {
        Bundle args = new Bundle();
        CheckoutStep2 fragment = new CheckoutStep2();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint({"SetTextI18n", "StringFormatMatches"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.checkout_form_2, container, false);
        SessionData.getInstance().setCheckoutForm2Binding(binding);
        SessionData.getInstance().setCheckoutStep2(this);

        tax_amount = Double.parseDouble(String.format("%.2f", (Config.cart.getTotal_price() * SessionData.getInstance().getOverAllTaxValue()) / 100));
//        shipping_tax_amount = (shipping_cost * Config.TAX_SHIPPING_VALUE) / 100;
//        final_total = (Config.cart.getTotal_price() + shipping_cost + tax_amount + shipping_tax_amount );
//        final_total= Double.parseDouble(Config.cart.getFinalPrice());

        if (Config.cart.getProductsAsList().size()>0) {
            binding.setOverAllTaxValue(SessionData.getInstance().getOverAllTaxValue());
            if (SessionData.getInstance().getOverAllTaxValue() == 0) {
                binding.overAllTax.setVisibility(View.GONE);
                binding.taxValue.setVisibility(View.GONE);
            }
            if (Config.cart.getTotal_save() > 0)
                binding.discountValue.setText(String.valueOf(Config.cart.getTotal_save()));

            binding.totalItemCountValue.setText(String.valueOf(Config.cart.getTotal_qty()));
            binding.totalValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + Config.cart.getTotal_price());
            binding.subtotalValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + Config.cart.getTotal_price());
            binding.taxValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + Config.cart.getTotalTax());

            if (Config.sharedPreferences.getString(Constants.COD_CHARGE_FROM, "").equals("0")) {
                binding.deliveryChargeValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + SessionData.getInstance().getShippingTaxValue());
                binding.finalTotalValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + Config.cart.getFinalPrice());
            } else {
                binding.deliveryChargeValue.setText("Free");
                binding.finalTotalValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " "
                        + (Double.parseDouble(Config.cart.getFinalPrice())
                        - Config.cart.getDelivery_charge()));
            }
            dataManager.getData().setShipping_amount(String.valueOf(SessionData.getInstance().getShippingTaxValue()));

            binding.shippingCostValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + shipping_cost);

            binding.shippingTaxValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + shipping_tax_amount);

            if (SessionData.getInstance().getShop().getPackage_charges().length()>0) {
                if (Float.parseFloat(SessionData.getInstance().getShop().getPackage_charges())>0) {
                    binding.packageChargeValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + SessionData.getInstance().getShop().getPackage_charges());
                }else {
                    binding.packageChargeValue.setVisibility(View.GONE);
                    binding.packageChargeTitle.setVisibility(View.GONE);
                }
            }else {
                binding.packageChargeValue.setVisibility(View.GONE);
                binding.packageChargeTitle.setVisibility(View.GONE);
            }

            binding.couponDiscountValue.setText("-");
            binding.couponDiscountValueInput.setText("");

            binding.overAllTax.setText(String.format(getString(R.string.tax), SessionData.getInstance().getOverAllTaxValue()));
            binding.shippingTax.setText(String.format(getString(R.string.shipping_tax), Config.TAX_SHIPPING_VALUE));

        }else {
            getActivity().finish();
        }
        return binding.getRoot();
    }

    @Contract(pure = true)
    private boolean checkForm2() {
        return true;
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        if (checkForm2()) {
            dataManager.getData().setSub_total_amount(String.valueOf(Config.cart.getTotal_price()));
            dataManager.getData().setTax_amount(Config.cart.getTotalTax());
            dataManager.getData().setTax_percent(SessionData.getInstance().getOverAllTaxValue() + "%");
            dataManager.getData().setTotal_item_count(String.valueOf(Config.cart.getTotal_qty()));
            dataManager.getData().setTotal_item_amount(Config.cart.getFinalPrice());
            dataManager.getData().setShipping_tax_percent(Config.TAX_SHIPPING_VALUE + "%");
//            dataManager.getData().setShipping_amount(String.valueOf(Config.cart.getDelivery_charge()));

            dataManager.getData().setShipping_method_amount("");
            dataManager.getData().setShipping_method_name("");
            dataManager.getData().setDiscountAmount("0");
            dataManager.getData().setCoupon_discount_amount("");
            dataManager.getData().setPackage_charges(SessionData.getInstance().getShop().getPackage_charges()+"");
            callback.goToNextStep();
        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dataManager = (DataManager<Transaction>) context;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Activity must implement DataManager interface!");
        }
    }

    public void setDeliveryCharge(float deliveryCharge) {
        if (Config.sharedPreferences.getString(Constants.COD_CHARGE_FROM, "").equals("0")) {
            binding.deliveryChargeValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + deliveryCharge);
            binding.finalTotalValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " " + Config.cart.getFinalPrice());
        } else {
            binding.deliveryChargeValue.setText("Free");
            binding.finalTotalValue.setText(Config.cart.getProductsAsList().get(0).getCurrency_symbol() + " "
                    + (Double.parseDouble(Config.cart.getFinalPrice())
                    - deliveryCharge));
        }

        dataManager.getData().setShipping_amount(String.valueOf(deliveryCharge));

    }
}
