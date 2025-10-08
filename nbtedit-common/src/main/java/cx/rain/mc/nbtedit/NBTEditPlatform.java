package cx.rain.mc.nbtedit;

import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

public class NBTEditPlatform {
    @ExpectPlatform
    public static IModNetworking getNetworking() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static IModConfig getConfig() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static IModPermission getPermission() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void onServerStarted(Consumer<MinecraftServer> consumer) {
        throw new AssertionError();
    }
}
