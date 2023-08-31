package ru.hollowhorizon.fancychunks.mixin.shader;

import me.jellysquid.mods.sodium.client.gl.buffer.GlMutableBuffer;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformBlock;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.hollowhorizon.fancychunks.core.FadeShaderInterface;
import ru.hollowhorizon.fancychunks.ext.ChunkShaderInterfaceExt;
import ru.hollowhorizon.fancychunks.hook.OculusApiHook;

@Mixin(value = ChunkShaderInterface.class, remap = false)
public abstract class ChunkShaderInterfaceMixin implements ChunkShaderInterfaceExt {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkShaderInterfaceMixin.class);
    private FadeShaderInterface fadeInterface;
    private static boolean warning = false;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void modifyShaderForFadeInEffect(ShaderBindingContext context, ChunkShaderOptions options, CallbackInfo ci) {
        if (OculusApiHook.isOculusShaderPackInUse()) return;

        fadeInterface = new FadeShaderInterface(context);
    }

    public void setFadeDatas(GlMutableBuffer buffer) {
        if (this.fadeInterface == null) {
            if (OculusApiHook.isOculusShaderPackInUse() && !warning) {
                LOGGER.warn("Shader pack is in use, but Sodium's shader interface is used. Something went really wrong!");
                warning = true;
            }
        }
    }
}