## Android四大组件

### 如何掌握Activity

Activity：Android里面用来展示组件的功能点，可以通过申明自己为Activity，然后交给android系统，系统再交给硬件系统，来展示，通过硬件系统，捕捉到用户点击效果，传递给操作系统，系统再传递给Activity，进行处理，然后再将处理的结果传回给硬件。

作为android的基础的组件之一，在整个的设计过程中，承载了显示和交互的功能，要用好这个组件，要搞清楚这个组件都能干嘛，以及怎么做

我们首先要了解其生命周期，目的在于管理Activity进入和离开的时候，假如没有生命周期，会如何，我是这样理解的，系统资源的局限，代表了我们在设计应用的时候，不可能让应用处于一个稳定的状态中，必然会有内存的回收，那么，我们的系统如何管理这些活动呢，可以应对资源的局限性，可以提高资源的利用率。在使用形式上，我们应该对用户的操作做出必要的反应，这个是属于操作系统的层次。Google在这个层次上，充分的考虑到用户体验感，快速的反应，在Activity中，切换，销毁，都要对这些状态进行缓存和管理，控制。

Activity的所有生命周期，是不是就只是Acitivity所特有的呢，

在Activity的四种启动模式的玩法：

```java
Log.i("Www","OnCreate"+getClass().getSimpleName()+"TaskId"+getTaskId());
```

### 如何掌握Service

学习一个新的对象，首先明确这个对象的使命是什么，Service的使命是在后台执行任务，并且与用户是否推出应用脱离了关系，作为android，这样设计的目的，在于让用户在同一个设备上，能够同时运行多个App。在播放音乐，后台下载，后台运行传感器数据采集等方面，不需要用户参与的工作，可以安排到后台执行。Service分为本地的和远程的，

服务的运行模型：启动服务，服务运行起来，在绑定服务，其中关联数据的采集，采集过来的数据就存储在绑定的参数里面

如何合理安排任务在各个的函数中：这个只能在各种软件中去寻找，然后熟悉。

如果要想掌握这个服务，首先，其大脑内部要能够知道服务到底是什么东西。然后是怎么使用的，这个人的大脑里面，要能够将这个过程捋一遍，之后就会明白了它的套路。然后，借用开发软件，将服务的开启和绑定进行进行测试，经常测试。在xml里面创建四个按键：

```xml
<Button
    android:id="@+id/start"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="启动服务"
    android:onClick="operate" />

<Button
    android:id="@+id/stop"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="停止服务"
    android:onClick="operate" />

<Button
    android:id="@+id/bind"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="绑定服务"
    android:onClick="operate" />

<Button
    android:id="@+id/unbind"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="解绑服务"
    android:onClick="operate"/>
```

然后再Activity中将这几个按键进行实例化：

```java
private ServiceConnection conn = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.e("TAG","慕课");
        MyService.MyBinder mb = (MyService.MyBinder) iBinder;
        int step = mb.getProcess();
        Log.e("TAG","当前进度是：" + step);

    }

    //当客户端和服务的连接丢失了
    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
};

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
}

public void operate(View v){
    switch (v.getId()){
        case R.id.start:
            //启动服务:创建-->启动-->销毁
            //如果服务已经创建了，后续重复启动，操作的都是同一个服务，不会再重新创建了，除非你先销毁它
            Intent it1 = new Intent(this,MyService.class);
            startService(it1);
            break;
        case R.id.stop:
            Intent it2 = new Intent(this,MyService.class);
            stopService(it2);
            break;
        case R.id.bind:
            //绑定服务：最大的 作用是用来实现对Service执行的任务进行进度监控
            //如果服务不存在： onCreate-->onBind-->onUnbind-->onDestory
            // （此时服务没有再后台运行，并且它会随着Activity的摧毁而解绑并销毁）
            //服务已经存在：那么bindService方法只能使onBind方法被调用，而unbindService方法只能使onUnbind被调用
            Intent it3 = new Intent(this,MyService.class);
            bindService(it3, conn,BIND_AUTO_CREATE);

        break;
        case R.id.unbind:
            //解绑服务
            unbindService(conn);
            break;
    }
}
```



建立一个服务，并完成其构造函数：

```java
public MyService() {
}

private int i;
//创建[34                                                                                                                                                                                                                                  
@Override
public void onCreate() {
    super.onCreate();
    Log.e("TAG","服务创建了");
    //开启一个线程（从1数到100），用于模拟耗时的任务
    new Thread(){
        @Override
        public void run() {
            super.run();
            try {
                for (i = 1; i <= 100; i++) {
                    sleep(1000);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }.start();
}

//启动
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    Log.e("TAG","服务启动了");
    return super.onStartCommand(intent, flags, startId);
}

//绑定
//IBinder：在android中用于远程操作对象的一个基本接口
@Override
public IBinder onBind(Intent intent) {
    Log.e("TAG","服务绑定了");
    return new MyBinder();
}

//对于onBind方法而言，要求返回IBinder对象
//实际上，我们会自己定义一个内部类，集成Binder类

class MyBinder extends Binder{
    //定义自己需要的方法（实现进度监控）
    public int getProcess(){
        return i;
    }
}

//解绑
@Override
public boolean onUnbind(Intent intent) {
    Log.e("TAG","服务解绑了");
    return super.onUnbind(intent);
}

//摧毁
@Override
public void onDestroy() {
    super.onDestroy();
    Log.e("TAG","服务销毁了");
}
```

那么在测试的时候，有以下这几种情况，我们的目的，是为了验证，

1，绑定创建的服务，是不是只能由解绑结束

2，绑定之后，有开始，解绑是否会能够结束服务

只要没有任何的关联了，就可以停止了呢：

如果没有绑定，直接解绑，是会导致闪退的。

Service：作为一个不需要与用户交互的存在，默默在背后处理自己的任务，

#### 二，AIDL如何玩

AIDL是用来进行远程通信的，只是其启动绑定是在其他应用，

玩这个需要两个端，服务端和客户端，服务端在自己的aidl中申明：

```java
void showProgress();
```

generate：远程的数据进行传输，是要使用这个来进行传输的，虽然可以进行启动，但是，其数据的传输需要借助特定的手段来执行，

```java
public MyService() {
}

private int i;
//创建[34                                                                                                                                                                                                                                  
@Override
public void onCreate() {
    super.onCreate();
    Log.e("TAG","服务创建了");
    //开启一个线程（从1数到100），用于模拟耗时的任务
    new Thread(){
        @Override
        public void run() {
            super.run();
            try {
                for (i = 1; i <= 100; i++) {
                    sleep(1000);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }.start();
}

//启动
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    Log.e("TAG","服务启动了");
    return super.onStartCommand(intent, flags, startId);
}

//绑定
//IBinder：在android中用于远程操作对象的一个基本接口
@Override
public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    Log.e("TAG","服务绑定了");
    //Binder
    return new IMyAidlInterface.Stub(){
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void showProgress() throws RemoteException {
            Log.e("TAG","当前进度是" + i);
        }
    };
}

//对于onBind方法而言，要求返回IBinder对象
//实际上，我们会自己定义一个内部类，集成Binder类

class MyBinder extends Binder{
    //定义自己需要的方法（实现进度监控）
    public int getProcess(){
        return i;
    }
}

//解绑
@Override
public boolean onUnbind(Intent intent) {
    Log.e("TAG","服务解绑了");
    return super.onUnbind(intent);
}

//摧毁
@Override
public void onDestroy() {
    super.onDestroy();
    Log.e("TAG","服务销毁了");
}
```

```java
IMyAidlInterface imai = IMyAidlInterface.Stub.asInterface(iBinder);
```

在使用的时候，样式如上所示。

在客户端，按照如下执行：

```java
ServiceConnection conn = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        IMyAidlInterface imai = IMyAidlInterface.Stub.asInterface(iBinder);
        try {
            imai.showProgress();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
};
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
}

public void operate(View v){
    switch(v.getId()){
        case R.id.start:
            //远程启动服务
            Intent it = new Intent();
            it.setAction("com.imooc.myservice");
            it.setPackage("com.example.servicedemo");
            startService(it);
            break;
        case R.id.stop:
            Intent it2 = new Intent();
            it2.setAction("com.imooc.myservice");
            it2.setPackage("com.example.servicedemo");
            stopService(it2);
            break;
        case R.id.bind:
            Intent it3 = new Intent();
            it3.setAction("com.imooc.myservice");
            it3.setPackage("com.example.servicedemo");
            bindService(it3,conn,BIND_AUTO_CREATE);
            break;
        case R.id.unbind:
            unbindService(conn);
            break;

    }
}
```

如此，就可以实现，在远端获取服务提供的数据了。



### 如何掌握ContentProvider

获取系统的里面的短信息：由于是读取系统的信息，那么，就必须提供对应的权限。

其xml为：

```xml
<Button
    android:id="@+id/sms_btn"
    android:layout_width="match_parent" android:layout_height="wrap_content"
    android:text="点击访问短信箱"  />

<Button
    android:id="@+id/read_contact_btn"
    android:layout_width="match_parent" android:layout_height="wrap_content"
    android:text="点击读取联系人"  />

<Button
    android:id="@+id/add_contact_btn"
    android:layout_width="match_parent" android:layout_height="wrap_content"
    android:text="点击添加联系人"  />
```

```xml
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:id="@+id/id_txt"/>

<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:id="@+id/name_txt"/>

<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:id="@+id/age_txt"/>

<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:id="@+id/gender_txt"/>
```



```java
        findViewById(R.id.sms_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1.获取内容处理者
                ContentResolver resolver = getContentResolver();
                //2.查询方法
                //sms: short message service
                //    content://sms     短信箱
                //    content://sms/inbox     收件箱
                //     content://sms/sent       发件箱
                //      content://sms/draft     草稿箱
                Uri uri = Uri.parse("content://sms/draft");
                Cursor c = resolver.query(uri,null,null,null,null);
                //3.解析Cursor
                //遍历Cursor
                while(c.moveToNext()){
                    //对象，内容
                    //参数：列索引
                    //c.getString(2);
                    //遍历该行的列
                    String msg = "";

                    String address = c.getString(c.getColumnIndex("address"));
                    String body = c.getString(c.getColumnIndex("body"));

                    msg = address + ":" + body;
                    /*
                    for(int i = 0 ; i < c.getColumnCount() ; i++){
                        msg += c.getString(i) + "  ";
                    }
                    */
                    Log.e("TAG",msg);
                }
            }
        });

        findViewById(R.id.read_contact_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver resolver = getContentResolver();
                //对于联系人而言，他们的存储方式是将姓名和其他内容（电话号码）由不同点contentProvider操作的
                //首先想象姓名和其他内容属于不同的表
                //而姓名所在的表是主表，其他内容位于从表
                //而主表中的主键会在从表中作为外键使用
                Cursor c1 = resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                while(c1.moveToNext()){

//                    ContactsContract.Contacts.DISPLAY_NAME    姓名
//                    ContactsContract.Contacts._ID     主键
                    String name = c1.getString(c1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String _id = c1.getString(c1.getColumnIndex(ContactsContract.Contacts._ID ));
                    Log.e("TAG","姓名是：" + name +" , id是" + _id);

                    String selections = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
                    Cursor c2 = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            selections,
                            new String[]{_id},
                            null);
                    while(c2.moveToNext()){
                        String number = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        name += "   " + number;
                    }

                    Log.e("TAG" , name);
                    /*
                    String msg = "";
                    for(int i = 0 ; i < c1.getColumnCount() ; i++){
                        msg += c1.getString(i) + "  ";
                    }
                    Log.e("TAG",msg);
                    */

                }
            }
        });

        findViewById(R.id.add_contact_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver resolver = getContentResolver();

                //1.往一个ContentProvider中插入一条空数据，获取新生成的id
                //2.利用刚刚生成的id分别组合姓名和电话号码往另一个ContentProvider中插入数据U
                ContentValues values = new ContentValues();
                Uri uri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI,values);
                long id = ContentUris.parseId(uri);

                //插入姓名
                //指定姓名列的内容
                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,"Tommy");
                //指定和姓名关联的编号列的内容
                values.put(ContactsContract.Data.RAW_CONTACT_ID,id);
                //指定该行数据的类型
                values.put(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                resolver.insert(ContactsContract.Data.CONTENT_URI,values);

                //插入电话号码
                //清空ContentValues对象
                values.clear();
                //指定电话号码列的内容
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "15789065588");
                //指定和电话号码关联的编号列的内容
                values.put(ContactsContract.Data.RAW_CONTACT_ID,id);
                //指定该行数据的类型
                values.put(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                //指定联系方式的类型
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);

                resolver.insert(ContactsContract.Data.CONTENT_URI,values);
            }
        });
    }
}
```

自己编写来定：

```java
    ContentResolver resolver;
    private EditText nameEdt , ageEdt , idEdt;
    private RadioGroup rg;
    private ListView stuList;
    private String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取ContentResovler对象
        resolver = getContentResolver();
//        resolver.query();
           // resolver.insert()
//            resolver.delete()
//            resolver.update()

        nameEdt = (EditText) findViewById(R.id.name_edt);
        ageEdt = (EditText) findViewById(R.id.age_edt);
        idEdt = (EditText) findViewById(R.id.id_edt);

        stuList= (ListView) findViewById(R.id.stu_list);

        rg = (RadioGroup) findViewById(R.id.gender_rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.male){
                    gender = "男";
                }else{
                    gender = "女";
                }
            }
        });
    }

    public void operate(View v ){
        Uri uri = Uri.parse("content://com.imooc.myprovider");

        String name = nameEdt.getText().toString();
        String age = ageEdt.getText().toString();
        String _id = idEdt.getText().toString();
        switch (v.getId()){
            case R.id.insert_btn:
                //参数1：URI（Uniform Resource Identifier,同一资源定位符）对象，content://authorities[/path]
                //参数2：
                ContentValues values = new ContentValues();
                values.put("name",name);
                values.put("age",age);
                values.put("gender",gender);
                Uri uri2 = resolver.insert(uri,values);
                long id = ContentUris.parseId(uri2);
                Toast.makeText(this,"添加成功，新学生的学号是：" + id , Toast.LENGTH_SHORT).show();
                break;
            case R.id.query_btn:
                //查询所有。
                //参数2：查询列，为null代表查询所有
                Cursor c = resolver.query(uri,null,null,null,null);
                //参数2：每一个学员信息对象所显示的样式布局
                //参数3：数据源
                //参数4：查询结果中所有列的列名
                //参数5：数据未来所要加载到的对应控件的id数组
                //
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this ,
                        R.layout.item,
                        c,
                        new String[]{"_id","name","age","gender"},
                        new int[]{R.id.id_txt,R.id.name_txt,R.id.age_txt,R.id.gender_txt},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

                stuList.setAdapter(adapter);
                break;
            case R.id.delete_btn:
                int result = resolver.delete(uri , "_id=?",new String[]{_id});
                if(result > 0){
                    Toast.makeText(this , "删除成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "删除失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.update_btn:

                ContentValues values2 = new ContentValues();
                values2.put("name",name);
                values2.put("age",age);
                values2.put("gender",gender);
                int result2 = resolver.update(uri ,values2,"_id=?", new String[]{_id} );
                if(result2 > 0){
                    Toast.makeText(this , "修改成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mather_btn:
                resolver.delete(Uri.parse("content://com.imooc.myprovider/helloworld"),null,null);
                resolver.delete(Uri.parse("content://com.imooc.myprovider/helloworld/abc"),null,null);
                resolver.delete(Uri.parse("content://com.imooc.myprovider/helloworld/123"),null,null);
                resolver.delete(Uri.parse("content://com.imooc.myprovider/helloworld/090"),null,null);
                resolver.delete(Uri.parse("content://com.imooc.myprovider/helloworld/89ii"),null,null);
                resolver.delete(Uri.parse("content://com.imooc.myprovider/nihaoshijie/ab90"),null,null);
                break;
            case R.id.uri_btn:
                Uri uri1 = resolver.insert(Uri.parse("content://com.imooc.myprovider/whatever?name=张三&age=23&gender=男"),
                        new ContentValues());
                long id2 = ContentUris.parseId(uri1);
                Toast.makeText(this , "添加成功，也意味着uri解析成功，新学员学号是：" + id2 , Toast.LENGTH_SHORT).show();
                break;
        }
    }
```

xml文件为：

```xml
<EditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="姓名:"
    android:layout_margin="5dp"
    android:id="@+id/name_edt"/>


<EditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="年龄:"
    android:layout_margin="5dp"
    android:id="@+id/age_edt"/>

<RadioGroup
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:layout_margin="5dp"
    android:id="@+id/gender_rg">
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="性别："/>
    <RadioButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="男"
        android:layout_marginLeft="10dp"
        android:id="@+id/male"/>
    <RadioButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="女"
        android:layout_marginLeft="10dp"
        android:id="@+id/female"/>
</RadioGroup>
<EditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="输入你想要操作的学员的编号"
    android:layout_margin="5dp"
    android:id="@+id/id_edt"/>
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal">
    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="添加"
        android:id="@+id/insert_btn"
        android:onClick="operate"/>
    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="查询"
        android:id="@+id/query_btn"
        android:onClick="operate"/>
    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="删除"
        android:id="@+id/delete_btn"
        android:onClick="operate"/>
    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="修改"
        android:id="@+id/update_btn"
        android:onClick="operate"/>
</LinearLayout>
<Button
    android:id="@+id/mather_btn"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="利用UriMatcher去解析Uri"
    android:onClick="operate"/>
<Button
    android:id="@+id/uri_btn"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="利用URI自带的解析方法解析Uri"
    android:onClick="operate"/>
<ListView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/stu_list"></ListView>
```



