package ru.hollowhorizon.fancychunks;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import ru.hollowhorizon.fancychunks.hook.OculusApiHook;

import java.util.List;
import java.util.Set;

public class FancyChunksMixinConfig implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean isOculusShaderMixinV15 = mixinClassName
                .equals("ru.hollowhorizon.fancychunks.mixin.oculus.versioned.v15OculusShaderInterfaceMixin");
        boolean isOculusRegionMixin = mixinClassName
                .equals("ru.hollowhorizon.fancychunks.mixin.oculus.OculusRegionChunkRendererMixin");

        boolean isOculusMixin = isOculusRegionMixin || isOculusShaderMixinV15;

        if (!isOculusMixin) {
            return true;
        }

        try {
            Class.forName("net.coderbot.iris.compat.sodium.impl.shader_overrides.ShaderChunkRendererExt", false,
                    getClass().getClassLoader());

            if (isOculusRegionMixin) return true;

            boolean isOculusV15 = ModList.get().getModFileById("oculus").versionString()
                    .compareTo(new DefaultArtifactVersion("1.5").toString()) >= 0;

            if (isOculusV15 && isOculusShaderMixinV15) return true;
            else return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
