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
不用加载私有化操作，然后一个类，在创造出来后，还要考虑是否可以被其他类进行继承，目的在于保护自己的一些信息，以及是否可以被多个申明，
在创建完之后，也是基本限定了其使用的范围和范畴。此处是主构造函数， */

/*Kotlin中的open关键字
在Kotlin开发中类和方法默认不允许被继承和重写，等同于Java中用 final 修饰类和方法。
如果在Kotlin 中类和方法想被继承和重写，需添加open 关键字修饰。
此处，如果我们的类是想要可以被继承的，那么，我们就应该写成这样：
open class MemoryStore private constructor() { 如果一个类是可以被继承的，那么他的类是否是可以被私有化的呢，那么有一个问题，如果
    主类的主构造函数私有化了，那么，子类的会如何，我如果继承了的化，那么其主构造函数，我是继承不了的，在面向对象里面，私有属性和方法
    是不能被继承的，那也就是说，在我的子类里面无法进行操作父类的这个部分的知识。
*/
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

    /*对于此处，有哪些选择，这个部分是属于类里面的操作，
    这称为对象声明。并且它总是在 object 关键字后跟一个名称。 就像变量声明一样，对象声明不是一个表达式，不能用在赋值语句的右边。

对象声明的初始化过程是线程安全的并且在首次访问时进行。

如需引用该对象，我们直接使用其名称即可： 
决策：在此处，我是否需要建立一个与世隔绝的变量，如果是的话，那就用private进行修饰，如果不是，就是默认的public，对于其内部的internal
，我觉得是没有必要的，因为其外部的private已经保证了该对象只能在这个类以内进行访问，其内部的，是否可以进行传导，
*/
    private object Holder {
        internal val INSTANCE = MemoryStore()
    }
/*伴生对象，可以理解为java里面的static，就是通过类来进行对象的访问和获取， */
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