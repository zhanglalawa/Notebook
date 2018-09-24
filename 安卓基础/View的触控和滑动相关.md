# TouchSlop（slop：vt.使溅出，溢出）
- **功能**：TouchSlop是系统所能识别出的被认为是滑动的最小距离，是一个常量。即当手指在屏幕上滑动时，如果两次滑动之间的距离小于这个常量，那么系统就不认为是在进行滑动操作。
- **获取方法**：ViewConfiguration.get(getContext()).getScaledTouchSlop()。该常量和设备有关，可用它来判断用户的滑动是否达到阈值。
这个常量有什么意义呢？比如我们处理滑动时候，可以利用这个常量来做一些过滤，滑动的距离小于它，那就认为不是华东。
# VelocityTracker
- **功能**：速度追踪，用于追踪手指在滑动过程中的速度，包括水平和竖直方向的速度。
- **使用过程**：
    - 1.在View的onTouchEvent中追踪当前单击事件的速度：
    
    ```Java
        VelocityTracker velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
    ```
    首先获取了速度追踪对象，我们直译过来速度追踪器，然后把点击事件添加到了这个追踪器里面。
    - 2.获取当前速度：
    
    ```Java
        velocityTracker.computeCurrentVelocity(1000);
        int xVelocity = (int) velocityTracker.getXVelocity();
        int yVelocity = (int) velocityTracker.getYVelocity();
    ```
    逻辑是这样的，先调用**computeCurrent**来计算速度，这是必须的，然后才能通过get方法来获取x和y方向的速度。computeCurrent方法的参数表示的是一个时间间隔，单位是毫秒。计算结果就是在这个时间间隔内手指在x或者y方向上滑动过的像素数。这里我们设置的1s。
    - 3.最后clear和recycle方法回收内存：
    ```Java
        velocityTracker.clear();
        velocityTracker.recycle();
    ```
