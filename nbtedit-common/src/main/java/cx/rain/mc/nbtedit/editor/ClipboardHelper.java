package cx.rain.mc.nbtedit.editor;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

public class ClipboardHelper {
    public static void setNode(NbtTree.Node<?> node) {
        var data = node.asString();
        Minecraft.getInstance().keyboardHandler.setClipboard(data);
    }

    public static @Nullable NbtTree.Node<?> getNode() {
        var data = Minecraft.getInstance().keyboardHandler.getClipboard();
        return NbtTree.Node.fromString(data);
    }
}
