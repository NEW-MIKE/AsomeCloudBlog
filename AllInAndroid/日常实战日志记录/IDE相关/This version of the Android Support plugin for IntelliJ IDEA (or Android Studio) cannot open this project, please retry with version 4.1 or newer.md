问题：Unrecognized Android Studio (or Android Support plugin for IntelliJ IDEA) version '202.7660.26.42.7351085', please retry with version 2020.3.1 or newer.

解决方式 1：
 在工程的 gradle.properties 文件中添加：
 `android.injected.studio.version.check=false`



在这个部分，别人直接开箱系统的代码来进行查找问题，进行解决，厉害了我的哥

解决方式 2:
 选择 `File -> Project Structure` 配置 Project 的 Android Gradle Plugin Version
 比如将其从 7.0.2 修改为 4.2.2

解决方式 3:
 选择 `Android Studio -> Check for Updates` 更新 Android Studio 的版本