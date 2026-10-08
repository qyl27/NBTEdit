package cx.rain.mc.nbtedit.forge;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.forge.config.ModConfigImpl;
import lombok.extern.slf4j.Slf4j;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Slf4j
@Mod(value = NBTEdit.MODID)
public class NBTEditForge {
    public NBTEditForge(FMLJavaModLoadingContext context) {
        NBTEditPlatformImpl.load();

        context.registerConfig(ModConfig.Type.COMMON, ModConfigImpl.CONFIG, "nbtedit.toml");

        NBTEdit.init();
    }
}
