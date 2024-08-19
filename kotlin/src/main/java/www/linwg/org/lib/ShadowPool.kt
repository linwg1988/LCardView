package www.linwg.org.lib

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Shader
import android.util.Log
import java.util.*
import kotlin.collections.set

class ShadowPool private constructor() {

    companion object {
        /**
         * Shader cache size
         */
        private var currentSize: Int = 0

        /**
         * Max shader cache size.
         */
        private const val maxSize: Int = 8 * 12

        /**
         * Dirty bitmap pool size
         */
        private var currentDirtySize: Int = 0

        /**
         * Max dirty size in bitmap pool.  16MB
         */
        private const val maxDirtySize: Int = 16 * 1024 * 1024

        /**
         * Linear mesh bitmap pool size
         */
        private var currentMeshSize: Int = 0

        /**
         * Max linear mesh size in bitmap pool size.  8MB
         */
        private const val maxMeshSize: Int = 8 * 1024 * 1024

        /**
         * Radial mesh bitmap pool size
         */
        private var currentMeshRadialSize: Int = 0

        /**
         * Max radial mesh size in bitmap pool size.  16MB
         */
        private const val maxMeshRadialSize: Int = 16 * 1024 * 1024
        private val head: Entry<Key, Shader> = Entry(Key(), null)
        private val linearShadowPool: HashMap<Key, Entry<Key, Shader>> = HashMap()
        private val linearKeyQueue = ArrayDeque<LinearKey>(20)
        private val radialKeyQueue = ArrayDeque<RadialKey>(20)
        private val dirtyKeyQueue = ArrayDeque<DirtyBitmapKey>(20)
        private val meshKeyQueue = ArrayDeque<MeshBitmapKey>(20)
        private val meshRadialKeyQueue = ArrayDeque<MeshRadialBitmapKey>(20)

        private val dirtyBitmapHead: Entry<DirtyBitmapKey, BitmapEntry> =
            Entry(DirtyBitmapKey(), null)
        private val dirtyBitmapPool: HashMap<DirtyBitmapKey, Entry<DirtyBitmapKey, BitmapEntry>> =
            HashMap()

        private val meshBitmapHead: Entry<MeshBitmapKey, BitmapEntry> = Entry(MeshBitmapKey(), null)
        private val meshBitmapPool: HashMap<MeshBitmapKey, Entry<MeshBitmapKey, BitmapEntry>> =
            HashMap()

        private val meshRadialBitmapHead: Entry<MeshRadialBitmapKey, BitmapEntry> =
            Entry(MeshRadialBitmapKey(), null)
        private val meshRadialBitmapPool: HashMap<MeshRadialBitmapKey, Entry<MeshRadialBitmapKey, BitmapEntry>> =
            HashMap()

        private fun getMeshKey(
            width: Int,
            height: Int,
            curvature: Int,
            bookRadius: Float = 0f,
            isLinear: Boolean,
            color: Int
        ): MeshBitmapKey {
            var key = meshKeyQueue.poll()
            if (key == null) {
                key = MeshBitmapKey(width, height, curvature, bookRadius, isLinear, color)
            } else {
                key.init(width, height, curvature, bookRadius, isLinear, color)
            }
            return key
        }

        fun putMesh(
            width: Int,
            height: Int,
            curvature: Int,
            color: Int,
            bookRadius: Float = 0f,
            isLinear: Boolean,
            bitmap: Bitmap
        ) {
            val key = getMeshKey(width, height, curvature, bookRadius, isLinear, color)
            var entry = meshBitmapPool[key]
            if (entry == null) {
                val v = BitmapEntry(bitmap, bitmap.allocationByteCount)
                entry = Entry(key, v)
                meshBitmapPool[key] = entry
                currentMeshSize += v.size
            } else {
                if (meshKeyQueue.size <= 20) {
                    meshKeyQueue.offer(key)
                }
                breakEntry(entry)
            }
            entry.next = meshBitmapHead
            entry.prev = meshBitmapHead.prev
            ensureEntry(entry)
            trimMesh()
        }

        fun getMesh(
            width: Int,
            height: Int,
            curvature: Int,
            bookRadius: Float = 0f,
            isLinear: Boolean,
            color: Int
        ): Bitmap? {
            val key = getMeshKey(width, height, curvature, bookRadius, isLinear, color)
            val entry = meshBitmapPool[key] ?: return null
            if (meshKeyQueue.size <= 20) {
                meshKeyQueue.offer(key)
            }
            breakEntry(entry)
            entry.next = meshBitmapHead.next
            entry.prev = meshBitmapHead
            ensureEntry(entry)
            return entry.value?.value
        }

        private fun trimMesh() {
            while (currentMeshSize > maxMeshSize) {
                val last = meshBitmapHead.prev
                if (last == null || last == meshBitmapHead) {
                    return
                }
                breakEntry(last)
                meshBitmapPool.remove(last.key)
                if (meshKeyQueue.size <= 20) {
                    meshKeyQueue.offer(last.key)
                }
                putDirty(last.value!!.value, true)
                Log.i("LCardView", "ShadowPool trim one mesh bitmap and put it to the dirty pool.")
                currentMeshSize -= last.value.size
            }
        }

        private fun getMeshRadialKey(
            width: Int,
            height: Int,
            widthDecrement: Float,
            heightDecrement: Float,
            part: Int,
            color: Int
        ): MeshRadialBitmapKey {
            var key = meshRadialKeyQueue.poll()
            if (key == null) {
                key =
                    MeshRadialBitmapKey(width, height, widthDecrement, heightDecrement, part, color)
            } else {
                key.init(width, height, widthDecrement, heightDecrement, part, color)
            }
            return key
        }

        fun putMeshRadial(
            width: Int,
            height: Int,
            widthDecrement: Float,
            heightDecrement: Float,
            part: Int,
            color: Int,
            bitmap: Bitmap
        ) {
            val key = getMeshRadialKey(width, height, widthDecrement, heightDecrement, part, color)
            var entry = meshRadialBitmapPool[key]
            if (entry == null) {
                val v = BitmapEntry(bitmap, bitmap.allocationByteCount)
                entry = Entry(key, v)
                meshRadialBitmapPool[key] = entry
                currentMeshRadialSize += v.size
            } else {
                if (meshRadialKeyQueue.size <= 20) {
                    meshRadialKeyQueue.offer(key)
                }
                breakEntry(entry)
            }
            entry.next = meshRadialBitmapHead
            entry.prev = meshRadialBitmapHead.prev
            ensureEntry(entry)
            trimMeshRadial()
        }

        fun getMeshRadial(
            width: Int,
            height: Int,
            widthDecrement: Float,
            heightDecrement: Float,
            part: Int,
            color: Int
        ): Bitmap? {
            val key = getMeshRadialKey(width, height, widthDecrement, heightDecrement, part, color)
            val entry = meshRadialBitmapPool[key] ?: return null
            if (meshRadialKeyQueue.size <= 20) {
                meshRadialKeyQueue.offer(key)
            }
            breakEntry(entry)
            entry.next = meshRadialBitmapHead.next
            entry.prev = meshRadialBitmapHead
            ensureEntry(entry)
            return entry.value?.value
        }

        private fun trimMeshRadial() {
            while (currentMeshRadialSize > maxMeshRadialSize) {
                val last = meshRadialBitmapHead.prev
                if (last == null || last == meshRadialBitmapHead) {
                    return
                }
                breakEntry(last)
                meshRadialBitmapPool.remove(last.key)
                if (meshRadialKeyQueue.size <= 20) {
                    meshRadialKeyQueue.offer(last.key)
                }
                putDirty(last.value!!.value, true)
                Log.i("LCardView", "ShadowPool trim one mesh bitmap and put it to the dirty pool.")
                currentMeshRadialSize -= last.value.size
            }
        }

        private fun getDirtyKey(width: Int, height: Int, isMesh: Boolean): DirtyBitmapKey {
            var key = dirtyKeyQueue.poll()
            if (key == null) {
                key = DirtyBitmapKey(width, height, isMesh)
            } else {
                key.init(width, height, isMesh)
            }
            return key
        }

        fun putDirty(bitmap: Bitmap, isMesh: Boolean = false) {
            val key = getDirtyKey(bitmap.width, bitmap.height, isMesh)
            var entry = dirtyBitmapPool[key]
            if (entry == null) {
                val v = BitmapEntry(bitmap, bitmap.allocationByteCount)
                entry = Entry(key, v)
                dirtyBitmapPool[key] = entry
                currentDirtySize += v.size
            } else {
                breakEntry(entry)
                if (dirtyKeyQueue.size <= 20) {
                    dirtyKeyQueue.offer(key)
                }
            }
            entry.next = dirtyBitmapHead
            entry.prev = dirtyBitmapHead.prev
            ensureEntry(entry)
            trimDirty()
        }

        fun getDirty(width: Int, height: Int, isMesh: Boolean = false): Bitmap {
            val key = getDirtyKey(width, height, isMesh)
            val entry = dirtyBitmapPool[key] ?: return Bitmap.createBitmap(
                width, height, Bitmap.Config.ARGB_8888
            )
            if (dirtyKeyQueue.size <= 20) {
                dirtyKeyQueue.offer(key)
            }
            breakEntry(entry)
            dirtyBitmapPool.remove(key)
            entry.value?.value?.eraseColor(Color.TRANSPARENT)
            return entry.value!!.value
        }

        private fun trimDirty() {
            while (currentDirtySize > maxDirtySize) {
                val last = dirtyBitmapHead.prev
                if (last == null || last == dirtyBitmapHead) {
                    return
                }
                breakEntry(last)
                if (dirtyKeyQueue.size <= 20) {
                    dirtyKeyQueue.offer(last.key)
                }
                dirtyBitmapPool.remove(last.key)
                last.value?.value?.recycle()
                Log.i("LCardView", "ShadowPool trim one dirty bitmap.")
                currentDirtySize -= last.value!!.size
            }
        }

        fun getLinearKey(
            width: Int,
            height: Int,
            widthDecrement: Float,
            heightDecrement: Float,
            mode: Int,
            part: Int,
            color: Int
        ): LinearKey {
            var key = linearKeyQueue.poll()
            if (key == null) {
                key = LinearKey()
            }
            key.init(width, height, widthDecrement, heightDecrement, mode, part, color)
            return key
        }

        fun getRadialKey(
            width: Int, height: Int, mode: Int, part: Int, cornerRadius: Float, color: Int
        ): RadialKey {
            var key = radialKeyQueue.poll()
            if (key == null) {
                key = RadialKey()
            }
            key.init(width, height, mode, part, cornerRadius, color)
            return key
        }


        fun put(key: Key, shadow: Shader) {
            var entry = linearShadowPool[key]
            if (entry == null) {
                entry = Entry(key, shadow)
                linearShadowPool[key] = entry
                currentSize++
            } else {
                breakEntry(entry)
                if (key is LinearKey) {
                    if (linearKeyQueue.size <= 20) {
                        linearKeyQueue.offer(key)
                    }
                }
                if (key is RadialKey) {
                    if (radialKeyQueue.size <= 20) {
                        radialKeyQueue.offer(key)
                    }
                }
            }
            entry.next = head
            entry.prev = head.prev
            ensureEntry(entry)
            trim()
        }

        fun get(key: Key): Shader? {
            val entry = linearShadowPool[key] ?: return null
            if (key is LinearKey) {
                if (linearKeyQueue.size <= 20) {
                    linearKeyQueue.offer(key)
                }
            }
            if (key is RadialKey) {
                if (radialKeyQueue.size <= 20) {
                    radialKeyQueue.offer(key)
                }
            }
            breakEntry(entry)
            entry.next = head.next
            entry.prev = head
            ensureEntry(entry)
            return entry.value
        }

        private fun trim() {
            while (currentSize > maxSize) {
                val last = head.prev
                if (last == null || last == head) {
                    return
                }
                if (last.key is LinearKey) {
                    if (linearKeyQueue.size <= 20) {
                        linearKeyQueue.offer(last.key)
                    }
                }
                if (last.key is RadialKey) {
                    if (radialKeyQueue.size <= 20) {
                        radialKeyQueue.offer(last.key)
                    }
                }
                breakEntry(last)
                linearShadowPool.remove(last.key)
                Log.i("LCardView", "ShadowPool trim one shadow.")
                currentSize--
            }
        }

        private fun <K, V> breakEntry(entry: Entry<K, V>) {
            entry.next?.prev = entry.prev
            entry.prev?.next = entry.next
        }

        private fun <K, V> ensureEntry(entry: Entry<K, V>) {
            entry.next?.prev = entry
            entry.prev?.next = entry
        }
    }
}

