package com.example.chooserimpl;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;

import com.example.contact.ContactChooser;
import com.example.contact.PhoneContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Chooser logic implementation according to the requirements
 */
public class ContactChooserImpl implements ContactChooser {
    private static final String TAG = "PC";

    private static final int CACHE_ENTRIES = 2; // should be tweaked as needed
    private static final String[] PROJECTION = {ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.PhoneLookup.NUMBER};

    private final ContentResolver resolver;

    private final LruCache<String, PhoneContact> cache = new LruCache<String, PhoneContact>(CACHE_ENTRIES);

    public ContactChooserImpl(ContentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public PhoneContact chooseBestContactForNumber(String number) {
        long time1 = SystemClock.elapsedRealtime();
        PhoneContact contact = cache.get(number);
        if (contact == null) {
            contact = queryBestContactForNumber(number);
            if (contact != null) {
                cache.put(number, contact);
            }
            Log.d(TAG, "cache miss for: " + number + ", query returned: " + contact + ", free cache entries: " + freeCacheEntries());
        }
        Log.d(TAG, "query time: " + (SystemClock.elapsedRealtime() - time1) + "ms");

        return contact;
    }

    private PhoneContact queryBestContactForNumber(String number) {
        List<PhoneContact> contacts = queryContactsForNumber(number);
        if (contacts.isEmpty()) {
            return null;
        }
        return chooseBestContact(contacts);
    }

    private List<PhoneContact> queryContactsForNumber(String number) {
        List<PhoneContact> contacts = new ArrayList<PhoneContact>();
        Cursor cur = query(number);
        while (cur.moveToNext()) {
            contacts.add(extractContactFromCursor(cur));
        }
        return contacts;
    }

    private PhoneContact chooseBestContact(List<PhoneContact> contacts) {
        for (PhoneContact contact :
                contacts) {
            if (contact.hasPhoto()) {
                Log.d(TAG, contact + " has photo, returning");
                return contact;
            }
        }

        Log.d(TAG, "none has photo, returning any");
        return contacts.get(0);
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

    private int freeCacheEntries() {
        return cache.maxSize() - cache.size();
    }
}
