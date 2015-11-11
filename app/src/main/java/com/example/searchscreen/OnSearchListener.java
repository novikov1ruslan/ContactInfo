package com.example.searchscreen;

public interface OnSearchListener {

    /**
     * Fired when search needs to be performed
     *
     * @param phoneNumber number to query for
     */
    void onSearch(String phoneNumber);
}
