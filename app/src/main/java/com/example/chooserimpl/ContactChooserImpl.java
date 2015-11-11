package com.example.chooserimpl;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.contact.ContactChooser;
import com.example.contact.PhoneContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Chooser logic implementation according to the requirements
 */
public class ContactChooserImpl implements ContactChooser {
    private static final String TAG = "PC";

    private static final int ANY_CONTACT_INDEX = 0;
    private static final String[] PROJECTION = {ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.PhoneLookup.NUMBER};

    private final ContentResolver resolver;

    public ContactChooserImpl(ContentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public List<PhoneContact> getContactsForNumber(String number) {
        List<PhoneContact> contacts = new ArrayList<PhoneContact>();
        Cursor cur = query(number);
        while (cur.moveToNext()) {
            contacts.add(extractContactFromCursor(cur));
        }
        return contacts;
    }

    private Cursor query(String number) {
        return resolver.query(getUriForNumber(number), PROJECTION, null, null, null);
    }

    private Uri getUriForNumber(String number) {
        return Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
    }

    @NonNull
    private PhoneContact extractContactFromCursor(Cursor cur) {
        String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.NUMBER));
        PhoneContact.Builder builder = new PhoneContact.Builder(phoneNumber);
        builder.setPhotoUri(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)));
        builder.setName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

        return builder.build();
    }

    @Override
    public PhoneContact chooseBestContact(List<PhoneContact> contacts) {
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

}