自己编写内容提供者：

```java 
//URI的解析
//1.UriMatcher：在contentProvider创建时，制定好匹配规则，当调用了ContentProvider中的操作方法时
//利用匹配类去匹配传的uri，根据不同的uri给出不同的处理
//2.Uri自带解析方法

UriMatcher matcher;
public MyContentProvider() {
}

@Override
public int delete(Uri uri, String selection, String[] selectionArgs) {
    // Implement this to handle requests to delete one or more rows.
    //                                             "_id=?"
    int result = 0;
    int code = matcher.match(uri);
    switch (code) {
        case 1000:
            Log.e("TAG","匹配到的路径是helloworld");
            break;
        case 1001:
            Log.e("TAG","匹配到的路径是helloworld/abc");
            break;
        case 1002:
            Log.d("TAG","匹配到路径为helloworld/任意数字的内容");
            break;
        case 1003:
            Log.i("TAG","匹配到路径为nihaoshijie/任意字符的内容");
            break;
        default:
            Log.e("TAG","执行删除数据库内容的方法");
            result = db.delete("info_tb", selection, selectionArgs);
            break;
    }
    return result;
}

@Override
public String getType(Uri uri) {
    // TODO: Implement this to handle requests for the MIME type of the data
    // at the given URI.
    throw new UnsupportedOperationException("Not yet implemented");
}

@Override
public Uri insert(Uri uri, ContentValues values) {
    // TODO: Implement this to handle requests to insert a new row.
    Log.e("TAG","调用了DataProviderDemo中的insert方法");
    long id = 0;
    if(values.size() > 0) {
        id = db.insert("info_tb", null, values);
    }else{
        String authority = uri.getAuthority();
        String path = uri.getPath();
        String query = uri.getQuery();
        String name = uri.getQueryParameter("name");
        String age = uri.getQueryParameter("age");
        String gender = uri.getQueryParameter("gender");
        Log.e("TAG","主机名：" + authority + "，路径：" + path + "，查询数据：" + query
                + "，姓名：" + name + "，年龄：" + age + ",性别：" + gender);
        values.put("name" , name);
        values.put("age",age);
        values.put("gender",gender);
        id = db.insert("info_tb",null,values);
    }
    //将id追加到uri后面
    return  ContentUris.withAppendedId(uri,id);
}

//在ContentProvider创建调用
SQLiteDatabase db;
@Override
public boolean onCreate() {
    // TODO: Implement this to initialize your content provider on startup.
    SQLiteOpenHelper helper = new SQLiteOpenHelper(getContext() , "stu.db" , null , 1) {
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql = "create table info_tb (_id integer primary key autoincrement," +
                    "name varchar(20)," +
                    "age integer," +
                    "gender varchar(2))";
            sqLiteDatabase.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    };

    db = helper.getReadableDatabase();

    //参数：代表无法匹配
    //     content://com.imooc.myprovider/helloworld
    matcher = new UriMatcher(UriMatcher.NO_MATCH);
    matcher.addURI("com.imooc.myprovider","helloworld",1000);
    matcher.addURI("com.imooc.myprovider","helloworld/abc",1001);
    matcher.addURI("com.imooc.myprovider","helloworld/#",1002);
    matcher.addURI("com.imooc.myprovider","nihaoshijie/*",1003);

    //matcher.match()
    //返回true
    return true;
}

@Override
public Cursor query(Uri uri, String[] projection, String selection,
        String[] selectionArgs, String sortOrder) {
    // TODO: Implement this to handle query requests from clients.
    //参数2：索要查询的列
    //参数3：查询条件
    //参数4:查询条件值
    //参数5：分组
    //参数6：分组条件
    //参数7：排序
    Cursor c = db.query("info_tb",projection,selection,selectionArgs,null,null,sortOrder);
    return  c;
}

@Override
public int update(Uri uri, ContentValues values, String selection,
                  String[] selectionArgs) {
    // TODO: Implement this to handle requests to update one or more rows.
    //  update info_tb set name = 'xx'  , age = 20 , gender = '男' where _id = 2
    int result = db.update("info_tb",values,selection,selectionArgs);
    return result;
}
```

### 如何掌握BroadcastReceiver

自己发送，自己接收

```Java
public static final String MY_ACTION = "com.imooc.demo.afdsabfdaslj";
public static final String BROADCAST_CONTENT = "broadcast_content";
private ImoocBroadcastReceiver mBroadcastReceiver;
private EditText mInputEditText;
private Button mSendBroadcastButton;
private TextView mResultTextView;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // 用包名做title
    setTitle(getPackageName());

    mInputEditText = findViewById(R.id.inputEditText);
    mSendBroadcastButton = findViewById(R.id.sendBroadcastButton);
    mResultTextView = findViewById(R.id.resultTextView);



    // 新建广播接收器
    mBroadcastReceiver = new ImoocBroadcastReceiver(mResultTextView);

    // 注册广播接收器

    // 为广播接收器添加Action
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
    intentFilter.addAction(MY_ACTION);

    // 注册广播接收器
    registerReceiver(mBroadcastReceiver, intentFilter);


    mSendBroadcastButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 新建广播
            Intent intent = new Intent(MY_ACTION);
            // 放入广播要携带的数据
            intent.putExtra(BROADCAST_CONTENT, mInputEditText.getText().toString());
            sendBroadcast(intent);
        }
    });

}

@Override
protected void onDestroy() {
    super.onDestroy();
    // 取消注册广播接收器，不然会导致内存泄露
    if(mBroadcastReceiver != null){
        unregisterReceiver(mBroadcastReceiver);
    }
}
```

```JAVA
TextView mTextView;
public ImoocBroadcastReceiver() {
}

public ImoocBroadcastReceiver(TextView textView) {
    mTextView = textView;
}

private static final String TAG = "ImoocBroadcastReceiver";
@Override
public void onReceive(Context context, Intent intent) {
    // 接收广播
    if(intent != null){
        // 接收到的是什么广播
        String action  = intent.getAction();
        Log.d(TAG, "onReceive: " + action);

        // 判断是什么广播（是不是我们自己发送的自定义广播）
        if(TextUtils.equals(action, MainActivity.MY_ACTION)){
            // 获取广播携带的内容， 可自定义的数据
            String content = intent.getStringExtra(MainActivity.BROADCAST_CONTENT);
            if(mTextView != null){
                mTextView.setText("接收到的action是："+ action + "\n接收到的内容是：\n" + content);
            }
        }
    }
}
```

xml

```xml
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="请输入发送内容："/>

<EditText
    android:id="@+id/inputEditText"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:layout_marginTop="16dp"
    />

<Button
    android:id="@+id/sendBroadcastButton"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="16dp"
    android:layout_gravity="center_horizontal"
    android:text="发送广播"/>

<TextView
    android:id="@+id/resultTextView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="16dp"
    android:text="收到的内容："/>
```

### 如何掌握Fragment

Fragment定义的时候，自己继承，

```java
package iom.imooc.fragmentdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 列表fragment.
 */
public class ListFragment extends Fragment {


    public static final String BUNDLE_TITLE = "bundle_title";
    private String mTitle = "imooc";
    private User mUser;

    public void setUser(User user) {
        mUser = user;
    }

    public class User {

    }


    public static ListFragment newInstance(String title, User user){
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        fragment.setArguments(bundle);
        fragment.setUser(user);
        return fragment;
    }
    public static ListFragment newInstance(String title){
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mTitle = getArguments().getString(BUNDLE_TITLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // 创建视图
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // new view
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        TextView textView = view.findViewById(R.id.textView);

        textView.setText(mTitle);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnTitleClickListener != null){
                    mOnTitleClickListener.onClick(mTitle);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    // 设置接口的方法
    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        mOnTitleClickListener = onTitleClickListener;
    }

    // 定义变量
    private OnTitleClickListener mOnTitleClickListener;

    // 定义接口
    public interface OnTitleClickListener{
        void onClick(String title);
    }
}
```

其中，Fragment内部的布局是这样的：

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@color/colorAccent">


    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:text="TextView"
        android:textColor="#FFFFFF"
        android:textSize="20sp"/>
</RelativeLayout>
```

在布局里面申明两个fragment控件

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="iom.imooc.fragmentdemo.MainActivity">

   <fragment
       android:id="@+id/listFragment"
       android:name="iom.imooc.fragmentdemo.ListFragment"
       android:layout_width="100dp"
       android:layout_height="100dp"/>


   <fragment
       android:id="@+id/detailFragment"
       android:name="iom.imooc.fragmentdemo.ListFragment"
       android:layout_centerInParent="true"
       android:layout_width="100dp"
       android:layout_height="100dp"/>

</RelativeLayout>
```

申明控件容器：

```xml
<LinearLayout
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:orientation="horizontal"
        android:id="@+id/listContainer"
        android:layout_width="150dp"
        android:layout_margin="1dp"
        android:layout_height="match_parent">

    </LinearLayout>

    <LinearLayout
        android:orientation="horizontal"
        android:id="@+id/detailContainer"
        android:layout_width="200dp"
        android:layout_margin="1dp"
        android:layout_height="match_parent">

    </LinearLayout>

</LinearLayout>
```

将控件和Fragment进行关联

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views on onclick event.

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // static load fragment.
            startActivity(new Intent(MainActivity.this, StaticLoadFragmentActivity.class));
            }
        });

        // 1. container  2. fragment  3. fragment-->container


        // activity--->fragment value
        ListFragment listFragment = ListFragment.newInstance("list");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.listContainer, listFragment)
                .commit();
        listFragment.setOnTitleClickListener(this);

        ListFragment detail = ListFragment.newInstance("detail");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detailContainer, detail)
                .commit();

        detail.setOnTitleClickListener(this);


    }

    @Override
    public void onClick(String title) {
        setTitle(title);
    }
}
```





## 网络通信



### UDP

```java
package sample02;//package com.imooc.sample.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;



public class UdpClient {

    private String mServerIp = "192.168.52.1";
    private InetAddress mServerAddress;
    private int mServerPort = 7777;
    private DatagramSocket mSocket;
    private Scanner mScanner;


