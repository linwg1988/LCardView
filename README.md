# LCardView
卡片布局，可设置阴影颜色，透明度，圆角大小，阴影宽度，阴影偏移量。</br>

gradle中的引用：
~~~
gradle:
dependencies {
    implementation 'org.linwg1988:lcardview:1.5.2'
}
~~~

安卓原生的CardView没办法设置阴影的颜色，遇到UI中一些卡片布局阴影是可变的或者</br>

阴影透明度比较淡的情况下，咱又不想往包里面添加.9图片,就自己动手写了这个卡片布局。</br>

**1.2.版本说明此版本新增了一些xml初始化属性，让布局在xml中更加直观。**</br>

**1.4.版本说明:增加了X轴和Y轴偏移量属性,暂时只允许偏移至边缘位置。**</br>

**1.4.1修复Android P圆角失效的问题**</br>

**1.4.2修复因为偏移属性的增加导致阴影透明度影响背景色的问题**</br>

**控件存在的局限性：四边阴影大小不一的时候无法设置圆角；同理设置圆角大小**</br>

**的时候四边的阴影大小会自动恢复成初始值**</br>

**1.5.0版本说明**</br>

**此版本已经弃用四边阴影宽度分别设置的方法。**</br>

**对于阴影偏移的实现进行修改替换，分为上、下、左、右四个区域，**</br>

**偏移的数值为正，则从卡片中心向外偏移，**</br>

**偏移的数值为负，则从卡片中心向内偏移，**</br>

**你可以使用:**</br>
~~~
setShadowOffsetCenter(int offset)
~~~
**进行以卡片中心的整体偏移。**</br>


**1.5.2优化阴影创建的条件，只有在参数变化时才重新创建，修复View在没有测量完成时设置卡片属性导致的阴影异常,**</br>
**去除setShadowOffset()方法。**</br>

下面是布局里面可使用的属性：</br>

| xml属性名称 | 中文释义 |
| --- | --- |
| ~~attr:leftShadowWidth~~ | ~~左侧阴影宽度~~ |
| ~~attr:topShadowHeight~~ | ~~顶部阴影宽度~~ |
| ~~attr:rightShadowWidth~~ |~~右侧阴影宽度~~ |
| ~~attr:bottomShadowHeight~~ | ~~底部阴影宽度~~ |
| **attr:shadowSize** | **四边阴影宽度** |
| **attr:shadowStartAlpha** | **阴影颜色初始透明度** |
| **attr:shadowFluidShape** | **阴影流动形状（线性/吸附）** |
| attr:shadowColor | 阴影颜色RGB值（透明度此处无效） |
| attr:cardBackgroundColor | 卡片背景色 |
| attr:cornerRadius | 卡片四个角的圆角半径 |
| attr:leftTopCornerRadius | 左上圆角半径 |
| attr:rightTopCornerRadius | 右上圆角半径 |
| attr:leftBottomCornerRadius | 左下圆角半径 |
| attr:rightBottomCornerRadius | 右下圆角半径 |
| attr:elevation | 卡片高度 |
| attr:elevationAffectShadowColor | 卡片高度是否影响阴影颜色 |
| attr:elevationAffectShadowSize | 卡片高度是否影响阴影宽度 |
| ~~attr:xOffset~~ | ~~卡片X轴偏移量(已弃用)~~ |
| ~~attr:yOffset~~ | ~~卡片Y轴偏移量(已弃用)~~ |
| **attr:leftOffset** | **卡片左半区阴影偏移量** |
| **attr:rightOffset** | **卡片右半区阴影偏移量** |
| **attr:topOffset** | **卡片上半区阴影偏移量** |
| **attr:bottomOffset** | **卡片右半区阴影偏移量** |

在Java代码中也有对应相关的属性设置方法。

gif效果：</br>
 ![img](https://github.com/linwg1988/LCardView/blob/master/dem2.gif) 
