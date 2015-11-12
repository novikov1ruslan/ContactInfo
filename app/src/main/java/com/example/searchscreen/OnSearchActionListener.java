package com.example.searchscreen;

public interface OnSearchActionListener {

    /**
     * Fired when search needs to be performed
     *
     * @param phoneNumber number to query for
     */
    void onSearch(String phoneNumber);
}
