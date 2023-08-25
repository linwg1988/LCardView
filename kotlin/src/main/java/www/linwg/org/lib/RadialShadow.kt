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
    val mPath = Path()

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
        makePath()
    }

    fun reset() {
        offsetLeft = 0f
        offsetTop = 0f
        offsetRight = 0f
        offsetBottom = 0f
    }

    fun makePath() {
        mPath.reset()
        when (part) {
            IShadow.LEFT_TOP -> {
                if (offsetRight > 0 || offsetBottom > 0) {
                    val scaleX = (frame.width() - offsetRight) / frame.width()
                    val scaleY = (frame.height() - offsetBottom) / frame.height()
                    matrix.postScale(scaleX, scaleY, offsetRight, offsetBottom)
                    shader!!.setLocalMatrix(matrix)
                }

                mPath.moveTo(frame.left, frame.centerY() - offsetBottom)
                mPath.lineTo(frame.centerX() - offsetRight, frame.centerY() - offsetBottom)
                mPath.lineTo(frame.centerX() - offsetRight, frame.top)
                mPath.lineTo(frame.left, frame.top)
                mPath.lineTo(frame.left, frame.centerY() - offsetBottom)
                mPath.close()
            }
            IShadow.RIGHT_TOP -> {
                mPath.moveTo(frame.centerX() + offsetLeft, frame.top)
                mPath.lineTo(frame.centerX() + offsetLeft, frame.centerY() - offsetBottom)
                mPath.lineTo(frame.right, frame.centerY() - offsetBottom)
                mPath.lineTo(frame.right, frame.top)
                mPath.lineTo(frame.centerX() + offsetLeft, frame.top)
                mPath.close()
            }
            IShadow.RIGHT_BOTTOM -> {
                mPath.moveTo(frame.right, frame.centerY() + offsetTop)
                mPath.lineTo(frame.centerX() + offsetLeft, frame.centerY() + offsetTop)
                mPath.lineTo(frame.centerX() + offsetLeft, frame.bottom)
                mPath.lineTo(frame.right, frame.bottom)
                mPath.lineTo(frame.right, frame.centerY() + offsetTop)
                mPath.close()
            }
            IShadow.LEFT_BOTTOM -> {
                mPath.moveTo(frame.centerX() - offsetRight, frame.bottom)
                mPath.lineTo(frame.centerX() - offsetRight, frame.centerY() + offsetTop)
                mPath.lineTo(frame.left, frame.centerY() + offsetTop)
                mPath.lineTo(frame.left, frame.bottom)
                mPath.lineTo(frame.centerX() - offsetRight, frame.bottom)
                mPath.close()
            }
        }
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
        paint.shader = shader
        canvas.drawPath(mPath, paint)
    }

    override fun recreateShader() {
        shader = createShader(percentChange = false, assign = true)
    }

    override fun onDestroy() {
        linearShape = null
        adsorptionShape = null
        shader = null
    }

    var offsetRight = 0f
    var offsetLeft = 0f
    var offsetTop = 0f
    var offsetBottom = 0f
}