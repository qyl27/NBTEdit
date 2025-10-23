package cx.rain.mc.nbtedit.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import java.util.Optional;

public class RegistryContextSerializeHelper {
    public static RegistryOps<Tag> getNbtRegistryOps(RegistryAccess registryAccess) {
        return registryAccess.createSerializationContext(NbtOps.INSTANCE);
    }

    public static RegistryOps<JsonElement> getJsonRegistryOps(RegistryAccess registryAccess) {
        return registryAccess.createSerializationContext(JsonOps.INSTANCE);
    }

    public static Optional<String> serializeComponent(RegistryAccess registryAccess, Component component) {
        return ComponentSerialization.CODEC.encodeStart(getJsonRegistryOps(registryAccess), component)
                .resultOrPartial()
                .map(JsonElement::toString);
    }

    public static Optional<Component> deserializeComponent(RegistryAccess registryAccess, String str) {
        try {
            var json = GsonHelper.parse(str);
            return ComponentSerialization.CODEC.parse(getJsonRegistryOps(registryAccess), json)
                    .resultOrPartial();
        } catch (JsonParseException ex) {
            return Optional.empty();
        }
    }

    public static Optional<CompoundTag> serializeItemStack(RegistryAccess registryAccess, ItemStack stack) {
        return ItemStack.CODEC.encodeStart(getNbtRegistryOps(registryAccess), stack)
                // Assert ItemStack always can be serialized to Tag
                .map(t -> (CompoundTag) t)
                .resultOrPartial();
    }

    public static Optional<ItemStack> deserializeItemStack(RegistryAccess registryAccess, CompoundTag tag) {
        return ItemStack.CODEC.parse(getNbtRegistryOps(registryAccess), tag)
                .resultOrPartial();
    }

    public static TagValueOutput createTagValueOutput(RegistryAccess registryAccess) {
        return TagValueOutput.createWithContext(NBTEditProblemReporter.TAG_SERIALIZE, registryAccess);
    }

    public static ValueInput createTagValueInput(RegistryAccess registryAccess, CompoundTag tag) {
        return TagValueInput.create(NBTEditProblemReporter.TAG_DESERIALIZE, registryAccess, tag);
    }
}
