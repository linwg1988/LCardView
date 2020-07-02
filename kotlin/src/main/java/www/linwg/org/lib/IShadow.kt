package www.linwg.org.lib

import android.graphics.*

interface IShadow {

    companion object {
        const val TOP = 0
        const val RIGHT = 1
        const val BOTTOM = 2
        const val LEFT = 3
        const val LEFT_TOP = 4
        const val RIGHT_TOP = 5
        const val RIGHT_BOTTOM = 6
        const val LEFT_BOTTOM = 7
    }

    fun markColorChange()

    fun onShapeModeChange(mode: Int)

    fun setMode(mode: Int)

    fun draw(canvas: Canvas, path: Path, paint: Paint)

    fun onAttachedToWindow()

    fun onDetachedFromWindow()

    fun onDestroy()
}

abstract class BaseShadow() : IShadow {
    val frame: RectF = RectF()
    var colorChange = false
    var alphaHalf = false
    var useShadowPool = false
    private var fadeColors: IntArray? = null
    private var hasBeenDetached = false

    override fun markColorChange() {
        //mark shader should recreate when onFrameChange call
        colorChange = true
    }

    fun markColorAlphaHalf() {
        colorChange = true
        alphaHalf = true
    }

    fun finishColorAlphaHalf(){
        colorChange = true
        alphaHalf = false
    }

    fun halfAlpha(colors: IntArray): IntArray {
        if (fadeColors == null) {
            fadeColors = IntArray(colors.size) {
                newColor(it, colors)
            }
        } else {
            for (i in colors.indices) {
                fadeColors!![i] = newColor(i, colors)
            }
        }
        return fadeColors!!
    }

    private fun newColor(index: Int, colors: IntArray): Int {
        val alpha = Color.alpha(colors[index]) / 2
        val r = Color.red(colors[index])
        val g = Color.green(colors[index])
        val b = Color.blue(colors[index])
        return Color.argb(alpha, r, g, b)
    }

    override fun onAttachedToWindow() {
        if (hasBeenDetached) {
            hasBeenDetached = false
            recreateShader()
        }
    }

    abstract fun recreateShader()

    override fun onDetachedFromWindow() {
        hasBeenDetached = true
        onDestroy()
    }
}