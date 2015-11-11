package com.example.searchscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.chooserimpl.ContactChooserImpl;
import com.example.contact.ContactChooser;
import com.example.contact.PhoneContact;
import com.example.novikov.contactinfo.R;
import com.example.phoneutils.NormalizerFactory;
import com.example.phoneutils.PhoneNumberNormalizer;

import java.util.List;


public class MainActivity extends AppCompatActivity implements OnSearchListener {
    private static final String TAG = "PC";

    private SearchScreen screen;

    private ContactChooser contactChooser;
    private PhoneNumberNormalizer numberNormalizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = (SearchScreen) getLayoutInflater().inflate(R.layout.search_screen, null);
        setContentView(screen);
        screen.setOnSearchListener(this);

        contactChooser = new ContactChooserImpl(getContentResolver());
        numberNormalizer = NormalizerFactory.create();
    }

    @Override
    public void onSearch(String phoneNumber) {
        String number = numberNormalizer.normalize(phoneNumber);
        List<PhoneContact> contacts = contactChooser.getContactsForNumber(number);

        if (contacts.isEmpty()) {
            Log.d(TAG, "no contacts found");
            screen.showNoContactFound();
        } else {
            Log.d(TAG, contacts.size() + " contacts found, retrieving");
            screen.showContact(contactChooser.chooseBestContact(contacts));
        }
    }

}
