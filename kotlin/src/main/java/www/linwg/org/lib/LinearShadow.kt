package www.linwg.org.lib

import android.graphics.*
import kotlin.math.pow
import kotlin.math.roundToInt

class LinearShadow(private val colors: IntArray, percent: Float, private val part: Int) : BaseShadow() {
    private var curvatureChange = false
    private var bookRadiusChange = false
    private var meshTypeChange = false
    var curvature: Float = 4f
        set(value) {
            curvatureChange = field != value
            field = value
        }
    private val percents: FloatArray = floatArrayOf(0f, percent, (1 - percent) / 2 + percent, 1f)
    private var linearShape: LinearGradient? = null
    private var adsorptionShape: LinearGradient? = null
    private var shader: LinearGradient? = null
    private val origin: RectF = RectF()
    private var mode: Int = LCardView.ADSORPTION
    private val matrix = Matrix()
    private var meshBitmap: Bitmap? = null
    var linearBookEffect: Boolean = false
        set(value) {
            field = value
            if (value) {
                if (curveShadowEffect) {
                    meshTypeChange = true
                }
                curveShadowEffect = false
            }
        }
    var curveShadowEffect: Boolean = false
        set(value) {
            field = value
            if (value) {
                if (linearBookEffect) {
                    meshTypeChange = true
                }
                linearBookEffect = false
            }
        }
    var bookRadius: Float = 0f
        set(value) {
            bookRadiusChange = field != value
            field = value
        }
    private val mPaint = Paint()
    private var decrementChange = false
    private var widthDecrement = 0f
    private var heightDecrement = 0f

    fun setDecrement(x: Float, y: Float) {
        if (part == IShadow.LEFT || part == IShadow.RIGHT) {
            if (widthDecrement != x) {
                widthDecrement = x
                decrementChange = true
            }
        }
        if (part == IShadow.TOP || part == IShadow.BOTTOM) {
            if (heightDecrement != y) {
                heightDecrement = y
                decrementChange = true
            }
        }
    }

