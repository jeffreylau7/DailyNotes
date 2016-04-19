# Handler
* Handler创建的那个线程会在构造函数里面调Looper.myLooper().然后会在Threadlocal里面找到当前线程的looper产生关联.
* 一个looper对应一个MessageQueue,looper在构造函数里面创建MessageQueue.
* looper调用了loop()就会循环在Message里面取消息
* handler发送Message,massage的target会设置为发送它的handler,然后放进Messagequeue.最后被queue创建的线程的looper取出分发给handler的handlemessage()方法

## HandlerThread

* HandlerThread的getLooper()方法保证一定可以拿到非空返回值.如果Looper还没创建就一直wait()
* 可以防止像子线程发消息的时候,子线程的handler创建的代码还没执行的情况.保证一定可以收到消息

# AsyncTask原理

* excute()方法中首先直接调用preExcute()方法
* AsyncTask的构造方法构造了mWorkder这个WorkerRunnable对象.再用mWorker构造一个FutureTask对象丢到线程池里面去执行
* AsyncTask的成员变量有个InternalHandler,构造的时候进行初始化.
* FutureTask转调mWorker的run方法最后在线程池的子线程调用doInBackGround()方法.
* 然后FutureTask调用setResult()方法把运行结果返回,完成后调用FutureTask的done方法.这里面用InternalHandler发了个消息给主线程,最后拿到结果调用 finish()

# 事件分发机制

* 对于一个根ViewGroup来说,发生点击事件首先调用dispatchTouchEvent
* 如果这个ViewGroup的onIterceptTouchEvent返回true就表示它要拦截当前事件,接着这个ViewGroup的onTouchEvent就会被调用.如果onIterceptTouchEvent返回false,那么就会继续向下调用子View的dispatchTouchEvent方法
* 当一个View需要处理事件的时候,如果它没有设置onTouchListener,那么直接调用onTouchEvent.如果设置了Listenter 那么就要看Listener的onTouch方法返回值.为true就不调,为false就调onTouchEvent().
* View的默认实现会在onTouchEvent里面把touch事件解析成Click之类的事件
* 点击事件传递顺序 Activity -> Window -> View
* 一旦一个元素拦截了某事件,那么一个事件序列里面后续的Move,Down事件都会交给它处理.并且它的onInterceptTouchEvent不会再调用
* View的onTouchEvent默认都会消耗事件,除非它的clickable和longClickable都是false(不可点击)
* View的enable属性不影响OnTouchEvent的返回值

# Activity启动模式

任务栈是一种后进先出的结构,当按下back按钮的时候,栈内的Activity会一个一个的出栈,如果栈内没有Activity,那么系统就会回收这个栈,每个APP默认只有一个栈,以APP的包名来命名.
* standard : 标准模式,每次启动Activity都会创建一个新的Activity实例,而不管这个Activity是否已经存在.Activity的启动三回调都会执行.
* singleTop : 栈顶复用模式.这种模式下,如果新Activity已经位于任务栈的栈顶,那么此Activity不会被重新创建,所以它的启动三回调就不会执行,同时它的onNewIntent方法会被回调.如果Activity已经存在但是不在栈顶,那么新的Activity仍然会重新创建.
* singleTask: 栈内复用模式.创建这样的Activity的时候,系统会先确认它所需任务栈已经创建,否则先创建任务栈.然后放入Activity,如果栈中已经有一个Activity实例,那么这个Activity就会被调到栈顶,并运行onNewIntent(),并且singleTask会清理在当前Activity上面的所有Activity.(clear top)
* singleInstance : 加强版的singleTask模式,这种模式的Activity只能单独位于一个任务栈内,由于栈内复用的特性,后续请求均不会创建新的Activity,除非这个独特的任务栈被系统销毁了

* Activity的堆栈管理以ActivityRecord为单位,所有的ActivityRecord都放在一个List里面.可以认为一个ActivityRecord就是一个Activity栈

* **TIP** 
  *  用intent启动时指定的启动模式比写在menifest中的优先级要高.
  *  当TaskAffinity和allowTaskReparenting结合的时候,这种情况比较复杂,会产生特殊的效果.当一个应用A启动了应用B的某个Activity后,如果这个Activity的allowTaskReparenting为true的话,那么B启动后,那么这个新的Activity会转到B的任务栈中.

# service的生命周期 2种启动的区别

* startService() 启动本地服务`Local Service`
* bindService() 启动远程服务`Remote Service`,bindService()可以绑定一个已有的service,如果bind的service还没启动,就先启动然后再bind()
* 远程服务允许暴露接口并让系统内不同程序相互注册调用。LocalService无法抵抗一些系统清理程序如MIUI自带的内存清除

