## 一、概念和机制
适配器模式的使用，LisView的显示和数据源解耦。（这个先挖坑，在学了适配器模式之后会回来补充）

**1.数据源、适配器和ListView之间的关系**
![数据源、适配器和ListView之间的关系](https://upload-images.jianshu.io/upload_images/13852523-0778834bdcb3304f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/934/format/webp)

**2.ListView的缓存和显示机制**：需要时候显示，划出屏幕时候被回收到缓存。
![缓存和显示机制](https://upload-images.jianshu.io/upload_images/13852523-7757d4f11257c6b5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/779/format/webp)

**3.BaseAdapter结构：**
![image](https://upload-images.jianshu.io/upload_images/13852523-4a94fbdd8b1a039e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1000/format/webp)
## 二、基本使用举例
#### 1.ListView布局添加和子项Item的布局配置
首先需要像添加某一个View一样，在布局里面加一个ListView:

```xml
    <ListView
        android:id="@+id/list_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

然后给ListView每一个子项都需要一个布局，我们就做一个这样的布局：

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <ImageView
        android:id="@+id/item_image"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:src="@mipmap/ic_launcher" />

    <TextView
        android:id="@+id/title"
        android:layout_width="match_parent"
        android:layout_height="30dp"
        android:layout_toRightOf="@id/item_image"
        android:gravity="center"
        android:text="title"
        android:textSize="25sp" />

    <TextView
        android:id="@+id/content"
        android:layout_width="match_parent"
        android:layout_height="30dp"
        android:layout_below="@id/title"
        android:layout_toRightOf="@id/item_image"
        android:gravity="center_vertical"
        android:text="content"
        android:textSize="20sp" />
</RelativeLayout>

```

![布局示意图](https://upload-images.jianshu.io/upload_images/13852523-367d65ec13f4d302.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
#### 2.创建子项Bean和数据提供
然后要给子项建一个Bean。我们这里建立一个ItemBean，包括了三部分内容图片，内容和标题：

```Java
public class ItemBean {
    private int ItemImageResourceId;
    private String ItemTitle;
    private String ItemContent;

    public ItemBean(int itemImageResourceId, String itemTitle, String itemContent) {
        ItemImageResourceId = itemImageResourceId;
        ItemTitle = itemTitle;
        ItemContent = itemContent;
    }

    public int getItemImageResourceId() {
        return ItemImageResourceId;
    }

    public String getItemTitle() {
        return ItemTitle;
    }

    public String getItemContent() {
        return ItemContent;
    }
}

```
然后在主活动里面搞一个List，造一些假的数据，这就是数据源：

```Java
    List<ItemBean> list = new ArrayList<>();
    for (int i = 1;i<=20;i++){
        list.add(new ItemBean(R.mipmap.ic_launcher,
                    "title"+i,
                    "content"+i));
        }
```
#### 3.Adapter的配置

```Java
public class TestAdapter extends BaseAdapter {
    private List<ItemBean> mList;
    
    public TestAdapter(List<ItemBean> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ...
}    

```
需要一个List去接收数据源，然后前三个方法都比较水，不需解释。

最后一个getView是最关键的方法，它决定了每一项返回的内容。

***第一步***：需要一个LayoutInflater对象来把子项的XML文件加载成一个View对象。

这个LayoutInflater对象就是个布局装饰器对象，调用它的Inflate()方法，可以把一个XML转化成View。那么这个对象从何而来？他需要context来构建。我们在Adapter里面加以下代码，来构建这个对象：


```Java
public class TestAdapter extends BaseAdapter {
    private List<ItemBean> mList;

    //布局装饰器对象，要用它把一个下面Item的XML转化成我们需要的View
    private LayoutInflater mLayoutInflater;

    public TestAdapter(Context context,List<ItemBean> list) {
        mList = list;
        //通过传来的Context来初始化LayoutInflater对象
        //这个Context要使用当前Adapter的界面对象
        mLayoutInflater = LayoutInflater.from(context);
    }
    ...
}    
```

***第二步***：重写getView方法

不要忘了return这个view

```Java
//返回每一项的显示内容
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item,null);
        ItemBean item = mList.get(position);
        //用这个View我们可以findViewById
        TextView title = view.findViewById(R.id.title);
        TextView content = view.findViewById(R.id.content);
        ImageView itemImage = view.findViewById(R.id.item_image);

        title.setText(item.getItemTitle());
        content.setText(item.getItemContent());
        itemImage.setImageResource(item.getItemImageResourceId());
        return view;
    }
```

***第三步***：通过构造器构造Adapter绑定数据源和Adapter，然后通过ListView的setAdatper()绑定ListView和我们的Adapter

```Java
        List<ItemBean> list = new ArrayList<>();
        for (int i = 1;i<=20;i++){
            list.add(new ItemBean(R.mipmap.ic_launcher,
                    "title"+i,
                    "content"+i));
        }

        TestAdapter adapter = new TestAdapter(this,list);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
```
最后的效果:
![image](https://upload-images.jianshu.io/upload_images/13852523-ffe8ea1b959c9cc9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 三、ListView的优化
上面是最基本一种构建Adapter的方式，不过我们看到getView中每次都在重新加载view，这实际上是十分不好的，效率低下我们在上面介绍机制的时候也是说ListView存在缓存机制，但是我们好像完全没有使用，是的，细心一点我们发现getView还有其他的参数都被我们无视了，实际上第二个参数convertView是缓存的关键。

我们下面就来利用一下缓存机制。

```
    if (convertView == null){//如果从来没有初始化过view，即缓存中都没有加载过
            convertView = mLayoutInflater.inflate(R.layout.item,null);
        }
        ItemBean item = mList.get(position);
        TextView title = convertView.findViewById(R.id.title);
        TextView content = convertView.findViewById(R.id.content);
        ImageView itemImage = convertView.findViewById(R.id.item_image);

        title.setText(item.getItemTitle());
        content.setText(item.getItemContent());
        itemImage.setImageResource(item.getItemImageResourceId());
        return convertView;
```
这里是避免了反复去inflate那个xml，这个Inflate方法十分之耗时，这样利用ListView提供的参数的确是节省了不少时间，但是我们还是想，有没有可能让每个布局里面的控件也不用每次都去重新findViewById呢？findViewById依然是很耗时的。当然我们是有方法的.

***利用ViewHolder的终极优化***：

***第一步***：创建一个ViewHolder类（一般是Adapter这块的内部类）

```
    class ViewHolder{
        public ImageView itemImage;
        public TextView title;
        public TextView content;
    }
```
里面的三个字段就是我们要findViewById的那三个View。

***第二步***：新建ViewHolder引用。

- 如果没有缓存时候，实例化ViewHolder，把控件保存在ViewHolder对象中，即findViewById的结果全部给ViewHolder对象的字段，然后调用convertView的setTag方法，传入ViewHolder对象，建立起了convertView与ViewHolder之间的关系

```Java
        ItemBean item = mList.get(position);
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.item,null);
            viewHolder = new ViewHolder();
            viewHolder.itemImage = (ImageView)convertView.findViewById(R.id.item_image);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.content = (TextView)convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        }...
```
- 如果有缓存了，调用convertView的getTag取得关联的对象，将返回结果赋给ViewHolder引用

```Java
    else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
    ...    
```
这里也贴一下setTag和getTag的源码，看注释已经很清楚这是干什么的方法了：

```
public class View implements Drawable.Callback, KeyEvent.Callback,
        AccessibilityEventSource {
        ...
    /**
     * Returns this view's tag.
     *
     * @return the Object stored in this view as a tag, or {@code null} if not
     *         set
     *
     * @see #setTag(Object)
     * @see #getTag(int)
     */
    @ViewDebug.ExportedProperty
    public Object getTag() {
        return mTag;
    }

    /**
     * Sets the tag associated with this view. A tag can be used to mark
     * a view in its hierarchy and does not have to be unique within the
     * hierarchy. Tags can also be used to store data within a view without
     * resorting to another data structure.
     *
     * @param tag an Object to tag the view with
     *
     * @see #getTag()
     * @see #setTag(int, Object)
     */
    public void setTag(final Object tag) {
        mTag = tag;
    }
    ...
```
***第三步***:直接给viewHolder中的成员去setText，setImageResource等等，并且返回convertView

```
    viewHolder.title.setText(item.getItemTitle());
    viewHolder.content.setText(item.getItemContent());
    viewHolder.itemImage.setImageResource(item.getItemImageResourceId());

    return convertView;
```
这样呢我们就又避免了反复的findViewById，完成了最终极的优化！

## 四、总结
![BaseAdapter总结](https://upload-images.jianshu.io/upload_images/13852523-7f56cd65df2a7415.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)