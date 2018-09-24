# 1.1View的绘制流程
## 1.1.1 View和ViewGroup
> 什么是View？

我觉的在AS里面截一张图就可以说明问题了，我们找到源码中的View，然后在View上按Cirl+Alt+左击，就可以看到这个
![image](https://upload-images.jianshu.io/upload_images/13852523-4fcd23fe4ada5675.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
没错，就是我们平时用的那些Button,TextView等等，即所谓的控件。它是用户界面基本的构成快，占据一个矩形区域，**View是所有控件的基类**。

ViewGroup继承自View,本质上也是一个View,但是从名字也能看出来，它可以包含其他的View,比如我们常用的各种Layout都是属于ViewGroup的。

## 1.1.2 View的绘制流程
我们不光要会用View，还要清楚View是怎么展示在页面上的，即它的绘制流程。我们从onCreate方法中的setContentView方法入手，debug模式下跟踪到源码如下：

```Java
@Override
    public void setContentView(int resId) {
        ensureSubDecor();
        
        //找到ID为content的ViewGroup，它是一个Activity默认有的布局
        ViewGroup contentParent = (ViewGroup)
        mSubDecor.findViewById(android.R.id.content);
        
        //清空contentParent内部的所有子布局
        contentParent.removeAllViews();
        
        //解析resId对应的布局文件，将其嵌入contentParent内部
        LayoutInflater.from(mContext).inflate(resId, contentParent);
        mOriginalWindowCallback.onContentChanged();
    }

```

### 解读：
ID为content的这个ViewGroup每个Activity都默认有，也是函数名是setContentView的由来。解析布局是用LayoutInflate的inflate方法，这个方法底层实现大致使用递归遍历这个resId布局中的所有控件，调用createViewFromTag方法创建控件对应的对象。


那么我们看到这个方法不过是根据布局文件去创建了对应的View对象，并不涉及到最最底层是如何绘制View。布局文件xml只是个资源配置文件，并不涉及到View的绘制。

而View的绘制实际上有三个过程，也分别对应View类中的三个API：

- measure
- layout
- draw

### (1)measure测量
首先我们需要知道控件的大小，就需要进行测量，这里涉及到measure方法，实际上是measure中调用了onMeasure方法完成的测量工作,**onMeasure(int widthMeasureSpec, int heightMeasureSpec)**，这里两个参数默认传递0，当有布局xml时候，这两个参数会与xml中设置的width和height关联，我们看一个例子：

首先自定义一个View，第一个构造方法通常在代码中创建对象时候使用，第二个必须要有，**解析布局文件时候，就是调用带有两个参数的构造方法完成对View对象的创建**，我们也在里面补上onMeasure方法，不过此时并没有重写，还是默认的调用超类的onMeasure：

```Java
public class MyView extends View {
    public MyView(Context context) {
        this(context, null);
    }
    
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
```
在MainActivity布局中我们设置MyView的宽高,都是200dp：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.example.myapplication.Views.MyView
        android:layout_width="200dp"
        android:layout_height="200dp" 
        android:background="@android:color/black"/>

</LinearLayout>

```
运行之后结果是看到了200dp的黑框。实际上是super的onMeasure方法中内部调用**setMeasuredDimension**设置了宽高，宽高的值就是onMesure方法的两个参数从布局里面拿来的。所以我们在布局中设置了200dp之后，自然最后也是200dp了。

这是super的onMeasure方法：

```java
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }
```

setMeasuredDimension方法：

```java
protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        boolean optical = isLayoutModeOptical(this);
        if (optical != isLayoutModeOptical(mParent)) {
            Insets insets = getOpticalInsets();
            int opticalWidth  = insets.left + insets.right;
            int opticalHeight = insets.top  + insets.bottom;

            measuredWidth  += optical ? opticalWidth  : -opticalWidth;
            measuredHeight += optical ? opticalHeight : -opticalHeight;
        }
        setMeasuredDimensionRaw(measuredWidth, measuredHeight);
    }
```


当然我们可以重写onMeasure方法如下,我们注释掉了超类的onMeasure的调用，直接自己调用setMeasureDimension，这里的单位是px，50px：

```Java
@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(50,50);
    }
