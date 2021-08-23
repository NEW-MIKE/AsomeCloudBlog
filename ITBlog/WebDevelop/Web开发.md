

### 一，从服务端发送文本：

https://www.liaoxuefeng.com/wiki/1252599548343744/1304265903570978

https://blog.csdn.net/zhouziyu2011/article/details/52900024
1、数据格式提要
在服务器端Ajax是一门与语言无关的技术。在业务逻辑层使用何种服务器端语言都可以。
从服务器端接收数据的时候，那些数据必须以浏览器能够理解的格式来发送，服务器端的编程语言只能以如下3种格式返回数据——XML、JSON、HTML。

2、解析HTML
HTML由一些普通的文本组成，如果服务器通过XMLHttpRequest对象来发送HTML，文本将存储在ResponseText属性中。
不必从ResponseText属性中读取数据，因为它本身就是希望的数据格式，可以直接将它插入页面中。
插入HTML代码最简单的方法是更新这个元素的innerHTML属性。
优点：
（1）从服务器端发送的HTML代码在浏览器端不需要用JavaScript进行解析；
（2）HTML的可读性好。
（3）HTML代码与innerHTML属性搭配，效率高。
缺点：
（1）若需要通过Ajax更新一篇文档的多个部分，HTML不合适。

（2）innerHTML并非DOM标准。
3、解析XML

优点：
（1）XML是一种通用的数据格式。
（2）不必把数据强加到已定义好的格式中，而是要为数据自定义合适的标记。
（3）利用DOM可以完全掌控文档。
缺点：
（1）如果文档来自于服务器，就必须保证文档含有正确的首部信息，若文档类型不正确，那么requestXML属性将是空的。
（2）当浏览器接收到长的XML文件后，DOM解析可能会很复杂。

4、解析JSON
JSON是一种简单的数据格式，比XML更加轻巧。JSON 是JavaScript原生格式，这意味着在javascript中处理JSON数据不需要任何特殊的API或者工具包。
JSON的规则很简单：对象是一个无序的 “名称/值”对集合，一个对象以“{”（左括号）开始，以“}”（右括号）结束，每一个名称后面跟一个“:”（冒号），名称/值之间用逗号分隔开。
JSON用冒号而不是等号来赋值。每一条赋值语句用逗号分开。整个对象用大括号封装起来，可用大括号分级嵌套数据。
对象描述中存储的数据可以是字符串，数字或布尔值，对象描述也可存储函数，即对象的方法。
JSON只是一种文本字符串，它被存储在responseText属性中。
为了读取存储在responseText属性中的JSON数据，需要用到javascript的eval()语句，eval函数会把一个字符串当做它的一个参数，然后这个字符串会被当做javascript代码来执行，因为JSON的字符串就是javascript代码构成的，所以它本身是可以执行的。
JSON提供了json.js包后，使用parseJSON()方法可以将JSON对象解析为js对象。
优点：
（1）作为一种数据传输格式，JSON与XML很相似，但是它更加灵巧。
（2）JSON不需要从服务器端发送含有特定内容类型的首部信息。
缺点：
（1）语法过于严谨。
（2）代码不易读。
（3）eval函数存在风险。
注意：若从服务器端返回json字符串，则属性名必须使用双引号（需要转义），不能使用单引号。 
Java对象列表转换为JSON对象数组，并转为字符串：

JSONArray jsonArray = JSONArray.fromObject(list);
String jsonArrayStr = jsonArray.toString();
把Java对象转换成JSON对象，并转化为字符串：

JSONObject jsonObject = JSONObject.fromObject(obj);
String jsonObjectStr = jsonObject.toString();


5、三种格式的对比总结

（1）如果应用程序不需要与其它应用程序共享数据的时候，使用HTML片段来返回数据最简单。
（2）如果数据需要重用，JSON数据是个不错的选择，其在性能和文件大小方面有一定优势。
（3）当远程应用程序未知时，使用XML文档是首选，因为XML是web服务的领域的“世界语言”

### 二，XMLHttpRequest Level 2 使用指南

https://www.ruanyifeng.com/blog/2012/09/xmlhttprequest_level_2.html

### 三，不同的返回值和服务器：

其原理很简单，结构上基本不变，只是改变处理返回数据的方式.

