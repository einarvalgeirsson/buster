package com.einarvalgeir.bussrapport.events;

import android.graphics.Bitmap;

public class AttachImageEvent {
    Bitmap bmp;

    public AttachImageEvent(Bitmap bmp) {
        this.bmp = bmp;
    }

    public Bitmap getBitmap() {
        return bmp;
    }
}