```
运行结果也符合我们的预料，显示了很小的50px*50px的黑框。

### 可见：
1. widthMeasureSpec和heightMeasureSpec的值在默认情况下是和xml中的配置相关联的，这过程中，肯定也涉及了dp和px的转换；
2. 我们也可以通过自己直接setMeasureDimension来自己测量大小，参数单位是px。

那么，widthMeasureSpec和heightMeasureSpec的值是怎么关联上xml中的值的？最早实际上我们说是最外层的measure方法传来的值，它们是父容器（这里就是外层这个LinearLayout)对MyView的宽高的限制。

它们都是32位的二进制，高2位表示SpecMode——测量模式，低30位表示SpecSize——在某种测量模式中的大小值，模式在MesureSpec类中定义，源码中是这样的：

```Java
        private static final int MODE_SHIFT = 30;
        /**
         * Measure specification mode: The parent has not imposed any constraint
         * on the child. It can be whatever size it wants.
         */
        public static final int UNSPECIFIED = 0 << MODE_SHIFT;

        /**
         * Measure specification mode: The parent has determined an exact size
         * for the child. The child is going to be given those bounds regardless
         * of how big it wants to be.
         */
        public static final int EXACTLY     = 1 << MODE_SHIFT;

        /**
         * Measure specification mode: The child can be as large as it wants up
         * to the specified size.
         */
        public static final int AT_MOST     = 2 << MODE_SHIFT;
```
这三个值对应的三个不同的模式，在源码注释中写的也很清楚了。
我们可以在代码中拆分出模式和大小，在onMeasure中用Log.d打印一下：

```
@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(100,100);
        int mode = MeasureSpec.getMode(widthMeasureSpec);//获得宽度的模式
        int size = MeasureSpec.getSize(widthMeasureSpec);//获得宽度的大小

        Log.d("MyView","onMeasure: "+(mode>>30)+" "+size);
    }

