​												                         多样式的RecycleView demo

举一反三类型分析：View的绘制。基本思路，是通过position计算数据集合里面的数据，当前的位置的View，数据已经知道了，如何根据数据类型，动态的选择需要的View。只需要根据不同的position实例化出不同的ViewHolder就可以实现。

步骤一：确定有多少种显示View，将View转化为ViewHolder：

```
class TextCardViewHeader5ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle5 = view.findViewById<TextView>(R.id.tvTitle5)
    val tvFollow = view.findViewById<TextView>(R.id.tvFollow)
    val ivInto5 = view.findViewById<ImageView>(R.id.ivInto5)
}
```

其中view从这里产生，将layout和其中的view进行了剥离：

```
fun getViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {

    TEXT_CARD_HEADER4 -> TextCardViewHeader4ViewHolder(R.layout.item_text_card_type_header_four.inflate(parent))

    TEXT_CARD_HEADER5 -> TextCardViewHeader5ViewHolder(R.layout.item_text_card_type_header_five.inflate(parent))
```

```
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View layout = LayoutInflater.from(mContext).inflate(R.layout.item_view, parent, false);
    return new MyHolder(layout);
}
```

步骤二：将Adapter进行接口的转换：

```
override fun getItemViewType(position: Int) = RecyclerViewHelp.getItemViewType(dataList[position])

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecyclerViewHelp.getViewHolder(parent, viewType)
```

步骤三：绑定View和对应的数据：

```
override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = dataList[position]
    when (holder) {
        is TextCardViewHeader5ViewHolder -> {
            holder.tvTitle5.text = item.data.text
            if (item.data.actionUrl != null) holder.ivInto5.visible() else holder.ivInto5.gone()
            if (item.data.follow != null) holder.tvFollow.visible() else holder.tvFollow.gone()
            holder.tvFollow.setOnClickListener { LoginActivity.start(fragment.activity) }
            setOnClickListener(holder.tvTitle5, holder.ivInto5) { ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text) }
        }
```



步骤四：对于Data的构造:

```
data class Discovery(val itemList: List<Item>, val count: Int, val total: Int, val nextPageUrl: String?, val adExist: Boolean) : Model() {

    data class Item(val `data`: Data, val type: String, val tag: Any?, val id: Int = 0, val adIndex: Int)
```

步骤五：在Fragment或者Activity中将RV和Adapter，LayoutManager，相关设置进行设置：

```
adapter = DiscoveryAdapter(this, viewModel.dataList)
recyclerView.layoutManager = LinearLayoutManager(activity)
recyclerView.adapter = adapter
recyclerView.setHasFixedSize(true)
recyclerView.itemAnimator = null
```

步骤六：在数据更新时候，进行页面的刷新：

```
viewModel.dataList.clear()
viewModel.dataList.addAll(response.itemList)
adapter.notifyDataSetChanged()
```



PS：
1，设置不同的缓存池：
recyclerView.setItemViewCacheSize(int);
2，https://developer.android.com/guide/topics/ui/layout/recyclerview?hl=zh-cn

