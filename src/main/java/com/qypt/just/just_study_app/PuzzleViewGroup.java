package com.qypt.just.just_study_app;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Administrator on 2016/7/4.
 */
public class PuzzleViewGroup extends ViewGroup implements Animator.AnimatorListener {

    private static int TIMES = 1; //关书

    private Bitmap bitmap;
    private static final Random RANDOM = new Random();  // 随机函数

    private static final ArrayList<ImageView> list = new ArrayList<ImageView>();

    private static final String TAG = "PuzzleViewGroup";
    private int screenWidth;
    private int scrrenHeight;
    private int DEFWH;

    private boolean isSelected = false;//是否切换图片
    private View target;
    private View target1;

    private static final ObjectAnimator targetObjectAnimator = new ObjectAnimator();
    private static final ObjectAnimator target2ObjectAnimator = new ObjectAnimator();

    public PuzzleViewGroup(Context context) {
        super(context);
    }

    public PuzzleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        targetObjectAnimator.setInterpolator(new LinearInterpolator());
        target2ObjectAnimator.setInterpolator(new LinearInterpolator());
        targetObjectAnimator.addListener(this);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        scrrenHeight = context.getResources().getDisplayMetrics().heightPixels;
        if (screenWidth < scrrenHeight) {
            DEFWH = screenWidth;
        } else {
            DEFWH = scrrenHeight;
        }
    }

    public PuzzleViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        this.setMeasuredDimension(widthMode == MeasureSpec.AT_MOST ? DEFWH : MeasureSpec.getSize(widthMeasureSpec),
                heightMode == MeasureSpec.AT_MOST ? DEFWH : MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        int downX;
        int downY;
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                if (!isSelected) {
                    target = getTarget(x, y);
                    if (target != null) {
                        setDrawableFitter(target);
                    } else {
                        isSelected = !isSelected;
                    }
                    isSelected = !isSelected;
//                    Log.i("PuzzleViewGroup","!isSelected");
                } else {
//                    Log.i("PuzzleViewGroup","isSelected");
                    target1 = getTarget(x, y);
                    if (target1 != null && target != target1 && target != null) {
                        setDrawableFitter(target1);
                        startPuzzle();
                    }
                    if (target == target1) {
                        clearDrawableFitter(target1);
                    }
                    isSelected = !isSelected;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private int lastMove[]=new int[2];
    private void startPuzzle() {

        int location1[] = this.getLocation(target);
        int location2[] = this.getLocation(target1);
        int locationParent[] = this.getLocation(this);
        int left1 = location1[0] - locationParent[0];
        int top1 = location1[1] - locationParent[1];
        int left2 = location2[0] - locationParent[0];
        int top2 = location2[1] - locationParent[1];
        Log.i(TAG, "left2:" + left2);
        Log.i(TAG, "left1:" + left1);
        Log.i(TAG, "top1:" + top1);
        Log.i(TAG, "top2:" + top2);


        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(target, PropertyValuesHolder.ofFloat("translationX", lastMove[0], left2 - left1), PropertyValuesHolder.ofFloat("translationY", 0, top2 - top1));
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(target1, PropertyValuesHolder.ofFloat("translationX", lastMove[1], left1 - left2), PropertyValuesHolder.ofFloat("translationY", 0, top1 - top2));
        lastMove[0]=left2-left1;
        lastMove[1]=left1-left2;
//        objectAnimator1.set
        animatorSet.play(objectAnimator1).with(objectAnimator2);
        animatorSet.setDuration(500).setInterpolator(new LinearInterpolator());
        animatorSet.start();
        animatorSet.addListener(this);
//      objectAnimator1.setDuration(500).start();

    }

    private void startAnimator() {
        targetObjectAnimator.setDuration(500);
        target2ObjectAnimator.setDuration(500);

        targetObjectAnimator.start();
        target2ObjectAnimator.start();
    }


    private int[] getLocation(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private boolean isBelongRound(View view, int x, int y) {

        int[] location = this.getLocation(view);
        int[] parent = this.getLocation(this);
        int left = location[0] - parent[0];
        int right = left + view.getWidth();
        int top = location[1];
        int bottom = top + view.getHeight();


        if (left <= x && right >= x && top <= y && bottom >= y) {
            return true;
        }

        return false;
    }

    private void clearDrawableFitter(View target) {
        if (target instanceof ImageView) {
            ImageView imageView = (ImageView) target;
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                drawable.clearColorFilter();
                imageView.setImageDrawable(drawable);
            }
        }
    }

    private void setDrawableFitter(View target) {
        if (target instanceof ImageView) {
            ImageView imageView = (ImageView) target;
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {

                drawable.setColorFilter(Color.parseColor("#333333"), PorterDuff.Mode.ADD);
                imageView.setImageDrawable(drawable);
            }
        }

    }

    public View getTarget(int downX, int downY) {  //通过两点坐标找到相应的View

        View target = null;

        int[] parentLocation = new int[2];
        this.getLocationInWindow(parentLocation);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            View child = (View) iterator.next();
            int[] childLocation = new int[2];
            child.getLocationInWindow(childLocation);
            int left = childLocation[0] - parentLocation[0];
            int right = left + child.getWidth();
            int top = childLocation[1] - parentLocation[1];
            int bottom = top + child.getHeight();
            if (left <= downX && right >= downX && top <= downY && bottom >= downY) {
                target = child;
            }
        }
        return target;

    }

    private void getChildrens() {
        int count = this.getChildCount();
        list.clear();
        while (true) {
            ImageView image = (ImageView) this.getChildAt(RANDOM.nextInt(count));
            if (list.indexOf(image) == -1) {
                list.add(image);

            }
            if (list.size() >= count)  //如果list的siz等于View的个数，说明已经遍历完毕
            {
                break;
            }
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = this.getChildCount();

        if (count < 1)
            return;
        int measureWidth = this.getMeasuredWidth();
        int measureHeight = this.getMeasuredHeight();


        if (list.isEmpty()) {
            getChildrens();
        }

        //开始布局
        int size = list.size();
        int lineWidth = 0;
        int lineHeight = 0;
        for (int i = 0; i < size; i++) {
            ImageView imageView = list.get(i);

            imageView.layout(lineWidth, lineHeight, lineWidth + imageView.getMeasuredWidth(), lineHeight + imageView.getMeasuredHeight());
            lineWidth = imageView.getMeasuredWidth() + lineWidth + 1;
            if (lineWidth > measureWidth) {
                lineHeight += imageView.getMeasuredHeight();
                lineWidth = 0;
            }
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;

        init(bitmap);
    }

    private void init(final Bitmap bitmap) {
        if (bitmap == null)
            return;
        if (TIMES == 1)//如果为第一局
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap[] bitmaps = BitmapUtils.divisionBitmap(bitmap, TIMES * 4);
                    int length = bitmaps.length;
                    for (int i = 0; i < length; i++) {
                        final Bitmap bitmap = bitmaps[i];
                        final ImageView image = new ImageView(PuzzleViewGroup.this.getContext());
                        image.setTag(i);
                        PuzzleViewGroup.this.post(new Runnable() {
                            @Override
                            public void run() {
                                PuzzleViewGroup.this.addView(image);
                                image.setImageBitmap(bitmap);
                                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                image.measure(MeasureSpec.makeMeasureSpec(DEFWH / 2, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(DEFWH / 2, MeasureSpec.EXACTLY));
                                PuzzleViewGroup.this.requestLayout();
                            }
                        });
                    }
                }
            }).start();


        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        clearDrawableFitter(target);
        clearDrawableFitter(target1);
        this.requestLayout();
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }
}
