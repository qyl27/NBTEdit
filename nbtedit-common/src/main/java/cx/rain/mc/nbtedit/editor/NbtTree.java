package cx.rain.mc.nbtedit.editor;

import com.google.common.base.Strings;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import cx.rain.mc.nbtedit.editor.tag.TagParseHelper;
import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.List;

public class NbtTree {
    private final Node<CompoundTag> rootNode;  // qyl27: We believe the parent must be Compound.

    private NbtTree(Node<CompoundTag> root) {
        rootNode = root;
    }

    public static NbtTree root(CompoundTag tag) {
        return new NbtTree(Node.root(tag));
    }

    public CompoundTag toCompound() {
        return compoundNodeToTag(rootNode);
    }

    public Node<CompoundTag> getRoot() {
        return rootNode;
    }

    private CompoundTag compoundNodeToTag(Node<?> node) {
        var tag = new CompoundTag();
        for (var child : node.getChildren()) {
            var name = child.getName();
            var childTag = child.getTag();

            if (childTag instanceof CompoundTag) {
                tag.put(name, compoundNodeToTag(child));
            } else if (childTag instanceof ListTag) {
                tag.put(name, listNodeToTag(child));
            } else {
                tag.put(name, childTag);
            }
        }
        return tag;
    }

    private ListTag listNodeToTag(Node<?> node) {
        var tag = new ListTag();
        for (var child : node.getChildren()) {
            var childTag = child.getTag();

            if (childTag instanceof CompoundTag) {
                tag.add(compoundNodeToTag(child));
            } else if (childTag instanceof ListTag) {
                tag.add(listNodeToTag(child));
            } else {
                tag.add(childTag);
            }
        }
        return tag;
    }

    @SuppressWarnings("unchecked")
    public static class Node<T extends Tag> {
        private String name;
        private T nbtTag;

        private Node<? extends Tag> parent = null;
        private final List<Node<Tag>> children = new ArrayList<>();
        private boolean shouldShowChildren = false;

        private Node(T tag) {
            this("", tag);
        }

        private Node(String name, T tag) {
            this(name, tag, null);
        }

        private Node(String name, T tag, Node<?> parentTag) {
            this.name = name;
            this.nbtTag = tag;
            this.parent = parentTag;

            walkThough(tag);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public T getTag() {
            return nbtTag;
        }

        public void setTag(Tag tag) {
            nbtTag = (T) tag;
        }

        private void walkThough(Tag tag) {
            if (tag instanceof CompoundTag compound) {
                for (var entry : compound.tags.entrySet()) {
                    newChild(entry.getKey(), entry.getValue());
                }
            }

            if (tag instanceof ListTag list) {
                for (var item : list.list) {
                    newChild(item);
                }
            }
        }

        public static <T extends Tag> Node<T> root(T tag) {
            return new Node<>(tag);
        }

        protected void newChild(Tag tag) {
            newChild("", tag);
        }

        public Node<Tag> newChild(String name, Tag tag) {
            var newChild = new Node<>(name, tag, this);
            children.add(newChild);
            return newChild;
        }

        public void addChild(Node<T> node) {
            children.add((Node<Tag>) node);
            node.setParent(this);
        }

        private void setParent(Node<T> parent) {
            this.parent = parent;
        }

        public void removeChild(int index) {
            children.remove(index);
        }

        public void removeChild(Node<?> node) {
            children.remove(node);
        }

        public boolean hasChild() {
            return !children.isEmpty();
        }

        public List<Node<Tag>> getChildren() {
            return children;
        }

        public boolean hasParent() {
            return parent != null;
        }

        public boolean isRoot() {
            return !hasParent();
        }

        public Node<?> getParent() {
            return parent;
        }

        public boolean shouldShowChildren() {
            return shouldShowChildren;
        }

        public void setShowChildren(boolean value) {
            shouldShowChildren = value;

            if (!value) {
                for (var c : getChildren()) {
                    c.setShowChildren(false);
                }
            }
        }

        public String getAsString() {
            String name = getName();
            Tag tag = getTag();

            if (tag instanceof CompoundTag) {
                return (Strings.isNullOrEmpty(name) ? "(CompoundTag)" : name) + ": ";
            }

            if (tag instanceof ListTag) {
                return (Strings.isNullOrEmpty(name) ? "(ListTag)" : name) + ": ";
            }

            String s = TagParseHelper.getValueAsString(tag);
            return Strings.isNullOrEmpty(name) ? s : name + ": " + s;
        }

        public static final String TAG_NAME = "name";
        public static final String TAG_TYPE = "type";
        public static final String TAG_LIST_TYPE = "elementType";
        public static final String TAG_VALUE = "value";

        public static Node<Tag> fromString(String data) {
            try {
                var tag = TagParser.parseCompoundFully(data);
                var nameOptional = tag.getString(TAG_NAME);
                var typeOptional = tag.getByte(TAG_TYPE);
                var value = tag.get(TAG_VALUE);

                if (nameOptional.isEmpty() || typeOptional.isEmpty() || value == null) {
                    return null;
                }

                var name = nameOptional.get();
                var type = typeOptional.get();
                var t = TagParseHelper.getAs(value, type);
                if (t == null) {
                    return null;
                }

                return new Node<>(name, t);
            } catch (CommandSyntaxException | IllegalStateException ignored) {
                return null;
            }
        }

        public String asString() {
            var tag = new CompoundTag();
            tag.putString(TAG_NAME, name);
            tag.put(TAG_VALUE, nbtTag);
            tag.putByte(TAG_TYPE, nbtTag.getId());

            if (nbtTag instanceof ListTag listTag) {
                tag.putByte(TAG_LIST_TYPE, TagParseHelper.getListElementType(listTag));
            }
            return TagParseHelper.getAsString(tag);
        }
    }
}
