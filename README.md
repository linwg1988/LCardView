# LCardView
卡片布局，可设置阴影颜色，透明度，圆角大小，阴影宽度，阴影偏移量。</br>

### Java gradle dependencies：
~~~groovy
dependencies {
    implementation 'org.linwg1988:lcardview:1.5.4'
}
~~~

### Kotlin gradle dependencies：
~~~groovy
dependencies {
    implementation 'org.linwg1988:lcardview-kt:0.0.1'
}
~~~

现在卡片式的设计还是比较常见的，设计师们常常天马行空的设计出各种好看(emmm)的的卡片样式，但是安卓原生的CardView的局限性还是比较大的，比如没办法设置阴影的颜色，阴影的透明度等等等等；那既要满足设计师们的要求且又不往包里面添加.9图片使,那可以试试这款卡片布局。</br>

LCardView继承FrameLayout,使用方式与之并没有什么太大区别，下面一些动图将展示LCardView具有哪些功能:

#### CornerRadius
 * 可单独设置也可同时设置圆角半径

<img src="screenshot/cn.gif" width="32%" />


下面是布局里面可使用的属性：</br>

| xml属性名称 | 中文释义 |
| --- | --- |
| attr:shadowSize | 四边阴影宽度 |
| attr:shadowStartAlpha | 阴影颜色初始透明度 |
| attr:shadowFluidShape | 阴影流动形状（线性/吸附） |
| attr:shadowColor | 阴影颜色RGB值（透明度此处无效） |
| attr:cardBackgroundColor | 卡片背景色 |
| attr:cornerRadius | 阴影圆角半径 |
| attr:leftTopCornerRadius | 左上圆角半径 |
| attr:rightTopCornerRadius | 右上圆角半径 |
| attr:leftBottomCornerRadius | 左下圆角半径 |
| attr:rightBottomCornerRadius | 右下圆角半径 |
| attr:elevation | 卡片高度 |
| attr:elevationAffectShadowColor | 卡片高度是否影响阴影颜色 |
| attr:elevationAffectShadowSize | 卡片高度是否影响阴影宽度 |
| attr:leftOffset | 卡片左半区阴影偏移量 |
| attr:rightOffset | 卡片右半区阴影偏移量 |
| attr:topOffset | 卡片上半区阴影偏移量 |
| attr:bottomOffset | 卡片右半区阴影偏移量 |
| attr:fixedContentWidth | 控件宽度是否固定为内容宽度 |
| attr:fixedContentHeight | 控件高度是否固定为内容高度 |
| attr:paperSyncCorner | 同步卡片圆角与阴影圆角大小 |
| attr:paperCorner | 卡片圆角半径 |
| attr:linearBookEffect | 线性书本阴影效果 |
| attr:bookRadius | 线性书本阴影偏移角度 |
| attr:curveShadowEffect | 底部阴影扭曲效果 |
| attr:curvature | 底部阴影扭曲率 |
| attr:useShadowPool | 是否启用阴影缓存池 |
| attr:bindLifeCircle | 是否绑定生命周期 |

## Change Logs.

### Kotlin版本
### 0.0.1
 * 新增卡片圆角与阴影圆角是否同步的属性，自由度更高
 * 新增底部线性类似于书本阴影的效果（不要吐槽命名）
 * 新增底部类似扭曲阴影的效果（同上，不要吐槽）
 * 新增 **properties()** 方法，链式设置多个属性，单次修改阴影实例
 * 去除部分场景下的无效绘制，优化绘制速度
 * 新增卡片列表使用时阴影重复使用的支持，依赖于缓存池，且自动绑定生命周期

### Java版本
### 1.5.4
 * 新增属性 fixedContentWidth,fixedContentHeight.使用场景：卡片布局的父布局因为动画需要大小动态变化来对卡片布局进行隐藏或显示，
 * 若卡片尺寸是根据内部子控件大小来获得，卡片尺寸属性设置为 wrap_content,此时卡片的父布局动态修改尺寸时会导致卡片重新测量大小。该属
 * 性值为 true 时，父布局的大小不影响卡片的测量内部子控件的结果，故而也不会触发阴影重新创建以及内容的裁切大小。

### 1.5.2
 * 优化阴影创建的条件，只有在参数变化时才重新创建，修复View在没有测量完成时设置卡片属性导致的阴影异常,去除setShadowOffset()方法。
 
### 1.5.0
 * 此版本已经弃用四边阴影宽度分别设置的方法。
 * 对于阴影偏移的实现进行修改替换，分为上、下、左、右四个区域，
 * 偏移的数值为正，则从卡片中心向外偏移，
 * 偏移的数值为负，则从卡片中心向内偏移，
 * 你可以使用: **setShadowOffsetCenter(offset)** 进行以卡片中心的整体偏移。
 
### 1.4.2
 * 修复因为偏移属性的增加导致阴影透明度影响背景色的问题
 * 控件存在的局限性：四边阴影大小不一的时候无法设置圆角；
 * 同理设置圆角大小的时候四边的阴影大小会自动恢复成初始值
 
### 1.4.1
 * 修复Android P圆角失效的问题
 
### 1.4
 * 增加了X轴和Y轴偏移量属性,暂时只允许偏移至边缘位置。
 
### 1.2
 * 新增了一些xml初始化属性，让布局在xml中更加直观。

gif效果：</br>
 ![img](https://github.com/linwg1988/LCardView/blob/master/dem2.gif) 
