package com.example.contact;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.graphics.Palette;

import java.io.IOException;
import java.io.InputStream;

public class ThumbnailUtils {
    private ThumbnailUtils() {
        // utility class
    }

    public static Bitmap getBitmapFromUri(Uri uri, ContentResolver resolver) throws IOException {
        InputStream is = resolver.openInputStream(uri);
        try {
            return BitmapFactory.decodeStream(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static int getPhotoBackground(Bitmap bm) {
        Palette palette = Palette.from(bm).generate();
        return palette.getDarkMutedColor(0);
    }
}
