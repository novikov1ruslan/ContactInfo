package com.example.phoneutils;

import android.os.Build;

public class NormalizerFactory {

    public static PhoneNumberNormalizer create() {
        if (Build.VERSION.SDK_INT >= 21) {
            return new LollipopNormalizer();
        }

        return new CommonNormalizer();
    }

}
