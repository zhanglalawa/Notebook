这篇主要是体力活。大体看了文档和一些教程之后学会基本的使用方法，没有原理。。只有如何使用没有源码分析。QAQ
# okhttp的基本使用
okhttp是一个网络请求框架。
## 引入依赖

```
implementation 'com.squareup.okhttp3:okhttp:3.11.0'
```

## 步骤
1. 拿到okHttpClient对象
2. 构造Request
3. 将Request封装为call
4. 执行call
```Java
    OkHttpclient okhttpClient = new OkHttpClient();
    Request.Builder builder = new Request.Builder();
    Request request = builder.get().url("http://fuck.com").build();
    Call call = okHttpClient.newCall(request);
    //Response response = call.execute();//同步执行,需要自己创建线程
    call.enqueue(new Callback(){//异步执行，enqueue已经创建好了子线程，里面的回调依然在子线程中，response在回调方法里面
        @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("fuck",response.body().string());
            }
    });
```
异步方法enqueue的回调依然在子线程里面，如果要更新UI，比如想把返回的结果set成一个TextView的text，就需要用runOnUiThread()切换到UI线程，再去setText。上面是get。下面看一个post的例子。

```java
    OkHttpClient client = new OkHttpClient();
    FormBody body = new FormBody.Builder()
        .add("your_param_1", "your_value_1")
        .add("your_param_2", "your_value_2")
        .build();
    Request request = new Request.Builder()
        .url("http://my.wonderfull.url/to/post")
        .post(body)
        .build();
    Response response = client.newCall(request).execute();
    //依然也可以异步enqueue
```
参考的其他一些例子：
> https://blog.csdn.net/qq_24004499/article/details/70054326

步骤很固定，具体还有一些其他方的方法去看文档。

# Retrofit使用
retrofit是网络请求框架的封装，它内部是使用okhttp来进行网络请求的。
## 准备工作：
添加Retrofit库、OkHttp库、数据解析器集成库的依赖，并注册网络权限：

```
implementation 'com.squareup.retrofit2:retrofit:2.4.0'
implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
```

```
<uses-permission android:name="android.permission.INTERNET"/>
```
## 步骤：
#### 1. 创建接收服务器返回数据的类
这要根据返回数据格式，比如有这样的json：

```json
{
  "code":1,
  "data":{
     "id":10001,
     "username":"Tom",
     "email":"888888888@qq.com",
     "tel":"18088888888"
  },
  "message":"success"
}
```
就需要这样的实体类：

```java
public class UserInfoModel {

    public int code;
    public UserInfo data;
    public String message;
    
    public static class UserInfo{
    
        public int id;
        public String username;
        public String email;
        public String tel;
    }
}
```
这个在第一行代码中是接触过的。不过我们可以使用插件快速生成这样的实体类。比如GsonFormat插件，可以参考这一篇：
> https://www.cnblogs.com/1024zy/p/6370305.html

File->Settings->Plugins—>查找所需插件—>Install 找到Gsonformat插件然后安装。
#### 2. 创建用于描述网络请求的接口
定义一个网络请求的接口，接口函数里要定义URL路径、请求参数、返回类型。其中，需要使用注解来描述请求类型和请求参数。注解什么意思后面会讲。下面是一个例子。

```
public interface Interface {
  
    @GET("URL")
    Call<Model>  getCall(@Query("xxx") String xxx);
   
    // 用@GET("URL")声明了URL路径
    // 用getCall()接收网络请求数据，并用注解@Query("xxx") 声明了请求参数
    // 该方法会返回一个Retrofit的Call对象，这里声明了这个对象处理的数据类型为自定义Model
}
```
再用上面那个实体举个例子：

```
public interface UserMgrService{
    
    @GET("login")
    Call<UserInfoModel> login(@Query("username") String username,@Query("pwd") String pwd);
}
```
大概看不懂了吧主要是注解emmmm，我们先过完流程。注解的具体内容参考文档或者博客。

#### 3.创建Retrofit对象并设置数据解析器


```Java
Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("URL") //设置网络请求的Url地址，注意以要以斜线（代表根目录）结尾
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器，这里表示用的是Gson
                .build();
```
创建Retrofit实例时需要通过**Retrofit.Builder**,并调用baseUrl方法设置URL。
**注意：**Retrofit2 的baseUlr 必须以/（斜线）结束，不然会抛出一个IllegalArgumentException
#### 4.生成接口对象

由于是interface不是class，无法直接调用内部方法，需要利用已创建的Retrofit对象去生成代理的接口对象。
```Java
UserMgrService service=retrofit.create(UserMgrService.class);
```

拿到代理对象之后，就可调用里面的方法了。

#### 5.调用接口方法返回Call对象

```Java
Call<UserInfoModel> call=service.login("Tom","123456");
```

