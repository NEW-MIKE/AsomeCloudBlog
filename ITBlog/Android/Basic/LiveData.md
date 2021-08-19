LiveData：
部分一：语句推导：
[`LiveData`](https://developer.android.google.cn/reference/androidx/lifecycle/LiveData?hl=zh-cn) 是一种可观察的数据存储器类。---->其操作的对象是数据。





部分二：一处更新，所有观察者都更新：
实验：
步骤1：申明一个LiveData:

```
public class NameViewModel extends ViewModel {

// Create a LiveData with a String
private MutableLiveData<String> currentName;

    public MutableLiveData<String> getCurrentName() {
        if (currentName == null) {
            currentName = new MutableLiveData<String>();
        }
        return currentName;
    }

// Rest of the ViewModel...
}
```

步骤2：进行订阅绑定：

```
public class NameActivity extends AppCompatActivity {

    private NameViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Other code to setup the activity...

        // Get the ViewModel.
        model = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(NameViewModel.class);


        // Create the observer which updates the UI.
        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                nameTextView.setText(newName);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.getCurrentName().observe(this, nameObserver);
    }
}
```

步骤3：更新LiveData：

```
button.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        String anotherName = "John Doe";
        model.getCurrentName().setValue(anotherName);
    }
});
```

步骤4，新建一个新的Activity：

```
   public static void actionStart(Context context, String data1, String data2) {
    Intent intent = new Intent(context, SecondActivity.class);
    intent.putExtra("param1", data1);
    intent.putExtra("param2", data2);
    context.startActivity(intent);
}
```

小结：其总的链条就是通过通过设置一个监听的链条，在设置一个LiveData的变量的时候，促发整个链条的动作起来。

步骤5：小demo：

```
package com.kaya.livedatademo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private NameViewModel model;
    private TextView nameTextView,secondTextView;
    private Button nameButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTextView = findViewById(R.id.nameTextId);
        secondTextView = findViewById(R.id.secondTextView);
        nameButton = findViewById(R.id.button);

        // Get the ViewModel.
        model = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(NameViewModel.class);

        // Create the observer which updates the UI.
        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                nameTextView.setText(newName);
            }
        };
        final Observer<String> secondObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                secondTextView.setText(newName);
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        LiveData<String> newName = Transformations.map(model.getCurrentName(),ddd -> ddd+"1234");
        newName.observe(this, nameObserver);
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.

        LiveData<String> newStringData = Transformations.switchMap(model.getCurrentName(),ddd -> getUser(ddd));
        newStringData.observe(this,secondObserver);

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String anotherName = "John Doe";
                model.getCurrentName().setValue(anotherName);
               // LiveActivity.actionStart(MainActivity.this);
            }
        });
    }
    private LiveData<String> getUser(String str) {
        MutableLiveData<String> temp = new MutableLiveData<>();
        temp.setValue(str.toLowerCase());;
        return temp;
    }
}
```

###  [`将协程与 LiveData 一起使用`](https://developer.android.google.cn/topic/libraries/architecture/coroutines?hl=zh-cn)

使用 [`LiveData`](https://developer.android.google.cn/topic/libraries/architecture/livedata?hl=zh-cn) 时，您可能需要异步计算值。例如，您可能需要检索用户的偏好设置并将其传送给界面。在这些情况下，您可以使用 `liveData` 构建器函数调用 `suspend` 函数，并将结果作为 `LiveData` 对象传送。

在以下示例中，`loadUser()` 是在其他位置声明的挂起函数。使用 `liveData` 构建器函数异步调用 `loadUser()`，然后使用 `emit()` 发出结果：

```kotlin
val user: LiveData<User> = liveData {
    val data = database.loadUser() // loadUser is a suspend function.
    emit(data)
}
```

`liveData` 构建块用作协程和 `LiveData` 之间的[结构化并发基元](https://medium.com/@elizarov/structured-concurrency-722d765aa952)。当 `LiveData` 变为活动状态时，代码块开始执行；当 `LiveData` 变为非活动状态时，代码块会在可配置的超时过后自动取消执行。如果代码块在完成前取消执行，则会在 `LiveData` 再次变为活动状态后重新开始执行；如果在上次运行中成功完成，则不会重启。请注意，代码块只有在自动取消的情况下才会重启。如果代码块由于任何其他原因（例如，抛出 `CancellationException`）而取消，则**不会**重启。

您还可以从代码块中发出多个值。每次 `emit()` 调用都会挂起代码块的执行，直到在主线程上设置 `LiveData` 值。



#### LiveData vs RxJava



































