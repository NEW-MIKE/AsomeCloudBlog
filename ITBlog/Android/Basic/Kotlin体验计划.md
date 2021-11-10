Kotlin体验计划：
目的，在于使用Kotlin得到自由，

一，高阶函数体验报告：

```
fun printParams(num: Int, str: String = "hello") {
    println("num is $num , str is $str")
}
fun example(func: (Int, String) -> Unit) {
    func( 123,"hello")
}
fun main() {
    example(::printParams)
    printParams(123)
}
```

出现的问题为：
1，直接在example(printParams(123,"hell")).而正确的打开方式为：example(::printParams)

### 综合体验：

#### 关于高阶函数：

1，利用高阶函数和泛型，构造let，run,with,apply四个标准函数，并且转化为对应的lamba的格式
此处，build的作用类似于apply：

```
fun StringBuilder.build(block: StringBuilder.() -> Unit): StringBuilder {
    block()
    return this
}
fun main(){
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
    val result = StringBuilder().build {
        append("Start eating fruits.\n")
        for (fruit in list) {
            append(fruit).append("\n")
        }
        append("Ate all fruits.")
    }
    println(result.toString())
}
```

推理：如果按照常规的调用：

```
fun printParams(num: Int, str: String = "hello") {
    println("num is $num , str is $str")
}
fun example(func: (Int, String) -> Unit) {
    func( 123,"hello")
}
fun main2() {
    example(::printParams)
    printParams(123)
}
```

应该在build里面加一个：：FuncName。猜测，在block()，代表了一切StringBuilder.fun的这部分。

```
val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
val result = StringBuilder().apply {
    append("Start eating fruits.\n")
    for (fruit in list) {
        append(fruit).append("\n")
    }
    append("Ate all fruits.")
}
println(result.toString())
```

