package com.example.phoneutils;

public interface PhoneNumberNormalizer {

    /**
     * @param phoneNumber number to normalize
     * @return normalized form of the {@code phoneNumber}
     */
    String normalize(String phoneNumber);
}
