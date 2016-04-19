# Java  hashmap

* Hashmap内部对每个键值对使用Entry这样的结构来描述.每个Entry除了有key和value之外还有一个next引用.内部的table中在对应位置上存储Entry.如果有新的结点计算出的hash值一样,就采用**头插法**将新的结点插入数组,将旧的entry的next指向新的.
* 当结点个数大于TREEIFY_THRESHOLD之后将不再使用链表,调用treeifybin()方法,而改成**红黑树**存储. 
* hashmap将装载因子设置为0.75,大于这个值的时候.调用resize()方法将table扩大.
* resize()的策略
* size超过阈值会调resize函数，resize函数中新建一个散列表数组，容量为旧表的**2倍**，接着需要把旧表的键值对迁移到新表，这里分三种情况：
  * 表项只有一个键值对时，针对新表计算新的索引位置并插入键值对
  * 表项节点是红黑树节点时（说明这个bin元素较多已经转成红黑树了），分割(split)到更高级或更低级的bin中,如果已经足够小,就恢复为链表.
  * 表项节点包含多个键值对组成的链表时（拉链法）把链表上的键值对按hash值分成lo和hi两串，lo串的新索引位置与原先相同[原先位置j]，hi串的新索引位置为[原先位置j+oldCap]； 链表的键值对加入lo还是hi串取决于 判断条件if ((e.hash & oldCap) == 0)，因为capacity是2的幂，所以oldCap为10...0的二进制形式，若判断条件为真，意味着oldCap为1的那位对应的hash位为0，对新索引的计算没有影响（新索引=hash&(newCap-1)，newCap=oldCap<<2）；若判断条件为假，则 oldCap为1的那位对应的hash位为1， 即新索引=hash&( newCap-1 )= hash&( (oldCap<<2) - 1)，相当于多了10...0，即oldCap 例子： 旧容量=16，二进制10000；新容量=32，二进制100000 旧索引的计算： hash = xxxx xxxx xxxy xxxx 旧容量-1 1111 &运算 xxxx 新索引的计算： hash = xxxx xxxx xxxy xxxx 新容量-1 1 1111 &运算 y xxxx 新索引 = 旧索引 + y0000，若判断条件为真，则y=0(lo串索引不变)，否则y=1(hi串索引=旧索引+旧容量10000)
  * LinkedHashmap 用循环链表记录插入顺序,还可以设置缓存为LRU.

    LinkedHashmap 设置为LRU的原理是用在get()方法中调用makeTail()方法,把使用过的元素调整到链表的最后
  * HashSet底层直接使用HashMap实现,value直接用一个叫做PRESENT的final对象填充,这个对象就是一个简单的Object.
  * ConcurrentHashMap 是线程安全的,但是不是对所有的访问加了同步锁,这样就变成hashtable了.ConcurrentHashmap把一张Table分成N个Segment,每个Segment是16的大小.调用spread()函数算出存储在哪个Segment(子table)里面.只有对segment的访问才会加锁.
  * 需要注意的是ConcurrentHashMap的读取操作是不加锁的,只有写操作加锁

# Excutor框架

* Excutor框架是Java5.0开始引入的.目的是把任务的执行和线程生命周期的管理分隔开来.
* 因为创建线程的代价比较大,并且为了有效的管理线程的生命周期以及对资源做有效管理(如并发量的控制),所以引入线程池的概念,Excutor提供几种线程池的默认实现
  这里可以结合Aexi谈谈池的作用和享元模式
  | 类型               | 说明                                       |
  | ---------------- | ---------------------------------------- |
  | CachedThreadPool | 每个任务一个线程,当有线程可以回收时不再创建新线程.               |
  | FixedThreadPool  | 刚开始就一次性创建固定数量的线程.最好的数量是CPU核心数+1.多余的任务将挂起等待 |
  | SingleThreadPool | 相当于只有一个线程的FixedThreadPool,多个任务将排队一个一个的执行. |
  ```Java
  private volatile long keepAliveTime;// 线程存活时间
  private volatile boolean allowCoreThreadTimeOut;// 是否允许核心线程存活
  private volatile int corePoolSize;// 核心池大小
  private volatile int maximumPoolSize; // 最大池大小
  private volatile int poolSize; //当前池大小
  private int largestPoolSize; //最大池大小，区别于maximumPoolSize，是用于记录线程池曾经达到过的最大并发,理论上小于等于maximumPoolSize
  ```

