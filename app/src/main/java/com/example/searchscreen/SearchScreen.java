package com.example.searchscreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contact.PhoneContact;
import com.example.contactinfo.R;

import java.io.IOException;

public class SearchScreen extends LinearLayout {
    private static final String TAG = "PC";

    private TextView phoneNumberView;
    private TextView statusText;
    private View contactInfoPanel;
    private TextView contactNumberView;
    private TextView contactNameView;
    private ImageView thumbnailView;
    private View thumbnailBackground;
    private OnSearchActionListener onSearchListener;
    private View searchButton;

    public SearchScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        phoneNumberView = (TextView) findViewById(R.id.phone_number);
        statusText = (TextView) findViewById(R.id.status);
        contactInfoPanel = findViewById(R.id.contact_info_panel);

        contactNameView = (TextView) findViewById(R.id.contact_name);
        contactNumberView = (TextView) findViewById(R.id.contact_number);
        thumbnailView = (ImageView) findViewById(R.id.thumbnail);
        thumbnailBackground = findViewById(R.id.thumbnail_frame);
        searchButton = findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchListener == null) {
                    Log.w(TAG, "search listener is not set");
                } else {
                    onSearchListener.onSearch(phoneNumberView.getText().toString());
                }
             }
        });
    }

    public void showNoContactFound() {
        statusText.setText(R.string.no_contact_found);
        searchButton.setEnabled(true);
        statusText.setVisibility(View.VISIBLE);
        contactInfoPanel.setVisibility(View.GONE);
    }

    public void showContact(PhoneContact contact) {
        Log.v(TAG, "displaying contact info for: " + contact);

        statusText.setVisibility(View.GONE);
        contactInfoPanel.setVisibility(View.VISIBLE);
        searchButton.setEnabled(true);
        contactNameView.setText(contact.getName());
        if (contact.hasPhoto()) {

            Uri uri = Uri.parse(contact.getPhotoUri());
            try {
                Bitmap bm = ThumbnailUtils.getBitmapFromUri(uri, getContext().getContentResolver());
                thumbnailView.setImageBitmap(bm);
                thumbnailBackground.setBackgroundColor(ThumbnailUtils.getPhotoBackground(bm));
            } catch (IOException ioe) {
                Log.w(TAG, "exception while setting thumbnail", ioe);
            }
        } else {
            thumbnailView.setImageResource(R.drawable.no_thumbnail);
//            int color = getResources().getColor(R.color.no_thumbnail_background);
            int color = ContextCompat.getColor(getContext(), R.color.no_thumbnail_background);
            thumbnailBackground.setBackgroundColor(color);
        }

        contactNumberView.setText(contact.getPhoneNumber());
    }

    public void setOnSearchListener(OnSearchActionListener listener) {
        this.onSearchListener = listener;
    }

    public void setError(CharSequence message) {
        phoneNumberView.setError(message);
    }

    public void showQueryInProgress() {
        searchButton.setEnabled(false);
        statusText.setText(R.string.searching);
    }
}