    public UdpClient() {
        try {
            mServerAddress = InetAddress.getByName(mServerIp);
            mSocket = new DatagramSocket();
            mScanner = new Scanner(System.in);
            mScanner.useDelimiter("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {

            try {
                String clientMsg = mScanner.next();
                byte[] clientMsgBytes = clientMsg.getBytes();
                DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes,
                        clientMsgBytes.length, mServerAddress, mServerPort);
                mSocket.send(clientPacket);

                System.out.println(clientMsg.toString());

                byte[] buf = new byte[1024];
                DatagramPacket serverMsgPacket = new DatagramPacket(buf ,buf.length);
                mSocket.receive(serverMsgPacket);
                String serverMsg = new String(serverMsgPacket.getData(),
                        0, serverMsgPacket.getLength());
                System.out.println("msg = " + serverMsg);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String[] args){
        new UdpClient().start();
    }

}
```

```java
package sample02;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpServer {

    private InetAddress mInetAddress;
    private int mPort = 7777;
    private DatagramSocket mSocket;

    private Scanner mScanner;


    public UdpServer() {
        try {
            mInetAddress = InetAddress.getLocalHost();
            mSocket = new DatagramSocket(mPort, mInetAddress);
System.out.println(mInetAddress.toString());
            mScanner = new Scanner(System.in);
            mScanner.useDelimiter("\n");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }


    public void start() {
        while (true) {


            try {
                byte[] buf = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
                mSocket.receive(receivedPacket);

                InetAddress address = receivedPacket.getAddress();
                int port = receivedPacket.getPort();
                String clientMsg = new String(receivedPacket.getData(),
                        0, receivedPacket.getLength());
                System.out.println("address = " + address
                        + " , port = " + port + " , msg = " + clientMsg);

                String returnedMsg = mScanner.next();
                byte[] returnedMsgBytes = returnedMsg.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(returnedMsgBytes,
                        returnedMsgBytes.length, receivedPacket.getSocketAddress());
                mSocket.send(sendPacket);


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String[] args){
        new UdpServer().start();
    }


}
```

### TCP

```JAVA
package sample02.tcp.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class TcpServer {

    public void start() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9090);
            MsgPool.getInstance().start();

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("ip = " + socket.getInetAddress().getHostAddress()
                        + " , port = " + socket.getPort() + " is online...");


                ClientTask clientTask = new ClientTask(socket);
                MsgPool.getInstance().addMsgComingListener(clientTask);
                clientTask.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        new TcpServer().start();
    }

}
```

```JAVA
package sample02.tcp.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;



public class TcpClient {

    private Scanner mScanner;

    public TcpClient() {
        mScanner = new Scanner(System.in);
        mScanner.useDelimiter("\n");
    }


    public void start() {
        try {
            Socket socket = new Socket("192.168.52.1", 9090);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            // 输出服务端发送的数据
            new Thread() {
                @Override
                public void run() {
                    try {
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                    }

                }
            }.start();

            while (true) {
                String msg = mScanner.next();
                bw.write(msg);
                bw.newLine();
                bw.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new TcpClient().start();
    }

}
```

### 网络操作

Http请求的工具类

```java
/**
 * Http请求的工具类
 */
public class HttpUtils {

    private static final int TIMEOUT_IN_MILLIONS = 5000;

    public interface CallBack {
        void onRequestComplete(String result);
    }


    /**
     * 异步的Get请求
     *
     * @param urlStr
     * @param callBack
     */
    public static void doGetAsyn(final String urlStr, final CallBack callBack) {
        new Thread() {
            public void run() {
                try {
                    String result = doGet(urlStr);
                    if (callBack != null) {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            ;
        }.start();
    }


    /**
     * Get请求，获得返回数据
     *
     * @param urlStr
     * @return
     * @throws Exception
     */
    public static String doGet(String urlStr) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
            }
            conn.disconnect();
        }

        return null;

    }

}  
```

利用这个工具类来获取网络数据，

```java
/**
 * Created by baidu on 2018/6/10.
 */

public class ChapterBiz {

    private ChapterDao chapterDao = new ChapterDao();

    public void loadDatas(final Context context, final CallBack callBack,
                          final boolean useCache) {
        AsyncTask<Boolean, Void, List<Chapter>> asyncTask
                = new AsyncTask<Boolean, Void, List<Chapter>>() {

            private Exception ex;

            @Override
            protected void onPostExecute(List<Chapter> chapters) {
                if (ex != null) {
                    callBack.loadFailed(ex);
                } else {
                    callBack.loadSuccess(chapters);
                }

            }

            @Override
            protected List<Chapter> doInBackground(Boolean... booleans) {
                final List<Chapter> chapters = new ArrayList<>();

                try {
                    // 从缓存中取
                    if (booleans[0]) {
                        chapters.addAll(chapterDao.loadFromDb(context));
                    }
                    Log.d("zhy", "loadFromDb -> " + chapters);

                    if (chapters.isEmpty()) {
                        // 从网络获取
                        final List<Chapter> chaptersFromNet = loadFromNet(context);
                        // 缓存在数据库
                        chapterDao.insertToDb(context, chaptersFromNet);
                        Log.d("zhy", "loadFromNet -> " + chaptersFromNet);
                        chapters.addAll(chaptersFromNet);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    ex = e;
                }
                return chapters;
            }
        };
        asyncTask.execute(useCache);
    }

    public static interface CallBack {
        void loadSuccess(List<Chapter> chapterList);

        void loadFailed(Exception ex);
    }

    public List<Chapter> loadFromNet(Context context) {

       // final String url = "https://www.wanandroid.com/tools/mockapi/2/mooc-expandablelistview";
        final String url = "https://www.imooc.com/api/expandablelistview";

        String content = HttpUtils.doGet(url);
        final List<Chapter> chapterList = parseContent(content);
        // 缓存到数据库
        chapterDao.insertToDb(context, chapterList);

        return chapterList;
    }

    private List<Chapter> parseContent(String content) {

        List<Chapter> chapters = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(content);
            int errorCode = jsonObject.optInt("errorCode");
            if (errorCode == 0) {
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject chapterJson = jsonArray.getJSONObject(i);
                    int id = chapterJson.optInt("id");
                    String name = chapterJson.optString("name");
                    Chapter chapter = new Chapter(id, name);
                    chapters.add(chapter);

                    JSONArray chapterItems = chapterJson.optJSONArray("children");
                    for (int j = 0; j < chapterItems.length(); j++) {
                        JSONObject chapterItemJson = chapterItems.getJSONObject(j);
                        id = chapterItemJson.optInt("id");
                        name = chapterItemJson.optString("name");
                        ChapterItem chapterItem = new ChapterItem(id, name);
                        chapter.addChild(chapterItem);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chapters;
    }

}
```

访问的数据的格式什么样的：

```java
public class ChapterItem {
    private int id;
    private String name;
    private int pid;

    public static final String TABLE_NAME = "tb_chapter_item";
    public static final String COL_ID = "_id";
    public static final String COL_PID = "pid";
    public static final String COL_NAME = "name";

    public ChapterItem() {
    }

    public ChapterItem(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pId) {
        this.pid = pId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

```java
public class Chapter {

    private int id;
    private String name;

    public static final String TABLE_NAME = "tb_chapter";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";

    private List<ChapterItem> children = new ArrayList<>();

    public Chapter() {
    }

    public Chapter(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChapterItem> getChildren() {
        return children;
    }

    public void addChild(ChapterItem child) {
        children.add(child);
        child.setPid(getId());
    }

    public void addChild(int id, String childName) {
        ChapterItem chapterItem = new ChapterItem(id, childName);
        chapterItem.setPid(getId());
        children.add(chapterItem);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
```



#### 第二种访问方式：

将访问的数据String化。

```java
private String requestDataByGet(String urlString) {
    String result = null;
    try {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(30000);
        connection.setRequestMethod("GET");  // GET POST
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            result = streamToString(inputStream);
        } else {
            String responseMessage = connection.getResponseMessage();
            Log.e(TAG, "requestDataByPost: " + responseMessage);
        }
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return result;
}
```

将Stream转化为String：

```java
public String streamToString(InputStream is) {
    try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        baos.close();
        is.close();
        byte[] byteArray = baos.toByteArray();
        return new String(byteArray);
    } catch (Exception e) {
        Log.e(TAG, e.toString());
        return null;
    }
}
```

将访问的数据UTF8化：

```java
/**
 * 将Unicode字符转换为UTF-8类型字符串
 */
public static String decode(String unicodeStr) {
    if (unicodeStr == null) {
        return null;
    }
    StringBuilder retBuf = new StringBuilder();
    int maxLoop = unicodeStr.length();
    for (int i = 0; i < maxLoop; i++) {
        if (unicodeStr.charAt(i) == '\\') {
            if ((i < maxLoop - 5)
                    && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr
                    .charAt(i + 1) == 'U')))
                try {
                    retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                    i += 5;
                } catch (NumberFormatException localNumberFormatException) {
                    retBuf.append(unicodeStr.charAt(i));
                }
            else {
                retBuf.append(unicodeStr.charAt(i));
            }
        } else {
            retBuf.append(unicodeStr.charAt(i));
        }
    }
    return retBuf.toString();
}
```

向服务器发送消息：

```java
    private String requestDataByPost(String urlString) {
        String result = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setRequestMethod("POST");

            // 设置运行输入,输出:
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // Post方式不能缓存,需手动设置为false
            connection.setUseCaches(false);
            connection.connect();

            // 我们请求的数据:
            String data = "username=" + URLEncoder.encode("imooc", "UTF-8")
                    + "&number=" + URLEncoder.encode("15088886666", "UTF-8");
            // 获取输出流
            OutputStream out = connection.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Reader reader = new InputStreamReader(inputStream, "UTF-8");
                char[] buffer = new char[1024];
                reader.read(buffer);
                result = new String(buffer);
                reader.close();
            } else {
                String responseMessage = connection.getResponseMessage();
                Log.e(TAG, "requestDataByPost: " + responseMessage);
            }

            final String finalResult = result;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(finalResult);
                }
            });

            connection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

处理JSON数据：

```java
private void handleJSONData(String json) {
    try {
        JSONObject jsonObject = new JSONObject(json);

        LessonResult lessonResult = new LessonResult();
        List<LessonResult.Lesson> lessonList = new ArrayList<>();
        int status = jsonObject.getInt("status");
        JSONArray lessons = jsonObject.getJSONArray("data");
        if (lessons != null && lessons.length() > 0) {
            for (int index = 0; index < lessons.length(); index++) {
                JSONObject item = (JSONObject) lessons.get(0);
                int id = item.getInt("id");
                String name = item.getString("name");
                String smallPic = item.getString("picSmall");
                String bigPic = item.getString("picBig");
                String description = item.getString("description");
                int learner = item.getInt("learner");

                LessonResult.Lesson lesson = new LessonResult.Lesson();
                lesson.setID(id);
                lesson.setName(name);
                lesson.setSmallPictureUrl(smallPic);
                lesson.setBigPictureUrl(bigPic);
                lesson.setDescription(description);
                lesson.setLearnerNumber(learner);
                lessonList.add(lesson);
            }
            lessonResult.setStatus(status);
            lessonResult.setLessons(lessonList);
            mTextView.setText("data is : " + lessonResult.toString());
        }

    } catch (JSONException e) {
        e.printStackTrace();
    }
}
```

#### 下载数据：

```java
private void download(String appUrl) {
    try {
        URL url = new URL(appUrl);
        URLConnection urlConnection = url.openConnection();

        InputStream inputStream = urlConnection.getInputStream();

        /**
         * 获取文件的总长度
         */

        int contentLength = urlConnection.getContentLength();

        String downloadFolderName = Environment.getExternalStorageDirectory()
                + File.separator + "imooc" + File.separator;

        File file = new File(downloadFolderName);
        if (!file.exists()) {
            file.mkdir();
        }

        String fileName = downloadFolderName + "imooc.apk";

        File apkFile = new File(fileName);

        if(apkFile.exists()){
            apkFile.delete();
        }

        int downloadSize = 0;
        byte[] bytes = new byte[1024];

        int length;

        OutputStream outputStream = new FileOutputStream(fileName);
        while ((length = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, length);
            downloadSize += length;
            /**
             * update UI
             */

            Message message = Message.obtain();
            message.obj = downloadSize * 100 / contentLength;
            message.what = DOWNLOAD_MESSAGE_CODE;
            mHandler.sendMessage(message);

        }
        inputStream.close();
        outputStream.close();


    } catch (MalformedURLException e) {
        notifyDownloadFaild();
        e.printStackTrace();
    } catch (IOException e) {
        notifyDownloadFaild();
        e.printStackTrace();
    }
}
```

### Handle通信

封装Handler 

```java
public static class DiglettHandler extends Handler{
    public static final int RANDOM_NUMBER = 500;
    public final WeakReference<DiglettActivity> mWeakReference;

    public DiglettHandler(DiglettActivity activity) {
        mWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        DiglettActivity activity = mWeakReference.get();

        switch (msg.what){
            case CODE:
                if(activity.mTotalCount > MAX_COUNT){
                    activity.clear();
                    Toast.makeText(activity, "地鼠打完了!", Toast.LENGTH_LONG ).show();
                    return;
                }

                int position = msg.arg1;
                activity.mDiglettImageView.setX(activity.mPosition[position][0]);
                activity.mDiglettImageView.setY(activity.mPosition[position][1]);
                activity.mDiglettImageView.setVisibility(View.VISIBLE);

                int randomTime = new Random().nextInt(RANDOM_NUMBER) + RANDOM_NUMBER;

                activity.next(randomTime);
                break;
        }

    }
}
```



```java
public static class TestHandler extends Handler{

    public final WeakReference<MainActivity> mWeakReference;
    public TestHandler(MainActivity activity) {
        mWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        MainActivity activity = mWeakReference.get();
        if(msg.what == CODE){
            int time = msg.arg1;
            activity.mTextview.setText(String.valueOf(time/1000));
            Message message = Message.obtain();
            message.what = CODE;
            message.arg1  = time - 1000;

            if (time > 0) {
                sendMessageDelayed(message, 1000);
            }
        }

    }
}
```

利用封装的Handler来发送消息：

```java
Message message = Message.obtain();
message.what = CODE;
message.arg1 = position;

mHandler.sendMessageDelayed(message, delayTime);
```

### AsyncTask

定义一个类继承这个基类：

```java
/**
 * String 入参
 * Integer 进度
 * Boolean 返回值
 */
public class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {
    String mFilePath;
    /**
     * 在异步任务之前，在主线程中
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // 可操作UI  类似淘米,之前的准备工作
        mDownloadButton.setText(R.string.downloading);
        mResultTextView.setText(R.string.downloading);
        mProgressBar.setProgress(INIT_PROGRESS);
    }

    /**
     * 在另外一个线程中处理事件
     *
     * @param params 入参  煮米
     * @return 结果
     */
    @Override
    protected Boolean doInBackground(String... params) {
        if(params != null && params.length > 0){
            String apkUrl = params[0];

            try {
                // 构造URL
                URL url = new URL(apkUrl);
                // 构造连接，并打开
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                // 获取了下载内容的总长度
                int contentLength = urlConnection.getContentLength();

                // 下载地址准备
                mFilePath = Environment.getExternalStorageDirectory()
                        + File.separator + FILE_NAME;

                // 对下载地址进行处理
                File apkFile = new File(mFilePath);
                if(apkFile.exists()){
                    boolean result = apkFile.delete();
                    if(!result){
                        return false;
                    }
                }

                // 已下载的大小
                int downloadSize = 0;

                // byte数组
                byte[] bytes = new byte[1024];

                int length;

                // 创建一个输入管道
                OutputStream outputStream = new FileOutputStream(mFilePath);

                // 不断的一车一车挖土,走到挖不到为止
                while ((length = inputStream.read(bytes)) != -1){
                    // 挖到的放到我们的文件管道里
                    outputStream.write(bytes, 0, length);
                    // 累加我们的大小
                    downloadSize += length;
                    // 发送进度
                    publishProgress(downloadSize * 100/contentLength);
                }

                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        // 也是在主线程中 ，执行结果 处理
        mDownloadButton.setText(result? getString(R.string.download_finish) : getString(R.string.download_finish));
        mResultTextView.setText(result? getString(R.string.download_finish) + mFilePath: getString(R.string.download_finish));

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        // 收到进度，然后处理： 也是在UI线程中。
        if (values != null && values.length > 0) {
            mProgressBar.setProgress(values[0]);
        }
    }

}
```

实例化这个类，并调用函数：

```java
// TODO: 16/12/19 下载任务
DownloadAsyncTask asyncTask = new DownloadAsyncTask();
asyncTask.execute(APK_URL);
```

### 通过接口来监听

```java
public interface OnDownloadListener{

    void onStart();

    void onSuccess(int code, File file);

    void onFail(int code , File file, String message);

    void onProgress(int progress);


    abstract class SimpleDownloadListener implements OnDownloadListener{
        @Override
        public void onStart() {

        }

        @Override
        public void onProgress(int progress) {

        }
    }
}
```

## View系类

### expandablelistview

基本使用的套路就是Adapter和View相结合

xml方面，在layout中申明：

```xml
<ExpandableListView
    android:id="@+id/id_expandable_listview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:groupIndicator="@null"
    android:indicatorLeft="0dp"
    android:indicatorRight="56dp"
    tools:context="com.imooc.expandablelistview_imooc.MainActivity">

</ExpandableListView>
```

然后准备父布局；

```xml
<?xml version="1.0" encoding="utf-8"?>

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:background="#44337dd7"
    android:layout_height="56dp"
    android:orientation="horizontal">

    <ImageView
        android:id="@+id/id_indicator_group"
        android:layout_width="24dp"
        android:layout_height="24dp"
        android:layout_gravity="center_vertical"
        android:background="@drawable/indicator_group"
        />


    <TextView
        android:id="@+id/id_tv_parent"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center_vertical"
        tools:text="Android"
        android:textSize="24dp"
        android:textStyle="bold">

    </TextView>

</LinearLayout>
```

在准备子布局；

```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/id_tv_child"
    android:layout_width="match_parent"
    android:layout_height="40dp"
    android:gravity="center_vertical"
    android:textSize="16sp"></TextView>
```

将父布局和子布局写入Adapter中：

```java
public class ChapterAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Chapter> mDatas;
    private LayoutInflater mInflater;

    public ChapterAdapter(Context context, List<Chapter> chapters) {
        mContext = context;
        mDatas = chapters;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    // TODO
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        ParentViewHolder vh;
        if (convertView == null) {
            // 修改item height即可演示，第二个参数作用
            convertView = mInflater.inflate(R.layout.item_parent_chapter, parent, false);
            vh = new ParentViewHolder();
            vh.tv = convertView.findViewById(R.id.id_tv_parent);
            vh.iv = convertView.findViewById(R.id.id_indicator_group);
            convertView.setTag(vh);

        } else {
            vh = (ParentViewHolder) convertView.getTag();
        }
        vh.tv.setText(mDatas.get(groupPosition).getName());
        vh.iv.setSelected(isExpanded);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        ChildViewHolder vh;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_child_chapter, parent, false);
            vh = new ChildViewHolder();
            vh.tv = convertView.findViewById(R.id.id_tv_child);

            convertView.setTag(vh);

        } else {
            vh = (ChildViewHolder) convertView.getTag();
        }
        vh.tv.setText(mDatas.get(groupPosition).getChildren().get(childPosition).getName());
        return convertView;
    }

    // 控制child item不可点击
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    public static class ParentViewHolder {
        TextView tv;
        ImageView iv;
    }

    public static class ChildViewHolder {
        TextView tv;
    }

}
```

在Activity中填充数据并设置点击效果：

```java
public class MainActivity extends AppCompatActivity {

    private Button mBtnRefresh;

    private ExpandableListView mExpandableListView;
    private ChapterAdapter mAdapter;
    private List<Chapter> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExpandableListView = findViewById(R.id.id_expandable_listview);
        mBtnRefresh = findViewById(R.id.id_btn_refresh);

//        mDatas = ChapterLab.generateDatas();
        mAdapter = new ChapterAdapter(this, mDatas);
        mExpandableListView.setAdapter(mAdapter);

        initEvents();
        loadDatas(true);


    }

    private ChapterBiz mChapterBiz = new ChapterBiz();

    private void loadDatas(boolean useCache) {
        mChapterBiz.loadDatas(this, new ChapterBiz.CallBack() {
            @Override
            public void loadSuccess(List<Chapter> chapterList) {
                Log.e("zhy", "loadSuccess  ");

                mDatas.clear();
                mDatas.addAll(chapterList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void loadFailed(Exception ex) {
                ex.printStackTrace();
                Log.e("zhy", "loadFailed ex= " + ex.getMessage());
            }
        }, useCache);
    }

    private void initEvents() {

        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatas(false);
            }
        });


        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d("zhy", "onGroupClick groupPosition = " + groupPosition);
                return false;
            }
        });


        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Log.d("zhy", "onChildClick groupPosition = "
                        + groupPosition + " , childPosition = " + childPosition + " , id = " + id);

                return false;
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            // 收回
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d("zhy", "onGroupCollapse groupPosition = " + groupPosition);

            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            // 展开
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d("zhy", "onGroupExpand groupPosition = " + groupPosition);

            }
        });

        mExpandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("zhy", "onItemClick position = " + position);

            }
        });


    }
}
```



### RecyclerView

先申明一个控件：

```xml
<android.support.v7.widget.RecyclerView
    android:id="@+id/recycler_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"></android.support.v7.widget.RecyclerView>
```

申明控件里面的子布局的样式：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:background="#A4D3EE"
    android:layout_margin="4dp">

    <ImageView
        android:id="@+id/iv"
        android:layout_width="88dp"
        android:layout_height="88dp"
        android:scaleType="fitXY"/>

    <TextView
        android:id="@+id/tv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:textSize="22sp"
        android:textColor="#fff"
        android:gravity="center_vertical"
        android:layout_marginLeft="8dp"/>

</LinearLayout>
```

定义Adapter：

```java
/**
 * 1、继承RecyclerView.Adapter
 * 2、绑定ViewHolder
 * 3、实现Adapter的相关方法
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private OnItemClickListener onItemClickListener;
    private RecyclerView mRv;
    private List<String> dataSource;
    private Context mContext;
    private int addDataPosition = -1;

    public MyRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        this.dataSource = new ArrayList<>();
        this.mRv = recyclerView;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setDataSource(List<String> dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    /**
     * 创建并且返回ViewHolder
     * @param viewGroup
     * @param position
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout, viewGroup, false));
    }

    /**
     * ViewHolder 绑定数据
     * @param myViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.mIv.setImageResource(getIcon(position));
        myViewHolder.mTv.setText(dataSource.get(position));

        /**
         * 只在瀑布流布局中使用随机高度
         */
        if (mRv.getLayoutManager().getClass() == StaggeredGridLayoutManager.class) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getRandomHeight());
            myViewHolder.mTv.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            myViewHolder.mTv.setLayoutParams(params);
        }

//        改变ItemView背景颜色
        if (addDataPosition == position) {
            myViewHolder.mItemView.setBackgroundColor(Color.RED);
        } else {
            myViewHolder.mItemView.setBackgroundColor(Color.parseColor("#A4D3EE"));
        }

        myViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                调用接口的回调方法
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    /**
     * 返回数据数量
     * @return
     */
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    private int getIcon (int position) {
        switch (position % 5) {
            case 0:
                return R.mipmap.a;
            case 1:
                return R.mipmap.b;
            case 2:
                return R.mipmap.c;
            case 3:
                return R.mipmap.d;
            case 4:
                return R.mipmap.e;
        }
        return 0;
    }

    /**
     * 返回不同的ItemView高度
     * @return
     */
    private int getRandomHeight () {
        return (int)(Math.random() * 1000);
    }

    /**
     * 添加一条数据
     * @param position
     */
    public void addData (int position) {
        addDataPosition = position;
        dataSource.add(position, "插入的数据");
        notifyItemInserted(position);

//        刷新ItemView
        notifyItemRangeChanged(position, dataSource.size() - position);
    }

    /**
     * 删除一条数据
     * @param position
     */
    public void removeData (int position) {
        addDataPosition = -1;
        dataSource.remove(position);
        notifyItemRemoved(position);

//        刷新ItemView
        notifyItemRangeChanged(position, dataSource.size() - position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        View mItemView;
        ImageView mIv;
        TextView mTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mIv = itemView.findViewById(R.id.iv);
            mTv = itemView.findViewById(R.id.tv);
            mItemView = itemView;
        }
    }

    /**
     * ItemView点击事件回调接口
     */
    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
```

