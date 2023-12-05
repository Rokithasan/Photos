package com.example.photos.model;

import android.content.Context;
import android.util.AttributeSet;

import androidx.cardview.widget.CardView;

public class ClickableCardView extends CardView {

    public ClickableCardView(Context context) {
        super(context);
    }

    public ClickableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        // Handle the click event here if needed
        super.performClick();
        return true;
    }
}
