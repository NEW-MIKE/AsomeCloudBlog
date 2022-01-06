#### 总诉：











#### 名词录：

- ActivityThread：是安卓应用程序的主线程类，也就是UI线程或者称为主线程，所有的处理用户消息，以及绘制页面的工作都在该线程中完成。
-  Activity： ActivityThread会根据用户的操作选择让哪个Activity对象上它的船。
-  PhoneWindow：富二代，继承于牛气的Window类，自己屋里住着一个DecorView对象，像它老爸喜欢制定规则提供了一些通用窗口操作API。
-  Window：富一代，长得比较抽象，喜欢制定规则提供了一些通用的窗口操作API。它不喜欢被人管。所以呢，注意：WindowManagerService管理的窗口不是Window类，其实是View和ViewGroup。
-  DecorView：很能干的家伙，家产来自FrameLayout，比较注重外在喜欢打扮，DecorView是对FrameLayout进行了一些修饰，从名字就可以看出来。
-  ViewRoot：小管家。继承于Handler，主要作用是把WMS的IPC调用转换为本地的一个异步调用。
-  W类：ViewRoot小助手，继承于binder，是ViewRoot内部类。主要帮助ViewRoot实现把WMS的IPC调用转换为本地的一个异步调用。
-  WindowManager：客户端如果想创建一个窗口得先告诉WindowManager一声，然后它再和WindowManagerService交流一下看看能不能创建，客户端不能直接和WMS交互。
- WindowManagerService（WMS）
- ActivityManagerService（AMS）。
- [Init 进程
- 开机启动 Zygote 进程
- Android Framework - 开机启动 SystemServer 进程
- Android Binder 驱动 - Media 服务的添加过程
- Android Binder 驱动 - 启动 ServiceManager 进程
- Websocket
- SocketIO 



#### 高人怎么看：

