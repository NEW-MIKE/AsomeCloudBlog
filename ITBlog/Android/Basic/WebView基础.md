### 原理：

在这个过程中，WebView只是提供一个平台，在平台上可以跳舞的，是我们的看到的内容，对于内容的可以控制。其效果可以等效于，自己编写自己的浏览器。在html，js，css，然后将自己的想要的东西，赋予这个之上，这个是否可以覆盖前端还有H5，小程序的范畴，这是一个亟待解决的方程式。

#### 1，想象力空间：



#### 2，操作步骤：

##### 1，先有一个控件：

```
    <WebView
        android:id="@+id/webview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
    />
    
```

```
    WebView myWebView = (WebView) findViewById(R.id.webview);
    myWebView.loadUrl("http://www.example.com");
    
```

2，添加网络权限：

```
    <manifest ... >
        <uses-permission android:name="android.permission.INTERNET" />
        ...
    </manifest>
    
```

3，自定义的相关的操作：

- 使用 [`WebChromeClient`](https://developer.android.com/reference/android/webkit/WebChromeClient) 启用全屏支持。如果 `WebView` 需要权限以更改主机应用的界面（例如创建或关闭窗口以及向用户发送 JavaScript 对话框），也需要调用此类。要详细了解如何在这种情况下进行调试，请参阅[调试 Web 应用](https://developer.android.com/guide/webapps/debugging)。
- 处理影响内容呈现的事件，例如提交表单时或使用 [`WebViewClient`](https://developer.android.com/reference/android/webkit/WebViewClient) 导航时出现的错误。 您也可以使用此子类拦截网址加载。
- 通过修改 [`WebSettings`](https://developer.android.com/reference/android/webkit/WebSettings) 来启用 JavaScript。
- 使用 JavaScript 访问已注入到 `WebView` 的 Android 框架对象。



### 主题：

#### 1，替换css:



#### 2，加载本地html：



### 标准代码：





### 解说：

对---WebView的基本使用.pdf的解说：





## 无脑操作：

### 对现有项目的一个简单的描述（这个部分可以广而推之）：

我看到了什么，把我看到了的简单描述在下面：
1，和js，css，html打交道
2，和网址打交道，编码打交道utf8
3，和输入流，输出流打交道，
4，和https，http打交道，
5，使用了缓存
6，对是不是https,http,是否包含image等的判断。
7，各种client的合集。
8，各种本地缓存的css,js，盲猜是从网上的下下来，然后自己改的，这个猜想的可能性相当的大，我就会这样干。
9，Script对象的直接的创建
10，增加对于对象的Script对象的操作。
11，缓存动用了数据库了

### 对参数的自我的理解：





### 思绪飘扬：

1，这个与网页访问的区别和联系，如何理解。
2，我要达到一个标准，超越现在的一切的网文，在不看他们之前，我就知道了大概的论述的范畴。





### 参考：

https://developer.android.com/guide/webapps/webview
https://blog.csdn.net/carson_ho/article/details/52693322
https://developer.android.com/guide/webapps/best-practices?hl=zh-cn
https://developers.google.com/speed?hl=zh-cn
https://developer.android.com/reference/android/webkit/WebView