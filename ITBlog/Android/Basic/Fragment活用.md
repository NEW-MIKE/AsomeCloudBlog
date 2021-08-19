                                           Fragment活用

其存在的本质，就是可以在同一个页面进行动态的替换，决定替换的因素，可以是尺寸，还有自己主动的设置，自己通过模拟器，设置平板还有手机的动态，然后在这个的基础上，添加一个网页的选择的tab页面和对应的fragment的切换和替换。
直接从代码里面进行解析，这就是最佳的资料，关于用法，不同的示例，在这里进行最佳的垂直的校验，分为2个部分：base部分，示例部分。
示例部分：动态适配，自己调用，制作一个格式，以后直接依据文档直接调用，直接从现有的项目里面进行剥离，而后在以后的阅读的代码的过程中，不断的补充各种用法，然后对各种用法，了然于胸，然后基于此，不断的积累经验。去拓展自己的各种可能性的应用中，提高自己的效率。这是基于使用的学习的方式，如何使用中学习，在使用中学习。阅读代码，充实自己的经验，基于经验，去构造产品。在自己的使用和不断的实践磨合中，逐步升级出自己的一套仓库，
做这件事情，必须长期怀有的心态是：这是我为以后的自己更快速的开发产品，制作的说明书，先运量一下，等自己的体内产生了这样的成分之后，再行动。先对这个过程产生一个愿景，然后在这个愿景的驱动下，去做事，先去想象这样的愿景，在这样的愿景下做事。这也可以作为自己的完成自己的想法的大背景下，去做事的一个场景。以后先做一个愿景，再做事，基于此调动自己的知识和愿景。以此作为一个模式，来培养。

### base部分：

步骤一：确定公共的部分：从子类获取到View，然后在这里操作公共的部件部分

```
open class BaseFragment : Fragment(), RequestLifecycle {

    /**
     * 是否已经加载过数据
     */
    private var mHasLoadedData = false

    /**
     * Fragment中由于服务器或网络异常导致加载失败显示的布局。
     */
    private var loadErrorView: View? = null

    /**
     * Fragment中inflate出来的布局。
     */
    protected var rootView: View? = null

    /**
     * Fragment中显示加载等待的控件。
     */
    protected var loading: ProgressBar? = null

    /**
     * 依附的Activity
     */
    lateinit var activity: Activity

    /**
     * 日志输出标志
     */
    protected val TAG: String = this.javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 缓存当前依附的activity
        activity = getActivity()!!
        logD(TAG, "BaseFragment-->onAttach()")
    }
```

```
override fun onResume() {
    super.onResume()
    logD(TAG, "BaseFragment-->onResume()")
    //MobclickAgent.onPageStart(javaClass.name)
    //当Fragment在屏幕上可见并且没有加载过数据时调用
    if (!mHasLoadedData) {
        loadDataOnce()
        logD(TAG, "BaseFragment-->loadDataOnce()")
        mHasLoadedData = true
    }
}
```



```
override fun onDestroyView() {
    super.onDestroyView()
    logD(TAG, "BaseFragment-->onDestroyView()")
    EventBus.getDefault().unregister(this)
    if (rootView?.parent != null) (rootView?.parent as ViewGroup).removeView(rootView)
}
```



```
/**
 * 在Fragment基类中获取通用的控件，会将传入的View实例原封不动返回。
 * @param view Fragment中inflate出来的View实例。
 * @return  Fragment中inflate出来的View实例原封不动返回。
 */
fun onCreateView(view: View): View {
    logD(TAG, "BaseFragment-->onCreateView()")
    rootView = view
    loading = view.findViewById(R.id.loading)
    if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    return view
}

/**
 * 页面首次可见时调用一次该方法，在这里可以请求网络数据等。
 */
open fun loadDataOnce() {

}
```



```
override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return super.onCreateView(inflater.inflate(R.layout.fragment_main_container, container, false))
}
```

步骤二：确定需要子类继承的 部分



### 示例部分：

#### 自动适配部分：使用限定符（大小，分辨率，方向）(layout-large)或者最小宽度限定符（layout-sw600dp）

步骤一：确定不同的限定条件下，加载什么样的xml。然后用文件夹的名字来区分
步骤二：基于不同的页面，使用不同的逻辑控制代码：

```
  @Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
    final ViewHolder holder = new ViewHolder(view);
    view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            News news = mNewsList.get(holder.getAdapterPosition());
            if (isTwoPane) {
                NewsContentFragment newsContentFragment = (NewsContentFragment)
                        getFragmentManager().findFragmentById(R.id.news_content_fragment);
                newsContentFragment.refresh(news.getTitle(), news.getContent());
            } else {
                NewsContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());
            }
        }
    });
    return holder;
}
```



