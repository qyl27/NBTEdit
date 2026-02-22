package cx.rain.mc.nbtedit.fabric.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cx.rain.mc.nbtedit.api.command.ModPermission;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.permissions.PermissionLevel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class ModConfigImpl implements IModConfig {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected File configDir;
    protected File configFile;
    protected ConfigBean config = new ConfigBean();

    public ModConfigImpl(File gameDir) {
        configDir = new File(gameDir, "config");
        configFile = new File(configDir, "nbtedit.json");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        loadConfig();
    }

    private void loadConfig() {
        if (configFile.exists()) {
            try {
                config = GSON.fromJson(Files.readString(configFile.toPath()), ConfigBean.class);
            } catch (IOException ex) {
                log.error("Failed to load config, using default", ex);
            }
        }

        validateConfig();

        saveConfig();
    }

    private void validateConfig() {
        for (var p : ModPermission.values()) {
            var level = config.permissionsLevels.get(p.getNodeName());
            if (level == null) {
                config.permissionsLevels.put(p.getNodeName(), p.getDefaultLevel().id());
            } else if (level < 0 || level > 4) {
                log.warn("Permission level {} for {} is out of range (0 ~ 4), using default", level, p.getNodeName());
                config.permissionsLevels.put(p.getNodeName(), p.getDefaultLevel().id());
            }
        }
    }

    private void saveConfig() {
        try {
            var json = GSON.toJson(config);
            Files.writeString(configFile.toPath(), json);
        } catch (IOException ex) {
            log.error("Failed to save config", ex);
        }
    }

    @Override
    public boolean isDebug() {
        return config.debug;
    }

    @Override
    public PermissionLevel getOverriddenPermissionLevel(ModPermission permission) {
        var level = config.permissionsLevels.get(permission.getNodeName());
        return PermissionLevel.byId(level);
    }
}
