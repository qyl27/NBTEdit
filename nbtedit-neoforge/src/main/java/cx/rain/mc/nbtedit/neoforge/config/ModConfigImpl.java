package cx.rain.mc.nbtedit.neoforge.config;

import cx.rain.mc.nbtedit.api.command.ModPermission;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import net.minecraft.server.permissions.PermissionLevel;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class ModConfigImpl implements IModConfig {
    public static ModConfigSpec CONFIG;

    public static ModConfigSpec.BooleanValue DEBUG;

    public static Map<ModPermission, ModConfigSpec.ConfigValue<Integer>> PERMISSION_LEVELS = new HashMap<>();

    static {
        var builder = new ModConfigSpec.Builder();

        builder.comment("General settings.")
                .push("general");

        DEBUG = builder
                .comment("Enable debug logs. Necessary if you are reporting bugs.")
                .define("debug", false);

        builder.comment("Override the default permission levels. Like vanilla, should in 0 ~ 4 range.")
                .push("permission");

        for (var p : ModPermission.values()) {
            var spec = builder.defineInRange(p.getNodeName(), p.getDefaultLevel().id(), 0, 4);
            PERMISSION_LEVELS.put(p, spec);
        }

        builder.pop();
        builder.pop();

        CONFIG = builder.build();
    }

    @Override
    public boolean isDebug() {
        return DEBUG.get();
    }

    @Override
    public PermissionLevel getOverriddenPermissionLevel(ModPermission permission) {
        var level = PERMISSION_LEVELS.get(permission).get();
        return PermissionLevel.byId(level);
    }
}
