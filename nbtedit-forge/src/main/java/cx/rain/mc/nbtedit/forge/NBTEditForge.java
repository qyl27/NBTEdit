package cx.rain.mc.nbtedit.forge;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.forge.config.ModConfigImpl;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = NBTEdit.MODID)
public class NBTEditForge {
    private final NBTEdit nbtedit;

    public NBTEditForge(FMLJavaModLoadingContext context) {
        NBTEditPlatformImpl.load();

        nbtedit = new NBTEdit();

        context.registerConfig(ModConfig.Type.COMMON, ModConfigImpl.CONFIG, "nbtedit.toml");

        nbtedit.getLogger().info("NBTEdit loaded!");
    }
}
