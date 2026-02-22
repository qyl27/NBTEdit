package cx.rain.mc.nbtedit.editor;

import cx.rain.mc.nbtedit.utility.IdentifierHelper;
import cx.rain.mc.nbtedit.utility.ModConstants;
import lombok.Getter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Getter
public enum EditorButton {
    BYTE(0, ModConstants.NBT_TYPE_BYTE, IdentifierHelper.modLoc("tag_type/byte")),
    SHORT(1, ModConstants.NBT_TYPE_SHORT, IdentifierHelper.modLoc("tag_type/short")),
    INT(2, ModConstants.NBT_TYPE_INT, IdentifierHelper.modLoc("tag_type/int")),
    LONG(3, ModConstants.NBT_TYPE_LONG, IdentifierHelper.modLoc("tag_type/long")),
    FLOAT(4, ModConstants.NBT_TYPE_FLOAT, IdentifierHelper.modLoc("tag_type/float")),
    DOUBLE(5, ModConstants.NBT_TYPE_DOUBLE, IdentifierHelper.modLoc("tag_type/double")),
    BYTE_ARRAY(6, ModConstants.NBT_TYPE_BYTE_ARRAY, IdentifierHelper.modLoc("tag_type/byte_array")),
    STRING(7, ModConstants.NBT_TYPE_STRING, IdentifierHelper.modLoc("tag_type/string")),
    LIST(8, ModConstants.NBT_TYPE_LIST, IdentifierHelper.modLoc("tag_type/list")),
    COMPOUND(9, ModConstants.NBT_TYPE_COMPOUND, IdentifierHelper.modLoc("tag_type/compound")),
    INT_ARRAY(10, ModConstants.NBT_TYPE_INT_ARRAY, IdentifierHelper.modLoc("tag_type/int_array")),
    LONG_ARRAY(11, ModConstants.NBT_TYPE_LONG_ARRAY, IdentifierHelper.modLoc("tag_type/long_array")),
    EDIT(12, ModConstants.GUI_BUTTON_EDIT, IdentifierHelper.modLoc("editor/edit")),
    DELETE(13, ModConstants.GUI_BUTTON_DELETE, IdentifierHelper.modLoc("editor/delete")),
    PASTE(14, ModConstants.GUI_BUTTON_PASTE, IdentifierHelper.modLoc("editor/paste")),
    CUT(15, ModConstants.GUI_BUTTON_CUT, IdentifierHelper.modLoc("editor/cut")),
    COPY(16, ModConstants.GUI_BUTTON_COPY, IdentifierHelper.modLoc("editor/copy")),
    ;

    private final int id;
    private final Component name;
    private final Identifier sprite;

    EditorButton(int id, String name, Identifier sprite) {
        this.id = id;
        this.name = Component.translatable(name);
        this.sprite = sprite;
    }

    public static EditorButton of(int id) {
        for (var v : values()) {
            if (v.getId() == id) {
                return v;
            }
        }

        return BYTE;
    }

    public static EditorButton ofTag(Tag t) {
        return switch (t.getId()) {
            case 1 -> BYTE;
            case 2 -> SHORT;
            case 3 -> INT;
            case 4 -> LONG;
            case 5 -> FLOAT;
            case 6 -> DOUBLE;
            case 7 -> BYTE_ARRAY;
            case 8 -> STRING;
            case 9 -> LIST;
            case 10 -> COMPOUND;
            case 11 -> INT_ARRAY;
            case 12 -> LONG_ARRAY;
            default -> throw new IllegalStateException("Unexpected value: " + t.getId());
        };
    }
}