关联实例：

```java
public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);

        // 线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
////        横向排列ItemView
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
////        数据反向展示
//        linearLayoutManager.setReverseLayout(true);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        itemView点击事件监听
        mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "第" + position + "数据被点击", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 添加数据
     * @param v
     */
    public void onAddDataClick (View v) {
        List<String> data = new ArrayList<>();

        for (int i = 0 ; i < 20 ; i ++) {
            String s = "第" + i + "条数据";
            data.add(s);
        }

        mAdapter.setDataSource(data);
    }

    /**
     * 切换布局
     * @param v
     */
    public void onChangeLayoutClick (View v) {
//        从线性布局 切换为 网格布局
        if (mRecyclerView.getLayoutManager().getClass() == LinearLayoutManager.class) {
//            网格布局
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }else if (mRecyclerView.getLayoutManager().getClass() == GridLayoutManager.class) {
//            瀑布流布局
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        } else {
            // 线性布局
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    /**
     * 插入一条数据
     * @param v
     */
    public void onInsertDataClick (View v) {
        mAdapter.addData(1);
    }

    /**
     * 删除一条数据
     * @param v
     */
    public void onRemoveDataClick (View v) {
        mAdapter.removeData(1);
    }
}
```





### WebView

xml设置一个WebView

```xml
<WebView
    android:id="@+id/webview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"></WebView>
```

引用之后，调用Api：

```java
package com.sunday.webviewdeom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mWebView = findViewById(R.id.webview);

//        mWebView.loadUrl("http://www.baidu.com/");
//        mWebView.loadUrl("file://" + Environment.getExternalStorageDirectory().getPath() + "/1/index.html");
//        mWebView.loadUrl("file:///android_asset/index.html");

//        mWebView.loadUrl("http://192.168.2.124:3000/");

//        mWebView.loadData("<h1>这是我们通过loadData添加进来的内容</h1>","text/html; charset=utf-8", null);
//        https://www.imooc.com/static/img/index/logo.png
//        mWebView.loadDataWithBaseURL("https://www.imooc.com/" , "<img src=\"static/img/index/logo.png\"/><a href=\"http://www.baidu.com\">toBaiDu.com</a>", "text/html", "utf-8","http://www.sogou.com/");

//        mWebView.loadUrl("http://www.baidu.com/");

        mWebView.loadUrl("http://192.168.2.124:3000/");
//        mWebView.loadUrl("http://192.168.2.128:3000/");

        mWebView.addJavascriptInterface(new DemoJsObject(), "android");

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e("WebViewActivity","webview-》onReceivedError : 加载了url：" + failingUrl + " - 错误描述：" + description+ " - 错误代码：" + errorCode);
                view.loadUrl("http://192.168.2.124:3000/");
            }


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("WebViewActivity","webview-》onReceivedError (android6.0以上调用) : 加载了url：" + request.getUrl().toString() + " - 错误描述：" + error.getDescription()+ " - 错误代码：" + error.getErrorCode());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("WebViewActivity","webview-》shouldOverrideUrlLoading : 加载了url：" + url);
                if ("http://www.baidu.com/".equals(url)) {
//                    view.loadUrl("http://www.sogou.com/");
                    Toast.makeText(WebViewActivity.this, "webview-》shouldOverrideUrlLoading : 加载了url：" + url, Toast.LENGTH_SHORT).show();
                    return true;
                }

                Uri uri = Uri.parse(url);
                if ("android".equals(uri.getScheme())) {
                    String functionName = uri.getAuthority();
                    if ("print".equals(functionName)) {
                        String msg = uri.getQueryParameter("msg");
                        print(msg);
                        return true;
                    }
                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("WebViewActivity","webview-》shouldOverrideUrlLoading(Android7.0以上调用) : 加载了url：" + request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse result = super.shouldInterceptRequest(view, url);
                Log.e("WebViewActivity","webview-》shouldInterceptRequest请求了url：" + url);
                Log.e("WebViewActivity", "result = " + result);
//                if ("http://www.baidu.com/".equals(url)) {
//                    return new WebResourceResponse("text/html", "utf-8", null);
//                }
                return result;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.e("WebViewActivity","webview-》shouldInterceptRequest请求了(android5.0之上调用)url：" + request.getUrl().toString());
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("WebViewActivity","webview-》onPageStarted 网页开始进行加载url：" + url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.e("WebViewActivity","webview-》onLoadResource 网页开始加载资源url：" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("WebViewActivity","webview-》onPageFinished 网页已经加载完成url：" + url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.e("webViewActivity", "newProgress:" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.e("webViewActivity", "title:" + title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                boolean res = super.onJsAlert(view, url, message, result);
                res = true;
                Log.e("webViewActivity", "onJsAlert - url : " + url + " - message : " + message + "  - res : " + res);
                Toast.makeText(WebViewActivity.this, message, Toast.LENGTH_SHORT).show();
                result.confirm();
                return res;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                boolean res = super.onJsConfirm(view, url, message, result);
                res = true;
                Log.e("webViewActivity", "onJsConfirm - url : " + url + " - message : " + message + "  - res : " + res);
                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
                builder.setMessage(message);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result.confirm();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result.cancel();
                    }
                });
                builder.create().show();
                return res;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                boolean res = super.onJsPrompt(view, url, message, defaultValue, result);
                res = true;
                Log.e("webViewActivity", "onJsConfirm - url : " + url + " - message : " + message + " - defaultValue : " + defaultValue + "  - res : " + res);
                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
                builder.setMessage(message);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result.confirm("这是点击了确定按钮之后的输入框内容");
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result.cancel();
                    }
                });
                builder.create().show();
                return res;
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDisplayZoomControls(true);
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.clearCache(true);
    }

//    public void onCanGoBack (View v) {
//        Toast.makeText(this, String.valueOf(mWebView.canGoBack()), Toast.LENGTH_SHORT).show();
//    }
//
//    public void onGoBack (View v) {
//        mWebView.goBack();
//    }
//
//    public void onCanGoForward (View v) {
//        Toast.makeText(this, String.valueOf(mWebView.canGoForward()), Toast.LENGTH_SHORT).show();
//    }
//
//    public void onGoForward (View v) {
//        mWebView.goForward();
//    }
//
//    public void onCanGoBackOrForward (View v) {
//        int steps = Integer.valueOf(((EditText)findViewById(R.id.steps)).getText().toString());
//        Toast.makeText(this, String.valueOf(mWebView.canGoBackOrForward(steps)), Toast.LENGTH_SHORT).show();
//    }
//
//    public void onGoBackOrForward (View v) {
//        int steps = Integer.valueOf(((EditText)findViewById(R.id.steps)).getText().toString());
//        mWebView.goBackOrForward(steps);
//    }
//
//    public void onClearHistory (View v) {
//        mWebView.clearHistory();
//    }

    public void onShowAlertFromloadUrl (View v) {
        mWebView.loadUrl("javascript:showAlert()");
    }

    public void onSumFromloadUrl (View v) {
        mWebView.loadUrl("javascript:alert(sum(2, 3))");
    }

    public void onSumFromEVJS (View v) {
        mWebView.evaluateJavascript("javascript:sum(2, 3)", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Toast.makeText(WebViewActivity.this, "evaluateJavascript - " + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void print (String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        String result = "这是android的返回值";
        mWebView.loadUrl("javascript:showAlert('" + result + "')");
    }

    @Override
    protected void onPause() {
        super.onPause();

        mWebView.onPause();
//        mWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mWebView.onResume();
//        mWebView.resumeTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mWebView.destroy();
    }
}
```



### CardView

使用的效果，就是用这个包含想要的控件：

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
<android.support.v7.widget.CardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginLeft="@dimen/margin_item_msg_l_r"
    android:layout_marginRight="@dimen/margin_item_msg_l_r"
    android:layout_marginTop="@dimen/margin_item_msg_t_b"
    android:layout_marginBottom="@dimen/margin_item_msg_t_b"
    app:cardUseCompatPadding="false"
    android:foreground="?attr/selectableItemBackground"
    app:cardPreventCornerOverlap="true"
    app:cardCornerRadius="8dp"
    app:cardElevation="4dp">


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <ImageView
            android:id="@+id/id_iv_img"
            android:layout_width="match_parent"
            android:layout_height="150dp"
            android:scaleType="centerCrop"
            tools:src="@drawable/img01"/>

        <TextView
            android:id="@+id/id_tv_title"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="8dp"
            android:textColor="#000000"
            android:textSize="16dp"
            android:textStyle="bold"
            tools:text="使用慕课网学习Android技术"
            />

        <TextView
            android:id="@+id/id_tv_content"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginLeft="8dp"
            android:layout_marginRight="8dp"
            android:layout_marginBottom="8dp"
            tools:text="使用慕课网学习Android技术使用慕课网学习Android技术使用慕课网学习Android技术使用慕课网学习Android技术"
            />


    </LinearLayout>


</android.support.v7.widget.CardView>
```





### ListView

申明一个ListView控件：

```xml
<?xml version="1.0" encoding="utf-8"?>
<ListView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/id_lv_msgList"
    android:divider="@null"
    android:background="#ffffff"
    android:paddingTop="8dp"
    tools:context="com.imooc.imooc_cardview.MainActivity">

</ListView>
```

设置ListView的Adapter：

```java
public class MsgAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Msg> mDatas;

    public MsgAdapter(Context context, List<Msg> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Msg getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_msg, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mIvImg = convertView.findViewById(R.id.id_iv_img);
            viewHolder.mTvTitle = convertView.findViewById(R.id.id_tv_title);
            viewHolder.mTvContent = convertView.findViewById(R.id.id_tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Msg msg = mDatas.get(position);
        viewHolder.mIvImg.setImageResource(msg.getImgResId());
        viewHolder.mTvTitle.setText(msg.getTitle());
        viewHolder.mTvContent.setText(msg.getContent());

        return convertView;
    }

    public static class ViewHolder {
        ImageView mIvImg;
        TextView mTvTitle;
        TextView mTvContent;

    }
}
```

申明Adapter里面的样式：

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <android.support.v7.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="@dimen/margin_item_msg_l_r"
        android:layout_marginRight="@dimen/margin_item_msg_l_r"
        android:layout_marginTop="@dimen/margin_item_msg_t_b"
        android:layout_marginBottom="@dimen/margin_item_msg_t_b"
        app:cardUseCompatPadding="false"
        android:foreground="?attr/selectableItemBackground"
        app:cardPreventCornerOverlap="true"
        app:cardCornerRadius="8dp"
        app:cardElevation="4dp">


        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <ImageView
                android:id="@+id/id_iv_img"
                android:layout_width="match_parent"
                android:layout_height="150dp"
                android:scaleType="centerCrop"
                tools:src="@drawable/img01"/>

            <TextView
                android:id="@+id/id_tv_title"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="8dp"
                android:textColor="#000000"
                android:textSize="16dp"
                android:textStyle="bold"
                tools:text="使用慕课网学习Android技术"
                />

            <TextView
                android:id="@+id/id_tv_content"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginLeft="8dp"
                android:layout_marginRight="8dp"
                android:layout_marginBottom="8dp"
                tools:text="使用慕课网学习Android技术使用慕课网学习Android技术使用慕课网学习Android技术使用慕课网学习Android技术"
                />


        </LinearLayout>


    </android.support.v7.widget.CardView>

</FrameLayout>
```

在Activity中将数据和适配器进行关联：

```java
private ListView mLvMsgList;
private List<Msg> mDatas = new ArrayList<>();
private MsgAdapter mAdapter;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mLvMsgList = findViewById(R.id.id_lv_msgList);

    mDatas.addAll(MsgLab.generateMockList());
    mDatas.addAll(MsgLab.generateMockList());

    mAdapter = new MsgAdapter(this, mDatas);
    mLvMsgList.setAdapter(mAdapter);


}
```

### ViewPager

一般来说，对于控件的基本使用，是在xml中申明，

容器里面放的是什么：

可以放任何的东西，例如图片，fragment等等。



容器里面的内容如何和下面的进行关联，如何实现相互之间的联动：

通过调用控件对应的位置函数来进行同步设置。



下面的样式如何定义：

1，可以通过布局里面添加View来实现

2，可以通过自定义PagerAdapter来实现

#### 第一种使用模式：图片浏览器

新建一个xml，包含：

```xml
<android.support.v4.view.ViewPager
    android:id="@+id/view_pager"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    />

<LinearLayout
    android:id="@+id/dot_layout"
    android:layout_width="120dp"
    android:layout_height="30dp"
    android:gravity="center"
    android:layout_alignParentBottom="true"
    android:layout_centerHorizontal="true"
    android:layout_marginBottom="30dp"
    android:orientation="horizontal">

</LinearLayout>
```

将这两个控件导入其中：

```java
mViewPager = (ViewPager) findViewById(R.id.view_pager);
mDotViewGroup = (ViewGroup) findViewById(R.id.dot_layout);
```

将需要显示的View封装入Adapter中。

公式是数量加位置，实现数据的整体的导入

```java
PagerAdapter mPagerAdapter = new PagerAdapter() {

    @Override
    public int getCount() {
        return mLayoutIDs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View child = mViews.get(position);
        container.addView(child);
        return child;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }
};
```

通过设置适配器，将数据与控件进行关联：

```java
// 设置adapter
mViewPager.setAdapter(mPagerAdapter);
mViewPager.setOffscreenPageLimit(4);
mViewPager.setCurrentItem(INIT_POSITION);
setDotViews(INIT_POSITION);
```

关联联动效果：

```java
mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setDotViews(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
});
```

动态添加控件的方式：

```java
ImageView dot = new ImageView(this);
dot.setImageResource(R.mipmap.ic_launcher);
dot.setMaxWidth(100);
dot.setMaxHeight(100);

LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80,80);
layoutParams.leftMargin = 20;
dot.setLayoutParams(layoutParams);
dot.setEnabled(false);

mDotViewGroup.addView(dot);
mDotViews.add(dot);
```



#### 第二种使用模式：Tab模式

xml最外层直接用Tab包围：

```xml
<TabHost
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/tab_host"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#f8f8f8"
    android:orientation="vertical">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v4.view.ViewPager
            android:id="@+id/view_pager"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_above="@+id/tab_divider"
            />

        <FrameLayout
            android:id="@android:id/tabcontent"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:visibility="gone"
            android:layout_above="@+id/tab_divider"
            >

        </FrameLayout>

        <View
            android:id="@+id/tab_divider"
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:layout_above="@android:id/tabs"
            android:background="#dfdfdf"
            />

        <TabWidget
            android:id="@android:id/tabs"
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:layout_alignParentBottom="true"
            android:showDividers="none"
            >

        </TabWidget>

    </RelativeLayout>


</TabHost>
```

设计每一个Tab的样式：

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/tab_bg"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#4dd0c8">

    <LinearLayout
        android:id="@+id/main_content"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:layout_centerInParent="true"
        android:orientation="vertical">

        <ImageView
            android:id="@+id/main_tab_icon"
            android:layout_width="30dp"
            android:layout_height="30dp"
            android:layout_marginTop="4dp"
            android:scaleType="centerInside"
            android:src="@drawable/main_tab_icon_home"/>

        <TextView
            android:id="@+id/main_tab_txt"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="5dp"
            android:textColor="@color/color_main_tab_txt"
            android:text="@string/home"/>
    </LinearLayout>

    <ImageView
        android:visibility="gone"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>

</RelativeLayout>
```

完成样式和Tab控件的关联：

```java
// 初始化总布局
mTabHost = (TabHost) findViewById(R.id.tab_host);
mTabHost.setup();

// 三个Tab 做处理

// 1. init data
int[] titleIDs = {
        R.string.home,
        R.string.message,
        R.string.me
};
int[] drawableIDs = {
        R.drawable.main_tab_icon_home,
        R.drawable.main_tab_icon_message,
        R.drawable.main_tab_icon_me
};
// data < -- > view