class Entry<K, V>(val key: K, val value: V?) {
    var prev: Entry<K, V>? = null
    var next: Entry<K, V>? = null
}

internal class BitmapEntry constructor(val value: Bitmap, val size: Int)

open class Key

data class LinearKey(
    var width: Int = 0,
    var height: Int = 0,
    var widthDecrement: Float = 0f,
    var heightDecrement: Float = 0f,
    var mode: Int = 0,
    var part: Int = 0,
    var startColor: Int = 0
) : Key() {
    fun init(
        width: Int,
        height: Int,
        widthDecrement: Float,
        heightDecrement: Float,
        mode: Int,
        part: Int,
        color: Int
    ) {
        this.width = width
        this.height = height
        this.widthDecrement = widthDecrement
        this.heightDecrement = heightDecrement
        this.mode = mode
        this.part = part
        this.startColor = color
    }
}

data class RadialKey(
    var width: Int = 0,
    var height: Int = 0,
    var mode: Int = 0,
    var part: Int = 0,
    var cornerRadius: Float = 0f,
    var startColor: Int = 0
) : Key() {
    fun init(width: Int, height: Int, mode: Int, part: Int, cornerRadius: Float, color: Int) {
        this.width = width
        this.height = height
        this.mode = mode
        this.part = part
        this.cornerRadius = cornerRadius
        this.startColor = color
    }
}

