# 1.2View的触摸事件
## 1.2.1触摸事件的类型
触摸事件的类型保存在**MotionEvent**类中，共有0到12，其中0~6是触摸事件，7~12是手机外设操作事件，我们这里自定义一个控件MyView(基本的自定义View以及在xml中引入的方法，在第一行代码中已经介绍)，覆写onTouchEvent方法，打印一下触摸事件的Log:
```Java
public class MyView extends View{
    private static final String TAG = "MyView";
    public MyView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            //0
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"onTouchEvent:ACTION_DOWN");
                break;
            //1
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"onTouchEvent.ACTION_UP");
                break;
            //2
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"onTouchEvent.ACTION_MOVE");
                break;
            //3
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG,"onTouchEvent.ACTION_CANCEL");
                break;
            //4
            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG,"onTouchEvent.ACTION_OUTSIDE");
                break;
            //5
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d(TAG,"onTouchEvent.ACTION_POINTER_DOWN");
                break;
            //6
            case MotionEvent.ACTION_POINTER_UP:
                Log.d(TAG,"onTouchEvent.ACTION_POINTER_UP");
                break;
        }
        return true;
    }
}
```
我们主要关注ACTION_DOWN,ACTION_MOVE和ACTION_UP。

手指按下屏幕瞬间（**ACTION_DOWN**)，离开屏幕瞬间(**ACTION_UP**)，按下和离开之间是**一个事件序列**。中间是一系列移动事件（**ACTION_MOVE**).在真机上模拟的时候记得打开Log。我们在Log中看到的结果也是符合预期的。

其它暂时先放下，比如ACTION_CANCEL是取消当前手势动作。父容器拦截事件时候触发，不是用户行为。又比如后面的两个ACTION_POINTER_DOWN和ACTION_POINTER_UP是多点触摸按下和离开屏幕时候会触发，但是我们却没有看到相应的Log。是因为event.getAction()返回的是一个32位整型，低八位表示事件类型,即0~12高八位表示触控点的索引，多点触摸时候，会给每一个触控点都分配一个索引值，所以第二根手指点上去时候，event.getAction()返回值是0000 0001 0000 0101 那拆分下来，事件类型是5，表示多点触碰了，而索引值是1，最终switch里面得到的是261，所以看不到log。如果想拆分，则调用event.getActionMasked()。
## 1.2.2触摸事件的传递过程
假设有这样一个布局，LinearLayout内部嵌套一个TextView，有某个时刻，手指在TextView上按下，根据上面讲解我们知道会触发ACTION_DOWN事件，那么究竟是LinearLayout还是TextView会先拿到这个事件呢？为了知道这个答案，我们必须分析触摸事件的传递过程。这设计了3个方法：
- **public boolean dispatchTouchEvent(MotionEvent event)**
>   1.功能：分发事件
>
>   2.调用时机：如果事件能够传递给当前View，那么这个方法一定会被调用
>
>   3.返回值的意义：如果返回true，表示时间分发下去之后被处理了；如果返回flase，表示分发下去没有被任何View处理。
- **public boolean onInterceptTouchEvent(MotionEvent event)**
>   1.功能:判断是否拦截某个事件
>
>   2.调用时机：在上面dispatchTouchEvent的内部调用
>
>   3.返回值的意义：如果返回true，表示拦截事件；如果返回false，表示不拦截
>
>   4.注意：①这个方法ViewGroup才有，View没有
>           ②如果当前ViewGroup拦截了事件（即返回了true），那么在同一个事件序列中，该方法不会被再次调用

- **public boolean onTouchEvent(MotionEvent event)**
>   1.功能：处理点击事件
>
>   2.调用时机：在dispatchTouchEvent中调用
>
>   3.返回值的意义：返回结果表示是否处理当前事件
>
>   4.注意：如果不处理事件，（即返回了false）那么在同一个事件序列中，这个View无法再接收到此事件。

这三个方法之间的关系可以用以下伪代码来体现：

```Java
public boolean dispatchTouchEvent(MotionEvent event){
    boolean consume = false;
    if(onInterceptTouchEvent(event)){
        consume = onTouchEvent(event);
    }else{
        consume = child.dispatchTouchEvent(event)l
    }
    return consume;
}
```
解读一下：

