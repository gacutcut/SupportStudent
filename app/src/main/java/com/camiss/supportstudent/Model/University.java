package com.camiss.supportstudent.Model;

/**
 * Created by hatuananh on 12/30/15.
 */
public class University {
    private int idUniversity;
    private String uniName;
    private String uniAddress;
    private String uniPhoneNumber;
    private String uniLogoURL;
    private String uniWebsite;

    public University() {

    }

    public University(int idUniversity, String uniName, String uniAddress, String uniPhoneNumber, String uniLogoURL, String uniWebsite) {
        this.idUniversity = idUniversity;
        this.uniName = uniName;
        this.uniAddress = uniAddress;
        this.uniPhoneNumber = uniPhoneNumber;
        this.uniLogoURL = uniLogoURL;
        this.uniWebsite = uniWebsite;
    }

    public int getIdUniversity() {
        return idUniversity;
    }

    public void setIdUniversity(int idUniversity) {
        this.idUniversity = idUniversity;
    }

    public String getUniName() {
        return uniName;
    }

    public void setUniName(String uniName) {
        this.uniName = uniName;
    }

    public String getUniAddress() {
        return uniAddress;
    }

    public void setUniAddress(String uniAddress) {
        this.uniAddress = uniAddress;
    }

    public String getUniPhoneNumber() {
        return uniPhoneNumber;
    }

    public void setUniPhoneNumber(String uniPhoneNumber) {
        this.uniPhoneNumber = uniPhoneNumber;
    }

    public String getUniLogoURL() {
        return uniLogoURL;
    }

    public void setUniLogoURL(String uniLogoURL) {
        this.uniLogoURL = uniLogoURL;
    }

    public String getUniWebsite() {
        return uniWebsite;
    }

    public void setUniWebsite(String uniWebsite) {
        this.uniWebsite = uniWebsite;
    }
}
