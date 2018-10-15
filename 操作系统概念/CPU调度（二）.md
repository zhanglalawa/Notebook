所有能进入到就绪队列的进程都是所有准备工作（要执行的代码、处理的数据等其他资源）已经向操作系统申请准备好了，只欠东风（CPU）。当CPU有了空闲，就会启动一个调度算法，在就绪队列里面选一个合适的进程。什么样的进程是合适的？能让上一节中提到的各种指标越好的选择是越合适的，而更加重视哪个指标，则由具体的算法决定。
下面具体看几个调度算法。

#### FCFS（First-Come,First-Served）Scheduling
先到先服务调度，就绪队列中先到先得，实现也很简单，就是最简单的队列。是最朴素的算法。
> 一个实例
![P1P2P3](https://upload-images.jianshu.io/upload_images/13852523-fb3ccb4ace504007.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
P1P2P3三个进程的区间时间给出了，然后P1P2P3依次进入就绪队列，CPU的处理的“甘特图”就是下面那样。
甘特图中，画个矩形，下面的数字是单位时间轴，中间的字符表示哪个进程在这段时间内占有CPU。利用率在这段时间内一直在忙，所以达到了100%，等待时间在图中已经计算，周转时间依次是24、27、30，响应时间依次为0、24、27。
![P2P3P1](https://upload-images.jianshu.io/upload_images/13852523-688a67b067baeac6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
可以看到随顺序不同，等待时间波动很大。

#### SJF（Shortest-Job-First）
最短作业优先算法，进入就绪队列的进程预告需要多长CPU时间才能完成本次执行，选取需要时间最短的进程。实现应该是使用一个优先队列。
> 一个实例 非抢占式SJF
![](https://upload-images.jianshu.io/upload_images/13852523-5abe638d6d270011.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 另一个例子 抢占式SJF
![](https://upload-images.jianshu.io/upload_images/13852523-707fcbb52e10810c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
和非抢占式不一样的地方，在于当前占有CPU的进程在执行过程中有可能会被过程中进入就绪队列的执行时间更短的进程所代替。比如这个图中P1执行到2时候，P1还剩5个Burst Time，而此时P2进来了，它只需要4个Burst Time，它比P1的剩余时间要少，所以P2得到CPU，P1被抢占。剩下的同理，只要选择时间最短的即可，可以抢占正在占有CPU的进程资源。
周转时间（尾部时间减去Arrival Time）依次是16、5、1、6，等待时间就是周转时间减去执行时间，16-7=9，5-4=1，1-1=0，6-4=2。

这个算法绝对是由于FCFS算法的。
但是这个算法是无法实现的，需要预报队列中的Burst Cycle，这个时间报不准，算法就很难实现。预报是不可能准的，一个程序里面分支语句很多，各种if else等等，这是不可预估的。好了，你说可以把所有分支语句的时间全部预估，这没有问题。但是如果再考虑个如果有个scanf，要根据scanf的内容执行程序，scanf的东西数据规模，数据类型等等造成的不同分支能列的完吗hhhh，所以我们说预报是不可能的，这个算法是难以实现的。

> 指数平均模型去拟合算是一种方法：
![](https://upload-images.jianshu.io/upload_images/13852523-626caec99d432fe4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这是一个拟合的例子：
![](https://upload-images.jianshu.io/upload_images/13852523-6b479d5350d2dabd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### HRN （Highest response Ratio Next）
最高响应比算法
> 一个实例
![](https://upload-images.jianshu.io/upload_images/13852523-0aec61b2ce01f06c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
HRN的意思就是一个比例，显然W越长，HRN越大，CPU总是为HRN越高的进程服务，因为它的W相对于T，已经很高了，这是综合考虑了W和T的一个比例。

#### Priority Scheduling
优先权算法，每个进程都有一个**优先数**（priority number）通常是一个整型数。每次调度选择优先权最高的进程。最简单的实现方法就是一个链表，从里面选个最高优先级的即可。当然也有抢占式和非抢占式的区分。
- Preemptive 高优先权进程到达就绪队列时候引起调度
- Nonpreemptive 高优先权到达时不会触发调度

显然SJF的优先数就是执行时间。

> 优先权算法的一个缺陷——**进程饥饿**
  当优先权比较低的进程待在就绪队列时候，由于就绪队列是开放的，可以随时接收新的准备好的进程，这样一来，可能某些太可怜的进程一辈子也得不到CPU。
有一种**Aging**的思想去解决这个问题，在队列里面等的时间会折算到优先权里面去，比如一个低优先级的进程可能等了一年，等待时间折算到优先权，它的优先权变得越来越高了，那么总有一天可以得到CPU。

#### RR（Round Robin） 
轮转法，它解决了有可能一些进程永远响应不了的问题，它需要一个**time quantum（时间片）**帮忙，每个就绪进程获得一小段CPU时间，通常10ms~100ms。一个时钟在走，每隔时间片，就要有一个行动，时间片用完，引起中断。这个进程就被迫交出CPU，重新回到就绪队列。
实现很容易，就是需要一个硬件的计时器，以及软件的计数器。比如定时100ns，有一个计数器，定时一到，计数器减一，一直减到0时候认为时间片到了，调用Scheduler进行CPU调度，正在拥有CPU的进程被迫交出CPU，重新进入队列去和其它进程竞争。
这样一来，假设一共有n个进程，时间片是q。那么对任何进程而言，等待的时间最长是（n-1）q。
> 一个实例
![](https://upload-images.jianshu.io/upload_images/13852523-d7890bbdefb03cd5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

由于上下文切换的代价还是很大的，所以时间片的选择很有讲究。
**1.**时间片太短，确实能在尽可能短时间内，让所有进程都得到CPU响应，但是上下文切换频繁这个时间代价十分高；
**2.**时间片太长，那又体现不出来轮转法的特点。

下图是时间片与上下文切换时间的关系：
![](https://upload-images.jianshu.io/upload_images/13852523-788eaf8bebc3ca74.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
性能上平均周转一时间一般优于SJF，响应时间一定是优于SJF的。

都是一些基本算法，我们下面介绍在上面基础的调度算法之外的一些改进的算法。
#### Multilevel Queue
多层队列，把就绪队列分成几个队列。
实际情况中就绪队列中的进程是有不同的需求的，有些进程只是需要大量的运算，它不追求立刻得到响应，而有些进程要求CPU很短，但是希望立刻响应等等。把这些不同类进程搞到一起就是不合理的。
由此把一个就绪队列分成几个队列。有了多层队列，把有交互要求的放到前台，没有交互要求的放在后台去。
> 下面是一个实例
![](https://upload-images.jianshu.io/upload_images/13852523-77a4e57b6910edd2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

为每一个队列根据队列的特点分配一个调度算法。

设计多层队列时候，要考虑就绪进程进入就绪队列时候，它去哪里？CPU在队列间又是怎么分配的？
- 固定优先权法
  例如，先前台队列，再后台队列
- 时间片办法
  例如，80%的CPU时间给前台队列，20%的CPU时间给后台队列。

#### Multilevel Feedback Queue
多层反馈队列，就绪进程进入就绪队列以后，还能继续在几个队列迁移。比如一个进程开始交互要求很高，要做命名操作，操作完之后要做文本编辑了，交互要求降低了，那就把它迁移到第三层，编辑完之后，要编译了，交给编译器，连交互都不需要了，可以迁移到再低优先级的第四层去。
> 一个示例
三层队列
![](https://upload-images.jianshu.io/upload_images/13852523-918495e85e25d426.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
Q0——用RR算法，时间片8ms
Q1——用RR算法，时间片16ms
Q2——用FCFS算法
![](https://upload-images.jianshu.io/upload_images/13852523-6653b311f787aa35.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

设计时候需要考虑以下几点：

1.每层队列自己的调度算法；

2.一个算法，将就绪进程升级到高层次队列；

3.一个算法，将就绪进程降级到低层次队列；

4.一个算法，决定一个就绪进程进入到就绪队列时候，去哪一层；

5.队列个数。
