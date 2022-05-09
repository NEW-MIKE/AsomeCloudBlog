如何理解父view对子view的限制？  
onMeasure的两个参数既然是父view对子view的限制，那么这个限制的值到底是哪来的呢？  
实际上，父view对子view的限制绝大多数就来自于我们开发者所设置的layout开头的这些属性   
比方说我们给一个imageview设置了他的layout_width和layout_height 这2个属性，那这2个属性其实就是我们开发者   
所期望的宽高属性，但是要注意了，   
设置的这2个属性是给父view看的,实际上对于绝大多数的layout开头的属性这些属性都是设置给父view看的   
为什么要给父view看？因为父view要知道这些属性以后才知道要对子view的测量加以什么限制？   
到底是不限制（UNSPECIFIED）？还是限制个最大值（AT_MOST），让子view不超过这个值？还是直接限制死，我让你是多少就得是多少（EXACTLY）。   

https://juejin.cn/post/6844903622233292807

https://blog.csdn.net/lmj623565791/article/details/38339817


https://www.jianshu.com/p/705a6cb6bfee

当一个MotionEvent产生了以后，就是你的手指在屏幕上做一系列动作的时候，系统需要把这一系列的MotionEvent分发给一个具体的View。我们重点需要了解这个分发的过程，那么系统是如何去判断这个事件要给哪个View，也就是说是如何进行分发的呢？

链接：https://www.jianshu.com/p/238d1b753e64

https://segmentfault.com/a/1190000039254459

