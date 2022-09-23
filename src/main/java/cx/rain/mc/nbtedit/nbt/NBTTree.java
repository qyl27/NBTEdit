package cx.rain.mc.nbtedit.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class NBTTree {
    private Node<CompoundTag> rootNode;  // qyl27: We believe the parent must be Compound.

    private NBTTree(Node<CompoundTag> root) {
        rootNode = root;
    }

    public static NBTTree root(CompoundTag tag) {
        return new NBTTree(Node.root(tag));
    }

    public CompoundTag toCompound() {
        var tag = new CompoundTag();
        toCompoundInternal(rootNode, tag);
        return tag;
    }

    public Node<CompoundTag> getRoot() {
        return rootNode;
    }

    private void toCompoundInternal(Node<?> node, CompoundTag tag) {
        for (var child : node.getChildren()) {
            String name = child.getName();
            Tag base = child.getTag();
            if (base instanceof CompoundTag compound) {
                toCompoundInternal(child, compound);
                tag.put(name, compound);
            } else if (base instanceof ListTag list) {
                toCompoundInternal(child, list);
                tag.put(name, list);
            } else {
                tag.put(name, base.copy());
            }
        }
    }

    public void toCompoundInternal(Node<?> node, ListTag list) {
        for (var child : node.getChildren()) {
            Tag childTag = child.getTag();
            if (childTag instanceof CompoundTag compound) {
                toCompoundInternal(child, compound);
                list.add(compound);
            } else if (childTag instanceof ListTag childListTag) {
                toCompoundInternal(child, childListTag);
                list.add(childListTag);
            } else {
                list.add(childTag.copy());
            }
        }
    }

    public static class Node<T extends Tag> {
        private String nbtName;
        private T nbtTag;

        private Node<?> parent = null;
        private List<Node<?>> children = new ArrayList<>();
        private boolean shouldShowChildren = false;

        private Node(T tag) {
            this("", tag);
        }

        private Node(String name, T tag) {
            this(name, tag, null);
        }

        private Node(String name, T tag, Node<?> parentTag) {
            nbtName = name;
            nbtTag = tag;
            parent = parentTag;

            walkThough(tag);
        }

        public String getName() {
            return nbtName;
        }

        public void setName(String name) {
            nbtName = name;
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

        protected Node<Tag> newChild(Tag tag) {
            return newChild("", tag);
        }

        public Node<Tag> newChild(String name, Tag tag) {
            var newChild = new Node<>(name, tag, this);
            children.add(newChild);
            return newChild;
        }

        public void addChild(Node<?> node) {
            children.add(node);
        }

        public void removeChild(int index) {
            children.remove(index);
        }

        public void removeChild(Node<?> node) {
            children.remove(node);
        }

        public boolean hasChild() {
            return children.size() > 0;
        }

        public List<Node<?>> getChildren() {
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
        }
    }
}
