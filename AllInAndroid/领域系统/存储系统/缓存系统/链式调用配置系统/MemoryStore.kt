package configure;
// 要指定一个类的的主构造函数的可见性，使用以下语法（注意你需要添加一个显式 constructor 关键字）：
// 这里的构造函数是私有的。默认情况下，所有构造函数都是 public，这实际上等于类可见的地方它就可见
// （即 一个 internal 类的构造函数只能在相同模块内可见).

// 模块
// 可见性修饰符 internal 意味着该成员只在相同模块内可见。更具体地说， 一个模块是编译在一起的一套 Kotlin 文件：

// 一个 IntelliJ IDEA 模块；
// 一个 Maven 项目；
// 一个 Gradle 源集（例外是 test 源集可以访问 main 的 internal 声明）；
// 一次 <kotlinc> Ant 任务执行所编译的一套文件。

// 可见性修饰符
// 类、对象、接口、构造函数、方法、属性和它们的 setter 都可以有 可见性修饰符。 （getter 总是与属性有着相同的可见性。） 
// 在 Kotlin 中有这四个可见性修饰符：private、 protected、 internal 和 public。 
// 如果没有显式指定修饰符的话，默认可见性是 public。

/*决策：什么情况下，指定我的类的可见性，如果我这个类，我不想要被外部实例化，也就意味着我更多的是一种单例模式，更有可能保证了我就只有
一个实例，也为了做这样一个保证，我的使用就只是通过类名加上我的方法名来进行实例化，如果我的类，不在乎是否存在多个对象，那么，此处可以
不用加载私有化操作， */
class MemoryStore private constructor() {

    /**
     * 线程安全的单例模式
     * 深入学习可以仿照Java写法
     */
    // 局部声明
    // 局部变量、函数和类不能有可见性修饰符。
    // 类和接口
    // 对于类内部声明的成员：
    
    // private 意味着只在这个类内部（包含其所有成员）可见；
    // protected—— 和 private一样 + 在子类中可见。
    // internal —— 能见到类声明的 本模块内 的任何客户端都可见其 internal 成员；
    // public —— 能见到类声明的任何客户端都可见其 public 成员。
    // 如果你不指定任何可见性修饰符，默认为 public，这意味着你的声明将随处可见；
    // 如果你声明为 private，它只会在声明它的文件内可见；
    // 如果你声明为 internal，它会在相同模块内随处可见；
    // protected 不适用于顶层声明。
    private object Holder {
        internal val INSTANCE = MemoryStore()
    }

    companion object {
        val instance: MemoryStore
            get() = Holder.INSTANCE
    }

    private val mDataMap = HashMap<String, Any>()

    fun <T> getData(key: String): T {
        @Suppress("UNCHECKED_CAST")
        return mDataMap[key] as T
    }

    fun addData(key: String, value: Any): MemoryStore {
        mDataMap[key] = value
        return this
    }

    fun addData(key: Enum<*>, value: Any): MemoryStore {
        addData(key.name, value)
        return this
    }

    fun <T> getData(key: Enum<*>): T {
        return getData(key.name)
    }
}