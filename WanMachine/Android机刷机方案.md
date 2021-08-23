1，有什么方案

2，需要准备什么工具
3，如何操作这些工具

### 方案一：

https://umshare.com/2016/10/meizu-m3note-change-your-chinese-firmware-into-global-firmware/
http://forum.flymeos.com/thread-8994-1-1.html

更改ID：讲解：update.zip是国际版固件，并且得改成这个名称才能识别的；global.sh是改ID脚本，beta.sh是强刷脚本，当改ID脚本刷入国际版验证固件提示版本过低才使用这个强刷脚本。
第一步，先把手机Root了，这时因为稍后要用来刷入国际包和安装脚本的。
方法一：用系统自带的Root，方法：[设置]，找到[Flyme账号]并登入，然后返回上一层找到[指纹和安全]进入，滑到底部就可以看到[Root权限]，然后就进行Root吧，系统自动重启完之后再返回这里看看有没有成功，然后安装Supersu这个权限管理软件。
方法二：安装Kingroot这类一键Root（此类虽然快，但是会捆绑很多没用的东西，慎入）第二步，下载国际版的update.zip包，然后下载beta.sh和global.sh放在手机根目录（/storage/emulated/0/）
讲解：update.zip是国际版固件，并且得改成这个名称才能识别的；global.sh是改ID脚本，beta.sh是强刷脚本，当改ID脚本刷入国际版验证固件提示版本过低才使用这个强刷脚本。

第三步，在手机安装一个终端输入（我用的是ConnectBot下面简称终端，选择Local模式，回车就可以进入了），安装后打开并授予Root权限。

第四步，关键步骤
1、在终端输入su并回车 这里表示切换到超级管理员用户，成功的就会在下一行显示root@m3note:/ #，这个#号表示成功，否则你就要重新Root权限。

2、输入sh /storage/emulated/0/global.sh并回车（注意，前面的sh与/是有空格的，）我试了很多次刷改ID脚本识别，后来直接刷入beta.sh这个脚本绕过验证固件强刷（慎重考虑，有可能刷成砖）。
2-1、就会重启进入到刷机模式了，记得两个选项都要勾选（Update和清除数据），然后选择开始刷入，这时会先验证国际版固件，如果提示失败，就重试第四步打开终端刷入改ID脚本，如果还是提示固件版本过低，只有强刷了。
2-1-1、强刷beta.sh，方法跟上2步一样的，只不过第2这里改为输入sh /storage/emulated/0/beta.sh而已。

3、这时会跳过验证固件，并且会刷入，然后坐等成功或者失败吧。

![s61030-133934](http://umshare.com/wp-content/uploads/2016/10/S61030-133934-576x1024.jpg) ![s61030-134014](http://umshare.com/wp-content/uploads/2016/10/S61030-134014-576x1024.jpg)![s61030-134005](http://umshare.com/wp-content/uploads/2016/10/S61030-134005-576x1024.jpg)

刷完机进入系统后，我发现显示的版本变成5.1.3.3G而不是我下载的5.1.3.4G（根据其他人反映这是正常现象）。
然后还有一些小问题，就是安装非官方应用商店的第三方应用，应用安装器会一直在停留在正在验证，我们还是安装一个其他的安装器吧。

![s61029-165601](http://umshare.com/wp-content/uploads/2016/10/S61029-165601-576x1024.jpg)

下面附上所需的文件，因为update.zip太大，暂时不上传了，请自行到官网下载。Hi all, today a create a tutorial, that help you to change your Chinese firmware (5.1.10.0A) into global firmware (5.1.3.4G).

The first you need:
\1. Root (Create flyme account or login, and reques the root)
\2. SuperSU (for permissions)
\3. Terminal (can download from Google Play)

Download files beta and global archives, in attachment

Or download from cloud - [beta.sh](https://yadi.sk/d/lVjZXLW6u58fG) | [global.sh](https://yadi.sk/d/yWfZ-zOStCQG3)

After:
Extract the contents of the beta.zip and global.zip to your root directory of sdcard, there place update.zip

*[Tutorial 1]* Open Terminal, and write next:

- su
- cd/sdcard
- sh beta.sh
- reboot


After boot up do this:

- su
- cd/sdcard
- sh global.sh
- reboot to recovery, and apply update

*
[Tutorial 2]* If it doesn't help, you will get notice **Firmware too old ...** do only this:

Open Terminal, and write next:

- su

- cd/sdcard

- sh beta.sh

- reboot to recovery, and again apply update

  

After, phone must start the booting, this take ~5 minutes, then phone boot up, you can use new stable firmware ![img](http://bbs.res.flymeos.com/resources/flymeosimg/image/smiley/default/smile.gif)

### 方案二：

