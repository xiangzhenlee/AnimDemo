package com.yushan.animdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by beiyong on 2017-4-26.
 */

public class DragLayout extends FrameLayout {
    private View redView;//红孩儿
    private View blueView;//蓝精灵
    private View yellowView;//小黄人
    private ViewDragHelper viewDragHelper;
    private Scroller scroller;

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public DragLayout(Context context) {
        super(context);
        init();
    }

    private void init(){
        scroller = new Scroller(getContext());
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    /**
     * 当加载完布局xml的时候会执行该方法，所以执行该方法 的时候就能够知道当前的ViewGroup
     * 有多少个子View,但是此时并不知道子View的宽高是多少，因为还没有测量
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        redView = getChildAt(0);
        blueView = getChildAt(1);
        yellowView = getChildAt(2);
    }

    /**
     * 测量自己和子控件的宽高
     * MeasureSpec: 测量规则，由size和mode组成
     * size：表示的是具体的大小值
     * mode:测量模式      封装的是我们在布局xml中的宽高参数
     *
     * MeasureSpec.AT_MOST: 对应的是wrap_content；
     * MeasureSpec.EXACTLY: 对应的是具体的dp值，match_parent;
     * MeasureSpec.UNSPECIFIED: 未定义的，一般只在adapter的测量中用到
     */
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		//构建测量规则
//		//测量红孩子
//		int measureSpec = MeasureSpec.makeMeasureSpec(redView.getLayoutParams().width,MeasureSpec.EXACTLY);
////		redView.measure(measureSpec, measureSpec);
////		//测量蓝精灵
////		blueView.measure(measureSpec, measureSpec);
//
//		//更加简单的测量子View的方法是这样的：
//		measureChild(redView, widthMeasureSpec, heightMeasureSpec);
//		measureChild(blueView, widthMeasureSpec, heightMeasureSpec);
//	}
//
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;

        redView.layout(left,top,left+redView.getMeasuredWidth(), top+redView.getMeasuredHeight());

        blueView.layout(left,redView.getBottom(),left+blueView.getMeasuredWidth(), redView.getBottom()+blueView.getMeasuredHeight());

        yellowView.layout(left,blueView.getBottom(),left+yellowView.getMeasuredWidth(), blueView.getBottom()+yellowView.getMeasuredHeight());

        //将某个子View提到最上面
//		bringChildToFront(redView);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 让viewDragHelper帮助我们判断是否应该拦截
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将TouchEvent传递给viewDragHelper来处理
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        /**
         * 是否捕获view的触摸
         * child: 表示当前所触摸的子VIew
         * return: true:会捕获      false：不会捕获，即忽略
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child==blueView || child==redView || child==yellowView;
        }
        /**
         * 当View被捕获的时候会回调
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
//			Log.e("tag", "onViewCaptured");
        }
        /**
         * 获取view水平方向拖拽范围，但是目前并不起作用，但是最好还要实现下，不要
         * 返回0，它目前返回的值会用在计算view释放移动的动画时间计算上面
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return DragLayout.this.getMeasuredWidth()-blueView.getMeasuredWidth();
        }
        /**
         * 控制child在水平方向的移动
         * child:当前所触摸的子View
         * left:表示ViewDragHelper帮你计算好的child的最终要变成的left值, left=child.getLeft()+dx
         * dx:表示本次水平移动的距离
         * return: 表示我们真正想让child的left变成的值
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if(left<0){
                left = 0;
            }
            return left;
        }
        /**
         * 控制child在垂直方向的移动
         * child:当前所触摸的子View
         * top:表示ViewDragHelper帮你计算好的child的最终要变成的top值, top=child.getTop()+dy
         * dy:表示本次垂直移动的距离
         * return: 表示我们真正想让child的top变成的值
         */
        public int clampViewPositionVertical(View child, int top, int dy) {
            if(top<0){
                top = 0;
            }
            return top;
        }

        /**
         * 当view位置改变的回调,一般用来实现view的伴随移动
         * changedView:表示当前位置改变了的view
         * left:changedView的最新的left
         * top：changedView的最新的top
         * dx:changedView本次水平移动距离
         * dy:changedView本次垂直移动距离
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if(changedView==blueView){
                //让redView跟随移动
                redView.layout(redView.getLeft()+dx,redView.getTop()+dy, redView.getRight()+dx, redView.getBottom()+dy);
            }else if (changedView==redView) {
                //让blueView跟随移动
                blueView.layout(blueView.getLeft()+dx,blueView.getTop()+dy, blueView.getRight()+dx, blueView.getBottom()+dy);
            } else if (changedView==yellowView){
                //让redView跟随移动
                redView.layout(redView.getLeft()+dx,redView.getTop()+dy, redView.getRight()+dx, redView.getBottom()+dy);
                //让blueView跟随移动
                blueView.layout(blueView.getLeft()+dx,blueView.getTop()+dy, blueView.getRight()+dx, blueView.getBottom()+dy);
            }

            //1.计算移动的百分比
            int maxLeft = DragLayout.this.getMeasuredWidth()-blueView.getMeasuredWidth();
            float fraction = changedView.getLeft()*1f/maxLeft;
            //2.根据移动的百分比执行很多的伴随动画
            executeAnim(fraction);
        }
        /**
         * 当View释放的时候执行，就是touch_up
         * releasedChild：当前抬起的子VIew
         * xvel：x方向移动的速度
         * yvel：y方向移动的速度
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
//			Log.e("tag", "xvel:"+xvel  +  "  yvel: "+yvel);
            //首先算出在正中间的left
            int centerLeft = getMeasuredWidth()/2-releasedChild.getMeasuredWidth()/2;
            if(releasedChild.getLeft()<centerLeft){
                //说明在左半边
                viewDragHelper.smoothSlideViewTo(releasedChild,0,releasedChild.getTop());
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);

//				scroller.startScroll(startX, startY, dx, dy, duration);
//				invalidate();
            }else {
                //说明在右半边
                int finalLeft = getMeasuredWidth()-releasedChild.getMeasuredWidth();
                viewDragHelper.smoothSlideViewTo(releasedChild,finalLeft,releasedChild.getTop());
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);
            }
        }
    };

    /**
     * 执行动画
     * @param fraction
     */
    private void executeAnim(float fraction){
        //旋转
//        blueView.setRotation(360*fraction);//设置旋转的角度
//		blueView.setRotationX(360*fraction);//设置围绕x轴旋转的角度
		blueView.setRotationY(360*fraction);//设置围绕Y轴旋转的角度

        //使用NineOldAndroid中的方法
		ViewHelper.setRotation(redView, 360*fraction);//设置旋转的角度
//        ViewHelper.setScaleX(redView, 1+fraction*0.5f);
//        ViewHelper.setScaleY(redView, 1+fraction*0.5f);

        //进行颜色的过度变化
        redView.setBackgroundColor((Integer) ColorUtil.evaluateColor(fraction, Color.RED,Color.GREEN));

    }

    @Override
    public void computeScroll() {
        //scroller的写法
//		if(scroller.computeScrollOffset()){
//			scrollTo(scroller.getCurrX(),scroller.getCurrY());
//			invalidate();
//		}
        if(viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(DragLayout.this);
        }
    }

}
