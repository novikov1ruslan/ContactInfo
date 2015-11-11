package com.example.contact;

import java.util.List;

public interface ContactChooser {
    List<PhoneContact> getContactsForNumber(String cur);

    PhoneContact chooseBestContact(List<PhoneContact> contacts);
}
