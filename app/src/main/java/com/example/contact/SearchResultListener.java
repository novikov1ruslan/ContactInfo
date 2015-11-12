package com.example.contact;

public interface SearchResultListener {

    /**
     * always called on the main thread
     *
     * @param contact return contact, can be {@code null}
     */
    void onSearchResult(PhoneContact contact);
}
