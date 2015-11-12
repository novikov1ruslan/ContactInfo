package com.example.chooserimpl;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;

import com.example.contact.ContactChooser;
import com.example.contact.PhoneContact;
import com.example.contact.SearchResultListener;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Chooser logic implementation according to the requirements.
 * This implementation uses {@link AsyncTask}, so its methods must be called in the main thread.
 * Additionally lifecycle of the calling listener is not taken into account.
 */
public class AsyncTaskContactChooser implements ContactChooser {
    private static final String TAG = "PC";

    private static final int CACHE_ENTRIES = 2; // should be tweaked as needed
    private static final String[] PROJECTION = {ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.PhoneLookup.NUMBER};

    private final ContentResolver resolver;

    private final LruCache<String, PhoneContact> cache = new LruCache<String, PhoneContact>(CACHE_ENTRIES);
    private SearchResultListener listener;

    public AsyncTaskContactChooser(ContentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void chooseBestContactForNumber(String number, SearchResultListener listener) {
        this.listener = Validate.notNull(listener);
        new ChoosingTask().execute(Validate.notBlank(number));
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

    private class ChoosingTask extends AsyncTask<String, Void, PhoneContact> {

        @Override
        protected PhoneContact doInBackground(String... numbers) {
            long time1 = SystemClock.elapsedRealtime();
            String number = numbers[0];
            PhoneContact contact = cache.get(number);
            if (contact == null) {
                Log.d(TAG, "cache miss for: " + number);
                contact = queryBestContactForNumber(number);
//                debug_simulate_long_query();
                if (contact != null) {
                    cache.put(number, contact);
                }
            }
            Log.d(TAG, "query time: " + (SystemClock.elapsedRealtime() - time1) + "ms");

            return contact;
        }

        private void debug_simulate_long_query() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @Override
        protected void onPostExecute(PhoneContact contact) {
            Log.d(TAG, "query returned: " + contact + ", free cache entries: " + freeCacheEntries());
            listener.onSearchResult(contact);
        }
    }

    ;
}
