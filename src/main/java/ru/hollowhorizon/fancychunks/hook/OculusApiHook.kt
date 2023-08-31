package ru.hollowhorizon.fancychunks.hook

import net.irisshaders.iris.api.v0.IrisApi
import net.minecraftforge.fml.ModList
import org.slf4j.LoggerFactory
import ru.hollowhorizon.fancychunks.ext.ChunkShaderInterfaceExt

object OculusApiHook {
    private val LOGGER = LoggerFactory.getLogger(OculusApiHook::class.java)

    @JvmStatic
    lateinit var oculusExt: ChunkShaderInterfaceExt

    @JvmStatic
    fun isOculusShaderPackInUse(): Boolean {
        if (!oculusIsLoaded()) return false

        return try {
            IrisApi.getInstance().isShaderPackInUse
        } catch (e: Exception) {
            LOGGER.error("Enable to render chunk animation", e)
            false
        }
    }

    private fun oculusIsLoaded(): Boolean = ModList.get().isLoaded("oculus")
}