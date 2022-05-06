package com.dvishapps.yourshop.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Shop implements Serializable {
    @NonNull
    @Expose
    private String id;
    @Expose
    private String shipping_id;
    @Expose
    private String name;
    @Expose
    private String individual_app = "";
    @Expose
    private String description;
    @Expose
    private String city = "";
    @Expose
    private String area = "";
    @Expose
    private String postal_code = "";
    @Expose
    private String coordinate;
    @Expose
    private String lat="";
    @Expose
    private String lng;
    @Expose
    private String cod_needed_type;
    @Expose
    private String cod_charge_from;
    @Expose
    private String paypal_email;
    @Expose
    private String paypal_environment;
    @Expose
    private String paypal_appid_live;
    @Expose
    private String paypal_merchantname;
    @Expose
    private String paypal_customerid;
    @Expose
    private String paypal_ipnurl;
    @Expose
    private String paypal_memo;
    @Expose
    private String paypal_merchant_id;
    @Expose
    private String email;
    @Expose
    private String paypal_public_key;
    @Expose
    private String paypal_private_key;
    @Expose
    private String bank_account;
    @Expose
    private String bank_name;
    @Expose
    private String bank_code;
    @Expose
    private String branch_code;
    @Expose
    private String swift_code;
    @Expose
    private String cod_email;
    @Expose
    private String stripe_publishable_key;
    @Expose
    private String stripe_secret_key;
    @Expose
    private String currency_symbol;
    @Expose
    private String currency_short_form;
    @Expose
    private String sender_email;
    @Expose
    private String added_date;
    @Expose
    private String status="";
    @Expose
    private String paypal_enabled;
    @Expose
    private String stripe_enabled;
    @Expose
    private String cod_enabled;
    @Expose
    private String banktransfer_enabled;
    @Expose
    private String is_featured;
    @Expose
    private String pinterest;
    @Expose
    private String youtube;
    @Expose
    private String instagram;
    @Expose
    private String twitter;
    @Expose
    private String google_plus;
    @Expose
    private String facebook;
    @Expose
    private String about_website;
    @Expose
    private String about_phone1;
    @Expose
    private String about_phone2;
    @Expose
    private String about_phone3;
    @Expose
    private String overall_tax_label;
    @Expose
    private float overall_tax_value;
    @Expose
    private String shipping_tax_label;
    @Expose
    private float shipping_tax_value;

    @Expose
    private String whapsapp_no;

    @Expose
    private String refund_policy;

    @Expose
    private String privacy_policy;

    @Expose
    private String terms;

    @Expose
    private String address1;

    @Expose
    private String address2;

    @Expose
    private String address3;

    @Expose
    private String added_user_id;

    @Expose
    private String updated_date;

    @Expose
    private String updated_user_id;

    @Expose
    private String featured_date;

    @Expose
    private String added_date_str;

    @Expose
    private Photo default_photo;

    @Expose
    private Photo default_icon;

    @Expose
    private String touch_count;

    @Expose
    private String messenger;

    @Expose
    private String standard_shipping_enable;

    @Expose
    private String zone_shipping_enable;

    @Expose
    private String no_shipping_enable;


    //--------------------------------------------

    @Expose
    private String shop_current_version = "";

    @Expose
    private String shop_open_time = "";

    @Expose
    private String shop_close_time = "";

    @Expose
    private String shop_pin_number = "";

    @Expose
    private String shop_app_force_update = "";

    @Expose
    private String shop_bussiness_type = "";

    @Expose
    private String plan_type="";                //0- premium , 1-classic ,2-basic

    @Expose
    private String shop_main_branch_id = "";

    @Expose
    private String shop_contract_form = "";

    @Expose
    private String shop_branch = "";



    @Expose
    private String is_enable = "";

    @Expose
    private String package_charges = "";


    @Expose
    private double distance = 0;


    public Shop() {
    }

    public Shop(@NonNull String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Shop(@NonNull String id, String shipping_id, String name, String individual_app, String description, String city, String area, String postal_code, String coordinate, String lat, String lng, String cod_needed_type, String cod_charge_from, String paypal_email, String paypal_environment, String paypal_appid_live, String paypal_merchantname, String paypal_customerid, String paypal_ipnurl, String paypal_memo, String paypal_merchant_id, String email, String paypal_public_key, String paypal_private_key, String bank_account, String bank_name, String bank_code, String branch_code, String swift_code, String cod_email, String stripe_publishable_key, String stripe_secret_key, String currency_symbol, String currency_short_form, String sender_email, String added_date, String status, String paypal_enabled, String stripe_enabled, String cod_enabled, String banktransfer_enabled, String is_featured, String pinterest, String youtube, String instagram, String twitter, String google_plus, String facebook, String about_website, String about_phone1, String about_phone2, String about_phone3, String overall_tax_label, float overall_tax_value, String shipping_tax_label, float shipping_tax_value, String whapsapp_no, String refund_policy, String privacy_policy, String terms, String address1, String address2, String address3, String added_user_id, String updated_date, String updated_user_id, String featured_date, String added_date_str, Photo default_photo, Photo default_icon, String touch_count, String messenger, String standard_shipping_enable, String zone_shipping_enable, String no_shipping_enable,
                String shop_current_version, String shop_open_time,
                String shop_close_time, String shop_pin_number, String shop_app_force_update,
                String shop_bussiness_type, String shop_main_branch_id,
                String shop_contract_form, String shop_branch, String is_enable, String package_charges, String plan_type
    ) {
        this.id = id;
        this.shipping_id = shipping_id;
        this.name = name;
        this.individual_app = individual_app;
        this.description = description;
        this.city = city;
        this.area = area;
        this.postal_code = postal_code;
        this.coordinate = coordinate;
        this.lat = lat;
        this.lng = lng;
        this.cod_needed_type = cod_needed_type;
        this.cod_charge_from = cod_charge_from;
        this.paypal_email = paypal_email;
        this.paypal_environment = paypal_environment;
        this.paypal_appid_live = paypal_appid_live;
        this.paypal_merchantname = paypal_merchantname;
        this.paypal_customerid = paypal_customerid;
        this.paypal_ipnurl = paypal_ipnurl;
        this.paypal_memo = paypal_memo;
        this.paypal_merchant_id = paypal_merchant_id;
        this.email = email;
        this.paypal_public_key = paypal_public_key;
        this.paypal_private_key = paypal_private_key;
        this.bank_account = bank_account;
        this.bank_name = bank_name;
        this.bank_code = bank_code;
        this.branch_code = branch_code;
        this.swift_code = swift_code;
        this.cod_email = cod_email;
        this.stripe_publishable_key = stripe_publishable_key;
        this.stripe_secret_key = stripe_secret_key;
        this.currency_symbol = currency_symbol;
        this.currency_short_form = currency_short_form;
        this.sender_email = sender_email;
        this.added_date = added_date;
        this.status = status;
        this.paypal_enabled = paypal_enabled;
        this.stripe_enabled = stripe_enabled;
        this.cod_enabled = cod_enabled;
        this.banktransfer_enabled = banktransfer_enabled;
        this.is_featured = is_featured;
        this.pinterest = pinterest;
        this.youtube = youtube;
        this.instagram = instagram;
        this.twitter = twitter;
        this.google_plus = google_plus;
        this.facebook = facebook;
        this.about_website = about_website;
        this.about_phone1 = about_phone1;
        this.about_phone2 = about_phone2;
        this.about_phone3 = about_phone3;
        this.overall_tax_label = overall_tax_label;
        this.overall_tax_value = overall_tax_value;
        this.shipping_tax_label = shipping_tax_label;
        this.shipping_tax_value = shipping_tax_value;
        this.whapsapp_no = whapsapp_no;
        this.refund_policy = refund_policy;
        this.privacy_policy = privacy_policy;
        this.terms = terms;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.added_user_id = added_user_id;
        this.updated_date = updated_date;
        this.updated_user_id = updated_user_id;
        this.featured_date = featured_date;
        this.added_date_str = added_date_str;
        this.default_photo = default_photo;
        this.default_icon = default_icon;
        this.touch_count = touch_count;
        this.messenger = messenger;
        this.standard_shipping_enable = standard_shipping_enable;
        this.zone_shipping_enable = zone_shipping_enable;
        this.no_shipping_enable = no_shipping_enable;

        this.shop_current_version = shop_current_version;
        this.shop_open_time = shop_open_time;
        this.shop_close_time = shop_close_time;
        this.shop_pin_number = shop_pin_number;
        this.shop_app_force_update = shop_app_force_update;
        this.shop_bussiness_type = shop_bussiness_type;
        this.shop_main_branch_id = shop_main_branch_id;
        this.shop_contract_form = shop_contract_form;
        this.shop_branch = shop_branch;
        this.is_enable = is_enable;
        this.package_charges = package_charges;
        this.plan_type = plan_type;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id='" + id + '\'' +
                ", shipping_id='" + shipping_id + '\'' +
                ", name='" + name + '\'' +
                ", individual_app='" + individual_app + '\'' +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", coordinate='" + coordinate + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", cod_needed_type='" + cod_needed_type + '\'' +
                ", cod_charge_from='" + cod_charge_from + '\'' +
                ", paypal_email='" + paypal_email + '\'' +
                ", paypal_environment='" + paypal_environment + '\'' +
                ", paypal_appid_live='" + paypal_appid_live + '\'' +
                ", paypal_merchantname='" + paypal_merchantname + '\'' +
                ", paypal_customerid='" + paypal_customerid + '\'' +
                ", paypal_ipnurl='" + paypal_ipnurl + '\'' +
                ", paypal_memo='" + paypal_memo + '\'' +
                ", paypal_merchant_id='" + paypal_merchant_id + '\'' +
                ", email='" + email + '\'' +
                ", paypal_public_key='" + paypal_public_key + '\'' +
                ", paypal_private_key='" + paypal_private_key + '\'' +
                ", bank_account='" + bank_account + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", bank_code='" + bank_code + '\'' +
                ", branch_code='" + branch_code + '\'' +
                ", swift_code='" + swift_code + '\'' +
                ", cod_email='" + cod_email + '\'' +
                ", stripe_publishable_key='" + stripe_publishable_key + '\'' +
                ", stripe_secret_key='" + stripe_secret_key + '\'' +
                ", currency_symbol='" + currency_symbol + '\'' +
                ", currency_short_form='" + currency_short_form + '\'' +
                ", sender_email='" + sender_email + '\'' +
                ", added_date='" + added_date + '\'' +
                ", status='" + status + '\'' +
                ", paypal_enabled='" + paypal_enabled + '\'' +
                ", stripe_enabled='" + stripe_enabled + '\'' +
                ", cod_enabled='" + cod_enabled + '\'' +
                ", banktransfer_enabled='" + banktransfer_enabled + '\'' +
                ", is_featured='" + is_featured + '\'' +
                ", pinterest='" + pinterest + '\'' +
                ", youtube='" + youtube + '\'' +
                ", instagram='" + instagram + '\'' +
                ", twitter='" + twitter + '\'' +
                ", google_plus='" + google_plus + '\'' +
                ", facebook='" + facebook + '\'' +
                ", about_website='" + about_website + '\'' +
                ", about_phone1='" + about_phone1 + '\'' +
                ", about_phone2='" + about_phone2 + '\'' +
                ", about_phone3='" + about_phone3 + '\'' +
                ", overall_tax_label='" + overall_tax_label + '\'' +
                ", overall_tax_value=" + overall_tax_value +
                ", shipping_tax_label='" + shipping_tax_label + '\'' +
                ", shipping_tax_value=" + shipping_tax_value +
                ", whapsapp_no='" + whapsapp_no + '\'' +
                ", refund_policy='" + refund_policy + '\'' +
                ", privacy_policy='" + privacy_policy + '\'' +
                ", terms='" + terms + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", added_user_id='" + added_user_id + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", updated_user_id='" + updated_user_id + '\'' +
                ", featured_date='" + featured_date + '\'' +
                ", added_date_str='" + added_date_str + '\'' +
                ", default_photo=" + default_photo +
                ", default_icon=" + default_icon +
                ", touch_count='" + touch_count + '\'' +
                ", messenger='" + messenger + '\'' +
                ", standard_shipping_enable='" + standard_shipping_enable + '\'' +
                ", zone_shipping_enable='" + zone_shipping_enable + '\'' +
                ", no_shipping_enable='" + no_shipping_enable + '\'' +

                ", shop_current_version='" + shop_current_version + '\'' +
                ", shop_open_time='" + shop_open_time + '\'' +
                ", shop_close_time='" + shop_close_time + '\'' +
                ", shop_pin_number='" + shop_pin_number + '\'' +
                ", shop_app_force_update='" + shop_app_force_update + '\'' +
                ", shop_bussiness_type='" + shop_bussiness_type + '\'' +
                ", shop_main_branch_id='" + shop_main_branch_id + '\'' +
                ", shop_contract_form='" + shop_contract_form + '\'' +
                ", shop_branch='" + shop_branch + '\'' +
                ", is_enable='" + is_enable + '\'' +
                ", package_charges='" + package_charges + '\'' +
                ", plan_type='" + plan_type + '\'' +

                '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(String shipping_id) {
        this.shipping_id = shipping_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndividual_app() {
        return individual_app;
    }

    public void setIndividual_app(String individual_app) {
        this.individual_app = individual_app;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null) {
            this.city = "";
        } else {
            this.city = city;
        }
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        if (area == null) {
            this.area = "";
        } else {
            this.area = area;
        }
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        if (postal_code == null) {
            this.postal_code = "";
        } else {
            this.postal_code = postal_code;
        }
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPaypal_email() {
        return paypal_email;
    }

    public void setPaypal_email(String paypal_email) {
        this.paypal_email = paypal_email;
    }

    public String getPaypal_environment() {
        return paypal_environment;
    }

    public void setPaypal_environment(String paypal_environment) {
        this.paypal_environment = paypal_environment;
    }

    public String getPaypal_appid_live() {
        return paypal_appid_live;
    }

    public void setPaypal_appid_live(String paypal_appid_live) {
        this.paypal_appid_live = paypal_appid_live;
    }

    public String getPaypal_merchantname() {
        return paypal_merchantname;
    }

    public void setPaypal_merchantname(String paypal_merchantname) {
        this.paypal_merchantname = paypal_merchantname;
    }

    public String getPaypal_customerid() {
        return paypal_customerid;
    }

    public void setPaypal_customerid(String paypal_customerid) {
        this.paypal_customerid = paypal_customerid;
    }

    public String getPaypal_ipnurl() {
        return paypal_ipnurl;
    }

    public void setPaypal_ipnurl(String paypal_ipnurl) {
        this.paypal_ipnurl = paypal_ipnurl;
    }

    public String getPaypal_memo() {
        return paypal_memo;
    }

    public void setPaypal_memo(String paypal_memo) {
        this.paypal_memo = paypal_memo;
    }

    public String getPaypal_merchant_id() {
        return paypal_merchant_id;
    }

    public void setPaypal_merchant_id(String paypal_merchant_id) {
        this.paypal_merchant_id = paypal_merchant_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaypal_public_key() {
        return paypal_public_key;
    }

    public void setPaypal_public_key(String paypal_public_key) {
        this.paypal_public_key = paypal_public_key;
    }

    public String getPaypal_private_key() {
        return paypal_private_key;
    }

    public void setPaypal_private_key(String paypal_private_key) {
        this.paypal_private_key = paypal_private_key;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getSwift_code() {
        return swift_code;
    }

    public void setSwift_code(String swift_code) {
        this.swift_code = swift_code;
    }

    public String getCod_email() {
        return cod_email;
    }

    public void setCod_email(String cod_email) {
        this.cod_email = cod_email;
    }

    public String getStripe_publishable_key() {
        return stripe_publishable_key;
    }

    public void setStripe_publishable_key(String stripe_publishable_key) {
        this.stripe_publishable_key = stripe_publishable_key;
    }

    public String getStripe_secret_key() {
        return stripe_secret_key;
    }

    public void setStripe_secret_key(String stripe_secret_key) {
        this.stripe_secret_key = stripe_secret_key;
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

    public String getSender_email() {
        return sender_email;
    }

    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaypal_enabled() {
        return paypal_enabled;
    }

    public void setPaypal_enabled(String paypal_enabled) {
        this.paypal_enabled = paypal_enabled;
    }

    public String getStripe_enabled() {
        return stripe_enabled;
    }

    public void setStripe_enabled(String stripe_enabled) {
        this.stripe_enabled = stripe_enabled;
    }

    public String getCod_enabled() {
        return cod_enabled;
    }

    public void setCod_enabled(String cod_enabled) {
        this.cod_enabled = cod_enabled;
    }

    public String getBanktransfer_enabled() {
        return banktransfer_enabled;
    }

    public void setBanktransfer_enabled(String banktransfer_enabled) {
        this.banktransfer_enabled = banktransfer_enabled;
    }

    public String getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(String is_featured) {
        this.is_featured = is_featured;
    }

    public String getPinterest() {
        return pinterest;
    }

    public void setPinterest(String pinterest) {
        this.pinterest = pinterest;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGoogle_plus() {
        return google_plus;
    }

    public void setGoogle_plus(String google_plus) {
        this.google_plus = google_plus;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getAbout_website() {
        return about_website;
    }

    public void setAbout_website(String about_website) {
        this.about_website = about_website;
    }

    public String getAbout_phone1() {
        return about_phone1;
    }

    public void setAbout_phone1(String about_phone1) {
        this.about_phone1 = about_phone1;
    }

    public String getAbout_phone2() {
        return about_phone2;
    }

    public void setAbout_phone2(String about_phone2) {
        this.about_phone2 = about_phone2;
    }

    public String getAbout_phone3() {
        return about_phone3;
    }

    public void setAbout_phone3(String about_phone3) {
        this.about_phone3 = about_phone3;
    }

    public String getOverall_tax_label() {
        return overall_tax_label;
    }

    public void setOverall_tax_label(String overall_tax_label) {
        this.overall_tax_label = overall_tax_label;
    }

    public float getOverall_tax_value() {
        return overall_tax_value;
    }

    public void setOverall_tax_value(float overall_tax_value) {
        this.overall_tax_value = overall_tax_value;
    }

    public String getShipping_tax_label() {
        return shipping_tax_label;
    }

    public void setShipping_tax_label(String shipping_tax_label) {
        this.shipping_tax_label = shipping_tax_label;
    }

    public float getShipping_tax_value() {
        return shipping_tax_value;
    }

    public void setShipping_tax_value(float shipping_tax_value) {
        this.shipping_tax_value = shipping_tax_value;
    }

    public String getWhapsapp_no() {
        return whapsapp_no;
    }

    public void setWhapsapp_no(String whapsapp_no) {
        this.whapsapp_no = whapsapp_no;
    }

    public String getRefund_policy() {
        return refund_policy;
    }

    public void setRefund_policy(String refund_policy) {
        this.refund_policy = refund_policy;
    }

    public String getPrivacy_policy() {
        return privacy_policy;
    }

    public void setPrivacy_policy(String privacy_policy) {
        this.privacy_policy = privacy_policy;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
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

    public String getFeatured_date() {
        return featured_date;
    }

    public void setFeatured_date(String featured_date) {
        this.featured_date = featured_date;
    }

    public String getAdded_date_str() {
        return added_date_str;
    }

    public void setAdded_date_str(String added_date_str) {
        this.added_date_str = added_date_str;
    }

    public Photo getDefault_photo() {
        return default_photo;
    }

    public void setDefault_photo(Photo default_photo) {
        this.default_photo = default_photo;
    }

    public Photo getDefault_icon() {
        return default_icon;
    }

    public void setDefault_icon(Photo default_icon) {
        this.default_icon = default_icon;
    }

    public String getTouch_count() {
        return touch_count;
    }

    public void setTouch_count(String touch_count) {
        this.touch_count = touch_count;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public String getStandard_shipping_enable() {
        return standard_shipping_enable;
    }

    public void setStandard_shipping_enable(String standard_shipping_enable) {
        this.standard_shipping_enable = standard_shipping_enable;
    }

    public String getZone_shipping_enable() {
        return zone_shipping_enable;
    }

    public void setZone_shipping_enable(String zone_shipping_enable) {
        this.zone_shipping_enable = zone_shipping_enable;
    }

    public String getNo_shipping_enable() {
        return no_shipping_enable;
    }

    public void setNo_shipping_enable(String no_shipping_enable) {
        this.no_shipping_enable = no_shipping_enable;
    }

    public String getCod_charge_from() {
        return cod_charge_from;
    }

    public void setCod_charge_from(String cod_charge_from) {
        this.cod_charge_from = cod_charge_from;
    }

    public String getCod_needed_type() {
        return cod_needed_type;
    }

    public void setCod_needed_type(String cod_needed_type) {
        this.cod_needed_type = cod_needed_type;
    }

    public String getShop_current_version() {
        return shop_current_version;
    }

    public void setShop_current_version(String shop_current_version) {
        this.shop_current_version = shop_current_version;
    }

    public String getShop_open_time() {
        return shop_open_time;
    }

    public void setShop_open_time(String shop_open_time) {
        this.shop_open_time = shop_open_time;
    }

    public String getShop_close_time() {
        return shop_close_time;
    }

    public void setShop_close_time(String shop_close_time) {
        this.shop_close_time = shop_close_time;
    }

    public String getShop_pin_number() {
        return shop_pin_number;
    }

    public void setShop_pin_number(String shop_pin_number) {
        this.shop_pin_number = shop_pin_number;
    }

    public String getShop_app_force_update() {
        return shop_app_force_update;
    }

    public void setShop_app_force_update(String shop_app_force_update) {
        this.shop_app_force_update = shop_app_force_update;
    }

    public String getShop_bussiness_type() {
        return shop_bussiness_type;
    }

    public void setShop_bussiness_type(String shop_bussiness_type) {
        this.shop_bussiness_type = shop_bussiness_type;
    }

    public String getShop_main_branch_id() {
        return shop_main_branch_id;
    }

    public void setShop_main_branch_id(String shop_main_branch_id) {
        this.shop_main_branch_id = shop_main_branch_id;
    }

    public String getShop_contract_form() {
        return shop_contract_form;
    }

    public void setShop_contract_form(String shop_contract_form) {
        this.shop_contract_form = shop_contract_form;
    }

    public String getShop_branch() {
        return shop_branch;
    }

    public void setShop_branch(String shop_branch) {
        this.shop_branch = shop_branch;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getPackage_charges() {
        return package_charges;
    }

    public void setPackage_charges(String package_charges) {
        this.package_charges = package_charges;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
