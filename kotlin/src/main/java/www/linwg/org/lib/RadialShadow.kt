package www.linwg.org.lib

import android.graphics.*
import android.os.Build
import android.util.Log

class RadialShadow(private val colors: IntArray, private val part: Int) : BaseShadow() {
    private val percent = 0.33f
    private val percents = floatArrayOf(0f, 0.33f, 0.66f, 1f)
    private var linearShape: RadialGradient? = null
    private var adsorptionShape: RadialGradient? = null
    private var shader: RadialGradient? = null
    private val center = PointF()
    private var shaderCornerRadius: Int = 0
    private val matrix = Matrix()
    var cornerRadius: Int = 0
    private var mode: Int = LCardView.ADSORPTION

    private fun createShader(centerX: Float = center.x, centerY: Float = center.y, percentChange: Boolean = true, assign: Boolean = true): RadialGradient {
        val colors = if (alphaHalf) {
            halfAlpha(colors)
        } else {
            this.colors
        }
        if (percentChange) {
            percents[0] = cornerRadius / shaderCornerRadius.toFloat()
            percents[1] = (1 - percents[0]) * percent + percents[0]
            percents[2] = (1 - percents[1]) / 2 + percents[1]
        }
        val shader = if (!useShadowPool) {
            newShader(centerX, centerY, colors)
        } else {
            val key = ShadowPool.getRadialKey(frame.width().toInt(), frame.height().toInt(), mode, part, cornerRadius, colors[0])
            val radialGradient = ShadowPool.get(key) as RadialGradient?
                    ?: newShader(centerX, centerY, colors)
            ShadowPool.put(key, radialGradient)
            radialGradient
        }

        if (assign) {
            if (mode == LCardView.ADSORPTION) {
                adsorptionShape = shader
                linearShape = null
            } else {
                linearShape = shader
                adsorptionShape = null
            }
        }
        return shader
    }

    private fun newShader(centerX: Float, centerY: Float, colors: IntArray): RadialGradient {
        Log.i("LCardView", "create RadialGradient at ${System.currentTimeMillis()}")
        return RadialGradient(centerX, centerY, shaderCornerRadius.toFloat(), colors, percents, Shader.TileMode.CLAMP)
    }

    fun onFrameChange(centerX: Float, centerY: Float, r: Int) {
        if (shaderCornerRadius != r || shader == null || colorChange) {
            colorChange = false
            //means we need create new shader
            shaderCornerRadius = r
            center.x = centerX
            center.y = centerY
            shader = createShader()
            return
        }
        //check center point change
        matrix.setTranslate(centerX - center.x, centerY - center.y)
        shader!!.setLocalMatrix(matrix)
    }

    override fun onShapeModeChange(mode: Int) {
        this.mode = mode
        shader = if (mode == LCardView.ADSORPTION) {
            if (adsorptionShape == null) {
                adsorptionShape = createShader(percentChange = false, assign = false)
            }
            adsorptionShape
        } else {
            if (linearShape == null) {
                linearShape = createShader(percentChange = false, assign = false)
            }
            linearShape
        }
    }

    override fun setMode(mode: Int) {
        this.mode = mode
    }

    override fun draw(canvas: Canvas, path: Path, paint: Paint) {
        if (frame.isEmpty) {
            return
        }
        canvas.save()
        when (part) {
            IShadow.LEFT_TOP -> canvas.clipRect(frame.left, frame.top, frame.centerX(), frame.centerY())
            IShadow.RIGHT_TOP -> canvas.clipRect(frame.centerX(), frame.top, frame.right, frame.centerY())
            IShadow.RIGHT_BOTTOM -> canvas.clipRect(frame.centerX(), frame.centerY(), frame.right, frame.bottom)
            IShadow.LEFT_BOTTOM -> canvas.clipRect(frame.left, frame.centerY(), frame.centerX(), frame.bottom)
        }
        path.reset()
        path.addCircle(frame.centerX(), frame.centerY(), cornerRadius.toFloat(), Path.Direction.CCW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(path)
        } else {
            @Suppress("DEPRECATION")
            canvas.clipPath(path, Region.Op.DIFFERENCE)
        }
        paint.shader = shader
        canvas.drawCircle(frame.centerX(), frame.centerY(), shaderCornerRadius.toFloat(), paint)
        canvas.restore()
    }

    override fun recreateShader() {
        shader = createShader(percentChange = false, assign = true)
    }

    override fun onDestroy() {
        linearShape = null
        adsorptionShape = null
        shader = null
    }
}