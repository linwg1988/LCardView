# LCardView
卡片布局，可设置阴影颜色，透明度，圆角大小，阴影宽度。</br>

gradle中的引用：
~~~Java
gradle:
dependencies {
    implementation 'org.linwg1988:lcardview:1.0'
}
~~~

安卓原生的CardView没办法设置阴影的颜色，遇到UI中一些卡片布局阴影是可变的或者</br>

阴影透明度比较淡的情况下，咱又不想往包里面添加.9图片,就自己动手写了这个卡片布局。</br>

下面是布局里面可使用的属性：</br>

| xml属性名称 | 中文释义 |
| --- | --- |
| attr:leftShadowWidth | 左侧阴影宽度 |
| attr:topShadowHeight | 顶部阴影宽度 |
| attr:rightShadowWidth |右侧阴影宽度 |
| attr:bottomShadowHeight | 底部阴影宽度 |
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

在Java代码中也有对应相关的属性设置方法。

gif效果：</br>
 ![img](https://github.com/linwg1988/LCardView/blob/master/dem2.gif) 
