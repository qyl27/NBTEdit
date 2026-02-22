package cx.rain.mc.nbtedit.neoforge.command;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.command.ModPermission;
import cx.rain.mc.nbtedit.api.command.VanillaModPermission;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = NBTEdit.MODID)
public class ModPermissionImpl implements IModPermission {
    public static final Map<ModPermission, PermissionNode<Boolean>> NODES = new HashMap<>();

    private static PermissionNode<Boolean> bool(ModPermission permission) {
        return new PermissionNode<>(permission.getId(), PermissionTypes.BOOLEAN,
                (player, uuid, context) -> player != null && VanillaModPermission.INSTANCE.hasPermission(player, permission));
    }

    @SubscribeEvent
    public static void registerPermission(PermissionGatherEvent.Nodes event) {
        for (var p : ModPermission.values()) {
            var node = bool(p);
            NODES.put(p, node);
            event.addNodes(node);
        }
    }

    @Override
    public boolean hasPermission(CommandSourceStack sourceStack, ModPermission permission) {
        if (sourceStack.getPlayer() instanceof ServerPlayer player) {
            return hasPermission(player, permission);
        }
        return VanillaModPermission.INSTANCE.hasPermission(sourceStack, permission);
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ModPermission permission) {
        return PermissionAPI.getPermission(player, NODES.get(permission));
    }
}
