package com.dvishapps.yourshop.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    @Expose
    @NonNull
    private String id;

    @Expose
    private String cat_id;

    @Expose
    private String sub_cat_id;

    @Expose
    private String is_discount;

    @Expose
    private String is_featured;

    @Expose
    private String is_available;

    @Expose
    private String code;

    @Expose
    private String name;

    @Expose
    private String description;

    @Expose
    private String search_tag;

    @Expose
    private String highlight_information;

    @Expose
    private String status;

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
    private String deleted_flag;

    @Expose
    private String added_date_str;

    @Expose
    private String shipping_cost;

    @Expose
    private String minimum_order;

    @Expose
    private String product_unit;

    @Expose
    private String product_measurement;

    @Expose
    private Photo default_photo;

    @Expose
    private Photo default_icon;

    @Expose
    private Category category;

    @Expose
    private SubCategory sub_category;

    @Expose
    private RatingDetail rating_details;

    @Expose
    private int like_count;

    @Expose
    private int image_count;

    @Expose
    private int favourite_count;

    @Expose
    private int touch_count;

    @Expose
    private String featured_date;

    @Expose
    private int comment_header_count;

    @Expose
    private float original_price;

    @Expose
    private float unit_price;

    @Expose
    private float discount_amount;

    @Expose
    private String currency_short_form;

    @Expose
    private String currency_symbol;

    @Expose
    private float discount_percent;

    @Expose
    private float discount_value;

    @Expose
    private String is_liked;

    @Expose
    private String is_favourited;

    @Expose
    private String overall_rating;

    @Expose
    private String trans_status;

    @Expose
    private List<ProductAttributeHeader> attributes_header;

    @Expose
    private List<ProductColor> colors;

    @Expose
    private List<ProductSpecs> specs;




    @Expose
    private String product_bussiness_type;


    @Expose
    private String product_open_time;


    @Expose
    private String product_close_time;


    @Expose
    private String is_enable;


    @Expose
    private float offer_price=0;


    private int qty;

    public Product() {
    }

    public Product(String id, String name, Photo default_photo) {
        this.id = id;
        this.default_photo = default_photo;
        this.name = name;
    }

    public Product(@NonNull String id, Photo default_photo, Photo default_icon,
                   String name, String cat_id, String added_date, String added_user_id, String updated_date,
                   String updated_user_id, String updated_flag, String added_date_str,
                   String product_bussiness_type, String product_open_time, String product_close_time, String is_enable, float offer_price
    ) {
        this.id = id;
        this.default_photo = default_photo;
        this.default_icon = default_icon;
        this.name = name;
        this.cat_id = cat_id;
        this.added_date = added_date;
        this.added_user_id = added_user_id;
        this.updated_date = updated_date;
        this.updated_user_id = updated_user_id;
        this.updated_flag = updated_flag;
        this.added_date_str = added_date_str;
        this.product_bussiness_type = product_bussiness_type;
        this.product_open_time = product_open_time;
        this.product_close_time = product_close_time;
        this.is_enable = is_enable;
        this.offer_price = offer_price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public String getIs_discount() {
        return is_discount;
    }

    public void setIs_discount(String is_discount) {
        this.is_discount = is_discount;
    }

    public String getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(String is_featured) {
        this.is_featured = is_featured;
    }

    public String getIs_available() {
        return is_available;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSearch_tag() {
        return search_tag;
    }

    public void setSearch_tag(String search_tag) {
        this.search_tag = search_tag;
    }

    public String getHighlight_information() {
        return highlight_information;
    }

    public void setHighlight_information(String highlight_information) {
        this.highlight_information = highlight_information;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDeleted_flag() {
        return deleted_flag;
    }

    public void setDeleted_flag(String deleted_flag) {
        this.deleted_flag = deleted_flag;
    }

    public String getAdded_date_str() {
        return added_date_str;
    }

    public void setAdded_date_str(String added_date_str) {
        this.added_date_str = added_date_str;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public String getMinimum_order() {
        return minimum_order;
    }

    public void setMinimum_order(String minimum_order) {
        this.minimum_order = minimum_order;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSub_category() {
        return sub_category;
    }

    public void setSub_category(SubCategory sub_category) {
        this.sub_category = sub_category;
    }

    public RatingDetail getRating_details() {
        return rating_details;
    }

    public void setRating_details(RatingDetail rating_details) {
        this.rating_details = rating_details;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getImage_count() {
        return image_count;
    }

    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }

    public int getFavourite_count() {
        return favourite_count;
    }

    public void setFavourite_count(int favourite_count) {
        this.favourite_count = favourite_count;
    }

    public int getTouch_count() {
        return touch_count;
    }

    public void setTouch_count(int touch_count) {
        this.touch_count = touch_count;
    }

    public String getFeatured_date() {
        return featured_date;
    }

    public void setFeatured_date(String featured_date) {
        this.featured_date = featured_date;
    }

    public int getComment_header_count() {
        return comment_header_count;
    }

    public void setComment_header_count(int comment_header_count) {
        this.comment_header_count = comment_header_count;
    }

    public float getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(float original_price) {
        this.original_price = original_price;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(float unit_price) {
        this.unit_price = unit_price;
    }

    public float getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(float discount_amount) {
        this.discount_amount = discount_amount;
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

    public float getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(float discount_percent) {
        this.discount_percent = discount_percent;
    }

    public float getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(float discount_value) {
        this.discount_value = discount_value;
    }

    public String getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }

    public String getIs_favourited() {
        return is_favourited;
    }

    public void setIs_favourited(String is_favourited) {
        this.is_favourited = is_favourited;
    }

    public String getOverall_rating() {
        return overall_rating;
    }

    public void setOverall_rating(String overall_rating) {
        this.overall_rating = overall_rating;
    }

    public String getTrans_status() {
        return trans_status;
    }

    public void setTrans_status(String trans_status) {
        this.trans_status = trans_status;
    }

    public List<ProductAttributeHeader> getAttributes_header() {
        return attributes_header;
    }

    public void setAttributes_header(List<ProductAttributeHeader> attributes_header) {
        this.attributes_header = attributes_header;
    }

    public List<ProductColor> getColors() {
        return colors;
    }

    public void setColors(List<ProductColor> colors) {
        this.colors = colors;
    }

    public List<ProductSpecs> getSpecs() {
        return specs;
    }

    public void setSpecs(List<ProductSpecs> specs) {
        this.specs = specs;
    }

    public String getProduct_bussiness_type() {
        return product_bussiness_type;
    }

    public void setProduct_bussiness_type(String product_bussiness_type) {
        this.product_bussiness_type = product_bussiness_type;
    }

    public String getProduct_open_time() {
        return product_open_time;
    }

    public void setProduct_open_time(String product_open_time) {
        this.product_open_time = product_open_time;
    }

    public String getProduct_close_time() {
        return product_close_time;
    }

    public void setProduct_close_time(String product_close_time) {
        this.product_close_time = product_close_time;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public float getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(float offer_price) {
        this.offer_price = offer_price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", cat_id='" + cat_id + '\'' +
                ", sub_cat_id='" + sub_cat_id + '\'' +
                ", is_discount='" + is_discount + '\'' +
                ", is_featured='" + is_featured + '\'' +
                ", is_available='" + is_available + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", search_tag='" + search_tag + '\'' +
                ", highlight_information='" + highlight_information + '\'' +
                ", status='" + status + '\'' +
                ", added_date='" + added_date + '\'' +
                ", added_user_id='" + added_user_id + '\'' +
                ", updated_date='" + updated_date + '\'' +
                ", updated_user_id='" + updated_user_id + '\'' +
                ", updated_flag='" + updated_flag + '\'' +
                ", deleted_flag='" + deleted_flag + '\'' +
                ", added_date_str='" + added_date_str + '\'' +
                ", shipping_cost='" + shipping_cost + '\'' +
                ", minimum_order='" + minimum_order + '\'' +
                ", product_unit='" + product_unit + '\'' +
                ", product_measurement='" + product_measurement + '\'' +
                ", default_photo=" + default_photo +
                ", default_icon=" + default_icon +
                ", category=" + category +
                ", sub_category=" + sub_category +
                ", rating_details=" + rating_details +
                ", like_count=" + like_count +
                ", image_count=" + image_count +
                ", favourite_count=" + favourite_count +
                ", touch_count=" + touch_count +
                ", featured_date='" + featured_date + '\'' +
                ", comment_header_count=" + comment_header_count +
                ", original_price=" + original_price +
                ", unit_price=" + unit_price +
                ", discount_amount=" + discount_amount +
                ", currency_short_form='" + currency_short_form + '\'' +
                ", currency_symbol='" + currency_symbol + '\'' +
                ", discount_percent=" + discount_percent +
                ", discount_value=" + discount_value +
                ", is_liked='" + is_liked + '\'' +
                ", is_favourited='" + is_favourited + '\'' +
                ", overall_rating='" + overall_rating + '\'' +
                ", trans_status='" + trans_status + '\'' +
                ", attributes_header=" + attributes_header +
                ", colors=" + colors +
                ", specs=" + specs +
                ", product_bussiness_type=" + product_bussiness_type +
                ", product_open_time=" + product_open_time +
                ", product_close_time=" + product_close_time +
                ", is_enable=" + is_enable +
                ", offer_price=" + offer_price +
                '}';
    }
}

