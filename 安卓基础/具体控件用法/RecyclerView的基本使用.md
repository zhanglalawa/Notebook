RecyclerView自己提供了Adapter，主要为了强制人们使用ViewHolder。下面直接看使用步骤。
## 步骤
由于已经比较熟练掌握了ListView，所以在和ListView比较相似的点处不会再啰嗦。不同点会重点标出。
#### 1.引入依赖（不同点一）
```
  implementation 'com.android.support:recyclerview-v7:27.1.1'
```
#### 2.添加RecyclerView于Layout中
```Xml
  <android.support.v7.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```
#### 3.获得RecyclerView对象，并且准备数据源
```Java
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        List<ItemBean> list = new ArrayList<>();
        for (int i = 1;i<=20;i++){
            list.add(new ItemBean(R.mipmap.ic_launcher,
                    "title"+i,
                    "content"+i));
        }
```
#### 4.创建Adapter和ViewHolder（不同点二，细则不同，强制要求ViewHolder）
![](https://upload-images.jianshu.io/upload_images/13852523-cb3e86bd9160befa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这里的Adapter需要继承自RecyclerView.Adapter<ViewHolder>,我们需要自己写一个继承自ViewHolder的类传给这个泛型参数。
![](https://upload-images.jianshu.io/upload_images/13852523-52767d4ab27d26a7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
当然把ViewHolder放成内部类也是可以的。
#### 5.配置ViewHolder和重写Adapter的方法(不同点三，方法不同)
![三个方法](https://upload-images.jianshu.io/upload_images/13852523-61836ba11e6a21f2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里涉及到了三个方法。
首先字段、构造器和getItemCount还是和ListView的没有什么区别：
```Java
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<ItemBean> mList;
    private LayoutInflater mLayoutInflater;
    public MyAdapter(Context context, List<ItemBean> list){
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }
    ...
}
```
重点在后面的两个方法，我们从名字上看出来，一个是创建ViewHolder，一个是绑定ViewHolder。
下面我们分步骤说明，我们这里用的子项布局还是上一篇文章一样的那个item.xml。

**第零步：**完成ViewHolder的配置
我们来完成ViewHolder类的，这个必须要有一个构造器，并且需要一个View参数，这个参数就是当前子项的View，ViewHolder将会和它进行绑定。同时我们还必须要设置子项布局中的各个控件的字段，并通过这个view来findViewById给赋值。
```Java
class MyViewHolder extends RecyclerView.ViewHolder{
    public ImageView itemImage;
    public TextView title;
    public TextView content;

    public MyViewHolder(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        content = itemView.findViewById(R.id.content);
        itemImage = itemView.findViewById(R.id.item_image);
    }
}
```
**第一步：**重写onCreateViewHolder
```Java
//创建一个ViewHolder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item,parent,false);
        return new MyViewHolder(itemView);
    }
```

**第二步：**重写onBindViewHolder
```Java
//绑定一个ViewHolder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemBean item = mList.get(position);
        holder.itemImage.setImageResource(item.getItemImageResourceId());
        holder.title.setText(item.getItemTitle());
        holder.content.setText(item.getItemContent());
    }
````
这样就完成了Adapter。
可以看到，RecyclerView中实际上是弄了两个方法来完成我们在ListView的getView用ViewHolder来优化ListView的逻辑，可见Goolge在RecyclerView中已经是让开发者强制使用ViewHolder了。
#### 6.通过构造器创建Adapter，通过RecyclerView实例setAdapter
```Java
MyAdapter myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);
```
#### 7.通过RecyclerView实例setLayoutManager设置RecyclerView的整体布局（不同点四，可以自己决定整体布局，最大的不同）
ListView中并没有这个setLayoutManager的方法，从名字上我们也能看出来，ListView，都List了，肯定是像列表那样线性排列下来的。而RecyclerVIew则是可选布局的。这里我们就用LinearLayout去做一个和前面ListView一样的效果,先要创建一个LayoutManager，我们用的是LinearLayoutManager，然后再去set，先贴上源码中能看懂的两个LinearLayoutManager的构造方法，第三个目前不太懂。。
![LinearLayoutManager的源码中的两个构造函数](https://upload-images.jianshu.io/upload_images/13852523-a65acc7ddfc2f300.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里我们直接用第一个就好了。
```Java
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
```
运行结果如下：
![image.png](https://upload-images.jianshu.io/upload_images/13852523-9c76a2c7b78a1ef8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
## 最后再提一下RecyclerView的点击事件
RecyclerView并不像ListView那样给我们提供了专门的onItemClickListener,但是我们可以在Adapter里面自己去注册view的点击事件，例如holder.title.setOnClickListener这样按照最一般的view的点击事件去注册。可以看自己当时知乎上写的文章的最后。。（终于用了自己文章一次。。）
[RecyclerView用法](https://zhuanlan.zhihu.com/p/37381538)