```
         @Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (getActivity().findViewById(R.id.news_content_layout) != null) {
        isTwoPane = true; // 可以找到news_content_layout布局时，为双页模式
    } else {
        isTwoPane = false; // 找不到news_content_layout布局时，为单页模式
    }
}
```





#### 与tab联动部分：

##### 一：TabLayout 和ViewPager联用：

步骤一：
布局ViewPager和TabLayout,:

```
protected fun initVeiwPager() {
    viewPager = rootView?.findViewById(R.id.viewPager)
    tabLayout = rootView?.findViewById(R.id.tabLayout)

    viewPager?.offscreenPageLimit = offscreenPageLimit
    viewPager?.adapter = adapter
    tabLayout?.setTabData(createTitles)
    tabLayout?.setOnTabSelectListener(object : OnTabSelectListener {

        override fun onTabSelect(position: Int) {
            viewPager?.currentItem = position
        }

        override fun onTabReselect(position: Int) {

        }
    })
    pageChangeCallback = PageChangeCallback()
    viewPager?.registerOnPageChangeCallback(pageChangeCallback!!)
}
```



```
inner class VpAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = mutableListOf<Fragment>()

    fun addFragments(fragment: Array<Fragment>) {
        fragments.addAll(fragment)
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}
```

步骤二：设置第二种联动的方式：

```
inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        tabLayout?.currentTab = position
    }
}
```

步骤三：在子类加入两者的具体值：

```
override val createTitles = ArrayList<CustomTabEntity>().apply {
    add(TabEntity(GlobalUtil.getString(R.string.discovery)))
    add(TabEntity(GlobalUtil.getString(R.string.commend)))
    add(TabEntity(GlobalUtil.getString(R.string.daily)))
}

override val createFragments: Array<Fragment> = arrayOf(DiscoveryFragment.newInstance(), CommendFragment.newInstance(), DailyFragment.newInstance())
```

PS：其基类中申明了能操作的部分还有需要子类补充的部分：

```
abstract class BaseViewPagerFragment : BaseFragment() {

    protected var viewPager: ViewPager2? = null

    protected var tabLayout: CommonTabLayout? = null

    protected var pageChangeCallback: PageChangeCallback? = null

    protected val adapter: VpAdapter by lazy { VpAdapter(getActivity()!!).apply { addFragments(createFragments) } }

    protected var offscreenPageLimit = 1

    abstract val createTitles: ArrayList<CustomTabEntity>

    abstract val createFragments: Array<Fragment>
```



##### 二，自定义按键，来联动Fragment：

