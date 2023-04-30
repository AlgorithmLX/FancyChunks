package ru.hollowhorizon.fancychunks.mixin;

import me.jellysquid.mods.sodium.client.gl.buffer.GlMutableBuffer;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformBlock;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.hollowhorizon.fancychunks.ext.ChunkShaderInterfaceExt;

@Mixin(
        value = {ChunkShaderInterface.class},
        remap = false
)
public abstract class ChunkShaderInterfaceMixin implements ChunkShaderInterfaceExt {
    private GlUniformBlock uniformFadeDatas;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void modifyShaderForFadeInEffect(ShaderBindingContext context, ChunkShaderOptions options, CallbackInfo ci) {
        this.uniformFadeDatas = context.bindUniformBlock("ubo_ChunkFadeDatas", 1);
    }

    public void setFadeDatas(GlMutableBuffer buffer) {
        this.uniformFadeDatas.bindBuffer(buffer);
    }
}