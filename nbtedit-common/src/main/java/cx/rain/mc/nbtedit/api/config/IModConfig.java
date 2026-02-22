package cx.rain.mc.nbtedit.api.config;

import cx.rain.mc.nbtedit.api.command.ModPermission;
import net.minecraft.server.permissions.PermissionLevel;

public interface IModConfig {
    boolean isDebug();

    PermissionLevel getOverriddenPermissionLevel(ModPermission permission);
}
