package www.linwg.org.lib

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

internal class ShadowManager(colors: IntArray, percent: Float = 0.33f) {
    companion object {
        var hasReadLifeCircleSupport = false
        var lifeCirCleSupport = false
    }

    init {
        if (!hasReadLifeCircleSupport) {
            hasReadLifeCircleSupport = true
            try {
                Class.forName("androidx.lifecycle.Lifecycle")
                Log.i("LCardView", "Lifecycle support")
                lifeCirCleSupport = true
            } catch (e: ClassNotFoundException) {
                lifeCirCleSupport = false
                Log.w("LCardView", "Lifecycle not support")
            }
        }
    }

    /**
     * Corner shadows will be draw on 4 corner.
     */
    private val ltShader: RadialShadow = RadialShadow(colors, IShadow.LEFT_TOP)
    private val lbShader: RadialShadow = RadialShadow(colors, IShadow.LEFT_BOTTOM)
    private val rbShader: RadialShadow = RadialShadow(colors, IShadow.RIGHT_BOTTOM)
    private val rtShader: RadialShadow = RadialShadow(colors, IShadow.RIGHT_TOP)

    /**
     * Edge shadows will be draw on 4 edge.
     */
    private val tShadow: LinearShadow = LinearShadow(colors, percent, IShadow.TOP)
    private val rShadow: LinearShadow = LinearShadow(colors, percent, IShadow.RIGHT)
    private val bShadow: LinearShadow = LinearShadow(colors, percent, IShadow.BOTTOM)
    private val lShadow: LinearShadow = LinearShadow(colors, percent, IShadow.LEFT)
    val shaderArray =
        arrayOf(ltShader, tShadow, rtShader, rShadow, rbShader, bShadow, lbShader, lShadow)
    var useShadowPool: Boolean = false
        set(value) {
            field = value
            shaderArray.forEach {
                it.useShadowPool = value
            }
        }
    var linearBookEffect: Boolean = false
        set(value) {
            field = value
            bShadow.linearBookEffect = value
            curveShadowEffect = false
        }
    var curveShadowEffect: Boolean = false
        set(value) {
            field = value
            bShadow.curveShadowEffect = value
            if (value) {
                fluidShape = LCardView.LINEAR
                shaderArray.forEach {
                    if (it != bShadow) {
                        it.markColorAlphaHalf()
                    }
                }
            } else {
                shaderArray.forEach {
                    if (it != bShadow) {
                        it.finishColorAlphaHalf()
                    }
                }
            }
        }

    var fluidShape: Int = LCardView.ADSORPTION
        set(value) {
            field = value
            shaderArray.forEach {
                it.setMode(value)
            }
        }

    fun measureShadowPath(path: Path) {
        path.reset()
        path.moveTo(lShadow.frame.right, lShadow.frame.top)
        path.arcTo(
            RectF(
                lShadow.frame.right,
                tShadow.frame.bottom,
                lShadow.frame.right + ltShader.cornerRadius * 2,
                tShadow.frame.bottom + ltShader.cornerRadius * 2
            ), 180f, 90f
        )
        path.lineTo(tShadow.frame.right, tShadow.frame.bottom)
        path.arcTo(
            RectF(
                rShadow.frame.left - rtShader.cornerRadius * 2,
                rShadow.frame.top - rtShader.cornerRadius,
                rShadow.frame.left,
                rShadow.frame.top + rtShader.cornerRadius
            ), 270f, 90f
        )
        path.lineTo(rShadow.frame.left, rShadow.frame.bottom)
        path.arcTo(
            RectF(
                rShadow.frame.left - rbShader.cornerRadius * 2,
                rShadow.frame.bottom - rbShader.cornerRadius,
                rShadow.frame.left,
                rShadow.frame.bottom + rbShader.cornerRadius
            ), 0f, 90f
        )
        path.lineTo(bShadow.frame.left, bShadow.frame.top)
        path.arcTo(
            RectF(
                lShadow.frame.right,
                lShadow.frame.bottom - lbShader.cornerRadius,
                lShadow.frame.right + lbShader.cornerRadius * 2,
                lShadow.frame.bottom + lbShader.cornerRadius
            ), 90f, 90f
        )
        path.close()
    }

    fun needDrawOffsetShadowColor(cardView: LCardView): Boolean {
        return lShadow.frame.right < cardView.paddingLeft || tShadow.frame.bottom < cardView.paddingTop
                || rShadow.frame.left > cardView.paddingRight
                || bShadow.frame.top > cardView.paddingBottom
    }

