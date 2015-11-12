package com.example.contact;

public interface ContactChooser {
    /**
     * @param number   number to query for
     * @param listener listener where the result will be returned
     * @throws IllegalArgumentException if the number is blank
     * @throws NullPointerException if the listener is null
     */
    void chooseBestContactForNumber(String number, SearchResultListener listener);
}
