package cx.rain.mc.nbtedit.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import cx.rain.mc.nbtedit.NBTEditClient;
import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.fabric.networking.ModNetworkingImpl;
import cx.rain.mc.nbtedit.utility.ModConstants;
import cx.rain.mc.nbtedit.utility.client.RayTraceHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class NBTEditFabricClient implements ClientModInitializer {
    private static final KeyMapping NBTEDIT_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        ModConstants.KEY_NBTEDIT_SHORTCUT,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_N,
        NBTEditClient.KEY_CATEGORY));

    @Override
    public void onInitializeClient() {
        ((ModNetworkingImpl) NBTEditPlatform.getNetworking()).addClient();

        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            while (NBTEDIT_KEY.consumeClick()) {
                RayTraceHelper.doRayTrace();
            }
        });
    }
}
