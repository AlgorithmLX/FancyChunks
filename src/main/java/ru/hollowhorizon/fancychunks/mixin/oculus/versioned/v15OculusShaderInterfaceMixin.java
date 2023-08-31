package ru.hollowhorizon.fancychunks.mixin.oculus.versioned;

import me.jellysquid.mods.sodium.client.gl.buffer.GlMutableBuffer;
import net.coderbot.iris.compat.sodium.impl.shader_overrides.IrisChunkShaderInterface;
import net.coderbot.iris.compat.sodium.impl.shader_overrides.ShaderBindingContextExt;
import net.coderbot.iris.gl.blending.BlendModeOverride;
import net.coderbot.iris.pipeline.SodiumTerrainPipeline;
import net.coderbot.iris.uniforms.custom.CustomUniforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import repack.joml.Matrix4f;
import ru.hollowhorizon.fancychunks.core.FadeShaderInterface;
import ru.hollowhorizon.fancychunks.ext.ChunkShaderInterfaceExt;
import ru.hollowhorizon.fancychunks.hook.OculusApiHook;

import java.util.List;

@Pseudo
@Mixin(value = IrisChunkShaderInterface.class, remap = false)
public class v15OculusShaderInterfaceMixin implements ChunkShaderInterfaceExt {
    @Unique
    private FadeShaderInterface fadeInterface;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInj(int handle, ShaderBindingContextExt contextExt, SodiumTerrainPipeline pipeline, boolean isShadowPass, BlendModeOverride blendModeOverride, List bufferOverrides, float alpha, CustomUniforms customUniforms, CallbackInfo ci) {
        fadeInterface = new FadeShaderInterface(contextExt);
    }

    @Inject(method = "setModelViewMatrix", at = @At("HEAD"))
    private void modifySetModelViewMatrix(Matrix4f modelView, CallbackInfo ci) {
        OculusApiHook.oculusExt = this;
    }

    @Override
    public void setFadeDatas(GlMutableBuffer var1) {
        fadeInterface.setFadeDatas(var1);
    }
}