```
logcat打印结果为： **onMeasure: 1 600**

可见这里是exactly模式，然后size为600px，那么我们其实可以推测出，我这台机器1dp=3px，分辨率是xxxhdpi了。

### 总结：
整个结果也就说明MyView的父容器对MyView的大小有确切的要求，onMesure方法得到了要求的数值，进行相应的大小的设置。

那么测量的过程就是这样的：MyView在布局文件中配置layout_width和layout_height的数值，父容器LinearLayout通过获取MyView的LayoutParams对象从而获取这两个值，然后根据实际情况（如屏幕的分辨率啊等等转换成Px）构建出两个参数(int widthMeasureSpec, int heightMeasureSpec)，接着父容器的调用MyView的measure方法传入这两个参数，measure方法再调用onMeasure方法，一般会默认根据父容器的限制来设置宽高，当然也不排除上面的例子中直接调用setMeasureDimension方法。

### （2）layout布局
知道了大小之后，我们还必须清楚这个View的位置在什么地方，布局对应的API为View类中的layout方法，**layout(int l, int t, int r, int b)**,这四个参数依次对应于父容器的left,top,right,bottom坐标，这个坐标是相对于当前View的父容器来说的，坐标系中x轴和y轴正方向分别为右和下。

网上找一张图片：
![image](https://upload-images.jianshu.io/upload_images/5494434-613b848a886ecd4e?imageMogr2/auto-orient/strip%7CimageView2/2/w/437/format/webp)

其中可以有View的几个方法获得相关的值：
- left = getLeft();
- right = getRight();
- top = getTop();
- bottom = getBottom();
- width=getWidth();
- height=getHeight();

这个layout方法自然也是父容器调用的，用来指定相对于自己上下左右的位置。

### （3）draw绘制
知道了大小和布局之后，就是要进行最终的绘制环节了。这里对应的是draw(Canvas canvas)方法,draw里面会调用onDraw方法。onDraw在View里面是空方法，需要子类自己实现，draw方法一样，也是由父容器调用的。
## ViewGroup的绘制流程
ViewGroup自然也遵循View的绘制流程，但是一般情况下ViewGroup作为一个Group它肯定要承担更多的职责，在View绘制的基础上还要做很多事情。View中我们讲它的所有三个方法都是父容器调用起来的，那么ViewGroup本身作为别人的父容器，也是要考虑中如何测量、布局以及绘制它里面的子View。

### （1）ViewGroup的测量
ViewGroup除了遵循View的测量机制要测量自己之外，还要发起子View的测量，测量子View在onMeasure方法中执行，一般的过程是遍历ViewGroup中的所有子View,获取子View在布局中的参数LayoutParams的宽高，然后指定一个模式，将宽高和模式构建成对子View宽高的限制，也就是那两个参数widthMeasureSpec和heightMeasureSpec，然后调用子View的measure方法传入限制完成对子View的测量。我们可以在很多ViewGroup源码的onMeasure方法里面发现这些代码，如LinearLayout或RelativeLayout。我们也可以直接使用ViewGroup封装好的**measureChildren()**方法，完成对子View的测量：

```java
public class MyViewGroup extends ViewGroup {
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);//ViewGroup封装好的方法
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
```
这是源码中的measureChildren:

```java
protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        final int size = mChildrenCount;
        final View[] children = mChildren;
        for (int i = 0; i < size; ++i) {
            final View child = children[i];
            if ((child.mViewFlags & VISIBILITY_MASK) != GONE) {//子View可见才去测量
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }
```
又调用了measureChild：

```java
protected void measureChild(View child, int parentWidthMeasureSpec,
            int parentHeightMeasureSpec) {
        //获得参数
        final LayoutParams lp = child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom, lp.height);

        //child的measure调用之后就和View上一节讲的一样了
        //去调用onMeasure，如果是Group的话再去像上面那样一样
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
```
### （2）ViewGroup的布局

ViewGroup的布局过程遵循View的布局过程，同理作为一个Group还是要去布局子View。ViewGroup同样要先被调用layout方法完成自己的布局，并且在layout中会调用onLayout方法。layout方法如果是一个ViewGroup，那么它必须实现onLayout方法，在onLayout方法中去调用它的子View的layout方法去布局子View。在ViewGroup类中，onLayout是一个抽象方法。

这是RelativeLayout中onLayout的源码:

```java
@Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //  The layout has actually already been performed and the positions
        //  cached.  Apply the cached values to the children.
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                RelativeLayout.LayoutParams st =
                        (RelativeLayout.LayoutParams) child.getLayoutParams();
                child.layout(st.mLeft, st.mTop, st.mRight, st.mBottom);
            }
        }
    }
```
相对布局作为一个Group，它指定了子View相对于自己上下左右的位置，调用子View的layout方法。我们自己写onLayout方法的话，就要涉及到对于这些坐标的计算，一个例子如下：

```java
@Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int right = left + mChild.getMeasuredWidth();   //获得测量后的宽度
        int bottom = top + mChild.getMeasuredHeight();  //获得测量后的高度
        mChild.layout(left,top,right,bottom);
    }
```
### （3）ViewGroup的绘制
ViewGroup的绘制流程与View有一些区别，View是在调用draw(Canvas canvas)方法中，第三步onDraw方法绘制View的内容，第四步是调用dispatchDraw方法绘制子View。如果一个View没有子View，也就是不是ViewGroup，就不用调用dispatchDraw，如果是Group那就要调用了。ViewGroup默认实现的dispatchDraw中是遍历了所有子View，调用了drawChild方法去绘制子View：

```java
protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return child.draw(canvas, this, drawingTime);
    }
```
这个draw是三个参数，和我们之前那个draw(Canvas canvas)是不一样的。源码中有一段这样的逻辑：

```java
// Fast path for layouts with no backgrounds
            if ((mPrivateFlags & PFLAG_SKIP_DRAW) == PFLAG_SKIP_DRAW) {
                //如果自己本身是一个ViewGroup且没有背景。就会直接去绘制子View
                mPrivateFlags &= ~PFLAG_DIRTY_MASK;
                dispatchDraw(canvas);
            } else {
                //否则才调用自己的draw，经过onDraw绘制自己，再去进行和上面一样步骤
                draw(canvas);
            }
```
即，ViewGroup和View的draw的过程区别在于前者如果没有背景就不会绘制自己，跳过绘制直接dispatchDraw，不满足跳过时候还是用一个参数的draw进行正常流程。