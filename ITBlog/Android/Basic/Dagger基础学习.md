Dagger基础学习

### 一，原理：

类如何实例化，关注依赖。统一的去获取和管理，只管如何提供实例，不管如何生成这个用例的。
那如果要实现这样一个结局的充分必要条件是什么，如何形成一个闭环:



### 二，逻辑推导：

在使用的基础上，最好的就是，如何提供实例，在实例的基础上，自动的产生依赖，形成自己的依赖的链条。这个的用法就是利用一个东西，自动完成自己的依赖链条，形成一个封闭的自动完成依赖的空间，我外部的其他需要使用的地方，直接使用就可以了。
在这里进行推导，对每一个部分都有自己的个人的解释，在整体上面形成一个必然的闭环，比方说，具体框架方面，细节方面，都会有什么样的变化，然后自己针对这个具体会有多少的应用场景，如何应多这个应用场景，详细的进行描述，就是解释它的这个形而上学的还有逻辑链条上的变化，
预览推导：本身是通过注解来完成信息的传递的，通过引入新的编译器的机制，来完成其自动关联，如果我是编译器的编辑者，处于用这种方式来组织代码，
首先，编译之前，我需要检查哪些是用@Inject来进行标识的，然后从代码里面检测是否有Dagger开头的标识，然后在这个里面取出和@Inject同种类型的变量，并调用其常规的对象生成形式，然后自动的将变量进行初始化，如果该对象的生成具有形参，那就检查所有的@Inject里面，还有Component里面是否具有提供同种类型的变量，自动把这个变量给关联起来。其中Dagger开头的就是提供变量的地方，所以这个地方要能够正确生产出正确的初始化出来。

### 三，基础知识：

Module提供实例，Component用来注入。

### 四，杂思：

1，是不是每个生成的对象都要生成一个对应的module。目测是不需要的，那么，生成的依据是什么，Component聚合的依据是什么。有两个依据，第一个依据就是同一种类型放在不同的module里面。就是说，我要建立起来一种霸气，可以将所有需要形成依赖的地方，统统地
2，直接从另外的一个角度，全面重新审视变量的初始化，特地设定一个地方，来完成类的初始化操作，如果这个和工厂结合起来会如何。当编程也可以直接面对初始化了的时候，变成了什么样的世界。
3，在使用的时候，如果自己已经定义了，那么就不用再特地的进行申明了。
4，其目的在于使用一个闭环的操作来完成这个目的，

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

有一个问题，如果自己的产生，是希望通过inject的方式来产生的，为啥要通过inject来标识，还有不同的标识，是为了解决什么样的问题而创造出来的。标识是用来引导对应的变量，变量之间的初始化进行牵引，为什么要对这个运行时进行定义呢，


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

```
.applicationComponent(((MyApplication) getApplication()).applicationComponent)
```

以上的这种模式，是添加复合的Component的方式，最终的就是复用其他地方生产的形式的变量。

代码来源于这里：https://blog.mindorks.com/dependency-injection-with-dagger2-advance  可以直接用来指导和采取实验。

在以上的这个示例里面，具体的路线是，ViewModel需要依赖于NetworkService，然后这个NetworkService又依赖于Context和String类型，Context类型我们有两个地方可以产生，一个是Application，一个是Activity，这里我们通过Scope来进行了区分，并且实现了对于Component的复用。
其中可以在其形参中进行Scope的指定，这样子，可以在使用变量的时候，直接清晰的完成变量的使用，对于相关的依赖，就不用再编程，

https://blog.mindorks.com/introduction-to-dagger-2-using-dependency-injection-in-android-part-2-b55857911bcd
如何解释这个部分，为什么component 还需要添加，如何对整个形成一个合理的解释，这是复用。

### 参考：

https://blog.mindorks.com/a-complete-guide-to-learn-dagger-2-b4c7a570d99c
https://www.huaweicloud.com/articles/1f4d7115336c83080815bac044b1920e.html