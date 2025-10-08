package cx.rain.mc.nbtedit.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
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
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistryContextSerializer {

    private final RegistryOps<Tag> nbtRegistryOps;
    private final RegistryOps<JsonElement> jsonRegistryOps;
    private final Function<CompoundTag, ValueInput> tagValueInputFactory;
    private final Supplier<TagValueOutput> tagValueOutputFactory;

    public RegistryContextSerializer(RegistryAccess registryAccess) {
        nbtRegistryOps = registryAccess.createSerializationContext(NbtOps.INSTANCE);
        jsonRegistryOps = registryAccess.createSerializationContext(JsonOps.INSTANCE);
        tagValueInputFactory = (tag) -> TagValueInput.create(NBTEditProblemReporter.TAG_DESERIALIZE, registryAccess, tag);
        tagValueOutputFactory = () -> TagValueOutput.createWithContext(NBTEditProblemReporter.TAG_SERIALIZE, registryAccess);
    }


    public RegistryOps<Tag> getNbtRegistryOps() {
        return this.nbtRegistryOps;
    }

    public Optional<String> serializeComponent(Component component) {
        return ComponentSerialization.CODEC.encodeStart(jsonRegistryOps, component).resultOrPartial().map(JsonElement::toString);
    }

    public Optional<Component> deserializeComponent(String str) {
        var json = GsonHelper.parse(str);
        return ComponentSerialization.CODEC.parse(jsonRegistryOps, json).resultOrPartial();
    }

    public CompoundTag serializeItemStack(ItemStack stack) {
        return ItemStack.CODEC.encodeStart(getNbtRegistryOps(), stack)
                // Assert ItemStack always can be serialized to Tag
                .map(t -> (CompoundTag) t)
                .getOrThrow();
    }

    public ItemStack deserializeItemStack(CompoundTag tag) {
        return ItemStack.CODEC.parse(getNbtRegistryOps(), tag)
                .getOrThrow();
    }

    public TagValueOutput createTagValueOutput() {
        return tagValueOutputFactory.get();
    }

    public ValueInput createTagValueInput(CompoundTag tag) {
        return tagValueInputFactory.apply(tag);
    }
}
