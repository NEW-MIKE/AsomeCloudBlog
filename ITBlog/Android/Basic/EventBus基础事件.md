EventBus基础事件

### 1，原理：

在这个部分，就是发送事件，接收事件。

步骤一：定义事件，bean。

```cpp
public class MessageEvent{
    private String message;
    public  MessageEvent(String message){
        this.message=message;
    }
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
}
```

步骤二，注册事件：

```css
@Override
protected void onCreate(Bundle savedInstanceState) {           
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main)；
     EventBus.getDefault().register(this)；
} 
```

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
}
```

步骤三，发送事件：
EventBus.getDefault().post(messageEvent);

步骤四，处理事件：

```java
@Subscribe(threadMode = ThreadMode.MAIN)
public void XXX(MessageEvent messageEvent) {
    ...
}
```



ps：其他

```css
EventBus.getDefault().postSticky(messageEvent);
```

```java
@Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
public void XXX(MessageEvent messageEvent) {
    ...
}
```





