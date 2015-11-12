package com.example.searchscreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.chooserimpl.AsyncTaskContactChooser;
import com.example.chooserimpl.ContactUtils;
import com.example.contact.ContactChooser;
import com.example.contact.PhoneContact;
import com.example.contact.SearchResultListener;
import com.example.contactinfo.R;
import com.example.phoneutils.NormalizerFactory;
import com.example.phoneutils.PhoneNumberNormalizer;

import org.apache.commons.lang3.StringUtils;

public class SearchActivity extends AppCompatActivity implements OnSearchActionListener {
    private static final String TAG = "PC";
    private static final String CONTACT = "CONTACT";

    private SearchScreen screen;
    private ContactChooser contactChooser;
    private PhoneNumberNormalizer numberNormalizer;
    private PhoneContact contact;

    /**
     * passed to {@link ContactChooser#chooseBestContactForNumber(String, SearchResultListener)}
     */
    private final SearchResultListener searchResultListener = new SearchResultListener() {
        @Override
        public void onSearchResult(PhoneContact contact) {
            SearchActivity.this.contact = contact;
            if (contact == null) {
                Log.d(TAG, "no contact found");
                screen.showNoContactFound();
            } else {
                Log.d(TAG, contact + " found");
                screen.showContact(contact);
            }
        }
    };

    // suppressing cause content view does not have parent
    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = (SearchScreen) getLayoutInflater().inflate(R.layout.search_screen, null);
        setContentView(screen);
        screen.setOnSearchListener(this);

        contactChooser = new AsyncTaskContactChooser(getContentResolver());
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
        String normalizedNumber = numberNormalizer.normalize(phoneNumber);
        if (isNumberValid(normalizedNumber)) {
            screen.showQueryInProgress();
            contactChooser.chooseBestContactForNumber(normalizedNumber, searchResultListener);
        } else {
            screen.setError("Invalid number");
        }
    }

    private boolean isNumberValid(String number) {
        return !StringUtils.isEmpty(number);
    }
}
