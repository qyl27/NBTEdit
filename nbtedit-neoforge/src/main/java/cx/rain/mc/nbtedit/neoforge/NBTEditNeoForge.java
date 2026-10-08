package cx.rain.mc.nbtedit.neoforge;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.neoforge.config.ModConfigImpl;
import lombok.extern.slf4j.Slf4j;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Slf4j
@Mod(value = NBTEdit.MODID)
public class NBTEditNeoForge {
    public NBTEditNeoForge(IEventBus bus, ModContainer container) {
        NBTEditPlatformImpl.load();

        container.registerConfig(ModConfig.Type.COMMON, ModConfigImpl.CONFIG, "nbtedit.toml");

        NBTEdit.init();
    }
}
