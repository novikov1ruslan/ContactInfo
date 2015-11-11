package com.example.searchscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.chooserimpl.ContactChooserImpl;
import com.example.chooserimpl.ContactUtils;
import com.example.contact.ContactChooser;
import com.example.contact.PhoneContact;
import com.example.contactinfo.R;
import com.example.phoneutils.NormalizerFactory;
import com.example.phoneutils.PhoneNumberNormalizer;


public class SearchActivity extends AppCompatActivity implements OnSearchListener {
    private static final String TAG = "PC";
    private static final String CONTACT = "CONTACT";

    private SearchScreen screen;

    private ContactChooser contactChooser;
    private PhoneNumberNormalizer numberNormalizer;
    private PhoneContact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = (SearchScreen) getLayoutInflater().inflate(R.layout.search_screen, null);
        setContentView(screen);
        screen.setOnSearchListener(this);

        contactChooser = new ContactChooserImpl(getContentResolver());
        numberNormalizer = NormalizerFactory.create();

        if (savedInstanceState != null) {
            restoreActivityState(savedInstanceState);
        }
    }

    private void restoreActivityState(Bundle savedInstanceState) {
        Log.d(TAG, "restoring activity state");
        String contactJson = savedInstanceState.getString(CONTACT);
        if (contactJson != null) {
            contact = ContactUtils.fomJson(contactJson);
            Log.d(TAG, contact + " restored");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (contact == null) {
            screen.showNoContactFound();
        } else {
            screen.showContact(contact);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveActivityState(outState);
    }

    private void saveActivityState(Bundle outState) {
        if (contact != null) {
            outState.putString(CONTACT, ContactUtils.toJson(contact));
        }
    }

    @Override
    public void onSearch(String phoneNumber) {
        String number = numberNormalizer.normalize(phoneNumber);
        contact = contactChooser.chooseBestContactForNumber(number);

        if (contact == null) {
            Log.d(TAG, "no contact found");
            screen.showNoContactFound();
        } else {
            Log.d(TAG, contact + " found");
            screen.showContact(contact);
        }
    }

}
