package com.dvishapps.yourshop.models;

import java.io.Serializable;

public class Search implements Serializable {
    private String name;
    private String cat_id;
    private String sub_cat_id;
    private String min_price;
    private String max_price;
    private int star;
    private boolean featured;
    private boolean discount;
    private String searchTerm;

    public Search(String name, String min_price, String max_price, int star, boolean featured, boolean discount) {
        this.name = name;
        this.min_price = min_price;
        this.max_price = max_price;
        this.star = star;
        this.featured = featured;
        this.discount = discount;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Search{" +
                "name='" + name + '\'' +
                ", min_price='" + min_price + '\'' +
                ", max_price='" + max_price + '\'' +
                ", star=" + star +
                ", featured=" + featured +
                ", discount=" + discount +
                '}';
    }
}
