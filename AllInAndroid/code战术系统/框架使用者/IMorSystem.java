/**RxJava最重要的概念是什么？几个操作符方便你把数据变来变去，方便你做线程调度，让你不用去看callback hell？确实很方便，
 * 但如果只看到这一层，未免过于肤浅。以我用RxJava这几年肤浅的体验来看，RxJava最大的作用是提供一个优秀的，
 * 现成的响应式/流式调用封装，而你只需付出些许学习成本就可以少做很多工作。一个RxJava的调用链从create开始到subscribe结束，
 * 可以大概把整个调用链分为上游、中游、下游，上游数据源，中游数据变换，下游数据接收&展示。除非你变更整条调用链的数据结构，
 * 变更其中一块对外输出的数据，否则你修改上中下游任何一块都不会影响到其它地方，整条调用链可以起于应用的网络层，终于UI层，
 * 完美契合中大规模工程中的分层架构。举个例子，我的应用中用户的系统账号和IM账号是分开的，IM是第三方SDK，
 * 而且用户并不是一定要登录IM才能使用应用，但是进入IM相关的模块时，需要检查用户是否登录，如果登录，检查IM是否登录，
 * 如果没登录，检查是否缓存IM账号，通过接口获取IM账号并登录IM。这么一段逻辑用RxJava怎么做？

作者：知乎用户
链接：https://www.zhihu.com/question/53151203/answer/443672379
来源：知乎
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。 */


//reloadAccount = true : 无论当前状态如何都重新加载账号数据并执行登录
public Observable<Object> checkAndLogin(boolean reloadAccount) {
    // 数据上游，检查账号状态决定下一步操作
    return Observable.create((emitter) -> {
        if (!reloadAccount && isIMLogin) {
            emitter.onNext(ACTION_ALREADY_LOGIN);
            return;
        }
        if (!reloadAccount && hasCacheData()) {
            emitter.onNext(ACTION_CACHE_LOGIN);
            return;
        }
        // 用户没有登录系统账号，向下游返回异常
        if (!UserDataManager.isLogin()) {
            emitter.onError(new ApiExecption("请先登录"));
            return;
        }
        emitter.onNext(ACTION_RELOAD_LOGIN);
    })
    // 数据变换，是否需要请求账号数据
    .flatMap((Function)(actionCode) -> {
        if(actionCode == ACTION_RELOAD_LOGIN) {
            return NetworkManager.getIMAccount();
        }
        //按下游定义数据格式构建缓存数据发送
        JSONObject cacheData = new JSONObject();
        cacheData.put("isLogin", actionCode == ACTION_ALREADY_LOGIN);
        cacheData.put("userId", cachedUserId);
        cacheData.put("password", cachedUserPwd);
        return Observable.just(cacheData);
    })
    // 执行IM登录
    .flatMap((Function) (data) -> {
        if ( data.optBoolean("isLogin", false)) {
            return Observable.just(new Object());
        }
        String userId = data.getString("userId");
        String password = data.getString("password");
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password)) {
            return Observable.error(new ApiExecption("获取IM账号失败"));
        }
        cacheAccountData(userId, password);
        // 返回RxJava封装过后的第三方IM登录方法
        return rxIMLogin();
    })
    // 出现登录异常时请求账号数据重试
    .onErrorResumeNext((Function)(throwable) -> {
        // 如果没有重试次数则直接将异常交给下游
        if (retryTimes <= 0) {
            return Observable.error(throwable);
        }
        retryTimes--;
        return checkAndLogin(true);
    })
    // 登录成功重试次数重置
    .map((Function)(o) -> {
        retryTimes = 1;
        return o;
    })
    .subscribeOn(Schedulers.io())
    .observableOn(AndroidSchedulers.mainThread());
}

/**

这么一段“中游”代码，封装了是否强制重新加载账号、检查IM是否登录、检查系统账号是否登录、请求IM账号、缓存已获取账号、
执行IM登录，出错重连，异常处理。而下游的subscribe订阅，只需关心“IM登录了没？”，在初始化界面，如果登录了就去请求预加载数据，
如果没登录就pass，不管异常。在IM界面，如果登录了就去请求其它IM数据，没登录直接弹Alert提示异常并退出界面。
这个例子应该能比较好的说明“流式调用”是怎么回事了吧。你用其它手段能实现这一套逻辑吗？当然能，而且很简单，
不过是你用另外一种方式实现了“流式调用”而已，只是可能没有RxJava实现的代码这么紧密，修改方便罢了。
分层架构就是这样，面向协议编程。比如分页加载，我不管你上游中游要做多少事，下游UI层只关心拿到List<T>，
是否有更多，触发加载更多时调用；中游只关心把数据变换成下游所需的数据，RxJava用这些数据操作符实现了中游不用关心上游数据，
上游用接口请求数据下来，还是Observable.just()一段假数据下来，对中游、下游没有丝毫影响。
这种调用形式在某些客户端先用假数据看效果，后端接口以后给到的情景下，实现和替换成本极其低廉。说了这么多总结一下，
组件间调用可以看作一个应用的“神经系统”，而RxJava相当于一套优秀的，现成的“神经信号传递机制”，要用，
就要替换整个应用“神经系统的信号传递机制”。如果你的应用现行的机制已经运行良好使用方便，且替换成本高昂，
那么完全没有必要为了Rx而集成RxJava，或者说你在写随时可能拿去其它环境使用的组件，那更不能随便集成SDK；
但如果你应用现在的相互调用写的乱七八糟，
或者开始搭建新应用，为什么放着现成的机制不拿过来用而要自己再写一套蹩脚的callback呢？ */