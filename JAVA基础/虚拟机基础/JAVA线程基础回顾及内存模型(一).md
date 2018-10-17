## 线程创建的两种方式
- 继承Thread类

```Java
class MyThread extends Thread{
    ......
    @Override
    public void run(){
        ......
    }
}

MyThread mt = new MyThread();  //创建线程
mt.start();                    //启动线程
```
- 实现Runnable接口

```Java
class MyThread implements Runnable{
    ......
    @Override
    public void run(){
        ......
    } 
}
MyThread mt = new MyThread();    //创建Runnable对象
Thread td = new Thread(mt);           //创建线程
td.start();                                          //启动线程
```
> - 两种方式的区别
  1.继承Thread类受限于JAVA单继承的特性，而实现Runnable接口则没有这种限制；
  2.Runnable的代码可以被多个Thread共享，适用于多个线程处理**同一资源（同一个Runnable对象）**的情形。

## 线程的生命周期
![](https://upload-images.jianshu.io/upload_images/13852523-7e119e5ddfcb89d1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 创建
  new一个线程对象。
- 就绪
  创建线程之后，调用了start()方法，此时线程进入就绪队列，等待CPU调度。或者阻塞状态的线程被唤醒。
- 运行
  获取到了CPU资源，执行run()方法。
- 终止
  线程的run()方法执行完毕，或者线程调用了相关的终止方法。
- 阻塞
  运行状态下，由于某种原因让出CPU资源，暂停自己的执行，就进入了阻塞状态，如调用了sleep()方法。

## 守护线程
- 基本概念
  JAVA线程有两类
  - 用户线程
    运行在前台，执行具体的任务，程序的主线程、连接网络的子线程等等都是用户线程
  - **守护线程**
    运行在后台，为其他前台线程服务，一旦所有用户线程都结束运行，守护线程会随JVM一起结束工作。数据库连接池中的监测线程，JVM虚拟机启动后的检测线程还有最常见的垃圾回收线程等等都是守护线程。

- 设置守护线程
  通过调用Thread类的**setDaemon（true）**方法来设置当前线程为守护线程。
  
> **注意事项**
> - setDaemon(true)必须在start()方法之前调用，否则会抛出异常。
> - 守护线程中产生的新线程也是守护线程。
> - 不是所有的任务都可以分配给守护线程来执行，比如读写操作或计算逻辑。
前面提到用户线程结束之后，守护线程就没有守护对象了，会随JVM一起结束工作，所以一旦在守护线程里面读写操作，那如果用户线程都结束了，可能会读写异常。

下面就是一个主线程结束，打断了守护线程的写操作的实例,我们本来是想写到word1到word999的，主线程scanner读取键盘输入，阻塞等待，一旦键盘输入完成，即scanner结束阻塞解除之后，主线程运行下去就结束了，守护线程即便没有写到999，也还是结束了。
```Java
class DaemonThread implements Runnable{
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		File file = new File("D:" + File.separator+"word.txt");
		try {
			writeFile(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private void writeFile(File file) throws Exception {
		OutputStream oStream =new FileOutputStream(file);
		int i = 1;
		while(i < 1000) {
			Thread.sleep(1000);
			oStream.write(("\r\nword" + i).getBytes());
			System.out.println(Thread.currentThread().getName() + ": word" + i++);
		}
		oStream.close();
	}
}
public class ThreadTest {
	public static void main(String[] args) {
		System.out.println("进入主线程" + Thread.currentThread().getName());
		Thread thread = new Thread(new DaemonThread());
		thread.setDaemon(true);
		thread.start();
		
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		System.out.println("退出主线程");
			
	}
}
```
在word6时候，控制台输入结束：
![](https://upload-images.jianshu.io/upload_images/13852523-9d7bb04395aa0896.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
文件内容是这样的：
![](https://upload-images.jianshu.io/upload_images/13852523-5bef354baab32c6f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 可见性
- 基本概念
  可见性：一个线程对共享变量值的修改，能够及时地被其他线程看到。
  共享变量：如果一个变量在多个线程的工作内存中都存在副本，那么这个变量就是这几个线程的共享变量。

## JAVA内存模型（JMM）
Java Memory Model描述了Java程序中各种变量（**线程共享变量**）的访问规则，以及在JVM中将变量存储到内存和从内存中读取出变量这样的底层细节。

> JMM
> - 所有共享变量都存储在主内存中
> - 每个线程都有自己独立的工作内存，里面保存该线程使用到的变量副本（是主内存中该变量的一份拷贝）
![](https://upload-images.jianshu.io/upload_images/13852523-88b73d68230ce9c2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 工作内存和主内存交互的八种原子操作
  关于一个变量如何从主内存中拷贝到工作内存、如何从工作内存同步回主内存之类的实现细节，Java内存模型定义了以下八种操作来完成，JVM实现时候必须保证下面提及的每一种操作都是原子的、不可分的。

 ①用于主内存变量：
  -  锁定（lock）：把变量标识为一条线程独占的状态。
  -  解锁（unlock）：把处于锁定状态的变量释放出来。
  -  读取（read）：把变量的值从主内存传输到线程的工作内存中，以便随后的load动作使用。
  -  载入（load）：把read操作从主内存中得到的变量值放入工作内存的变量副本中。
  
②用于工作内存变量：
  - 使用（use）：把工作内存中一个变量的值传递给执行引擎。
  - 赋值（assign）：把从执行引擎接收到的值赋给工作内存的变量。
  - 存储（store）：把工作内存中变量的值传送到主内存中，以便随后的write操作使用。
  - 写入（write）：把store操作从工作内存中得到的变量的值放入主内存的变量中。
> **确保并发操作安全的原则**：


> 1.**顺序但不连续**：必然是先`read`后`load`以及先`store`后`write`，有先后顺序的要求，但是并没有要求连续执行，比如`read`和`load`，`store`和`write`之间是要插入其他指令的，比如read a,read b,load a,load b就是合理的；

> 2.**读了一定载，存了一定写**，读或者存之后不允许拒绝载入和写操作，即read\load,store\write不允许他们之一单独出现；

> 3.**线程不能没有`assign`，就同步回主内存**，想同步回去总归要做点不一样的事情吧hhh

> 4.**新的变量只能在主内存“诞生”，不允许在工作内存中使用未初始化的变量**，即`use`和`store`这种涉及工作内存变量的操作，都要求这个变量是先执行过`assign`和`load`操作的（初始化操作）

> 5.**一个变量只允许一个线程`lock`操作，但是可以同一个线程`lock`套Lock**，如果多次Lock，自然也需要多次unlock才能解锁变量

> 6.**加锁之前先重新读取值**，Lock操作会清空工作内存中此变量的值，`use`之前需要重新`load`或者`assign`进行初始化

> 7.**不能解空锁，不能解别人的锁**unlock不能解锁没有lock的变量，也不能在一个线程unlock另一个线程中lock的变量

> 8.**解锁之前先同步**,`unlock`前，必须要先`store`、`write`，把变量同步回主内存。
我们下面所讲的所有内容，都可以在这些规则中找到对应的点。
以上8种内存操作以及8种规则限定，再加上其他volatile的一些特殊规定，完全确定了JAVA中哪些内存访问操作在并发下是安全的，由于定义相当严谨但又十分繁琐，很麻烦，后面我们会介绍一个**先行发生原则**来确定一个访问在并发环境下是否安全。

两条规定：

1.**线程不能直接与主内存交互。**
线程对共享变量的所有操作必须在自己的工作内存中进行，不能直接从主内存读写。

2.**各线程工作内存之间相互独立。**
不同线程之间无法直接访问其他线程工作内存中的变量，线程间变量

- 共享变量可见性实现的原理：

以线程1对共享变量的修改要被线程2及时看到为例，要经过以下步骤：

    1.把工作内存1中更新过的共享变量刷新到主内存中；

    2.从主内存中将刷新过的共享变量读取工作内存2中。

- 那么要实现可见性，必须保证：

    1.线程修改后的共享变量值**及时**从工作内存刷新到主内存(`store` and `write`)；

    2.其他线程能**及时**把共享变量新值从主内存更新到自己的工作内存(`load` and `read`)。



- 可见性实现方式：
  语言层面：
  - synchronized
  - volatile

先了解两个小的基本概念之后我们再来介绍synchronized和volatile。
## 指令重排序
代码书写的顺序与实际执行的顺序不同，是编译器或者处理器为了提高程序性能而做的优化。原因是重排序之后的指令可能更加符合CPU的执行特点，最大限度发挥CPU性能。
```Java
int num1 = 1;
int num2 = 2;
```
可能实际的执行顺序是相反的：
```Java
int num2 = 2;
int num1 = 1;
```
## as-if-serial
无论如何重排序，程序执行的结果应该与代码顺序执行的结果一致。
一个例子：
```Java
int num1 = 1;
int num2 = 2;
int sum = num1 + num2;
```
在单线程中前两句随便重排序（数据依赖关系不允许重排序），但是第三句绝对是在前两句之后，这样不会影响sum的最终结果是3。指令重排序不会影响单线程中变量可见性。
而多线程中，指令重排序很有可能导致变量可见性问题。后面详细叙述。

下面详细叙述synchronized和volatile。

## synchronized
synchronized实现互斥锁，能够实现**原子性**和**可见性**。
- 实现可见性原理
  JMM关于synchronized的两条规定：
  - 线程解锁`unlock`前（退出sychronized代码块时候），必须把共享变量的最新值刷新到主内存中`store` and `write`
  - 线程加锁`lock`时（进入sychronized代码块时候），将清空工作内存中共享变量的值，从而使用共享变量时需要从主内存重新读取最新的值`load` and `read`（加锁和解锁需要同一把锁）

  这两条规定也就保证了synchronized能够实现共享变量的可见性。

- 过程
  1.获得互斥锁
  2.清空工作内存
  3.从主内存拷贝变量的最新副本到工作内存
  4.执行代码
  5.更改后的共享变量值刷新到主内存
  6.释放互斥锁
- synchronized可见性分析代码示例
下面是一个线程不安全的代码：
```Java
public class Test {
	private boolean ready = false;
	private int result = 0;
	private int number = 1;

	public void write() {
		ready = true;//1.1
		number = 2;//1.2
	}

	public void read() {
		if (ready) {
			result = number * 3;//2.1
		}
		System.out.println("result的值为:" + result);//2.2
	}
	
	private class ReadWriteThread extends Thread{
		private boolean flag;
		
		public ReadWriteThread(boolean flag) {
			this.flag = flag;
		}
		
		@Override
		public void run() {
			if (flag) {//构造方法传入true执行写操作
				write();
			}else {//构造方法传入false执行读操作
				read();
			}
		}
	}

	public static void main(String[] args) {
		Test fuck = new Test();
		
		fuck.new ReadWriteThread(true).start();//启动写线程
		fuck.new ReadWriteThread(false).start();//启动读线程
	}
}
```
以上程序的逻辑很简单，就是一个Test类里面，有个result和number，有write和read方法。它内部有个线程类，通过传给线程类的构造器的boolean值来控制这个线程是读线程还是写线程。

然后在main里面分别启动一个读和写线程，运行后结果的输出可能有很多种情形。
result的值可能为：0,6,3
导致这种情况（共享变量不可见）出现的原因：
- **线程的交叉执行**
  比如最朴素的情况，先写后读，则输出结果6.写线程先启动1.1执行完ready=true，number此时是1，然后读线程得到CPU，进入if语句，最终打印result是3，而如果读线程直接执行完毕，result就是0。
- **重排序结合线程交叉执行**
  比如写线程先启动，重排序先执行1.2，然后读线程就执行了，这时候即使number值变了，result的结果也是0。
- **共享变量更新后的值没有在工作内存与主内存间及时更新**

而安全的代码则是在write和read方法都加上synchronized关键字。
```Java
  public synchronized void write() {
        ready = true;//1.1
        number = 2;//1.2
  }
  public synchronized void read() {
        if (ready) {
            result = number * 3;//2.1
        }
        System.out.println("result的值为:" + result);//2.2
  }
```
加了一把锁，锁内的代码在一段时间内只能有一个线程可以执行，只有当前线程释放锁，其他线程才能进入这块代码执行。
synchronized解决可见性的解决方案：
- 原子性
  避免了线程交叉执行锁内代码，同样由于保证锁内只有一个线程执行，那么重排序对单个线程来讲as-if-serial，始终结果是不变的，也不存在由于指令重排序导致的不可见问题了。
- 可见性
  上面提到了synchronized实现可见性的原理，在加锁和释放锁时候都是要刷内存的。也就保证了共享变量能够**及时**更新。不加synchronized也能更新，但是并不能保证是及时更新，那么高并发情况下就可能出现意外。

加了synchronized之后（这里是对象锁），就只有两种结果，0或者6了~
在写读之间加个sleep，让主线程休眠上1000ms，那么基本可以保证先写后读，最终结果只有6。

## volatile
synchronized保证原子性和可见性，volatile保证了可见性。

- volatile实现内存可见性的原理：
  从编译器和处理器角度来讲，通过加入**内存屏障**和**禁止重排序优化**实现
  - 对volatile变量执行`write`操作时候，处理器会在写操作前加入一条`store`屏障指令，会把工作内存中共享变量副本的值强制刷新到主内存共享变量中去，以便进行后续的`write`，主内存中就是写的最新的值。还能防止处理器，把volatile前面的变量重排序到volatile写操作之后。
  - 对volatile变量执行`read`操作时候，处理器会在读操作前加入一条`load`屏障指令，会把主内存中共享变量的值放入工作内存的共享变量副本中去，以便进行后续的`read`，工作内存中就是读的最新的值。还能防止重排序。

volatile没有lock，不能实现原子性。最简单的例子就是count++，这个实在不想说了...算了还是写个代码看一下，这段代码是保证了race的可见性的，我们预期的race的结果应该是200000：
```Java
public class Test {
	public static volatile int race = 0;
	public static void increase() {
		race ++;
	}
	
	public static void main(String[] args) {
		Thread[] threads = new Thread[20];
		for(int i = 0; i < 20; i++) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < 10000; j++) {
						Test.increase();
					}
				}
			});
			threads[i].start();
		}
		
		while(Thread.activeCount() > 1) {
			Thread.yield();
		}
		
		System.out.println(race);
	}
}

```
不过最终的运行结果小于200000，就是由于volatile并不能保证原子性，race++的底层机器指令是分几步完成的，不同的线程就会进行交叉操作。
- 自增操作原子性的解决方案
  - 使用synchronized关键字
  - 使用ReentrantLock(java.until.concurrent.locks包下)
  - 使用AtomicInteger(vava.util.concurrent.atomic包下)

- 使用synchronized示例：
```Java
...
public synchronized static void increase() {
	race ++;
}
...
```
- 使用ReentrantLock示例：
首先需要一个ReentrantLock对象，调用这个对象的lock方法，就加上了锁，这里推荐用try...finally写法，finally中调用unlock释放锁，因为锁中的操作可能抛出异常，当然这里race++显然没有异常，我们只是在这里演示一下推荐写法。
```Java
...
    public static ReentrantLock lock = new ReentrantLock();

	public static void increase() {
		lock.lock();
		try {
			race ++;
		}finally {
			lock.unlock();
		}
		
	}
...
```
- volatile使用场合

①对变量的写入操作不依赖当前值或者能够确保只有单一线程修改变量的值
  - 不满足：number ++、 count = count *5等等
  - 满足：boolean变量等

②该变量没有包含在具有其它变量的不变式中。
  - 不满足：程序中有两个volatile变量low和up，还有个不变式low<up

不满足上面两种情形的还是要通过加锁来保证原子性。第一种情形不再讨论了，第二个其实没有看懂。。网上搜了个例子。

```JAVA
public class A {
   private volatile int low=0；

   private volatile int up=100;

   public int getLow() { return low; }
   public int getUp() { return up; }

   public void setLow(int value) { 
       if (value > upper) 
            system.out.print(".......");
       low = value;
   }

   public void setUp(int value) { 
       if (value < low) 
           system.out.print(".......");

       up = value;
   }
}
```
这个例子里面是没有a++之类的东西的，这里面有两个VOLATILE变量，它包含了一个不变式就是LOW<UP（ if (value > upper) 和   if (value < low) ）初始值是0和100，如果有两个线程同时操作set函数，一比如A线程操作setlow(4),B线程setup(3),最后结果是4和3。所以这里我们是需要加锁的。volatile并没有卵用。

## volatile和synchronized的比较
![](https://upload-images.jianshu.io/upload_images/13852523-f15be8e68b087ed8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 最后的补充
两点：

①即使没有保证可见性的操作，如加锁或者volatile，很多时候共享变量依然能够得到主内存和工作内存之间的及时更新，出现不可见问题一般都是高并发情况下。

②之前的那八个操作都必须是原子操作，而对64位的数据类型（long和double），模型中特别定义了宽松的规定，允许虚拟机将没有被volatile修饰的64位数据的读写划分为两次32位的操作分开进行，这叫long和double的**非原子性协定**（Noatomic Treatment of double and long Variables）这样就有可能导致线程交叉操作，出现某些线程修改“半个变量”的情形。但现在这样实现的JVM还是很罕见的。