for (int index = 0; index < titleIDs.length; index++) {

    View view = getLayoutInflater().inflate(R.layout.main_tab_layout, null, false);

    ImageView icon = (ImageView) view.findViewById(R.id.main_tab_icon);
    TextView title = (TextView) view.findViewById(R.id.main_tab_txt);
    View tab = view.findViewById(R.id.tab_bg);

    icon.setImageResource(drawableIDs[index]);
    title.setText(getString(titleIDs[index]));

    tab.setBackgroundColor(getResources().getColor(R.color.white));

    mTabHost.addTab(
            mTabHost.newTabSpec(getString(titleIDs[index]))
            .setIndicator(view)
            .setContent(this)
    );




}
```

往ViewPager中添加显示效果：并联动Tab

```java
// 三个fragment组成的viewpager

final Fragment[] fragments = new Fragment[]{
        TestFragment.newInstance("home"),
        TestFragment.newInstance("message"),
        TestFragment.newInstance("me")
};
final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
viewPager.setOffscreenPageLimit(fragments.length);
viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
});

viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
          if(mTabHost != null){
              mTabHost.setCurrentTab(position);
          }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
});
```

Tab联动ViewPager

```java
mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
    @Override
    public void onTabChanged(String s) {
        if (mTabHost != null) {
            int position = mTabHost.getCurrentTab();
            viewPager.setCurrentItem(position);
        }

    }
});
```

其他：

```java
@Override
public View createTabContent(String s) {
    View view = new View(this);
    view.setMinimumHeight(0);
    view.setMinimumWidth(0);
    return view;
}
```



### 对话框

#### 1，提示对话框：

```java
public void showNormalDialog(){
    AlertDialog dialog = new AlertDialog.Builder(this).create();
    dialog.setTitle("提示");
    dialog.setMessage("您确定退出程序吗？");
    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    });
    dialog.show();
}
```

```java
 //AlertDialog的构造方法时protected
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("您确定退出程序吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
//                AlertDialog dialog = builder.create();
//                dialog.show();
                break;
```

#### 2，自定义对话框：

建立一个布局：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center_horizontal"
    android:background="@mipmap/dialog_bg">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="真的要退出吗？"
        android:textSize="34sp"
        android:textColor="#e61414"
        android:textStyle="bold"
        android:layout_marginTop="265dp"/>

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_margin="25dp">
        <Button
            android:id="@+id/yes_btn"
            android:layout_width="120dp"
            android:layout_height="50dp"
            android:background="@mipmap/yes_btn"/>
        <Button
            android:id="@+id/no_btn"
            android:layout_width="120dp"
            android:layout_height="50dp"
            android:background="@mipmap/no_btn"
            android:layout_marginLeft="20dp"/>
    </LinearLayout>
</LinearLayout>
```

将这个xml加入到继承的类中：

```java
public class MyDialog extends Dialog {

    public MyDialog(@NonNull final Context context, int themeResId) {
        super(context, themeResId);
        //为对话框设置布局
        setContentView(R.layout.dialog_layout);

        findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

        findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
```

在styles中创建一个xml:

```xml
<style name="mydialog" parent="android:style/Theme.Dialog">
    <item name="android:windowNoTitle">true</item>
    <item name="android:windowBackground">@android:color/transparent</item>
</style>
```

在Activity中显示：

```
    MyDialog md = new MyDialog(this,R.style.mydialog);
    md.show();
```

#### 3，popupWindows

新建一个布局：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android" 
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:background="#00ffff"
    android:padding="2dp"
    >

    <TextView
        android:id="@+id/choose"
        android:layout_width="60dp"
        android:layout_height="30dp"
        android:text="选择"
        android:textColor="#ffffff"
        android:gravity="center"
        android:background="#000000"/>

    <View
        android:layout_width="2dp"
        android:layout_height="30dp"
        android:background="#00ffff" />

    <TextView
        android:id="@+id/choose_all"
        android:layout_width="60dp"
        android:layout_height="30dp"
        android:text="全选"
        android:textColor="#ffffff"
        android:gravity="center"
        android:background="#000000"/>

    <View
        android:layout_width="2dp"
        android:layout_height="30dp"
        android:background="#00ffff" />

    <TextView
        android:id="@+id/copy"
        android:layout_width="60dp"
        android:layout_height="30dp"
        android:text="复制"
        android:textColor="#ffffff"
        android:gravity="center"
        android:background="#000000"/>


</LinearLayout>
```

将这个布局和xml进行关联：

```java
//设置PopupWindow
public void showPopupWindow(View view) {
    //准备弹窗所需要的视图对象
    View v = LayoutInflater.from(this).inflate(R.layout.popup_layout,null);
    //1.实例化对象
    //参数1：用在弹窗中的View
    //参数2、3：弹窗的宽高
    //参数4（focusable）：能否获取焦点
    final PopupWindow window = new PopupWindow(v,190,35,true);

    //2.设置（背景、动画）
    //设置背景
    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    //设置能响应外部的点击事件
    window.setOutsideTouchable(true);
    //设置能响应点击事件
    window.setTouchable(true);
    //①创建动画资源   ②创建一个style应用动画资源    ③对当前弹窗的动画风格设置为第二部的资源索引
    window.setAnimationStyle(R.style.translate_anim);

    //3.显示
    //参数1(anchor)：锚
    //参数2、3：相对于锚在x、y方向上的偏移量
    window.showAsDropDown(view,-190,0);

    //为弹窗中的文本添加点击事件
    v.findViewById(R.id.choose).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(MainActivity.this,"您点击了选择",Toast.LENGTH_SHORT).show();
            window.dismiss();   //控制弹窗消失
        }
    });

    v.findViewById(R.id.choose_all).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(MainActivity.this,"您点击了全选",Toast.LENGTH_SHORT).show();
            window.dismiss();
        }
    });

    v.findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(MainActivity.this,"您点击了复制",Toast.LENGTH_SHORT).show();
            window.dismiss();
        }
    });
}
```



### 菜单

菜单的使用，都是在menu的文件夹下面来进行xml文件的创造和引入：

#### 1，contextMenu：

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/delete"
        android:title="删除" />
    <item android:title="重命名" >
        <menu >
            <item
                android:id="@+id/opera1"
                android:title="操作1" />
            <item
                android:id="@+id/opera2"
                android:title="操作2" />
        </menu>
    </item>
</menu>
```

在java中如何体现：

```java
ActionMode.Callback cb = new ActionMode.Callback() {
    //创建，在启动上下文操作模式（startActionMode(Callback)）时调用
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        Log.e("TAG","创建");
        getMenuInflater().inflate(R.menu.context,menu);
        return true;
    }

    //在创建方法后进行调用
    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        Log.e("TAG","准备");
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        Log.e("TAG","点击");
        switch (menuItem.getItemId()){
            case R.id.delete:
                Toast.makeText(MainActivity.this,"删除",Toast.LENGTH_SHORT).show();
                break;
            case R.id.opera1:
                Toast.makeText(MainActivity.this,"操作1",Toast.LENGTH_SHORT).show();
                break;
            case R.id.opera2:
                Toast.makeText(MainActivity.this,"操作2",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    //上下文操作模式结束时被调用
    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        Log.e("TAG","结束");
    }
};

/*
@Override
public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    getMenuInflater().inflate(R.menu.context,menu);
}

@Override
public boolean onContextItemSelected(MenuItem item) {
    switch (item.getItemId()){
        case R.id.delete:
            Toast.makeText(this,"删除",Toast.LENGTH_SHORT).show();
            break;
        case R.id.opera1:
            Toast.makeText(this,"操作1",Toast.LENGTH_SHORT).show();
            break;
        case R.id.opera2:
            Toast.makeText(this,"操作2",Toast.LENGTH_SHORT).show();
            break;
    }
    return super.onContextItemSelected(item);
}*/
```

#### 2，option菜单：

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <!--showAsAction属性值：always ：直接在标题栏显示
    never：不显示
    withText：控制图标和文本一起显示
    ifRoom：有空间就显示-->
    <item android:title="保存"
        android:id="@+id/save"
        android:icon="@mipmap/ic_launcher"
        app:showAsAction="always"/>
    <item android:title="设置"
        android:id="@+id/setting"/>
    <item android:title="更多操作" >
        <menu >
            <item android:title="退出"
                android:id="@+id/exit"/>
            <item android:title="子菜单2" />
            <item android:title="子菜单3" />
        </menu>
    </item>

</menu>
```

在活动中体现：

```java
    //创建OptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单资源
        //通过XML资源来设计menu
        //getMenuInflater().inflate(R.menu.option,menu);

        //纯java代码设计menu
        /*
        设置
        更多
                添加
                删除
         */
//        Menu
        //参数1：组id     参数2：菜单项id    参数3：序号   参数4:设置
        menu.add(1, 1, 1,"设置");
        SubMenu sub = menu.addSubMenu(1,2,2,"更多");
//        SubMenu
        sub.add(2,3,1,"添加");
        sub.add(2,4,2,"删除");
        //一定要记得返回true，否则菜单不显示
        return true;
    }

    //OptionMenu菜单项的选中方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.save:
//                Toast.makeText(this,"保存",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.setting:
//                Toast.makeText(this,"设置",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.exit:
//                finish();
//                break;
            case 1:
                Toast.makeText(this,"设置",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this,"更多",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this,"添加",Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this,"删除",Toast.LENGTH_SHORT).show();
                break;
            default:
                super.onOptionsItemSelected(item);

        }
        return true;
    }
}
```

#### 3,popmenu:

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/copy"
        android:title="复制" />
    <item
        android:id="@+id/paste"
        android:title="粘贴" />
</menu>
```

```java
    //popup_btn:演示PopupMenu
    final Button popupBtn = findViewById(R.id.popup_btn);
    popupBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //①实例化PopupMenu对象 (参数2：被锚定的view)
            final PopupMenu menu = new PopupMenu(MainActivity.this,popupBtn);
            //②加载菜单资源：利用MenuInflater将Menu资源加载到PopupMenu.getMenu()所返回的Menu对象中
            //将R.menu.xx对于的菜单资源加载到弹出式菜单中
            menu.getMenuInflater().inflate(R.menu.popup,menu.getMenu());
            //③为PopupMenu设置点击监听器
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.copy:
                            Toast.makeText(MainActivity.this,"复制",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.paste:
                            Toast.makeText(MainActivity.this,"粘贴",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return false;
                }
            });
            //④千万不要忘记这一步,显示PopupMenu
            menu.show();
        }
    });
}
```



### SurfaceView







### 动画







### 自定义View

在res 的Value下面的attr文件中写明：

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="RoundProgressBar">
        <attr name="color" format="color"></attr>
        <attr name="line_width" format="dimension"></attr>
        <attr name="radius" format="dimension"></attr>
        <attr name="android:progress" ></attr>
        <attr name="android:textSize" ></attr>
    </declare-styleable>
    <declare-styleable name="TestView">
        <attr name="test_boolean" format="boolean"></attr>
        <attr name="test_string" format="string"></attr>
        <attr name="test_integer" format="integer"></attr>
        <attr name="test_enum" format="enum">
            <enum name="top" value="1"></enum>
            <enum name="bottom" value="2"></enum>
        </attr>
        <attr name="test_dimension" format="dimension"></attr>
    </declare-styleable>
</resources>
```

导入进行绘制：

```java
package com.imooc.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


public class RoundProgressBar extends View
{

    private int mRadius;
    private int mColor;
    private int mLineWidth;
    private int mTextSize;
    private int mProgress;

    private Paint mPaint;


    public RoundProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        mRadius = (int) ta.getDimension(R.styleable.RoundProgressBar_radius, dp2px(30));
        mColor = ta.getColor(R.styleable.RoundProgressBar_color, 0xffff0000);
        mLineWidth = (int) ta.getDimension(R.styleable.RoundProgressBar_line_width, dp2px(3));
        mTextSize = (int) ta.getDimension(R.styleable.RoundProgressBar_android_textSize, dp2px(36));
        mProgress = ta.getInt(R.styleable.RoundProgressBar_android_progress, 30);

        ta.recycle();

        initPaint();
    }

    private float dp2px(int dpVal)
    {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    private void initPaint()
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
    }

    public void setProgress(int progress)
    {
        mProgress = progress;
        invalidate();
    }

    public int getProgress()
    {
        return mProgress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = 0;
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else
        {
            int needWidth = measureWidth() + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST)
            {
                width = Math.min(needWidth, widthSize);
            } else
            {
                width = needWidth;
            }
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            int needHeight = measureHeight() + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST)
            {
                height = Math.min(needHeight, heightSize);
            } else //MeasureSpec.UNSPECIFIED
            {
                height = needHeight;
            }
        }
        setMeasuredDimension(width, height);

    }

    private int measureHeight()
    {
        return mRadius * 2;
    }

    private int measureWidth()
    {
        return mRadius * 2;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth * 1.0f / 4);

        int width = getWidth();
        int height = getHeight();

        canvas.drawCircle(width / 2, height / 2,
                width / 2 - getPaddingLeft() - mPaint.getStrokeWidth() / 2, mPaint);

        mPaint.setStrokeWidth(mLineWidth);
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        float angle = mProgress * 1.0f / 100 * 360;
        canvas.drawArc(new RectF(0, 0, width - getPaddingLeft() * 2, height - getPaddingLeft() * 2), 0, angle, false, mPaint);
        canvas.restore();

        String text = mProgress + "%";
//        text = "张鸿洋";
        mPaint.setStrokeWidth(0);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
        int y = getHeight() / 2;
        Rect bound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bound);
        int textHeight = bound.height();
        canvas.drawText(text, 0, text.length(), getWidth() / 2, y + textHeight / 2, mPaint);

        mPaint.setStrokeWidth(0);
//        canvas.drawLine(0, height / 2, width, height / 2, mPaint);

    }


    private static final String INSTANCE = "instance";
    private static final String KEY_PROGRESS = "key_progress";

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PROGRESS, mProgress);
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            Parcelable parcelable = bundle.getParcelable(INSTANCE);
            super.onRestoreInstanceState(parcelable);
            mProgress = bundle.getInt(KEY_PROGRESS);
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
```

```java
package com.imooc.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class TestView extends View
{
    private String mText = "Imooc";

    private Paint mPaint;


    public TestView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        initPaint();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TestView);

        boolean booleanTest = ta.getBoolean(R.styleable.TestView_test_boolean, false);
        int integerTest = ta.getInteger(R.styleable.TestView_test_integer, -1);
        float dimensionTest = ta.getDimension(R.styleable.TestView_test_dimension, 0);
        int enumTest = ta.getInt(R.styleable.TestView_test_enum, 1);
//        mText = ta.getString(R.styleable.TestView_test_string);


        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++)
        {
            int index = ta.getIndex(i);
            switch (index)
            {
                case R.styleable.TestView_test_string:
                    mText = ta.getString(R.styleable.TestView_test_string);
                    break;
            }
        }

        Log.e("TAG", booleanTest + " , "
                + integerTest + " , "
                + dimensionTest + " , " + enumTest + " ," + mText);

        ta.recycle();
    }

    private void initPaint()
    {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        mPaint.setColor(0xFFFF0000);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = 0;
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else
        {
            int needWidth = measureWidth() + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST)
            {
                width = Math.min(needWidth, widthSize);
            } else
            {
                width = needWidth;
            }
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            int needHeight = measureHeight() + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST)
            {
                height = Math.min(needHeight, heightSize);
            } else //MeasureSpec.UNSPECIFIED
            {
                height = needHeight;
            }
        }
        setMeasuredDimension(width, height);

    }

    private int measureHeight()
    {
        return 0;
    }

    private int measureWidth()
    {
        return 0;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
//        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mPaint.getStrokeWidth() / 2, mPaint);
//        mPaint.setStrokeWidth(1);
//        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);
//        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mPaint);

        mPaint.setTextSize(72);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        canvas.drawText(mText, 0, mText.length(), 0, getHeight(), mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mText = "8888";
        invalidate();
        return true;
    }


    private static final String INSTANCE = "instance";
    private static final String KEY_TEXT = "key_text";

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TEXT, mText);
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            Parcelable parcelable = bundle.getParcelable(INSTANCE);
            super.onRestoreInstanceState(parcelable);
            mText = bundle.getString(KEY_TEXT);
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
```

在Layout的xml中进行使用：

```xml
<com.imooc.custom_view.RoundProgressBar
    android:id="@+id/id_pb"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_centerInParent="true"
    android:padding="10dp"
    android:progress="0"
    android:textSize="18sp"
    hyman:color="#ea22e4"
    hyman:radius="36dp"
/>
```



## 存储系列

### 数据库存储

数据库存储，先建一个类用来创建数据库

```java
public class ChapterDbHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "db_chapter.db";
    private static final int VERSION = 1;

    private static ChapterDbHelper sInstance;

    public ChapterDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static synchronized ChapterDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ChapterDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + Chapter.TABLE_NAME + " ("
                        + Chapter.COL_ID + " INTEGER PRIMARY KEY, "
                        + Chapter.COL_NAME + " VARCHAR"
                        + ")"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + ChapterItem.TABLE_NAME + " ("
                        + ChapterItem.COL_ID + " INTEGER PRIMARY KEY, "
                        + ChapterItem.COL_NAME + " VARCHAR, "
                        + ChapterItem.COL_PID + " INTEGER"
                        + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
