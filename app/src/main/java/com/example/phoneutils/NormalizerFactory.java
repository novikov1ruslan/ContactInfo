package com.example.phoneutils;

import android.os.Build;

/**
 * creates most appropriate phone number normalizer
 */
public class NormalizerFactory {

    /**
     * creates most appropriate phone number normalizer
     */
    public static PhoneNumberNormalizer create() {
        if (Build.VERSION.SDK_INT >= 21) {
            return new LollipopNormalizer();
        }

        return new CommonNormalizer();
    }

}
