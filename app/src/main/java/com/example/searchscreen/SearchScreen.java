package com.example.searchscreen;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contact.PhoneContact;
import com.example.novikov.contactinfo.R;

public class SearchScreen extends LinearLayout {
    private static final String TAG = "PC";

    private TextView phoneNumberView;
    private View noContactFoundPanel;
    private View contactInfoPanel;
    private TextView contactNumberView;
    private TextView contactNameView;
    private ImageView thumbnailView;
    private View thumbnailBackground;
    private OnSearchListener onSearchListener;

    public SearchScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        phoneNumberView = (TextView) findViewById(R.id.phone_number);
        noContactFoundPanel = findViewById(R.id.status);
        contactInfoPanel = findViewById(R.id.contact_info_panel);

        contactNameView = (TextView) findViewById(R.id.contact_name);
        contactNumberView = (TextView) findViewById(R.id.contact_number);
        thumbnailView = (ImageView) findViewById(R.id.thumbnail);
        thumbnailBackground = findViewById(R.id.thumbnail_frame);
        findViewById(R.id.search_btn).setOnClickListener(new OnClickListener() {
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
        noContactFoundPanel.setVisibility(View.VISIBLE);
        contactInfoPanel.setVisibility(View.GONE);
    }

    public void showContact(PhoneContact contact) {
        Log.v(TAG, "displaying contact info for: " + contact);

        noContactFoundPanel.setVisibility(View.GONE);
        contactInfoPanel.setVisibility(View.VISIBLE);
        contactNameView.setText(contact.getName());
        if (contact.hasPhoto()) {
            Uri uri = Uri.parse(contact.getPhotoUri());
            thumbnailView.setImageURI(uri);
        }
//        Palette palette = Palette.from(thumbnailView.getDrawingCache()).generate();
//        int photoBackground = palette.getDarkMutedColor(0);
//        thumbnailBackground.setBackgroundColor(photoBackground);
        contactNumberView.setText(contact.getPhoneNumber());
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.onSearchListener = listener;
    }
}