有了以上定义好的数据，下面来看看内部是如何实现的 。 Doug Lea 的整个思路总结起来就是 5 句话：
1.  如果当前池大小 poolSize 小于 corePoolSize ，则创建新线程执行任务。
2.  如果当前池大小 poolSize 大于 corePoolSize ，且等待队列未满，则进入等待队列
3.  如果当前池大小 poolSize 大于 corePoolSize 且小于 maximumPoolSize ，且等待队列已满，则创建新线程执行任务。
4.  如果当前池大小 poolSize 大于 corePoolSize 且大于 maximumPoolSize ，且等待队列已满，则调用拒绝策略来处理该任务。
5.  线程池里的每个线程执行完任务后不会立刻退出，而是会去检查下等待队列里是否还有线程任务需要执行，如果在 keepAliveTime 里等不到新的任务了，那么线程就会退出。

**cached传入的poolSize为0,MaximumPoolSize为Integer.maxvalue.  queue的容量为0**

##  结束一条 Thread 有什么方法？

**非阻塞的情况:**
* 可以引入一个stop变量,对所有需要控制生命周期的线程都可见,最好还要设置为volatile保持可见性.然后while(!stop)(或者直接while(!isInterupted())
  stop()函数已经废弃,因为它不释放锁,并且让线程处于"受损的中间状态",其他线程可以在这种状态去浏览并修改它.

**阻塞的情况:**
* 可以使用interupt()方法来中断阻塞中的线程.但是只能中断`sleep()`,`wait()`,`join()`,`NIO的阻塞`的线程.
* interupt()中断通过抛出异常来中断
* I/O阻塞和sychronized阻塞不会被interupt抛出中断异常.这种情况可以通过释放底层资源来中断
* 调用interupt **ed()**会清除中断标记,而isinterupted()不会.这两个的功能都是判断中断标记位.
* 某些版本的JDK提供interuptedIOException但是不是所有的平台都支持.
* NIO会相应interupt()中断
* interrupt()不是为了终止线程,而是为了通知线程可以停止了

**interupt()实现原理:**
* 内部调用interuptblocker对象的interupt()方法.还是检测内部的中断标记.
* 猜测一下(如果给我实现),sleep()实现应该是定时器,如果CPU调度到自己看看定时器有没有到,如果没到就放弃时间片.如果发现interupt()标记就回去,并由block.interupt()抛出interupt异常.

# 几种引用类型
JDK1.2之前只有强引用,其他几种引用都是在JDK1.2之后引入的.
* 强引用（Strong Reference）
  * 最常用的引用类型，如Object obj = new Object(); 。只要强引用存在则GC时则必定不被回收。
* 软引用（Soft Reference）
  * 用于描述还有用但非必须的对象，当堆将发生OOM（Out Of Memory）时则会回收软引用所指向的内存空间，若回收后依然空间不足才会抛出OOM。一般用于实现内存敏感的高速缓存。
  * 当真正对象的finalize()方法调用之后并且内存已经清理, 那么如果SoftReference object还存在就被加入到它的 ReferenceQueue.只有前面几步完成后,Soft Reference和Weak Reference的get方法才会返回null
* 弱引用（Weak Reference）
  * 发生GC时必定回收弱引用指向的内存空间。
  * 和软引用加入队列的时机相同
* 虚引用（Phantom Reference)
  *  又称为幽灵引用或幻影引用，，虚引用既不会影响对象的生命周期，也无法通过虚引用来获取对象实例，仅用于在发生GC时接收一个系统通知。
  *  当一个对象的finalize方法已经被调用了之后，这个对象的幽灵引用会被加入到队列中。通过检查该队 列里面的内容就知道一个对象是不是已经准备要被回收了。
  *  虚引用和软引用和弱引用都不同,它会在内存没有清理的时候被加入引用队列.虚引用的建立必须要传入引用队列,其他可以没有

