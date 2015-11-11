package com.example.searchscreen;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.example.chooserimpl.ContactChooserImpl;
import com.example.contact.ContactChooser;
import com.example.contact.PhoneContact;
import com.example.novikov.contactinfo.R;

import java.util.List;


public class MainActivity extends AppCompatActivity implements OnSearchListener {
    private static final String TAG = "PC";

    private SearchScreen screen;

    private ContactChooser contactChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = (SearchScreen) getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(screen);
        screen.setOnSearchListener(this);

        contactChooser = new ContactChooserImpl(getContentResolver());
    }

    @Override
    public void onSearch(String phoneNumber) {
        String number = extractNormalizedPhoneNumber(phoneNumber);
        List<PhoneContact> contacts = contactChooser.getContactsForNumber(number);

        if (contacts.isEmpty()) {
            Log.d(TAG, "no contacts found");
            screen.showNoContactFound();
        } else {
            Log.d(TAG, contacts.size() + " contacts found, retrieving");
            screen.showContact(contactChooser.chooseBestContact(contacts));
        }
    }

    private String extractNormalizedPhoneNumber(String phoneNumber) {
        String number;
        if (Build.VERSION.SDK_INT >= 21) {
            number = PhoneNumberUtils.normalizeNumber(phoneNumber);
        } else {
            number = PhoneNumberUtils.stripSeparators(phoneNumber);
        }
//        number = "0151 220 87605";
        return number;
    }
}
