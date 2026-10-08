package cx.rain.mc.nbtedit.fabric;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.command.NBTEditCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class NBTEditFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, _, _) ->
            dispatcher.register(NBTEditCommand.NBTEDIT)));

        NBTEdit.init();
    }
}
