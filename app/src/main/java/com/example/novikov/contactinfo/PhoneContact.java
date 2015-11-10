package com.example.novikov.contactinfo;

public class PhoneContact {
    private String name;
    private String photoUri;

    private final String phoneNumber;

    public PhoneContact(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public boolean hasPhoto() {
        return photoUri != null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "C:[" + name + ";" + phoneNumber + "]";
    }
}
