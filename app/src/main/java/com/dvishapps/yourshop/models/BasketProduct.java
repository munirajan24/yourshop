package com.dvishapps.yourshop.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class BasketProduct implements Serializable {

    @Expose
    private String shop_id;

    @Expose
    private String product_id;

    @Expose
    private String product_name;

    @Expose
    private String product_attribute_id;

    @Expose
    private String product_attribute_name;

    @Expose
    private String product_attribute_price;

    @Expose
    private String product_color_id;

    @Expose
    private String product_color_code;

    @Expose
    private String product_unit;

    @Expose
    private String product_measurement;

    @Expose
    private String shipping_cost;

    @Expose
    private String unit_price;

    @Expose
    private String original_price;

    @Expose
    private String discount_price;

    @Expose
    private String discount_amount;

    @Expose
    private String qty;

    @Expose
    private String discount_value;

    @Expose
    private String discount_percent;

    @Expose
    private String currency_short_form;

    @Expose
    private String currency_symbol;

//    public BasketProduct() {
//    }


    public BasketProduct(String shop_id, String product_id, String product_name, String product_attribute_id, String product_attribute_name,
                         String product_attribute_price, String product_color_id, String product_color_code, String product_unit,
                         String product_measurement, String shipping_cost, String unit_price, String original_price, String discount_price, String discount_amount, String qty,
                         String discount_value, String discount_percent, String currency_short_form, String currency_symbol) {
        this.shop_id = shop_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_attribute_id = product_attribute_id;
        this.product_attribute_name = product_attribute_name;
        this.product_attribute_price = product_attribute_price;
        this.product_color_id = product_color_id;
        this.product_color_code = product_color_code;
        this.product_unit = product_unit;
        this.product_measurement = product_measurement;
        this.shipping_cost = shipping_cost;
        this.unit_price = unit_price;
        this.original_price = original_price;
        this.discount_price = discount_price;
        this.discount_amount = discount_amount;
        this.qty = qty;
        this.discount_value = discount_value;
        this.discount_percent = discount_percent;
        this.currency_short_form = currency_short_form;
        this.currency_symbol = currency_symbol;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_attribute_id() {
        return product_attribute_id;
    }

    public void setProduct_attribute_id(String product_attribute_id) {
        this.product_attribute_id = product_attribute_id;
    }

    public String getProduct_attribute_name() {
        return product_attribute_name;
    }

    public void setProduct_attribute_name(String product_attribute_name) {
        this.product_attribute_name = product_attribute_name;
    }

    public String getProduct_attribute_price() {
        return product_attribute_price;
    }

    public void setProduct_attribute_price(String product_attribute_price) {
        this.product_attribute_price = product_attribute_price;
    }

    public String getProduct_color_id() {
        return product_color_id;
    }

    public void setProduct_color_id(String product_color_id) {
        this.product_color_id = product_color_id;
    }

    public String getProduct_color_code() {
        return product_color_code;
    }

    public void setProduct_color_code(String product_color_code) {
        this.product_color_code = product_color_code;
    }

    public String getProduct_unit() {
        return product_unit;
    }

    public void setProduct_unit(String product_unit) {
        this.product_unit = product_unit;
    }

    public String getProduct_measurement() {
        return product_measurement;
    }

    public void setProduct_measurement(String product_measurement) {
        this.product_measurement = product_measurement;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(String discount_value) {
        this.discount_value = discount_value;
    }

    public String getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(String discount_percent) {
        this.discount_percent = discount_percent;
    }

    public String getCurrency_short_form() {
        return currency_short_form;
    }

    public void setCurrency_short_form(String currency_short_form) {
        this.currency_short_form = currency_short_form;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    @Override
    public String toString() {
        return "BasketProduct{" +
                "shop_id='" + shop_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_attribute_id='" + product_attribute_id + '\'' +
                ", product_attribute_name='" + product_attribute_name + '\'' +
                ", product_attribute_price='" + product_attribute_price + '\'' +
                ", product_color_id='" + product_color_id + '\'' +
                ", product_color_code='" + product_color_code + '\'' +
                ", product_unit='" + product_unit + '\'' +
                ", product_measurement='" + product_measurement + '\'' +
                ", shipping_cost='" + shipping_cost + '\'' +
                ", unit_price='" + unit_price + '\'' +
                ", original_price='" + original_price + '\'' +
                ", discount_price='" + discount_price + '\'' +
                ", discount_amount='" + discount_amount + '\'' +
                ", qty='" + qty + '\'' +
                ", discount_value='" + discount_value + '\'' +
                ", discount_percent='" + discount_percent + '\'' +
                ", currency_short_form='" + currency_short_form + '\'' +
                ", currency_symbol='" + currency_symbol + '\'' +
                '}';
    }
}