data class DirtyBitmapKey(var width: Int = 0, var height: Int = 0, var isMesh: Boolean = false) : Key() {
    fun init(width: Int, height: Int, isMesh: Boolean = false) {
        this.width = width
        this.height = height
        this.isMesh = isMesh
    }
}

data class MeshBitmapKey(
    var width: Int = 0,
    var height: Int = 0,
    var curvature: Int = 0,
    var bookRadius: Float = 0f,
    var isLinear: Boolean = false,
    var startColor: Int = 0
) : Key() {
    fun init(
        width: Int,
        height: Int,
        curvature: Int,
        bookRadius: Float = 0f,
        isLinear: Boolean,
        color: Int
    ) {
        this.width = width
        this.height = height
        this.curvature = curvature
        this.bookRadius = bookRadius
        this.isLinear = isLinear
        this.startColor = color
    }
}

data class MeshRadialBitmapKey(
    var width: Int = 0,
    var height: Int = 0,
    var widthDecrement: Float = 0f,
    var heightDecrement: Float = 0f,
    var part: Int = 0,
    var startColor: Int = 0
) : Key() {
    fun init(
        width: Int,
        height: Int,
        widthDecrement: Float,
        heightDecrement: Float,
        part: Int,
        color: Int
    ) {
        this.width = width
        this.height = height
        this.widthDecrement = widthDecrement
        this.heightDecrement = heightDecrement
        this.part = part
        this.startColor = color
    }
}