    fun measureContentPath(
        cardView: LCardView,
        paperSyncCorner: Boolean,
        paperCorner: Int,
        path: Path,
        strokePath: Path,
        strokeWidth: Int
    ) {
        val startX = cardView.paddingLeft.toFloat()
        val startY = cardView.paddingTop.toFloat()
        val stopX = cardView.paddingRight.toFloat()
        val stopY = cardView.paddingBottom.toFloat()
        val viewHeight = cardView.viewHeight
        val viewWidth = cardView.viewWidth
        path.reset()
        strokePath.reset()
        if (paperSyncCorner) {
            path.moveTo(startX, startY + ltShader.cornerRadius)
            path.arcTo(
                RectF(
                    startX,
                    startY,
                    ltShader.cornerRadius * 2 + startX,
                    startY + ltShader.cornerRadius * 2
                ), 180f, 90f
            )
            path.lineTo(viewWidth - stopX - rtShader.cornerRadius, startY)
            path.arcTo(
                RectF(
                    viewWidth - stopX - rtShader.cornerRadius * 2,
                    startY,
                    viewWidth - stopX,
                    startY + rtShader.cornerRadius * 2
                ), 270f, 90f
            )
            path.lineTo(viewWidth - stopX, viewHeight - stopY - rbShader.cornerRadius)
            path.arcTo(
                RectF(
                    viewWidth - stopX - rbShader.cornerRadius * 2,
                    viewHeight - stopY - rbShader.cornerRadius * 2,
                    viewWidth - stopX,
                    viewHeight - stopY
                ), 0f, 90f
            )
            path.lineTo(startX + lbShader.cornerRadius, viewHeight - stopY)
            path.arcTo(
                RectF(
                    startX,
                    viewHeight - stopY - lbShader.cornerRadius * 2,
                    startX + lbShader.cornerRadius * 2,
                    viewHeight - stopY
                ), 90f, 90f
            )

            val offset = strokeWidth / 2f
            if (strokeWidth > 0) {
                strokePath.moveTo(startX + offset, startY + ltShader.cornerRadius)
                strokePath.arcTo(
                    RectF(
                        startX + offset,
                        startY + offset,
                        ltShader.cornerRadius * 2 + startX - offset,
                        startY + ltShader.cornerRadius * 2 - offset
                    ), 180f, 90f
                )
                strokePath.lineTo(viewWidth - stopX - rtShader.cornerRadius, startY + offset)
                strokePath.arcTo(
                    RectF(
                        viewWidth - stopX - rtShader.cornerRadius * 2 + offset,
                        startY + offset,
                        viewWidth - stopX - offset,
                        startY + rtShader.cornerRadius * 2 - offset
                    ), 270f, 90f
                )
                strokePath.lineTo(
                    viewWidth - stopX - offset,
                    viewHeight - stopY - rbShader.cornerRadius
                )
                strokePath.arcTo(
                    RectF(
                        viewWidth - stopX - rbShader.cornerRadius * 2 + offset,
                        viewHeight - stopY - rbShader.cornerRadius * 2 + offset,
                        viewWidth - stopX - offset,
                        viewHeight - stopY - offset
                    ), 0f, 90f
                )
                strokePath.lineTo(startX + lbShader.cornerRadius, viewHeight - stopY - offset)
                strokePath.arcTo(
                    RectF(
                        startX + offset,
                        viewHeight - stopY - lbShader.cornerRadius * 2 + offset,
                        startX + lbShader.cornerRadius * 2 - offset,
                        viewHeight - stopY - offset
                    ), 90f, 90f
                )
                strokePath.close()
            }
        } else {
            if (paperCorner == 0) {
                path.moveTo(startX, startY)
                path.lineTo(viewWidth - stopX, startY)
                path.lineTo(viewWidth - stopX, viewHeight - stopY)
                path.lineTo(startX, viewHeight - stopY)
            } else {
                path.moveTo(startX, startY + paperCorner)
                path.arcTo(RectF(startX, startY, paperCorner * 2 + startX, startY + paperCorner * 2), 180f, 90f)
                path.lineTo(viewWidth - stopX - paperCorner, startY)
                path.arcTo(RectF(viewWidth - stopX - paperCorner * 2, startY, viewWidth - stopX, startY + paperCorner * 2), 270f, 90f)
                path.lineTo(viewWidth - stopX, viewHeight - stopY - paperCorner)
                path.arcTo(RectF(viewWidth - stopX - paperCorner * 2, viewHeight - stopY - paperCorner * 2, viewWidth - stopX, viewHeight - stopY), 0f, 90f)
                path.lineTo(startX + paperCorner, viewHeight - stopY)
                path.arcTo(RectF(startX, viewHeight - stopY - paperCorner * 2, startX + paperCorner * 2, viewHeight - stopY), 90f, 90f)
            }
        }
        path.close()
    }

