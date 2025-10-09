package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.networking.packet.c2s.BlockEntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.EntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.ItemStackRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.utility.LoggingHelper;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class NetworkServerHandler {
    public static void handleBlockEntityResult(ServerPlayer player, BlockEntityRaytraceResultPacket packet) {
        NetworkEditingHelper.editBlockEntity(player, packet.pos());
    }

    public static void handleEntityResult(ServerPlayer player, EntityRaytraceResultPacket packet) {
        NetworkEditingHelper.editEntity(player, packet.uuid());
    }

    public static void handleItemStackResult(ServerPlayer player, ItemStackRaytraceResultPacket packet) {
        NetworkEditingHelper.editItemStack(player, packet.itemStack());
    }

    public static void saveBlockEntity(ServerPlayer player, BlockEntityEditingPacket packet) {
        if (!NetworkingHelper.checkWritePermission(player)) {
            return;
        }

        var pos = packet.pos();
        if (!NetworkingHelper.checkPosLoaded(player, pos)) {
            return;
        }

        var tag = packet.tag();

        var server = player.server;
        var level = player.level();
        server.execute(() -> {
            var blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                try {
                    var input = NBTEdit.getInstance().getRegistryContextSerializer().createTagValueInput(tag);
                    blockEntity.loadWithComponents(input);
                    blockEntity.setChanged();	// Ensure changes gets saved to disk later on. (qyl27: In MCP it is markDirty.)
                    if (blockEntity.hasLevel() && blockEntity.getLevel() instanceof ServerLevel) {
                        ((ServerLevel) blockEntity.getLevel()).getChunkSource().blockChanged(pos);	// Broadcast changes.
                    }

                    NBTEdit.getInstance().getLogger().info("Player {} successfully edited the tag of a BlockEntity at {} {} {}.",
                            player.getName().getString(), pos.getX(), pos.getY(), pos.getZ());


                    LoggingHelper.debugBlockEntityTag(pos, tag);

                    player.sendSystemMessage(Component
                            .translatable(ModConstants.MESSAGE_SAVING_SUCCESSFUL)
                            .withStyle(ChatFormatting.GREEN));
                } catch (Exception ex) {
                    player.sendSystemMessage(Component
                            .translatable(ModConstants.MESSAGE_SAVING_FAILED_INVALID_NBT)
                            .withStyle(ChatFormatting.RED));

                    NBTEdit.getInstance().getLogger().error("Player {} edited the tag of BlockEntity at XYZ {} {} {} and caused an exception!",
                            player.getName().getString(), pos.getX(), pos.getY(), pos.getZ());

                    LoggingHelper.errorParsingTag(ex, tag);
                }
            } else {
                NBTEdit.getInstance().getLogger().info("Player {} tried to edit a non-existent BlockEntity at {} {} {}.",
                        player.getName(), pos.getX(), pos.getY(), pos.getZ());
                player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_SAVING_FAILED_BLOCK_ENTITY_NOT_EXISTS)
                        .withStyle(ChatFormatting.RED));
            }
        });
    }

    public static void saveEntity(ServerPlayer player, EntityEditingPacket packet) {
        if (!NetworkingHelper.checkWritePermission(player)) {
            return;
        }

        var tag = packet.tag();
        var entityUuid = packet.uuid();

        var server = player.server;
        var level = player.level();
        server.execute(() -> {
            var entity = level.getEntity(entityUuid);

            if (entity != null) {
                if (entity instanceof Player
                        && entity != player
                        && !NetworkingHelper.checkEditOnPlayerPermission(player)) {
                    NBTEdit.getInstance().getLogger().info("Player {} tried to use nbtedit on a player which is not allowed",
                            player.getName().getString());
                    return;
                }

                try {
                    GameType prevGameMode = null;
                    if (entity instanceof ServerPlayer) {
                        prevGameMode = ((ServerPlayer) entity).gameMode.getGameModeForPlayer();
                    }

                    var input = NBTEdit.getInstance().getRegistryContextSerializer().createTagValueInput(tag);
                    entity.load(input);
                    NBTEdit.getInstance().getLogger().info("Player {} edited the tag of Entity with UUID {} .",
                            player.getName().getString(), entityUuid);

                    LoggingHelper.debugEntityTag(entityUuid, tag);

                    if (entity instanceof ServerPlayer targetPlayer) {
                        // qyl27: if anyone found bugs with it, please open an issue.
                        // Update player info
                        // This is fairly hacky.
                        // Consider swapping to an event driven system, where classes can register to
                        // receive entity edit events and provide feedback/send packets as necessary.
                        targetPlayer.initMenu(targetPlayer.inventoryMenu);
                        var gameMode = targetPlayer.gameMode.getGameModeForPlayer();
                        if (prevGameMode != gameMode) {
                            targetPlayer.setGameMode(gameMode);
                        }
                        targetPlayer.connection.send(new ClientboundSetHealthPacket(targetPlayer.getHealth(),
                                targetPlayer.getFoodData().getFoodLevel(),
                                targetPlayer.getFoodData().getSaturationLevel()));
                        targetPlayer.connection.send(new ClientboundSetExperiencePacket(
                                targetPlayer.experienceProgress,
                                targetPlayer.totalExperience,
                                targetPlayer.experienceLevel));

                        targetPlayer.onUpdateAbilities();
                        targetPlayer.setPos(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ());
                    }

                    player.sendSystemMessage(Component
                            .translatable(ModConstants.MESSAGE_SAVING_SUCCESSFUL)
                            .withStyle(ChatFormatting.GREEN));
                } catch (Exception ex) {
                    player.sendSystemMessage(Component
                            .translatable(ModConstants.MESSAGE_SAVING_FAILED_INVALID_NBT)
                            .withStyle(ChatFormatting.RED));

                    NBTEdit.getInstance().getLogger().error("Player {} edited the tag of entity {} and caused an exception!",
                            player.getName().getString(), entityUuid);

                    LoggingHelper.errorParsingTag(ex, tag);
                }
            } else {
                NBTEdit.getInstance().getLogger().info("Player {} tried to edit a non-existent entity {}.",
                        player.getName(), entityUuid);
                player.sendSystemMessage(Component
                        .translatable(ModConstants.MESSAGE_SAVING_FAILED_ENTITY_NOT_EXISTS)
                        .withStyle(ChatFormatting.RED));
            }
        });
    }

    public static void saveItemStack(ServerPlayer player, ItemStackEditingPacket packet) {
        if (!NetworkingHelper.checkWritePermission(player)) {
            return;
        }

        var tag = packet.tag();
        var itemStack = packet.itemStack();

        var server = player.server;
        server.execute(() -> {
            try {
                var item = NBTEdit.getInstance().getRegistryContextSerializer().deserializeItemStack(tag);
                player.setItemInHand(InteractionHand.MAIN_HAND, item);

                NBTEdit.getInstance().getLogger().info("Player {} successfully edited the tag of a ItemStack named {}.",
                        player.getName().getString(), itemStack.getDisplayName().getString());

                LoggingHelper.debugItemStackTag(player, tag);

                player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_SAVING_SUCCESSFUL)
                        .withStyle(ChatFormatting.GREEN));
            } catch (Exception ex) {
                player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_SAVING_FAILED_INVALID_NBT)
                        .withStyle(ChatFormatting.RED));

                NBTEdit.getInstance().getLogger().error("Player {} edited the tag of ItemStack named {} and caused an exception!",
                        player.getName().getString(), itemStack.getDisplayName().getString());

                LoggingHelper.errorParsingTag(ex, tag);
            }
        });
    }
}