* Service在被创建之后都会进入回调onCreate()方法，随后根据启动方式分别回调onStartCommand()方法和onBind()方法。如果Service是经由bindService()启动，则需要所有client全部调用unbindService()才能将Service释放等待系统回收,最后onDestroy()

* 让Service不运行在主线程,可以在子线程内开启Service.

# 如何保证Service不被杀
**onStartCommand方法，返回START_STICKY**
  * 手动返回START_STICKY，亲测当service因内存不足被kill，当内存又有的时候，service又被重新创建，比较不错，但是不能保证任何情况下都被重建，比如进程被干掉了
  * StartCommond几个常量参数简介：
    1、START_STICKY
    在运行onStartCommand后service进程被kill后，那将保留在开始状态，但是不保留那些传入的intent。不久后service就会再次尝试重新创建，因为保留在开始状态，在创建     service后将保证调用onstartCommand。如果没有传递任何开始命令给service，那将获取到null的intent。
    * 【结论】: 手动返回START_STICKY，亲测当service因内存不足被kill，当内存又有的时候，service又被重新创建，比较不错，但是不能保证任何情况下都被重建，比如进程被干掉了....

**提升service优先级**
 * 在AndroidManifest.xml文件中对于intent-filter可以通过android:priority = "1000"这个属性设置最高优先级，1000是最高值，如果数字越小则优先级越低，同时适用于广播。
 * 【结论】目前看来，priority这个属性貌似只适用于broadcast，对于Service来说可能无效

**提升service进程优先级**
 * Android中的进程是托管的，当系统进程空间紧张的时候，会依照优先级自动进行进程的回收
 * 当service运行在低内存的环境时，将会kill掉一些存在的进程。因此进程的优先级将会很重要，可以在startForeground使用startForeground 将service放到前台状态。这样在低内存时被kill的几率会低一些。
 * 【结论】如果在极度极度低内存的压力下，该service还是会被kill掉，并且不一定会restart

**onDestroy方法里重启service**
 * service +broadcast  方式，就是当service走ondestory的时候，发送一个自定义的广播，当收到广播的时候，重新启动service
 * 也可以直接在onDestroy（）里startService
 * 【结论】当使用类似口口管家等第三方应用或是在setting里-应用-强制停止时，APP进程可能就直接被干掉了，onDestroy方法都进不来，所以还是无法保证

**Application加上Persistent属性**
 * 看Android的文档知道，当进程长期不活动，或系统需要资源时，会自动清理门户，杀死一些Service，和不可见的Activity等所在的进程。但是如果某个进程不想被杀死（如数据缓存进程，或状态监控进程，或远程服务进程），可以这么做
   ```XML
    <application
        android:name="com.test.Application"
        android:allowBackup="true"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:persistent="true"
        android:theme="@style/AppTheme" >
    </application>
   ```
   * 【结论】据说这个属性不能乱设置，不过设置后，的确发现优先级提高不少，或许是相当于系统级的进程，但是还是无法保证存活

**监听系统广播判断Service状态**
 * 通过系统的一些广播，比如：手机重启、界面唤醒、应用状态改变等等监听并捕获到，然后判断我们的Service是否还存活，别忘记加权限
 * 【结论】这也能算是一种措施，不过感觉监听多了会导致Service很混乱，带来诸多不便

**root之后放到system/app变成系统级应用**

**大招: 放一个像素在前台(手机QQ)**

# Activity生命周期
* 启动Activity：
  onCreate()—>onStart()—>onResume()，Activity进入运行状态。
* Activity退居后台：
  当前Activity转到新的Activity界面或按Home键回到主屏：
  onPause()—>onStop()，进入停滞状态。
* Activity返回前台：
  onRestart()—>**onStart()**—>onResume()，再次回到运行状态。
* Activity退居后台，且系统内存不足，
  系统会杀死这个后台状态的Activity，若再次回到这个Activity,则会走onCreate()–>onStart()—>onResume()
* 锁定屏与解锁屏幕
  **只会调用onPause()，而不会调用onStop方法，开屏后则调用onResume()**

# Activty被系统杀死如何保存数据恢复数据
主要是两个方法,一个是onSaveInstanceState()和onRestoreInstanceState()

当系统调用onSaveInstanceState()的时候,可以把状态存在bundle里面.然后如果重启的话,oncreate(bundle)参数就不会为空,会收到前面保存的信息.同时onRestoreInstanceState(bundle)也会调用,也可以在bundle里面恢复数据

