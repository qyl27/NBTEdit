package cx.rain.mc.nbtedit.fabric.networking;

import cx.rain.mc.nbtedit.networking.NetworkClientHandler;
import cx.rain.mc.nbtedit.networking.packet.common.BlockEntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.EntityEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.common.ItemStackEditingPacket;
import cx.rain.mc.nbtedit.networking.packet.s2c.RaytracePacket;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class NBTEditNetworkingClient {
	public NBTEditNetworkingClient() {
		ClientPlayNetworking.registerGlobalReceiver(RaytracePacket.TYPE, this::clientHandle);
		ClientPlayNetworking.registerGlobalReceiver(BlockEntityEditingPacket.TYPE, this::clientHandle);
		ClientPlayNetworking.registerGlobalReceiver(EntityEditingPacket.TYPE, this::clientHandle);
		ClientPlayNetworking.registerGlobalReceiver(ItemStackEditingPacket.TYPE, this::clientHandle);
	}

	private void clientHandle(RaytracePacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> NetworkClientHandler.handleRaytrace(packet));
	}

	private void clientHandle(BlockEntityEditingPacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> NetworkClientHandler.handleBlockEntityEditing(packet));
	}

	private void clientHandle(EntityEditingPacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> NetworkClientHandler.handleEntityEditing(packet));
	}

	private void clientHandle(ItemStackEditingPacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> NetworkClientHandler.handleItemStackEditing(packet));
	}

	public void send(CustomPacketPayload packet) {
        if (!ClientPlayNetworking.canSend(packet.type())) {
            Minecraft.getInstance().getChatListener().handleSystemMessage(Component.translatableWithFallback(ModConstants.MESSAGE_MISSING_SERVER_MOD, ModConstants.MESSAGE_MISSING_SERVER_MOD_FALLBACK), false);
            return;
        }

		ClientPlayNetworking.send(packet);
	}
}
