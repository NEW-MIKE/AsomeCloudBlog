### 一，上代码：

1，项目代码：

```
@BindingAdapter(
    value = ["refreshing", "moreLoading", "hasMore"],
    requireAll = false
)
```

```
app:hasMore="@{vm.hasMore}"
app:moreLoading="@{vm.moreLoading}"
```





2，原理代码：

```
<layout xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto">
        <data>
            <variable
                name="viewmodel"
                type="com.myapp.data.ViewModel" />
        </data>
        <ConstraintLayout... /> <!-- UI layout's root element -->
    </layout>
    
```

上面的viewmodel可以直接与下面的控件进行数据绑定。

```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"/>
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.lastName}"/>
   </LinearLayout>
</layout>
    
```

```
public class User {
  private final String firstName;
  private final String lastName;
  public User(String firstName, String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
  }
  public String getFirstName() {
      return this.firstName;
  }
  public String getLastName() {
      return this.lastName;
  }
}

```



```
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
   User user = new User("Test", "User");
   binding.setUser(user);
}
```



```
ListItemBinding binding = ListItemBinding.inflate(layoutInflater, viewGroup, false);
// or
ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false);
```



```
android:text="@{user.displayName ?? user.lastName}"
```

### 二，原理分析：

这个部分的原理是，数据通过xml和使用的地方直接绑定，xml成为了数据绑定的中心。
其次，此处是与ViewHolder进行绑定的。

### 三，摘抄：

1，是mvvm在android上的一种实现
2，数据绑定库是一种支持库，借助该库，您可以使用声明性格式（而非程序化地）将布局中的界面组件绑定到应用中的数据源。
3，借助布局文件中的绑定组件，您可以移除 Activity 中的许多界面框架调用，使其维护起来更简单、方便。还可以提高应用性能，并且有助于防止内存泄漏以及避免发生 Null 指针异常。
4，

```
@BindingAdapter({"attribute_name1","attribute_name2","attribute_name3",……})
public static void method(View view,Type type1,Type type2,Type type3,……){
    //TO DO:一些view的操作
    ……
}
```

```
@BindingAdapter({"attribute_name1","attribute_name2","attribute_name3",……})
public static void method(View view,Type type1,Type type2,Type type3,……){
    //TO DO:一些view的操作
    ……
}
```

### 四，参考：

https://developer.android.google.cn/topic/libraries/data-binding/binding-adapters?hl=zh-cn