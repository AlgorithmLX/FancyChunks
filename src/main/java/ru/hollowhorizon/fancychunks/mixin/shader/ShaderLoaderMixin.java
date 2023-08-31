package ru.hollowhorizon.fancychunks.mixin.shader;

import me.jellysquid.mods.sodium.client.gl.shader.ShaderLoader;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.hollowhorizon.fancychunks.hook.OculusApiHook;

@Mixin(value = ShaderLoader.class, remap = false)
public class ShaderLoaderMixin {
    private ShaderLoaderMixin() {
    }

    @Inject(method = "getShaderSource", at = @At("RETURN"), cancellable = true)
    private static void modifyShaderForFadeInEffect(ResourceLocation name, CallbackInfoReturnable<String> cir) {
        if (OculusApiHook.isOculusShaderPackInUse()) return;

        String path = name.getPath();

        String[] splittedPath = path.split("/");
        String shaderFileName = splittedPath[splittedPath.length - 1];

        String source = cir.getReturnValue();

        switch (shaderFileName) {
            case "fog.glsl" -> source = source
                    .replaceFirst("float fogEnd\\)", "float fogEnd, float fadeCoeff)")
                    .replaceFirst("float fadeCoeff\\) \\{",
                            "float fadeCoeff) {\n    fragColor = mix(fragColor, fogColor, 1.0 - fadeCoeff);\n");
            case "block_layer_opaque.fsh" -> source = source
                    .replaceFirst("in vec4", "in float v_fadeCoeff;\nin vec4")
                    .replaceFirst("u_FogEnd\\);", "u_FogEnd, v_fadeCoeff);");
            case "block_layer_opaque.vsh" -> source = source
                    .replaceFirst("out", "out float v_fadeCoeff;\nout")
                    .replaceFirst("_vert_tex_diffuse_coord;",
                            "_vert_tex_diffuse_coord;\n    v_fadeCoeff = _fade_coeff;")
                    .replaceFirst("\\+ _vert_position;", "+ _vert_position + _fade_offset;");
            case "chunk_vertex.glsl" -> {
                source += "\n#define _fade_offset Chunk_FadeDatas[_draw_id].fadeData.xyz";
                source += "\n#define _fade_coeff Chunk_FadeDatas[_draw_id].fadeData.w";
            }
            case "chunk_parameters.glsl" -> {
                source += "\n\nstruct ChunkFadeData {\n    vec4 fadeData;\n};";
                source += "\n\nlayout(std140) uniform ubo_ChunkFadeDatas {\n    ChunkFadeData Chunk_FadeDatas[256];\n};";
            }
            default -> {}
        }

        cir.setReturnValue(source);
    }
}
