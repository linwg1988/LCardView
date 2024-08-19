package www.linwg.org.app

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DemoViewModel:ViewModel() {
    val shadowFluidShape = MutableLiveData<Int>()
    val shadowColor = MutableLiveData<Int>()
    val shadowStartAlpha = MutableLiveData<Int>()
    val shadowSize = MutableLiveData<Int>()
    val leftWidthDecrement = MutableLiveData<Float>()
    val topWidthDecrement = MutableLiveData<Float>()
    val rightWidthDecrement = MutableLiveData<Float>()
    val bottomWidthDecrement = MutableLiveData<Float>()
    val leftOffset = MutableLiveData<Float>()
    val topOffset = MutableLiveData<Float>()
    val rightOffset = MutableLiveData<Float>()
    val bottomOffset = MutableLiveData<Float>()
    val shadowOffsetCenter = MutableLiveData<Float>()
    val cornerRadius = MutableLiveData<Float>()
    val paperCorner = MutableLiveData<Float>()
    val leftTopCornerRadius = MutableLiveData<Float>()
    val rightTopCornerRadius = MutableLiveData<Float>()
    val rightBottomCornerRadius = MutableLiveData<Float>()
    val leftBottomCornerRadius = MutableLiveData<Float>()
    val paperSyncCorner = MutableLiveData<Boolean>()
    val curveShadowEffect = MutableLiveData<Boolean>()
    val linearBookEffect = MutableLiveData<Boolean>()
    val gradientSizeFollowView = MutableLiveData<Boolean>()
    val curvature = MutableLiveData<Float>()
    val strokeWidth = MutableLiveData<Int>()
    val bookRadius = MutableLiveData<Float>()
    val gradientDirection = MutableLiveData<Int>()
    val cardBackgroundDrawableRes = MutableLiveData<Int>()
    val cardBackground = MutableLiveData<Drawable?>()
    val gradientColors = MutableLiveData<IntArray>()
    val leftDe = MutableLiveData<IntArray>()

}