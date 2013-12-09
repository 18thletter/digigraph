package rlvn.digigraph;

import org.metalev.multitouch.controller.MultiTouchController;
import org.metalev.multitouch.controller.MultiTouchController.MultiTouchObjectCanvas;
import org.metalev.multitouch.controller.MultiTouchController.PointInfo;
import org.metalev.multitouch.controller.MultiTouchController.PositionAndScale;

import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class PhotoView extends View implements MultiTouchObjectCanvas<Object> {
    private MultiTouchController<Object> multiTouchController = new MultiTouchController<Object>(
            this);
    private Uri photoUri;
    private DisplayMetrics metrics;

    public PhotoView(Context context, Uri imageUri, DisplayMetrics dm) {
        super(context);
        photoUri = imageUri;
        metrics = dm;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return multiTouchController.onTouchEvent(event);
    }

    @Override
    public Object getDraggableObjectAtPoint(PointInfo touchPoint) {
        // TODO Auto-generated method stub
        // for photo, no draggable object, but signature will be
        // adding signature later...no draggables for now
        return null;
    }

    @Override
    public boolean pointInObjectGrabArea(PointInfo touchPoint, Object obj) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void getPositionAndScale(Object obj,
            PositionAndScale objPosAndScaleOut) {
        // TODO Auto-generated method stub
        // drag or stretch op...won't be any until add signature
    }

    @Override
    public boolean setPositionAndScale(Object obj,
            PositionAndScale newObjPosAndScale, PointInfo touchPoint) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void selectObject(Object obj, PointInfo touchPoint) {
        // TODO Auto-generated method stub

    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
