Unable to find method 'org.gradle.api.artifacts.result.ComponentSelectionReason.getDescription()Ljava/lang/String;'.
Possible causes for this unexpected error include:
Gradle's dependency cache may be corrupt (this sometimes occurs after a network connection timeout.)
Re-download dependencies and sync project (requires network)

The state of a Gradle build process (daemon) may be corrupt. Stopping all Gradle daemons may solve this problem.
Stop Gradle build processes (requires restart)

Your project may be using a third-party plugin which is not compatible with the other plugins in the project or the version of Gradle requested by the project.

In the case of corrupt Gradle processes, you can also try closing the IDE and then killing all Java processes.



也就是说Idea版本跟gradle版本适配有关系。 idea创建项目时直接指定最版本的gradle(我使用idea版本是2019.3.3，当前时间是2021-08-04)。Gradle官网的最新版本是2021-7-2更新的7.1.1版本。

再次查看Idea的gradle的配置。File->settings->搜索gradle, 发现gradle的配置指定的是gradle-wrapper.properties文件，所以我们更改gradle版本适配idea自然会解决问题，
再次查看grdle的配置，“Use Gradle from”有个”Specified location”选项，截图如下。居然可以手动指定本地gradle的配置。手动指定本地gradle的配置,不依赖idea自带的gradle的插件指定版本。下载插件指定适配的gradle使用版本(idea2019.3.3适配gradle版本6.8、6.9,下载地址：https://gradle.org/releases)。,并手动指定。

自己的这个项目，采用的是网络上的项目直接clone下来处理，IDE是2020 June 25，目前是2022/3/27，项目原本采用的gradle版本是：distributionUrl=https\://services.gradle.org/distributions/gradle-7.4-bin.zip

两个思路：1，降级gradle版本

2，指定特定的gradle配置。

由于作者是采用降级，我这里就尝试一下指定gradle-wrapper.properties，采用的版本是7.33失败，最后采用降级处理改为（系统自带的）

```
distributionUrl=https\://services.gradle.org/distributions/gradle-6.1.1-all.zip
```

最后提示需要最低7.02，然后自动升级distributionUrl=https\://services.gradle.org/distributions/gradle-7.0.2-all.zip



后续有时间，可以研究一下IDE 插件，项目的gradle需求之间的矛盾和冲突，为什么项目的会要求gradle的版本呢，那就只有升级IDE了，最终搞定