### go学习总览

如何掌握这一门语言呢，



### 自我练习并记录坑:

1，依据思维导图，自己手写一遍这些语法，并记录相关的忽略的点：

##### 集合map：

所有的定义都是var 开头，且其固定的格式是 var 变量名  变量类型。任何的类型都有自己的特定的名字，所以也就可以直接在这个地方得到使用。
var myMap map[int]int
然后还需要通过make来创建新的实例，基本格式应该就是make然后类型加入其中。
然后就可以使用了。
其本身还有一个delete的操作和判断元素是否存在的操作。
delete的话就是直接是这个map的名字和其下标就可以完成操作。
判断某一个下标是否存在，直接用一个逗号标识来识别，简洁的识别出来其中的值和判断其是否存在。

##### 协程go:

通道的意义就是从协程里面把数据传导出来。
关于channel，在创造的时候，会写一个make，

##### 关于make：

在切片，map，channel中都有用到。

##### 关于接口：

```
type Phone interface {
   call()
}

type NokiaPhone struct {
}

func (nokiaPhone NokiaPhone) call() {
   fmt.Println("I am Nokia, I can call you!")
}
```

func的使用，默认(nokiaPhone NokiaPhone) 直接继承了接口，实现了接口，当有调用的时候，直接替换掉，这就是它的上下文，效果和java的一致，只是体现方式不一样。在形式上，需要创建一个struct来和interface配合使用，struct和interface结合的地方，就是具体实现interface的地方，这种写法，可以很好的扩展，在不改动代码的前提下，具体使用，就是在func的后面加一个小括号，在小括号里面放入一个struct申明的变量，后面放的名字是interface里面的名字，当要使用的时候，new(NokiaPhone)直接将结构体传进去，那么，与该结构体相关的代码就可以得到调用。
既然继承了，那么，所有相关的函数都要调用。使用。

还有一条规则，定义了就要使用，不使用就会标红。
此处感觉就是struct提供数据，interface提供方法，struct提供实际的子类。
不管是系统的还是用户自定义的，都可以定义对应的接口interface。

##### 关于if:

```
if _, errorMsg := Divide(100, 0); errorMsg != "" {
fmt.Println("errorMsg is: ", errorMsg)
}
```

只是判断；后面的部分。

关于返回值：

```
// 定义 `int` 类型除法运算的函数
func Divide(varDividee int, varDivider int) (result int, errorMsg string) {
   if varDivider == 0 {
      dData := DivideError{
         dividee: varDividee,
         divider: varDivider,
      }
      errorMsg = dData.Error()
      return
   } else {
      return varDividee / varDivider, ""
   }
```

返回值占用符直接写，在编译器的时候，直接可以替换掉。如下所示：
result, errorMsg := Divide(100, 10); 
通过占位符号直接传值进去。

##### 关于结构体：

指针在使用的时候，其形式和不使用是一个样的，但是如果想要改变结构体的值，就必须要使用其指针的形式。

##### 关于协程同步：

创建两个channel值，channel的属性是边传边取，如果传了，不取，就会形成阻塞，两个协程之间可以相互存取。接收方在有值可以接收之前会一直阻塞。如果通道不带缓冲，发送方会阻塞直到接收方从通道中接收了值。关闭通道并不会丢失里面的数据，只是让读取通道数据的时候不会读完之后一直阻塞等待新数据写入

### go在嵌入式中的应用：

在这个竞争激烈的市场上，拼的是人无我有，人有我优，人优我新，人新我更新，且做到低成本和高效率。
如何开发，到时候就是要新建一个模块，这个模块里面有什么数据，数据的封装，逐层的封装，调用。

