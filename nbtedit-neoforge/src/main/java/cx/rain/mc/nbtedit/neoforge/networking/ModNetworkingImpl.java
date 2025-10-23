package cx.rain.mc.nbtedit.neoforge.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.networking.NetworkClientHandler;
import cx.rain.mc.nbtedit.networking.NetworkServerHandler;
import cx.rain.mc.nbtedit.networking.packet.c2s.BlockEntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.EntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.ItemStackRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.s2c.RaytracePacket;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = NBTEdit.MODID)
public class ModNetworkingImpl implements IModNetworking {

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		var registrar = event.registrar(NBTEdit.VERSION).optional();

		registrar.playToClient(RaytracePacket.TYPE, RaytracePacket.CODEC, ModNetworkingImpl::clientHandle);

		registrar.playToServer(BlockEntityRaytraceResultPacket.TYPE, BlockEntityRaytraceResultPacket.CODEC, ModNetworkingImpl::serverHandle);
		registrar.playToServer(EntityRaytraceResultPacket.TYPE, EntityRaytraceResultPacket.CODEC, ModNetworkingImpl::serverHandle);
		registrar.playToServer(ItemStackRaytraceResultPacket.TYPE, ItemStackRaytraceResultPacket.CODEC, ModNetworkingImpl::serverHandle);

		registrar.playBidirectional(BlockEntityEditingPacket.TYPE, BlockEntityEditingPacket.CODEC, ModNetworkingImpl::serverHandle, ModNetworkingImpl::clientHandle);
		registrar.playBidirectional(EntityEditingPacket.TYPE, EntityEditingPacket.CODEC, ModNetworkingImpl::serverHandle, ModNetworkingImpl::clientHandle);
		registrar.playBidirectional(ItemStackEditingPacket.TYPE, ItemStackEditingPacket.CODEC, ModNetworkingImpl::serverHandle, ModNetworkingImpl::clientHandle);
	}

	private static void clientHandle(RaytracePacket packet, IPayloadContext context) {
		context.enqueueWork(() -> NetworkClientHandler.handleRaytrace(packet));
	}

    private static void clientHandle(BlockEntityEditingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> NetworkClientHandler.handleBlockEntityEditing(packet));
    }

    private static void clientHandle(EntityEditingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> NetworkClientHandler.handleEntityEditing(packet));
    }

    private static void clientHandle(ItemStackEditingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> NetworkClientHandler.handleItemStackEditing(packet));
    }

	private static void serverHandle(BlockEntityRaytraceResultPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			var player = context.player();
			if (player instanceof ServerPlayer serverPlayer) {
				NetworkServerHandler.handleBlockEntityResult(serverPlayer, packet);
			}
		});
	}

	private static void serverHandle(EntityRaytraceResultPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			var player = context.player();
			if (player instanceof ServerPlayer serverPlayer) {
				NetworkServerHandler.handleEntityResult(serverPlayer, packet);
			}
		});
	}

	private static void serverHandle(ItemStackRaytraceResultPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			var player = context.player();
			if (player instanceof ServerPlayer serverPlayer) {
				NetworkServerHandler.handleItemStackResult(serverPlayer, packet);
			}
		});
	}

	private static void serverHandle(BlockEntityEditingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                NetworkServerHandler.saveBlockEntity(serverPlayer, packet);
            }
        });
	}

	private static void serverHandle(EntityEditingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                NetworkServerHandler.saveEntity(serverPlayer, packet);
            }
        });
	}

	private static void serverHandle(ItemStackEditingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                NetworkServerHandler.saveItemStack(serverPlayer, packet);
            }
        });
	}

	public ModNetworkingImpl() {
	}

	@Override
	public void sendTo(ServerPlayer player, CustomPacketPayload packet) {
        if (!player.connection.hasChannel(packet)) {
            player.sendSystemMessage(Component.translatableWithFallback(ModConstants.MESSAGE_MISSING_CLIENT_MOD, ModConstants.MESSAGE_MISSING_CLIENT_MOD_FALLBACK));
            return;
        }
		player.connection.send(packet);
	}

	@Override
	public void sendToServer(CustomPacketPayload packet) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
            if (!connection.hasChannel(packet)) {
                Minecraft.getInstance().getChatListener().handleSystemMessage(Component.translatableWithFallback(ModConstants.MESSAGE_MISSING_SERVER_MOD, ModConstants.MESSAGE_MISSING_SERVER_MOD_FALLBACK), false);
                return;
            }
			connection.send(packet);
		}
	}
}
