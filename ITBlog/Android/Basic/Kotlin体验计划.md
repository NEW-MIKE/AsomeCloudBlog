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

### 难度挑战：

使用泛型，内联，Lambda，逐步的赋能，并且在上面对于每一次的变化，进行识别。



参考：
https://www.kotlincn.net/docs/reference/lambdas.html