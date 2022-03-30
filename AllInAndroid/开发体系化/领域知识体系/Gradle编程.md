从 1.5.0-beta1 开始，Gradle 插件包含一个 Transform API，允许 3rd 方插件在将编译的类文件转换为 dex 文件之前对其进行操作。（API 在 1.4.0-beta2 中存在，但在 1.5.0-beta1 中已完全修改）
此 API 的目标是简化注入自定义类操作而无需处理任务，并为操作的内容提供更大的灵活性。内部代码处理（jacoco、progard、multi-dex）在 1.5.0-beta1 中已经全部转移到这个新机制。
注意：这仅适用于 javac/dx 代码路径。Jack 目前不使用此 API。https://blog.csdn.net/yubo_725/article/details/118567763