**加入引用队列的都是引用对象,而不是真正的对象**

若一个对象的引用类型有多个，那到底如何判断它的可达性呢？其实规则如下：
* 单条引用链的可达性以最弱的一个引用类型来决定；
* 多条引用链的可达性以最强的一个引用类型来决定；

参考资料
>* If the garbage collector discovers an object that is softly reachable, the following occurs:
>  The SoftReference object's referent **field is set to null**, thereby making it not refer to the heap object any longer.
>  The heap object that had been referenced by the SoftReference **is declared finalizable**.
>  When the heap object's **finalize() method is run and its memory freed**, the SoftReference object **is added to its ReferenceQueue**, if it exists.
>* If the garbage collector discovers an object that is weakly reachable, the following occurs:
>  The WeakReference object's referent **field is set to null**, thereby making it not refer to the heap object any longer.
>  The heap object that had been referenced by the WeakReference is declared finalizable.
>  When the heap object's **finalize() method is run and its memory freed**, the WeakReference object **is added to its ReferenceQueue**, if it exists.
>* If the garbage collector discovers an object that is phantomly reachable, the following occurs:
>  The heap object that is referenced by the PhantomReference **is declared finalizable**.
>  Unlike soft and weak references, the PhantomReference is **added to its ReferenceQueue before the heap object is freed**. This allows for action to be taken before the heap object is reclaimed.

# NIO
JDK1.4引入了NIO.旧的IO库已经用NIO重新实现过,以便利用这种速度的提高.因此,我们不显式的使用NIO的API,也能从其中受益
## NIO的三个概念

* Channels 与 Buffers（通道和缓冲区）：而NIO是基于Channel和Buffer进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。
  这些通道覆盖了UDP和TCP网络IO，以及文件IO
* Selectors（选择器）：Java NIO引入了选择器的概念，选择器用于监听多个通道的事件（比如：连接打开，数据到达）。因此，单个的线程可以监听多个数据通道
  在一个单线程中使用一个Selector处理多个Channel
  要使用Selector，得向Selector注册Channel，然后调用它的select()方法。这个方法会一直阻塞到某个注册的通道有事件就绪。一旦这个方法返回，线程就可以处理这些事件，事件的例子有如新连接进来，数据接收等。

## NIO和IO和AIO区别
* NIO同步非阻塞,BIO同步阻塞,AIO异步非阻塞
* NIO操作系统相关,充分利用了磁盘的预读(提前读后面几个扇区的内容)和缓存(读出来放在内存里)
* NIO按照操作系统提供的block(块)从磁盘拿数据.可以随时切换在块中的指针位置,更灵活.io使用流线性的读取.
* 调用线程的interupt()方法的时候,nio可发生中断.而io不会中断
* AIO是jdk1.7版本加进来的,异步非阻塞

## 同步异步 阻塞非阻塞

* 同步
  用户进程触发IO操作，并等待或轮询查看IO是否就绪

* 异步
  用户进程触发IO后，该进程可以做其他事，当IO已完成后会通知该进程（异步的特点）
  使用异步IO，JAVA将IO读写委托给OS处理，同时需要将数据缓冲区地址和大小传给IO

* 阻塞
  如果程序试图对文件进行读写操作，但是并没有可进行的东西，那么此时程序一直进入等待状态

* 非阻塞
  没有东西可读写，立即返回，不会进入等待状态；非阻塞是指请求的资源没有准备好的时候不要阻塞请求的线程，线程会继续往下执行

* 同步阻塞—顺序执行
  当系统调用read()时，app会阻塞并对内核进行上下文切换。然后触发读操作，当响应返回时（例如数据从一个读取的设备中返回），数据被移动到用户空间的缓存中。最后app就会解除阻塞（read()调用返回）

