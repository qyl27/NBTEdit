package cx.rain.mc.nbtedit.utility.client;

import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.networking.packet.c2s.BlockEntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.EntityRaytraceResultPacket;
import cx.rain.mc.nbtedit.networking.packet.c2s.ItemStackRaytraceResultPacket;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

public class RayTraceHelper {
    public static void doRayTrace() {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        if (player == null) {
            return;
        }
        var partialTicks = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);
        var result = pick(player, player.blockInteractionRange(), player.entityInteractionRange(), partialTicks);

        if (result.getType() == HitResult.Type.BLOCK) {
            var block = (BlockHitResult) result;
            NBTEditPlatform.getNetworking().sendToServer(new BlockEntityRaytraceResultPacket(block.getBlockPos()));
        } else if (result.getType() == HitResult.Type.ENTITY) {
            var entity = ((EntityHitResult) result).getEntity();
            NBTEditPlatform.getNetworking().sendToServer(new EntityRaytraceResultPacket(entity.getUUID(), entity.getId()));
        } else if (!player.getMainHandItem().isEmpty()) {
            NBTEditPlatform.getNetworking().sendToServer(new ItemStackRaytraceResultPacket(player.getMainHandItem()));
        } else {
            player.displayClientMessage(Component
                    .translatable(ModConstants.MESSAGE_NOTHING_TO_EDIT)
                    .withStyle(ChatFormatting.RED), false);
        }
    }

    private static @NotNull HitResult pick(Entity entity, double blockInteractionRange, double entityInteractionRange, float partialTick) {
        double d = Math.max(blockInteractionRange, entityInteractionRange);
        double e = Mth.square(d);
        var eye = entity.getEyePosition(partialTick);
        var hitResult = entity.pick(d, partialTick, false);
        double f = hitResult.getLocation().distanceToSqr(eye);
        if (hitResult.getType() != HitResult.Type.MISS) {
            e = f;
            d = Math.sqrt(f);
        }

        var view = entity.getViewVector(partialTick);
        var eyeView = eye.add(view.x * d, view.y * d, view.z * d);
        var box = entity.getBoundingBox().expandTowards(view.scale(d)).inflate(1.0);
        var entityHitResult = ProjectileUtil.getEntityHitResult(entity, eye, eyeView, box, __ -> true, e);
        return entityHitResult != null && entityHitResult.getLocation().distanceToSqr(eye) < f
                ? filterHitResult(entityHitResult, eye, entityInteractionRange)
                : filterHitResult(hitResult, eye, blockInteractionRange);
    }

    private static HitResult filterHitResult(HitResult hitResult, Vec3 pos, double blockInteractionRange) {
        var hit = hitResult.getLocation();
        if (!hit.closerThan(pos, blockInteractionRange)) {
            var direction = Direction.getApproximateNearest(hit.x - pos.x, hit.y - pos.y, hit.z - pos.z);
            return BlockHitResult.miss(hit, direction, BlockPos.containing(hit));
        } else {
            return hitResult;
        }
    }
}
