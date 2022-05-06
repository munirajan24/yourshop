package com.dvishapps.yourshop.models;

import com.google.gson.annotations.Expose;
import com.dvishapps.yourshop.utils.Constants;

import java.io.Serializable;

/**
 * The type Photo.
 */
public class Photo implements Serializable {

    @Expose
    private String img_id;
    @Expose
    private String img_parent_id;
    @Expose
    private String img_type;
    @Expose
    private String img_path;
    @Expose
    private String Img_width;
    @Expose
    private String img_height;
    @Expose
    private String img_desc;

    @Expose
    private String url = Constants.IMG_URL;

    /**
     * Instantiates a new Photo.
     */
    public Photo() {

    }

    /**
     * Instantiates a new Photo.
     *
     * @param img_id   the img id
     * @param url      the url
     * @param img_path the img path
     * @param img_desc the img desc
     */
    public Photo(String img_id, String url, String img_path, String img_desc) {
        this.img_id = img_id;
        this.img_desc = img_desc;
        this.url = url;
        this.img_path = img_path;
    }

    /**
     * Instantiates a new Photo.
     *
     * @param img_id        the img id
     * @param img_parent_id the img parent id
     * @param img_type      the img type
     * @param img_path      the img path
     * @param img_width     the img width
     * @param img_height    the img height
     * @param img_desc      the img desc
     * @param url           the url
     */
    public Photo(String img_id, String img_parent_id, String img_type, String img_path, String img_width, String img_height, String img_desc, String url) {
        this.img_id = img_id;
        this.img_parent_id = img_parent_id;
        this.img_type = img_type;
        this.img_path = img_path;
        this.Img_width = img_width;
        this.img_height = img_height;
        this.img_desc = img_desc;
        this.url = url;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getImg_parent_id() {
        return img_parent_id;
    }

    public void setImg_parent_id(String img_parent_id) {
        this.img_parent_id = img_parent_id;
    }

    public String getImg_type() {
        return img_type;
    }

    public void setImg_type(String img_type) {
        this.img_type = img_type;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getImg_width() {
        return Img_width;
    }

    public void setImg_width(String img_width) {
        Img_width = img_width;
    }

    public String getImg_height() {
        return img_height;
    }

    public void setImg_height(String img_height) {
        this.img_height = img_height;
    }

    public String getImg_desc() {
        return img_desc;
    }

    public void setImg_desc(String img_desc) {
        this.img_desc = img_desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "img_id='" + img_id + '\'' +
                ", img_parent_id='" + img_parent_id + '\'' +
                ", img_type='" + img_type + '\'' +
                ", img_path='" + img_path + '\'' +
                ", Img_width='" + Img_width +'\'' +
                ", img_height='" + img_height +'\'' +
                ", img_desc='" + img_desc + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
