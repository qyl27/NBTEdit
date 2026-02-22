package cx.rain.mc.nbtedit.editor.tag;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TagParseHelper {
    /**
     * Get {@link TagType} from numeric tag type.
     *
     * @param type Byte tag type in {@link Tag}.
     * @param <C>  Tag type.
     * @return Tag type.
     */
    @SuppressWarnings("unchecked")
    public static <C extends Tag> TagType<C> getTagType(byte type) {
        return (TagType<C>) switch (type) {
            case 0 -> EndTag.TYPE;
            case 1 -> ByteTag.TYPE;
            case 2 -> ShortTag.TYPE;
            case 3 -> IntTag.TYPE;
            case 4 -> LongTag.TYPE;
            case 5 -> FloatTag.TYPE;
            case 6 -> DoubleTag.TYPE;
            case 7 -> ByteArrayTag.TYPE;
            case 8 -> StringTag.TYPE;
            case 9 -> ListTag.TYPE;
            case 10 -> CompoundTag.TYPE;
            case 11 -> IntArrayTag.TYPE;
            case 12 -> LongArrayTag.TYPE;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    /**
     * Parse tag to specific type.
     *
     * @param tag  Tag to parse.
     * @param type Tag type in {@link Tag}.
     * @param <C>  Tag type from type.
     * @return Got tag.
     */
    @SuppressWarnings("unchecked")
    public static <C extends Tag> @Nullable C getAs(@Nullable Tag tag, byte type) {
        if (tag == null) {
            return null;
        }

        return switch (type) {
            case 1 -> (C) tag.asByte().map(ByteTag::valueOf).orElse(null);
            case 2 -> (C) tag.asShort().map(ShortTag::valueOf).orElse(null);
            case 3 -> (C) tag.asInt().map(IntTag::valueOf).orElse(null);
            case 4 -> (C) tag.asLong().map(LongTag::valueOf).orElse(null);
            case 5 -> (C) tag.asFloat().map(FloatTag::valueOf).orElse(null);
            case 6 -> (C) tag.asDouble().map(DoubleTag::valueOf).orElse(null);
            case 7 -> (C) tag.asByteArray().map(ByteArrayTag::new).orElse(null);
            case 8 -> (C) tag.asString().map(StringTag::valueOf).orElse(null);
            case 9 -> (C) tag.asList().orElse(null);
            case 10 -> (C) tag.asCompound().orElse(null);
            case 11 -> (C) tag.asIntArray().map(IntArrayTag::new).orElse(null);
            case 12 -> (C) tag.asLongArray().map(LongArrayTag::new).orElse(null);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public static byte getListElementType(@NotNull ListTag list) {
        byte type = 0;

        for (var e : list) {
            byte c = e.getId();
            if (type == 0) {
                type = c;
            } else if (type != c) {
                return Tag.TAG_COMPOUND;
            }
        }

        return type;
    }

    public static String getAsString(@NotNull Tag tag) {
        return tag.toString();
    }

    public static String getValueAsString(@NotNull Tag tag) {
        switch (tag) {
            case ByteTag b -> {
                return Byte.toString(b.byteValue());
            }
            case ShortTag s -> {
                return Short.toString(s.shortValue());
            }
            case IntTag i -> {
                return Integer.toString(i.intValue());
            }
            case LongTag l -> {
                return Long.toString(l.longValue());
            }
            case FloatTag f -> {
                return Float.toString(f.floatValue());
            }
            case DoubleTag d -> {
                return Double.toString(d.doubleValue());
            }
            case StringTag s -> {
                return s.value();
            }
            case ByteArrayTag ba -> {
                var s = new StringBuilder();
                for (var b : ba.getAsByteArray()) {
                    s.append(b).append(", ");
                }
                return s.toString();
            }
            case IntArrayTag ia -> {
                var s = new StringBuilder();
                for (var i : ia.getAsIntArray()) {
                    s.append(i).append(", ");
                }
                return s.toString();
            }
            case LongArrayTag la -> {
                var s = new StringBuilder();
                for (var l : la.getAsLongArray()) {
                    s.append(l).append(", ");
                }
                return s.toString();
            }
            default -> {
                // List, Compound or End, returns empty.
                return "";
            }
        }
    }

    public static void putUuid(@NotNull CompoundTag tag, @NotNull String name, @NotNull UUID uuid) {
        tag.store(name, UUIDUtil.CODEC, uuid);
    }

    public static @Nullable UUID getUuid(@NotNull CompoundTag tag, @NotNull String name) {
        return tag.read(name, UUIDUtil.CODEC).orElse(null);
    }

    public static @Nullable UUID loadUuid(@NotNull Tag tag) {
        return UUIDUtil.CODEC.parse(NbtOps.INSTANCE, tag).mapOrElse(u -> u, e -> null);
    }

    public static @Nullable Tag createUuid(@NotNull UUID uuid) {
        return UUIDUtil.CODEC.encodeStart(NbtOps.INSTANCE, uuid).mapOrElse(i -> i, e -> null);
    }
}
