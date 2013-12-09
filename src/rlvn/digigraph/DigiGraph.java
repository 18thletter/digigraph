package rlvn.digigraph;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

public class DigiGraph extends Activity {
    // implements ColorPicker.OnColorChangedListener {
    private static final String TAG = "PhotoAutograph";
    // private Uri imageUri = null;
    private Paint paint;

    // private MaskFilter mEmboss;
    // private MaskFilter mBlur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(new SignView(this));

        Intent intent = getIntent();
        if (Intent.ACTION_MAIN.equals(intent.getAction()))
            setContentView(new SignView(this));

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0xFF000000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(2);

        // if (Intent.ACTION_SEND.equals(intent.getAction())) {
        // Log.d(TAG, "intent=send");
        // imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        // }
        // if (imageUri!=null){
        // // ImageView im=new ImageView(this);
        // // im.setImageURI(imageUri);
        // setContentView(new PhotoView(this));
        // }

        // mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
        // 0.4f, 6, 3.5f);
        //
        // mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }

    // public void colorChanged(int color) {
    // mPaint.setColor(color);
    // }

    // public class PhotoView extends ImageView {
    //
    // public PhotoView(Context context) {
    // super(context);
    // if (imageUri != null) {
    // setImageURI(imageUri);
    // }
    //
    // }
    //
    // @Override
    // protected void onDraw(Canvas canvas) {
    // super.onDraw(canvas);
    // if (imageUri != null) {
    // Paint p = new Paint();
    // p.setColor(Color.RED);
    // canvas.drawCircle(75, 75, 75, p);
    // }
    // }
    //
    // }

    public class SignView extends View {

        private Bitmap bitmap;
        private Canvas canvas;
        private Path path;
        private VelocityTracker vt;
        private float xStart;
        private float yStart;

        // private Paint bitmapPaint;

        public SignView(Context c) {
            super(c);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            bitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels,
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            path = new Path();
            // bitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // canvas.drawColor(0xFFAAAAAA);

            canvas.drawBitmap(bitmap, 0, 0, null);

            canvas.drawPath(path, paint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            xStart = x;
            yStart = y;
            path.reset();
            path.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            vt.computeCurrentVelocity(1);
            Log.d(TAG, "current x velocity: " + vt.getXVelocity());
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }

        }

        private void touch_up() {
            // if there's just a dot, put a dot
            if (mX == xStart && mY == yStart)
                path.addCircle(mX, mY, (float) .5, Path.Direction.CCW);
            // if there's a dot in a certain direction of the pen, put a
            // tear drop shaped dot
            if (Math.abs(mX - xStart) < 4 && Math.abs(mY - yStart) < 4)
                ;
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

    // private static final int COLOR_MENU_ID = Menu.FIRST;
    // private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
    // private static final int BLUR_MENU_ID = Menu.FIRST + 2;
    // private static final int ERASE_MENU_ID = Menu.FIRST + 3;
    // private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // super.onCreateOptionsMenu(menu);

        // menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
        // menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
        // menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
        // menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
        // menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');

        /****
         * Is this the mechanism to extend with filter effects? Intent intent =
         * new Intent(null, getIntent().getData());
         * intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
         * menu.addIntentOptions( Menu.ALTERNATIVE, 0, new ComponentName(this,
         * NotesList.class), null, intent, 0, null);
         *****/
        getMenuInflater().inflate(R.menu.signview_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // mPaint.setXfermode(null);
        // mPaint.setAlpha(0xFF);
        //
        // switch (item.getItemId()) {
        // case COLOR_MENU_ID:
        // new ColorPicker(this, this, mPaint.getColor()).show();
        // return true;
        // case EMBOSS_MENU_ID:
        // if (mPaint.getMaskFilter() != mEmboss) {
        // mPaint.setMaskFilter(mEmboss);
        // } else {
        // mPaint.setMaskFilter(null);
        // }
        // return true;
        // case BLUR_MENU_ID:
        // if (mPaint.getMaskFilter() != mBlur) {
        // mPaint.setMaskFilter(mBlur);
        // } else {
        // mPaint.setMaskFilter(null);
        // }
        // return true;
        // case ERASE_MENU_ID:
        // mPaint.setXfermode(new PorterDuffXfermode(
        // PorterDuff.Mode.CLEAR));
        // return true;
        // case SRCATOP_MENU_ID:
        // mPaint.setXfermode(new PorterDuffXfermode(
        // PorterDuff.Mode.SRC_ATOP));
        // mPaint.setAlpha(0x80);
        // return true;
        // }
        // return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
        case R.id.menu_clear:
            setContentView(new SignView(this));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
