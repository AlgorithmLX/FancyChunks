package ru.hollowhorizon.fancychunks.core

import net.minecraft.core.SectionPos
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunkSection

object ChunkUtils {
    @JvmStatic
    fun getChunkOn(level: Level, pos: SectionPos): LevelChunkSection? {
        return getChunkOn(level, pos.x, pos.y, pos.z)
    }

    @JvmStatic
    fun getChunkOn(level: Level, pos: ChunkPos, y: Int): LevelChunkSection? {
        return getChunkOn(level, pos.x, y, pos.z)
    }

    @JvmStatic
    fun getChunkOn(level: Level, x: Int, y: Int, z: Int): LevelChunkSection? {
        val array = level.getChunk(x, z).sections
        val sectionY = level.getSectionIndexFromSectionY(y)

        return if (sectionY < array.size && sectionY >= 0) array[sectionY] else null
    }
}