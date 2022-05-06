package com.dvishapps.yourshop.models;

import com.google.gson.annotations.Expose;

public class RatingDetail {
    @Expose
    private int five_star_count;

    @Expose
    private float five_star_percent;

    @Expose
    private int four_star_count;

    @Expose
    private float four_star_percent;

    @Expose
    private int three_star_count;

    @Expose
    private float three_star_percent;

    @Expose
    private int two_star_count;

    @Expose
    private float two_star_percent;

    @Expose
    private int one_star_count;

    @Expose
    private float one_star_percent;

    @Expose
    private int total_rating_count;

    @Expose
    private float total_rating_value;

    public RatingDetail(int five_star_count, float five_star_percent, int four_star_count, float four_star_percent, int three_star_count, float three_star_percent, int two_star_count, float two_star_percent, int one_star_count, float one_star_percent, int total_rating_count, float total_rating_value) {
        this.five_star_count = five_star_count;
        this.five_star_percent = five_star_percent;
        this.four_star_count = four_star_count;
        this.four_star_percent = four_star_percent;
        this.three_star_count = three_star_count;
        this.three_star_percent = three_star_percent;
        this.two_star_count = two_star_count;
        this.two_star_percent = two_star_percent;
        this.one_star_count = one_star_count;
        this.one_star_percent = one_star_percent;
        this.total_rating_count = total_rating_count;
        this.total_rating_value = total_rating_value;
    }

    public int getFive_star_count() {
        return five_star_count;
    }

    public void setFive_star_count(int five_star_count) {
        this.five_star_count = five_star_count;
    }

    public float getFive_star_percent() {
        return five_star_percent;
    }

    public void setFive_star_percent(float five_star_percent) {
        this.five_star_percent = five_star_percent;
    }

    public int getFour_star_count() {
        return four_star_count;
    }

    public void setFour_star_count(int four_star_count) {
        this.four_star_count = four_star_count;
    }

    public float getFour_star_percent() {
        return four_star_percent;
    }

    public void setFour_star_percent(float four_star_percent) {
        this.four_star_percent = four_star_percent;
    }

    public int getThree_star_count() {
        return three_star_count;
    }

    public void setThree_star_count(int three_star_count) {
        this.three_star_count = three_star_count;
    }

    public float getThree_star_percent() {
        return three_star_percent;
    }

    public void setThree_star_percent(float three_star_percent) {
        this.three_star_percent = three_star_percent;
    }

    public int getTwo_star_count() {
        return two_star_count;
    }

    public void setTwo_star_count(int two_star_count) {
        this.two_star_count = two_star_count;
    }

    public float getTwo_star_percent() {
        return two_star_percent;
    }

    public void setTwo_star_percent(float two_star_percent) {
        this.two_star_percent = two_star_percent;
    }

    public int getOne_star_count() {
        return one_star_count;
    }

    public void setOne_star_count(int one_star_count) {
        this.one_star_count = one_star_count;
    }

    public float getOne_star_percent() {
        return one_star_percent;
    }

    public void setOne_star_percent(float one_star_percent) {
        this.one_star_percent = one_star_percent;
    }

    public int getTotal_rating_count() {
        return total_rating_count;
    }

    public void setTotal_rating_count(int total_rating_count) {
        this.total_rating_count = total_rating_count;
    }

    public float getTotal_rating_value() {
        return total_rating_value;
    }

    public void setTotal_rating_value(float total_rating_value) {
        this.total_rating_value = total_rating_value;
    }
}
