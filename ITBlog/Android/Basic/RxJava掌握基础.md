RxJava掌握基础

### 一，基础知识：

https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Create.html
https://github.com/ReactiveX/RxJava
五种形式：https://bugfender.com/blog/data-flows-in-rxjava2-observable-flowable-single-maybe-completable/

### 自我理解：

RxJava 其实本身是一种封装（封装了一些标准的编程的逻辑过程，）。总的来说，就是观察者模式，被观察的对象可以发送数据，数据任意，观察者接收数据，在数据传递的过程中，可以修改数据。
谁来发送数据：Observable
谁来接收数据：Observer
如何接收：Subscribe订阅来接收
发送和接收可以在不同的线程上面。

这不神秘，这就是一个类。在创建的时候，需要进行转化，成为RxJava的可观察的对象。决定的是封装了多少我可以不用关注的事情。就是省掉了很多不必要的设置和编程达到了配置式的开发。

### 二，如何解决RxJava2 vs RxJava3

### 三，如何使用RxJava:

1,使用适配器创建Observerble对象：

```
Retrofit retrofit = new Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
        // Or
        // .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .baseUrl("")
        .build();
```



### 四，它是如何工作的：

1，基础的格式为：创建生产数据流的对象，创建消费数据流的对象，然后两者关联起来。不需要第三方的介入。其附加的意义还在于，计算的过程。由此，可以衍生出很多的操作符。
2，基于不同的流的，基于流里面的数据的，
