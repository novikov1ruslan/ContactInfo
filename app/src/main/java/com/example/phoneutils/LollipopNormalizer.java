package com.example.phoneutils;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.PhoneNumberUtils;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class LollipopNormalizer implements PhoneNumberNormalizer {

    @Override
    public String normalize(String phoneNumber) {
//        number = "0151 220 87605";
        return PhoneNumberUtils.normalizeNumber(phoneNumber);
    }
}
