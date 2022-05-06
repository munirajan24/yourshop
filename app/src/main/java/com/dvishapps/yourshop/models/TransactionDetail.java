package com.dvishapps.yourshop.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class TransactionDetail implements Serializable {
    @NonNull
    @Expose
    private String id;

    @Expose
    private String transactions_header_id;

    @Expose
    private String product_id;

    @Expose
    private String product_name;

    @Expose
    private String product_unit;

    @Expose
    private String product_measurement;

    @Expose
    private String shipping_cost;

    @Expose
    private String product_attribute_id;

    @Expose
    private String product_attribute_name;

    @Expose
    private String original_price;

    @Expose
    private String price;

    @Expose
    private String discount_amount;

    @Expose
    private String qty;

    @Expose
    private String color_id;

    @Expose
    private String discount_value;

    @Expose
    private String discount_percent;

    @Expose
    private String added_date;

    @Expose
    private String added_user_id;

    @Expose
    private String updated_date;

    @Expose
    private String updated_user_id;

    @Expose
    private String updated_flag;

    @Expose
    private String currency_symbol;

    @Expose
    private String currency_short_form;

    @Expose
    private String added_date_str;

    @Expose
    private String updated_date_str;

    @Expose
    private String product_color_id;

    @Expose
    private String product_color_code;

    public TransactionDetail(@NonNull String id, String transactions_header_id, String product_id, String product_name, String product_unit, String product_measurement, String shipping_cost, String product_attribute_id, String product_attribute_name, String original_price, String price, String discount_amount, String qty, String color_id, String discount_value, String discount_percent, String added_date, String added_user_id, String updated_date, String updated_user_id, String updated_flag, String currency_symbol, String currency_short_form, String added_date_str, String updated_date_str, String product_color_id, String product_color_code) {
        this.id = id;
        this.transactions_header_id = transactions_header_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_unit = product_unit;
        this.product_measurement = product_measurement;
        this.shipping_cost = shipping_cost;
        this.product_attribute_id = product_attribute_id;
        this.product_attribute_name = product_attribute_name;
        this.original_price = original_price;
        this.price = price;
        this.discount_amount = discount_amount;
        this.qty = qty;
        this.color_id = color_id;
        this.discount_value = discount_value;
        this.discount_percent = discount_percent;
        this.added_date = added_date;
        this.added_user_id = added_user_id;
        this.updated_date = updated_date;
        this.updated_user_id = updated_user_id;
        this.updated_flag = updated_flag;
        this.currency_symbol = currency_symbol;
        this.currency_short_form = currency_short_form;
        this.added_date_str = added_date_str;
        this.updated_date_str = updated_date_str;
        this.product_color_id = product_color_id;
        this.product_color_code = product_color_code;
    }

    @Override
    public String toString() {
        return "TransactionDetail{" +
                "id='" + id + '\'' +
                ", transactions_header_id='" + transactions_header_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_unit='" + product_unit + '\'' +
                ", product_measurement='" + product_measurement + '\'' +
                ", shipping_cost='" + shipping_cost + '\'' +
                ", product_attribute_id='" + product_attribute_id + '\'' +
                ", product_attribute_name='" + product_attribute_name + '\'' +
                ", original_price='" + original_price + '\'' +
                ", price='" + price + '\'' +
                ", discount_amount='" + discount_amount + '\'' +
                ", qty='" + qty + '\'' +
                ", color_id='" + color_id + '\'' +
                ", discount_value='" + discount_value + '\'' +
                ", discount_percent='" + discount_percent + '\'' +
                ", added_date='" + added_date + '\'' +
                ", added_user_id='" + added_user_id + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", updated_user_id='" + updated_user_id + '\'' +
                ", updated_flag='" + updated_flag + '\'' +
                ", currency_symbol='" + currency_symbol + '\'' +
                ", currency_short_form='" + currency_short_form + '\'' +
                ", added_date_str='" + added_date_str + '\'' +
                ", updated_date_str='" + updated_date_str + '\'' +
                ", product_color_id='" + product_color_id + '\'' +
                ", product_color_code='" + product_color_code + '\'' +
                '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTransactions_header_id() {
        return transactions_header_id;
    }

    public void setTransactions_header_id(String transactions_header_id) {
        this.transactions_header_id = transactions_header_id;
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

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
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

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getAdded_user_id() {
        return added_user_id;
    }

    public void setAdded_user_id(String added_user_id) {
        this.added_user_id = added_user_id;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getUpdated_user_id() {
        return updated_user_id;
    }

    public void setUpdated_user_id(String updated_user_id) {
        this.updated_user_id = updated_user_id;
    }

    public String getUpdated_flag() {
        return updated_flag;
    }

    public void setUpdated_flag(String updated_flag) {
        this.updated_flag = updated_flag;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getCurrency_short_form() {
        return currency_short_form;
    }

    public void setCurrency_short_form(String currency_short_form) {
        this.currency_short_form = currency_short_form;
    }

    public String getAdded_date_str() {
        return added_date_str;
    }

    public void setAdded_date_str(String added_date_str) {
        this.added_date_str = added_date_str;
    }

    public String getUpdated_date_str() {
        return updated_date_str;
    }

    public void setUpdated_date_str(String updated_date_str) {
        this.updated_date_str = updated_date_str;
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
}