```

再用这个类来完成数据的插入删除

```java
public class ChapterDao {


    public List<Chapter> loadFromDb(Context context) {
        ChapterDbHelper dbHelper = ChapterDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<Chapter> chapterList = new ArrayList<>();
        Chapter chapter = null;
        Cursor cursor = db.rawQuery("select * from " + Chapter.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            chapter = new Chapter();
            int id = cursor.getInt(cursor.getColumnIndex(Chapter.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(Chapter.COL_NAME));
            chapter.setId(id);
            chapter.setName(name);
            chapterList.add(chapter);
        }
        cursor.close();

        ChapterItem chapterItem = null;
        for (Chapter tmpChapter : chapterList) {
            int pid = tmpChapter.getId();
            cursor = db.rawQuery("select * from " + ChapterItem.TABLE_NAME + " where " + ChapterItem.COL_PID + " = ? ", new String[]{pid + ""});
            while (cursor.moveToNext()) {
                chapterItem = new ChapterItem();
                int id = cursor.getInt(cursor.getColumnIndex(ChapterItem.COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(ChapterItem.COL_NAME));
                chapterItem.setId(id);
                chapterItem.setName(name);
                chapterItem.setPid(pid);
                tmpChapter.addChild(chapterItem);
            }
            cursor.close();
        }

        return chapterList;
    }

    public void insertToDb(Context context, List<Chapter> chapters) {

        if (chapters == null || chapters.isEmpty()) {
            return;
        }
        ChapterDbHelper dbHelper = ChapterDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();

        ContentValues cv = null;
        for (Chapter chapter : chapters) {
            cv = new ContentValues();
            cv.put(Chapter.COL_ID, chapter.getId());
            cv.put(Chapter.COL_NAME, chapter.getName());
            db.insertWithOnConflict(Chapter.TABLE_NAME, null, cv, CONFLICT_REPLACE);

            List<ChapterItem> chapterItems = chapter.getChildren();
            for (ChapterItem chapterItem : chapterItems) {
                cv = new ContentValues();
                cv.put(ChapterItem.COL_ID, chapterItem.getId());
                cv.put(ChapterItem.COL_NAME, chapterItem.getName());
                cv.put(ChapterItem.COL_PID, chapter.getId());
                db.insertWithOnConflict(ChapterItem.TABLE_NAME, null, cv, CONFLICT_REPLACE);
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }
}
```

同一个，数据管理。

一般来说，数据操作，都是使用数据类来完成的：

```java
public class Student{
        //私有属性
        private int id;
        private String name;
        private int age;
        private String gender;
        //无参构造
        public Student(){

        }

        public Student(String name, int age, String gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

    //有参构造
        public Student(int id, String name, int age, String gender) {
            super();
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
        }
        //创建的setter和getter方法
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public String getGender() {
            return gender;
        }
        public void setGender(String gender) {
            this.gender = gender;
        }


}
```



#### 第二种：直接执行命令的方式：

```java
public class StudentDao {
    private SQLiteDatabase db;

    public StudentDao(Context context){
        String path = Environment.getExternalStorageDirectory() + "/stu.db";
        SQLiteOpenHelper helper = new SQLiteOpenHelper(context,path,null,2) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            }
        };
        db = helper.getReadableDatabase();
    }

    public void addStudent(Student stu){
        String sql = "insert into info_tb (name,age,gender) values(?,?,?)";
        db.execSQL(sql,new Object[]{stu.getName(),stu.getAge()+"",stu.getGender()});
    }

    public Cursor getStudent(String... strs){
        //1.查询所有(没有参数)
        String sql = "select * from info_tb ";
        //2.含条件查询（姓名/年龄/编号）（参数形式：第一个参数指明条件，第二个参数指明条件值）
        if(strs.length != 0){
            sql += " where " + strs[0] + "='" + strs[1] + "'";
        }
        Cursor c = db.rawQuery(sql,null);
        return c;
    }

    public ArrayList<Student> getStudentInList(String... strs){
        ArrayList<Student> list = new ArrayList<>();
        Cursor c = getStudent(strs);
        while (c.moveToNext()){
            int id = c.getInt(0);
            String name = c.getString(1);
            int age = c.getInt(2);
            String gender = c.getString(3);
            Student s = new Student(id,name,age,gender);
            list.add(s);
        }
        return list;
    }

    public void deleteStudent(String... strs){
        String sql  = "delete from info_tb where " + strs[0] + "='" + strs[1] + "'";
        db.execSQL(sql);
    }

    public void updateStudent(Student stu){
        String sql = "update info_tb set name=?,age=?,gender=? where _id=?";
        db.execSQL(sql,new Object[]{stu.getName(),stu.getAge(),stu.getGender(),stu.getId()});
    }

}
```

#### 第三种：增删查改

```java
switch (v.getId()){
    case R.id.insert_btn:
        //在SqliteDatabase类下，提供四个方法
        //insert（添加）、delete（删除）、update（修改）、query（查询）
        //都不需要写sql语句
        //参数1：你所要操作的数据库表的名称
        //参数2：可以为空的列.  如果第三个参数是null或者说里面没有数据
        //那么我们的sql语句就会变为insert into info_tb () values ()  ，在语法上就是错误的
        //此时通过参数3指定一个可以为空的列，语句就变成了insert into info_tb (可空列) values （null）
        ContentValues values = new ContentValues();
        //insert into 表明(列1，列2) values（值1，值2）
        values.put("name",nameStr);
        values.put("age",ageStr);
        values.put("gender",genderStr);
        long id = db.insert("info_tb",null,values);
        Toast.makeText(this,"添加成功，新学员学号是：" + id,Toast.LENGTH_SHORT).show();
        break;
    case R.id.select_btn:
        //select 列名 from 表名 where 列1 = 值1 and 列2 = 值2
        //参数2：你所要查询的列。{”name","age","gender"},查询所有传入null/{“*”}
        //参数3：条件（针对列）
        //参数5:分组
        //参数6：当 group by对数据进行分组后，可以通过having来去除不符合条件的组
        //参数7:排序
        Cursor c = db.query("info_tb",null,null,null,null,null,null);

        //SimpleCursorAdapter
        //SimpleAdapter a = new SimpleAdapter()
        //参数3：数据源
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.item,c,
                new String[]{"_id","name","age","gender"},
                new int[]{R.id.id_item,R.id.name_item,R.id.age_item,R.id.gender_item});
        stuList.setAdapter(adapter);
        break;
    case R.id.delete_btn:
        int count = db.delete("info_tb","_id=?",new String[]{idStr});
        if(count > 0) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        }
        break;
    case R.id.update_btn:
        ContentValues values2 = new ContentValues();
        //update info_tb set 列1=xx , 列2=xxx where 列名 = 值
        values2.put("name",nameStr);
        values2.put("age",ageStr);
        values2.put("gender",genderStr);
        int count2 = db.update("info_tb",values2,"_id=?",new String[]{idStr});
        if(count2 > 0) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        }
        break;
}
```

#### 第四种：增删查改：

```java
public void operate(View v){

        String nameStr = nameEdt.getText().toString();
        String ageStr = ageEdt.getText().toString();
        String idStr = idEdt.getText().toString();
        switch (v.getId()){
            case R.id.insert_btn:


//                db.rawQuery();        查询    select * from 表名
//                db.execSQL();         添加、删除、修改、创建表

                //String sql = "insert into info_tb (name,age,gender) values ('"+nameStr+"',"+ageStr+",'"+genderStr+"')";
                String sql = "insert into info_tb (name,age,gender) values (?,?,?)";
                db.execSQL(sql,new String[]{nameStr,ageStr,genderStr});
                Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.select_btn:
                //SQLiteOpenHelper
                //select * from 表名 where _id = ?
                String sql2 = "select * from info_tb";
                if(!idStr.equals("")){
                    sql2 += " where _id=" + idStr;
                }
                //查询结果
                Cursor c = db.rawQuery(sql2,null);

                //SimpleCursorAdapter
                //SimpleAdapter a = new SimpleAdapter()
                //参数3：数据源
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        this, R.layout.item,c,
                        new String[]{"_id","name","age","gender"},
                        new int[]{R.id.id_item,R.id.name_item,R.id.age_item,R.id.gender_item});
                stuList.setAdapter(adapter);
                break;
            case R.id.delete_btn:
                //' '   "23"   23
                String sql3 = "delete from info_tb where _id=?";
                db.execSQL(sql3,new String[]{idStr});
                Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.update_btn:
                String sql4 = "update info_tb set name=? , age=? , gender=?  where _id=?";
                db.execSQL(sql4,new String[]{nameStr,ageStr,genderStr,idStr});
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                break;
        }
```

#### 数据库创建并获取

```java
//添加操作
//数据库名称
//如果只有一个数据库名称，那么这个数据库的位置会是在私有目录中
//如果带SD卡路径，那么数据库位置则在指定的路径下
String path = Environment.getExternalStorageDirectory() + "/stu.db";
SQLiteOpenHelper helper = new SQLiteOpenHelper(this,path,null,2) {
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建
        Toast.makeText(MainActivity2.this,"数据库创建",Toast.LENGTH_SHORT).show();
        //如果数据库不存在，则会调用onCreate方法，那么我们可以将表的创建工作放在这里面完成
                /*
                String sql = "create table test_tb (_id integer primary key autoincrement," +
                        "name varhcar(20)," +
                        "age integer)";
                sqLiteDatabase.execSQL(sql);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //升级
        Toast.makeText(MainActivity2.this,"数据库升级",Toast.LENGTH_SHORT).show();
    }
};

//用于获取数据库库对象
//1.数据库存在，则直接打开数据库
//2.数据库不存在，则调用创建数据库的方法，再打开数据库
//3.数据库存在，但版本号升高了，则调用数据库升级方法
db = helper.getReadableDatabase();
```

### SharePreference

```java
//2.1存储信息到SharePreference
//①获取SharePreference对象(参数1：文件名  参数2：模式)
SharedPreferences share = getSharedPreferences("myshare",MODE_PRIVATE);
//②获取Editor对象
SharedPreferences.Editor edt = share.edit();
//③存储信息
edt.putString("account",account);
edt.putString("pwd",pwd);
//④指定提交操作
edt.commit();
```



### 内部存储

```java
//  data/data/包名/files
//   getCacheDir()    data/data/包名/cache
File f = new File(getFilesDir(),"getFilesDirs.txt");
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(edt.getText().toString().getBytes());
            fos.close();
```



### 外部存储

动态申请权限：

```java
    int permisson = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    if(permisson!=PackageManager.PERMISSION_GRANTED){
        //动态去申请权限
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }

}

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if(requestCode == 1){
        //xxxxxxxxxxxxx
    }
}
```

读写操作；

```java
String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/imooc.txt";
Log.e("TAG",path);
//if(Environment.getExternalStorageState().equals("mounted"))
switch (v.getId()){
    case R.id.save_btn:
        File f = new File(path);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(path,true);
            String str = infoEdt.getText().toString();
            fos.write(str.getBytes());
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        break;
    case R.id.read_btn:
        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] b = new byte[1024];
            int len = fis.read(b);
            String str2 = new String(b,0,len);
            txt.setText(str2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        break;
}
```



## 框架系列

### Glide

Glide使用的各种样式：

```java
public class MainActivity extends AppCompatActivity {

    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIv = findViewById(R.id.iv);
    }

    public void onLoadImageClick (View v) {
//        使用 HttpURLConnection 来加载网络中的图片
//        loadUrlImage("https://www.imooc.com/static/img/index/logo.png");
//        但是当网络图片过大时，会导致我们生成的bitmap所占用的内存过多，从而导致 OOM
//        loadUrlImage("http://res.lgdsunday.club/big_img.jpg");
//        同样，当我们在加载本地资源文件中的图片时，如果图片过大，一样会导致 OOM
//        loadResImage(R.mipmap.big_img);


//        使用 Glide 来去图片的时候，Glide则会帮助我们处理上面的问题
//        glideLoadImage("http://res.lgdsunday.club/big_img.jpg");
        glideLoadImage("https://img2.mukewang.com/5b037fb30001534202000199-140-140.jpg");
//        glideAppLoadImage("https://img2.mukewang.com/5b037fb30001534202000199-140-140.jpg");
    }

    /**
     * 加载网络图片
     * @param img 网络图片地址
     */
    private void loadUrlImage (final String img) {
//        开启子线程，用于进行网络请求
        new Thread(){
            @Override
            public void run() {
//                创建消息对象，用于通知handler
                Message message = new Message();
                try {
//                    根据传入的路径生成对应的URL
                    URL url = new URL(img);
//                    创建连接
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                    设置请求方式为GET
                    httpURLConnection.setRequestMethod("GET");
//                    获取返回码
                    int code = httpURLConnection.getResponseCode();
//                    当返回码为200时，表示请求成功
                    if (code == 200) {
//                        获取数据流
                        InputStream inputStream = httpURLConnection.getInputStream();
//                        利用位图工程根据数据流生成对应的位图对象
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        利用message对象将生成的bitmap携带到handler
                        message.obj = bitmap;
//                        成功的状态码
                        message.what = 200;
                    } else {
//                        失败的状态码
                        message.what = code;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    当出现异常的时候，状态码设置为 -1
                    message.what = -1;
                } finally {
//                    通知handler
                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                当获取到成功的状态码时执行
                case 200:
//                    获取携带的bitmap
                    Bitmap bitmap = (Bitmap) msg.obj;
//                    imageView展示
                    mIv.setImageBitmap(bitmap);
                    break;
//                 当请求失败获取出现异常的时候回调
                default:
//                    展示加载失败的图片
                    mIv.setImageResource(R.mipmap.loader_error);
//                    打印失败的状态码
                    Toast.makeText(MainActivity.this, "code: " + msg.what , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 加载本地图片
     * @param resId 本地图片ID
     */
    private void loadResImage (int resId) {
        mIv.setImageResource(resId);
    }

    /**
     * 使用Glide加载网络图片
     * @param img 网络图片地址
     */
    private void glideLoadImage (String img) {
//      通过 RequestOptions 对象来设置Glide的配置
        RequestOptions options = new RequestOptions()
//                设置图片变换为圆角
                .circleCrop()
//                设置站位图
                .placeholder(R.mipmap.loading)
//                设置加载失败的错误图片
                .error(R.mipmap.loader_error);

//      Glide.with 会创建一个图片的实例，接收 Context、Activity、Fragment
        Glide.with(this)
//                指定需要加载的图片资源，接收 Drawable对象、网络图片地址、本地图片文件、资源文件、二进制流、Uri对象等等
                .load(img)
//                指定配置
                .apply(options)
//                用于展示图片的ImageView
                .into(mIv);
    }

    /**
     * 使用GlideApp进行图片加载
     * @param img
     */
    private void glideAppLoadImage (String img) {
        /**
         * 不想每次都通过 .apply(options) 的方式来进行配置的时候，可以使用GlideApp的方式来进行全局统一的配置
         * 需要注意以下规则：
         * 1、引入 repositories {mavenCentral()}  和 dependencies {annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'}
         * 2、集成 AppGlideModule 的类并且通过 @GlideModule 进行了注解
         * 3、有一个使用了 @GlideExtension 注解的类 MyGlideExtension，并实现private的构造函数
         * 4、在 MyGlideExtension 可以通过被 @GlideOption 注解了的静态方法来添加可以被GlideApp直接调用的方法，该方法默认接受第一个参数为：RequestOptions
         */
       GlideApp.with(this)
               .load(img)
//               调用在MyGlideExtension中实现的，被@GlideOption注解的方法，不需要传递 RequestOptions 对象
               .injectOptions()
               .into(mIv);
    }
}
```

其中，如果使用GlideApp，需要设置一下：

```java
/**
 * 帮助我们生成 GlideApp 对象
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
}
```

```java
@GlideExtension
public class MyGlideExtension{

    /**
     * 实现private的构造函数
     */
    private MyGlideExtension() {
    }

    @GlideOption
    public static void injectOptions (RequestOptions options) {
        options
//                设置图片变换为圆角
                .circleCrop()
//                设置站位图
                .placeholder(R.mipmap.loading)
//                设置加载失败的错误图片
                .error(R.mipmap.loader_error);

    }
}
```

引入库和相关的解释器：

```xml
implementation 'com.github.bumptech.glide:glide:4.8.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'
```

### OkHttp

申明客户端：

```java
private final OkHttpClient mClient = new OkHttpClient();
```

使用客户端来发送请求：

```java
Request.Builder builder = new Request.Builder();
builder.url(POST_URL);
builder.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, "Hello world github/linguist#1 **cool**, and #1!"));
Request request = builder.build();
Call call = mClient.newCall(request);
call.enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            final String content = response.body().string();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mContentTextView.setText(content);
                }
            });
        }
    }
});
```

```java
private void response() {
    Request.Builder builder = new Request.Builder();
    builder.url("https://raw.githubusercontent.com/square/okhttp/master/README.md");
    Request request = builder.build();
    Call call = mClient.newCall(request);
    call.enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG, "onFailure() called with: call = [" + call + "], e = [" + e + "]");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
            int code = response.code();
            Headers headers = response.headers();
            String content = response.body().string();
            final StringBuilder buf = new StringBuilder();
            buf.append("code: " + code);
            buf.append("\nHeaders: \n" + headers);
            buf.append("\nbody: \n" + content);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mContentTextView.setText(buf.toString());
                }
            });
        }
    });
}

