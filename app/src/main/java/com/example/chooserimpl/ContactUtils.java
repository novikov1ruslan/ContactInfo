package com.example.chooserimpl;

import com.example.contact.PhoneContact;
import com.google.gson.Gson;

/**
 * Utility class that helps serialize/de-serialize a {@link com.example.contact.PhoneContact}
 */
public class ContactUtils {

    private ContactUtils() {
        // utility class
    }

    public static String toJson(PhoneContact contact) {
        return new Gson().toJson(contact);
    }

    public static PhoneContact fomJson(String contactJson) {
        return new Gson().fromJson(contactJson, PhoneContact.class);
    }
}