    fun createDrawables(cardView: LCardView, shadowSize: Int) {
        val paddingLeft = cardView.paddingLeft
        val paddingRight = cardView.paddingRight
        val paddingTop = cardView.paddingTop
        val paddingBottom = cardView.paddingBottom
        val viewHeight = cardView.viewHeight
        val viewWidth = cardView.viewWidth
        val effectLeftOffset = cardView.effectLeftOffset
        val effectTopOffset = cardView.effectTopOffset
        val effectRightOffset = cardView.effectRightOffset
        val effectBottomOffset = cardView.effectBottomOffset
        val ltRadius = shadowSize + ltShader.cornerRadius
        if (ltRadius == 0) {
            ltShader.frame.setEmpty()
        } else {
            val centerX: Float = if (paddingLeft > 0) ltRadius.toFloat() else ltRadius - effectLeftOffset.toFloat()
            val centerY: Float = if (paddingTop > 0) ltRadius.toFloat() else ltRadius - effectTopOffset.toFloat()
            ltShader.frame[centerX - ltRadius, centerY - ltRadius, centerX + ltRadius] = centerY + ltRadius
            ltShader.onFrameChange(centerX, centerY, ltRadius)
        }
        val rtRadius = shadowSize + rtShader.cornerRadius
        if (rtRadius == 0) {
            rtShader.frame.setEmpty()
        } else {
            val centerX: Float = if (paddingRight > 0) viewWidth - rtRadius.toFloat() else viewWidth - rtRadius + effectRightOffset.toFloat()
            val centerY: Float = if (paddingTop > 0) rtRadius.toFloat() else rtRadius - effectTopOffset.toFloat()
            rtShader.frame[centerX - rtRadius, centerY - rtRadius, centerX + rtRadius] = centerY + rtRadius
            rtShader.onFrameChange(centerX, centerY, rtRadius)
        }
        val rbRadius = shadowSize + rbShader.cornerRadius
        if (rbRadius == 0) {
            rbShader.frame.setEmpty()
        } else {
            val centerX: Float = if (paddingRight > 0) viewWidth - rbRadius.toFloat() else viewWidth - rbRadius + effectRightOffset.toFloat()
            val centerY: Float = if (paddingBottom > 0) viewHeight - rbRadius.toFloat() else viewHeight - rbRadius + effectBottomOffset.toFloat()
            rbShader.frame[centerX - rbRadius, centerY - rbRadius, centerX + rbRadius] = centerY + rbRadius
            rbShader.onFrameChange(centerX, centerY, rbRadius)
        }
        val lbRadius = shadowSize + lbShader.cornerRadius
        if (lbRadius == 0) {
            lbShader.frame.setEmpty()
        } else {
            val centerX: Float = if (paddingLeft > 0) lbRadius.toFloat() else lbRadius - effectLeftOffset.toFloat()
            val centerY: Float = if (paddingBottom > 0) viewHeight - lbRadius.toFloat() else viewHeight - lbRadius + effectBottomOffset.toFloat()
            lbShader.frame[centerX - lbRadius, centerY - lbRadius, centerX + lbRadius] = centerY + lbRadius
            lbShader.onFrameChange(centerX, centerY, lbRadius)
        }
        var left = if (paddingLeft > 0) ltRadius.toFloat() else ltRadius - effectLeftOffset.toFloat()
        var right = if (paddingRight > 0) (viewWidth - rtRadius).toFloat() else viewWidth - rtRadius + effectRightOffset.toFloat()
        var top: Float = if (paddingTop > 0) 0f else -effectTopOffset.toFloat()
        var bottom = top + shadowSize
        tShadow.frame[left, top, right] = bottom
        tShadow.onFrameChange()
        right =
            if (paddingRight > 0) viewWidth.toFloat() else viewWidth + effectRightOffset.toFloat()
        left = right - shadowSize
        top = if (paddingTop > 0) rtRadius.toFloat() else rtRadius - effectTopOffset.toFloat()
        bottom =
            if (paddingBottom > 0) (viewHeight - rbRadius).toFloat() else viewHeight - rbRadius + effectBottomOffset.toFloat()
        rShadow.frame[left, top, right] = bottom
        rShadow.onFrameChange()
        left = if (paddingLeft > 0) lbRadius.toFloat() else lbRadius - effectLeftOffset.toFloat()
        right =
            if (paddingRight > 0) (viewWidth - rbRadius).toFloat() else viewWidth - rbRadius + effectRightOffset.toFloat()
        top =
            if (paddingBottom > 0) (viewHeight - shadowSize).toFloat() else viewHeight - shadowSize + effectBottomOffset.toFloat()
        bottom = top + shadowSize
        bShadow.frame[left, top, right] = bottom
        bShadow.onFrameChange()
        right =
            if (paddingLeft > 0) shadowSize.toFloat() else shadowSize.toFloat() - effectLeftOffset
        left = right - shadowSize
        top = if (paddingTop > 0) ltRadius.toFloat() else ltRadius - effectTopOffset.toFloat()
        bottom =
            if (paddingBottom > 0) (viewHeight - lbRadius).toFloat() else viewHeight - lbRadius + effectBottomOffset.toFloat()
        lShadow.frame[left, top, right] = bottom
        lShadow.onFrameChange()

        ltShader.reset()
        lbShader.reset()
        rtShader.reset()
        rbShader.reset()
        var ltChange = false
        var lbChange = false
        var rtChange = false
        var rbChange = false
        if (paddingLeft < lShadow.frame.width()) {
            if (tShadow.frame.width() <= 0f) {
                ltShader.offsetRight = lShadow.frame.width() - paddingLeft
                ltChange = true
            }
            if (bShadow.frame.width() <= 0f) {
                lbShader.offsetRight = lShadow.frame.width() - paddingLeft
                lbChange = true
            }
        }
        if (paddingTop < tShadow.frame.height()) {
            if (lShadow.frame.height() <= 0f) {
                ltShader.offsetBottom = tShadow.frame.height() - paddingTop
                ltChange = true
            }
            if (rShadow.frame.height() <= 0f) {
                rtShader.offsetBottom = tShadow.frame.height() - paddingTop
                rtChange = true
            }
        }
        if (paddingRight < rShadow.frame.width()) {
            if (tShadow.frame.width() <= 0f) {
                rtShader.offsetLeft = rShadow.frame.width() - paddingRight
                rtChange = true
            }
            if (bShadow.frame.width() <= 0f) {
                rbShader.offsetLeft = rShadow.frame.width() - paddingRight
                rbChange = true
            }
        }
        if (paddingBottom < bShadow.frame.height()) {
            if (lShadow.frame.height() <= 0f) {
                lbShader.offsetTop = bShadow.frame.height() - paddingBottom
                lbChange = true
            }
            if (rShadow.frame.height() <= 0f) {
                rbShader.offsetTop = bShadow.frame.height() - paddingBottom
                rbChange = true
            }
        }
        if (lbChange) {
            lbShader.makePath()
        }
        if (rbChange) {
            rbShader.makePath()
        }
        if (ltChange) {
            ltShader.makePath()
        }
        if (rtChange) {
            rtShader.makePath()
        }
    }

