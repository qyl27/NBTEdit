package cx.rain.mc.nbtedit.api.command;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.IdentifierHelper;
import lombok.Getter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

@Getter
public enum ModPermission {
    USE(PermissionLevel.GAMEMASTERS, "use"),
    READ_ONLY(PermissionLevel.MODERATORS, "read_only"),
    EDIT_ON_PLAYER(PermissionLevel.OWNERS, "edit_on_player"),
    ;

    private final PermissionLevel defaultLevel;
    private final Identifier id;
    private final String nodeName;
    private final String fullName;
    private final Permission.Atom atomPermission;

    ModPermission(PermissionLevel defaultLevel, String nodeName) {
        this.defaultLevel = defaultLevel;
        this.id = IdentifierHelper.modLoc(nodeName);
        this.nodeName = nodeName;
        this.fullName = NBTEdit.MODID + "." + nodeName;
        this.atomPermission = new Permission.Atom(this.id);
    }
}
