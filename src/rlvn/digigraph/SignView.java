package rlvn.digigraph;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

public class SignView extends View {

    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private VelocityTracker vt;
    private float xStart;
    private float yStart;
    private float mX, mY;
    private int previousVelocity = 0;

    // how much of a pixel difference there must be between the old and new
    // touch coordinate point
    private static final float TOUCH_TOLERANCE = 4;

    public SignView(Context context) {
        super(context);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        bitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels,
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        path = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0xFF000000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawColor(0xFFAAAAAA);

        canvas.drawBitmap(bitmap, 0, 0, null);

        canvas.drawPath(path, paint);
    }

    private void touch_start(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        xStart = mX = x;
        yStart = mY = y;
    }

    private void touch_move(float x, float y) {

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            vt.computeCurrentVelocity(1);
            // Log.d(TAG, "current x velocity: " + vt.getXVelocity());
            float xVelocity = vt.getXVelocity();
            float yVelocity = vt.getYVelocity();
            double velocity = Math.sqrt(xVelocity * xVelocity + yVelocity
                    * yVelocity);
            // shave the decimals from the velocity
            // this will determine how light of a color we will use for
            // the paint
            int truncatedVelocity = (int) velocity;
            if (truncatedVelocity != previousVelocity) {
                // max velocity (don't want the line too light)
                if (truncatedVelocity > 6)
                    truncatedVelocity = 6;
                int newColor = truncatedVelocity;
                // create a string of 6 truncatedVelocity's
                for (int i = 0; i < 5; i++)
                    newColor = newColor * 0x10 + truncatedVelocity;
                paint.setColor(newColor | 0xff000000);
            }
            float endPoint1 = (x + mX) / 2, endPoint2 = (y + mY) / 2;
            path.quadTo(mX, mY, endPoint1, endPoint2);
            if (truncatedVelocity != previousVelocity) {
                canvas.drawPath(path, paint);
                path.reset();
                path.moveTo(endPoint1, endPoint2);
            }
            mX = x;
            mY = y;
            previousVelocity = truncatedVelocity;
        }

    }

    private void touch_up() {
        // if there's just a dot, put a dot
        if (mX == xStart && mY == yStart)
            path.addCircle(mX, mY, (float) .5, Path.Direction.CCW);

        path.lineTo(mX, mY);
        // commit the path to our offscreen
        canvas.drawPath(path, paint);
        // kill this so we don't double draw
        path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            vt = VelocityTracker.obtain();
            vt.addMovement(event);
            touch_start(x, y);
            invalidate();
            break;
        case MotionEvent.ACTION_MOVE:
            vt.addMovement(event);
            touch_move(x, y);
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            vt.addMovement(event);
            touch_up();
            vt.recycle();
            invalidate();
            break;
        }
        return true;
    }
}