1.对于一个根ViewGroup来说，点击事件产生之后，首先会传递它，此时其dispatchTouchEvent被调用；

2.如果这个ViewGroup的onInterceptTouchEvent返回true，说明要拦截事件，那么就会调
用这个ViewGroup的onTouchEvent方法，自己去消费这个事件；而如果返回false，那就把事件交给子View，交付给子view体现于调用子view的dispatchTouchEvent。

3.对于一个非ViewGroup的View来说，是没有onInterceptTouchEvent方法的，它会直接调用自己的onTouchEvent方法。如果这个onTouchEvent方法返回false不处理，那么事件又会传递给父容器的onTouchEvent方法。

直到有一天这个事件被处理。

补充：
> 如果一个View设置了OnTouchListener，那么处理事件时候，onTouch方法会被回调，onTouch返回false，当前View的onTouchEvent才会调用，而如果返回true，那么onTouchEvent不会被调用，在onTouchEvent方法中，如果当前设置有OnClickListener，那么onClick方法会被回调。

可见优先级上OnTouchListener > onTouchEvnet > OnClickListener

## 1.2.3事件传递细节
#### 1.事件传递和Activity的关系
一个点击事件发生后，它传递过程遵循如下顺序：Activity -> Window -> View。所有事件总是先传递给Activity，再给Window，再传递给顶级View,顶级View收到事件之后，按照我们上面所描述的去dispatchTouchEvent。我们也讲了，如果子View没人消费事件，那么会调用父容器的onTouchEvent,那如果大家都很谦让，以至于顶级View的onTouchEvent方法都返回flase呢？那很明显最终会交给Activity的onTouchEvent去处理。

Activity的dispatchTouchEvent方法的具体工作是由phoneWindow来完成的，phoneWindow会将事件传递给decorView，**decorView一般是当前界面的底层容器**，即就是在setContentView中设置的View的父容器，可以通过**Activity.getWindow().getDecorView()**获取，Activity的dispatchTouchEvent如下：

```Java
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onUserInteraction();
        }
        //获取到Activity的PhoneWindow，将时间传递给DecorView，DecorView再将时间分发给子View
        if (getWindow().superDispatchTouchEvent(ev)) {
            //如果有子View消费了就返回true
            return true;
        }
        
        //如果没有消费，那就交给Activity的onTouchEvent方法处理
        return onTouchEvent(ev);
    }
```

所以DecorView解释了，是个最父亲、最底层容器的View，我们想知道Window怎么交给DecorView的，那这个Window，还有提到的PhoneWindow又是什么。我们点到源码里面发现Window是个抽象类，superDispatchTouchEvent也是个抽象方法，不过Window的注释中有这么一段话：

```Java
/**
 * Abstract base class for a top-level window look and behavior policy.  An
 * instance of this class should be used as the top-level view added to the
 * window manager. It provides standard UI policies such as a background, title
 * area, default key processing, etc.
 *
 * <p>The only existing implementation of this abstract class is
 * android.view.PhoneWindow, which you should instantiate when needing a
 * Window.
 */
```
可见Window可以控制顶级View的外观和行为策略，提供标准的UI策略。唯一的实现是**PhoneWindow**，当我们需要一个Window的时候，PhoneWWindow就会实例化。那就顺藤摸瓜进入到PhoneWindow里面，看看superDispatchTouchEvent方法：

```Java
    @Override
    public boolean superDispatchTouchEvent(MotionEvent event) {
        return mDecor.superDispatchTouchEvent(event);
    }
```
只有一行代码，又把事件交给了mDecor的superDispatchTouchEvent方法了。

mDecor不用想都知道是什么。。这是源码中的注释。
```Java
// This is the top-level view of the window, containing the window decor.
    private DecorView mDecor;

```
可以通过Window中的getDecorView获取到它：

```Java
    @Override
    public final View getDecorView() {
        if (mDecor == null || mForceDecorInstall) {
            installDecor();
        }
        return mDecor;
    }
```
再底层就不看了。。太过于底层已经逐渐看不懂了。我们通过setContentView设置的View都是它的一个子View。

