package cx.rain.mc.nbtedit;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.SharedConstants;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Properties;

@Slf4j
public class NBTEdit {
    public static final String MODID = "nbtedit";
    public static final String NAME = "In-game NBTEdit Reborn";
    public static final String VERSION;
    public static final @Nullable OffsetDateTime BUILD_TIME;

    static {
        var properties = new Properties();
        var version = "";
        OffsetDateTime buildTime;
        try {
            properties.load(NBTEdit.class.getResourceAsStream("/build_info.properties"));
            version = properties.getProperty("build_version");
            buildTime = OffsetDateTime.parse(properties.getProperty("build_time"));
        } catch (IOException ex) {
            version = "Unknown";
            buildTime = null;
        }
        VERSION = version;
        BUILD_TIME = buildTime;
    }

    public static void init() {
        log.info("Loaded NBTEdit ver: {} on mc {}, build at {}",
            VERSION,
            SharedConstants.getCurrentVersion().name(),
            BUILD_TIME != null ? BUILD_TIME : "B.C. 3200");
    }
}
