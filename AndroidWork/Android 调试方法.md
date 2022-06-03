https://blog.csdn.net/lsw305264677/article/details/87803990



# 静态调试

适用于：通过打印变量的值来查看某一时刻值是否正确

## Toast（Snackbar）打印法

一般的Android开发人员最爱的调试法，使用简便，仅需一行代码，轻松打印：

方便是挺方便的，不过有一点要注意：Android 5.0后，如果把「消息通知的权限」 关闭掉的话，部分机型是不会显示Toast的！你还可以使用Snackbar来显示值进行调试：

或者其他变通的方法，比如在页面上添加一个TextView，把值直接显示在文本框上。

## Log日志打印法

Toast调试是挺爽的，但是有两个问题：

1.想调试打印多个值的话，Toast会弹个不停，毕竟同一时刻只有「一个」Toast显示在前台；
 2.Toast间隔一段时间后会消失，即使你设置了Toast.LENGTH_LONG；

可能你一走神，没来得及看调试的值，Toast就消失了。我们着实需要一种无需担心调试
 结果消失的方法——「Log日志打印法」，就是利用Android系统提供的Log类，在调试
 的地方，把日志打印到「Logcat控制台」上，使用方法也非常简单：

当代码执行到这一句的时候，就会在Logcat控制台打印调试信息，另外Logcat默认 会打印出所有的日志信息，我们可以做一些过滤来定位到我们调试的日志信息。 首先是：「日志类型」，Android支持6种日志类型，依次如下：

- Verbose：详细，所有类型的日志信息。
- Debug：调试，调试用的日志信息。
- Info：信息，正常使用时需要关注的日志信息。
- Warn：警告，可能有问题，但没发生错误的日志信息。
- Error：错误，运行时出现严重错误的日志信息。
- Assert：断言。

温馨提示：
 不要上来就Error级别，我以前打Log全部用Log.e，原因是日志信息是红色的，好看…
 结果被组长屌了一顿，觉得颜色不好看，你可以按照下述的操作进行自定义。
 打开「File」-> 「Settings」-> 「Editor」->「Colors Scheme」->「Android Logcat」
 选择日志类型，然后去掉勾选，然后点击选择颜色色值
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190220160318890.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 这是我的配色方案，读者可以设置自己喜欢的颜色：

1. Assert：7A7B8F
2. Debug：0070BB
3. Error：FF7646
4. Info：48BB31
5. Verbose：BBBBBB
6. Warning：3899BB

设置后的配色如图所示：
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190220161154869.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 自定义Logcat日志头信息的显示内容：点击面板上的「Logcat Header」来设置日志头信息
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190220162005249.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 可设置的内容如下：
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190220162149405.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 右侧还可以「过滤日志信息」，支持正则，再右面是过滤特定日志的选项。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/2019022016304076.png)
 如果觉得还不够的话，可以点击最右侧的「Edit Filter Configuration」来配置一个自己的过滤器。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190220163244493.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 另外，还可以对进行「日志搜索」，鼠标点击Logcat中间区域获得焦点，Ctrl + F 调出 搜索工具栏，接着搜索相关的日志内容。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190220163405399.png)
 注意：Android系统的单条日志打印长度是有限的，长度是固定的4*1024个字符长度。

# 静态调试

用Android Studio提供的Debug模式来程序调试

**1. 基本的调试流程**
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221103701224.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 **2. 打(下)断点**
 当程序执行到断点所在的代码时，会暂停应用程序的运行，线程被挂起，然后 可以通过调试器进行跟踪。
 打断点的方式也很简单，点击某行代码的左侧，会出现如图所示的小红点。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221104223440.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 这个小红点就是断点，而在Android Studio中，有多种类型的断点，下面一一介绍

1） 行断点
 就是上面这种，对特定行进行调试时用到，点击行所在的左侧边栏即可设置。 右键点击这个断点，会弹出如下所示的设置对话框：
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221104714408.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 如果你取消了Enabled勾选，断点就处于如图所示的禁用状态：
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221105156868.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 2）方法断点
 如果你把断点下到一个方法前，断点就会变成这样：
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221110311680.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 这个就是方法断点，一般用来检查方法的「输入参数」与「返回值」。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221110626637.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 3）变量断点
 有时我们对程序运行过程并不关心，而只关注某个变量的变化，可以在变量定义前加一个断点。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221110904280.png)
 在程序运行过程中，如果该变量的值发生改变，程序会自动停下来，并定位到变量值改变的地方，供开发者调试。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221111103672.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 4）条件断点
 有时会有这样的场景：把断点打到循环体的中，我们只关心特定循环次数下的运行情况。 比如一个循环10次的循环体，我们想知道循环到第8次时的运行情况，如果你不知道 条件断点的话，你需要一直按「Run to Cursor」直到满足我们的条件。
 如果用条件断点，当循环体执行到某个条件才停下来，右键断点，输入如图所示的等式条件：
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221111649412.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 然后可以发现，程序直接跳到i=8的时候才挂起，非常方便。
 5）日志断点
 调试的时候我们可以通过打印日志的方式来定位异常代码大概位置，以缩小引发问题的范围，然后再使用断点精确定位问题。如果是普通的打印日志，我们需要等待重新构建程序，如果用「日志断点」
 就避免这个无意义的等待。使用日志断点非常简单，右键断点，去掉「Suspend」的勾选，会出现
 如下所示的弹窗，勾选「Evaluate and log」在此输入想输出的内容。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221112124382.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 运行调试后，当执行到日志断点的时候可以看到控制台输出了对应的日志信息，而且程序正常运行，并不会挂起。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221112414784.png)
 如果想查看更详细的信息，比如断点的位置和触发时的堆栈信息，可以勾选「“Breakpint hit” message」和「Stacktrace」，勾选后输出内容会变得更详细：
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221112515126.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 6）临时断点
 所谓的临时断点就是：触发一次后就自动删除的断点。设置的方法有两种：

1.光标移到想打点的行，点击菜单栏「Run」->「Toggle Temporary Line Breakpoint」，
 等价于快捷键：「Ctrl+Alt+Shift+F8」
 2.更便捷的操作：按住Alt，鼠标点击左侧边栏。
 临时断点样式和普通断点没区别，只是点击右键不太一样，如下图所示，鼠标点击后可以去掉临时断点，如果想把临时断点变成普通断点，可以取消勾选 「Remove once hit」的选项。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221113432770.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 7）异常断点
 用于监听程序异常，一旦程序奔溃，直接定位到异常所在的确切位置。依次点击： 「Run」->「View  Breakpoints」打开断点视图。点击「+」，然后选择 「Java Exception  Breakpoints」，在弹出的窗口中输入要调试的异常：
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190221114101758.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xzdzMwNTI2NDY3Nw==,size_16,color_FFFFFF,t_70)
 除了设置异常断点外，你在这里看到项目设置的所有断点，并进行断点管理与配置。 另外，你还可以设置自定义异常断点，点击「5.Exception Breakpoints」自行配置即可。