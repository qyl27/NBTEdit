package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.editor.tag.TagParseHelper;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

@Slf4j
public class LoggingHelper {
    private static boolean isDebug() {
        return NBTEditPlatform.getConfig().isDebug();
    }

    public static void errorParsingTag(Throwable ex, Tag tag) {
        log.warn("Parse failed! Check tag structure.");
        if (isDebug()) {
            log.debug("Runtime error while parsing tag.\nData: {}\nException: {}.",
                TagParseHelper.getAsString(tag), new RuntimeException(ex));
        }
    }

    public static void errorSerializingItemStack(ItemStack stack) {
        log.warn("Item stack couldn't be serialized. It must be a bug.");
        if (isDebug()) {
            log.debug("Item stack serialize failed, strange.\nItem: {}.",
                stack.toString());
        }
    }

    public static void debugEntityTag(UUID uuid, Tag tag) {
        if (isDebug()) {
            log.debug("New NBT of Entity {} is {}.",
                uuid, TagParseHelper.getAsString(tag));
        }
    }

    public static void debugBlockEntityTag(BlockPos pos, Tag tag) {
        if (isDebug()) {
            log.debug("New NBT of BlockEntity at ({},{},{}) is {}.",
                pos.getX(), pos.getY(), pos.getZ(), TagParseHelper.getAsString(tag));
        }
    }

    public static void debugItemStackTag(Player player, Tag tag) {
        if (isDebug()) {
            log.debug("New NBT of ItemStack on {}'s hand is {}.",
                player.getName(), TagParseHelper.getAsString(tag));
        }
    }
}
