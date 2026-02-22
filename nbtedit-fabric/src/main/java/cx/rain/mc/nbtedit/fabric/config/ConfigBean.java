package cx.rain.mc.nbtedit.fabric.config;

import cx.rain.mc.nbtedit.api.command.ModPermission;

import java.util.HashMap;
import java.util.Map;

public class ConfigBean {
    public boolean debug = false;
    public Map<String, Integer> permissionsLevels = new HashMap<>();

    public ConfigBean() {
        for (var p : ModPermission.values()) {
            permissionsLevels.put(p.getNodeName(), p.getDefaultLevel().id());
        }
    }
}