## onSaveInstanceState()被执行的场景有哪些：
系统不知道你按下HOME后要运行多少其他的程序，自然也不知道activity A是否会被销毁，因此系统都会调用onSaveInstanceState()，让用户有机会保存某些非永久性的数据。以下几种情况的分析都遵循该原则
* 当用户按下HOME键时
* 长按HOME键，选择运行其他的程序时
* 锁屏时(注意的生命周期只会调用pause和resume)
* 从activity A中启动一个新的activity时
* 屏幕方向切换时

# 横竖屏切换activty的生命周期
onPause -> onSaveInstanceState ->  onStop -> onDestroy -> onCreate -> onStart -> onRestoreInstanceState -> onResume

不想切换解决办法: 

* 写死横竖屏
  * menifest配置screenorientation属性
* 配置configChanges:orientation|screensize|keyboardhidder

# Fragment生命周期



![](http://7xntdm.com1.z0.glb.clouddn.com/fragment_lifecycle.png)

注意和Activity的相比的区别,按照执行顺序

* onAttach(),onDetach()
* onActivityCreated()
* onCreateView(),onDestroyView()

# Android程序运行时权限和文件系统权限的区别
运行时权限 Dalvik(Android授权)
文件系统权限(linux 内核授权)

# Android布局优化

## `<include>`标签

可以直接用这个标签导入同一个整个的布局文件,复用这个布局文件就不用每次都重新写了

**TIP:**注意使用findViewbyId的时候要用`include`标签的ID,而**不是**被include的那个布局的根id.否则会find出空指针.

## `<Merge>`标签

* `<merge>`是一个Activity


* 使用`<Merge>`标签,会直接把`<merge>`标签引入的布局的外层ViewGroup去掉,直接把外层ViewGroup的儿子放到`<merge>`的父亲上去
* 查看源码可以看出,源码中解析的时候发现如果 name == TAG_MERGE.就拿出儿子放到自己的父亲上去

## `<ViewStub />`标签

ViewStub是一个宽高都为0的一个View，它默认是不可见的，只有通过调用setVisibility函数或者Inflate函数才会将其要装载的目标布局给加载出来，从而达到延迟加载的效果，这个要被加载的布局通过android:layout属性来设置。例如我们通过一个ViewStub来惰性加载一个消息流的评论列表，因为一个帖子可能并没有评论，此时我可以不加载这个评论的ListView，只有当有评论时我才把它加载出来，这样就去除了加载ListView带来的资源消耗以及延时

* 要使用findViewById方法来获取被ViewStub加载的布局,ID属性不能用本来的ID,而是应该使用`<ViewStub />`标签的`InflateId`属性设置的ID.如果没有设置`InfalateId`,那么就可以直接这么做
* ViewStub这个View的inflate()方法的返回值其实也就是内部的View.可以直接使用
* 源码中当setVisibility()或者inflate()时会加载布局,首先会获取`InfalateId`,如果这个值设置了就把他设置为加载的布局的根元素的ID,没有设置就用里面布局元素的根ID.

# Android中内存泄漏
* 类的静态变量持有大数据对象
  静态变量长期维持到大数据对象的引用，阻止垃圾回收。
* 非静态内部类存在静态实例
  非静态内部类会维持一个到外部类实例的引用，如果非静态内部类的实例是静态的，就会间接长期维持着外部类的引用，阻止被回收掉。
* 资源对象未关闭
  资源性对象比如（Cursor，File文件等）往往都用了一些缓冲，我们在不使用的时候，应该及时关闭它们， 以便它们的缓冲及时回收内存。它们的缓冲不仅存在于java虚拟机内，还存在于java虚拟机外。 如果我们仅仅是把它的引用设置为null,而不关闭它们，往往会造成内存泄露。
  **解决办法**： 比如SQLiteCursor（在析构函数finalize（）,如果我们没有关闭它，它自己会调close()关闭）， 如果我们没有关闭它，系统在回收它时也会关闭它，但是这样的效率太低了。 因此对于资源性对象在不使用的时候，应该调用它的close()函数，将其关闭掉，然后才置为null. 在我们的程序退出时一定要确保我们的资源性对象已经关闭。 程序中经常会进行查询数据库的操作，但是经常会有使用完毕Cursor后没有关闭的情况。如果我们的查询结果集比较小， 对内存的消耗不容易被发现，只有在常时间大量操作的情况下才会复现内存问题，这样就会给以后的测试和问题排查带来困难和风险，记得try catch后，在finally方法中关闭连接
* Android 3.0 之前的Bitmap需要调用recycle()方法手动回收,因为是放在C堆中的.Android3.0之后放在Java堆中,GC机制会帮我们处理
* Handler内存泄漏
  见下面

# Handler内存泄漏
发生的原因:
当使用一般的内部类来创建handler的时候.Handler对象会持有Activity的引用.
此时如果有delayed的Message就会有这样的引用链条.
Message queue ---> DelayedMessage --> handler(message.target) --> Activity(内部类隐式引用)
或者子线程中的延时任务(因为还在虚拟机栈里面,可达性分析的起点)持有着Handler的引用同理.

预防的方法:
* 在Activity退出的时候关闭子线程,或者Handler的removeCallbacks()把消息清理掉
* 把隐式的强引用转成显式的**弱引用**
  * 使用静态内部类,静态内部类不隐式持有外部引用
  * 显式的写一个Activity的WeakReference<Activity>


# Oom 是如何解决?
1. 使用更加轻量的数据结构
   例如，我们可以考虑使用ArrayMap/SparseArray而不是HashMap等传统数据结构。通常的HashMap的实现方式更加消耗内存，因为它需要一个额外的实例对象来记录Mapping操作。另外，SparseArray更加高效，在于他们避免了对key与value的自动装箱（autoboxing），并且避免了装箱后的解箱。

2. 避免在Android里面使用Enum
   Android官方培训课程提到过“Enums often require more than twice as much memory as static constants. You should strictly avoid using enums on Android.”，具体原理请参考《Android性能优化典范（三）》，所以请避免在Android里面使用到枚举。不仅dexcode的大小会增加,连运行时内存也要占用更多

3. 减小Bitmap对象的内存占用
   Bitmap是一个极容易消耗内存的大胖子，减小创建出来的Bitmap的内存占用可谓是重中之重，，通常来说有以下2个措施：
   inSampleSize：缩放比例，在把图片载入内存之前，我们需要先计算出一个合适的缩放比例，避免不必要的大图载入。
   decode format：解码格式，选择ARGB_6666/RBG_545/ARGB_4444/ALPHA_6，存在很大差异

4. Bitmap对象的复用
   缩小Bitmap的同时，也需要提高BitMap对象的复用率，避免频繁创建BitMap对象，复用的方法有以下2个措施
   LRUCache : “最近最少使用算法”在Android中有极其普遍的应用。ListView与GridView等显示大量图片的控件里，就是使用LRU的机制来缓存处理好的Bitmap，把近期最少使用的数据从缓存中移除，保留使用最频繁的数据，
   inBitMap高级特性:利用inBitmap的高级特性提高Android系统在Bitmap分配与释放执行效率。使用inBitmap属性可以告知Bitmap解码器去尝试使用已经存在的内存区域，新解码的Bitmap会尝试去使用之前那张Bitmap在Heap中所占据的pixel data内存区域，而不是去问内存重新申请一块区域来存放Bitmap。利用这种特性，即使是上千张的图片，也只会仅仅只需要占用屏幕所能够显示的图片数量的内存大小

5. 使用更小的图片
   在涉及给到资源图片时，我们需要特别留意这张图片是否存在可以压缩的空间，是否可以使用更小的图片。尽量使用更小的图片不仅可以减少内存的使用，还能避免出现大量的InflationException。假设有一张很大的图片被XML文件直接引用，很有可能在初始化视图时会因为内存不足而发生InflationException，这个问题的根本原因其实是发生了OOM。

6. StringBuilder
   在有些时候，代码中会需要使用到大量的字符串拼接的操作，这种时候有必要考虑使用StringBuilder来替代频繁的“+”。

7. 避免在onDraw方法里面执行对象的创建
   类似onDraw等频繁调用的方法，一定需要注意避免在这里做创建对象的操作，因为他会迅速增加内存的使用，而且很容易引起频繁的gc，甚至是内存抖动。

8. 避免对象的内存泄露,参考内存泄漏分段
9. 在Aexi中,对重复使用的对象引入了池,也是一种享元设计模式

# 用SparseArray 和 ArrayMap替换HashMap

HashMap会造成空间浪费主要是因为,在扩容的时候执行

```java
int newCapacity = oldCapacity * 2;
```

只要一满足扩容条件，HashMap的空间将会以2倍的规律进行增大。假如我们有几十万、几百万条数据，那么HashMap要存储完这些数据将要不断的扩容，而且在此过程中也需要不断的做hash运算，这将对我们的内存空间造成很大消耗和浪费

## SparseArray

* SparseArray使用两个数组来进行数据存储，一个存储key，另外一个存储value，为了优化性能，它内部对数据还采取了压缩的方式来表示稀疏数组的数据,从而节约内存空间.


* SparseArray的key只能是int,避免了自动装箱


* SparseArray的Key都是按顺序排好的,查找key的时候使用二分查找


* SparseArray扩容采用GrowingArrayUtil.insert(),猜想他的扩容方式是直接copy数组,比每个元素都重新计算hash要快的多
* SparseArray删除元素会把garbage赋值为true.当put内存不够的时候会先调用GC()进行数组收缩方法,用时间换空间

## ArrayMap

* ArrayMap中有hashcode和key以及value

```Java
mHashes[index] = hash;
mArray[index<<1] = key; //mArray
mArray[(index<<1)+1] = value;
```

* 除了存储方式的差异,ArrayMap其他的地方和SparseArray都很相似,ArrayMap中也有数组收缩机制

## 效率的提升和使用限制

SparseArray和ArrayMap的使用，对于索引是整数的情景，有时能带来一些效率的提升。

1. 减少了hashCode时间消耗
2. 减小了所使用的内存大小。

但在所管理的对象数量很大时，效率却反而有可能更低：

1. 在插入的时候，有可能导致大段数组的复制;
2. 在删除之后，也有可能导致数组的大段元素被按个移动（不是复制数组，而是一个一个单独移动）;
3. 索引的映射，采用了二分查找，时间复杂度为O(logn)

# 进程优先级

* 空进程
  * 不运行任何四大组件(一般是Activity)的进程
* 后台进程
  * 运行一个当前不可见(onStop()方法被调用)的Activity
  * 所以一般不在Activity里面写需要后台运行的代码,如下载或者播放音乐等
* 服务进程
  * 运行着一个由startService()启动的服务的进程
  * 服务进程虽然在第三级,但是已经很难被杀死了.就算被杀死,内存充足的时候还会回来
* 可见进程
  * 运行着可见,但是没有焦点(onPause())的Activity的进程
  * 运行着一个被可见进程绑定的Service
* 前台进程
  * 运行着用户正在交互的Activity的进程(onResume())
  * 运行着被某前台Activity所在进程绑定的Service
  * 调用了startForeground()方法运行在所谓"前台"的服务所在进程
  * Service的生命周期方法调用的瞬间所在的进程

# Linux系统的IPC

**线程之间不存在通信,因为本来就共享同一片内存**

各个线程可以访问进程中的公共变量，资源，所以使用多线程的过程中需要注意的问题是如何防止两个或两个以上的线程同时访问同一个数据，以免破坏数据的完整性。数据之间的相互制约包括
1、直接制约关系，即一个线程的处理结果，为另一个线程的输入，因此线程之间直接制约着，这种关系可以称之为同步关系
2、间接制约关系，即两个线程需要访问同一资源，该资源在同一时刻只能被一个线程访问，这种关系称之为线程间对资源的互斥访问，某种意义上说互斥是一种制约关系更小的同步

## 线程间同步

* 临界区
  * 每个线程中访问临界资源的代码,一个线程拿到临界区的所有权后,可以多次重入.只有前一个线程放弃,后一个才可以进来


* 互斥量
  * 互斥量就是简化版的信号量
  * 互斥与临界区很相似，但是使用时相对复杂一些（互斥量为内核对象），不仅可以在同一应用程序的线程间实现同步，还可以在不同的进程间实现同步，从而实现资源的安全共享。
  * 互斥量由于也有线程所有权的概念，故也只能进行线程间的资源互斥访问，不能由于线程同步
  * 由于互斥量是内核对象，因此其可以进行进程间通信，同时还具有一个很好的特性，就是在进程间通信时完美的解决了"遗弃"问题


* 信号量
  * 信号量的用法和互斥的用法很相似，不同的是它可以同一时刻允许多个线程访问同一个资源，PV操作
  * 信号量也是内核对象,也可以进行进程间通信

## 进程间通信

* 管道
  * 半双工,数据只能单向流动
  * 只能在具有父子关系的进程间使用
  * FIFO的共享内存实现
* 命名管道
  * 以linux中的文件的形式存在
  * 不要求进程有用父子关系,甚至通过网络也是可以的
  * 半双工
* 信号量
  * 有up和down操作
  * 共享内存实现


* 消息队列
  * 队列数据结构
  * 共享内存实现
* 信号
  * 较为复杂的方式,用于通知进程某事件已经发生.例如kill信号


* 共享内存
  * 将同一个物理内存附属到两个进程的虚拟内存中
* socket
  * 可以跨网络通信

# Android系统的IPC

* Bundle

  Bundle本身是Parceable的,要求使用Bundle的对象也必须是可序列化的

* 文件共享

* Messenger

  对AIDL的一层封装

* AIDL

  AIDL主要是解决两边的Binder不在同一个工程里,开始的时候没有统一接口的问题

  * 服务端创建一个Service用来监听客户端的连接请求,然后创建一个AIDL文件,将暴露给客户端的接口在AIDL中声明,最后在Service中实现这个AIDL.
  * 客户端绑定服务端的Service,然后将服务端返回的binder转成客户端公开的AIDL接口所属类型
* Binder

  见下面的

# Binder机制

* 在Android启动的时候，Zygote进程孵化出第一个子进程叫SystemServer，而在这个进程中，很多系统提供的服务，比如ActivityManagerSerivce, PowerManagerService等，都在此进程中的某一条线程上运行
* SystemServer和ServiceManager都是单独的进程.ServiceManger为Client提供名字到句柄(系统空间的索引,使用数字标识,对应用户空间的引用)的查询
* ServiceManager在Binder驱动中的句柄永远是0
* Android启动ServiceManager进程的时候,做了这些事
  1. 首先打开“/dev/binder”设备文件，映射内存
  2. 利用BINDER_SET_CONTEXT_MGR命令，令自己成为上下文管理者，其实也就是成为ServiceManager。
  3. 进入一个无限循环，等待Client的请求到来
* Binder驱动通过命令BINDER_SET_CONTEXT_MGR使某个进程成为ServiceManager.BINDER_SET_CONTEXT_MGR就是某某进程告诉驱动，它想成为ServiceManager。而只要它是第一个发送这个命令的，它就是ServiceManager了
* Binder驱动是一段运行在内核空间的代码，通过一个叫"/dev/binder"的文件在内核空间和用户空间来回搬数据.因为作为驱动的形式运行在内核空间,所以他可以为不同的进程搬运数据.它负责Binder节点的建立，负责Binder在进程间的传递，负责对Binder引用的计数管理，在不同进程间传输数据包等底层操作
* 每一个提供服务的Server都会通过Binder驱动，将自身给注册到ServiceManager中，方便众多想获取服务的Client可以去ServiceManager找到自己."通过Binder驱动"本质上就是Server们会将自身作为一个对象，封装在数据包中，将这些数据复制到内核空间中由Binder驱动访问.而Binder驱动读取数据包的时候，如果发现其中有Binder实体，类似ServiceManager那样的服务提供商，那么也会为对应的Binder实体创建对应的Binder节点（BinderNode）.这些节点位于Server所属的进程内。Binder驱动也会为这些服务分配句柄（大于0），同时会将这些句柄也记录在Binder驱动中，然后再将这些句柄和名字发送给ServiceManager，由ServiceManager来维护


* 也就是说，Server们通过和Binder驱动通信，Binder驱动做了如下两件事：
  1. 在内核空间中创建了一个Binder节点，隶属于Server们的进程。
  2. 驱动为这些Binder节点分配一个大于0的包柄，将这些句柄和名字发送给ServiceManager。

## Binder通信的全过程

1. Server启动了，他要创建一个Binder实体，它的句柄是0，比如BpBinder(0)，当设置了BINDER_SET_CONTEXT_MGR，驱动收到这个命令，它就知道是要将当前进程设置为ServiceManager，于是它就会当前这个Binder实体创建一个Binder节点（BinderNode），它在这里记录了 0 -> ServiceManager这样的mapping。

2. 另一个Server启动了，它也会创建一个Binder实体，名字叫 XXXManagerService吧，但是它的句柄就不会是0了，是什么呢？不知道，当它这个Binder实体放到驱动中，驱动同时也会为其创建一个Binder节点，并且为其随机创建一个句柄，比如就叫 1 吧。

3. 它进入驱动是为了寻找句柄0，去注册自己，也就是addService。当然，驱动就会找到句柄0，发现其对应的是ServiceManager，就会将对应的数据，让ServiceManager来进行处理，这当然利用的就是内核空间的内存了。在ServiceManager中，有一个服务列表svclist，保存了不同的服务名称对应的句柄，比如ServiceManager就会在自己的空间中保存这样一个对应  1-  > “XXXManagerService”。到这里，XXXManagerService就将自己注册到ServiceManager中了。
4. 有一个Client也启动了，它要找XXXManagerService请求点资源，但是它只知道XXXManagerService的名字，而不知道其具体的句柄，不同进程之间，咫尺就是天涯啊。但是它知道有个叫ServiceManager的东西，它可能知道XXXManagerService在哪里，刚好，它知道它句柄就是0，于是它就去驱动中找一个句柄为0的，找到了，跟他通信，说它要找XXXManagerService，也就是findService。
5. 刚好XXXManagerService注册了，ServiceManager就将其句柄传给了Client，现在Client终于知道XXXManagerService的句柄了，于是它就又重新包装好数据，这次的句柄就不是0了，而是 1 了，又一头扎进驱动了，继续做该做的事情。

## AIDL

* ALDL本质上是ADT或者AS为了简化我们的Binder操作生成的.懂得了原理,不使用AIDL仍然可以进行进程间通信.

* 服务把自己的功能用接口提供出来,屏蔽自己的内部实现.但是客户端和服务端不在一个工程里面,不能直接导服务端的包

* 定义AIDL之后,会生成相对应的接口类,接口类中定义了Stub类.Stub类是一个抽象类，继承了Binder类，并且实现了ITestService。其实这就是在我们定义服务端服务时需要去实现的Binder类，也就是说，当我们创建一个服务，并且希望这个服务能够被跨进程使用的话，我们就可以在我们的服务中去实现这样一个Stub类，在其定义的方法中去实现对应的逻辑，

* Stub类中呢，又定义了一个Proxy类，同样也实现了ITestService接口，所以对于客户端进程来说，其与Proxy通信，感觉就会像跟服务器端的Binder类通信一样，因为两边暴露出来的方法都是一样的，这也是设计模式中代理模式的一个非常典型的应用。

  而实际上Proxy类则是通过一个IBinder类型的mRemote对象来跟驱动进行交互，并将对应的数据信息通过驱动与服务端的进程进行交互.
* 服务端的是用Stub类，也即Binder对象，客户端的是使用Proxy类，虽然Proxy类中也是利用mRemote这个Binder接口，在具体的进程中，怎么判断拿哪个对象呢？到底是拿Stub类呢，还是拿Proxy类呢？
  ```java
  /** 
   * Cast an IBinder object into an com.lms.aidl.ITestService interface, 
   * generating a proxy if needed. 
   */  
  public static com.lms.aidl.ITestService asInterface(  
          android.os.IBinder obj) {  
      if ((obj == null)) {  
          return null;  
      }  
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);  
      if (((iin != null) && (iin instanceof com.lms.aidl.ITestService))) {  
          return ((com.lms.aidl.ITestService) iin);  
      }  
      return new com.lms.aidl.ITestService.Stub.Proxy(obj);  
  }
  ```

* 这是将一个IBinder接口的对象转变成我们定义的接口对象，在这里，我们可以发现，当某进程去将某IBinder接口对象转化成接口时，其会先去利用IBinder对象的queryLocalInterface方法去获取有没有本地的接口对象，也即Stub对象，如果没有的话，它就会创建一个Proxy对象.如果找不到的话，就说明那个Binder对象并不是在本进程内，那就是要进行进程间通信，那你是异进程，当然要创建一个Proxy对象，



* onTransact方法其实主要就是在用户空间和内核空间中进行数据的交换，也就是实现进程间数据的交互，从而来通知彼此应该要干什么事。

# 几种Context区别

* Activity和Service以及Application的Context是不一样的,Activity继承自ContextThemeWraper.其他的继承自ContextWrapper
* 每一个Activity和Service以及Application的Context都是一个新的ContextImpl对象
* Activity在创建的时候会new一个ContextImpl对象并在attach方法中关联它.Application和Service也差不多.这里使用的是外观设计模式.ContextWrapper的方法内部都是转调ContextImpl的方法
* 对于第三方应用，一个应用只存在一个Application对象，且通过getApplication和getApplicationContext得到的是同一个对象，两者的区别仅仅是返回类型不同
* 创建对话框传入Application的Context是不可以的

# MVC MVP MVVM

## 同

如果把这三者放在一起比较，先说一下三者的共同点，也就是Model和View：

- Model：数据对象，同时，提供本应用外部对应用程序数据的操作的接口，也可能在数据变化时发出变更通知。**Model不依赖于View的实现**，只要外部程序调用Model的接口就能够实现对数据的增删改查。
- View：UI层，提供对最终用户的交互操作功能，包括UI展现代码及一些相关的界面逻辑代码。

## 异

三者的差异在于如何粘合View和Model，实现用户的交互操作以及变更通知

- Controller

Controller接收View的操作事件，根据事件不同，或者调用Model的接口进行数据操作，或者进行View的跳转，从而也意味着一个Controller可以对应多个View。Controller对View的实现不太关心，只会被动地接收，Model的数据变更不通过Controller直接通知View，通常View采用观察者模式监听Model的变化。

- Presenter

Presenter与Controller一样，接收View的命令，对Model进行操作；与Controller不同的是Presenter会反作用于View，Model的变更通知首先被Presenter获得，然后Presenter再去更新View。一个Presenter只对应于一个View。根据Presenter和View对逻辑代码分担的程度不同，这种模式又有两种情况：Passive View和Supervisor Controller。

- ViewModel

注意这里的“Model”指的是View的Model，跟MVVM中的一个Model不是一回事。所谓View的Model就是包含View的一些数据属性和操作的这么一个东东，这种模式的关键技术就是数据绑定（data binding），View的变化会直接影响ViewModel，ViewModel的变化或者内容也会直接体现在View上。这种模式实际上是框架替应用开发者做了一些工作，开发者只需要较少的代码就能实现比较复杂的交互。

# Activity启动过程

* 辗转调用链 Context的startActivity() ActivityManagerService.getDefault()的startActivity()方法 Instrumentation的checkStartActivityResult() -> 继续调用到AMS的startActivity方法

  * Instrumentation允许你监听和系统和APP的交互
  * ActivityManagerNative.getDefault()方法实际上是一个Binder,具体实现就是AMS.这里采用了单例模式,AMS继承自ActivityManagerNative实现了IActivityManager这个Binder接口
  * AMS运行在System_Thread中

* ActivityStackSupervisor的realStartActivityLocked() -> app.thread.scheduleLaunchActivity

  * app.thread的类型为IApplicationThread

  * IApplicationThread继承了IInterface这样的Binder接口.这个接口定义了大量启动和停止Activity以及Service的方法

  * IApplicationThread的实现者是ActivityThread的内部类ApplicationThread.

    ApplicationThread继承自ApplicationThreadNative.ApplicationThreadNative继承了Binder并实现了IApplicationThread.所以ApplicationThread是IApplicationThread的最终实现者

* ApplicationThread调用scheduleLaunchActivity()方法.这个方法实现很简单就是发了一个消息给Handler H处理.H中辗转调用到performLaunchActivity()方法.

* performLaunchActivity()完成了这几件事
  * 从ActivityClientRecord中获取待启动的Activity的组件信息
  * Activity真正new出来是在performLaunchActivity()方法 **通过Instrumentation的newActivity方法使用反射创建Activity对象**
  * 通过LoadedApk的makeApplication方法尝试创建Application对象
  * 创建ContextImpl对象并通过Activity的attach方法来完成一些重要数据的初始化
  * 调用Activity的onCreate()方法
* 总结一下就是通过AMS这个Binder处理Activity栈做检查等信息,然后Handler回来performLaunchActivity方法初始化,onCreate()


# Android开机过程

* BootLoder引导,然后加载Linux内核.
* 0号进程init启动.加载init.rc配置文件,配置文件有个命令启动了zygote进程.
* zygote开始fork出SystemServer进程
* SystemServer加载各种JNI库,然后init1,init2方法,init2方法中开启了新线程ServerThread.
* 在SystemServer中会创建一个socket客户端，后续AMS（ActivityManagerService）会通过此客户端和zygote通信
* ServerThread的run方法中开启了AMS,还孵化新进程ServiceManager,加载注册了一溜的服务,最后一句话进入loop 死循环
* run方法的SystemReady调用resumeTopActivityLocked打开锁屏界面

# APP启动过程

![](http://7xntdm.com1.z0.glb.clouddn.com/activity_start_flow.png)

* 上图就可以很好的说明App启动的过程
* ActivityManagerService组织回退栈时以ActivityRecord为基本单位，所有的ActivityRecord放在同一个ArrayList里，可以将mHistory看作一个栈对象，索引0所指的对象位于栈底，索引mHistory.size()-1所指的对象位于栈顶
* Zygote进程孵化出新的应用进程后，会执行ActivityThread类的main方法.在该方法里会先准备好Looper和消息队列，然后调用attach方法将应用进程绑定到ActivityManagerService，然后进入loop循环，不断地读取消息队列里的消息，并分发消息。
* ActivityThread的main方法执行后,应用进程接下来通知ActivityManagerService应用进程已启动，ActivityManagerService保存应用进程的一个代理对象，这样ActivityManagerService可以通过这个代理对象控制应用进程，然后ActivityManagerService通知**应用进程**创建入口Activity的实例，并执行它的生命周期方法