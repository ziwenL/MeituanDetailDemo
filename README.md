# MeituanDetailDemo
<p>本项目是基于 <b>CoordinatorLayout + 自定义Behavior + 属性动画 </b>实现高仿美团外卖详情页 </p>

:star:<a href="https://blog.csdn.net/lzw398756924/article/details/110000692">博客地址</a>:star:
 
 ### 效果比对
![美团外卖详情页](https://img-blog.csdnimg.cn/20201123163648660.gif)
![仿美团外卖详情页](https://img-blog.csdnimg.cn/20201125093444306.gif)
<h6>(:star:左为美团外卖详情页实际效果，:star:右为该项目高仿效果)</h6>

### 具体实现
<ul>
<li>
<p>主要是通过自定义 <b>CoordinatorLayout.Behavior</b> 重写嵌套滚动相关回调方法，通过监听滚动偏移量的变化，动态调整 CoordinatorLayout 中子 View 的相关属性，达到样式跟随滚动变化的目的</p>
</li>
<li>
<p>通过自定义 <b>RecyclerView.ItemDecoration</b> 实现列表 Item 吸顶过渡替换效果</p>
</li>
<li>
<p>通过 <b>Scroller + Handler</b> 实现 View 自动滑动</p>
</li>
<li>
<p>通过 <b>属性动画</b> 实现 View 展开/收缩效果</p>
</li>
</ul>

### About Me
<ul>
<li>
<p>Email: ziwen.lan@foxmail.com</p>
</li>
</ul>