1.Text/HTML格式
这种返回类型处理很简单，直接就当作字符串用就行了.为了方便使用，封装成如下函数:
```php
/**
 * @function 利用ajax动态交换数据(Text/HTML格式)
 * @param url   要提交请求的页面
 * @param jsonData  要提交的数据,利用Json传递
 * @param getMsg  这个函数可以获取到处理后的数据
 */
 function ajaxText(url,jsonData,getMsg)
 {
    //创建Ajax对象,ActiveXObject兼容IE5,6
    var oAjax = window.XMLHttpRequest?new XMLHttpRequest():new ActiveXObject("Microsoft.XMLHTTP");
    //打开请求
    oAjax.open('POST',url,true);//方法,URL,异步传输
    //发送请求
    var data = '';
    for(var d in jsonData)   //拼装数据
        data += (d + '=' +jsonData[d]+'&');
    oAjax.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    oAjax.send(data);
    //接收返回,当服务器有东西返回时触发
    oAjax.onreadystatechange = function ()
    {
        if(oAjax.readyState == 4 && oAjax.status == 200)
        {
            if(getMsg) getMsg(oAjax.responseText);
        }
    }
 }
```
 服务器端返回数据格式如下:
 例如:
```php
//返回的是xml格式
//header("Content-Type:text/xml;charset=utf-8");
//返回的是text或Json格式
header("Content-Type:text/html;charset=utf-8");
//禁用缓存,是为了数据一样的前提下还能正常提交，而不是缓存数据
header("Cache-Control:no-cache");
$username = $_POST['username'];  //获取用户名
if(empty($username))
    echo '请输入用户名';
else if($username == 'acme')
    echo '用户名已被注册';
else
    echo '用户名可用';

调用格式如下:

url = 'abc.php';
var jsonData={username:'acme',passw:'acme'};
ajaxText(url,jsonData,getMsg);
function getMsg(msg)
{
 //do something
}
```
2.XML格式
返回的是一个XML DOM对象，解析其中的数据就类似于HTML DOM 编程. 比如通过name获取标签对象(数组形式)，再从该数组中获取需要的标签对象，再从标签对象中获取文本值.
函数如下:
```php
/**
 * @function 利用ajax动态交换数据(XML格式)
 * @param url   要提交请求的页面
 * @param jsonData  要提交的数据,利用Json传递
 * @param tagName  要获取值的标签名
 * @param getMsg  这个函数可以获取到处理后的数据
 */
    function ajaxXML(url,jsonData,tagName,getMsg)
    {
    //创建Ajax对象,ActiveXObject兼容IE5,6
    var oAjax = window.XMLHttpRequest?new XMLHttpRequest():new ActiveXObject("Microsoft.XMLHTTP");
    //打开请求
    oAjax.open('POST',url,true);//方法,URL,异步传输
    //发送请求
    var data = '';
    for(var d in jsonData)   //拼装数据
        data += (d + '=' +jsonData[d] + '&');
    oAjax.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    oAjax.send(data);
    //接收返回,当服务器有东西返回时触发
    oAjax.onreadystatechange = function ()
    {
        if(oAjax.readyState == 4 && oAjax.status == 200)
        {
            var oXml =  oAjax.responseXML; //返回的是一个XML DOM对象
            var oTag = oXml.getElementsByTagName(tagName);
            var tagValue = oTag[0].childNodes[0].nodeValue;
            if(getMsg)getMsg(tagValue);
        }
    }
    }
```
    服务器端返回数据格式如下:
    例如:
