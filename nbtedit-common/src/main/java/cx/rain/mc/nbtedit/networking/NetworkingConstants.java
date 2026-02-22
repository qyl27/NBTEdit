package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.utility.IdentifierHelper;
import net.minecraft.resources.Identifier;

public class NetworkingConstants {
    // Common
    public static final Identifier BLOCK_ENTITY_EDITING_ID = IdentifierHelper.modLoc("block_entity_editing");
    public static final Identifier ENTITY_EDITING_ID = IdentifierHelper.modLoc("entity_editing");
    public static final Identifier ITEM_STACK_EDITING_ID = IdentifierHelper.modLoc("item_stack_editing");

    // C2S
    public static final Identifier BLOCK_ENTITY_RAYTRACE_RESULT_ID = IdentifierHelper.modLoc("block_entity_raytrace_result");
    public static final Identifier ENTITY_RAYTRACE_RESULT_ID = IdentifierHelper.modLoc("entity_raytrace_result");
    public static final Identifier ITEM_STACK_RAYTRACE_RESULT_ID = IdentifierHelper.modLoc("item_stack_raytrace_result");

    // S2C
    public static final Identifier RAYTRACE_REQUEST_ID = IdentifierHelper.modLoc("raytrace_request");
}