那么再来看mDecor的superDispatchTouchEvent方法：

```Java
    public boolean superDispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
```
而从源码中也可以看到public class DecorView extends FrameLayout，DecorView是个FrameLayout,这个超类FrameLayout中并没有重写dispatchTouchEvent，所以实际上是在调用ViewGroup的dispatch，我们将会在下一个部分分析。

这样我们的事件传递从Activity到Window再到DecorView，它是我们用setContentView设置的顶级View的父容器，那么最终事件也肯定会交给顶级View——DecorView。

#### 2.顶级View对点击事件的分发过程
事实上我们在1.2.2中描述的dispatch的过程基本上是清晰的，不过当时我们用的是简单的伪代码来描述，下面我们主要从源码角度分析一下，这是ViewGroup（DecorView是个FrameLayout，那自然就是ViewGroup了）的dispatchTouchEvent方法比较长，逐段摘取分析：

```Java
            // Check for interception.
            final boolean intercepted;
            if (actionMasked == MotionEvent.ACTION_DOWN
                    || mFirstTouchTarget != null) {
                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
                if (!disallowIntercept) {
                    intercepted = onInterceptTouchEvent(ev);
                    ev.setAction(action); // restore action in case it was changed
                } else {
                    intercepted = false;
                }
            } else {
                // There are no touch targets and this action is not an initial down
                // so this view group continues to intercept touches.
                intercepted = true;
            }
```
分析如下：

1.有两种情况ViewGroup会判断是否要拦截当前事件：
> ①事件类型为ACTION_DONW
>
> ②mFirstTouchTarget != null 

第一种情况就是按下屏幕，第二种这个mFirstTouchTarget是个TouchTarget对象，用来保存处理触摸事件的View和触摸点的id值，它什么时候为空？

> 当ACTION_DONW事件分发下去之后被子View成功处理，即onTouchEvent返回了true，那么这个mFirstTouchTarget就会被赋值为这个子View，而如果没有处理，那么mFirstTouchTarget将会是Null。

***总结*：**

那么综上所述，如果事件分发下去之后没有子View处理，那么mFirstTouchTarget必然为null,那么后续的触摸事件必然也不遵循ACTION_DOWN,所以一个事件序列中的后续事件都会不会再去询问onInterceptTouchEvent是否要拦截，并且将intercepted设置为true，ViewGroup将会拦截所有事件自己处理。但也有一种特殊情况，通过调用requestDisallowInterceptTouchEvent可以设置源码中展示的**FLAG_DISALLOW_INTERCEPT**标志位，一旦设置，ViewGroup将无法拦截除了ACTION_DOWN以外的所有事件(因为ACTION_DOWN会重置这个标志位，所以ACTION_DOWN动作之后一定会去访问onInterceptToucnEvent方法)，intercepted被设置为false。重置的源码是这样的：

```Java
            // Handle an initial down.
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                // Throw away all previous state when starting a new touch gesture.
                // The framework may have dropped the up or cancel event for the previous gesture
                // due to an app switch, ANR, or some other state change.
                cancelAndClearTouchTargets(ev);
                resetTouchState();//这个歌方法中会重置FLAG_DISALLOW_INTERCEPT
            }
```
那么我们可以知道以下结论：

> 1.ViewGroup决定拦截事件之后（即intercepted设置为true之后)，同一序列的后续事件都会默认交给它处理而不去调用onInterceptTouchEvent方法；
>
> 2.我们通过requestDisallowInterceptTouchEvent在子元素中干预父元素的事件分发，但是ACTION_DOWN除外；

而ViewGroup决定不拦截事件之后，事件会下发交给子View处理，源码如下：