private void get() {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(new Runnable() {
        @Override
        public void run() {
            Request.Builder builder = new Request.Builder();
            builder.url("https://raw.githubusercontent.com/square/okhttp/master/README.md");
            Request request = builder.build();
            Log.d(TAG, "run: " + request);
            Call call = mClient.newCall(request);
            try {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    final String string = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContentTextView.setText(string);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
    executor.shutdown();
}
```



### EventBus

申明Bus类：

```java
public class PostingEvent {
    public final String threadInfo;

    public PostingEvent(String threadInfo) {
        this.threadInfo = threadInfo;
    }
}
```

针对申明的类，进行接收处理：

```java
@Subscribe(threadMode = ThreadMode.POSTING)
public void onPositingEvent(final PostingEvent event) {
    final String threadInfo = Thread.currentThread().toString();
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            setPublisherThreadInfo(event.threadInfo);
            setSubscriberThreadInfo(threadInfo);
        }
    });
}
```

发送事件：

```java
    EventBus.getDefault().post(new AsyncEvent(Thread.currentThread().toString()));
```

事件的注册和解注册：

```java
@Override
protected void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
}

@Override
protected void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
}
```

事件延迟接受：

```java
@Subscribe(sticky = true)
public void onStickyMessageEvent(StickyMessageEvent event) {
    setTitle(event.message);
}
```



### GreenDao

申明模型：

```java
@Entity
public class GoodsModel implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private Integer goodsId;
    private String name;
    private String icon;
    private String info;
    private String type;

    protected GoodsModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            goodsId = null;
        } else {
            goodsId = in.readInt();
        }
        name = in.readString();
        icon = in.readString();
        info = in.readString();
        type = in.readString();
    }
```

在Application中初始化：

```java
    public static DaoSession mSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initDb();
    }

    public void initDb () {
//        获取SQLiteOpenHelper对象devOpenHelper
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "imooc.db");
//        获取SQLiteDatabase
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
//        加密数据库
//        Database db = devOpenHelper.getEncryptedWritableDb("123");
//        创建DaoMaster实例
//        DaoMaster保存数据库对象（SQLiteDatabase）并管理特定模式的Dao类（而不是对象）。
//        它具有静态方法来创建表或将它们删除。
//        其内部类OpenHelper和DevOpenHelper是在SQLite数据库中创建模式的SQLiteOpenHelper实现。
        DaoMaster daoMaster = new DaoMaster(db);
//        管理特定模式的所有可用Dao对象
        mSession = daoMaster.newSession();
    }
```

获取 初始化后的数据库进行增删查改：

```java
public class GreenDaoManager {

    private Context mContext;
    private GoodsModelDao mGoodsModelDao;

    public GreenDaoManager (Context context) {
        this.mContext = context;
//        获取DAO实例
        mGoodsModelDao = MyApplication.mSession.getGoodsModelDao();
    }

    /**
     * 添加所有的数据到数据库
     */
    public void insertGoods () {
        String json = DataUtils.getJson("goods.json", mContext);
//        如果不想因为重复添加数据而导致崩溃,可以使用insertOrReplaceInTx API
//        mGoodsModelDao.insertInTx(DataUtils.getGoodsModels(json));
        mGoodsModelDao.insertOrReplaceInTx(DataUtils.getGoodsModels(json));
    }

    /**
     * 查询所有的数据
     * @return
     */
    public List<GoodsModel> queryGoods () {
        QueryBuilder<GoodsModel> result = mGoodsModelDao.queryBuilder();
        result = result.orderAsc(GoodsModelDao.Properties.GoodsId);
        return result.list();
    }

    /**
     * 查询水果的数据
     * @return
     */
    public List<GoodsModel> queryFruits () {
        QueryBuilder<GoodsModel> result = mGoodsModelDao.queryBuilder();
        /**
         * 借助Property属性类提供的筛选方法
         */
        result = result.where(GoodsModelDao.Properties.Type.eq("0")).orderAsc(GoodsModelDao.Properties.GoodsId);
        return result.list();
    }

    /**
     * 查询零食的数据
     * @return
     */
    public List<GoodsModel> querySnacks () {
        QueryBuilder<GoodsModel> result = mGoodsModelDao.queryBuilder();
        /**
         * 借助Property属性类提供的筛选方法
         */
        result = result.where(GoodsModelDao.Properties.Type.eq("1")).orderAsc(GoodsModelDao.Properties.GoodsId);
        return result.list();
    }

    /**
     * 修改指定商品的商品信息
     * @param model
     */
    public void updateGoodsInfo (GoodsModel model) {
        mGoodsModelDao.update(model);
        mGoodsModelDao.updateInTx();
    }

    /**
     * 删除指定商品的商品信息
     * @param model
     */
    public void deleteGoodsInfo (GoodsModel model) {
        mGoodsModelDao.deleteByKey(model.getId());
    }
}
```

真正实例化：

```java
mDbManager = new GreenDaoManager(this);
mGoodsModel = getIntent().getParcelableExtra("goodsModel");
```

### ButterKnife

官网+文档

适配器里面：

```java
static class ViewHolder
{
    @BindView(R.id.id_title_tv)
    TextView mTextView;

    public ViewHolder(View view)
    {
        ButterKnife.bind(this, view);
    }

}
```

正常的使用：

```java
@BindView(R.id.id_listview)
ListView mListView;

private List<String> mData = new ArrayList<>(Arrays.asList("Simple Use", "RecyclerView Use"));


@Override
protected void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category);

    ButterKnife.bind(this);

    mListView.setAdapter(new CategoryAdapter(this, mData));

}

@OnItemClick(R.id.id_listview)
public void itemClicked(int position)
{
```

多个按键使用：

```java
@BindView(R.id.id_tv)
 TextView mTv;
@BindView(R.id.id_btn1)
Button mBtn1;
@BindView(R.id.id_btn2)
Button mBtn2;

@BindString(R.string.hello_world)
String str;

@Override
protected void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    mTv.setText(str);
    mBtn1.setText("Hello");
    mBtn2.setText("Imooc");

}

@OnClick({R.id.id_btn1, R.id.id_btn2})
public void btnClick(View view)
{
    switch (view.getId())
    {
        case R.id.id_btn1:
            Toast.makeText(this, "Btn1 Clicked!", Toast.LENGTH_SHORT).show();
            break;
        case R.id.id_btn2:
            Toast.makeText(this, "Btn2 Clicked!", Toast.LENGTH_SHORT).show();
            break;
    }

}
```



### 百度地图

通过Demo+文档（逐行去看）

### 极光推送

直接使用demo

### RxJava2

![](https://s1.ax1x.com/2020/10/23/BAAwpF.png)

观察者观察可被观察者，只要可被观察者有数据更新，那么，观察者就可以取用这个数据，那么，数据是如何生产，如何传递的。

语法结构为：可被观察者被xxx观察者订阅。

```
observable.subscribe(observer);
```

```java
    Observable{
        //The work you need to do
    }
.subscribeOn(Schedulers.io) //thread you need the work to perform on
.observeOn(AndroidSchedulers.mainThread()) //thread you need to handle the result on
            .subscribeWith(Observer{
        //handle the result here
    })
```

```java
animals.add("Tiger");
animals.add("Lion");
animals.add("Elephant");
Observable.just(animals)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new Observer<ArrayList>() {
        @Override
        public void onSubscribe(Disposable d) {

        }
        @Override
        public void onNext(ArrayList arrayList) {
            //handling the result
            adapter.updateList(animals);
            adapter.notifyDataSetChanged();
        }
        @Override
        public void onError(Throwable e) {
            //error handling made simple
        }
        @Override
        public void onComplete() {
            //cleaning up tasks
        }
    });
```

![](https://cms-assets.tutsplus.com/uploads/users/769/posts/28345/image/observable.jpg)

Unlike traditional programming that considers objects, the fundamental unit of reactive reasoning is the stream of events. Events can come in the form of objects, data feeds, mouse movements, or even exceptions. First thing to keep in mind is that in reactive everything is a stream,Observable is the fundamental unit that wraps a stream. Streams can contain zero or more events, and may or may not complete, and may or may not issue an error. Once a stream completes or issues an error, it is essentially done, although there are tools for retrying or substituting different streams when an exception occurs.

我的理解:RxJava提供一个可被观察的事件，事件可以发生开始和结束，事件本身，是带有数据和事件双重属性的。

In terms of strengths, RxJava is a robust toolset. There are tons of operators to allow you to manipulate your data stream in a multitude of ways, including built-in tools for specifying which threads operators execute on. This is great for complex operations on data, such as might be found in the business logic of applications.

While LiveData has a smaller set of operators, it offers the benefit of built-in lifecycle awareness. This means that it can safely and easily update UI elements even through the complex lifecycles of Activities and Fragments, which is not built in to RxJava (though there are tools like [AutoDispose](https://github.com/uber/AutoDispose) that can make this somewhat more automatic).



The advantages of using LiveData

Using LiveData provides the following advantages:

- **Ensures your UI matches your data state**

  LiveData follows the observer pattern. LiveData notifies [`Observer`](https://developer.android.com/reference/androidx/lifecycle/Observer) objects when the lifecycle state changes. You can consolidate your code to update the UI in these `Observer` objects. Instead of updating the UI every time the app data changes, your observer can update the UI every time there's a change.

- **No memory leaks**

  Observers are bound to [`Lifecycle`](https://developer.android.com/reference/androidx/lifecycle/Lifecycle) objects and clean up after themselves when their associated lifecycle is destroyed.

- **No crashes due to stopped activities**

  If the observer's lifecycle is inactive, such as in the case of an activity in the back stack, then it doesn’t receive any LiveData events.

- **No more manual lifecycle handling**

  UI components just observe relevant data and don’t stop or resume observation. LiveData automatically manages all of this since it’s aware of the relevant lifecycle status changes while observing.

- **Always up to date data**

  If a lifecycle becomes inactive, it receives the latest data upon becoming active again. For example, an activity that was in the background receives the latest data right after it returns to the foreground.

- **Proper configuration changes**

  If an activity or fragment is recreated due to a configuration change, like device rotation, it immediately receives the latest available data.

- **Sharing resources**

  You can extend a [`LiveData`](https://developer.android.com/reference/androidx/lifecycle/LiveData) object using the singleton pattern to wrap system services so that they can be shared in your app. The `LiveData` object connects to the system service once, and then any observer that needs the resource can just watch the `LiveData` object. For more information, see [Extend LiveData](https://developer.android.com/topic/libraries/architecture/livedata#extend_livedata).

  

- Currently, we're using `RxJava` in data source and repository layers, and it's transformed into `LiveData` (using `LiveDataReactiveStreams`) in ViewModels (before exposing data to activities/fragments) - quite happy with this approach.



1. LiveData is **synchronous**, So you can't execute a chunk of code (network call, database manipulation etc.) asynchronously using just LiveData as you do with RxJava.
2. What best you can do to exploit the most of this duo is to use RxJava for your business logic (network call, data manipulation etc, anything that happens in and beyond **Repository**) and use LiveData for your presentation layer. By this, you get transformation and stream capabilities for your business logic and lifecycle-aware operation for your UI.

1. LiveData doesn't have a history (just the current state). Hence, you shouldn't use LiveData for a chat application.

### LiveData

[`LiveData`](https://developer.android.com/reference/androidx/lifecycle/LiveData) is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an active lifecycle state.

```
public class MutableLiveData`
\```extends LiveData<T>
```

his method must be called from the main thread. If you need set a value from a background thread, you can use `postValue(Object)`

.LiveData在实体类里可以通知指定某个字段的数据更新.

3.MutableLiveData则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段



申明一个MutableLiveData变量，并setData.然后返回这个变量，申明要装载的数据类型，并装载。

```java
public class BlogRepository {
    private ArrayList<Blog> movies = new ArrayList<>();
    private MutableLiveData<List<Blog>> mutableLiveData = new MutableLiveData<>();
    private Application application;

    public BlogRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Blog>> getMutableLiveData() {

        RestApiService apiService = RetrofitInstance.getApiService();
        Call<BlogWrapper> call = apiService.getPopularBlog();
        call.enqueue(new Callback<BlogWrapper>() {
            @Override
            public void onResponse(Call<BlogWrapper> call, Response<BlogWrapper> response) {
                BlogWrapper mBlogWrapper = response.body();
                if (mBlogWrapper != null && mBlogWrapper.getBlog() != null) {
                    movies = (ArrayList<Blog>) mBlogWrapper.getBlog();
                    mutableLiveData.setValue(movies);
                }
            }

            @Override
            public void onFailure(Call<BlogWrapper> call, Throwable t) { }
        });
        return mutableLiveData;
    }
}
```

建立一个仓库，把这个变量转为liveData:

```java
public class MainViewModel extends AndroidViewModel {
    private BlogRepository movieRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new BlogRepository(application);
    }

    public LiveData<List<Blog>> getAllBlog() {
        return movieRepository.getMutableLiveData();
    }


}
```

将其中包含的数据取出来：

```java
 mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        getPopularBlog();
        // lambda expression
        swipeRefresh.setOnRefreshListener(() -> {
            getPopularBlog();
        });
    }

    private void initializationViews() {
        swipeRefresh = findViewById(R.id.swiperefresh);
        mRecyclerView = findViewById(R.id.blogRecyclerView);
    }

    public void getPopularBlog() {
        swipeRefresh.setRefreshing(true);
        mainViewModel.getAllBlog().observe(this, new Observer<List<Blog>>() {
            @Override
            public void onChanged(@Nullable List<Blog> blogList) {
                swipeRefresh.setRefreshing(false);
                prepareRecyclerView(blogList);
            }
        });
```















### [android-dagger2-example](https://github.com/MindorksOpenSource/android-dagger2-example)

注入，目的是将类与类之间的依赖给隔离开来，相比较，视图的隔离，组件之间的隔离，插件化的隔离，这个部分，是类与类之间的隔离。

**Dependency provider:** Classes annotated with `@Module` are responsible for providing objects which can be injected. Such classes define methods annotated with `@Provides`. The returned objects from these methods are available for dependency injection.///就是说，`@Provides`用来定义， `@Module` 用来生成。

**Dependency consumer:** The `@Inject` annotation is used to define a dependency.// `@Inject` 用来接收

**Connecting consumer and producer:** A `@Component` annotated interface defines the connection between the provider of objects (modules) and the objects which express a dependency. The class for this connection is generated by the Dagger.//链接两者之间的东西由Dagger生成。并且用`@Component`来修饰

首先是依赖的导入：

```java
dependencies {
    ...
    compile "com.google.dagger:dagger:2.8"
    annotationProcessor "com.google.dagger:dagger-compiler:2.8"
    provided 'javax.annotation:jsr250-api:1.0'
    compile 'javax.inject:javax.inject:1'
}
```

构造一个映射数据库格式的类：

```java
public class User {

    private Long id;
    private String name;
    private String address;
    private String createdAt;
    private String updatedAt;

    public User() {
    }
```

https://blog.mindorks.com/introduction-to-dagger-2-using-dependency-injection-in-android-part-2-b55857911bcd

为了区分同一种返回类型，定义一系列注解器：

```java
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityContext {
}
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationContext {
}
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseInfo {
}
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
```

利用自定义的注解描述一个构造函数，且是单例的：

```java
@Singleton
public class DbHelper extends SQLiteOpenHelper {

    //USER TABLE
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_COLUMN_USER_ID = "id";
    public static final String USER_COLUMN_USER_NAME = "usr_name";
    public static final String USER_COLUMN_USER_ADDRESS = "usr_add";
    public static final String USER_COLUMN_USER_CREATED_AT = "created_at";
    public static final String USER_COLUMN_USER_UPDATED_AT = "updated_at";

    @Inject
    public DbHelper(@ApplicationContext Context context,
                    @DatabaseInfo String dbName,
                    @DatabaseInfo Integer version) {
        super(context, dbName, null, version);
    }
```

利用自定义的注解器来构造model，其中以下的构造函数在后面那个里面进行了初始化：

```java
@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application app) {
        mApplication = app;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }
```

chushihua ：

```java
applicationComponent = DaggerApplicationComponent
                            .builder()
                            .applicationModule(new ApplicationModule(this))
                            .build();
```



利用model来建立Compoment，并指明注入的类型，此处inject和实例的时候那个进行对应：

```java
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(DemoApplication demoApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    SharedPrefsHelper getPreferenceHelper();

    DbHelper getDbHelper();

}
```



#### 最小模型：

需要通过注入来产生实例的类：

```java
public class HelloWorld {
    @Inject
    public void HelloWorld()
    {}
    public void ShowHelloWorld()
    {
        System.out.println("hello 大大yang");
    }
}
```

建立model和providers来产生实例，依据类型来关联，如若类型一致，如何区分：

