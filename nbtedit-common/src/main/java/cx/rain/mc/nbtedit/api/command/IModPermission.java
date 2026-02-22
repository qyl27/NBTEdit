package cx.rain.mc.nbtedit.api.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public interface IModPermission {
    boolean hasPermission(CommandSourceStack sourceStack, ModPermission permission);

    boolean hasPermission(ServerPlayer player, ModPermission permission);

    default boolean canOpenEditor(ServerPlayer player) {
        return hasPermission(player, ModPermission.USE) || hasPermission(player, ModPermission.READ_ONLY);
    }

    default boolean isReadOnly(ServerPlayer player) {
        return !hasPermission(player, ModPermission.USE) && hasPermission(player, ModPermission.READ_ONLY);
    }

    default boolean canSave(ServerPlayer player) {
        return hasPermission(player, ModPermission.USE);
    }

    default boolean canEditOnPlayer(ServerPlayer player) {
        return hasPermission(player, ModPermission.EDIT_ON_PLAYER);
    }
}
