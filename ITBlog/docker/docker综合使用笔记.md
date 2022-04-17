







优化各个命令的使用，实现docker 自由

### docker如何将内部文件映射到外部？挂载本地目录及实现文件共享的方法

docker run -it -v /test（主机的绝对路径）:/soft（容器里面的绝对路径，/开头） centos（镜像名称） /bin/bash（在bash格式下显示）。

如果容器已经run了，该如何挂载，重新打包，然后挂载。



### 镜像打包



















### 常见问题

docker tool box的virtualbox版本要更新

设置的源在dockers里面

docker ngrok 穿透命令：
docker run -it --link 启动容器的名字 -e NGROK_AUTHTOKEN=23An5iCfMoezmpramgZQPhV9oR4_2BRBKAH5rGfTgqQqTfYug ngrok/ngrok http 启动容器的名字:70

然后在这个容器就可以共享这个docker-machine了

