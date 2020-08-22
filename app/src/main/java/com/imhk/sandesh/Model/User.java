package com.imhk.sandesh.Model;

public class User {
    private String UID;
    private String name;
    private String mobile;
    private String imageURL;
    private String status;

    public User(String UID, String name, String mobile, String imageURL,String status) {
        this.UID = UID;
        this.name = name;
        this.mobile = mobile;
        this.imageURL = imageURL;
        this.status = status;
    }

    public User() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
