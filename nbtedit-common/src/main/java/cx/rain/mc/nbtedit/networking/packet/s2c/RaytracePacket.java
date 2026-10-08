package cx.rain.mc.nbtedit.networking.packet.s2c;

import cx.rain.mc.nbtedit.networking.NetworkingConstants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RaytracePacket() implements CustomPacketPayload {
    public static final Type<RaytracePacket> TYPE = new Type<>(NetworkingConstants.RAYTRACE_REQUEST_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, RaytracePacket> CODEC = StreamCodec.of(
            (_, _) -> {}, _ -> new RaytracePacket()
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
