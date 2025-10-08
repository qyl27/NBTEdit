package cx.rain.mc.nbtedit.utility;

import net.minecraft.util.ProblemReporter;

public class NBTEditProblemReporter {
    public static final ProblemReporter INSTANCE = new ProblemReporter.Collector(() -> "NBTEdit");
    public static final ProblemReporter TAG_SERIALIZE = INSTANCE.forChild(() -> "Tag Serialize");
    public static final ProblemReporter TAG_DESERIALIZE = INSTANCE.forChild(() -> "Tag Deserialize");
}
