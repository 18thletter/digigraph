/*
 * Code based off the PhotoSortrView from Luke Hutchinson's MTPhotoSortr
 * example (http://code.google.com/p/android-multitouch-controller/)
 *
 * License:
 *   Dual-licensed under the Apache License v2 and the GPL v2.
 */
package org.metalev.multitouch.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

public class ImageEntity extends MultiTouchEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 4626382109609861973L;

    private static final double INITIAL_SCALE_FACTOR = 1.00;

    private transient Drawable mDrawable;

    private int mResourceId;
    private Uri imageUri;

    public ImageEntity(Uri imageUri, Resources res) {
        super(res);
        this.imageUri = imageUri;
        Log.d("digigraph", imageUri.getPath());
    }

    // public ImageEntity(int resourceId, Resources res) {
    // super(res);
    //
    // mResourceId = resourceId;
    // }

    public ImageEntity(ImageEntity e, Resources res) {
        super(res);

        mDrawable = e.mDrawable;
        mResourceId = e.mResourceId;
        mScaleX = e.mScaleX;
        mScaleY = e.mScaleY;
        mCenterX = e.mCenterX;
        mCenterY = e.mCenterY;
        mAngle = e.mAngle;
    }

    public void draw(Canvas canvas) {
        canvas.save();

        float dx = (mMaxX + mMinX) / 2;
        float dy = (mMaxY + mMinY) / 2;

        mDrawable.setBounds((int) mMinX, (int) mMinY, (int) mMaxX, (int) mMaxY);

        canvas.translate(dx, dy);
        canvas.rotate(mAngle * 180.0f / (float) Math.PI);
        canvas.translate(-dx, -dy);

        mDrawable.draw(canvas);

        canvas.restore();
    }

    /**
     * Called by activity's onPause() method to free memory used for loading the
     * images
     */
    @Override
    public void unload() {
        this.mDrawable = null;
    }

    /** Called by activity's onResume() method to load the images */
    @Override
    public void load(Context context, float startMidX, float startMidY) {
        Resources res = context.getResources();
        getMetrics(res);

        mStartMidX = startMidX;
        mStartMidY = startMidY;

        // mDrawable = res.getDrawable(mResourceId);

        if ("content".equals(imageUri.getScheme())) {
            try {
                mDrawable = Drawable.createFromStream(context
                        .getContentResolver().openInputStream(imageUri), null);
            } catch (Exception e) {
                Log.w("ImageEntity", "Unable to open content: " + imageUri, e);
            }
        } else {
            mDrawable = Drawable.createFromPath(imageUri.toString());
        }

        mWidth = mDrawable.getIntrinsicWidth();
        mHeight = mDrawable.getIntrinsicHeight();

        float centerX;
        float centerY;
        float scaleX;
        float scaleY;
        float angle;
        if (mFirstLoad) {
            centerX = startMidX;
            centerY = startMidY;

            float scaleFactor = (float) (Math
                    .max(mDisplayWidth, mDisplayHeight)
                    / (float) Math.max(mWidth, mHeight) * INITIAL_SCALE_FACTOR);
            scaleX = scaleY = scaleFactor;
            angle = 0.0f;

            mFirstLoad = false;
        } else {
            centerX = mCenterX;
            centerY = mCenterY;
            scaleX = mScaleX;
            scaleY = mScaleY;
            angle = mAngle;
        }
        setPos(centerX, centerY, scaleX, scaleY, angle);
    }
}
