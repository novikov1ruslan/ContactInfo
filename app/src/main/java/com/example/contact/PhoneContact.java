package com.example.contact;

public class PhoneContact {
    private String name;
    private String photoUri;

    private final String phoneNumber;

    public static class Builder {

        private String name;
        private String photoUri;

        private final String phoneNumber;

        public Builder(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Builder setPhotoUri(String photoUri) {
            this.photoUri = photoUri;
            return this;
        }


        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public PhoneContact build() {
            PhoneContact contact = new PhoneContact(phoneNumber);
            contact.photoUri = photoUri;
            contact.name = name;
            return contact;
        }

    }

    private PhoneContact(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public boolean hasPhoto() {
        return photoUri != null;
    }

    @Override
    public String toString() {
        return "C:[" + name + ";" + phoneNumber + "]";
    }
}
