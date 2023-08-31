package ru.hollowhorizon.fancychunks.mixin.oculus;

import net.coderbot.iris.compat.sodium.impl.shader_overrides.IrisChunkProgramOverrides;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ru.hollowhorizon.fancychunks.core.ShaderInjector;

@Pseudo
@Mixin(IrisChunkProgramOverrides.class)
public class OculusChunkProgramOverridesMixin {
    @Unique
    private static final ShaderInjector fancyChunks$vertexInjectorFull = new ShaderInjector();
    @Unique
    private static final ShaderInjector fancyChunks$fragmentInjectorFull = new ShaderInjector();
    static {
        fancyChunks$vertexInjectorFull.insertAfterDefines(
            "out float fadeCoeff;",
            "struct ChunkFadeData {",
            "    vec4 fadeData;",
            "};",
            "layout(std140) uniform ubo_ChunkFadeDatas {",
            "    ChunkFadeData Chunk_FadeDatas[256];",
            "};");
        fancyChunks$vertexInjectorFull.appendToFunction("void _vert_init()",
            "fadeCoeff = Chunk_FadeDatas[_draw_id].fadeData.w;",
            "_vert_position = _vert_position + Chunk_FadeDatas[_draw_id].fadeData.xyz;");

        fancyChunks$fragmentInjectorFull.insertAfterDefines("in float fadeCoeff;");

        fancyChunks$fragmentInjectorFull.appendToFunction("void main()",
                "if(fadeCoeff != 0.0) ${uniform_0} = ${uniform_0_prefix}mix(${uniform_0}, iris_FogColor, 1.0 - fadeCoeff)${uniform_0_postfix};");
    }

    @ModifyVariable(method = "createVertexShader", at = @At(value = "STORE", ordinal = 0), remap = false)
    private String modifyCreateVertexShader(String irisVertexShader) {
        if (irisVertexShader == null) return null;

        return fancyChunks$fragmentInjectorFull.get(irisVertexShader);
    }

    @ModifyVariable(method = "createFragmentShader", at = @At(value = "STORE", ordinal = 0), remap = false)
    private String modifyCreateFragmentShader(String irisFragmentShader) {
        if (irisFragmentShader == null) return null;

        return fancyChunks$fragmentInjectorFull.get(irisFragmentShader);
    }
}