#### 6.发送网络请求（异步 / 同步）并处理返回数据
这一步和Okhttp中最后call的execute和enqueue以及牵扯到的回调是一样的，就不再写了。返回的response.body就是call指定的泛型类型？
比如上例中Log一下：

```
Log.i("response","code:"+response.body().code);//结果为"code:1"
```
下面是一个官方的demo:

```java
//step3
public interface GitHub {
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(
        @Path("owner") String owner,
        @Path("repo") String repo);
  }

  public static void main(String... args) throws IOException {
    // step4：Create a very simple REST adapter which points the GitHub API.
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    // step5：Create an instance of our GitHub API interface.
    GitHub github = retrofit.create(GitHub.class);

    // step6：Create a call instance for looking up Retrofit contributors.
    Call<List<Contributor>> call = github.contributors("square", "retrofit");

    // step7：Fetch and print a list of the contributors to the library.
    List<Contributor> contributors = call.execute().body();
    
    //step8：
    for (Contributor contributor : contributors) {
      System.out.println(contributor.login + " (" + contributor.contributions + ")");
    } 
  }

```
最后是一个比较好的retrofit教程：
> https://www.jianshu.com/p/308f3c54abdd
> https://blog.csdn.net/whdAlive/article/details/81455792

# RxJava使用

rxjava是什么？a library for composing asynchronous and event-based programs by using observable sequences.用可用可观察的序列来组成异步和基于事件的程序的库。。。其实也看不太明白，目前的理解就是个用来处理异步的库，android中的Handler和AsyncTask就是干这事情的。为什么用它，就是因为rxjava很简洁。。。有多简洁往下看。
## 引入依赖：

```
    implementation 'io.reactivex:rxjava:2.0.1'
    implementation 'io.reactivex:rxandroid:2.0.1'
```
## 观察者模式
观察者模式在headfirst设计模式已经学习过了，观察者模式正式RxJava的基本工作原理。主题和观察者对应RxJava中的**Observable**和**Observer**，它们之间的连接就对应着**subscribe()**

与传统观察者模式不同， RxJava 的事件回调方法除了普通事件 onNext() 之外，还定义了三个特殊的事件：onComplete() 和 onError()，onSubscribe()。

- onComplete(): 事件队列完结时调用该方法。RxJava 不仅把每个事件单独处理，还会把它们看做一个队列。
- onError(): 事件队列异常。在事件处理过程中出异常时，onError() 会被触发，同时队列自动终止，不允许再有事件发出。
- onSubscribe()：RxJava 2.0 中新增的，传递参数为Disposable ，Disposable 相当于RxJava1.x中的Subscription,用于解除订阅。

这几个方法都是在为后面我们的异步做铺垫，RxJava中默认Observerble (被观察者)和Observer (观察者)都在同一线程执行任务。要实现异步，就要涉及线程的切换，下面会讲到具体用法。

## 基本用法
#### Observable的创建

```
Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                //执行一些其他操作
                //.............
                //执行完毕，触发回调，通知观察者
                e.onNext("我来发射数据");
            }
        });

```
从create方法的参数名ObservableOnSubscribe上也可以知道，只有主题和观察者建立连接之后, 主题才会开始发送事件. 也就是调用了subscribe() 方法之后才开始发送事件.
#### Observer的创建

```
Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            //观察者接收到通知,进行相关操作
            public void onNext(String aLong) {
                System.out.println("我接收到数据了");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

```
#### 订阅

```
 observable.subscribe(observer);
```
而最炫酷的是我们可以完全借助匿名类做一个链式操作，如下所示，和上面的两部分是相当的：

```Java
Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                //执行一些其他操作
                //.............
                //执行完毕，触发回调，通知观察者
                e.onNext("我来发射数据");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            //观察者接收到通知,进行相关操作
            public void onNext(String aLong) {
                System.out.println("我接收到数据了");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };);
```
两个参数ObservableEmitter和Disposable.

ObservableEmitter： Emitter是发射器的意思，用来发出事件的，它可以发出三种类型的事件，通过调用emitter的onNext(T value)、onComplete()和onError(Throwable error)就可以分别发出next事件、complete事件和error事件。next可以无限发送，complete和error发送一次之后后面再也接收不到消息了。onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError, 也不能先发一个onComplete, 然后再发一个onError, 反之亦然。

Disposable中的dispose方法调用后，观察者将不再接收事件。下面是Disposable的一个用例，我们调用4次onNext，在第2次的时候dispose了，输出结果符合预期：

