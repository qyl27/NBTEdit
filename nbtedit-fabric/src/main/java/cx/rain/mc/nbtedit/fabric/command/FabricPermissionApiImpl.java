package cx.rain.mc.nbtedit.fabric.command;

import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.command.ModPermission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionLevel;

public class FabricPermissionApiImpl implements IModPermission {
    public static final FabricPermissionApiImpl INSTANCE = new FabricPermissionApiImpl();

    @Override
    public boolean hasPermission(CommandSourceStack sourceStack, ModPermission permission) {
        return Permissions.check(sourceStack, permission.getFullName(), getOverriddenLevel(permission));
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ModPermission permission) {
        return Permissions.check(player, permission.getFullName(), getOverriddenLevel(permission));
    }

    private PermissionLevel getOverriddenLevel(ModPermission permission) {
        var config = NBTEditPlatform.getConfig();
        return config.getOverriddenPermissionLevel(permission);
    }
}
