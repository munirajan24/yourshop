package com.dvishapps.yourshop.ui.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.R;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.ui.layout.checkout.CheckoutActivity;
import com.dvishapps.yourshop.utils.Tools;

import org.jetbrains.annotations.NotNull;

@SuppressLint("Registered")
public class SCompatActivity extends AppCompatActivity {
    protected SharedPreferences.Editor editPreferences = Config.editPreferences;
    protected final LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    protected final LinearLayoutManager linearLayout_horizontal = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
    protected final GridLayoutManager gridLayout = new GridLayoutManager(this, 3);
    protected final GridLayoutManager gridLayout_col_1 = new GridLayoutManager(this, 1);
    protected final GridLayoutManager verticalLayout_col_1 = new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false);
    protected final GridLayoutManager verticalGridLayout = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
    protected boolean isScroll = false;
    protected int childCount, itemCount, visibleItemPosition;
    private Intent checkout_intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Config.cart.changeListener.observeForever(cart ->
                invalidateOptionsMenu());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        menu.findItem(R.id.search_menu).setVisible(false);
        if (Config.cart.getTotal_qty() <= 0)
            menu.findItem(R.id.action_cart).setVisible(false);
        else
            Tools.setBadgeCount(this, menu.findItem(R.id.action_cart), String.valueOf(Config.cart.getTotal_qty()));
        return true;
    }

    public void setCustomToolbar(@NotNull Toolbar toolbar, String title, String subTitle) {
        toolbar.setTitle(title);
        toolbar.setSubtitle(subTitle);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                if (checkout_intent == null)
                    checkout_intent = new Intent(this, CheckoutActivity.class);
                startActivity(checkout_intent);
                return true;
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    public LinearLayoutManager getLinearLayout() {
        return new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }
}
