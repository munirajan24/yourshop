package com.dvishapps.yourshop.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class Transaction implements Serializable {

    @Expose
    private String id;

    @Expose
    private String user_id;

    @Expose
    private String sub_total_amount;

    @Expose
    private String discount_amount;

    @Expose
    private String coupon_discount_amount;

    @Expose
    private String tax_amount;

    @Expose
    private String shipping_amount;

    @Expose
    private String balance_amount;

    @Expose
    private String total_item_amount;

    @Expose
    private String contact_name;

    @Expose
    private String contact_phone;

    @Expose
    private String is_cod;

    @Expose
    private String is_paypal;

    @Expose
    private String is_stripe;

    @Expose
    private String is_bank;                 //TODO : Using is_bank as a parameter of razor

    @Expose
    private String payment_method_nonce;

    @Expose
    private String trans_status_id="";

    @Expose
    private String currency_symbol;

    @Expose
    private String currency_short_form;

    @Expose
    private String billing_first_name;

    @Expose
    private String billing_last_name;

    @Expose
    private String billing_company;

    @Expose
    private String billing_address_1;

    @Expose
    private String billing_address_2;

    @Expose
    private String billing_country;

    @Expose
    private String billing_state;

    @Expose
    private String billing_city;

    @Expose
    private String billing_postal_code;

    @Expose
    private String billing_email;

    @Expose
    private String billing_phone;
//    @Expose
//    private String is_rozar;
    @Expose
    private String shipping_first_name;

    @Expose
    private String shipping_last_name;

    @Expose
    private String shipping_company;

    @Expose
    private String shipping_address_1;

    @Expose
    private String shipping_address_2;

    @Expose
    private String shipping_country;

    @Expose
    private String shipping_state;

    @Expose
    private String shipping_city;

    @Expose
    private String shipping_postal_code;

    @Expose
    private String shipping_email="";

    @Expose
    private String shipping_phone;

    @Expose
    private String shipping_tax_percent;

    @Expose
    private String tax_percent;

    @Expose
    private String shipping_method_amount;

    @Expose
    private String shipping_method_name;

    @Expose
    private String trans_status_title;

    @Expose
    private String trans_code;

    @Expose
    private String memo;

    @Expose
    private String total_item_count;

    @Expose
    private String zone_shipping_enable;

    @Expose
    private String added_date;

    @Expose
    private String shop_id;
    @Expose
    private String razor_payment_id;
    @Expose
    private String razor_payment_status="";


    @Expose
    private String transaction_location="";
    @Expose
    private String package_charges="";
    @Expose
    private List<BasketProduct> details;

    public Transaction() {
    }

    public Transaction(String user_id, String sub_total_amount,
                       String discount_amount, String coupon_discount_amount, String tax_amount,
                       String shipping_amount, String balance_amount,
                       String total_item_amount, String contact_name,
                       String contact_phone, String is_cod, String is_paypal,/*String is_rozar,*/
                       String is_stripe, String is_bank, String payment_method_nonce,
                       String trans_status_id, String currency_symbol, String currency_short_form,
                       String billing_first_name, String billing_last_name, String billing_company,
                       String billing_address_1, String billing_address_2, String billing_country, String billing_state,
                       String billing_city, String billing_postal_code, String billing_email, String billing_phone,
                       String shipping_first_name, String shipping_last_name, String shipping_company, String shipping_address_1,
                       String shipping_address_2, String shipping_country, String shipping_state, String shipping_city,
                       String shipping_postal_code, String shipping_email, String shipping_phone, String shipping_tax_percent,
                       String tax_percent, String shipping_method_amount, String shipping_method_name, String memo,
                       String total_item_count, String zone_shipping_enable,String added_date,String shop_id,
                       List<BasketProduct> details,String rozar_payment_id,String rozar_payment_status,String transaction_location,String package_charges) {
        this.user_id = user_id;
        this.sub_total_amount = sub_total_amount;
        this.discount_amount = discount_amount;
        this.coupon_discount_amount = coupon_discount_amount;
        this.tax_amount = tax_amount;
        this.shipping_amount = shipping_amount;
        this.balance_amount = balance_amount;
        this.total_item_amount = total_item_amount;
        this.contact_name = contact_name;
        this.contact_phone = contact_phone;
        this.is_cod = is_cod;
        this.is_paypal = is_paypal;
        this.is_stripe = is_stripe;
        this.is_bank = is_bank;
//        this.is_rozar = is_rozar;
        this.payment_method_nonce = payment_method_nonce;
        this.trans_status_id = trans_status_id;
        this.currency_symbol = currency_symbol;
        this.currency_short_form = currency_short_form;
        this.billing_first_name = billing_first_name;
        this.billing_last_name = billing_last_name;
        this.billing_company = billing_company;
        this.billing_address_1 = billing_address_1;
        this.billing_address_2 = billing_address_2;
        this.billing_country = billing_country;
        this.billing_state = billing_state;
        this.billing_city = billing_city;
        this.billing_postal_code = billing_postal_code;
        this.billing_email = billing_email;
        this.billing_phone = billing_phone;
        this.shipping_first_name = shipping_first_name;
        this.shipping_last_name = shipping_last_name;
        this.shipping_company = shipping_company;
        this.shipping_address_1 = shipping_address_1;
        this.shipping_address_2 = shipping_address_2;
        this.shipping_country = shipping_country;
        this.shipping_state = shipping_state;
        this.shipping_city = shipping_city;
        this.shipping_postal_code = shipping_postal_code;
        this.shipping_email = shipping_email;
        this.shipping_phone = shipping_phone;
        this.shipping_tax_percent = shipping_tax_percent;
        this.tax_percent = tax_percent;
        this.shipping_method_amount = shipping_method_amount;
        this.shipping_method_name = shipping_method_name;
        this.memo = memo;
        this.total_item_count = total_item_count;
        this.zone_shipping_enable = zone_shipping_enable;
        this.added_date = added_date;
        this.shop_id = shop_id;
        this.details = details;
        this.razor_payment_id =rozar_payment_id;
        this.razor_payment_status =rozar_payment_status;
        this.transaction_location =transaction_location;
        this.package_charges =package_charges;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public String getCurrency_short_form() {
        return currency_short_form;
    }

    public String getTrans_status_title() {
        return trans_status_title;
    }

    public void setTrans_status_title(String trans_status_title) {
        this.trans_status_title = trans_status_title;
    }

    public String getTrans_code() {
        return trans_code;
    }

    public void setTrans_code(String trans_code) {
        this.trans_code = trans_code;
    }

    public String getTotal_item_count() {
        return total_item_count;
    }

    public void setTotal_item_count(String total_item_count) {
        this.total_item_count = total_item_count;
    }

    public String getZone_shipping_enable() {
        return zone_shipping_enable;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getSub_total_amount() {
        return sub_total_amount;
    }

    public String getDiscountAmount() {
        return discount_amount;
    }

    public String getCoupon_discount_amount() {
        return coupon_discount_amount;
    }

    public String getTax_amount() {
        return tax_amount;
    }

    public String getShipping_amount() {
        return shipping_amount;
    }

    public String getBalance_amount() {
        return balance_amount;
    }

    public String getTotal_item_amount() {
        return total_item_amount;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public String getIs_cod() {
        return is_cod;
    }

    public String getIs_paypal() {
        return is_paypal;
    }

    public String getIs_stripe() {
        return is_stripe;
    }

    public String getIs_bank() {
        return is_bank;
    }

    public String getPayment_method_nonce() {
        return payment_method_nonce;
    }

    public String getTrans_status_id() {
        return trans_status_id;
    }

    public String getCurrencySymbol() {
        return currency_symbol;
    }

    public String getCurrencyShortForm() {
        return currency_short_form;
    }

    public String getBilling_first_name() {
        return billing_first_name;
    }

    public String getBilling_last_name() {
        return billing_last_name;
    }

    public String getBilling_company() {
        return billing_company;
    }

    public String getBilling_address_1() {
        return billing_address_1;
    }

    public String getBilling_address_2() {
        return billing_address_2;
    }

    public String getBilling_country() {
        return billing_country;
    }

    public String getBilling_state() {
        return billing_state;
    }

    public String getBilling_city() {
        return billing_city;
    }

    public String getBilling_postal_code() {
        return billing_postal_code;
    }

    public String getBilling_email() {
        return billing_email;
    }

    public String getBilling_phone() {
        return billing_phone;
    }

    public String getShipping_first_name() {
        return shipping_first_name;
    }

    public String getShipping_last_name() {
        return shipping_last_name;
    }

    public String getShipping_company() {
        return shipping_company;
    }

    public String getShipping_address_1() {
        return shipping_address_1;
    }

    public String getShipping_address_2() {
        return shipping_address_2;
    }

    public String getShipping_country() {
        return shipping_country;
    }

    public String getShipping_state() {
        return shipping_state;
    }

    public String getShipping_city() {
        return shipping_city;
    }

    public String getShipping_postal_code() {
        return shipping_postal_code;
    }

    public String getShipping_email() {
        return shipping_email;
    }

    public String getShipping_phone() {
        return shipping_phone;
    }

    public String getShipping_tax_percent() {
        return shipping_tax_percent;
    }

    public String getTax_percent() {
        return tax_percent;
    }

    public String getShipping_method_amount() {
        return shipping_method_amount;
    }

    public String getShipping_method_name() {
        return shipping_method_name;
    }

    public String getMemo() {
        return memo;
    }

    public String gettotal_item_count() {
        return total_item_count;
    }

    public String getZoneShippingEnable() {
        return zone_shipping_enable;
    }

    public List<BasketProduct> getDetails() {
        return details;
    }


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setSub_total_amount(String sub_total_amount) {
        this.sub_total_amount = sub_total_amount;
    }

    public void setDiscountAmount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public void setCoupon_discount_amount(String coupon_discount_amount) {
        this.coupon_discount_amount = coupon_discount_amount;
    }

    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

    public void setShipping_amount(String shipping_amount) {
        this.shipping_amount = shipping_amount;
    }

    public void setBalance_amount(String balance_amount) {
        this.balance_amount = balance_amount;
    }

    public void setTotal_item_amount(String total_item_amount) {
        this.total_item_amount = total_item_amount;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public void setIs_cod(String is_cod) {
        this.is_cod = is_cod;
    }

    public void setIs_paypal(String is_paypal) {
        this.is_paypal = is_paypal;
    }

    public void setIs_stripe(String is_stripe) {
        this.is_stripe = is_stripe;
    }

    public void setIs_bank(String is_bank) {
        this.is_bank = is_bank;
    }

    public void setPayment_method_nonce(String payment_method_nonce) {
        this.payment_method_nonce = payment_method_nonce;
    }

    public void setTrans_status_id(String trans_status_id) {
        this.trans_status_id = trans_status_id;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public void setCurrency_short_form(String currency_short_form) {
        this.currency_short_form = currency_short_form;
    }

    public void setBilling_first_name(String billing_first_name) {
        this.billing_first_name = billing_first_name;
    }

    public void setBilling_last_name(String billing_last_name) {
        this.billing_last_name = billing_last_name;
    }

    public void setBilling_company(String billing_company) {
        this.billing_company = billing_company;
    }

    public void setBilling_address_1(String billing_address_1) {
        this.billing_address_1 = billing_address_1;
    }

    public void setBilling_address_2(String billing_address_2) {
        this.billing_address_2 = billing_address_2;
    }

    public void setBilling_country(String billing_country) {
        this.billing_country = billing_country;
    }

    public void setBilling_state(String billing_state) {
        this.billing_state = billing_state;
    }

    public void setBilling_city(String billing_city) {
        this.billing_city = billing_city;
    }

    public void setBilling_postal_code(String billing_postal_code) {
        this.billing_postal_code = billing_postal_code;
    }

    public void setBilling_email(String billing_email) {
        this.billing_email = billing_email;
    }

    public void setBilling_phone(String billing_phone) {
        this.billing_phone = billing_phone;
    }

    public void setShipping_first_name(String shipping_first_name) {
        this.shipping_first_name = shipping_first_name;
    }

    public void setShipping_last_name(String shipping_last_name) {
        this.shipping_last_name = shipping_last_name;
    }

    public void setShipping_company(String shipping_company) {
        this.shipping_company = shipping_company;
    }

    public void setShipping_address_1(String shipping_address_1) {
        this.shipping_address_1 = shipping_address_1;
    }

    public void setShipping_address_2(String shipping_address_2) {
        this.shipping_address_2 = shipping_address_2;
    }

    public void setShipping_country(String shipping_country) {
        this.shipping_country = shipping_country;
    }

    public void setShipping_state(String shipping_state) {
        this.shipping_state = shipping_state;
    }

    public void setShipping_city(String shipping_city) {
        this.shipping_city = shipping_city;
    }

    public void setShipping_postal_code(String shipping_postal_code) {
        this.shipping_postal_code = shipping_postal_code;
    }

    public void setShipping_email(String shipping_email) {
        this.shipping_email = shipping_email;
    }

    public void setShipping_phone(String shipping_phone) {
        this.shipping_phone = shipping_phone;
    }

    public void setShipping_tax_percent(String shipping_tax_percent) {
        this.shipping_tax_percent = shipping_tax_percent;
    }

    public void setTax_percent(String tax_percent) {
        this.tax_percent = tax_percent;
    }

    public void setShipping_method_amount(String shipping_method_amount) {
        this.shipping_method_amount = shipping_method_amount;
    }

    public void setShipping_method_name(String shipping_method_name) {
        this.shipping_method_name = shipping_method_name;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setZone_shipping_enable(String zone_shipping_enable) {
        this.zone_shipping_enable = zone_shipping_enable;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public void setDetails(List<BasketProduct> details) {
        this.details = details;
    }

//    public String getIs_rozar() {
//        return is_rozar;
//    }
//
//    public void setIs_rozar(String is_rozar) {
//        this.is_rozar = is_rozar;
//    }

    public String getRazor_payment_id() {
        return razor_payment_id;
    }

    public void setRazor_payment_id(String razor_payment_id) {
        this.razor_payment_id = razor_payment_id;
    }

    public String getRazor_payment_status() {
        return razor_payment_status;
    }

    public void setRazor_payment_status(String razor_payment_status) {
        this.razor_payment_status = razor_payment_status;
    }

    public String getTransaction_location() {
        return transaction_location;
    }

    public void setTransaction_location(String transaction_location) {
        this.transaction_location = transaction_location;
    }

    public String getPackage_charges() {
        return package_charges;
    }

    public void setPackage_charges(String package_charges) {
        this.package_charges = package_charges;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "user_id='" + user_id + '\'' +
                ", sub_total_amount='" + sub_total_amount + '\'' +
                ", discount_amount='" + discount_amount + '\'' +
                ", coupon_discount_amount='" + coupon_discount_amount + '\'' +
                ", tax_amount='" + tax_amount + '\'' +
                ", shipping_amount='" + shipping_amount + '\'' +
                ", balance_amount='" + balance_amount + '\'' +
                ", total_item_amount='" + total_item_amount + '\'' +
                ", contact_name='" + contact_name + '\'' +
                ", contact_phone='" + contact_phone + '\'' +
                ", is_cod='" + is_cod + '\'' +
                ", is_paypal='" + is_paypal + '\'' +
                ", is_stripe='" + is_stripe + '\'' +
                ", is_bank='" + is_bank + '\'' +
                ", payment_method_nonce='" + payment_method_nonce + '\'' +
                ", trans_status_id='" + trans_status_id + '\'' +
                ", currency_symbol='" + currency_symbol + '\'' +
                ", currency_short_form='" + currency_short_form + '\'' +
                ", billing_first_name='" + billing_first_name + '\'' +
                ", billing_last_name='" + billing_last_name + '\'' +
                ", billing_company='" + billing_company + '\'' +
                ", billing_address_1='" + billing_address_1 + '\'' +
                ", billing_address_2='" + billing_address_2 + '\'' +
                ", billing_country='" + billing_country + '\'' +
                ", billing_state='" + billing_state + '\'' +
                ", billing_city='" + billing_city + '\'' +
                ", billing_postal_code='" + billing_postal_code + '\'' +
                ", billing_email='" + billing_email + '\'' +
                ", billing_phone='" + billing_phone + '\'' +
                ", shipping_first_name='" + shipping_first_name + '\'' +
                ", shipping_last_name='" + shipping_last_name + '\'' +
                ", shipping_company='" + shipping_company + '\'' +
                ", shipping_address_1='" + shipping_address_1 + '\'' +
                ", shipping_address_2='" + shipping_address_2 + '\'' +
                ", shipping_country='" + shipping_country + '\'' +
                ", shipping_state='" + shipping_state + '\'' +
                ", shipping_city='" + shipping_city + '\'' +
                ", shipping_postal_code='" + shipping_postal_code + '\'' +
                ", shipping_email='" + shipping_email + '\'' +
                ", shipping_phone='" + shipping_phone + '\'' +
                ", shipping_tax_percent='" + shipping_tax_percent + '\'' +
                ", tax_percent='" + tax_percent + '\'' +
                ", shipping_method_amount='" + shipping_method_amount + '\'' +
                ", shipping_method_name='" + shipping_method_name + '\'' +
                ", trans_status_title='" + trans_status_title + '\'' +
                ", trans_code='" + trans_code + '\'' +
                ", memo='" + memo + '\'' +
                ", total_item_count='" + total_item_count + '\'' +
                ", zone_shipping_enable='" + zone_shipping_enable + '\'' +
                ", added_date='" + added_date + '\'' +
                ", details=" + details +
                ", shop_id=" + shop_id +
                ", razor_payment_id=" + razor_payment_id +
                ", razor_payment_status=" + razor_payment_status +
                ", transaction_location=" + transaction_location +
                ", package_charges=" + package_charges +
                '}';
    }
}