![img](https://upload-images.jianshu.io/upload_images/436713-5c6f5a2b83364498.png?imageMogr2/auto-orient/strip|imageView2/2/w/761/format/webp)

函数声明为`inline`内联则会在编译时将代码复制粘贴到对应调用的地方，如果函数体很大很复杂，不建议使用内联，否则会使包体积增大。

```kotlin
//声明：
inline fun debug(code: () -> Unit){
    if (BuildConfig.DEBUG) {
        code() 
    }
}

//用法：
fun onCreate(savedInstanceState: Bundle?) {
    debug {
        showDebugTools();
    }
}
```

```kotlin
/**
 * Calls the specified function [block] with `this` value as its receiver and returns `this` value.
 */
@kotlin.internal.InlineOnly
public inline fun <T> T.apply(block: T.() -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
    return this
}

/**
 * Calls the specified function [block] with `this` value as its argument and returns `this` value.
 */
@kotlin.internal.InlineOnly
@SinceKotlin("1.1")
public inline fun <T> T.also(block: (T) -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block(this)
    return this
}
```



```kotlin
/**
 * Calls the specified function [block] and returns its result.
 */
@kotlin.internal.InlineOnly
public inline fun <R> run(block: () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its result.
 */
@kotlin.internal.InlineOnly
public inline fun <T, R> T.run(block: T.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

/**
 * Calls the specified function [block] with the given [receiver] as its receiver and returns its result.
 */
@kotlin.internal.InlineOnly
public inline fun <T, R> with(receiver: T, block: T.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return receiver.block()
}
```



```kotlin
/**
 * Calls the specified function [block] with `this` value as its receiver and returns its result.
 */
@kotlin.internal.InlineOnly
public inline fun <T, R> T.run(block: T.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

/**
 * Calls the specified function [block] with `this` value as its argument and returns its result.
 */
@kotlin.internal.InlineOnly
public inline fun <T, R> T.let(block: (T) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block(this)
```

lambda：首先这个东西，是针对函数来说的，是对与函数调用的一种省略性质的优化，
val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
其优化的形式为：val sum = { x: Int, y: Int -> x + y }。可以看出->前后是具备一定的关联性的作用的。

在 Kotlin 中有一个约定：如果函数的最后一个参数是函数，那么作为相应参数传入的 lambda 表达式可以放在圆括号之外：
val product = items.fold(1) { acc, e -> acc * e }
如果该 lambda 表达式是调用时唯一的参数，那么圆括号可以完全省略：`it`：单个参数的隐式名称

一个 lambda 表达式只有一个参数是很常见的。

如果编译器自己可以识别出签名，也可以不用声明唯一的参数并忽略 `->`。 该参数会隐式声明为 `it`：

ints.filter { it > 0 } // 这个字面值是“(it: Int) -> Boolean”类型的

下划线用于未使用的变量（自 1.1 起）

如果 lambda 表达式的参数未使用，那么可以用下划线取代其名称：

#### 结论1：

高阶函数，是在Lambda的基础上进行构建的。



#### 关于协程：

协程是干嘛的：https://www.kotlincn.net/docs/reference/coroutines/coroutines-guide.html。协程应该视为代码的调用来实现的，任务的重新的分配的方式。在另外一种程度上的并发。耗时的任务，也可以分配给协程来完成。我现在需要培养一个意识，什么时候，调用协程，主动用它来解决一次问题，并进行记录。而使用协程却可以仅在编程语言的层面就能实现不同协程之间的切换，从而大 大提升了并发编程的运行效率。最终的目的在于并发的调用，只是是基于提供了协程的语言，在kotlin里面，能够调用协程，就不会去调用线程，当然这也不是万能的，
协程如何操作：
协程操作空间有多宽：
应用场景：我们可以使用协程来实现异步操作，比如在网络爬虫场景下，我们发出一个请求之后，需要等待一定的时间才能得到响应，但其实在这个等待过程中，程序可以干许多其他的事情，等到响应得到之后才切换回来继续处理，这样可以充分利用 CPU 和其他资源，这就是异步协程的优势。

```
runBlocking {
    coroutineScope {
        launch {
            for (i in 1..10) {
                println(i)
                delay(1000)
            }
        }
    }
    println("coroutineScope finished")
}
```

#### 结论2：

协程，是用户态的线程。

#### 关于密封类：

做这一次进攻的目的，在于看起来，这个东西很烦，体验一下自己的战斗力。

#### 关于委托by：

被委托的对象，其读写都被委托的对象替换掉。有什么好处呢，不就是用这个申明吗。那么上面这段伪码，你打算插入到哪里呢？难道B和C就一直不停的用While循环做上述判断吗？显然不合适吧。
https://www.cnblogs.com/guoqiang1/p/8138889.html
https://blog.csdn.net/weixin_39309402/article/details/98126885
https://xuyisheng.top/kotlin5/
https://juejin.cn/post/6844904038589267982

### 难度挑战：

使用泛型，内联，Lambda，逐步的赋能，并且在上面对于每一次的变化，进行识别。



## 容器化学习：

#### 第一容器：

该容器的作用，在于将多个订阅源进行合并统一处理，并将结果处理后传递出来。巧妙利用高阶函数。

```kotlin
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

object LiveDataUtil {
    fun <T1, T2, R> zip2(
        src1: LiveData<T1>, src2: LiveData<T2>,
        zipper: (T1, T2) -> R
    ): LiveData<R> {
        return MediatorLiveData<R>().apply {
            var src1Version = 0
            var src2Version = 0
            var lastSrc1: T1? = null
            var lastSrc2: T2? = null
            fun updateValueIfNeeded() {
                if (src1Version > 0 && src2Version > 0 &&
                    lastSrc1 != null && lastSrc2 != null
                ) {
                    value = zipper(lastSrc1!!, lastSrc2!!)
                    src1Version = 0
                    src2Version = 0
                }
            }
            addSource(src1) {
                lastSrc1 = it
                src1Version++
                updateValueIfNeeded()
            }
            addSource(src2) {
                lastSrc2 = it
                src2Version++
                updateValueIfNeeded()
            }
        }
    }
}
```

1，对于Kotlin，由于其默认本身的任何的变量都是非空的，当一个变量可能为空的时候，在申明的时候，我们需要将其类型背后加？。为了调用非空类型的属性和或者函数，可以使用？.来判断是否能够调用属性或函数，Evvis是用来在空与否的时候，到底返回什么值。！！用来断言此处一定不是空，否则，调用将会异常。
            var lastSrc1: T1? = null
            var lastSrc2: T2? = null
所以以上，我们只能用一个来申明变量，且只有如此，才能给它赋值为null空。
value = zipper(lastSrc1!!, lastSrc2!!)
函数调用的时候，肯定会进行非空检查的，为了在编译器那里能够编译通过，我们需要使用非空断言来编译通过。此处没有涉及到对象的属性和函数的调用，其他的使用形式不论。

2，object关键字主要在申明一个类的同时，生成这个类的对象：对象表达式，对象声明，伴生对象。它的用处在于可以省略其约束的类名（也叫做匿名类），如果类是有构造函数的，那么后面是要有小括号的，如果没有，小括号可以省略。
object LiveDataUtil {
此处的作用在于static的使用方式，所以用到了关键词object，当使用这个类名的时候，自动生成了实例。

3，在函数名之前添加<>就是泛型函数了，可以用大小写的英文字母，如果是多个参数类型，用逗号分开。当为了约束泛型的时候，可以使用冒号。如果是可以比较的类型可以将<T:Comparable<T>> ,<T:Number>,.如果不想接收任何可空数据类型，可以《T：Any》,可空的话，写为《T：Any?》
Kotlin中的泛型属性一定是扩展属性。
泛型类，在类的名字后面<T>.泛型接口也是类似。
    fun <T1, T2, R> zip2(
        src1: LiveData<T1>, src2: LiveData<T2>,
        zipper: (T1, T2) -> R
    ): LiveData<R> {
此处用到的是泛型函数，且是三个泛型参数，返回值也是一个泛型类的声明。这个函数传入三个形参，前面两个是LiveData，后面是一个函数。

4，高阶函数：在函数式编程中，一切皆是函数，所谓高阶函数就是一个函数可以作为另外一个函数的参数或返回值。可以将函数视为一个数据类型。函数类型的定义为形参和返回值，在表达上面是（A,B）->C.函数列表中的参数类型 ->返回类型。四个点：：函数名。
zipper: (T1, T2) -> R，此处的zipper就是一个函数变量，类型是两个形参，一个返回值的类型。

5，Lambda表达式
Lambda是一种匿名函数，可以作为表达式，函数参数和函数返回值使用。其运算结果就是一个函数。用箭头来讲参数列表和Lambda体分开。如果没有return，Lambda最后一个表达式就是其返回值，如果有就是return后面的表达式。所以其返回值是推导出来的。相对于函数类型，其没有小括号。Kotlin有很强的类型推导能力。所以其参数类型和返回值类型可以省略，直接交给编译器来进行推导。另外，如果一个函数的最后一个参数是Lambda表达式，那么这个Lambda表达式可以放在函数括号之后。这叫做尾随Lambda表达式，很多时候，容易被误认为是函数声明。
如果Lambda表达式的参数只有一个，并且能够根据上下文环境推导出它的数据类型，那么这个参数声明就可以省略，在Lambda体中使用隐式参数it替代Lambda表达式的参数。
闭包closure是一种特殊的函数，可以访问函数体之外的变量。
在高阶函数中参数如果是函数类型，则可以接收Lambda表达式，而Lambda表达式在编译时被编译成为一个匿名类，每次调用函数时都会创建一个对象，如果被函数反复调用则创建很多对象，会带来运行时的额外开销。可以用内联函数解决。使用inline修饰。
let：let是一个函数，当调用他的对象是空的时候，不调用后面的代码，否则调用。所有的对象都可以调用这个函数。
with，apply。当需要对一个对象设置多个属性或调用多个函数时，可以调用。apply返回其对象，with不返回。使用this访问其对象。以上是内联函数的使用场景。
 (T1, T2) -> R此处即是一个函数类型声明，也是一个Lambda表达式，
return MediatorLiveData<R>().apply {
此处使用的是为了修改其属性或者函数，所以调用了apply这个内联函数，返回的也是其对象的本身。
value = zipper(lastSrc1!!, lastSrc2!!)此处是一个闭包的调用，           
 addSource(src1) {
                lastSrc1 = it
                src1Version++
                updateValueIfNeeded()
            }
此处也是其函数的一个调用，其中it隐式参数代表的是src1,

```kotlin
val articlePage =LiveDataUtil.zip2(articleList, topArticleList) { list, top ->
    list.data?.datas?.let {
        top.data?.run {
            forEach { a -> a.top = true }
            (it as MutableList<ArticleVO>).addAll(0, this)
        }
        it.forEach { a ->
            a.read = dao.isRead(username, a.id)
        }
    }
    hasMore.value = !(list.data?.over ?: false)
    list.data?.run {
        if (curPage == 1) {
            refreshing.value = false
        } else {
            moreLoading.value = false
        }
    }
    list.data
}
```

以上是实际调用的地方，采用的是尾随调用的方式，zipper被替换成为了后面的Lambda表达式表示的函数部分。且这个值会传给value。而value又会是zip2函数返回值本身，就是articlePage。其返回值是自动推定出来的。

6，forEach:此函数适用于Collection，Map集合以及数组，函数只有一个函数类型的参数，实参往往使用尾随形式的Lambda表达式。在执行forEach会把集合或数组中的每一个元素传递给Lambda表达式（或其他的函数引用）以便去执行。
        it.forEach { a ->
            a.read = dao.isRead(username, a.id)
        }
此处就是一个尾随的函数，每一个值给a，然后交给以a为参数的函数来执行，后面是一个Lambda表达式。

7，run，有返回值，且是最后一个值。相对于apply来说，它可以指定自己的返回值。with和run类似，差别在语法方面。with在括号里面提供上下文。

8，MediatorLiveData 中介者LiveData,

- 它可以监听另一个LiveData的数据变化，
- 同时也可以做为一个liveData，被其他Observer观察。
- MediatorLiveData 主要有四个方法
  - addSource
  - removeSource
  - onActive
  - onInactive

主要用于合并多个变化源，addSource可以保证被监视Livedata不会被重复订阅，如果我们手动observe的话，可能会因为重复订阅造成数据接受异常，通过addSrouce的源码可以清楚看到这一点，在简单的代码中，这样的bug很容易被察觉，但在一个复杂项目中，像这样的时序问题在写代码的时候很容被忽略，所以当大家使用LiveData时，要多利用MediatorLiveData这样的工具，哪怕多写两行代码，也要保证时序的准确性。
            addSource(src1) {
                lastSrc1 = it
                src1Version++
                updateValueIfNeeded()
            }
此处表明了它对src1进行了监听。

10，Transformations.switchMap(page) 此种调用方式，其关键是在于其返回值，默认最后一个就是其返回值，而传入的只是一个参数而已。

#### 第二容器：

此处容器的作用在于属性扩展，将懒加载进行封装，作为ViewModel的统一的提供方式。目的在于简化这样的调用。将所有的非逻辑的模板封装出去。

```kotlin
import android.app.Activity
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

inline fun <reified T : ViewModel> FragmentActivity.viewModel() =
    lazy { ViewModelProviders.of(this).get(T::class.java) }

inline fun <reified T : ViewModel> FragmentActivity.viewModel(crossinline block: T.() -> Unit) =
    lazy {
        ViewModelProviders.of(this).get(T::class.java).apply(block)
    }

inline fun <reified T : ViewModel> Fragment.viewModel() =
    lazy { ViewModelProviders.of(this).get(T::class.java) }

/**
 * 相同类型使用多个的情况下
 */
@JvmOverloads
inline fun <reified T : ViewModel> Fragment.viewModel(
    key: Int? = null,
    crossinline block: T.() -> Unit
) =
    lazy {
        ViewModelProviders.of(this).get(key.toString(), T::class.java)
            .apply(block)
    }

/**
 * 获取参数
 */
inline fun <reified T> Activity.arg(key: String) =
    lazy {
        val ret = when (T::class.java) {
            String::class.java -> intent.getStringExtra(key)
            Int::class.java -> intent.getIntExtra(key, 0)
            Float::class.java -> intent.getFloatExtra(key, 0f)
            Boolean::class.java -> intent.getBooleanExtra(key, false)
            else -> (intent.getParcelableExtra(key) as Parcelable)
        }
        ret as T?
    }

inline fun <reified T> Fragment.arg(key: String) =
    lazy {
        val ret = when (T::class.java) {
            String::class.java -> arguments?.getString(key)
            Int::class.java -> arguments?.getInt(key)
            else -> null
        }
        ret as T?
    }
```

1，扩展函数：Kotlin 能够扩展一个类的新功能而无需继承该类或者使用像装饰者这样的设计模式。 这通过叫做 *扩展* 的特殊声明完成。 例如，你可以为一个你不能修改的、来自第三方库中的类编写一个新的函数。 这个新增的函数就像那个原始类本来就有的函数一样，可以用普通的方法调用。 这种机制称为 *扩展函数* 。此外，也有 *扩展属性* ， 允许你为一个已经存在的类添加新的属性。声明一个扩展函数，我们需要用一个 *接收者类型* 也就是被扩展的类型来作为他的前缀。
在以上的所有的函数，都是作为扩展函数存在的，且依赖于接收者类型。表示该函数依存于什么接收者。如果是这种类型的，那么所有的这种类型的变量都可以进行调用。

2，reified
使用reified很简单，主要分为两步

- 在泛型类型前面增加`reified`
- 在方法前面增加`inline`（必需的
  至于能在运行时得到`T`的类型信息是如何做到的，就需要了解`reified`的内部机制了。Java通过类型擦除实现泛型，不会生成新的类。
  ps：此处是为了在运行时获取到类型的信息，所以才用了这个关键字。

3，lazy
是接受一个 lambda 并返回一个 `Lazy <T>` 实例的函数，返回的实例可以作为实现延迟属性的委托： 第一次调用 `get()` 会执行已传递给 `lazy()` 的 lambda 表达式并记录结果， 后续调用 `get()` 只是返回记录的结果。
以上都是函数的返回值，其值只在首次访问时计算，所以使用了该关键字。

4，by
`by` keyword as ***provided by\***.
此处的意义，在于制定什么来提供。

5，as？
*null* 不能转换为 `String` 因该类型不是[可空的](https://www.kotlincn.net/docs/reference/null-safety.html)， 即如果 `y` 为空，上面的代码会抛出一个异常。 为了让这样的代码用于可空值，请在类型转换的右侧使用可空类型：val x: String? = y as String?为了避免抛出异常，可以使用*安全*转换操作符 *as?*，它可以在失败时返回 *null*：

```
val x: String? = y as? String
```

请注意，尽管事实上 *as?* 的右边是一个非空类型的 `String`，但是其转换的结果是可空的。

6，T.()
Now we can write **DSL style code** because of this. Let's see kotlin standard library function `Apply`

```kotlin
public inline fun <T> T.apply(block: T.() -> Unit): T { block(); return this }
```
As you can see apply is an Extension function of T, **block** will be function with receiver type **T**,
**Because of T.()** T will be available as the first argument in lambda.
Now here block is invoked by **block()** inside function body, but you can also write **this.block() or block(this)**
crossinline block: T.() -> Unit
) =
    lazy {
        ViewModelProviders.of(this).get(key.toString(), T::class.java)
            .apply(block)
此处就是说，其扩展函数是可以调用xxx.block的。.apply(block)的意义，具备xxx.block()的效果。

7，crossinline

- `inline`: 声明在编译时，将函数的代码拷贝到调用的地方(内联)
- `oninline`: 声明 `inline` 函数的形参中，不希望内联的 `lambda`
- `crossinline`: 表明 `inline` 函数的形参中的 `lambda` 不能有 `return`

8，@JvmOverloads

但是如果使用的了@JvmOverloads注解：

```kotlin
@JvmOverloads fun f(a: String, b: Int=0, c:String="abc"){
}
```

相当于在Java中声明了3个方法：

```dart
void f(String a)
void f(String a, int b)
void f(String a, int b, String c)
```

正如备注，构造多个构造函数。



### 参考：

https://www.kotlincn.net/docs/reference/lambdas.html
https://developer.android.com/kotlin
https://developer.android.com/courses/android-basics-kotlin/unit-1
https://kotlinlang.org/docs/android-overview.html
https://developer.android.com/kotlin/first