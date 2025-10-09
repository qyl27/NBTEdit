package cx.rain.mc.nbtedit.gui.component;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IComposedComponent extends IComponent, ContainerEventHandler {
    void addChild(@NotNull IComponent child);

    void removeChild(@NotNull IComponent child);

    List<IComponent> getChildren();

    default void clearChildren() {
        for (var c : getChildren()) {
            c.setParent(null);
            removeChild(c);
        }
    }

    @Override
    default boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        return ContainerEventHandler.super.mouseClicked(event, isDoubleClick);
    }

    @Override
    default boolean mouseReleased(MouseButtonEvent event) {
        return ContainerEventHandler.super.mouseReleased(event);
    }

    @Override
    default boolean mouseDragged(MouseButtonEvent event, double mouseX, double mouseY) {
        return ContainerEventHandler.super.mouseDragged(event, mouseX, mouseY);
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return ContainerEventHandler.super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    default boolean keyPressed(KeyEvent event) {
        return ContainerEventHandler.super.keyPressed(event);
    }

    @Override
    default boolean keyReleased(KeyEvent event) {
        return ContainerEventHandler.super.keyReleased(event);
    }

    @Override
    default boolean charTyped(CharacterEvent event) {
        return ContainerEventHandler.super.charTyped(event);
    }
}
