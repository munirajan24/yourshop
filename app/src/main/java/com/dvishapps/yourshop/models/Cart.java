package com.dvishapps.yourshop.models;

import android.os.Build;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.annotations.Expose;
import com.dvishapps.yourshop.BR;
import com.dvishapps.yourshop.app.Config;
import com.dvishapps.yourshop.utils.Constants;
import com.dvishapps.yourshop.utils.JsonUtils;
import com.dvishapps.yourshop.utils.SessionData;
import com.dvishapps.yourshop.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Cart extends BaseObservable {
    private MutableLiveData<Cart> cartChange = new MutableLiveData<>();
    public LiveData<Cart> changeListener;
    private DecimalFormat dec = new DecimalFormat("#0.00");

    public enum Actions {
        ADD,
        REMOVE,
        DELETE
    }

    @Expose
    private HashMap<String, List<Product>> items;
    @Expose
    private HashMap<String, Integer> productQty;
    @Expose
    private double total_price = 0d;
    @Expose
    private String str_total_price = "0";
    @Expose
    private double total_save = 0d;
    @Expose
    private double delivery_charge = 0d;
    @Expose
    private double packageCharge = 0d;
    @Expose
    private double defaultPackageCharge = 0;
    @Expose
    private double defaultShippingCharge = 0;
    @Expose
    private int total_qty = 0;

    public Cart() {
        this.items = new HashMap<>();
        this.productQty = new HashMap<>();
        changeListener = Transformations.switchMap(cartChange, input -> {
            if (input.items.size() > 0) {
                dynamicDeliveryChargeCalculation();
                dynamicPackageChargeCalculation();
//                if (SessionData.getInstance().getShippingTaxValue() != 0) {
//                    delivery_charge = defaultShippingCharge + SessionData.getInstance().getShippingTaxValue();
//                } else {
//                    delivery_charge = defaultShippingCharge;
//                }
//                try {
//                    delivery_charge = Double.parseDouble(dec.format(delivery_charge));
//                } catch (Exception e) {
//                    delivery_charge = defaultShippingCharge;
//                    e.printStackTrace();
//                }
            } else {
                delivery_charge = 0;
                packageCharge = 0;
            }
            MutableLiveData<Cart> data = new MutableLiveData<>();
            data.setValue(input);
            return data;
        });
    }

    public void initCart(@NotNull Cart cart) {
        items = cart.getItems();
        total_save = cart.getTotal_save();
        total_price = cart.getTotal_price();
        str_total_price = dec.format(total_price);
        total_qty = cart.getTotal_qty();
        delivery_charge = cart.getDelivery_charge();
        packageCharge = cart.getPackageCharge();

        cartChange.setValue(cart);
        setProductQty(cart.getProductQty());
    }

    public void onAdd(@NotNull Product prod, int qty) {
        //TODO: price changing issue fix partially done here

//        int savedProductQty = 0;
//        if (productQty.get(prod.getId()) != null) {
//            savedProductQty = productQty.get(prod.getId());
//        }
//
//
//        for (int i = 0; i < items.size(); i++) {
//            for (int j = 0; j < items.get(prod.getCat_id()).size(); j++) {
//                if (items.get(prod.getCat_id()).get(j).getId().equals(prod.getId())) {
//                    if (items.get(prod.getCat_id()).get(j).getOriginal_price() != prod.getOriginal_price()) {
////                    items.get(prod.getCat_id()).get(i).setOriginal_price(prod.getOriginal_price());
//                        onRemove(items.get(prod.getCat_id()).get(j), savedProductQty);
//                        qty = savedProductQty + qty;
//                    }
//                }
//            }
//        }


        total_qty += qty;
        total_save += prod.getOriginal_price() - prod.getUnit_price();
        total_price += prod.getUnit_price() * qty;
        str_total_price = dec.format(total_price);
        if (productQty.get(prod.getId()) != null) {
            int newQty = productQty.get(prod.getId()) + qty;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                productQty.replace(prod.getId(), newQty);
            } else {
                productQty.put(prod.getId(), newQty);
            }
        } else {
            productQty.put(prod.getId(), qty);
            if (items.get(prod.getCat_id()) == null)
                items.put(prod.getCat_id(), new ArrayList<>());
            Objects.requireNonNull(items.get(prod.getCat_id())).add(prod);
        }
        if (items.size() == 0) {
            delivery_charge = 0;
            packageCharge = 0;
        } else {
            dynamicDeliveryChargeCalculation();
            dynamicPackageChargeCalculation();

//            if (SessionData.getInstance().getShippingTaxValue() != 0) {
//                delivery_charge = defaultShippingCharge + SessionData.getInstance().getShippingTaxValue();
//            } else {
//                delivery_charge = defaultShippingCharge;
//            }
//            try {
//                delivery_charge = Double.parseDouble(dec.format(delivery_charge));
//            } catch (Exception e) {
//                delivery_charge = defaultShippingCharge;
//                e.printStackTrace();
//            }
        }
        saveCart();
    }

    public void onRemove(@NotNull Product prod, int qty) {
        List<Product> currentProducts = items.get(prod.getCat_id());
        boolean exist = false;
        int index = 0;
        int prod_qty = productQty.containsKey(prod.getId()) ? productQty.get(prod.getId()) : 0;
        if (currentProducts != null) {
            if (items.size() == 0) {
                delivery_charge = 0;
                packageCharge = 0;
            } else {
                dynamicDeliveryChargeCalculation();
                dynamicPackageChargeCalculation();

//                if (SessionData.getInstance().getShippingTaxValue() != 0) {
//                    delivery_charge = defaultShippingCharge + SessionData.getInstance().getShippingTaxValue();
//                } else {
//                    delivery_charge = defaultShippingCharge;
//                }
//                try {
//                    delivery_charge = Double.parseDouble(dec.format(delivery_charge));
//                } catch (Exception e) {
//                    delivery_charge = defaultShippingCharge;
//                    e.printStackTrace();
//                }
            }
            for (int i = 0; i < currentProducts.size(); i++) {
                if (prod.getId().equals(currentProducts.get(i).getId())) {
                    exist = true;
                    index = i;
                    break;
                }
            }
            if (exist) {
                total_qty -= qty;
                total_price -= prod.getUnit_price();
                str_total_price = dec.format(total_price);
                total_save -= (prod.getOriginal_price() - prod.getUnit_price());
                if (prod_qty - qty == 0) {
                    currentProducts.remove(index);
                    productQty.remove(prod.getId());
                    if (currentProducts.size() == 0) {
                        items.remove(prod.getCat_id());
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        productQty.replace(prod.getId(), prod_qty - qty);
                    } else {
                        productQty.put(prod.getId(), prod_qty - qty);
                    }
                }
            }
        }
        if (items.size() == 0) {
            total_qty = 0;
            total_price = 0;
            str_total_price = dec.format(total_price);
        }
        saveCart();
    }

    public void onDelete(@NotNull Product prod) {
        List<Product> currentProducts = items.get(prod.getCat_id());
        if (items.size() == 0) {
            delivery_charge = 0;
            packageCharge = 0;
        } else {
            dynamicDeliveryChargeCalculation();
            dynamicPackageChargeCalculation();

//            if (SessionData.getInstance().getShippingTaxValue() != 0) {
//                delivery_charge = defaultShippingCharge + SessionData.getInstance().getShippingTaxValue();
//            } else {
//                delivery_charge = defaultShippingCharge;
//            }
//            try {
//                delivery_charge = Double.parseDouble(dec.format(delivery_charge));
//            } catch (Exception e) {
//                delivery_charge = defaultShippingCharge;
//                e.printStackTrace();
//            }
        }
        boolean exist = false;
        int index = 0;
        if (currentProducts != null) {
            for (int i = 0; i < currentProducts.size(); i++) {
                if (prod.getId().equals(currentProducts.get(i).getId())) {
                    exist = true;
                    index = i;
                    break;
                }
            }

            if (exist) {
                total_qty -= productQty.get(prod.getId());
                total_price -= (prod.getUnit_price() * productQty.get(prod.getId()));
                str_total_price = dec.format(total_price);
                total_save -= ((prod.getOriginal_price() - prod.getUnit_price()) * productQty.get(prod.getId()));

                productQty.remove(prod.getId());
                currentProducts.remove(index);
                if (currentProducts.size() <= 0)
                    items.remove(prod.getCat_id());
                else
                    items.put(prod.getCat_id(), currentProducts);
            }
        }

        if (items.size() == 0) {
            total_qty = 0;
            total_price = 0;
            str_total_price = dec.format(total_price);
        }
        saveCart();
    }

    public void saveCart() {
        cartChange.setValue(this);
        setProductQty(productQty); //for data binding
        Config.editPreferences.putString(Constants.CART, JsonUtils.toJsonString(this));
        Config.editPreferences.apply();
    }

    public void clearCart() {
        items = new HashMap<>();
        productQty = new HashMap<>();
        total_qty = 0;
        total_price = 0;
        str_total_price = dec.format(total_price);
        total_save = 0;
        delivery_charge = 0;
        packageCharge = 0;
        saveCart();
    }

    public HashMap<String, List<Product>> getItems() {
        return items;
    }

    public void setItems(HashMap<String, List<Product>> items) {
        this.items = items;
    }

    public double getTotal_price() {
        return total_price;
    }

    public String getStr_total_price() {
        return str_total_price;
    }

    @Bindable
    public HashMap<String, Integer> getProductQty() {
        return productQty;
    }

    public void setProductQty(HashMap<String, Integer> productQty) {
        this.productQty = productQty;
        notifyPropertyChanged(BR.productQty);
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
        str_total_price = dec.format(total_price);

    }

    public double getTotal_save() {
        return total_save;
    }

    public void setTotal_save(double total_save) {
        this.total_save = total_save;
    }

    public int getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(int total_qty) {
        this.total_qty = total_qty;
    }

    public void updateCart(@NotNull Actions action, Product prod, int qty) {
        switch (action) {
            case ADD:
                this.onAdd(prod, qty);
                break;
            case REMOVE:
                this.onRemove(prod, qty);
                break;
            case DELETE:
                this.onDelete(prod);
                break;
        }
    }

    public ArrayList<Product> getProductsAsList() {
        ArrayList<Product> pts = new ArrayList<>();
        for (String key : this.items.keySet()) {
            pts.addAll(Objects.requireNonNull(items.get(key)));
        }
        if (pts.size() > 0) {
            dynamicDeliveryChargeCalculation();
            dynamicPackageChargeCalculation();

//            if (SessionData.getInstance().getShippingTaxValue() != 0) {
//                delivery_charge = defaultShippingCharge + SessionData.getInstance().getShippingTaxValue();
//            } else {
//                delivery_charge = defaultShippingCharge;
//            }
//            try {
//                delivery_charge = Double.parseDouble(dec.format(delivery_charge));
//            } catch (Exception e) {
//                delivery_charge = defaultShippingCharge;
//                e.printStackTrace();
//            }
        }
        return pts;
    }

    public String getTotalTax() {
        return StringUtil.setString("%s", String.format("%.2f", (total_price * SessionData.getInstance().getOverAllTaxValue()) / 100));
    }

    public String getFinalPrice() {

        return StringUtil.setString("%s", (String.format("%.2f", (total_price * SessionData.getInstance().getOverAllTaxValue()) / 100 + total_price + delivery_charge + packageCharge)));
    }

    public double getDelivery_charge() {
        if (delivery_charge != 0) {
            try {
                delivery_charge = Double.parseDouble(dec.format(delivery_charge));
            } catch (Exception e) {
//                delivery_charge = defaultShippingCharge;
                delivery_charge = 0;
                e.printStackTrace();
            }
        }
        return delivery_charge;
    }
    public double getPackageCharge() {
        if (packageCharge != 0) {
            try {
                packageCharge = Double.parseDouble(dec.format(packageCharge));
            } catch (Exception e) {
//                delivery_charge = defaultShippingCharge;
                packageCharge = 0;
                e.printStackTrace();
            }
        }
        return packageCharge;
    }

    public void setDelivery_charge(double delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public void setPackageCharge(double packageCharge) {
        this.packageCharge = packageCharge;
    }

    public void dynamicDeliveryChargeCalculation() {
        if (SessionData.getInstance().getShippingTaxValue() != 0) {
            delivery_charge = defaultShippingCharge + SessionData.getInstance().getShippingTaxValue();
        } else {
            delivery_charge = defaultShippingCharge;
        }
        try {
            delivery_charge = Double.parseDouble(dec.format(delivery_charge));
        } catch (Exception e) {
            delivery_charge = defaultShippingCharge;
            e.printStackTrace();
        }
    }
    public void dynamicPackageChargeCalculation() {
        if (SessionData.getInstance().getShop()!=null) {
            if (Float.parseFloat(SessionData.getInstance().getShop().getPackage_charges()) != 0) {
                packageCharge = defaultPackageCharge +Float.parseFloat( SessionData.getInstance().getShop().getPackage_charges());
            } else {
                packageCharge = defaultPackageCharge;
            }
        }
        try {
            packageCharge = Double.parseDouble(dec.format(packageCharge));
        } catch (Exception e) {
            packageCharge = defaultPackageCharge;
            e.printStackTrace();
        }
    }
}