* 同步非阻塞—轮询
  在上一个同样的场景下，设备以非阻塞的形式打开，这意味着在IO未完成时，read操作可能会返回一个错误代码直到真正有数据返回，这样会浪费一些CPU资源

* 异步非阻塞—委托完成
  处理与IO重叠进行的模型，read请求会立即返回（说明read请求已经成功发起了），在后台完成读操作时，app会执行其他处理操作。当read的响应到达时，会产生一个信号/执行一个基于先从的回调方法来完成这个IO处理过程（注意与同步非阻塞不同，同步非阻塞虽然会立即返回结果，但是一直在轮询是否有正确数据返回，会浪费一些 ）

# Volatile
* 线程的可见性
* 禁止指令重排序
* 对于64位数据,禁止读写操作分两次执行
* volatile不保证线程安全性
  * 由于volatile只提供了可见性，并没有提供互斥性，volatile不能替代synchronized,在多线程并发修改某个变量值时，依然会出现并发问题
* volatile保证可见性,但是不像atomic保证原子性
* 用途:线程的公共变量退出的机制

# wait()和sleep()的区别

* sleep来自Thread类，和wait来自Object类
* 调用sleep()方法的过程中，线程不会释放对象锁。而 调用 wait 方法线程会释放对象锁
* sleep睡眠后不出让系统资源，wait让出系统资源其他线程可以占用CPU
* sleep(milliseconds)需要指定一个睡眠时间，时间一到会自动唤醒

# 反射机制

## 反射机制的实现

反射机制主要借助以下4个类进行实现

1. Class ：运行时的类对象
2. Constructor：类的构造器对象
3. Field：类的属性对象
4. Method：类的方法对象

## 反射机制提供的功能

1. 得到一个对象所属的类
2. 获取一个类的所有成员变量和方法
3. 在运行时创建对象
4. 在运行时调用类方法

### 如何获得class类？

```java
Class.forName("类的路径");
类名.class;
实例.getClass();
```

# Java的各种锁

* 自旋锁

  自旋锁是采用让当前线程不停地的在循环体内执行实现的，当循环的条件被其他线程改变时 才能进入临界区

* 可重入锁

  Synchronize和reentrantlock都是可重入锁

  reentrantlock具有和synchronized相同的并发性和内存语义，但是添加了类似轮询锁、定时锁(设定超时释放)和可中断锁等候的一些特性。此外，它还提供了在激烈争用情况下更佳的性能。（换句话说，当许多线程都想访问共享资源时，JVM 可以花更少的时候来调度线程，把更多时间用在执行线程上。）

  reentrantlock锁意味着什么呢？简单来说，它有一个与锁相关的获取计数器，如果拥有锁的某个线程再次得到锁，那么获取计数器就加1，然后锁需要被释放两次才能获得真正释放。这模仿了 synchronized 的语义；如果线程进入由线程已经拥有的监控器保护的 synchronized 块，就允许线程继续进行，当线程退出第二个 synchronized块的时候，不释放锁，只有线程退出它进入的监控器保护的第一个synchronized 块时，才释放锁

* 阻塞锁

  synchronized就是阻塞锁,锁住的时候是阻塞的

* 读写锁--ReadWriteLock

  ReentrantReadWriteLock中定义了2个内部类, ReentrantReadWriteLock.ReadLock和ReentrantReadWriteLock.WriteLock, 分别用来代表读取锁和写入锁. ReentrantReadWriteLock对象提供了readLock()和writeLock()方法, 用于获取读取锁和写入锁. 

  - 读取锁允许多个reader线程同时持有, 而写入锁最多只能有一个writter线程持有.
  - 读写锁的使用场合: 读取共享数据的频率远大于修改共享数据的频率. 在上述场合下, 使用读写锁控制共享资源的访问, 可以提高并发性能.
  - 如果一个线程已经持有了写入锁, 则可以再持有读写锁. 相反, 如果一个线程已经持有了读取锁, 则在释放该读取锁之前, 不能再持有写入锁.
  - 可以调用写入锁的newCondition()方法获取与该写入锁绑定的Condition对象, 此时与普通的互斥锁并没有什么区别. 但是调用读取锁的newCondition()方法将抛出异常.

