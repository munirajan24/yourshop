package com.dvishapps.yourshop.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * The type User.
 */
public class User implements Serializable {
    @NonNull
    @Expose
    private String user_id;

    @Expose
    private String user_is_sys_admin;

    @Expose
    private String is_shop_admin;

    @Expose
    private String shop_id;

    @Expose
    private String facebook_id;

    @Expose
    private String google_id;

    @Expose
    private String user_name;

    @Expose
    private String user_email;

    @Expose
    private String user_phone;

    @Expose
    private String user_password;

    @Expose
    private String user_about_me;

    @Expose
    private String user_cover_photo;

    @Expose
    private String user_profile_photo;

    @Expose
    private String role_id;

    @Expose
    private String status="";

    @Expose
    private String is_banned;

    @Expose
    private String added_date;

    //    TODO: used for lattitude
    @Expose
    private String city_id;

    //    TODO: used for longitude
    @Expose
    private String country_id;

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
    private String shipping_email;

    @Expose
    private String shipping_phone;

    @Expose
    private String device_token;

    @Expose
    private String code;

    @Expose
    private String verify_types;

    @Expose
    private String added_date_str;

    @Expose
    private Country country;

    @Expose
    private Country city;

    private Boolean isLogin;
    /**
     * Instantiates a new User.
     *
     * @param user_id the user id
     */
    public User(@NonNull String user_id) {
        this.user_id = user_id;
    }

