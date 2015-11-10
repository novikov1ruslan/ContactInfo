package com.example.novikov.contactinfo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ANY_CONTACT_INDEX = 0;
    private static final String TAG = "PC";
    TextView phoneNumber;
    View noContactFoundPanel;
    View contactInfoPanel;
    TextView contactNumberView;
    TextView contactNameView;
    ImageView thumbnailView;

    private static final String[] PROJECTION = {Contacts._ID, Contacts.DISPLAY_NAME, Contacts.PHOTO_THUMBNAIL_URI, PhoneLookup.NUMBER};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumber = (TextView) findViewById(R.id.phone_number);
        noContactFoundPanel = findViewById(R.id.status);
        contactInfoPanel = findViewById(R.id.contact_info_panel);

        contactNameView = (TextView) findViewById(R.id.contact_name);
        contactNumberView = (TextView) findViewById(R.id.contact_number);
        thumbnailView = (ImageView) findViewById(R.id.thumbnail);
    }

    public void onSearch(View view) {
        ContentResolver resolver = getContentResolver();
//        String number = phoneNumber.getText().toString();
        String number = "0151 220 87605";
        if (Build.VERSION.SDK_INT >= 21) {
            number = PhoneNumberUtils.normalizeNumber(number);
        }
        else {
            number = PhoneNumberUtils.stripSeparators(number);
        }

        Uri uri = getUriForNumber(number);
        Cursor cur = resolver.query(uri, PROJECTION, null, null, null);
        if (cur.getCount() > 0) {
            Log.d(TAG, cur.getCount() + " contacts found, retrieving");
            List<PhoneContact> contacts = getContactsFromCursor(cur);
            PhoneContact contact = chooseBestContact(contacts);
            showContact(contact);
        } else {
            Log.d(TAG, "no contacts found");
            showNoContactFound();
        }
    }

    private Uri getUriForNumber(String number) {
        return Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
    }

    private void showNoContactFound() {
        noContactFoundPanel.setVisibility(View.VISIBLE);
        contactInfoPanel.setVisibility(View.GONE);
    }

    private void showContact(PhoneContact contact) {
        Log.v(TAG, "displaying contact info for: " + contact);
        
        noContactFoundPanel.setVisibility(View.GONE);
        contactInfoPanel.setVisibility(View.VISIBLE);
        contactNameView.setText(contact.getName());
        if (contact.hasPhoto()) {
            Uri uri = Uri.parse(contact.getPhotoUri());
            thumbnailView.setImageURI(uri);
        }
        contactNumberView.setText(contact.getPhoneNumber());
    }

    private PhoneContact chooseBestContact(List<PhoneContact> contacts) {
        for (PhoneContact contact :
                contacts) {
            if (contact.hasPhoto()) {
                Log.d(TAG, contact + " has photo, returning");
                return contact;
            }
        }

        PhoneContact contact = contacts.get(ANY_CONTACT_INDEX);
        Log.d(TAG, "none has photo, returning the first: " + contact);
        return contact;
    }

    private List<PhoneContact> getContactsFromCursor(Cursor cur) {
        List<PhoneContact> contacts = new ArrayList<PhoneContact>();
        while (cur.moveToNext()) {
            contacts.add(extractContactFromCursor(cur));
        }
        return contacts;
    }

    @NonNull
    private PhoneContact extractContactFromCursor(Cursor cur) {
        String phoneNumber = cur.getString(cur.getColumnIndex(PhoneLookup.NUMBER));
        PhoneContact contact = new PhoneContact(phoneNumber);
        contact.setPhotoUri(cur.getString(cur.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI)));
        contact.setName(cur.getString(cur.getColumnIndex(Contacts.DISPLAY_NAME)));
        return contact;
    }
}