```Java
Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
                Log.d(TAG, "emit 4");
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext: " + value);
                i++;
                if (i == 2) {
                    Log.d(TAG, "dispose");
                    mDisposable.dispose();
                    Log.d(TAG, "isDisposed : " + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        });
       
```
> 12-02 06:54:07.728 7404-7404/zlc.season.rxjava2demo D/TAG: subscribe
> 
> 12-02 06:54:07.728 7404-7404/zlc.season.rxjava2demo D/TAG: emit 1
> 
> 12-02 06:54:07.728 7404-7404/zlc.season.rxjava2demo D/TAG: onNext: 1
> 
> 12-02 06:54:07.728 7404-7404/zlc.season.rxjava2demo D/TAG: emit 2
> 
> 12-02 06:54:07.728 7404-7404/zlc.season.rxjava2demo D/TAG: onNext: 2
> 
> 12-02 06:54:07.728 7404-7404/zlc.season.rxjava2demo D/TAG: dispose
> 
> 12-02 06:54:07.728 7404-7404/zlc.season.rxjava2demo D/TAG: isDisposed : true
> 
> 12-02 06:54:07.728 7404-7404/zlc.season.rxjava2demo D/TAG: emit 3
> 
> 12-02 06:54:07.728 7404-7404/zlc.season.rxjava2demo D/TAG: emit complete
> 
> 12-02 06:54:07.728 7404-7404/zlc.season.rxjava2demo D/TAG: emit 4

#### subscribe的几个重载方法


```
    public final Disposable subscribe() {}
    public final Disposable subscribe(Consumer<? super T> onNext) {}
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {} 
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {}
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {}
    public final void subscribe(Observer<? super T> observer) {}

```
Action是无参无返回，Consumer是有一个参数的。

最后一个方法是我们一直在用的传入Observer对象的方法，上面的方法的参数中的Consumer呀，Action呀都对应着参数名的回调方法，这些参数可以简单理解成是把函数包装好，传了进去作为回调的方法。
例如：带有一个Consumer参数的方法表示观察者只关心onNext事件, 其他的事件我假装没看见, 因此我们如果只需要onNext事件可以这么写:

```Java
Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
                Log.d(TAG, "emit 4");
                emitter.onNext(4);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "onNext: " + integer);
            }
        });

```
上面扯了一大堆，我们也提到，默认的主题和观察者的方法都是运行在同一线程（main线程），我们说rxjava最强大的地方在于简介的异步处理，那么下面就来学一下如何实现异步，只需要在链中加一个点就好啦。

#### 线程调度器scheduler

链条中加方法，subscribeOn指定被观察者的动作线程，observeOn指定观察者的线程，这里我们的例子就是在子线程中发送信息，在主线程中接收子线程发送来的Integer：
```Java
@Override                                                                                       
protected void onCreate(Bundle savedInstanceState) {                                            
    super.onCreate(savedInstanceState);                                                         
    setContentView(R.layout.activity_main);                                                     
                                                                                                
    Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {   
        @Override                                                                               
        public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {            
            Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());           
            Log.d(TAG, "emit 1");                                                               
            emitter.onNext(1);                                                                  
        }                                                                                       
    });                                                                                         
                                                                                                
    Consumer<Integer> consumer = new Consumer<Integer>() {                                      
        @Override                                                                               
        public void accept(Integer integer) throws Exception {                                  
            Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());              
            Log.d(TAG, "onNext: " + integer);                                                   
        }                                                                                       
    };                                                                                          
                                                                                                
    observable.subscribeOn(Schedulers.newThread())                                              
            .observeOn(AndroidSchedulers.mainThread())                                          
            .subscribe(consumer);                                                               
}

```
注意事项：
> 1. 多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.
> 2. 多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.

subscribeOn和observeOn里面参数可以有以下几种可选值：
> Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
> 
> Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
> 
> Schedulers.newThread() 代表一个常规的新线程
> 
> AndroidSchedulers.mainThread() 代表Android的主线程

#### Retrofit和RxJava结合网络请求
我们用一个天气预报的接口做示例。
> https://www.sojson.com/open/api/weather/json.shtml?city=北京
city的值决定的查询的城市

先来看一下Retrofit单独的请求。先用GsonFormat去建一个实体类WeatherEntity，这里数据太长就不贴了。然后我们建立Interface:

```Java
public interface GetWeatherService {
    @GET("open/api/weather/json.shtml")
    Call<WeatherEntity> getMessage(@Query("city") String city);
}
```
然后我们找个按钮或者什么能响应点击的下面写上网络请求的代码:

```Java
    Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://www.sojson.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                GetWeatherService weatherService = retrofit.create(GetWeatherService.class);
                Call<WeatherEntity> call = weatherService.getMessage("西安");
                call.enqueue(new Callback<WeatherEntity>() {
                    @Override
                    public void onResponse(Call<WeatherEntity> call, Response<WeatherEntity> response) {
                        textView.setText(response.body().getData().getShidu());
                    }

                    @Override
                    public void onFailure(Call<WeatherEntity> call, Throwable t) {

                    }
                });
```
请求的结果我们只在TextView里面显示一个湿度。
好，接下来看一下Retrofit和RxJava,添加依赖需要新增一个。

