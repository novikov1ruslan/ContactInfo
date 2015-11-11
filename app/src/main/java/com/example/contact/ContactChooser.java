package com.example.contact;

import android.database.Cursor;

import com.example.contact.PhoneContact;

import java.util.List;

public interface ContactChooser {
    List<PhoneContact> getContactsForNumber(String cur);

    PhoneContact chooseBestContact(List<PhoneContact> contacts);
}