```Java
                final View[] children = mChildren;
                for (int i = childrenCount - 1; i >= 0; i--) {
                    final int childIndex = getAndVerifyPreorderedIndex(
                            childrenCount, i, customOrder);
                    final View child = getAndVerifyPreorderedView(
                            preorderedList, children, childIndex);
                            ...
                    if (!canViewReceivePointerEvents(child)
                            || !isTransformedTouchPointInView(x, y, child,null)){
                        ev.setTargetAccessibilityFocus(false);
                        continue;
                    }
                            
                    newTouchTarget = getTouchTarget(child);
                    if (newTouchTarget != null) {
                        // Child is already receiving touch within its bounds.
                        // Give it the new pointer in addition to the ones it is handling.
                        newTouchTarget.pointerIdBits |= idBitsToAssign;
                        break;
                    }
                    
                    resetCancelNextUpFlag(child);
                    if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                        // Child wants to receive touch within its bounds.
                        mLastTouchDownTime = ev.getDownTime();
                        if (preorderedList != null) {
                            // childIndex points into presorted list, find original index
                            for (int j = 0; j < childrenCount; j++) {
                                if (children[childIndex] == mChildren[j]) {
                                    mLastTouchDownIndex = j;
                                    break;
                                }
                            }
                        } else {
                            mLastTouchDownIndex = childIndex;
                        }
                        mLastTouchDownX = ev.getX();
                        mLastTouchDownY = ev.getY();
                        newTouchTarget = addTouchTarget(child, idBitsToAssign);
                        alreadyDispatchedToNewTouchTarget = true;
                        break;
                    }
                        ...
                }
```
摘取的这段代码的逻辑是这样的：

**1**.for遍历ViewGroup中的所有子View，

**2**.第一个if是判断这个child是否能够接收点击事件，两个标准，第一是否可见，第二点击事件是否发生在其中；

**3**.如果第一个if满足了，即又可见点击又在其坐标范围中，那么看第三个if中的dispatchTransformedTouchEvent方法，这个方法实际上就是在调用这个child的dispatchTouchEvent方法。里面有这样一段逻辑：

```Java
    if (child == null) {
        handled = super.dispatchTouchEvent(event);
    } else {
        handled = child.dispatchTouchEvent(event);
    }
```
即传过去那个child不是null的话就会去调用。

**4**.如果这个child的dispatchTouchEvent返回了true，那么看后面这几句：

```Java
    newTouchTarget = addTouchTarget(child, idBitsToAssign);
    alreadyDispatchedToNewTouchTarget = true;
    break;
```
这几行代码完成了对mFirstTouchTarget的赋值(在addTouchTarget方法中完成)，并且终止了子View的遍历。而如果这个child的dispatchTouchEvent返回false，那么将会进行下一个循环。

而如果遍历所有ViewGroup子元素后事件都没有被合适处理：要么是ViewGroup没有子元素，要么是子元素处理了点击事件，但是dispatchTouchEvent返回了false，又或者早早就决定拦截事件了，那么最后都会由ViewGroup自己处理：

```Java
    if (mFirstTouchTarget == null) {
        // No touch targets so treat this as an ordinary view.
        handled = dispatchTransformedTouchEvent(ev, canceled, null,
        TouchTarget.ALL_POINTER_IDS);
    }
```
注意上面代码的的第三个参数child是null，从前面的分析，我们知道会调用super.dispatchTouchEvent,就转到了View的dispatchTouchEvent方法。下面我们就来看View。

#### 3.View对点击事件的处理过程
View对点击事件的处理较为简单一些，这里的View不包括ViewGroup，dispatchTouchEvent方法如下：

```Java
public boolean dispatchTouchEvent(MotionEvent event) {
    ...
    boolean result = false;
    ...
    if (onFilterTouchEventForSecurity(event)) {
        //noinspection SimplifiableIfStatement
        ListenerInfo li = mListenerInfo;
        if (li != null && li.mOnTouchListener != null
                && (mViewFlags & ENABLED_MASK) == ENABLED
                && li.mOnTouchListener.onTouch(this, event)) {
            result = true;
        }

        if (!result && onTouchEvent(event)) {
            result = true;
        }
    }
    ...
}
```
View是一个单独的元素，不像ViewGroup一样会向子元素传递事件，所以处理过程比较简单，源码中可以看到

**1**.先会判断有没有设置OnTouchListenr，如果设置了，回调onTouch方法返回true，那么onTouchEvent就不会被调用，这就是我们前面说的OnTouchListener的优先级高于onTouchEvent；

