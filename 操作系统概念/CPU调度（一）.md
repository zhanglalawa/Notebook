## 相关基本概念
- CPU利用率 = CPU忙时 / 运行总时间
  引入多道程序设计，让多个进程竞争使用资源，目的就是为了提高计算机的资源使用率，尤其是CPU利用率。
  竞争使用资源，自然就存在了资源分配的问题。
- CPU区间 - I/O区间 的循环
  所有进程执行都包括CPU操作+I/O操作，都呈现出这样的循环。CPU burst和I/O burst翻译成CPU区间和IO区间。
![CPU IO burst cycle](https://upload-images.jianshu.io/upload_images/13852523-05018748b5525a46.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- CPU调度器
![](https://upload-images.jianshu.io/upload_images/13852523-34ed3ea5410e8e91.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/13852523-e3e9260f92b8ccc2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
调用scheduler的时机，通常有：
  - 某一进程从**运行状态转为等待状态**（如遇到了I/O请求，或调用wait等待一个子线程的终止）
  - 某一进程从**运行状态切换到就绪状态**（出现中断，或者被某个超高优先级进程抢占）上图中少了这个箭头
  - 某一进程从**等待状态转为就绪状态**（IO之后回到就绪队列，当然也有可能这个进程优先级很高等待完了立刻得到CPU）
  - 某一进程**终止**


切换CPU的情形不止以上所述的四种情形。还有其他调用scheduler的情形有好多好多。

不过所有情形可以有以下的分类：
1. 非抢占式nonpreemptive
    拥有CPU的进程**自愿**交出CPU，也就是上面的第一种（他需要新的资源，如IO）和第四种（结束了）情况。 
2. 抢占式preemptive
如果非自愿交出CPU，就是抢占式，如第二种（另一个高优先级来了被迫交出CPU）和第三种（进到就绪队列了他就想要资源，那对于正在拥有CPU的进程就是被迫的）情形。

- CPU分配器Dispatcher
  在Scheduler时候提到，调度器是先从就绪队列中选取一个进程，接下来再把CPU分配给这个进程。而第二步分配CPU的动作也可以由这里的dispatcher来完成，操作的内容通常包括：
  - switching context
  - kenel mode ——> user mode （CPU调度一定是在内核态里面做的，用户进程执行一定在用户态里面，所以一定涉及mode的切换）
  - 跳转至用户程序中PC寄存器所指示的位置

分配延迟：CPU分配器暂停前一进程，启动后一进程所经历的时间。

- CPU调度器的追求指标
  - CPU利用率（CPU utilization）
  - 吞吐率（Throughput）——单位时间内完成执行的进程数
  - 周转时间（Turnaround time）——执行某一进程所耗用的CPU累积时间，从进程提交到进程完成的所有时间段之和，包括等待存入内存、在就绪队列等待、在CPU上执行和IO执行
  - 等待时间（Waiting time）——某一进程等待在就绪队列里面的累计时间
  - 响应时间（Response time）——某一进程从发出调度请求（比如鼠标一动按了保存按钮请求保存文件），到其得到CPU调度器响应（页面有反应了，即文件保存成功或者失败），其间所经历的时间

这章的研究问题就是，有限的一个CPU怎样分配给就绪队列里的这些进程，使得CPU利用率最高，吞吐量最高，周转时间、等待时间、响应时间最短。