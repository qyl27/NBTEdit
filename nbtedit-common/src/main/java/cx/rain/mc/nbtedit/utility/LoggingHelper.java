package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.editor.tag.TagParseHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import java.util.UUID;

public class LoggingHelper {
    private static Logger getLogger() {
        return NBTEdit.getInstance().getLogger();
    }

    private static boolean isDebug() {
        return NBTEditPlatform.getConfig().isDebug();
    }

    public static void errorParsingTag(Throwable ex, Tag tag) {
        getLogger().warn("Parse failed! Check tag structure.");
        if (isDebug()) {
            getLogger().debug("Runtime error while parsing tag.\nData: {}\nException: {}.",
                    TagParseHelper.getAsString(tag), new RuntimeException(ex));
        }
    }

    public static void debugEntityTag(UUID uuid, Tag tag) {
        if (isDebug()) {
            getLogger().debug("New NBT of Entity {} is {}.",
                    uuid, TagParseHelper.getAsString(tag));
        }
    }

    public static void debugBlockEntityTag(BlockPos pos, Tag tag) {
        if (isDebug()) {
            getLogger().debug("New NBT of BlockEntity at ({},{},{}) is {}.",
                    pos.getX(), pos.getY(), pos.getZ(), TagParseHelper.getAsString(tag));
        }
    }

    public static void debugItemStackTag(Player player, Tag tag) {
        if (isDebug()) {
            getLogger().debug("New NBT of ItemStack on {}'s hand is {}.",
                    player.getName(), TagParseHelper.getAsString(tag));
        }
    }
}
