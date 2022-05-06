package com.dvishapps.yourshop.ui.adapters;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.models.User;
import com.dvishapps.yourshop.ui.layout.checkout.CheckoutStep1;
import com.dvishapps.yourshop.ui.layout.checkout.CheckoutStep2;
import com.dvishapps.yourshop.ui.layout.checkout.CheckoutStep3;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class CheckoutStepperAdapter extends AbstractFragmentStepAdapter {
    private User user;

    public CheckoutStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context, User user) {
        super(fm, context);
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyDataSetChanged();
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 1:
                return CheckoutStep2.newInstance();
            case 2:
                return CheckoutStep3.newInstance();

            default:
                return CheckoutStep1.newInstance(user);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context);
        switch (position) {
            case 0:
                builder.setEndButtonLabel(R.string.Address)
                        .setTitle(R.string.Address)
                        .setNextButtonEndDrawableResId(R.drawable.ic_chevron_right)
                        .setBackButtonStartDrawableResId(StepViewModel.NULL_DRAWABLE);
                break;
            case 1:
                builder
                        .setEndButtonLabel(R.string.Confirmation)
                        .setBackButtonLabel(R.string.Address)
                        .setTitle(R.string.Confirmation)
                        .setBackButtonStartDrawableResId(R.drawable.ms_ic_chevron_left);
                break;
            case 2:
                builder
                        .setBackButtonLabel(R.string.Confirmation)
                        .setTitle(R.string.Payment)
                        .setEndButtonLabel(R.string.Payment);
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
        return builder.create();
    }
}
