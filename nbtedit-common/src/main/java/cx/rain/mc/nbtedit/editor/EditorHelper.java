package cx.rain.mc.nbtedit.editor;

import net.minecraft.nbt.*;
import org.jetbrains.annotations.Nullable;

public class EditorHelper {
    public static @Nullable Tag newTag(int buttonId) {
        return switch (buttonId) {
            case 0 -> ByteTag.valueOf((byte) 0);
            case 1 -> ShortTag.valueOf((short) 0);
            case 2 -> IntTag.valueOf(0);
            case 3 -> LongTag.valueOf(0);
            case 4 -> FloatTag.valueOf(0.0f);
            case 5 -> DoubleTag.valueOf(0.0);
            case 6 -> new ByteArrayTag(new byte[0]);
            case 7 -> StringTag.valueOf("");
            case 8 -> new ListTag();
            case 9 -> new CompoundTag();
            case 10 -> new IntArrayTag(new int[0]);
            case 11 -> new LongArrayTag(new long[0]);
            default -> null;
        };
    }

    public static String newTagName(int buttonId, NbtTree.Node<?> parent) {
        return newName(NbtType.ofButtonId(buttonId).getTagName(), parent);
    }

    public static String newName(String name, NbtTree.Node<?> parent) {
        var result = name;
        int i = 1;
        while (!isUniqueNameInNode(result, parent)) {
            result = name + " " + i;
            i += 1;
        }
        return result;
    }

    public static boolean isUniqueNameInNode(String name, NbtTree.Node<?> parent) {
        for (var node : parent.getChildren()) {
            if (node.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }
}