```php
//返回的是xml格式
header("Content-Type:text/xml;charset=utf-8");
//返回的是text或Json格式
//header("Content-Type:text/html;charset=utf-8");
//禁用缓存,是为了数据一样的前提下还能正常提交，而不是缓存数据
header("Cache-Control:no-cache");
$username = $_POST['username'];  //获取用户名
if(empty($username))
    echo '<user><mes>请输入用户名</mes></user>'; //这些标签可以自定义
else if($username == 'acme')
    echo '<user><mes>用户名已被注册</mes></user>';
else
    echo '<user><mes>用户名可用</mes></user>';

调用格式如下:

var url = 'abc.php';
var jsonData = {username:'acme'};
ajaxXML(url,jsonData,'mes',getMsg);
function getMsg(msg)
 {
	 //do something
 }
```
3.返回json
函数如下：
```php
/**
 * @function 利用ajax动态交换数据(Text/HTML格式)，但是返回的是Json类型的文本数据

 * @param url   要提交请求的页面

 * @param jsonData  要提交的数据,利用Json传递

 * @param getMsg  这个函数可以获取到处理后的数据
 */
    function ajaxJson(url,jsonData,getMsg)
    {
    //创建Ajax对象,ActiveXObject兼容IE5,6
    var oAjax = window.XMLHttpRequest?new XMLHttpRequest():new ActiveXObject("Microsoft.XMLHTTP");
    //打开请求
    oAjax.open('POST',url,true);//方法,URL,异步传输
    //发送请求
    var data = '';
    for(var d in jsonData)   //拼装数据
        data += (d + '=' +jsonData[d] + '&');
    oAjax.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    oAjax.send(data);
    //接收返回,当服务器有东西返回时触发
    oAjax.onreadystatechange = function ()
    {
        if(oAjax.readyState == 4 && oAjax.status == 200)
        {
            var json = eval('('+oAjax.responseText+')');//把传回来的字符串解析成json对象
            if(getMsg)getMsg(json);
        }
    }
    }
```
 服务器端返回数据格式如下:
 例如:


```php
//返回的是xml格式
//header("Content-Type:text/xml;charset=utf-8");
//返回的是text或Json格式
header("Content-Type:text/html;charset=utf-8");
//禁用缓存,是为了数据一样的前提下还能正常提交，而不是缓存数据
header("Cache-Control:no-cache");
$username = $_POST['username'];  //获取用户名
if(empty($username))
    echo '{"mes":"请输入用户名"}';
else if($username == 'acme')
    echo '{"mes":"用户名已被注册"}';
else
    echo '{"mes":"用户名可用"}';

调用格式如下:

url = 'abc.php';
var jsonData={username:'acme',passw:'acme'};
ajaxText(url,jsonData,getMsg);
function getMsg(msg)
{
 //do something
}
```
为了方便使用，可以把三个函数合并.合并后的函数如下:
```php
/**
 * @function 利用ajax动态交换数据
 * @param url   要提交请求的页面
 * @param jsonData  要提交的数据,利用Json传递
 * @param getMsg  这个函数可以获取到处理后的数据
 * @param type    接受的数据类型,text/xml/json
 * @param tagName type = xml 的时候这个参数设置为要获取的文本的标签名
 * @return 无
 */
    function ajax(url,jsonData,getMsg,type,tagName)
    {
    //创建Ajax对象,ActiveXObject兼容IE5,6
    var oAjax = window.XMLHttpRequest?new XMLHttpRequest():new ActiveXObject("Microsoft.XMLHTTP");
    //打开请求
    oAjax.open('POST',url,true);//方法,URL,异步传输
    //发送请求
    var data = '';
    for(var d in jsonData)   //拼装数据
        data += (d + '=' +jsonData[d]+'&');
    oAjax.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    oAjax.send(data);
    //接收返回,当服务器有东西返回时触发
    oAjax.onreadystatechange = function ()
    {
        if(oAjax.readyState == 4 && oAjax.status == 200)
        {
            if(type == 'text')
            {
                if(getMsg) getMsg(oAjax.responseText);
            }
            else if(type == 'json')
            {
                var json = eval('('+oAjax.responseText+')');//把传回来的字符串解析成json对象
                if(getMsg)getMsg(json);
            }
            else if(type == 'xml')
            {
                var oXml =  oAjax.responseXML; //返回的是一个XML DOM对象
                var oTag = oXml.getElementsByTagName(tagName);
                var tagValue = oTag[0].childNodes[0].nodeValue;
                if(getMsg)getMsg(tagValue);
            }

        }
```





### 四，一篇文章带你详解 HTTP 协议（网络协议篇一）

https://www.huaweicloud.com/articles/736eef13dc3e4d73b10d155557bb93c5.html

### 五，[jQuery的ajax里dataType预期服务器返回数据类型](https://www.cnblogs.com/fightjianxian/p/12291430.html)

https://www.cnblogs.com/fightjianxian/p/12291430.html