    public User() {
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    @NonNull
    public String getUser_id() {
        return user_id;
    }

    /**
     * Sets user id.
     *
     * @param user_id the user id
     */
    public void setUser_id(@NonNull String user_id) {
        this.user_id = user_id;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public Boolean getLogin() {
        return isLogin;
    }

    /**
     * Sets login.
     *
     * @param login the login
     */
    public void setLogin(Boolean login) {
        isLogin = login;
    }

    /**
     * Gets user is sys admin.
     *
     * @return the user is sys admin
     */
    public String getUser_is_sys_admin() {
        return user_is_sys_admin;
    }

    /**
     * Sets user is sys admin.
     *
     * @param user_is_sys_admin the user is sys admin
     */
    public void setUser_is_sys_admin(String user_is_sys_admin) {
        this.user_is_sys_admin = user_is_sys_admin;
    }

    /**
     * Gets is shop admin.
     *
     * @return the is shop admin
     */
    public String getIs_shop_admin() {
        return is_shop_admin;
    }

    /**
     * Sets is shop admin.
     *
     * @param is_shop_admin the is shop admin
     */
    public void setIs_shop_admin(String is_shop_admin) {
        this.is_shop_admin = is_shop_admin;
    }


    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    /**
     * Gets facebook id.
     *
     * @return the facebook id
     */
    public String getFacebook_id() {
        return facebook_id;
    }

    /**
     * Sets facebook id.
     *
     * @param facebook_id the facebook id
     */
    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    /**
     * Gets google id.
     *
     * @return the google id
     */
    public String getGoogle_id() {
        return google_id;
    }

    /**
     * Sets google id.
     *
     * @param google_id the google id
     */
    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * Sets user name.
     *
     * @param user_name the user name
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * Gets user email.
     *
     * @return the user email
     */
    public String getUser_email() {
        return user_email;
    }

    /**
     * Sets user email.
     *
     * @param user_email the user email
     */
    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    /**
     * Gets user phone.
     *
     * @return the user phone
     */
    public String getUser_phone() {
        return user_phone;
    }

    /**
     * Sets user phone.
     *
     * @param user_phone the user phone
     */
    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    /**
     * Gets user password.
     *
     * @return the user password
     */
    public String getUser_password() {
        return user_password;
    }

    /**
     * Sets user password.
     *
     * @param user_password the user password
     */
    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    /**
     * Gets user about me.
     *
     * @return the user about me
     */
    public String getUser_about_me() {
        return user_about_me;
    }

    /**
     * Sets user about me.
     *
     * @param user_about_me the user about me
     */
    public void setUser_about_me(String user_about_me) {
        this.user_about_me = user_about_me;
    }

    /**
     * Gets user cover photo.
     *
     * @return the user cover photo
     */
    public String getUser_cover_photo() {
        return user_cover_photo;
    }

    /**
     * Sets user cover photo.
     *
     * @param user_cover_photo the user cover photo
     */
    public void setUser_cover_photo(String user_cover_photo) {
        this.user_cover_photo = user_cover_photo;
    }

    /**
     * Gets user profile photo.
     *
     * @return the user profile photo
     */
    public String getUser_profile_photo() {
        return user_profile_photo;
    }

    /**
     * Sets user profile photo.
     *
     * @param user_profile_photo the user profile photo
     */
    public void setUser_profile_photo(String user_profile_photo) {
        this.user_profile_photo = user_profile_photo;
    }

    /**
     * Gets role id.
     *
     * @return the role id
     */
    public String getRole_id() {
        return role_id;
    }

    /**
     * Sets role id.
     *
     * @param role_id the role id
     */
    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets is banned.
     *
     * @return the is banned
     */
    public String getIs_banned() {
        return is_banned;
    }

    /**
     * Sets is banned.
     *
     * @param is_banned the is banned
     */
    public void setIs_banned(String is_banned) {
        this.is_banned = is_banned;
    }

    /**
     * Gets added date.
     *
     * @return the added date
     */
    public String getAdded_date() {
        return added_date;
    }

    /**
     * Sets added date.
     *
     * @param added_date the added date
     */
    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    /**
     * Gets billing first name.
     *
     * @return the billing first name
     */
    public String getBilling_first_name() {
        return billing_first_name;
    }

    /**
     * Sets billing first name.
     *
     * @param billing_first_name the billing first name
     */
    public void setBilling_first_name(String billing_first_name) {
        this.billing_first_name = billing_first_name;
    }

    /**
     * Gets billing last name.
     *
     * @return the billing last name
     */
    public String getBilling_last_name() {
        return billing_last_name;
    }

    /**
     * Sets billing last name.
     *
     * @param billing_last_name the billing last name
     */
    public void setBilling_last_name(String billing_last_name) {
        this.billing_last_name = billing_last_name;
    }

    /**
     * Gets billing company.
     *
     * @return the billing company
     */
    public String getBilling_company() {
        return billing_company;
    }

    /**
     * Sets billing company.
     *
     * @param billing_company the billing company
     */
    public void setBilling_company(String billing_company) {
        this.billing_company = billing_company;
    }

    /**
     * Gets billing address 1.
     *
     * @return the billing address 1
     */
    public String getBilling_address_1() {
        return billing_address_1;
    }

    /**
     * Sets billing address 1.
     *
     * @param billing_address_1 the billing address 1
     */
    public void setBilling_address_1(String billing_address_1) {
        this.billing_address_1 = billing_address_1;
    }

    /**
     * Gets billing address 2.
     *
     * @return the billing address 2
     */
    public String getBilling_address_2() {
        return billing_address_2;
    }

    /**
     * Sets billing address 2.
     *
     * @param billing_address_2 the billing address 2
     */
    public void setBilling_address_2(String billing_address_2) {
        this.billing_address_2 = billing_address_2;
    }

    /**
     * Gets billing country.
     *
     * @return the billing country
     */
    public String getBilling_country() {
        return billing_country;
    }

    /**
     * Sets billing country.
     *
     * @param billing_country the billing country
     */
    public void setBilling_country(String billing_country) {
        this.billing_country = billing_country;
    }

    /**
     * Gets billing state.
     *
     * @return the billing state
     */
    public String getBilling_state() {
        return billing_state;
    }

    /**
     * Sets billing state.
     *
     * @param billing_state the billing state
     */
    public void setBilling_state(String billing_state) {
        this.billing_state = billing_state;
    }

    /**
     * Gets billing city.
     *
     * @return the billing city
     */
    public String getBilling_city() {
        return billing_city;
    }

    /**
     * Sets billing city.
     *
     * @param billing_city the billing city
     */
    public void setBilling_city(String billing_city) {
        this.billing_city = billing_city;
    }

    /**
     * Gets billing postal code.
     *
     * @return the billing postal code
     */
    public String getBilling_postal_code() {
        return billing_postal_code;
    }

    /**
     * Sets billing postal code.
     *
     * @param billing_postal_code the billing postal code
     */
    public void setBilling_postal_code(String billing_postal_code) {
        this.billing_postal_code = billing_postal_code;
    }

    /**
     * Gets billing email.
     *
     * @return the billing email
     */
    public String getBilling_email() {
        return billing_email;
    }

    /**
     * Sets billing email.
     *
     * @param billing_email the billing email
     */
    public void setBilling_email(String billing_email) {
        this.billing_email = billing_email;
    }

    /**
     * Gets billing phone.
     *
     * @return the billing phone
     */
    public String getBilling_phone() {
        return billing_phone;
    }

    /**
     * Sets billing phone.
     *
     * @param billing_phone the billing phone
     */
    public void setBilling_phone(String billing_phone) {
        this.billing_phone = billing_phone;
    }

    /**
     * Gets shipping first name.
     *
     * @return the shipping first name
     */
    public String getShipping_first_name() {
        return shipping_first_name;
    }

    /**
     * Sets shipping first name.
     *
     * @param shipping_first_name the shipping first name
     */
    public void setShipping_first_name(String shipping_first_name) {
        this.shipping_first_name = shipping_first_name;
    }

    /**
     * Gets shipping last name.
     *
     * @return the shipping last name
     */
    public String getShipping_last_name() {
        return shipping_last_name;
    }

    /**
     * Sets shipping last name.
     *
     * @param shipping_last_name the shipping last name
     */
    public void setShipping_last_name(String shipping_last_name) {
        this.shipping_last_name = shipping_last_name;
    }

    /**
     * Gets shipping company.
     *
     * @return the shipping company
     */
    public String getShipping_company() {
        return shipping_company;
    }

    /**
     * Sets shipping company.
     *
     * @param shipping_company the shipping company
     */
    public void setShipping_company(String shipping_company) {
        this.shipping_company = shipping_company;
    }

    /**
     * Gets shipping address 1.
     *
     * @return the shipping address 1
     */
    public String getShipping_address_1() {
        return shipping_address_1;
    }

    /**
     * Sets shipping address 1.
     *
     * @param shipping_address_1 the shipping address 1
     */
    public void setShipping_address_1(String shipping_address_1) {
        this.shipping_address_1 = shipping_address_1;
    }

    /**
     * Gets shipping address 2.
     *
     * @return the shipping address 2
     */
    public String getShipping_address_2() {
        return shipping_address_2;
    }

    /**
     * Sets shipping address 2.
     *
     * @param shipping_address_2 the shipping address 2
     */
    public void setShipping_address_2(String shipping_address_2) {
        this.shipping_address_2 = shipping_address_2;
    }

    /**
     * Gets shipping country.
     *
     * @return the shipping country
     */
    public String getShipping_country() {
        return shipping_country;
    }

    /**
     * Sets shipping country.
     *
     * @param shipping_country the shipping country
     */
    public void setShipping_country(String shipping_country) {
        this.shipping_country = shipping_country;
    }

    /**
     * Gets shipping state.
     *
     * @return the shipping state
     */
    public String getShipping_state() {
        return shipping_state;
    }

    /**
     * Sets shipping state.
     *
     * @param shipping_state the shipping state
     */
    public void setShipping_state(String shipping_state) {
        this.shipping_state = shipping_state;
    }

    /**
     * Gets shipping city.
     *
     * @return the shipping city
     */
    public String getShipping_city() {
        return shipping_city;
    }

    /**
     * Sets shipping city.
     *
     * @param shipping_city the shipping city
     */
    public void setShipping_city(String shipping_city) {
        this.shipping_city = shipping_city;
    }

    /**
     * Gets shipping postal code.
     *
     * @return the shipping postal code
     */
    public String getShipping_postal_code() {
        return shipping_postal_code;
    }

    /**
     * Sets shipping postal code.
     *
     * @param shipping_postal_code the shipping postal code
     */
    public void setShipping_postal_code(String shipping_postal_code) {
        this.shipping_postal_code = shipping_postal_code;
    }

    /**
     * Gets shipping email.
     *
     * @return the shipping email
     */
    public String getShipping_email() {
        return shipping_email;
    }

    /**
     * Sets shipping email.
     *
     * @param shipping_email the shipping email
     */
    public void setShipping_email(String shipping_email) {
        this.shipping_email = shipping_email;
    }

    /**
     * Gets shipping phone.
     *
     * @return the shipping phone
     */
    public String getShipping_phone() {
        return shipping_phone;
    }

    /**
     * Sets shipping phone.
     *
     * @param shipping_phone the shipping phone
     */
    public void setShipping_phone(String shipping_phone) {
        this.shipping_phone = shipping_phone;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    /**
     * Gets device token.
     *
     * @return the device token
     */
    public String getDevice_token() {
        return device_token;
    }

    /**
     * Sets device token.
     *
     * @param device_token the device token
     */
    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets verify types.
     *
     * @return the verify types
     */
    public String getVerify_types() {
        return verify_types;
    }

    /**
     * Sets verify types.
     *
     * @param verify_types the verify types
     */
    public void setVerify_types(String verify_types) {
        this.verify_types = verify_types;
    }

    /**
     * Gets added date str.
     *
     * @return the added date str
     */
    public String getAdded_date_str() {
        return added_date_str;
    }

    /**
     * Sets added date str.
     *
     * @param added_date_str the added date str
     */
    public void setAdded_date_str(String added_date_str) {
        this.added_date_str = added_date_str;
    }

    /**
     * Gets country.
     *
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets country.
     *
     * @param country the country
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Gets city.
     *
     * @return the city
     */
    public Country getCity() {
        return city;
    }

    /**
     * Sets city.
     *
     * @param city the city
     */
    public void setCity(Country city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", user_is_sys_admin='" + user_is_sys_admin + '\'' +
                ", is_shop_admin='" + is_shop_admin + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", facebook_id='" + facebook_id + '\'' +
                ", google_id='" + google_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_about_me='" + user_about_me + '\'' +
                ", user_cover_photo='" + user_cover_photo + '\'' +
                ", user_profile_photo='" + user_profile_photo + '\'' +
                ", role_id='" + role_id + '\'' +
                ", status='" + status + '\'' +
                ", is_banned='" + is_banned + '\'' +
                ", added_date='" + added_date + '\'' +
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
                ", device_token='" + device_token + '\'' +
                ", code='" + code + '\'' +
                ", verify_types='" + verify_types + '\'' +
                ", added_date_str='" + added_date_str + '\'' +
                ", city_id='" + city_id + '\'' +
                ", country_id='" + country_id + '\'' +
                ", country=" + country +
                ", city=" + city +
                ", isLogin=" + isLogin +
                '}';
    }


}