    private fun createShader(assign: Boolean = true): LinearGradient {
        val colors = if (alphaHalf) {
            halfAlpha(colors)
        } else {
            this.colors
        }
        val shader = if (!useShadowPool) {
            newShader(colors)
        } else {
            val key = ShadowPool.getLinearKey(
                origin.width().toInt(),
                origin.height().toInt(),
                widthDecrement,
                heightDecrement,
                mode,
                part,
                colors[0]
            )
            val linearGradient = ShadowPool.get(key) as LinearGradient? ?: newShader(colors)
            ShadowPool.put(key, linearGradient)
            linearGradient
        }

        if (curveShadowEffect || linearBookEffect) {
            meshBitmap = if (!useShadowPool) {
                newMesh(shader)
            } else {
                var mesh = ShadowPool.getMesh(
                    frame.width().toInt(),
                    frame.height().toInt(),
                    (curvature * 1000).toInt(),
                    bookRadius,
                    linearBookEffect,
                    colors[0]
                )
                if (mesh == null) {
                    mesh = newMesh(shader)
                }
                ShadowPool.putMesh(
                    frame.width().toInt(),
                    frame.height().toInt(),
                    (curvature * 1000).toInt(),
                    colors[0],
                    bookRadius,
                    linearBookEffect,
                    mesh
                )
                mesh
            }
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

    private fun newMesh(shader: Shader): Bitmap {
        val bitmap = ShadowPool.getDirty(frame.width().toInt(), frame.height().toInt())
        val vertWidthSize = (frame.width() / 5).roundToInt()
        val vertHeightSize = (frame.height() / 5).roundToInt()
        val verts = initVerts(vertWidthSize, vertHeightSize)
        val c = Canvas(bitmap)
        matrix.setTranslate(-frame.left, -frame.top)
        shader.setLocalMatrix(matrix)
        mPaint.shader = shader
        c.drawRect(Rect(0, 0, bitmap.width, bitmap.height), mPaint)

        val dest = ShadowPool.getDirty(frame.width().toInt(), frame.height().toInt(), true)
        val meshCanvas = Canvas(dest)
        meshCanvas.drawBitmapMesh(bitmap, vertWidthSize, vertHeightSize, verts, 0, null, 0, null)
        ShadowPool.putDirty(bitmap)
        return dest
    }

    private fun newShader(colors: IntArray): LinearGradient {
        return when (part) {
            IShadow.TOP -> LinearGradient(
                origin.left,
                origin.bottom,
                origin.left,
                origin.top + heightDecrement,
                colors,
                percents,
                Shader.TileMode.CLAMP
            )
            IShadow.RIGHT -> LinearGradient(
                origin.left,
                origin.top,
                origin.right - widthDecrement,
                origin.top,
                colors,
                percents,
                Shader.TileMode.CLAMP
            )
            IShadow.BOTTOM -> LinearGradient(
                origin.left,
                origin.top,
                origin.left,
                origin.bottom - heightDecrement,
                colors,
                percents,
                Shader.TileMode.CLAMP
            )
            else -> LinearGradient(
                origin.right,
                origin.top,
                origin.left + widthDecrement,
                origin.top,
                colors,
                percents,
                Shader.TileMode.CLAMP
            )
        }
    }

    fun onFrameChange() {
        val needRecreate: Boolean
        if (origin.isEmpty || shader == null || meshTypeChange || colorChange || (part == IShadow.BOTTOM && ((curveShadowEffect && (curvatureChange || meshBitmap == null)) || (linearBookEffect && (bookRadiusChange || meshBitmap == null)))) || decrementChange) {
            colorChange = false
            curvatureChange = false
            decrementChange = false
            bookRadiusChange = false
            meshTypeChange = false
            needRecreate = true
        } else {
            needRecreate = when (part) {
                IShadow.TOP -> origin.height() != frame.height()
                IShadow.RIGHT -> origin.width() != frame.width()
                IShadow.BOTTOM -> origin.height() != frame.height() || ((curveShadowEffect || linearBookEffect) && origin.width() != frame.width())
                IShadow.LEFT -> origin.width() != frame.width()
                else -> false
            }
        }
        if (needRecreate) {
            colorChange = false
            origin.set(frame)
            shader = createShader()
            return
        }
        matrix.setTranslate(frame.left - origin.left, frame.top - origin.top)
        shader!!.setLocalMatrix(matrix)
    }

    private fun initVerts(vertWidthSize: Int, vertHeightSize: Int): FloatArray {

        val count = (vertWidthSize + 1) * (vertHeightSize + 1)
        val verts = FloatArray(count * 2)

        val w = frame.width()
        val h = frame.height()
        var index = 0
        //将图片分割，然后保存坐标点

        val b = h - h / curvature
        val k = b / (w / 2).pow(2)

        for (y in 0..vertHeightSize) {
            for (x in 0..vertWidthSize) {
                val fx = (w / vertWidthSize) * x
                val oy = (h / vertHeightSize) * y
                val fy = if (linearBookEffect) {
                    val lk = h * bookRadius / (w / 2)
                    val funcY = if (fx < w / 2) {
                        fx * lk
                    } else {
                        (h * bookRadius) - ((fx - w / 2) * lk)
                    }
                    oy - funcY
                } else {
                    val funcY = h - getFY(fx - w / 2, k, b)
                    oy * (funcY / h)
                }

                verts[index * 2 + 0] = fx
                verts[index * 2 + 1] = fy
                index++
            }
        }
        return verts
    }


    private fun getFY(x: Float, k: Float, b: Float): Float {
        return -k * x.pow(2) + b
    }

    override fun onShapeModeChange(mode: Int) {
        this.mode = mode
        shader = if (mode == LCardView.ADSORPTION) {
            if (adsorptionShape == null) {
                adsorptionShape = createShader(false)
            }
            adsorptionShape
        } else {
            if (linearShape == null) {
                linearShape = createShader(false)
            }
            linearShape
        }
    }

    override fun setMode(mode: Int) {
        this.mode = mode
    }

    override fun draw(canvas: Canvas, path: Path, paint: Paint) {
        if (frame.width() <= 0 || frame.height() <= 0) return
        if(frame.width() - widthDecrement <= 0f || frame.height() - heightDecrement <= 0f) return
        paint.shader = shader
        if (part == IShadow.BOTTOM && (linearBookEffect || curveShadowEffect)) {
            canvas.save()
            canvas.clipRect(frame)
            canvas.translate(frame.left, frame.top)
            canvas.drawBitmap(meshBitmap!!, 0f, 0f, null)
            canvas.restore()
        } else {
            canvas.drawRect(frame.left, frame.top, frame.right, frame.bottom, paint)
        }
    }

    override fun recreateShader() {
        shader = createShader(true)
    }

    override fun onDestroy() {
        linearShape = null
        adsorptionShape = null
        shader = null
        meshBitmap = null
    }
}