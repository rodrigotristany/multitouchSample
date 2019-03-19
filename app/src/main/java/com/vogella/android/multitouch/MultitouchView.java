package com.vogella.android.multitouch;

        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.PointF;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.util.SparseArray;
        import android.view.MotionEvent;
        import android.view.ScaleGestureDetector;
        import android.view.View;
        import android.widget.Toast;

public class MultitouchView extends View {

    private static final int SIZE = 60;

    private SparseArray<PointF> mActivePointers;
    private Paint mPaint;
    private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
            Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW };

    private Paint textPaint;
    private ScaleGestureDetector scaleGesture;
    private OnTouchListener onTouchListener;

    private int last = MotionEvent.ACTION_DOWN;


    public MultitouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        scaleGesture = new ScaleGestureDetector(this.getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return false;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });
        onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int count = event.getPointerCount();
                return false;
            }
        };
        this.setOnTouchListener(onTouchListener);
        mActivePointers = new SparseArray<PointF>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // set painter color to a color you like
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        //get event pointers count
        int pointerCount = event.getPointerCount();

        if(scaleGesture != null)
            scaleGesture.onTouchEvent(event);

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                if(pointerCount == 2 && (pointerId == 1 || pointerId == 0)) {
                    last = MotionEvent.ACTION_POINTER_DOWN;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                last = MotionEvent.ACTION_MOVE;
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                int lss = event.getActionMasked();
            }
            case MotionEvent.ACTION_POINTER_UP: {
                int lss = event.getActionMasked();
                if(pointerCount == 2 && (pointerId == 1 || pointerId == 0)) {
                    if(last == MotionEvent.ACTION_MOVE) {
                        Toast.makeText(this.getContext(), "Hice DRAG", Toast.LENGTH_SHORT).show();
                        Log.d("DRAG", "RODRIGO");
                    }
                    if(last == MotionEvent.ACTION_POINTER_DOWN) {
                        Toast.makeText(this.getContext(), "Hice MULTITAP", Toast.LENGTH_SHORT).show();
                        Log.d("MULTITAP", "RODRIGO");
                    }
                }
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers.remove(pointerId);
                break;
            }
        }
        invalidate();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw all pointers
        for (int size = mActivePointers.size(), i = 0; i < size; i++) {
            PointF point = mActivePointers.valueAt(i);
            if (point != null)
                mPaint.setColor(colors[i % 9]);
            canvas.drawCircle(point.x, point.y, SIZE, mPaint);
        }
        canvas.drawText("Total pointers: " + mActivePointers.size(), 10, 40 , textPaint);
    }

}