```java
@Module
public class HelloModule {

    @Provides
    public HelloWorld getHello()
    {
        return (new HelloWorld());
    }
}
```

指明产生的实例用于哪个类中：

```java
@Component(modules = HelloModule.class)
public interface CommandRouterFactory {

    void inject(MainActivity mainActivity);
}
```

指明需要消费的类中，哪个变量来进行消费：

```java
public class MainActivity extends AppCompatActivity {
@Inject
    HelloWorld dd;
    @Inject
HelloWorld ddd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommandRouterFactory build = DaggerCommandRouterFactory.builder().helloModule(new HelloModule()).build();
        build.inject(this);
    }

    public void MyClick(View view) {

        dd.ShowHelloWorld();
        ddd.ShowHelloWorld();
    }
}
```



### [Fast-Android-Networking](https://github.com/amitshekhariitbhu/Fast-Android-Networking)

直接看官网就行。

### CompositeDisposable

// 创建网络请求的Observable
Observable<DataClass> remoteDataObservable = RetrofitFactory.createRequest()
        .getRemoteData()
        .subscribeOn(Schedulers.io()) // 订阅触发后，在子线程中进行请求
        .map(dataResponse -> {
            DataClass remoteData = dataResponse;
            // 假装对返回数据做了一些处理
            // ...
            return remoteData;
        })
        .observeOn(AndroidSchedulers.mainThread()); // 在UI线程中暗中观察并及时消费
        
// 然后在需要的地方订阅刚才创建的可观察对象，返回一个Disposable对象，后面我们会用到
Disposable disposable = remoteDataObservable.subscribe(remoteData -> {
    // 在回调中，UI线程食用远程数据
    // ...
});

需要注意的是，只有在调用Observable的**subscribe**方法时，网络请求才会触发，订阅即触发。
就这样，整个网络请求的过程就完毕了，完结撒花……个铲铲。

**正文开始了：**
如果在请求过程中，UI层destroy了怎么办，不及时取消订阅，可能会造成内存泄漏。因此，**CompositeDisposable**就上场了，它可以对我们订阅的请求进行统一管理。
大致三步走：
1、在UI层创建的时候（比如onCreate之类的），实例化CompositeDisposable；
2、把subscribe订阅返回的Disposable对象加入管理器；
3、UI销毁时清空订阅的对象。

rxjava虽然好用，但是总所周知，容易遭层内存泄漏。也就说在订阅了事件后没有及时取阅，导致在activity或者fragment销毁后仍然占用着内存，无法释放。而disposable便是这个订阅事件，可以用来取消订阅。

```java
private final CompositeDisposable disposables = new CompositeDisposable();


// adding an Observable to the disposable
disposables.add(sampleObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String value) {
                    }
                }));

    static Observable<String> sampleObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                // Do some long running operation
                SystemClock.sleep(2000);
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }                


// Using clear will clear all, but can accept new disposable
disposables.clear(); 
// Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
disposables.dispose(); 
```

### retrofit2

此部件，已经包含了Okhttp.很多时候，都是基于将网络上的数据转化为本地对象，然后再操作这个对象，

![](https://abhiandroid-8fb4.kxcdn.com/programming/wp-content/uploads/2018/01/Retrofit-In-Android.png)

![](https://abhiandroid.com/programming/wp-content/uploads/2018/01/Retrofit-Post-Example-In-Android-Studio.gif)

Here is the main difference between our three mainly used techniques for implementing API’s is our android app. You can see the difference in performance that for one discussion means for one network request and response they will take how much time.

**1. AsyncTask:** 
one(1) discussion: 941 ms
Seven(7) discussions: 4539 ms
Twenty Five(25) discussions: 13957 ms

**2. Volley:** 
one(1) discussion: 560 ms
Seven(7) discussions: 2202 ms
Twenty Five(25) discussions: 4275 ms

**3. Retrofit:** 
one(1) discussion: 312 ms
Seven(7) discussions: 889 ms
Twenty Five(25) discussions: 1059 ms

1,添加依赖和权限

2，构造接收的数据的格式：

```java
Sample POJO class:

public class SamplePojo {

private int userId;
private String name;

public int getUserId() {
return userId;
}

public String getName() {
return name;
}
}
```

3，建立一个请求的基础连接地址，

```java
 public class Api {

    public static ApiInterface getClient() {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://healthyblackmen.org") //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        ApiInterface api = adapter.create(ApiInterface.class);
        return api;
    }
}
```

4，将最终的地址，请求参数，返回的数据格式是什么，最终生成一个对象返回出来。

```java
public interface ApiInterface {


// For POST request

    @FormUrlEncoded    // annotation that used with POST type request
    @POST("/demo/login.php") // specify the sub url for our base url
    public void login(
            @Field("user_email") String user_email,
            @Field("user_pass") String user_pass, Callback<SignUpResponse> callback); 

//user_email and user_pass are the post parameters and SignUpResponse is a POJO class which recieves the response of this API


// for GET request

    @GET("/demo/countrylist.php") // specify the sub url for our base url
    public void getVideoList(Callback<List<CountryResponse>> callback);

// CountryResponse is a POJO class which receives the response of this API

}
```

```java
 public interface ApiInterface {


// For POST request

   @FormUrlEncoded // annotation used in POST type requests
    @POST("/retrofit/register.php")     // API's endpoints
    Call<SignUpResponse> registration(@Field("name") String name,
                                      @Field("email") String email);//user_email and user_pass are the post parameters and SignUpResponse is a POJO class which receives the response of this API


// for GET request

    @GET("/retrofit/getuser.php")
        // API's endpoints
    Call<List<UserListResponse>> getUsersList();

// UserListResponse is POJO class to get the data from API, we use List<UserListResponse> in callback because the data in our API is starting from JSONArray

}
```



其中response定义为：

```java
package com.abhiandroid.retrofitexample;

import java.util.HashMap;
import java.util.Map;

public class SignUpResponse {

    private String success;
    private String message;
    private Integer userid;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
```

通过如下方式进行关联：

```java
 Api.getClient().registration(name.getText().toString().trim(),
                email.getText().toString().trim(),
                password.getText().toString().trim(),
                "email", new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        // in this method we will get the response from API
                        progressDialog.dismiss(); //dismiss progress dialog
                        signUpResponsesData = signUpResponse;
                        // display the message getting from web api
                        Toast.makeText(MainActivity.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // if error occurs in network transaction then we can get the error in this method.
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss(); //dismiss progress dialog

                    }
                });
```

并可以在接口中，对整个的请求参数进行合成。

Multipart requests are used when `@Multipart` is present on the method. Parts are declared using the `@Part` annotation.

```java
@Multipart
@PUT("user/photo")
Call<User> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);
```



### coroutines









## 事件分发

为什么要了解事件分发，

事件分发，是用来处理同一个区域，点击事件如何在多个控件之间进行传递。

如何设计一个案例来体现事件的分发：

首先，Activity，布局，布局中的View。

## 架构与操作：

### MVC

The MVC pattern splits code into one of three MVC components. When you create a new class or file, you must know which component it belongs to:

MVC is already implemented in Android as:

1. View = layout, resources and built-in classes like `Button` derived from `android.view.View`.
2. Controller = Activity
3. Model = the classes that implement the application logic

In Android **View like is a layout**, **Controller Like Ia Activity**, **Model like is Class whare your implement logic** 

ne problem arises when connecting the Controller to the View since the Controller needs to tell the View to update. In the passive Model MVC architecture, the Controller needs to hold a reference to the View. The easiest way of doing this, while focusing on testing, is to have a BaseView interface, that the Activity/Fragment/View would extend. So, the Controller would have a reference to the BaseView.

1.View接受用户的请求，然后将请求传递给Controller。
2.Controller进行业务逻辑处理后，通知Model去更新。
3.Model数据更新后，通知View去更新界面显示。

Android中最典型的MVC莫过于`ListView`了，要显示的数据为Model，而要显示的`ListView`就是View了，`Adapter`则充当着Controller的角色。当Model发生改变的时候可以通过调用`Adapter`的`notifyDataSetChanged`方法来通知组件数据发生变化，这时Adapter会调用`getView`方法重新显示内容。具体代码这里就不分析了。

#### 2.1 Model层

创建一个数据模型，能够保存一个数字，并有一个更新的方法，数据更新完后会通知UI去更改显示的内容。



```java
public class NumModel {
    private int num = 0;

    public void add(ControllerActivity activity) {
        num = ++num;//更新数据
        activity.updateUI(num + "");//更新UI
    }

}
```

#### 2.2 View层

View层在Android中对应的就是布局的XML文件。
 activity_controller.xml ：



```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:orientation="vertical">

    <TextView
        android:id="@+id/tv_show"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="0"/>

    <Button
        android:id="@+id/btn_add"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="点击+1"/>
</LinearLayout>
```

#### 2.3 Controller层

Android中一般由`Activity`来充当Controller。Controller一方面接收来自View的事件，一方面通知Model处理数据。



```java
public class ControllerActivity extends Activity {

    private TextView mTextView;
    private Button mButton;
    private NumModel mNumModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        mTextView = findViewById(R.id.tv_show);
        mButton = findViewById(R.id.btn_add);
        mNumModel = new NumModel();
        
        mButton.setOnClickListener(new View.OnClickListener() {//接收来自View的事件
            @Override
            public void onClick(View v) {
                mNumModel.add(ControllerActivity.this);//通知Model处理数据
            }
        });
    }

    public void updateUI(String text) {//更新UI
        mTextView.setText(text);
    }
}
```





General MVC WorkFlow :

[![BV63KP.png](https://s1.ax1x.com/2020/10/24/BV63KP.png)](https://imgchr.com/i/BV63KP)



![](https://www.techyourchance.com/wp-content/uploads/2015/06/MVC_MVP.png)

### MVVM

![mvvm arch](https://s1.ax1x.com/2020/10/16/0H2R0J.png)

Model的建立：

```kotlin
data class Resource<out T>(val status:Status,val data:T?,val message:String?) {
    companion object
    {
        fun <T>success(data:T?):Resource<T>
        {
            return Resource(Status.SUCCESS,data,null)
        }

        fun <T>error(message: String?,data:T?):Resource<T>{
            return Resource(Status.ERROR,data,message)
        }

        fun <T>loading(data:T?):Resource<T>{
            return Resource(Status.LOADING,data,null)
        }
    }
}
```

```kotlin
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
```



针对数据的解构，建立一个新的类：

```kotlin
import com.google.gson.annotations.SerializedName

/*https://github.com/MindorksOpenSource/android-mvvm-architecture*/
data class User(
    @SerializedName("id")
    val id:Int = 0,
    @SerializedName("name")
    val name:String = "",
    @SerializedName("avatar")
    val avatar:String = "",
    @SerializedName("email")
    val email:String = ""
)
```

以这个类作为类型，构造一个接口，接口的作用就是用来统一类型的：

```kotlin
import io.reactivex.Single

interface ApiService {
    fun getUser():Single<List<User>>
}
```

实现接口，这个类可以实例出该接口申明的任何的类的形参中去：

```kotlin
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single

class ApiServiceImpl:ApiService {
    override fun getUser(): Single<List<User>> {
        return Rx2AndroidNetworking.get("https://5e510330f2c0d300147c034c.mockapi.io/users")
            .build()
            .getObjectListSingle(User::class.java)
    }
}
```

将接口的函数返回来：

```java
class ApiHelper(private val apiService:ApiService) {
    fun getUser() = apiService.getUser()
}
```

通过助手建立起自己的数据仓库：

```java
class MainRepository(private val apiHelper: ApiHelper) {
    fun getUsers():Single<List<User>>
    {
        return apiHelper.getUser()
    }
}
```



通过ViewModel让这个数据具有了被订阅的能力：

```kotlin
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaya.mvvm_architecture_android_beginners.data.model.User
import com.kaya.mvvm_architecture_android_beginners.data.repository.MainRepository
import com.kaya.mvvm_architecture_android_beginners.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {
    private val users = MutableLiveData<Resource<List<User>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        users.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {userList-> users.postValue(Resource.success(userList))},
                    {throwable -> users.postValue(Resource.error("something went wrong",null))  }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getUsers():LiveData<Resource<List<User>>>{
        return users
    }

}
```

通过工厂，来将具有订阅能力的类提供实例。

```kotlin
class ViewModelFactory():ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(MainRepository(ApiHelper(ApiServiceImpl()))) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
```

```kotlin
private fun setupViewModel() {
    mainViewModel = ViewModelProviders.of(
        this,
        ViewModelFactory()
    ).get(MainViewModel::class.java)
}
```

建立这种订阅关系：

```
private fun setupObserver() {
    mainViewModel.getUsers().observe(this, Observer {
        when(it.status){

            Status.SUCCESS -> {
                progressBar.visibility = View.GONE
                it.data?.let { users1 -> renderList(users1) }
                recyclerView.visibility = View.VISIBLE
            }
            Status.LOADING -> {
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
            Status.ERROR -> {
                //Handle Error
                progressBar.visibility = View.GONE
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }
    })
}

private fun renderList(users: List<User>) {
    adapter.addData(users)
    adapter.notifyDataSetChanged()
}
```



### MVP

基本上，都是利用接口来贯穿的。https://blog.csdn.net/c529283955/article/details/104501002

首先，给MVP：model，View，Presenter建立三个接口，里面包含了三个部分需要做的事情：

Presenter

```java
package cn.daccc.mvpdemo.presenter;

/**
 * 中间人接口，只要能帮我们买到可乐，能通知我们就行
 */
public interface MiddlePeople{
    //买可乐
    void buyCola();

    //购买成功后调用该方法
    void buySucceed();
}
```

model:

```java
package cn.daccc.mvpdemo.model;

/**
 * 工具人接口，只要能生产可乐就行
 */
public interface ToolPeople{
    //生产可乐
    void produceCola();

}
```

View:

```java
package cn.daccc.mvpdemo.view;

/**
 * 实际上不一定是你想喝，你同学也想喝呢？就假装他也是“你”
 */
public interface Me {
    //喝可乐
    void drinkCola();
}
```

实现Model的接口，申明一个Presenter的实例在其中：

```java
package cn.daccc.mvpdemo.model;
import android.util.Log;
import cn.daccc.mvpdemo.presenter.MiddlePeople;
/**
 * 工具人憨憨，他要符合工具人的特性才能帮我们做事，因此要实现Tool接口
 */
public class HanHan implements ToolPeople {
    //保存中间人的联系方式
    private MiddlePeople middlePeople;

    public HanHan(MiddlePeople middlePeople){
        this.middlePeople = middlePeople;
    }
    @Override
    public void produceCola() {
        Log.d("ToolPeople","生产可乐好累啊！");
        //HanHan生产好了，告诉中间人TA购买成功了
        middlePeople.buySucceed();
    }
}
```

Presenter传递View和Model，实例化Model：

```java 
package cn.daccc.mvpdemo.presenter;

import android.util.Log;

import cn.daccc.mvpdemo.model.ToolPeople;
import cn.daccc.mvpdemo.view.Me;

/**
 * 中间人，指定王大妈是中间人，因此她要做所有中间人该做的事情
 */
public class WangDaMa implements MiddlePeople {
    private Me me;
    private ToolPeople toolPeople;

    //保存“我”的联系方式，并指派HanHan为工具人，保存他的联系方式
    public WangDaMa(Me me){
        this.me=me;
        toolPeople = new HanHan(this);
    }

    @Override
    public void buyCola() {
        Log.d("MiddlePeople","我来帮你买可乐");
        //让工具人去生产可乐
        toolPeople.produceCola();
    }

    @Override
    public void buySucceed() {
        //买成了，通知“我”可以开始嚯阔乐啦
        me.drinkCola();
    }
}
```

在View中申明Presenter

```java
public class MainActivity extends AppCompatActivity implements Me{
	private MiddlePeople middlePeople = new WangDaMa(this);

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button btn_drink = findViewById(R.id.btn_drink);
    btn_drink.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //点击按钮，就让中间人帮我们买可乐
            middlePeople.buyCola();
        }
    });
}

/**
 * 实现Me接口中的drinkCola方法，让喝可乐这件事情有一个更加具体的描述
 */
@Override
public void drinkCola() {
    Toast.makeText(this, "这阔乐真他*的好嚯！", Toast.LENGTH_SHORT).show();
}
```

### MVI

Model-View-Intent

![](https://user-gold-cdn.xitu.io/2018/1/2/160b5955d9c82c9e?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

While this approach is not bad, there are still a couple of issues that MVI attempts to solve:

- *Multiple inputs*: In MVP and MVVM, the Presenter and the ViewModel often end up with a large number of inputs and outputs to manage. This is problematic in big apps with many background tasks.
- *Multiple states*: In MVP and MVVM, the business logic and the Views may have different states at any point. Developers often synchronize the state with Observable and Observer callbacks, but this may lead to conflicting behavior.

To solve this issues, make your Models represent a *state* rather than data.

Using the previous example, this is how you could create a Model that represents a state:

定一个类，利用这个类来管理触发发送和接收的部分，中间层直接调用数据层的封装，在Activity中进行实例化，抽动数据回流。



## Java工具类和基础语法，对象体验



## KotLin基础语法体验





