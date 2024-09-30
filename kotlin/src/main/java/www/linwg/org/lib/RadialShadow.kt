package www.linwg.org.lib

import android.graphics.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

class RadialShadow(private val colors: IntArray, private val part: Int) : BaseShadow() {
    private val percent = 0.33f
    private val percents = floatArrayOf(0f, 0.33f, 0.66f, 1f)
    private var linearShape: RadialGradient? = null
    private var adsorptionShape: RadialGradient? = null
    private var shader: RadialGradient? = null
    private val center = PointF()
    private var shaderCornerRadius: Float = 0f
    private val matrix = Matrix()
    var cornerRadius: Float = 0f
    private var mode: Int = LCardView.ADSORPTION
    val mPath = Path()
    private var decrementChange = false
    private var widthDecrement = 0f
    private var heightDecrement = 0f
    private var meshBitmap: Bitmap? = null
    private val paint: Paint by lazy { Paint() }

    fun setDecrement(x: Float, y: Float) {
        if (widthDecrement != x) {
            widthDecrement = x
            decrementChange = true
        }
        if (heightDecrement != y) {
            heightDecrement = y
            decrementChange = true
        }
    }

    private fun createShader(
        centerX: Float = center.x,
        centerY: Float = center.y,
        percentChange: Boolean = true,
        assign: Boolean = true
    ): RadialGradient {
        val colors = if (alphaHalf) {
            halfAlpha(colors)
        } else {
            this.colors
        }
        if (percentChange) {
            percents[0] = cornerRadius / shaderCornerRadius
            percents[1] = (1 - percents[0]) * percent + percents[0]
            percents[2] = (1 - percents[1]) / 2 + percents[1]
        }
        val shader = if (!useShadowPool) {
            newShader(centerX, centerY, colors)
        } else {
            val key = ShadowPool.getRadialKey(
                frame.width().toInt(), frame.height().toInt(), mode, part, cornerRadius, colors[0]
            )
            val radialGradient =
                ShadowPool.get(key) as RadialGradient? ?: newShader(centerX, centerY, colors)
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

    private fun makeMeshRadialGradient() {
        meshBitmap = if (!useShadowPool) {
            createMeshBitmap()
        } else {
            var mesh = ShadowPool.getMeshRadial(
                frame.width().toInt(),
                frame.height().toInt(),
                widthDecrement,
                heightDecrement,
                part,
                colors[0]
            )
            if (mesh == null) {
                mesh = createMeshBitmap()
            }
            ShadowPool.putMeshRadial(
                frame.width().toInt(),
                frame.height().toInt(),
                widthDecrement,
                heightDecrement,
                part,
                colors[0],
                mesh
            )
            mesh
        }
    }

    private fun createMeshBitmap(): Bitmap {
        val vertWidthSize = (frame.width() / 10).roundToInt()
        val vertHeightSize = (frame.height() / 10).roundToInt()
        val count = (vertWidthSize + 1) * (vertHeightSize + 1)
        val verts = FloatArray(count * 2)
        val w = frame.width() / 2
        val h = frame.height() / 2
        var index = 0
        val scaleY = (shaderCornerRadius - heightDecrement) / shaderCornerRadius
        val scaleX = (shaderCornerRadius - widthDecrement) / shaderCornerRadius
        for (y in 0..vertHeightSize) {
            for (x in 0..vertWidthSize) {
                val originX: Float
                val originY: Float
                when (part) {
                    IShadow.LEFT_TOP -> {
                        originX = w - (w / vertWidthSize) * x
                        originY = h - (h / vertHeightSize) * y
                    }
                    IShadow.RIGHT_TOP -> {
                        originX = (w / vertWidthSize) * x
                        originY = h - (h / vertHeightSize) * y
                    }
                    IShadow.RIGHT_BOTTOM -> {
                        originX = (w / vertWidthSize) * x
                        originY = (h / vertHeightSize) * y
                    }
                    else -> {
                        originX = w - (w / vertWidthSize) * x
                        originY = (h / vertHeightSize) * y
                    }
                }
                val pointOnCircle = getPointOnCircle(originX, originY, shaderCornerRadius)
                val pointOnEllipse = getPointOnEllipse(originX, originY, w * scaleX, h * scaleY)

                val lengthFromCircleToCenter = getLengthFromCenter(pointOnCircle)
                val lengthFromEllipseToCenter = getLengthFromCenter(pointOnEllipse)
                val lengthFromCurrentToCenter = getLengthFromCenter(Pair(originX, originY))

                if (lengthFromCurrentToCenter > cornerRadius) {
                    val originLengthToStaticCircle = lengthFromCurrentToCenter - cornerRadius
                    val scale =
                        (lengthFromEllipseToCenter - cornerRadius) / (lengthFromCircleToCenter - cornerRadius)
                    val targetLength = originLengthToStaticCircle * scale + cornerRadius
                    val point = lineSegmentsConvertedToCoordinate(originX, originY, targetLength)

                    when (part) {
                        IShadow.LEFT_TOP -> {
                            verts[index * 2 + 0] = w - point.first
                            verts[index * 2 + 1] = h - point.second
                        }
                        IShadow.RIGHT_TOP -> {
                            verts[index * 2 + 0] = point.first
                            verts[index * 2 + 1] = h - point.second
                        }
                        IShadow.RIGHT_BOTTOM -> {
                            verts[index * 2 + 0] = point.first
                            verts[index * 2 + 1] = point.second
                        }
                        else -> {
                            verts[index * 2 + 0] = w - point.first
                            verts[index * 2 + 1] = point.second
                        }
                    }
                } else {
                    when (part) {
                        IShadow.LEFT_TOP -> {
                            verts[index * 2 + 0] = w - originX
                            verts[index * 2 + 1] = h - originY
                        }
                        IShadow.RIGHT_TOP -> {
                            verts[index * 2 + 0] = originX
                            verts[index * 2 + 1] = h - originY
                        }
                        IShadow.RIGHT_BOTTOM -> {
                            verts[index * 2 + 0] = originX
                            verts[index * 2 + 1] = originY
                        }
                        else -> {
                            verts[index * 2 + 0] = w - originX
                            verts[index * 2 + 1] = originY
                        }
                    }

                }
                index++
            }
        }

        val bitmap = ShadowPool.getDirty((frame.width() / 2).toInt(), (frame.height() / 2).toInt())
        val c = Canvas(bitmap)

        if (part == IShadow.RIGHT_TOP) {
            matrix.setTranslate(-frame.centerX(), 0f)
            shader?.setLocalMatrix(matrix)
        }

        if (part == IShadow.RIGHT_BOTTOM) {
            matrix.setTranslate(-frame.centerX(), -frame.centerY())
            shader?.setLocalMatrix(matrix)
        }

        if (part == IShadow.LEFT_BOTTOM) {
            matrix.setTranslate(0f, -frame.centerY())
            shader?.setLocalMatrix(matrix)
        }

        paint.shader = shader
        c.drawRect(0f, 0f, frame.centerX(), frame.centerY(), paint)
        val dest = ShadowPool.getDirty(
            (frame.width() / 2).toInt(), (frame.height() / 2).toInt(), true
        )
        val tempCanvas = Canvas(dest)
        tempCanvas.drawBitmapMesh(bitmap, vertWidthSize, vertHeightSize, verts, 0, null, 0, null)
        ShadowPool.putDirty(bitmap)
        return dest
    }

    private fun getLengthFromCenter(p: Pair<Float, Float>): Float {
        return sqrt(p.first * p.first + p.second * p.second)
    }

    /**
     * 线段转换成坐标
     */
    private fun lineSegmentsConvertedToCoordinate(
        x: Float, y: Float, length: Float
    ): Pair<Float, Float> {
        if (x == 0.0f) {
            return Pair(0f, length)
        }
        val k = y / x
        val resultX = length / sqrt(1 + k * k)
        val resultY = resultX * k
        return Pair(resultX, resultY)
    }

    /**
     * 获取过点与圆心的直线与圆的交点
     *
     */
    private fun getPointOnCircle(x: Float, y: Float, r: Float): Pair<Float, Float> {
        if (x == 0.0f) {
            return Pair(0.0f, r)
        }
        val k = y / x
        val resultX = r / sqrt((1 + k * k))
        val resultY = resultX * k
        return Pair(resultX, resultY)
    }

    /**
     * 获取过点与椭圆圆心的直线与圆的交点
     *
     */
    private fun getPointOnEllipse(x: Float, y: Float, a: Float, b: Float): Pair<Float, Float> {
        if (x == 0.0f) {
            return Pair(0.0f, b)
        }
        val k = y / x
        val resultX = a * b / sqrt((b * b + k * k * a * a))
        val resultY = resultX * k
        return Pair(resultX, resultY)
    }

    private fun newShader(centerX: Float, centerY: Float, colors: IntArray): RadialGradient {
        return RadialGradient(
            centerX, centerY, shaderCornerRadius, colors, percents, Shader.TileMode.CLAMP
        )
    }

    fun onFrameChange(centerX: Float, centerY: Float, r: Float) {
        if (shaderCornerRadius != r || shader == null || colorChange || decrementChange) {
            colorChange = false
            decrementChange = false
            //means we need create new shader
            shaderCornerRadius = r
            center.x = centerX
            center.y = centerY
            shader = createShader()

            if ((widthDecrement > 0f || heightDecrement > 0f)) {
                makeMeshRadialGradient()
            } else {
                if (meshBitmap != null) {
                    ShadowPool.putDirty(meshBitmap!!, true)
                    meshBitmap = null
                }
            }

            makePath()
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

        if ((widthDecrement > 0f || heightDecrement > 0f)) {
            makeMeshRadialGradient()
        } else {
            if (meshBitmap != null) {
                ShadowPool.putDirty(meshBitmap!!, true)
                meshBitmap = null
            }
        }
    }

    override fun setMode(mode: Int) {
        this.mode = mode
    }

    override fun draw(canvas: Canvas, path: Path, paint: Paint) {
        if (frame.isEmpty) {
            return
        }

        if (meshBitmap != null) {
            val shadowSize = shaderCornerRadius - cornerRadius
            if(widthDecrement >= shadowSize && heightDecrement >= shadowSize) return
            when (part) {
                IShadow.RIGHT_TOP -> {
                    canvas.save()
                    canvas.translate(frame.centerX(), 0f)
                    canvas.drawBitmap(meshBitmap!!, 0f, 0f, null)
                    canvas.restore()
                }
                IShadow.RIGHT_BOTTOM -> {
                    canvas.save()
                    canvas.translate(frame.centerX(), frame.centerY())
                    canvas.drawBitmap(meshBitmap!!, 0f, 0f, null)
                    canvas.restore()
                }
                IShadow.LEFT_BOTTOM -> {
                    canvas.save()
                    canvas.translate(0f, frame.centerY())
                    canvas.drawBitmap(meshBitmap!!, 0f, 0f, null)
                    canvas.restore()
                }
                else -> {
                    canvas.drawBitmap(meshBitmap!!, 0f, 0f, null)
                }
            }
        } else {
            paint.shader = shader
            canvas.drawPath(mPath, paint)
        }
    }

    override fun recreateShader() {
        shader = createShader(percentChange = false, assign = true)


        if ((widthDecrement > 0f || heightDecrement > 0f)) {
            makeMeshRadialGradient()
        } else {
            if (meshBitmap != null) {
                ShadowPool.putDirty(meshBitmap!!, true)
                meshBitmap = null
            }
        }
    }

    override fun onDestroy() {
        linearShape = null
        adsorptionShape = null
        meshBitmap = null
        shader = null
    }

    var offsetRight = 0f
    var offsetLeft = 0f
    var offsetTop = 0f
    var offsetBottom = 0f
}