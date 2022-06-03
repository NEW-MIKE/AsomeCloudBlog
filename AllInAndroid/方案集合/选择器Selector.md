#### 使用场景：

该方案的使用场景是多个View进行联动的时候，可以进行考虑进行使用。在于进行联动的响应的方式。这里不仅涉及到了View，以及ViewGroup的联动的方式，自定义View以及自定义的要求。被选中之后，如何组织逻辑呢。

#### 技术要点：

```
public abstract class Selector extends FrameLayout implements View.OnClickListener 
```

```
public class AgeSelector extends Selector 
```

```
public class OrderSelector extends Selector
```

```
<taylor.com.selector.OrderSelector
```

```
public class SelectorGroup {
```

```
//multiple-choice
SelectorGroup multipleGroup = new SelectorGroup();
multipleGroup.setChoiceMode(SelectorGroup.MODE_MULTIPLE_CHOICE);
multipleGroup.setStateListener(new MultipleChoiceListener());
((Selector) findViewById(R.id.selector_10)).setGroup("multiple", multipleGroup);
((Selector) findViewById(R.id.selector_20)).setGroup("multiple", multipleGroup);
((Selector) findViewById(R.id.selector_30)).setGroup("multiple", multipleGroup);

//single-choice
SelectorGroup singleGroup = new SelectorGroup();
singleGroup.setChoiceMode(SelectorGroup.MODE_SINGLE_CHOICE);
singleGroup.setStateListener(new SingleChoiceListener());
((Selector) findViewById(R.id.single10)).setGroup("single", singleGroup);
((Selector) findViewById(R.id.single20)).setGroup("single", singleGroup);
((Selector) findViewById(R.id.single30)).setGroup("single", singleGroup);

//order-choice
SelectorGroup orderGroup = new SelectorGroup();
orderGroup.setStateListener(new OrderChoiceListener());
orderGroup.setChoiceMode(new OderChoiceMode());
((Selector) findViewById(R.id.selector_starters_duck)).setGroup("starters", orderGroup);
((Selector) findViewById(R.id.selector_starters_pork)).setGroup("starters", orderGroup);
((Selector) findViewById(R.id.selector_starters_springRoll)).setGroup("starters", orderGroup);
((Selector) findViewById(R.id.selector_main_pizza)).setGroup("main", orderGroup);
((Selector) findViewById(R.id.selector_main_pasta)).setGroup("main", orderGroup);
((Selector) findViewById(R.id.selector_soup_mushroom)).setGroup("soup", orderGroup);
((Selector) findViewById(R.id.selector_soup_scampi)).setGroup("soup", orderGroup);
orderGroup.setSelected(true, (Selector) findViewById(R.id.selector_starters_duck));
```

#### 技术思路：

此处是Group级别的操作，内部的元素的显示，放在子类来完成。意味着，我就只要添加我的子view来就行了。完成我的点击的操作的统一控制，以及资源的回收的统一的控制，此处是形式上的利于编程的控制，实际上在编译器之后，都会被编译通过其他的方面来展示。那就是什么地方用什么的标准。为什么此处。那就要能够统一控制的就统一控制，能够接口控制的就接口控制。为了实现这样的效果，那就要接触这样的操作，既然接触这样的操作。那就要想一想，每一个部分从哪里来，怎么定义其中的内涵。此处代码的用处。在于看待资源的集中和利用的方式。结合的方式是什么呢，结合的方式是获取过来的参数给谁用。动态获取过来的参数，给指定的View来进行使用。

那就通过那里获取到数据，统一放到基类中来完成这样的操作。然后再子类中完成View的搭建。然后把参数和View结合起来，就可以搭建属于这个的药剂了。一个是用于获取参数的，一个是用来作为承接的。为什么两个会放在一起呢，为什么会作为一个参数来共同使用呢，那就是说，基于父类的替换的原则。在这里操作好之后，然后统一的进行管理，如果说统一的进行管理，统一的进行操作。那么这样的管理，是什么样的联动的方式。基于父类的操作，统一的操作的原则。

多种模式，为什么还要放到子类去选择呢。为什么要进行区分呢。在这个过程中，两者的显示不一样。机制是一样的。反应的机制是一样的。



#### 基础使用：

首先申明一个Group，然后设置这个Group。然后再把这个Group拿过去进行使用。设置监听设置模式。



#### 调用顺序：

单个模式：

点击：触发以下事件：直接调用自己的归属的ViewGroup，把事件直接传导到onSelectorClick里面去。

```
    @Override
    public void onClick(View v) {
        //deliver the click event to the SelectorGroup
        if (selectorGroup != null) {
            selectorGroup.onSelectorClick(this);
        }
    }
```

在非空判断之后。继续传导：

```
void onSelectorClick(Selector selector) {
    if (choiceMode != null) {
        choiceMode.onChoose(selector, this, onStateChangeListener);
    }
    //keep click selector in map
    selectorMap.put(selector.getGroupTag(), selector);
}
```

接收传导的对象，并进行消息的处理：

```
private class SingleAction implements ChoiceAction {

    @Override
    public void onChoose(Selector selector, SelectorGroup selectorGroup, StateListener stateListener) {
        cancelPreSelector(selector);
        setSelected(true, selector);
    }
}
```

进行公有方法的抽象：并对在页面里面还需要进行显示的效果

```
public void setSelected(boolean selected, Selector selector) {
    if (selector == null) {
        return;
    }
    if (selected) {
        //keep click selector in map
        selectorMap.put(selector.getGroupTag(), selector);
    }
    selector.setSelected(selected);
    if (onStateChangeListener != null) {
        onStateChangeListener.onStateChange(selector.getGroupTag(), selector.getSelectorTag(), selected);
    }
}
```

然后就完成了整个的调用的过程。

#### 要素论证：

在使用的过程中，其抽象的成分，以及框架的设定，就决定了这个的过程，是一个必然的过程。不然的话，一个就出一个group，必然是很混乱的。在这个方面，是可以形成一定的统一的共识的。然后再进行分散。这样子就可以保证一定的很好的灵活性，将不变的进行统一，将变的放到外面去。