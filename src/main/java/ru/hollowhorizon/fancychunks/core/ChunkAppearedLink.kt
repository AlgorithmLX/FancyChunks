package ru.hollowhorizon.fancychunks.core

import ru.hollowhorizon.fancychunks.ext.RenderRegionExt
import ru.hollowhorizon.fancychunks.ext.RenderRegionManagerExt

object ChunkAppearedLink {
    @JvmStatic
    var regionManager: RenderRegionManagerExt? = null

    @JvmStatic
    fun getChunkData(x: Int, y: Int, z: Int): FloatArray? {
        if (regionManager == null) return null

        val region = regionManager?.getRenderRegion(x, y, z) ?: return ChunkData.FULLY_FADED

        val ext = region as RenderRegionExt

        return ext.getChunkData(x, y, z)
    }

    @JvmStatic
    fun completeChunkFade(x: Int, y: Int, z: Int, completeFade: Boolean) {
        if (regionManager == null) return

        val region = regionManager?.getRenderRegion(x, y, z) ?: return

        val ext = region as RenderRegionExt

        ext.competeChunkFade(x, y, z, completeFade)
    }
}

object ChunkData {
    var FULLY_FADED = floatArrayOf(0f, 0f, 0f, 1f)
}