# JVM的内存模型(JMM)
![](http://7xntdm.com1.z0.glb.clouddn.com/JVM%E5%86%85%E5%AD%98%E7%AE%A1%E7%90%86.png)

## 线程隔离数据区
 * 程序计数器 (Program Counter Register)
   * 当前线程所执行的字节码行号指示器
   * 通过改变计数器的值来选取下一条需要执行的字节码指令：分支、循环、跳转、异常处理、线程恢复


* JVM虚拟机栈 (Java Virtual Machine Stacks)
  * Java方法执行内存模型
  * 用于存储局部变量、操作数栈、动态链接、方法出口等信息
* 本地方法栈 (Native Method Stacks)
  * 为JVM用到本地方法服务

## 线程共享数据区

* 方法区(Method Area)，即Nod-Heap,永久代
  * 用于存储JVM加载的类信息、常量、静态变量以及编译器编译后的代码等数据
  * 方法区里面有个运行时的常量池
* 常量池
  * 用来存放编译器生成的各种符号引用和String常量
  * 它具备动态性，如果String类调用inter()方法，那么这个字面量将会被放入常量池中
* JVM堆(Java Virtual Machine Heap)，即GC堆
  * 垃圾回收主要地方
  * 存放所有对象实例的对象
  * JVM堆分为新生代与老生代
  * JVM堆分为新生代与老生代
    ![](http://7xntdm.com1.z0.glb.clouddn.com/JVM%E5%A0%86%E5%86%85%E7%A9%BA%E9%97%B4.png)
  * 新生代
    由Eden Space和两个大小相同的Survivor组成
  * 老生代
    存放经过多次垃圾回收依然存活的对象

## 直接内存
它并不是虚拟机运行时数据区的一部分，也不是JAVA虚拟机规范中定义的内存区域

在JDK1.4中加入了NIO类，引入了一种基于通道(Channel)于缓冲区(Buffer)的I/O方式，它可以使用Native函数库直接分配堆外内存，然后通过一个存储在JAVA堆里面的DirectByteBuffer对象作为这块内存的引用进行操作。这样能在一些场景中显著提高性能，因为避免了在JAVA堆中和Native堆中来回复制数据

# 静态分派与动态分派

* 所有依赖静态类型来定位方法执行版本的分派动作称为静态分派.静态分派的典型应用是**方法重载**.静态分派发生在编译阶段,和虚拟机关系不大.编译器虽然能确定出方法,但是有时候不是唯一的,所以只能选择一个最好的版本
* 动态分派
  * 首先把调用者的引用放入栈顶
  * 查找操作数栈顶的第一个元素指向的对象的实际类型记做C
  * 递归在C及其父类中找到符号引用都相同的方法调用
  * 字节码中,分为方法表中还带有一个Code属性表.方法表存放方法签名及其他信息,Code属性表存放代码
* 虚方法表
  * 由于动态分派是非常频繁的动作,而且动态分派需要再类的元数据中搜索合适的目标方法,因此在虚拟机的实际实现中是通过建立虚方法表的方法实现的
  * 虚方法表中存放着各个方法的实际入口地址.子类没有重写的方法都直接指向父类的方法入口,这样就避免了一层一层的向上查.
  * 为了程序实现上的方便,具有相同签名的方法,在父子类的虚方法表中都应该具有相同的索引号.这样对象类型变换的时候,只要切换虚方法表就行了.不用从头搜索一遍


# 类加载过程
类的整个生命周期包括 加载 -> 验证 -> 准备 -> 解析 -> 初始化 -> 使用 -> 卸载
## 触发加载
* 触发的意思是如果没有加载就加载
* 遇到new 或者访问类的静态成员的时候,就会触发
* 反射的时候
* 初始化某类的子类时候
* 虚拟机启动的主类

### 加载

加载就是类的二进制数据就存放在方法区中.并且内存中会创建一个Class对象(不一定是在堆中,Hotspot就把它放在方法区中)
### 验证

验证是保证虚拟机中存放的字节流符合当前Class文件格式的规范,比如开头魔数,版本号,是否实现父类方法等

### 准备

为类变量分配内存并设置初始值的阶段.这里的初始值指的是0,'\0',null这种

### 解析

把常量池的符号引用变成直接引用(因为都已经从类文件加载到内存了,所以已经有了确切的地址)

### 初始化

生成`<cinit>`方法,执行静态块和静态变量的赋值,虚拟机会保证这一步的同步,这也是单例模式可以这么写的原因


## **类加载器包括**

### **Bootstrap ClassLoader**
加载JACA_HOME\lib，或者被-Xbootclasspath参数限定的类

* 不可扩展，不可以被程序员引用

### **Extension ClassLoader**
加载\lib\ext，或者被java.ext.dirs系统变量指定的类

### **Application ClassLoader**
ClassPath中的类库

## **双亲委派模型**
![](http://7xntdm.com1.z0.glb.clouddn.com/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8.png)
除了顶层的Bootstrap ClassLoader外，每一个类都拥有父类

* 父子关系由组合（不是继承）来实现

**工作流程**

* 类加载器收到类加载请求，首先将请求委派给父类加载器完成（所有加载请求最终会委派到顶层的Bootstrap ClassLoader加载器中）
* 如果父类加载器无法完成这个加载（该加载器的搜索范围中没有找到对应的类），子类尝试自己加载（直接委派到Bootstrp ClassLoader中）

**好处**

* 优先级的层次关系
* 同一个全限定名可以得到同一个类的实例(但是自己实现的类加载器的loadclass方法需要调用super.loadclass)

# Jvm的GC

## 判断垃圾对象的方法

* 引用计数
  * 容易造成循环引用问题
* 可达性分析
  * 分析的起点有:
    * 常量区引用对象
    * 虚拟机栈引用的对象
    * 本地方法栈(JNI)引用的对象
    * 方法区静态变量引用的对象

## 空间维度上的GC算法分类

### 标记-清除

#### 步骤

* 首先标记出需要回收的对象
* 标记完成后统一回收所有被标记的对象

#### 存在的问题

* 标记和清除两个过程本身效率并不高
* 清除之后会产生大量的内存碎片,可能会导致后面分配大对象的时候无法找到连续的空间从而又去触发GC

### 标记-整理(常见于老年代)

* 老年代的对象特点是生存周期长,要运行复制算法需要有较大的内存"担保"

### 标记-复制(常见于新生代)

现代的商业虚拟机基本都是使用这种收集方法来回收新生代.

#### 步骤

* 把可用容量分为两块,每次只使用一块.
* 当其中的一块用完了,就按顺序把还存活的对象排列到另一块上面
* 然后一次性清理原来的那一块


- 一般而言，内存分为一块较大的Eden与两个较小的Survivor(8:1:1)
- 新生代中98%的对象都是朝生夕死的,每次使用Eden和一块Survivor.当回收时,将Eden和Survivor中的对象分配到另一块Survivor上,这里如果不够用,需要老年代提供"分配担保"

#### 问题

* 适用于短生存期的对象，不停的来回复制长生存期的对象则导致效率降低
* 只能使用内存的一半,有点浪费

### 标记-整理(常见于老年代)

- 老年代的对象特点是生存周期长,要运行复制算法需要有较大的内存"担保"
- 标记过程和"标记-清除"算法一样,但后续步骤不是直接对可回收对象进行清理,而是让所有存活的对象都向一端移动,然后清理掉端内存以外的内存

### 分代回收

* 把内存区域分为老年代和新生代
* 老年代存放存活期长的对象
* 新生代存放存活期短的对象

## 时间维度上的GC算法分类

#### 串行回收

- 用单线程处理所有垃圾回收工作，因为无需多线程交互，所以效率比较高
- 无法使用多处理器的优势，所以此收集器适合单处理器机器

![](http://7xntdm.com1.z0.glb.clouddn.com/%E4%B8%B2%E8%A1%8C%E5%9B%9E%E6%94%B6.jpg)

#### 并行回收

是多线程处理所有垃圾回收工作，可以利用多核处理器的优势

- 对于空间不大的区域（如young generation），采用并行收集器停顿时间很短，回收效率高，适合高频率执行
- 如果线程数量过多，导致线程之间频繁调度，也会影响性能
- 一般并行收集的线程数是处理器的个数

![](http://7xntdm.com1.z0.glb.clouddn.com/%E5%B9%B6%E8%A1%8C%E5%9B%9E%E6%94%B6.jpg)

#### 并发回收

并发时GC线程和应用线程大部分时间是并发执行，只是在初始标记（initial mark）和二次标记（remark）时需要stop-the-world，这可以大大缩短停顿时间（pause time），所以适用于响应时间优先的应用，减少用户等待时间。

- 由于GC是和应用线程并发执行，只有在多CPU场景下才能发挥其价值，在一个N个处理器的系统上，并发收集部分使用K/N个可用处理器进行回收，一般情况下1<=K<=N/4
- 在执行过程中还会产生新的垃圾**floating garbage（浮动垃圾）**，如果等空间满了再开始GC，那这些新产生的垃圾就没地方放了（并发收集器一般需要20%的预留空间用于这些浮动垃圾），这时就会启动一次串行GC，等待时间将会很长，所以要在空间还未满时就要启动GC
- mark和sweep操作会引起很多碎片，所以间隔一段时间需要整理整个空间，否则遇到大对象，没有连续空间也会启动一次串行GC
- 用此收集器，收集频率不能大，否则会影响到cpu的利用率，进而影响吞吐量

![](http://7xntdm.com1.z0.glb.clouddn.com/%E5%B9%B6%E5%8F%91%E5%9B%9E%E6%94%B6.jpg)

# 几种常见的垃圾收集器

![](http://7xntdm.com1.z0.glb.clouddn.com/%E5%9E%83%E5%9C%BE%E6%94%B6%E9%9B%86%E5%99%A8.jpg)

## CMS收集器

### 步骤：

1. 初始标记
   - Stop the world：用户不可见的情况下，把用户的正常进程全部停掉
   - 仅标记GC Roots能直接关联的对象
2. 并发标记
   - 进行GC Roots Tracing过程（时间特别长）
3. 重新标记
   - Stop the world
   - 修正并发标记期间变化的记录
4. 并发清除
   - 清除资源（时间特别长）

### 缺点

- CPU资源敏感
- 无法处理浮动垃圾（伴随着程序运行产生的垃圾，在下次GC时清除）
- 处理完后有大量空间碎片

## G1收集器

### G1原理与CMS类似

在CMS的基础上：

1. 进行空间整合
   - 将JVM堆划分为多个大小相等的独立区域(Region)
   - 新生代与老年代不再是物理隔离，他们都是Region的集合
2. 建立可预测的停顿时间模型
   - 有计划的避免在整个JAVA堆中进行全区域的垃圾收集(使用Remember Set避免全堆扫描)
   - 可以跟踪Region垃圾价值，优先回收最大的Region

### 具体实现

堆内存被划分为多个大小相等的 heap 区, 每个heap区都是逻辑上连续的一段内存 (virtual memory)。其中一部分区域被当成老一代收集器相同的角色 (eden, survivor, old), 但每个角色的区域个数都不是固定的。这在内存使用上提供了更多的灵活性

1. 标记阶段完成后,G1就可以知道哪些heap区的empty空间最大。它会首先回收这些区,通常会得到大量的自由空间——这也是为什么这种垃圾收集方法叫做Garbage-First(垃圾优先)的原因
2. 顾名思义, G1将精力集中放在可能布满可收回对象的区域, 可回收对象(reclaimable objects)也就是所谓的垃圾.
3. G1使用暂停预测模型(pause prediction model)来达到用户定义的目标暂停时间,并根据目标暂停时间来选择此次进行垃圾回收的heap区域数量.

### 优点

* 紧凑的空闲内存区间
* 没有很长的GC停顿时间