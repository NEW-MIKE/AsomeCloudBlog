Dagger基础学习

### 一，原理：

类如何实例化，关注依赖。统一的去获取和管理，只管如何提供实例，不管如何生成这个用例的。
那如果要实现这样一个结局的充分必要条件是什么，如何形成一个闭环:



### 二，逻辑推导：

在使用的基础上，最好的就是，如何提供实例，在实例的基础上，自动的产生依赖，形成自己的依赖的链条。这个的用法就是利用一个东西，自动完成自己的依赖链条，形成一个封闭的自动完成依赖的空间，我外部的其他需要使用的地方，直接使用就可以了。
在这里进行推导，对每一个部分都有自己的个人的解释，在整体上面形成一个必然的闭环，比方说，具体框架方面，细节方面，都会有什么样的变化，然后自己针对这个具体会有多少的应用场景，如何应多这个应用场景，详细的进行描述，就是解释它的这个形而上学的还有逻辑链条上的变化，

### 三，基础知识：

Module提供实例，Component用来注入。

### 四，杂思：

1，是不是每个生成的对象都要生成一个对应的module。目测是不需要的，那么，生成的依据是什么，Component聚合的依据是什么。有两个依据，第一个依据就是同一种类型放在不同的module里面。就是说，我要建立起来一种霸气，可以将所有需要形成依赖的地方，统统地
2，直接从另外的一个角度，全面重新审视变量的初始化，特地设定一个地方，来完成类的初始化操作，如果这个和工厂结合起来会如何。当编程也可以直接面对初始化了的时候，变成了什么样的世界。

### 五，各种使用场景：

```
public class Stream {

    public static void main(String[] args) {
        Math math = new Math();
        Science science = new Science();
        Subject subject = new Subject(math, science);
        subject.read();
    }

}
```

上面的例子，就是说，在我main里面，需要实例化三个变量，其中有两个是为第三个服务的。那么如果利用Dagger，我们将如何改造上面的方式呢，

以下来一个三连冠；
MainActivity作为我要输入的对象：对Provides的猜想，这个标识了一个返回的变量的类型，这个类型可以提供给Component中inject到对应的需要的模块中去，流体力学的角度，@Module通过@Component将自身的模型提交给对应的需要接收这个变量的模块中。在实际的使用中，通过builder的component和module具体的实例化完成必要性的操作，做完这些工作之后，通过inject标识的对象，就可以自动的初始化了。

有一个问题，如果自己的产生，是希望通过inject的方式来产生的，为啥要通过inject来标识，还有不同的标识，是为了解决什么样的问题而创造出来的。标识是用来引导对应的变量，变量之间的初始化进行牵引，为什么要对这个运行时进行定义呢，https://blog.mindorks.com/introduction-to-dagger-2-using-dependency-injection-in-android-part-2-b55857911bcd


```
@Module
public class ActivityModule {
    MainActivity activity;


    public ActivityModule(MainActivity activity){
        this.activity = activity;
    }

    @ActivityContextQualifier
    @Provides
    Context provideActivityContext(){return activity;}
}
```

注入到我自身之中：

```
@AcitvityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {ActivityModule.class})
public interface ActivityComponent {

     void inject(MainActivity activity);
}
```

然后在使用的时候，如此。

```
    getAcivityComponent();


}

void getAcivityComponent(){

   DaggerActivityComponent.builder()
            .applicationComponent(((MyApplication) getApplication()).applicationComponent)
            .activityModule(new ActivityModule(this))
            .build()
            .inject(this);
}
```



https://blog.mindorks.com/introduction-to-dagger-2-using-dependency-injection-in-android-part-2-b55857911bcd
如何解释这个部分，为什么component 还需要添加，如何对整个形成一个合理的解释，

### 参考：

https://blog.mindorks.com/a-complete-guide-to-learn-dagger-2-b4c7a570d99c
https://blog.mindorks.com/introduction-to-dagger-2-using-dependency-injection-in-android-part-2-b55857911bcd
https://www.huaweicloud.com/articles/1f4d7115336c83080815bac044b1920e.html