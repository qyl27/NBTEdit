package cx.rain.mc.nbtedit.utility;

import cx.rain.mc.nbtedit.NBTEdit;
import net.minecraft.resources.Identifier;

public class IdentifierHelper {
    public static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(NBTEdit.MODID, path);
    }
}
