### 1. 导入依赖

这里以 20180813 的 json 版本为例

```xml
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20180813</version>
</dependency>
```

### 2. 构建 json

##### 2.1 直接构建

```java
JSONObject obj = new JSONObject();
obj.put(key, value);
```

直接构建即直接实例化一个 JSONObject 对象，而后调用其 put() 方法，将数据写入。put() 方法的第一个参数为 key  值，必须为 String 类型，第二个参数为 value，可以为 boolean、double、int、long、Object、Map 以及  Collection 等。当然，double 以及 int 等类型只是在 Java 中，写入到 json 中时，统一都会以 Number  类型存储。

##### 2.2 使用HashMap构建

使用 HashMap 构建 json，实际上即先创建好一个 HashMap 对象并且将数据打包进去，而后在创建 JSONObject 时将其作为一个参数传进去。

```java
Map<String, Object> data = new HashMap<String, Object>();
data.put("name", "John");
data.put("sex", "male");
data.put("age", 22);
data.put("is_student", true);
data.put("hobbies", new String[] {"hiking", "swimming"});

JSONObject obj = new JSONObject(data);
// 或是下面这种写法，将 java 对象转换为 json 对象
JSONObject obj = JSONObject.fromObject(data);
```

##### 2.3 使用JavaBean构建

相较于前两种方法，实际开发中应用 JavaBean 构建 json 的情况更为常见，因为这样代码的重用率更高。

1. 创建 javaBean

```java
@Data
public class PersonInfo {
    private String name;
    private String sex;
    private int age;
    private boolean isStudent;
    private String[] hobbies;
```

1. 构建 json

```java
PersonInfo info = new PersonInfo();
info.setName("John");
info.setSex("male");
info.setAge(22);
info.setStudent(true);
info.setHobbies(new String[] {"hiking", "swimming"});

JSONObject obj = new JSONObject(info);
// 或是下面这种写法，将 java 对象转换为 json 对象
JSONObject obj = JSONObject.fromObject(data);
```

**注意：**JavaBean一定要有 getter 方法，否则会无法访问存储的数据。

### 3. 解析 json

解析 json 主要是基本类型如 Number、boolean 等，与数组 Array。

基本类型的解析直接调用 JSONObject 对象的 getXxx(key) 方法，如果获取字符串则 getString(key)，布尔值则 getBoolean(key)，以此类推。

数组的解析稍微麻烦一点，需要通过 JSONObject 对象的 getJSONArray(key) 方法获取到一个 JSONArray 对象，再调用 JSONArray 对象的 get(i) 方法获取数组元素，i 为索引值

##### 3.1 只包含一个对象的 json 字符串解析

```java
//将得到json数据转换为一个json对象
JSONObject jsonObject = new JSONObject(data);
//获取"persons"的json对象
jsonObject = jsonObject.getJSONObject("persons");
//通过相应的get方法,获取相应的属性
int id = jsonObject.getInt("id");
String name = jsonObject.getString("name");
int age = jsonObject.getInt("age");
//将获取到的数据放到一个Person对象中
Person person = new Person(id, name, age);
```

##### 3.2 含有多个对象的 json 字符串

```java
List<Person> list = new ArrayList<>();
//将得到json数据转换为一个json对象
JSONObject jsonObject = new JSONObject(data);
//获取"persons"的json对象,并将其转换为一个json数组
JSONArray array = jsonObject.getJSONArray("persons");
//通过循环获取数据,并放入list集合中
for (int i = 0;i<array.length();i++){
    int id = array.getJSONObject(i).getInt("id");
    String name = array.getJSONObject(i).getString("name");
    int age = array.getJSONObject(i).getInt("age");
    Person person = new Person(id, name, age);
    list.add(person);
}
return list;
```