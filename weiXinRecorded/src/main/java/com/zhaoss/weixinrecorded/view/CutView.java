package com.zhaoss.weixinrecorded.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhaoss.weixinrecorded.R;



public class CutView extends View {

    private int measuredWidth;
    private int measuredHeight;
    private Paint paint;
    private int dp3;
    private int cornerLength;
    private float marginLeft;
    private float marginRight;
    private float marginTop;
    private float marginBottom;
    private int dp1;

    public CutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CutView(Context context) {
        super(context);
        init();
    }

    private void init() {

        marginLeft = (int) getResources().getDimension(R.dimen.dp30);
        marginRight = (int) getResources().getDimension(R.dimen.dp30);
        marginTop = (int) getResources().getDimension(R.dimen.dp30);
        marginBottom = (int) getResources().getDimension(R.dimen.dp80);
        dp3 = (int) getResources().getDimension(R.dimen.dp3);
        dp1 = (int) getResources().getDimension(R.dimen.dp1);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
    }

    float downX;
    float downY;
    float lastSlideX;
    float lastSlideY;

    boolean isLeft;
    boolean isRight;
    boolean isTop;
    boolean isBottom;
    boolean isMove;

    float rectLeft;
    float rectRight;
    float rectTop;
    float rectBottom;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                downX = event.getX();
                downY = event.getY();
                //??????????????????????????????????????????
                if(Math.abs(rectLeft-downX) < cornerLength){
                    isLeft = true;
                }else if(Math.abs(rectRight-downX) < cornerLength){
                    isRight = true;
                }
                //??????????????????????????????????????????
                if(Math.abs(rectTop-downY) < cornerLength){
                    isTop = true;
                }else if(Math.abs(rectBottom-downY) < cornerLength){
                    isBottom = true;
                }
                //?????????????????????????????????????????????, ?????????????????????????????????????????????
                if(!isLeft && !isTop && !isRight && !isBottom){
                    isMove = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                //????????????????????????
                float slideX = moveX-downX+lastSlideX;
                float slideY = moveY-downY+lastSlideY;

                if(isMove){//???????????????????????????
                    rectLeft += slideX;
                    rectRight += slideX;
                    rectTop += slideY;
                    rectBottom += slideY;
                    //????????????left???right???, ???????????????????????????
                    if(rectLeft < marginLeft || rectRight > measuredWidth-marginRight) {//??????x??????????????????
                        rectLeft -= slideX;
                        rectRight -= slideX;
                    }
                    //????????????top???bottom???, ???????????????????????????
                    if(rectTop < marginTop || rectBottom > measuredHeight-marginBottom){//??????y??????????????????
                        rectTop -= slideY;
                        rectBottom -= slideY;
                    }
                }else{//????????????????????????
                    //?????????????????????
                    if(isLeft){
                        rectLeft += slideX;
                        if(rectLeft < marginLeft) rectLeft = marginLeft;
                        if(rectLeft > rectRight-cornerLength*2) rectLeft = rectRight-cornerLength*2;
                    }else if(isRight){
                        rectRight += slideX;
                        if(rectRight > measuredWidth-marginRight) rectRight = measuredWidth- marginRight;
                        if(rectRight < rectLeft+cornerLength*2) rectRight = rectLeft+cornerLength*2;
                    }
                    //?????????????????????, ?????????????????????(???????????????????????????),?????????????????????????????????
                    if(isTop){
                        rectTop += slideY;
                        if(rectTop < marginTop) rectTop = marginTop;
                        if(rectTop > rectBottom-cornerLength*2) rectTop = rectBottom-cornerLength*2;
                    }else if(isBottom){
                        rectBottom += slideY;
                        if(rectBottom > measuredHeight-marginBottom) rectBottom = measuredHeight- marginBottom;
                        if(rectBottom < rectTop+cornerLength*2) rectBottom = rectTop+cornerLength*2;
                    }
                }
                //????????????onDraw()??????
                invalidate();

                downX = moveX;
                downY = moveY;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isLeft = false;
                isRight = false;
                isTop = false;
                isBottom = false;
                isMove = false;
                break;
        }
        return true;
    }

    /**
     * ?????????????????????margin???
     */
    public float[] getCutArr(){

        float[] arr = new float[4];
        arr[0] = rectLeft-marginLeft;
        arr[1] = rectTop-marginTop;
        arr[2] = rectRight-marginLeft;
        arr[3] = rectBottom-marginTop;
        return arr;
    }

    public int getRectWidth(){
        return (int) (measuredWidth-marginLeft-marginRight);
    }

    public int getRectHeight(){
        return (int) (measuredHeight-marginTop-marginBottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(measuredWidth == 0) {
            initParams();
        }
    }

    public void setMargin(float left, float top, float right, float bottom) {

        marginLeft = left;
        marginTop = top;
        marginRight = right;
        marginBottom = bottom;
        initParams();
        invalidate();
    }

    private void initParams(){

        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
        cornerLength = measuredWidth / 10;

        rectLeft = marginLeft;
        rectRight = measuredWidth- marginRight;
        rectTop = marginTop;
        rectBottom = measuredHeight- marginBottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setStrokeWidth(dp1);
        //???????????????????????????, ??????margin??????????????????
        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint);
        //?????????????????????????????????
        drawLine(canvas, rectLeft, rectTop, rectRight, rectBottom);
    }

    /**
     * ?????????????????????????????????
     */
    private void drawLine(Canvas canvas, float left, float top, float right, float bottom){

        paint.setStrokeWidth(1);
        //?????????????????????
        float startX = (right-left)/3+left;
        float startY = top;
        float stopX = (right-left)/3+left;
        float stopY = bottom;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        startX = (right-left)/3*2+left;
        startY = top;
        stopX = (right-left)/3*2+left;
        stopY = bottom;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        startX = left;
        startY = (bottom-top)/3+top;
        stopX = right;
        stopY = (bottom-top)/3+top;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        startX = left;
        startY = (bottom-top)/3*2+top;
        stopX = right;
        stopY = (bottom-top)/3*2+top;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        paint.setStrokeWidth(dp3);
        //???????????????
        startX = left-dp3/2;
        startY = top;
        stopX = left+cornerLength;
        stopY = top;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        startX = left;
        startY = top;
        stopX = left;
        stopY = top+cornerLength;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        startX = right+dp3/2;
        startY = top;
        stopX = right-cornerLength;
        stopY = top;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        startX = right;
        startY = top;
        stopX = right;
        stopY = top+cornerLength;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        startX = left;
        startY = bottom;
        stopX = left;
        stopY = bottom-cornerLength;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        startX = left-dp3/2;
        startY = bottom;
        stopX = left+cornerLength;
        stopY = bottom;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        startX = right+dp3/2;
        startY = bottom;
        stopX = right-cornerLength;
        stopY = bottom;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        startX = right;
        startY = bottom;
        stopX = right;
        stopY = bottom-cornerLength;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }
}