    fun setCornerRadius(value: Int, part: Int) {
        when (part) {
            IShadow.LEFT_TOP -> ltShader.cornerRadius = value
            IShadow.RIGHT_TOP -> rtShader.cornerRadius = value
            IShadow.RIGHT_BOTTOM -> rbShader.cornerRadius = value
            IShadow.LEFT_BOTTOM -> lbShader.cornerRadius = value
        }
    }

    fun setCurvature(curvature: Float) {
        bShadow.curvature = curvature
    }

    fun onDraw(canvas: Canvas, mPath: Path, paint: Paint) {
        shaderArray.forEach {
            it.draw(canvas, mPath, paint)
        }
    }

    fun markColorChange() {
        shaderArray.forEach {
            it.markColorChange()
        }
    }

    fun onShapeModeChange(shape: Int): Boolean {
        if (curveShadowEffect) {
            //curve effect can only play well on linear shadow
            return false
        }
        if (shape != LCardView.ADSORPTION && shape != LCardView.LINEAR) {
            return false
        }
        if (fluidShape == shape) {
            return false
        }
        fluidShape = shape
        shaderArray.forEach {
            it.onShapeModeChange(fluidShape)
        }
        return true
    }

    fun setBookRadius(r: Int) {
        bShadow.bookRadius = r.toFloat()
    }

    fun onDestroy() {
        shaderArray.forEach {
            it.onDestroy()
        }
    }

    fun onAttachedToWindow() {
        shaderArray.forEach {
            it.onAttachedToWindow()
        }
    }

    fun onDetachedFromWindow() {
        shaderArray.forEach {
            it.onDetachedFromWindow()
        }
    }

    fun bindLifeCircle(context: Context): Boolean {
        if (context !is Activity) {
            Log.w("LCardView", "非Activity无法绑定生命周期")
            return false
        }
        if (lifeCirCleSupport && context is LifecycleOwner) {
            context.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        source.lifecycle.removeObserver(this)
                        onDestroy()
                    }
                }
            })
            return true
        }

        val applicationContext = context.applicationContext
        if (applicationContext is Application) {
            applicationContext.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityPaused(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    if (activity == context) {
                        applicationContext.unregisterActivityLifecycleCallbacks(this)
                        onDestroy()
                    }
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                }

            })
            return true
        }
        return false
    }
}