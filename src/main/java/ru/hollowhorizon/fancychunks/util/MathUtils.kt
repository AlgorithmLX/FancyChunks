package ru.hollowhorizon.fancychunks.util

import com.mojang.math.Vector3d
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion
import kotlin.math.*

object MathUtils {
    @JvmStatic
    fun clamp(value: Double, min: Double, max: Double): Double {
        return if (value < min) min else if (value > max) max else value
    }

    @JvmStatic
    fun clamp(value: Float, min: Float, max: Float): Float {
        return if (value < min) min else if (value > max) max else value
    }

    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int): Int {
        return if (value < min) min else if (value > max) max else value
    }

    @JvmStatic
    fun pow(`in`: Float, times: Int): Float {
        var shadowin = `in`
        for (i in -1 until times) shadowin *= shadowin
        return shadowin
    }

    @JvmStatic
    fun abs(`in`: Float): Float {
        return if (`in` >= 0f) `in` else -`in`
    }

    @JvmStatic
    fun abs(`in`: Vector3d): Vector3d {
        return Vector3d(abs(`in`.x), abs(`in`.y), abs(`in`.z))
    }

    @JvmStatic
    fun floor(x: Float): Int {
        var x = x
        val f = x % 1f
        if (x < 0f && f != 0f) x--
        return if (f >= 0.5f) (x - 0.5f).toInt() else x.toInt()
    }

    @JvmStatic
    fun chunkIdFromGlobal(x: Int, y: Int, z: Int): Int {
        val rX = x and RenderRegion.REGION_WIDTH - 1
        val rY = y and RenderRegion.REGION_HEIGHT - 1
        val rZ = z and RenderRegion.REGION_LENGTH - 1
        return RenderRegion.getChunkIndex(rX, rY, rZ)
    }

    @JvmStatic
    fun chunkInRange(aX: Int, aY: Int, aZ: Int, bX: Int, bY: Int, bZ: Int, radius: Int): Boolean {
        if (abs((aX - bX).toDouble()) > radius) return false
        if (abs((aY - bY).toDouble()) > radius) return false
        return if (abs((aZ - bZ).toDouble()) > radius) false else true
    }

    fun sqrt(f: Float): Float {
        return sqrt(f.toDouble()).toFloat()
    }
}