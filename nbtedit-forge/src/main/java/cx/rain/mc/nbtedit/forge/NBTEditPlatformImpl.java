package cx.rain.mc.nbtedit.forge;

import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.forge.command.ModPermissionImpl;
import cx.rain.mc.nbtedit.forge.config.ModConfigImpl;
import cx.rain.mc.nbtedit.forge.networking.ModNetworkingImpl;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;

import java.util.function.Consumer;

public class NBTEditPlatformImpl {
    private static final ModNetworkingImpl NETWORKING = new ModNetworkingImpl();
    private static final ModConfigImpl CONFIG = new ModConfigImpl();
    private static final ModPermissionImpl PERMISSION = new ModPermissionImpl();

    public static IModNetworking getNetworking() {
        return NETWORKING;
    }

    public static IModConfig getConfig() {
        return CONFIG;
    }

    public static IModPermission getPermission() {
        return PERMISSION;
    }

    public static void onServerStarted(Consumer<MinecraftServer> consumer) {
        MinecraftForge.EVENT_BUS.register((Consumer<ServerStartedEvent>) event -> consumer.accept(event.getServer()));
    }

    static void load() {
    }
}
