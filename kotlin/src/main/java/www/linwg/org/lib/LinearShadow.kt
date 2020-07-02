package www.linwg.org.lib

import android.graphics.*
import android.util.Log
import kotlin.math.pow

class LinearShadow(private val colors: IntArray, percent: Float, private val part: Int) : BaseShadow() {
    private var curvatureChange = false
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
    private val WIDTH = 50
    private val HEIGHT = 20
    private val count = (WIDTH + 1) * (HEIGHT + 1)
    private val verts: FloatArray by lazy { FloatArray(count * 2) }
    var linearBookEffect: Boolean = false
    var curveShadowEffect: Boolean = false
    var bookRadius: Float = 2f
    private val mPaint = Paint()

    private fun createShader(assign: Boolean = true): LinearGradient {
        val colors = if (alphaHalf) {
            halfAlpha(colors)
        } else {
            this.colors
        }
        val shader = if (!useShadowPool) {
            newShader(colors)
        } else {
            val key = ShadowPool.getLinearKey(origin.width().toInt(), origin.height().toInt(), mode, part, colors[0])
            val linearGradient = ShadowPool.get(key) as LinearGradient? ?: newShader(colors)
            ShadowPool.put(key, linearGradient)
            linearGradient
        }

        if (curveShadowEffect) {
            meshBitmap = if (!useShadowPool) {
                newMesh(shader)
            } else {
                var mesh = ShadowPool.getMesh(frame.width().toInt(), frame.height().toInt(), (curvature * 1000).toInt(), colors[0])
                if (mesh == null) {
                    mesh = newMesh(shader)
                }
                ShadowPool.putMesh(frame.width().toInt(), frame.height().toInt(), (curvature * 1000).toInt(), colors[0], mesh)
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
        initVerts()
        val c = Canvas(bitmap)
        matrix.setTranslate(-frame.left, -frame.top)
        shader.setLocalMatrix(matrix)
        mPaint.shader = shader
        c.drawRect(Rect(0, 0, bitmap.width, bitmap.height), mPaint)

        val dest = ShadowPool.getDirty(frame.width().toInt(), frame.height().toInt(),true)
        val meshCanvas = Canvas(dest)
        meshCanvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null)
        Log.i("LCardView", "create curve bitmap at ${System.currentTimeMillis()}")
        ShadowPool.putDirty(bitmap)
        return dest
    }

    private fun newShader(colors: IntArray): LinearGradient {
        Log.i("LCardView", "create LinearGradient part at $part  time at ${System.currentTimeMillis()}")
        return when (part) {
            IShadow.TOP -> LinearGradient(origin.left, origin.bottom, origin.left, origin.top, colors, percents, Shader.TileMode.CLAMP)
            IShadow.RIGHT -> LinearGradient(origin.left, origin.top, origin.right, origin.top, colors, percents, Shader.TileMode.CLAMP)
            IShadow.BOTTOM -> LinearGradient(origin.left, origin.top, origin.left, origin.bottom, colors, percents, Shader.TileMode.CLAMP)
            else -> LinearGradient(origin.right, origin.top, origin.left, origin.top, colors, percents, Shader.TileMode.CLAMP)
        }
    }

    fun onFrameChange() {
        val needRecreate: Boolean
        if (origin.isEmpty || shader == null || colorChange || (part == IShadow.BOTTOM && curvatureChange)) {
            colorChange = false
            curvatureChange = false
            needRecreate = true
        } else {
            needRecreate = when (part) {
                IShadow.TOP -> origin.height() != frame.height()
                IShadow.RIGHT -> origin.width() != frame.width()
                IShadow.BOTTOM -> origin.height() != frame.height()
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

    private fun initVerts() {
        val w = frame.width()
        val h = frame.height()
        var index = 0
        //将图片分割，然后保存坐标点
        for (y in 0..HEIGHT) {
            for (x in 0..WIDTH) {
                val fx = (w / WIDTH) * x
                val positiveY = findPositiveY(fx, w, h, h / curvature)
                val negativeY = findNegativeY(fx, w, h)
                val fy = (positiveY + negativeY) / HEIGHT * y - negativeY
                //用数组保存坐标点fx , fy
                verts[index * 2 + 0] = fx
                verts[index * 2 + 1] = fy
                index++
            }
        }
    }

    private fun findPositiveY(x: Float, width: Float, height: Float, min: Float): Float {
        val mid = width / 2f
        val a = (height - min) / mid.pow(2)
        return a * (x - mid).pow(2) + min
    }

    private fun findNegativeY(x: Float, width: Float, height: Float): Float {
        val mid = width / 2f
        val a = 4 * height / (width.pow(2))
        return a * (x - mid).pow(2)
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
        canvas.save()
        paint.shader = shader
        if (part == IShadow.BOTTOM) {
            when {
                linearBookEffect -> {
                    canvas.clipRect(frame.left, frame.top, frame.centerX(), frame.bottom)
                    matrix.setRotate(-bookRadius, frame.left, frame.top)
                    shader?.setLocalMatrix(matrix)
                    canvas.drawRect(frame.left, frame.top, frame.right, frame.bottom, paint)
                    canvas.restore()

                    canvas.save()
                    canvas.clipRect(frame.centerX(), frame.top, frame.right, frame.bottom)
                    matrix.setRotate(bookRadius, frame.right, frame.bottom)
                    shader?.setLocalMatrix(matrix)
                    canvas.drawRect(frame.left, frame.top, frame.right, frame.bottom, paint)
                }
                curveShadowEffect -> {
                    canvas.clipRect(frame)
                    canvas.translate(frame.left, frame.top)
                    canvas.drawBitmap(meshBitmap!!, 0f, 0f, null)
                }
                else -> {
                    canvas.clipRect(frame)
                    canvas.drawRect(frame.left, frame.top, frame.right, frame.bottom, paint)
                }
            }
        } else {
            canvas.clipRect(frame)
            canvas.drawRect(frame.left, frame.top, frame.right, frame.bottom, paint)
        }
        canvas.restore()
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