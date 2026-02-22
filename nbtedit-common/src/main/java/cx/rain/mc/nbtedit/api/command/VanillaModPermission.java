package cx.rain.mc.nbtedit.api.command;

import cx.rain.mc.nbtedit.NBTEditPlatform;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionSet;

import java.util.HashMap;
import java.util.Map;

public class VanillaModPermission implements IModPermission {
    public static final VanillaModPermission INSTANCE = new VanillaModPermission();

    private static final Map<ModPermission, Permission.HasCommandLevel> OVERRIDDEN_PERMISSIONS = new HashMap<>();

    @Override
    public boolean hasPermission(CommandSourceStack sourceStack, ModPermission permission) {
        return hasPermissionInternal(sourceStack.permissions(), permission);
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ModPermission permission) {
        return hasPermissionInternal(player.permissions(), permission);
    }

    private boolean hasPermissionInternal(PermissionSet permissionSet, ModPermission permission) {
        var overriddenLevel = OVERRIDDEN_PERMISSIONS.computeIfAbsent(permission, p -> {
            var config = NBTEditPlatform.getConfig();
            var level = config.getOverriddenPermissionLevel(p);
            return new Permission.HasCommandLevel(level);
        });

        return permissionSet.hasPermission(overriddenLevel)
                || permissionSet.hasPermission(permission.getAtomPermission());
    }
}
