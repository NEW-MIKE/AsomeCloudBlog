Dagger基础学习

### 一，原理：

类如何实例化，关注依赖。统一的去获取和管理，只管如何提供实例，不管如何生成这个用例的。
那如果要实现这样一个结局的充分必要条件是什么，如何形成一个闭环:



### 二，逻辑推导：

在使用的基础上，最好的就是，如何提供实例，在实例的基础上，自动的产生依赖，形成自己的依赖的链条。这个的用法就是利用一个东西，自动完成自己的依赖链条，形成一个封闭的自动完成依赖的空间，我外部的其他需要使用的地方，直接使用就可以了。

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
MainActivity作为我要输入的对象：

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





### 参考：

https://blog.mindorks.com/a-complete-guide-to-learn-dagger-2-b4c7a570d99c
https://blog.mindorks.com/introduction-to-dagger-2-using-dependency-injection-in-android-part-2-b55857911bcd