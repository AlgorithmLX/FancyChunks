package ru.hollowhorizon.fancychunks.core

import me.jellysquid.mods.sodium.client.gl.buffer.GlMutableBuffer
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformBlock
import me.jellysquid.mods.sodium.client.render.chunk.shader.ShaderBindingContext
import net.coderbot.iris.compat.sodium.impl.shader_overrides.ShaderBindingContextExt

class FadeShaderInterface(context: Any) {
    private var uniformFadeDatas: GlUniformBlock? = null

    init {
        if (context is ShaderBindingContext)
           this.uniformFadeDatas = context.bindUniformBlock("ubo_ChunkFadeDatas", 1)
        else if (context is ShaderBindingContextExt)
            this.uniformFadeDatas = context.bindUniformBlockIfPresent("ubo_ChunkFadeDatas", 1)
    }

    fun setFadeDatas(buf: GlMutableBuffer) {
        this.uniformFadeDatas?.bindBuffer(buf)
    }
}