# GestureDetector
- **功能**：手势检测，用于辅助检测用户的单击、滑动、长按和双击等行为。（如果只是监听滑动操作，建议在onTouchEvent中实现；如果要监听双击这种行为，则使用GestureDetector 。）
- **使用过程**：
    - 1.创建一个GestureDetector并实现OnGestureListener接口，根据需要还可以实现OnDoubleTapListener从而能够监听双击行为：
    ```Java
        GestureDetector mGestureDetector = new GestureDetector(this);
        //解决长按屏幕后无法拖动的现象
        mGestureDetector.setIsLongpressEnabled(false);
    ```
    - 2.接管目标View的onTouchEvent方法，在带监听View的onTouchEvent方法中添加：
    ```Java
        boolean consume = mGestureDetector.onTouchEvent(event);
        retturn consume;
    ```
    - 3.接下来就可以有选择的实现OnGestureListener和OnDoubleTapListener中的方法了，[GestureDetector详解](https://www.jianshu.com/p/95b3fdc46b0f)
# View的滑动
实现View的滑动有三种方法：
- ### 1.使用scrollTo/scrollBy
    
    这是两个方法，实现如下：
    
    ```Java
    /**
     * Set the scrolled position of your view. This will cause a call to
     * {@link #onScrollChanged(int, int, int, int)} and the view will be
     * invalidated.
     * @param x the x position to scroll to
     * @param y the y position to scroll to
     */
    public void scrollTo(int x, int y) {
        if (mScrollX != x || mScrollY != y) {
            int oldX = mScrollX;
            int oldY = mScrollY;
            mScrollX = x;
            mScrollY = y;
            invalidateParentCaches();
            onScrollChanged(mScrollX, mScrollY, oldX, oldY);
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        }
    }
    
    /**
     * Move the scrolled position of your view. This will cause a call to
     * {@link #onScrollChanged(int, int, int, int)} and the view will be
     * invalidated.
     * @param x the amount of pixels to scroll by horizontally
     * @param y the amount of pixels to scroll by vertically
     */
    public void scrollBy(int x, int y) {
        scrollTo(mScrollX + x, mScrollY + y);
    }
    ```
    注释里面对于方法的描述十分之详尽，两个区别是to是绝对，by是相对。我们需要回忆一下英语中to和by两个介词的区别了hhhhh......然后最关键的需要注意的是！View的内容被挪过去了，可是View本身依然在原地。View在布局中的位置并没有发生改变，而且如果内容的滑动已经出了View的边界，划出去的部分是看不到的，他不会滑进附近View的区域。
    
    我们需要解释一下mScrollX和mScorllY：
    - **mScrollX**：View的左边缘减去View的内容的左边缘，可以通过getScrollX方法获得；
    - **mScrollY**：View的上边缘减去View的内容的上边缘，可以通过getScrollY方法获得。
    
    注意正负值：
    ![示意图，还是盗用小姐姐的图。。](https://upload-images.jianshu.io/upload_images/5494434-af3255407d7f7aa8?imageMogr2/auto-orient/strip%7CimageView2/2/w/700/format/webp)
    单位都是像素。
- ### 2.使用动画：这太难了后面学了动画之后回来补充..
- ### 3.改变布局参数
    即改变**LayoutParams**，比如我们要把一个Button向右平移100px，只需要在LayoutParams中的marginLeft参数增加100px即可。或者可以在Button左边设置一个空View，当需要向右平移Button时候，只需要给空View的宽度增加100px就可以了，Button会自动被向右挤，也实现了平移。这是一个例子：
    ```Java
        MarginLayoutParams params = (MarginLayoutParams) mButton1.getLayoutParams()l
        params.width += 100;
        params.leftMargin += 100;
        mButton1.requestLayout();//或者使用mButton1.setLayoutParmas(params)也可以
    ```
- ### 4.三种方式优缺点：
    -   scrollTo/scrollBy：操作简单，适合对view内容滑动。非平滑
    -   动画：操作简单，主要适用于没有交互的view和实现复杂的动画效果
    -   改变LayoutParams：操作稍微复杂，适用于有交互的view。非平滑
# View的弹性滑动
上面我们将对scrollTo和LayoutParams都是强行生硬的滑动，与其说是滑动，不如说是直接搬过去。所以我们需要实现慢慢的滑动。实现的方法很多，有一个共同的思想：**讲一次大的滑动分成若干个小的滑动并在一个时间段内完成**。

这里介绍三种方法：

- ### 1.使用Scroller
    - Scroller使用简介
        
    Scorller要和View的computeScroll方法配合使用才能共同完成这个功能，它的典型的使用代码是固定的：
    ```Java
        Scroller mScroller = new Scroller(mContext);
        
        //缓慢移动到指定位置
        private void smoothScrollTo(int destX, int destY){
            int scrollX = getScrollX();
            int delta = destX - scrollX;
            //1000ms内滑向destX,慢慢滑动
            mScroller.startScroll(scrollX, 0, delta, 0, 1000);
            invalidate();
        }
        
        @Override
        public void computeScroll(){
            if(mScroller.computeScrollOffset()){
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                postInvalidate();
            }
        }
    ```
    startScorll究竟做了什么？就是保存了我们传递的几个参数，源码如下：
    ```JAVA
    /**
     * Start scrolling by providing a starting point, the distance to travel,
     * and the duration of the scroll.
     * 
     * @param startX Starting horizontal scroll offset in pixels. Positive
     *        numbers will scroll the content to the left.
     * @param startY Starting vertical scroll offset in pixels. Positive numbers
     *        will scroll the content up.
     * @param dx Horizontal distance to travel. Positive numbers will scroll the
     *        content to the left.
     * @param dy Vertical distance to travel. Positive numbers will scroll the
     *        content up.
     * @param duration Duration of the scroll in milliseconds.
     */
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        mMode = SCROLL_MODE;
        mFinished = false;
        mDuration = duration;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mStartX = startX;
        mStartY = startY;
        mFinalX = startX + dx;
        mFinalY = startY + dy;
        mDeltaX = dx;
        mDeltaY = dy;
        mDurationReciprocal = 1.0f / (float) mDuration;
    }
    ```
    这好像并没有做任何事情。的确是这样的，那到底是怎么实现弹性滑动的呢？我们不是还提到computeScroll方法了吗。请注意在调用完startScroll之后下面一行的**invalidate()**方法：
    ```Java
    /**
     * Invalidate the whole view. If the view is visible,
     * {@link #onDraw(android.graphics.Canvas)} will be called at some point in
     * the future.
     * <p>
     * This must be called from a UI thread. To call from a non-UI thread, call
     * {@link #postInvalidate()}.
     */
    public void invalidate() {
        invalidate(true);
    }
    ```
    再底层先不看了，注释说的很清楚，这个方法会导致View去调用onDraw重新绘制，而View的draw就会去调用computeScroll方法，这个方法再View中是一个空实现：
    ```Java
    /**
     * Called by a parent to request that a child update its values for mScrollX
     * and mScrollY if necessary. This will typically be done if the child is
     * animating a scroll using a {@link android.widget.Scroller Scroller}
     * object.
     */
    public void computeScroll() {
    }
    ```
    需要我们自己像上面那样去实现它。这个方法才是实现弹性滑动的关键，
    >  - 具体过程：在MotionEvent.ACTION_UP事件触发时调用startScroll方法->马上调用invalidate/postInvalidate方法->会请求View重绘，导致View.draw方法被执行->会调用View.computeScroll方法，此方法是空实现，需要自己处理逻辑。
    
    >  - 具体逻辑是：先判断computeScrollOffset，若为true（表示滚动未结束），则执行scrollTo方法，它会再次调用postInvalidate（相当于非UI线程的invalidate），如此反复执行，直到返回值为false，整个滑动结束。
    
    computeScrollOffset源码：
    ```Java
     /**
     * Call this when you want to know the new location.  If it returns true,
     * the animation is not yet finished.
     */ 
    public boolean computeScrollOffset() {
        if (mFinished) {
            return false;
        }

        int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
    
        if (timePassed < mDuration) {
            switch (mMode) {
            case SCROLL_MODE:
                final float x = mInterpolator.getInterpolation(timePassed * mDurationReciprocal);
                mCurrX = mStartX + Math.round(x * mDeltaX);
                mCurrY = mStartY + Math.round(x * mDeltaY);
                break;
        ...
        
    ```
    他会根据时间的流逝来计算出当前的scrollX和scrollY，返回true表示动画还未结束，返回false则表示滑动结束了。
    
    整体的过程可以借用小姐姐的这个流程图来说明问题：
    ![image](https://upload-images.jianshu.io/upload_images/5494434-049e25c8dd5caa0f?imageMogr2/auto-orient/strip%7CimageView2/2/w/1000/format/webp)
- ### 2.通过动画（还是等学了动画回头再搞）
- ### 3.使用延时策略
    核心思想是通过发送一些列演示消息达到一种渐进式的效果。可以使用Handler或者View的postDelayed方法，也可以使用线程的sleep()方法。对于postDelayed来说，可以通过它来延时发送一个消息，然后在消息中进行View的滑动，如果连续发送这种消息，就能实现渐进式滑动。对于sleep来说，可以通过在while循环中不断滑动View和sleep，就可以实现弹性滑动了。
    
    下面我们用Handler举例,这是大约在1000ms内把View的内容往右边移动了100像素（用的是scrollTo，所以View还在原地）：
    ```Java
    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;

    private int mCount;

    @SuppressWarnings("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_SCROLL_TO:
                    mCount++;
                    if (mCount <= FRAME_COUNT){
                        float fraction = mCount/(float) FRAME_COUNT;
                        int scrollX = -(int)(fraction * 100);
                        toggleButton.scrollTo(scrollX,0);
                        handler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO,DELAYED_TIME);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    ```