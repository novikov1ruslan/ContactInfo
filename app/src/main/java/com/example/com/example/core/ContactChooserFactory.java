package com.example.com.example.core;

public class ContactChooserFactory {

    private ContactChooserFactory() {
        // utility class
    }

    public static ContactChooser getContactChooser() {
        return new ContactChooserImpl();
    }

}
