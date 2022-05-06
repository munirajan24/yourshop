package com.dvishapps.yourshop.ui.layout.order;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.databinding.FOrdersABinding;
import com.dvishapps.yourshop.ui.viewModel.UserViewModel;
import com.dvishapps.yourshop.utils.Console;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersTabFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private UserViewModel userViewModel;
    TextView txt_name;
    TextView txt_phone_number;
    ImageView img_call;
    LinearLayout lnr_delivery_staff_layout;
    LinearLayout lnr_delivery_staff_not_available_layout;
    private String phone = "";
    private ContentLoadingProgressBar progressDialog;
    private ShimmerFrameLayout mShimmerViewContainer;

    public OrdersTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new UserViewModel(this.getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_orders_s, container, false);

        txt_name = view.findViewById(R.id.txt_name);
        txt_phone_number = view.findViewById(R.id.txt_phone_number);
        img_call = view.findViewById(R.id.img_call);
        lnr_delivery_staff_layout = view.findViewById(R.id.lnr_delivery_staff_layout);
        lnr_delivery_staff_not_available_layout = view.findViewById(R.id.lnr_delivery_staff_not_available_layout);
        progressDialog = view.findViewById(R.id.pb);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.length() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getContext().checkSelfPermission(Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                    112);

                            return;
                        } else {

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phone));
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }
                } else {
                    Console.toast("Phone number not available");
                }
            }
        });
        //        SessionData.getInstance().setOrdersSFragment(OrdersSFragment.this);

        FragmentManager fm = getChildFragmentManager();
        ViewStateAdapter sa = new ViewStateAdapter(fm, getLifecycle());
        ViewPager2 pa = view.findViewById(R.id.pager);
        pa.setAdapter(sa);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("On Progress"));
        tabLayout.addTab(tabLayout.newTab().setText("Delivered"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pa.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pa.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        setDeliveryStafDetails();
        return view;
    }


    private static class ViewStateAdapter extends FragmentStateAdapter {

        ViewStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Hardcoded in this order, you'll want to use lists and make sure the titles match

            if (position == 0) {
                return new PendingOrdersFragment();
            }
            if (position == 1) {
                return new OnProcessOrdersFragment();
            }
            return new DeliveredOrdersFragment();


        }

        @Override
        public int getItemCount() {
            // Hardcoded, use lists
            return 3;
        }
    }

    public void setDeliveryStafDetails() {
//        progressDialog.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmer();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        userViewModel.getDeliveryStaffInfo().observe(this.getViewLifecycleOwner(), user -> {
            if (user != null) {
//                progressDialog.setVisibility(View.GONE);
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                if (user.getStatus().contains("404 error")) {
                    if (user.getStatus().contains("UnknownHostException")) {
//                            internetSnack(binding.parentLayout);
                    }else {
                        //TODO : set delivery staff not available status
                    }

//                        binding.setIsLoading(false);
//                            nouserSetUp();
                    lnr_delivery_staff_not_available_layout.setVisibility(View.VISIBLE);
                    lnr_delivery_staff_layout.setVisibility(View.GONE);
                } else {
                    lnr_delivery_staff_not_available_layout.setVisibility(View.GONE);
                    lnr_delivery_staff_layout.setVisibility(View.VISIBLE);
                    txt_name.setText(user.getUser_name());
                    txt_phone_number.setText(user.getUser_phone());
                    phone=user.getUser_phone();
                }
            }
        });
    }

}

