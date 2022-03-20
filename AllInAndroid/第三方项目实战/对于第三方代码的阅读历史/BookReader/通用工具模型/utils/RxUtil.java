package com.justwayward.reader.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.justwayward.reader.ReaderApplication;

import java.lang.reflect.Field;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
/**此处是针对RxJava 进行一个封装管理借此机会，将RxJava整体撸一把http://www.xuetuwuyou.com/article/2335
 * https://www.zhihu.com/question/53151203
 * 第一步，明确RxJava 的功能，以及自己将要如何使用它，RxJava 的功能，还是在于数据变量的处理程序的一个重新的定义，
 * 这是一种直接的订阅式开发的方式，那么和平常的，以及什么时候用到呢，这是一种更有组织性的编程的方式，并且融合了线程，
 * 比起之前的那种散列式的开发的方式，这里可以直接找到彼此之间的关系，
 * 那么，他的使用场景是什么呢：
 * 最适合使用RxJava处理的四种场景
场景一： 单请求异步处理

由于在Android UI线程中不能做一些耗时操作，比如网络请求，大文件保存等，所以在开发中经常会碰到异步处理的情况，
我们最典型的使用场景是RxJava+Retrofit处理网络请求
场景二： 多异步请求连续调用
这种场景其实也很常见，我们做用户头像编辑的使用，一般就会有三个请求需要连续调用：

请求头像上传的地址
上传头像
更新用户信息
在平时的代码里，我们需要一步步callback嵌套下来，代码冗长太难看，而且不好维护，使用RxJava链式调用处理代码逻辑就会非常清晰
场景三： 多异步请求合并处理
场景四： 定时轮询
 */
public class RxUtil {

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable rxCreateDiskObservable(final String key, final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                LogUtils.d("get data from disk: key==" + key);
                String json = ACache.get(ReaderApplication.getsInstance()).getAsString(key);
                LogUtils.d("get data from disk finish , json==" + json);
                if (!TextUtils.isEmpty(json)) {
                    subscriber.onNext(json);
                }
                subscriber.onCompleted();
            }
        })
                .map(new Func1<String, T>() {
                    @Override
                    public T call(String s) {
                        return new Gson().fromJson(s, clazz);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static <T> Observable.Transformer<T, T> rxCacheListHelper(final String key) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())//指定doOnNext执行线程是新线程
                        .doOnNext(new Action1<T>() {
                            @Override
                            public void call(final T data) {
                                Schedulers.io().createWorker().schedule(new Action0() {
                                    @Override
                                    public void call() {
                                        LogUtils.d("get data from network finish ,start cache...");
                                        //通过反射获取List,再判空决定是否缓存
                                        if (data == null)
                                            return;
                                        Class clazz = data.getClass();
                                        Field[] fields = clazz.getFields();
                                        for (Field field : fields) {
                                            String className = field.getType().getSimpleName();
                                            // 得到属性值
                                            if (className.equalsIgnoreCase("List")) {
                                                try {
                                                    List list = (List) field.get(data);
                                                    LogUtils.d("list==" + list);
                                                    if (list != null && !list.isEmpty()) {
                                                        ACache.get(ReaderApplication.getsInstance())
                                                                .put(key, new Gson().toJson(data, clazz));
                                                        LogUtils.d("cache finish");
                                                    }
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable.Transformer<T, T> rxCacheBeanHelper(final String key) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())//指定doOnNext执行线程是新线程
                        .doOnNext(new Action1<T>() {
                            @Override
                            public void call(final T data) {
                                Schedulers.io().createWorker().schedule(new Action0() {
                                    @Override
                                    public void call() {
                                        LogUtils.d("get data from network finish ,start cache...");
                                        ACache.get(ReaderApplication.getsInstance())
                                                .put(key, new Gson().toJson(data, data.getClass()));
                                        LogUtils.d("cache finish");
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