**2**.接着我们看onTouchEvent的实现：

```Java
    ...
    final boolean clickable = ((viewFlags & CLICKABLE) == CLICKABLE
            || (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)
            || (viewFlags & CONTEXT_CLICKABLE) == CONTEXT_CLICKABLE;
            
    if ((viewFlags & ENABLED_MASK) == DISABLED) {
        if (action == MotionEvent.ACTION_UP && (mPrivateFlags & PFLAG_PRESSED) != 0) {
            setPressed(false);
        }
        mPrivateFlags3 &= ~PFLAG3_FINGER_DOWN;
        // A disabled view that is clickable still consumes the touch
        // events, it just doesn't respond to them.
        return clickable;
    }
    ...     
```
注释中也指明了，Disable的View依然会消耗事件，只不过没有响应罢了，只要是clickable就可以了。

如果View设置了代理，还会执行TouchDelegate的onTouchEvent方法，这个机制和OnTouchListener类似：

```Java
    if (mTouchDelegate != null) {
        if (mTouchDelegate.onTouchEvent(event)) {
            return true;
        }
    }
```

对点击事件的具体处理如下：
```Java
        if (clickable || (viewFlags & TOOLTIP) == TOOLTIP) {
            switch (action) {
                case MotionEvent.ACTION_UP:
                    ...
                    boolean prepressed = (mPrivateFlags & PFLAG_PREPRESSED) != 0;
                    if ((mPrivateFlags & PFLAG_PRESSED) != 0 || prepressed) {
                        ...

                        if (!mHasPerformedLongPress && !mIgnoreNextUpEvent) {
                            // This is a tap, so remove the longpress check
                            removeLongPressCallback();

                            // Only perform take click actions if we were in the pressed state
                            if (!focusTaken) {
                                // Use a Runnable and post this rather than calling
                                // performClick directly. This lets other visual state
                                // of the view update before click actions start.
                                if (mPerformClick == null) {
                                    mPerformClick = new PerformClick();
                                }
                                if (!post(mPerformClick)) {
                                    performClick();
                                }
                            }
                        }
                        ...
                    }
                    break;
            }
            ...
            return true;
        }
```
只要View的CLICKABLE和LONG_CLICKABLE有一个是true，那么就会消耗这个事件，即onTouchEvent返回true，不管是不是DISABLE状态。当ACTION_UP触发时候，会调用performClick方法，如果View设置了OnClickListener,其中会调用它的onClick方法。

```Java
    public boolean performClick() {
        final boolean result;
        final ListenerInfo li = mListenerInfo;
        if (li != null && li.mOnClickListener != null) {
            playSoundEffect(SoundEffectConstants.CLICK);
            li.mOnClickListener.onClick(this);
            result = true;
        } else {
            result = false;
        }

        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);

        notifyEnterOrExitForAutoFillIfNeeded(true);

        return result;
    }
```

这也就能解释我们之前说的，onTouchEvent优先级高于onClickListener。

View的LONG_CLICKABLE属性默认为false，CLICKABLE属性是否为false和具体哪种View有关，可点击的Button就是true，而TextView就是false。通过setClickable和setLongClickable可以分别改变View的CLICKABLE和LONG_CLICKABLE属性，而setOnClickable可以自动将View的CLICKABLE设置为true，而setOnLongClickListener则会自动将LONG_CLICKABLE设为true，源码是这样的：

```Java
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (!isClickable()) {
            setClickable(true);
        }
        getListenerInfo().mOnClickListener = l;
    }
    
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        getListenerInfo().mOnLongClickListener = l;
    }
```


可以有以下结论：
> ①View的onTouchEvent默认会消耗事件（返回true），除非其不可点击（clickable和longClickbale同时为false），其中longClickable默认为false，而clickable分情况，比如Button就是true，而TextView是false；
>
> ②View的enable属性不影响onTouchEvent的默认返回值，只要View的CLICKABLE和LONG_CLICKABLE有一个是true，那么就会消耗这个事件；
>
> ③onClick发生的前提是View是可点击的，并且它收到了ACTION_DOWN和ACTION_UP事件。