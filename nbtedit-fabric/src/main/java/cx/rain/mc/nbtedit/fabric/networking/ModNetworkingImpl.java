package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.networking.NetworkServerHandler;
import cx.rain.mc.nbtedit.networking.packet.c2s.BlockEntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.EntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.ItemStackRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.s2c.RaytracePacket;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class ModNetworkingImpl implements IModNetworking {
    private @Nullable NBTEditNetworkingClient client;

    public ModNetworkingImpl() {
        PayloadTypeRegistry.clientboundPlay().register(RaytracePacket.TYPE, RaytracePacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(BlockEntityEditingPacket.TYPE, BlockEntityEditingPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(EntityEditingPacket.TYPE, EntityEditingPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ItemStackEditingPacket.TYPE, ItemStackEditingPacket.CODEC);

        PayloadTypeRegistry.serverboundPlay().register(BlockEntityRaytraceResultPacket.TYPE, BlockEntityRaytraceResultPacket.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(EntityRaytraceResultPacket.TYPE, EntityRaytraceResultPacket.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ItemStackRaytraceResultPacket.TYPE, ItemStackRaytraceResultPacket.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(BlockEntityEditingPacket.TYPE, BlockEntityEditingPacket.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(EntityEditingPacket.TYPE, EntityEditingPacket.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ItemStackEditingPacket.TYPE, ItemStackEditingPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(BlockEntityRaytraceResultPacket.TYPE, this::serverHandle);
        ServerPlayNetworking.registerGlobalReceiver(EntityRaytraceResultPacket.TYPE, this::serverHandle);
        ServerPlayNetworking.registerGlobalReceiver(ItemStackRaytraceResultPacket.TYPE, this::serverHandle);
        ServerPlayNetworking.registerGlobalReceiver(BlockEntityEditingPacket.TYPE, this::serverHandle);
        ServerPlayNetworking.registerGlobalReceiver(EntityEditingPacket.TYPE, this::serverHandle);
        ServerPlayNetworking.registerGlobalReceiver(ItemStackEditingPacket.TYPE, this::serverHandle);
    }

    public void addClient() {
        client = new NBTEditNetworkingClient();
    }

    private void serverHandle(BlockEntityRaytraceResultPacket packet, ServerPlayNetworking.Context context) {
        context.player().server.execute(() -> NetworkServerHandler.handleBlockEntityResult(context.player(), packet));
    }

    private void serverHandle(EntityRaytraceResultPacket packet, ServerPlayNetworking.Context context) {
        context.player().server.execute(() -> NetworkServerHandler.handleEntityResult(context.player(), packet));
    }

    private void serverHandle(ItemStackRaytraceResultPacket packet, ServerPlayNetworking.Context context) {
        context.player().server.execute(() -> NetworkServerHandler.handleItemStackResult(context.player(), packet));
    }

    private void serverHandle(BlockEntityEditingPacket packet, ServerPlayNetworking.Context context) {
        context.player().server.execute(() -> NetworkServerHandler.saveBlockEntity(context.player(), packet));
    }

    private void serverHandle(EntityEditingPacket packet, ServerPlayNetworking.Context context) {
        context.player().server.execute(() -> NetworkServerHandler.saveEntity(context.player(), packet));
    }

    private void serverHandle(ItemStackEditingPacket packet, ServerPlayNetworking.Context context) {
        context.player().server.execute(() -> NetworkServerHandler.saveItemStack(context.player(), packet));
    }

    @Override
    public void sendTo(ServerPlayer player, CustomPacketPayload packet) {
        if (!ServerPlayNetworking.canSend(player, packet.type())) {
            player.sendSystemMessage(Component.translatableWithFallback(ModConstants.MESSAGE_MISSING_CLIENT_MOD, ModConstants.MESSAGE_MISSING_CLIENT_MOD_FALLBACK));
            return;
        }

        ServerPlayNetworking.send(player, packet);
    }

    @Override
    public void sendToServer(CustomPacketPayload packet) {
        if (client != null) {
            client.send(packet);
        }
    }
}
