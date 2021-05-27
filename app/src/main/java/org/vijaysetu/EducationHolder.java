package org.vijaysetu;

public class EducationHolder {
    String title = "";
    String fullAddress = "";
    String phone = "";
    String rating = "";
    String shortAddress = "";

    public EducationHolder() {
    }

    public EducationHolder(String title, String fullAddress, String phone, String rating) {
        this.title = title;
        this.fullAddress = fullAddress;
        this.phone = phone;
        this.rating = rating;
    }
    public String getTitle() {
        return title;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getPhone() {
        return "+91 "+phone;
    }

    public String getShortAddress() {
        if (getFullAddress().contains(","))
            return getFullAddress().split(",")[0];
        else
            return shortAddress;
    }

    public String getRating() {
        return rating;
    }
}
