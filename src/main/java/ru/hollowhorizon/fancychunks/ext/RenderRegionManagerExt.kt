package ru.hollowhorizon.fancychunks.ext

import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion

interface RenderRegionManagerExt {
    fun getRenderRegion(chunkX: Int, chunkY: Int, chunkZ: Int): RenderRegion
}