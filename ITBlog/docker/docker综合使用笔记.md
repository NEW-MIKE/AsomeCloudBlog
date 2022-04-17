







## 优化各个命令的使用，实现docker 自由

### docker文件映射

docker如何将内部文件映射到外部？挂载本地目录及实现文件共享的方法

docker文件夹映射的两种方式---主机卷映射和共享文件夹映射。挂载的时候，

我这里是用redis来测试。首先，要创建好本地要映射的文件夹，我的 D:\Docker\redis\data。docker pull redis。查看镜像

```undefined
docker images
```

启动并且映射，

```haskell
docker run -p 6777:6379 -v /D/Docker/redis/data:/data -d redis redis-server --appendonly yes
```

开启持久化存储
redis-server --appendonly yes

- **-d:**让容器在后台运行。

第一个, 在windows向docker映射文件目录的时候, 需要把盘符变成 /

比如D:/abc/def, 需要将windows的路径写成 //d/abd/def

第二个, docker默认安装的是在C盘, 第一种写法不会报错,但是可能会出现文件没有映射到docker里面的问题.

需要把要映射的文件放在C盘User目录下, 比如桌面

挂载Windows文件夹到Docker容器中：
/d/WindowsFolder 对应宿主的文件夹路径 D:\WindowsFolder；

docker run -it -v /test（主机的绝对路径）:/soft（容器里面的绝对路径，/开头） centos（镜像名称） /bin/bash（在bash格式下显示）。

如果容器已经run了，该如何挂载，重新打包，然后挂载。



### docker常规操作记录

\1. 启动容器，配置或确认文件接收路径

```
docker ps -a
docker start 容器ID或容器名
docker exec -it 容器ID或容器名 bash
```

\2. 关闭容器

```
docker stop 容器ID或容器名
```

删除容器使用 **docker rm** 命令：

```
$ docker rm -f 1e560fca3906
```

**导出容器**

如果要导出本地某个容器，可以使用 **docker export** 命令。

```
$ docker export 1e560fca3906 > ubuntu.tar
```

这样将导出容器快照到本地文件。

**导入容器快照**

可以使用 docker import 从容器快照文件中再导入为镜像，以下实例将快照文件 ubuntu.tar 导入到镜像 test/ubuntu:v1:

```
$ cat docker/ubuntu.tar | docker import - test/ubuntu:v1
```

网络端口的快捷方式

通过 **docker ps** 命令可以查看到容器的端口映射，**docker** 还提供了另一个快捷方式 **docker port**，使用 **docker port** 可以查看指定 （ID 或者名字）容器的某个确定端口映射到宿主机的端口号。

上面我们创建的 web 应用容器 ID 为 **bf08b7f2cd89** 名字为 **wizardly_chandrasekhar**。

我可以使用 **docker port bf08b7f2cd89** 或 **docker port wizardly_chandrasekhar** 来查看容器端口的映射情况。

多端口映射：

这都意味着这个flag可以多次出现，所以此处可以多次指定端口映射规则。

例：docker run -d -p 80:80 -p 22:22



### 常见问题

docker tool box的virtualbox版本要更新

设置的源在dockers里面

docker ngrok 穿透命令：
docker run -it --link 启动容器的名字 -e NGROK_AUTHTOKEN=23An5iCfMoezmpramgZQPhV9oR4_2BRBKAH5rGfTgqQqTfYug ngrok/ngrok http 启动容器的名字:70

然后在这个容器就可以共享这个docker-machine了

此处是重启一个一个docker服务器的过程。

https://github.com/NEW-MIKE/MeLib/blob/main/%E5%AE%B9%E5%99%A8%E4%BF%AE%E5%A4%8D%E6%AD%A5%E9%AA%A4.md



关于自动化执行脚本的设置：
https://blog.csdn.net/qq1049/article/details/78676504
