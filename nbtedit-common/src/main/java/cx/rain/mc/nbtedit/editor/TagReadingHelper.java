package cx.rain.mc.nbtedit.editor;

import cx.rain.mc.nbtedit.editor.tag.TagParseHelper;
import cx.rain.mc.nbtedit.utility.RegistryContextSerializeHelper;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Client only.
 */
public class TagReadingHelper {
    public static @Nullable ItemStack tryReadItem(Player player, @Nullable Tag tag) {
        if (tag instanceof CompoundTag compoundTag) {
            try {
                return RegistryContextSerializeHelper.deserializeItemStack(player.registryAccess(), compoundTag)
                        .orElse(null);
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    public static @Nullable UUID tryReadUuid(@Nullable Tag tag) {
        if (tag instanceof IntArrayTag intArrayTag) {
            try {
                return TagParseHelper.loadUuid(intArrayTag);
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    public static @Nullable Component tryReadText(Player player, @Nullable Tag tag) {
        if (tag instanceof StringTag stringTag) {
            return stringTag.asString()
                    .flatMap(s -> RegistryContextSerializeHelper.deserializeComponent(player.registryAccess(), s))
                    .orElse(null);
        }

        return null;
    }
}
