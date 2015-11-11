package com.example.phoneutils;

import android.telephony.PhoneNumberUtils;

class CommonNormalizer implements PhoneNumberNormalizer {

    @Override
    public String normalize(String phoneNumber) {
        return PhoneNumberUtils.stripSeparators(phoneNumber);
    }
}