步骤一：定义对应的按键的样式：

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="42dp"
    android:background="@color/colorPrimary"
    android:paddingLeft="24dp"
    android:paddingRight="24dp">

    <ImageView
        android:id="@+id/ivHomePage"
        android:layout_width="23dp"
        android:layout_height="23dp"
        android:src="@drawable/sel_btn_home_page"
        app:layout_constraintBottom_toTopOf="@+id/tvHomePage"
        app:layout_constraintEnd_toStartOf="@id/ivCommunity"
        app:layout_constraintHorizontal_chainStyle="spread_inside"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/tvHomePage"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:enabled="false"
        android:gravity="center"
        android:text="1111"
        android:textColor="@color/sel_bottom_navigation_bar_radio_color"
        android:textSize="8sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="@id/ivHomePage"
        app:layout_constraintStart_toStartOf="@id/ivHomePage"
        app:layout_constraintTop_toBottomOf="@id/ivHomePage"
         />

    <ImageView
        android:id="@+id/ivCommunity"
        android:layout_width="23dp"
        android:layout_height="23dp"
        android:padding="2dp"
        android:src="@drawable/sel_btn_home_page"
        app:layout_constraintBottom_toTopOf="@id/tvCommunity"
        app:layout_constraintEnd_toStartOf="@id/ivRelease"
        app:layout_constraintStart_toEndOf="@id/ivHomePage"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/tvCommunity"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:enabled="false"
        android:gravity="center"
        android:text="2222"
        android:textColor="@color/sel_bottom_navigation_bar_radio_color"
        android:textSize="8sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="@id/ivCommunity"
        app:layout_constraintStart_toStartOf="@id/ivCommunity"
        app:layout_constraintTop_toBottomOf="@+id/ivCommunity"
      />

    <ImageView
        android:id="@+id/ivRelease"
        android:layout_width="30dp"
        android:layout_height="30dp"
        android:src="@drawable/sel_btn_home_page"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@id/ivNotification"
        app:layout_constraintStart_toEndOf="@id/ivCommunity"
        app:layout_constraintTop_toTopOf="parent" />

    <ImageView
        android:id="@+id/ivNotification"
        android:layout_width="23dp"
        android:layout_height="23dp"
        android:padding="1dp"
        android:src="@drawable/sel_btn_home_page"
        app:layout_constraintBottom_toTopOf="@id/tvNotification"
        app:layout_constraintEnd_toStartOf="@id/ivMine"
        app:layout_constraintStart_toEndOf="@id/ivRelease"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/tvNotification"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:enabled="false"
        android:gravity="center"
        android:text="3333"
        android:textColor="@color/sel_bottom_navigation_bar_radio_color"
        android:textSize="8sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="@id/ivNotification"
        app:layout_constraintStart_toStartOf="@id/ivNotification"
        app:layout_constraintTop_toBottomOf="@+id/ivNotification"
         />

    <ImageView
        android:id="@+id/ivMine"
        android:layout_width="23dp"
        android:layout_height="23dp"
        android:padding="2dp"
        android:src="@drawable/sel_btn_home_page"
        app:layout_constraintBottom_toTopOf="@id/tvMine"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toEndOf="@id/ivNotification"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/tvMine"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:enabled="false"
        android:gravity="center"
        android:text="4444"
        android:textColor="@color/sel_bottom_navigation_bar_radio_color"
        android:textSize="8sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="@id/ivMine"
        app:layout_constraintStart_toStartOf="@id/ivMine"
        app:layout_constraintTop_toBottomOf="@+id/ivMine"

    />


    <androidx.constraintlayout.widget.Group
        android:id="@+id/btnHomePage"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="@+id/tvHomePage"
        app:layout_constraintEnd_toEndOf="@id/ivHomePage"
        app:layout_constraintStart_toStartOf="@id/ivHomePage"
        app:layout_constraintTop_toTopOf="@+id/ivHomePage"
        tools:background="@color/blackAlpha50" />

    <androidx.constraintlayout.widget.Group
        android:id="@+id/btnCommunity"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="@+id/tvCommunity"
        app:layout_constraintEnd_toEndOf="@id/ivCommunity"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="@id/ivCommunity"
        app:layout_constraintTop_toTopOf="@+id/ivCommunity"
        app:layout_constraintVertical_bias="0.0"
        tools:background="@color/blackAlpha50" />

    <androidx.constraintlayout.widget.Group
        android:id="@+id/btnNotification"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="@+id/tvNotification"
        app:layout_constraintEnd_toEndOf="@id/ivNotification"
        app:layout_constraintStart_toStartOf="@id/ivNotification"
        app:layout_constraintTop_toTopOf="@+id/ivNotification"
        tools:background="@color/blackAlpha50" />

    <androidx.constraintlayout.widget.Group
        android:id="@+id/btnMine"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="@+id/tvMine"
        app:layout_constraintEnd_toEndOf="@id/ivMine"
        app:layout_constraintStart_toStartOf="@id/ivMine"
        app:layout_constraintTop_toTopOf="@+id/ivMine"
        tools:background="@color/blackAlpha50" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

![image-20210812124047378](C:\Users\John-Mike\AppData\Roaming\Typora\typora-user-images\image-20210812124047378.png)

步骤二：将样式include到Activity的xml中：

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/colorPrimary"
    tools:context=".eyepetizer.EyepetizerActivity">
    <FrameLayout
        android:id="@+id/homeActivityFragContainer"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:background="@color/black" />

    <View
        android:layout_width="match_parent"
        android:layout_height="1px"
        android:background="@color/grayDark" />
    <include layout="@layout/layout_bottom_navigation_bar" />

</LinearLayout>
```

步骤三：根据不同的按键点击效果，替换不同的Fragment：

```
private void setTabSelection(int index) {
    clearAllSelected();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    hideFragments(fragmentTransaction);
    switch (index){
        case 1:
            ivHomePage.setSelected(true);
            tvHomePage.setSelected(true);
            if(communityFragment == null)
            {
                communityFragment = CommunityFragment.newInstance("","");
                fragmentTransaction.add(R.id.homeActivityFragContainer,communityFragment);
            }
            else {
                fragmentTransaction.show(communityFragment);
            }
            break;
            
            
        fragmentTransaction.commitAllowingStateLoss();
```