```
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'
```

我们主要在上面单独使用Retrofit基础上进行一些改变就好了，首先是在链条里面加上一条**addCallAdapterFactory(RxJavaCallAdapterFactory.create())**添加RxJava 适配器。
即：

```Java
    Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://www.sojson.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
```
其次我们还要修改请求接口，返回类型不再是Call，改成是Observable，泛型类型是WatherEntity：

```Java
public interface GetWeatherService {
    @GET("open/api/weather/json.shtml")
    Observable<WeatherEntity> getMessage(@Query("city") String city);
}
```
然后就可以用RxJava去请求了，这里我们好像第一不太清楚什么时候会发送网络请求，第二看不到什么时候观察者的方法会被调用，实际上呢，和上面一样，当上下游建立联系时即subscribe()方法调用之后，就开始发送请求了，而subscribe会监听网络请求的返回类，一旦请求返回subscribe里面的内容会被调用：

```
    GetWeatherService weatherService = retrofit.create(GetWeatherService.class);
    Observable<WeatherEntity> call = weatherService.getMessage("西安");
                call.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<WeatherEntity>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(WeatherEntity weatherEntity) {
                                textView.setText(weatherEntity.getData().getShidu());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                Log.d("test","onComplete()");
                            }
                        });
```
成功的回调在onNext里面。他还会调用onComplete。如果失败了会回调onError。

这里有一个隐患就是，网络请求在io线程里面，如果在请求过程中主线程结束了（也就是这里的activity已经退出了），再去UI线程去更新UI程序就有可能崩溃。这里呢可以借助我们之前提到的Disposable这个参数，在活动销毁的时候去调用d.dispose()来切断观察者和主题的联系，这样观察者收不到事件，也就不可能出现我们说的这种情况了。

如果有多个Disposable 该怎么办呢, RxJava中已经内置了一个容器CompositeDisposable, 每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中, 在退出的时候, 调用CompositeDisposable.clear()切断所有联系。

既然说了这么多了，感觉还是应该具体介绍一下Disposable.
#### Disposable的获取
获取Disposable对象，我们之前通过Observer中用onSubscribe的参数是一种方式，还有另一种方式，我们在介绍subscribe的的几个重载方法时候，发现除了最后一种传入Observer的方法，其他传那种Consumer、Action的全部返回类型都是Disposable，没错，就是通过subscribe的返回值来获得这个Disposable对象的。然后在适合的时候去dispose()就好了。


#### 其它一些操作符
##### map操作符
传入Function<T1,T2>参数将发送的类型实现由T1到T2的转换。Function的功能和之前的Action、Consumer是类似的，不过Function中的方法是带返回值的。如下示例：

```
Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });

```
这里加入的map操作符，就把本来发送的Integer类型变为String类型。
##### flatmap操作符
这个会比较难理解一些，它也是把传入的参数转化之后返回另一个对象，但是flatmap返回的是一个Observalbe对象，FlatMap将一个发送事件的上游Observable变换为多个发送事件的Observables，然后将它们发射的事件合并后放进一个单独的Observable里.看一个例子：

```
    Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10,TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });

```


flatmap第一个泛型是onNext传递过来的值，搞一个list，然后每个list通过Observable.fromIterable作为返回值又搞成多个发送事件被subscribe接收。

再看一个例子，假设此时有个学生类，我们想要打印出每个学生的姓名，可以直接用map从Student映射到String，是这样的：

```Java
Student[] students = ...;

Observable.fromIterable(students)
    .map(new Function<Student, String>() {
        @Override
        public String call(Student student) {
            return student.getName();
        }
    })
    .subscribe(new Consumer<String>() {
            @Override
            public void accept(String name) throws Exception {
                Log.d(TAG, name);
            }
        });
```
那么假如我们想要打印出学生的所有课程呢？

```Java
Student[] students = ...;

Observable.fromIterable(students)
        .subscribe(new Consumer<Student>() {
            @Override
            public void accept(Student student) throws Exception {
                List<Course> courses = student.getCourses();
                for(Course course : courses){
                    Log.d(TAG,course.getName());
                }
            }
        });
```
如果用上flatmap就可以不用去写那个循环：

```Java
Student[] students = ...;

Observable.fromIterable(students)
        .flatMap(new Function<Student, ObservableSource<Course>>() {
            @Override
            public ObservalbleSource<Course> apply(Student student)throws Exception {
                return Observable.fromIterable(student.getCourses());
            }
        }
        .subscribe(new Consumer<Student>() {
            @Override
            public void accept(Course course) throws Exception {
                    Log.d(TAG, course.getName());
                }
            }
        });
```
下面文章中是Retrofit联用的一个实现register后用flatmap直接一条链完成login的实例，很受启发：
> https://www.jianshu.com/p/128e662906af
