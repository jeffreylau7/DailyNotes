## Android 是如何实现不同线程间的通信的？

> *By 刘杰辉 ( Jeffrey Lau ) [ [GitHub](https://github.com/jeffreylau7) ] [ [CSDN](http://blog.csdn.net/jeffreylau7) ]*

## 热身知识： ##

有图形界面的程序时候需要用一条线程（main thread）负责图形界面的定时绘制和检测事件触发，一些耗时的操作或者不定时的操作都是不建议在这条线程中运行的，因为这些操作会导致界面的刷新变慢，出现界面卡住的问题，在使用者的角度来看这是很糟糕的，严重影响使用者的使用体验。

所以耗时的和不定时的操作都在界面绘制线程意外的其他线程来运行。

但又有新问题了，如果在另外一条线程从网络上获取了数据，如何把这些数据传递到main thread 里面通过图形界面展示出来呢？

在 Android 里面可以通过 handler 来处理线程间的通信问题。

Handler、 Looper、 Message 这三者都与Android异步消息处理线程相关的概念。

在Android开发中是离不开多线程和线程间通信的，所以这部分知识很重要。



## Handler 是什么？ ##

在 Android 里面的 handler 是消息处理器的意思，可以发送消息和处理消息，形象得说就是在两条线程之间开了一条通道来传输数据。

## Message 是什么？ ##

在 Android 里面 message 就是存储数据的包裹，在 handler 这条通道上传输消息数据用的。

## Looper 是什么？ ##

刚才说了有一条线程专门是负责程序界面绘制的，这个线程是一个大循环。Looper 这个就是一个循环器，绘制图形的线程会主动创建一个 Looper 来监测有没有其他线程发来消息用的。其他线程需要自己手动创建一个Looper，Looper 里面有一个 MessageQueue（消息队列）就是一个存放消息用的队列。

> 知道队列是什么吗？
> 
> 队列就好像我们去饭堂打饭排队一样，一个跟着一个，先排队的可以先打饭。

MessageQueue 里面就是存放着从其他线程发送来的Message（消息包裹），Looper 会不断地从MessageQueue 中取出一个消息来处理。

那其他线程又是如何发送消息的？看下面

## Handler、 Looper、 Message 这三者是如何工作的呢？ ##

一个线程只可以有一个Looper，Looper 创建的时候会创建一个 MessageQueue 

- 主线程，也就是负责图形绘制的线程，在创建时会自己创建一个Looper
- 其他线程，需要自己手动去创建一个 Looper 

有了 Looper 了，但没向 MessageQueue 发送消息也没用啊。要发送和处理消息就需要创建Handler 了，哪个线程需要处理接收消息的就创建一个 Handler ，在创建 Handler 的时候会关联 Looper 的 MessageQueue 的，这样就把 Handler 和 Looper 联系起来了。 handler 里面有多中发送消息的方法，例如：sendMessage(Message msg)，发送的消息自然是 Mesaage 那； handler 里面还有一个方法是用来处理消息的 叫 handleMessage(Message msg)。

## 例子1： ##

##### 一个自定义线程开启获取网络数据，发送给主线程展示出来。

运行Activity会创建一个线程A（ ActivityThread ），线程A是负责图形绘制的，默认创建 Looper。

线程B 专门去获取网络数据的。

线程B如何把数据传给线程A呢？

1. 线程A 创建一个消息处理器 handlerA，复写 handleMessageA 方法，处理从线程B传来的数据并加载到UI中展示出来。


2. 创建线程B 并把 handlerA 的应用指针传给线程B


3. 线程B 获取了数据，把数据打包到 messageB 中，用 handlerA 的 sendMessage 方法把 messageB 发送出去，messageB 会被添加到 线程A 的 MessageQueueA 中


4. 线程A 的Looper 从 MessageQueueA 中取到 messageB 消息，把 messageB 给 handlerA 的 handleMessageA 方法来处理。


## 例子2： ##

运行Activity会创建一个线程A（ ActivityThread ），线程A是负责图形绘制。

线程B 专门去等待线程A发来的数据进行运算。

##### 开启一个自定义线程，等待主线程发送数据来进行运算

1.线程B 默认是没有Looper的，所以先创建一个Looper在线程B中，同时在线程B中创建一个消息队列 MessageQueueB

    // 创建一个Looper
    Looper.prepare();

    // 创建Handler
    HandlerUIToThread = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	    		// 这里处理 线程A 发来的数据
    	}
    };

    // 开始Looper的循环
    Looper.loop();

2.线程A 开启 线程B ，线程A 用Message打包好数据，把数据发送到线程B的 MessageQueueB 中

    线程B.HandlerUIToThread.sendMessage(msg);

3.线程B 的Looper从 MessageQueueB 中取出消息 给线程B的handler来处理
